package com.gardenevery.tag;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class TagMod {

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        var tooltipEventHandler = new TagTooltip();
        MinecraftForge.EVENT_BUS.register(tooltipEventHandler);
    }

    @Mod.EventHandler
    public void onFMLoadComplete(FMLLoadCompleteEvent event) {
        OreSync.oreDictionarySync();
        AbstractTagBuilder.closeRegistration();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new TagCommand());
    }
}
