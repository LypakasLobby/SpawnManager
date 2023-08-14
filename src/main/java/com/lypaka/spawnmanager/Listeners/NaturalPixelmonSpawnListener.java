package com.lypaka.spawnmanager.Listeners;

import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.SpawnArea;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
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
            if (spawnArea.getNaturalSpawnerSettings().doesPreventPixelmonSpawns()) event.setCanceled(true);

        }

    }

}
