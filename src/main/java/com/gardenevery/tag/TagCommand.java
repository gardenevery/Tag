package com.gardenevery.tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import com.github.bsideup.jabel.Desugar;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class TagCommand extends CommandBase {

    public final CommandManager commandManager;
    public static final Level LEVEL0 = Level.ALL;
    public static final Level LEVEL1 = Level.PLAYER;
    public static final Level LEVEL2 = Level.MODERATOR;
    public static final Level LEVEL3 = Level.OPERATOR;
    public static final Level LEVEL4 = Level.ADMIN;

    public TagCommand() {
        this.commandManager = new CommandManager();
        registerCommands();
    }

    public enum Level {
        ALL(0),
        PLAYER(1),
        MODERATOR(2),
        OPERATOR(3),
        ADMIN(4);

        private final int level;

        Level(int level) {
            this.level = level;
        }

        public boolean hasPermission(ICommandSender sender, String commandName) {
            return sender.canUseCommand(level, commandName);
        }
    }

    @Desugar
    public record CommandHandler(String name, Level permission, BiConsumer<MinecraftServer, ICommandSender> executor, String descriptionKey) {}

    public class CommandManager {
        private final Map<String, CommandHandler> commands = new HashMap<>();
        private final List<String> subCommandNames = new ArrayList<>();

        public void registerCommand(CommandHandler handler) {
            commands.put(handler.name().toLowerCase(), handler);
            subCommandNames.add(handler.name());
        }

        public Optional<CommandHandler> getHandler(String command) {
            return Optional.ofNullable(commands.get(command.toLowerCase()));
        }

        public List<String> getSubCommandNames() {
            return Collections.unmodifiableList(subCommandNames);
        }

        public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
            if (args.length == 0) {
                showHelp(sender);
                return;
            }

            var subCommand = args[0].toLowerCase();
            Optional<CommandHandler> handlerOptional = getHandler(subCommand);

            if (handlerOptional.isPresent()) {
                executeHandler(server, sender, handlerOptional.get());
            } else {
                showHelp(sender);
            }
        }

        private void executeHandler(MinecraftServer server, ICommandSender sender, CommandHandler handler) {
            if (!handler.permission().hasPermission(sender, "tag")) {
                sender.sendMessage(new TextComponentTranslation(TranslationKeys.NO_PERMISSION));
                return;
            }
            handler.executor().accept(server, sender);
        }

        public List<String> getTabCompletions(String[] args) {
            return args.length == 1 ? getListOfStringsMatchingLastWord(args, getSubCommandNames()) : Collections.emptyList();
        }
    }

    private void registerCommands() {
        commandManager.registerCommand(new CommandHandler("hand", LEVEL0,
                (server, sender) -> showHeldItemTags(sender), TranslationKeys.HELP_HAND));

        commandManager.registerCommand(new CommandHandler("info", LEVEL2,
                (server, sender) -> showTagStatistics(sender), TranslationKeys.HELP_INFO));
    }

    private static final class TranslationKeys {
        static final String HELP_TITLE = "com.gardenevery.tag.help.title";
        static final String HELP_HAND = "com.gardenevery.tag.help.hand";
        static final String HELP_INFO = "com.gardenevery.tag.help.info";
        static final String NO_ITEM = "com.gardenevery.tag.noitem";
        static final String MAIN_HAND = "com.gardenevery.tag.mainhand";
        static final String OFF_HAND = "com.gardenevery.tag.offhand";
        static final String FLUID_TAGS = "com.gardenevery.tag.fluidtags";
        static final String NO_FLUID_TAGS = "com.gardenevery.tag.nofluidtags";
        static final String EMPTY_CONTAINER = "com.gardenevery.tag.emptycontainer";
        static final String ITEM_TAGS = "com.gardenevery.tag.itemtags";
        static final String NO_ITEM_TAGS = "com.gardenevery.tag.noitemtags";
        static final String NO_PERMISSION = "com.gardenevery.tag.nopermission";
        static final String STATISTICS_TITLE = "com.gardenevery.tag.statistics.title";
        static final String STATISTICS_ITEMS = "com.gardenevery.tag.statistics.items";
        static final String STATISTICS_FLUIDS = "com.gardenevery.tag.statistics.fluids";
        static final String STATISTICS_BLOCKS = "com.gardenevery.tag.statistics.blocks";
        static final String STATISTICS_TOTAL = "com.gardenevery.tag.statistics.total";
    }

    @Nonnull
    @Override
    public String getName() {
        return "tag";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "tag [hand|info]";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        commandManager.executeCommand(server, sender, args);
    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args,
                                          BlockPos targetPos) {
        return commandManager.getTabCompletions(args);
    }

    private void showHelp(@Nonnull ICommandSender sender) {
        sender.sendMessage(new TextComponentTranslation(TranslationKeys.HELP_TITLE));

        commandManager.getSubCommandNames().stream()
                .map(commandManager::getHandler)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(handler -> handler.permission().hasPermission(sender, "tag"))
                .forEach(handler -> sender.sendMessage(new TextComponentTranslation(handler.descriptionKey())));
    }

    private void showHeldItemTags(@Nonnull ICommandSender sender) {
        if (!(sender instanceof EntityPlayer player)) {
            return;
        }

        boolean hasItems = Stream.of(EnumHand.values()).map(player::getHeldItem).anyMatch(stack -> !stack.isEmpty());
        if (!hasItems) {
            player.sendMessage(new TextComponentTranslation(TranslationKeys.NO_ITEM));
            return;
        }

        for (var hand : EnumHand.values()) {
            var stack = player.getHeldItem(hand);
            if (!stack.isEmpty()) {
                processItemStack(player, hand, stack);
            }
        }
    }

    private void processItemStack(@Nonnull EntityPlayer player, EnumHand hand, @Nonnull ItemStack stack) {
        var handName = getHandDisplayName(hand);
        var fluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);

        if (fluidHandler != null) {
            handleFluidContainer(player, handName, fluidHandler);
        } else {
            handleRegularItem(player, handName, stack);
        }
    }

    private String getHandDisplayName(EnumHand hand) {
        return hand == EnumHand.MAIN_HAND ? new TextComponentTranslation(TranslationKeys.MAIN_HAND).getUnformattedText()
                : new TextComponentTranslation(TranslationKeys.OFF_HAND).getUnformattedText();
    }

    private void handleFluidContainer(@Nonnull EntityPlayer player, String handName, IFluidHandlerItem fluidHandler) {
        var fluid = fluidHandler.drain(Integer.MAX_VALUE, false);

        if (fluid != null && fluid.amount > 0) {
            var fluidTags = TagHelper.tags(fluid);
            var translationKey = !fluidTags.isEmpty() ? TranslationKeys.FLUID_TAGS : TranslationKeys.NO_FLUID_TAGS;
            sendFormattedMessage(player, handName, translationKey, String.join(", ", fluidTags));
        } else {
            sendFormattedMessage(player, handName, TranslationKeys.EMPTY_CONTAINER, "");
        }
    }

    private void handleRegularItem(@Nonnull EntityPlayer player, String handName, @Nonnull ItemStack stack) {
        var itemTags = TagHelper.tags(stack);
        var translationKey = !itemTags.isEmpty() ? TranslationKeys.ITEM_TAGS : TranslationKeys.NO_ITEM_TAGS;
        sendFormattedMessage(player, handName, translationKey, String.join(", ", itemTags));
    }

    private void sendFormattedMessage(@Nonnull EntityPlayer player, String handName,
                                      String translationKey, String tags) {
        var translatedPart = new TextComponentTranslation(translationKey, tags).getUnformattedText();
        var message = String.format("%s %s", handName, translatedPart).trim();
        player.sendMessage(new TextComponentString(message));
    }

    private void showTagStatistics(@Nonnull ICommandSender sender) {
        sender.sendMessage(new TextComponentTranslation(TranslationKeys.STATISTICS_TITLE));
        displayStatisticsForType(sender, TranslationKeys.STATISTICS_ITEMS, TagType.ITEM);
        displayStatisticsForType(sender, TranslationKeys.STATISTICS_FLUIDS, TagType.FLUID);
        displayStatisticsForType(sender, TranslationKeys.STATISTICS_BLOCKS, TagType.BLOCK);
        displayTotalStatistics(sender);
    }

    private void displayStatisticsForType(@Nonnull ICommandSender sender, String translationKey, TagType type) {
        TagStatistics stats = createTagStatistics(type);
        sendStatisticMessage(sender, translationKey, stats);
    }

    private void displayTotalStatistics(@Nonnull ICommandSender sender) {
        var totalStats = new TagStatistics(TagHelper.tagCount(), TagHelper.associations(), TagHelper.keyCount());
        sendStatisticMessage(sender, TranslationKeys.STATISTICS_TOTAL, totalStats);
    }

    private TagStatistics createTagStatistics(TagType type) {
        return new TagStatistics(TagHelper.tagCount(type), TagHelper.totalAssociations(type), TagHelper.uniqueKeyCount(type));
    }

    private void sendStatisticMessage(@Nonnull ICommandSender sender, String key, TagStatistics stats) {
        sender.sendMessage(new TextComponentTranslation(key, stats.tagCount(), stats.elementCount(), stats.uniqueElementCount()));
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender) {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Desugar
    private record TagStatistics(int tagCount, int elementCount, int uniqueElementCount) {}
}
