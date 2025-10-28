package com.gardenevery.tag;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

final class TagSync {

    private TagSync() {
    }

    private static final Logger LOGGER = LogManager.getLogger("TagSync");

    public static void oreDictionarySync() {
        var oreNames = OreDictionary.getOreNames();
        int totalEntries = 0;
        int failedEntries = 0;

        LOGGER.info("=== Starting Ore Dictionary Sync ===");
        LOGGER.info("Found {} ore dictionary categories", oreNames.length);

        for (var oreName : oreNames) {
            if (oreName == null || oreName.isEmpty()) continue;

            List<ItemStack> ores = OreDictionary.getOres(oreName);

            for (var oreStack : ores) {
                if (oreStack.isEmpty()) {
                    failedEntries++;
                    continue;
                }

                try {
                    if (oreStack.getMetadata() == OreDictionary.WILDCARD_VALUE && oreStack.getItem().getHasSubtypes()) {
                        int synced = syncWildcardEntry(oreStack.getItem(), oreName);
                        totalEntries += synced;
                    } else {
                        TagBuilder.item(oreName).add(oreStack.copy());
                        totalEntries++;
                    }
                } catch (Exception e) {
                    failedEntries++;
                    LOGGER.debug("Sync failed: {} - {}", oreName, oreStack.getDisplayName(), e);
                }
            }
        }

        LOGGER.info("=== Sync completed: {} successful, {} failed ===", totalEntries, failedEntries);
    }

    private static int syncWildcardEntry(Item item, String tagName) {
        int synced = 0;

        for (int meta = 0; meta < 16; meta++) {
            try {
                var specificStack = new ItemStack(item, 1, meta);
                if (!specificStack.isEmpty() && specificStack.getItem() == item) {
                    TagBuilder.item(tagName).add(specificStack);
                    synced++;
                }
            } catch (Exception e) {
                //
            }
        }

        if (synced == 0) {
            var wildcardStack = new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
            TagBuilder.item(tagName).add(wildcardStack);
            synced = 1;
        }

        return synced;
    }
}
