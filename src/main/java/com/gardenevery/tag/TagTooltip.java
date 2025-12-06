/*
 * MIT License
 *
 * Copyright (c) 2020 SnowShock35
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gardenevery.tag;

import java.util.List;
import java.util.Set;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class TagTooltip {

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        var stack = event.getItemStack();
        if (stack.isEmpty()) {
            return;
        }

        List<String> tooltip = event.getToolTip();
        if (isShiftKeyDown()) {
            generateDetailedTooltip(stack, tooltip);
        } else {
            tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.hold_shift_for_tags"));
        }
    }

    private static boolean isShiftKeyDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    private static void generateDetailedTooltip(ItemStack stack, List<String> tooltip) {
        Set<String> itemTags = TagHelper.tags(stack);
        Set<String> fluidTags = null;

        if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            var fluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
            if (fluidHandler != null) {
                var fluid = fluidHandler.drain(Integer.MAX_VALUE, false);
                if (fluid != null && fluid.amount > 0) {
                    fluidTags = TagHelper.tags(fluid);
                }
            }
        }

        boolean hasItemTags = !itemTags.isEmpty();
        boolean hasFluidTags = fluidTags != null && !fluidTags.isEmpty();

        if (!hasItemTags && !hasFluidTags) {
            return;
        }

        tooltip.add("Tags:");
        if (hasItemTags) {
            itemTags.stream().sorted().map(tag -> "  " + tag).forEach(tooltip::add);
        }

        if (hasFluidTags) {
            if (hasItemTags) {
                tooltip.add("");
            }
            fluidTags.stream().sorted().map(tag -> "  " + tag).forEach(tooltip::add);
        }
    }
}
