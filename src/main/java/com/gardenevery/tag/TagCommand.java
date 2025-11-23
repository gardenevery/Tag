package com.gardenevery.tag;

import java.util.List;
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
        if (args.length == 0) {
            showHelp(sender);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "hand" -> showHeldItemTags(sender);
            case "info" -> showTagStatistics(sender);
            default -> showHelp(sender);
        }
    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args, BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "hand", "info")
                : super.getTabCompletions(server, sender, args, targetPos);
    }

    private void showHelp(@Nonnull ICommandSender sender) {
        sender.sendMessage(new TextComponentTranslation("com.gardenevery.tag.help.title"));
        sender.sendMessage(new TextComponentTranslation("com.gardenevery.tag.help.hand"));

        if (sender.canUseCommand(2, this.getName())) {
            sender.sendMessage(new TextComponentTranslation("com.gardenevery.tag.help.info"));
        }
    }

    private void showHeldItemTags(@Nonnull ICommandSender sender) {
        if (!(sender instanceof EntityPlayer player)){
            return;
        }

        for (var hand : EnumHand.values()) {
            var stack = player.getHeldItem(hand);
            if (!stack.isEmpty()) {
                sendItemTagsMessage(player, hand, stack);
            }
        }

        if (player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && player.getHeldItem(EnumHand.OFF_HAND).isEmpty()) {
            player.sendMessage(new TextComponentTranslation("com.gardenevery.tag.noitem"));
        }
    }

    private void sendItemTagsMessage(@Nonnull EntityPlayer player, EnumHand hand, @Nonnull ItemStack stack) {
        String handName = hand == EnumHand.MAIN_HAND
                ? new TextComponentTranslation("com.gardenevery.tag.mainhand").getUnformattedText()
                : new TextComponentTranslation("com.gardenevery.tag.offhand").getUnformattedText();

        var fluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
        if (fluidHandler != null) {
            handleFluidTags(player, handName, fluidHandler);
        } else {
            handleItemTags(player, handName, stack);
        }
    }

    private void handleFluidTags(@Nonnull EntityPlayer player, String handName, IFluidHandlerItem fluidHandler) {
        var fluid = fluidHandler.drain(Integer.MAX_VALUE, false);
        if (fluid != null && fluid.amount > 0) {
            var fluidTags = TagHelper.tags(fluid);
            sendFormattedMessage(player, handName, !fluidTags.isEmpty() ? "com.gardenevery.tag.fluidtags" :
                    "com.gardenevery.tag.nofluidtags", String.join(", ", fluidTags));
        } else {
            sendFormattedMessage(player, handName, "com.gardenevery.tag.emptycontainer", "");
        }
    }

    private void handleItemTags(@Nonnull EntityPlayer player, String handName, @Nonnull ItemStack stack) {
        var itemTags = TagHelper.tags(stack);
        sendFormattedMessage(player, handName, !itemTags.isEmpty() ? "com.gardenevery.tag.itemtags"
                : "com.gardenevery.tag.noitemtags", String.join(", ", itemTags));
    }

    private void sendFormattedMessage(@Nonnull EntityPlayer player, String handName, String translationKey, String tags) {
        var message = String.format("%s %s", handName, new TextComponentTranslation(translationKey, tags).getUnformattedText()).trim();
        player.sendMessage(new TextComponentString(message));
    }

    @Desugar
    private record TagStatistics(int tagCount, int elementCount, int uniqueElementCount) {}

    private void showTagStatistics(@Nonnull ICommandSender sender) {
        if (!sender.canUseCommand(2, this.getName())) {
            sender.sendMessage(new TextComponentTranslation("com.gardenevery.tag.nopermission"));
            return;
        }

        sender.sendMessage(new TextComponentTranslation("com.gardenevery.tag.statistics.title"));
        sendStatisticMessage(sender, "com.gardenevery.tag.statistics.items", createTagStatistics(TagType.ITEM));
        sendStatisticMessage(sender, "com.gardenevery.tag.statistics.fluids", createTagStatistics(TagType.FLUID));
        sendStatisticMessage(sender, "com.gardenevery.tag.statistics.blocks", createTagStatistics(TagType.BLOCK));
        sendStatisticMessage(sender, "com.gardenevery.tag.statistics.total",
                new TagStatistics(TagHelper.getTagCount(), TagHelper.getAssociations(), TagHelper.getKeyCount()));
    }

    private TagStatistics createTagStatistics(TagType type) {
        return new TagStatistics(TagHelper.getTagCount(type), TagHelper.getTotalAssociations(type), TagHelper.getUniqueKeyCount(type));
    }

    private void sendStatisticMessage(@Nonnull ICommandSender sender, String key, TagStatistics stats) {
        sender.sendMessage(new TextComponentTranslation(key, stats.tagCount(), stats.elementCount(), stats.uniqueElementCount()));}

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender) {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
