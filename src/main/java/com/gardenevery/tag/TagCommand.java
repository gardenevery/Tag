package com.gardenevery.tag;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
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
                sender.sendMessage(new TextComponentString("§cYou do not have permission to use this command."));
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
                player.sendMessage(new TextComponentString("§cYou are not holding any item."));
                return;
            }

            if (heldStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                var fluidHandler = heldStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
                if (fluidHandler != null) {
                    var fluid = fluidHandler.drain(Integer.MAX_VALUE, false);
                    if (fluid != null && fluid.amount > 0) {
                        var fluidTags = TagHelper.tags(fluid);
                        if (!fluidTags.isEmpty()) {
                            player.sendMessage(new TextComponentString("§aFluid tags: " + String.join(", ", fluidTags)));
                        } else {
                            player.sendMessage(new TextComponentString("§7This fluid has no tags."));
                        }
                        return;
                    } else {
                        player.sendMessage(new TextComponentString("§eThis fluid container is empty."));
                    }
                }
            }

            var itemTags = TagHelper.tags(heldStack);
            if (!itemTags.isEmpty()) {
                player.sendMessage(new TextComponentString("§aItem tags: " + String.join(", ", itemTags)));
            } else {
                player.sendMessage(new TextComponentString("§7This item has no tags."));
            }
        }
    }

    private void showTagStatistics(@Nonnull ICommandSender sender) {
        int itemTagCount = TagManager.ITEM_TAGS.getTagCount();
        int fluidTagCount = TagManager.FLUID_TAGS.getTagCount();
        int blockTagCount = TagManager.BLOCK_TAGS.getTagCount();
        int totalTagCount = itemTagCount + fluidTagCount + blockTagCount;

        int itemAssociations = TagManager.ITEM_TAGS.getTotalAssociations();
        int fluidAssociations = TagManager.FLUID_TAGS.getTotalAssociations();
        int blockAssociations = TagManager.BLOCK_TAGS.getTotalAssociations();
        int totalAssociations = itemAssociations + fluidAssociations + blockAssociations;

        int uniqueItems = TagManager.ITEM_TAGS.getKeyCount();
        int uniqueFluids = TagManager.FLUID_TAGS.getKeyCount();
        int uniqueBlocks = TagManager.BLOCK_TAGS.getKeyCount();
        int totalUniqueElements = uniqueItems + uniqueFluids + uniqueBlocks;

        sender.sendMessage(new TextComponentString("§6=== Tag Statistics ==="));
        sender.sendMessage(new TextComponentString("§aItem Tags: §f" + itemTagCount + " §7Associations: §f" + itemAssociations + " (§f" + uniqueItems + "§7 unique)"));
        sender.sendMessage(new TextComponentString("§bFluid Tags: §f" + fluidTagCount + " §7Associations: §f" + fluidAssociations + " (§f" + uniqueFluids + "§7 unique)"));
        sender.sendMessage(new TextComponentString("§eBlock Tags: §f" + blockTagCount + " §7Associations: §f" + blockAssociations + " (§f" + uniqueBlocks + "§7 unique)"));
        sender.sendMessage(new TextComponentString("§6Total: §f" + totalTagCount + " tags, §f" + totalAssociations + " associations, §f" + totalUniqueElements + " unique elements"));
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
