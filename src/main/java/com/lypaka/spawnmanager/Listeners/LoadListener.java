package com.lypaka.spawnmanager.Listeners;

import com.lypaka.areamanager.API.AreasLoadedEvent;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.SpawnManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = SpawnManager.MOD_ID)
public class LoadListener {

    @SubscribeEvent
    public static void onAreaLoad (AreasLoadedEvent event) throws IOException, ObjectMappingException {

        SpawnAreaHandler.loadAreas();

    }

}
