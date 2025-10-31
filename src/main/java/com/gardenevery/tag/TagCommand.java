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
        return "gettags";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "gettags";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        if (sender instanceof EntityPlayer player) {
            var heldStack = player.getHeldItem(EnumHand.MAIN_HAND);

            if (heldStack.isEmpty()) {
                player.sendMessage(new TextComponentString("§cYou are not holding any item."));
                return;
            }

            if (heldStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                var fluidHandler = heldStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
                if (fluidHandler != null) {
                    var fluid = fluidHandler.drain(1, false);
                    if (fluid != null && fluid.amount > 0) {
                        var fluidTags = TagHelper.tags(fluid);
                        if (!fluidTags.isEmpty()) {
                            player.sendMessage(new TextComponentString("§aFluid tags: " + String.join(", ", fluidTags)));
                        } else {
                            player.sendMessage(new TextComponentString("§7This fluid has no tags."));
                        }
                        return;
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

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender) {
        return true;
    }
}
