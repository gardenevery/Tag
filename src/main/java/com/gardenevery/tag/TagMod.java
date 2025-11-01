package com.gardenevery.tag;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class TagMod {

    @Mod.EventHandler
    public void onFMLoadComplete(FMLLoadCompleteEvent event) {
        TagSync.oreDictionarySync();
        TagBuilder.closeRegistration();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new TagCommand());
    }
}
