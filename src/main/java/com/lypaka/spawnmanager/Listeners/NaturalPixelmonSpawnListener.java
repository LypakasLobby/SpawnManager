package com.lypaka.spawnmanager.Listeners;

import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.SpawnArea;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.Spawners.FishSpawner;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class NaturalPixelmonSpawnListener {

    @SubscribeEvent
    public void onPixelmonSpawn (SpawnEvent event) {

        World world = event.action.spawnLocation.location.world;
        int x = event.action.spawnLocation.location.pos.getX();
        int y = event.action.spawnLocation.location.pos.getY();
        int z = event.action.spawnLocation.location.pos.getZ();

        List<Area> areas = AreaHandler.getFromLocation(x, y, z, world);
        for (Area area : areas) {

            SpawnArea spawnArea = SpawnAreaHandler.areaMap.get(area);
            if (event.action.getOrCreateEntity() instanceof PixelmonEntity) {

                PixelmonEntity pixelmon = (PixelmonEntity) event.action.getOrCreateEntity();
                try {

                    if (spawnArea.getNaturalSpawnerSettings().doesPreventPixelmonSpawns()) {

                        event.setCanceled(true);
                        return;

                    }

                } catch (NullPointerException e) {



                }

                FishSpawner.fishSpawnerMap.entrySet().removeIf(e -> e.getKey().toString().equalsIgnoreCase(pixelmon.getUniqueID().toString()));

            }

        }

    }

}
