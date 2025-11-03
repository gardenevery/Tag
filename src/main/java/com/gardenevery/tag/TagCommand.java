package com.gardenevery.tag;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TagCommand extends CommandBase {

    @Nonnull
    @Override
    public String getName() {
        return "tag";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "tag [info]";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        if (args.length == 0) {
            showHeldItemTags(sender);
            return;
        }

        if ("info".equalsIgnoreCase(args[0])) {
            if (!sender.canUseCommand(2, this.getName())) {
                sender.sendMessage(new TextComponentTranslation("com.gardenevery.tag.nopermission"));
                return;
            }
            showTagStatistics(sender);
        } else {
            showHeldItemTags(sender);
        }
    }

    private void showHeldItemTags(@Nonnull ICommandSender sender) {
        if (sender instanceof EntityPlayer player) {
            var heldStack = player.getHeldItem(EnumHand.MAIN_HAND);

            if (heldStack.isEmpty()) {
                player.sendMessage(new TextComponentTranslation("com.gardenevery.tag.noitem"));
                return;
            }

            if (heldStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                var fluidHandler = heldStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
                if (fluidHandler != null) {
                    var fluid = fluidHandler.drain(Integer.MAX_VALUE, false);
                    if (fluid != null && fluid.amount > 0) {
                        var fluidTags = TagHelper.tags(fluid);
                        if (!fluidTags.isEmpty()) {
                            player.sendMessage(new TextComponentTranslation("com.gardenevery.tag.fluidtags", String.join(", ", fluidTags)));
                        } else {
                            player.sendMessage(new TextComponentTranslation("com.gardenevery.tag.nofluidtags"));
                        }
                        return;
                    } else {
                        player.sendMessage(new TextComponentTranslation("com.gardenevery.tag.emptycontainer"));
                    }
                }
            }

            var itemTags = TagHelper.tags(heldStack);
            if (!itemTags.isEmpty()) {
                player.sendMessage(new TextComponentTranslation("com.gardenevery.tag.itemtags", String.join(", ", itemTags)));
            } else {
                player.sendMessage(new TextComponentTranslation("com.gardenevery.tag.noitemtags"));
            }
        }
    }

    private void showTagStatistics(@Nonnull ICommandSender sender) {
        int itemTagCount = TagHelper.getTagCount(TagType.ITEM);
        int fluidTagCount = TagHelper.getTagCount(TagType.FLUID);
        int blockTagCount = TagHelper.getTagCount(TagType.BLOCK);
        int totalTagCount = TagHelper.getTagCount();

        int itemElementCount = TagHelper.getTotalAssociations(TagType.ITEM);
        int fluidElementCount = TagHelper.getTotalAssociations(TagType.FLUID);
        int blockElementCount = TagHelper.getTotalAssociations(TagType.BLOCK);
        int totalElementCount = TagHelper.getTotalAssociations();

        int uniqueItemElements = TagHelper.getUniqueKeyCount(TagType.ITEM);
        int uniqueFluidElements = TagHelper.getUniqueKeyCount(TagType.FLUID);
        int uniqueBlockElements = TagHelper.getUniqueKeyCount(TagType.BLOCK);
        int totalUniqueElements = TagHelper.getUniqueKeyCount();

        sender.sendMessage(new TextComponentTranslation("com.gardenevery.tag.statistics.title"));
        sender.sendMessage(new TextComponentTranslation("com.gardenevery.tag.statistics.items", itemTagCount, itemElementCount, uniqueItemElements));
        sender.sendMessage(new TextComponentTranslation("com.gardenevery.tag.statistics.fluids", fluidTagCount, fluidElementCount, uniqueFluidElements));
        sender.sendMessage(new TextComponentTranslation("com.gardenevery.tag.statistics.blocks", blockTagCount, blockElementCount, uniqueBlockElements));
        sender.sendMessage(new TextComponentTranslation("com.gardenevery.tag.statistics.total", totalTagCount, totalElementCount, totalUniqueElements));
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender) {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
