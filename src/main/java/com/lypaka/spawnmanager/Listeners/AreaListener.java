package com.lypaka.spawnmanager.Listeners;

import com.lypaka.areamanager.API.AreaLeaveEvent;
import com.lypaka.areamanager.Areas.Area;
import com.lypaka.spawnmanager.SpawnAreas.SpawnArea;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.Spawners.FishSpawner;
import com.lypaka.spawnmanager.Spawners.HeadbuttSpawner;
import com.lypaka.spawnmanager.Spawners.NaturalSpawner;
import com.lypaka.spawnmanager.Spawners.RockSmashSpawner;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class AreaListener {

    @SubscribeEvent
    public void onAreaLeave (AreaLeaveEvent event) {

        ServerPlayerEntity player = event.getPlayer();
        Area area = event.getArea();
        SpawnArea spawnArea = SpawnAreaHandler.areaMap.get(area);
        FishSpawner.pokemonSpawnedMap.entrySet().removeIf(a -> {

            if (spawnArea.getFishSpawnerSettings().doesClearSpawns()) {

                Map<UUID, List<PixelmonEntity>> spawns = FishSpawner.pokemonSpawnedMap.get(a.getKey());
                if (spawns.containsKey(player.getUniqueID())) {

                    List<PixelmonEntity> pokemon = spawns.get(player.getUniqueID());
                    for (PixelmonEntity entity : pokemon) {

                        if (entity.battleController == null) {

                            entity.remove();

                        }

                    }

                }

                return true;

            }

            return false;

        });
        HeadbuttSpawner.pokemonSpawnedMap.entrySet().removeIf(a -> {

            if (spawnArea.getHeadbuttSpawnerSettings().doesClearSpawns()) {

                Map<UUID, List<PixelmonEntity>> spawns = HeadbuttSpawner.pokemonSpawnedMap.get(a.getKey());
                if (spawns.containsKey(player.getUniqueID())) {

                    List<PixelmonEntity> pokemon = spawns.get(player.getUniqueID());
                    for (PixelmonEntity entity : pokemon) {

                        if (entity.battleController == null) {

                            entity.remove();

                        }

                    }

                }

                return true;

            }

            return false;

        });
        NaturalSpawner.pokemonSpawnedMap.entrySet().removeIf(a -> {

            if (spawnArea.getNaturalSpawnerSettings().doesClearSpawns()) {

                Map<UUID, List<PixelmonEntity>> spawns = NaturalSpawner.pokemonSpawnedMap.get(a.getKey());
                if (spawns.containsKey(player.getUniqueID())) {

                    List<PixelmonEntity> pokemon = spawns.get(player.getUniqueID());
                    for (PixelmonEntity entity : pokemon) {

                        if (entity.battleController == null) {

                            entity.remove();

                        }

                    }

                }

                return true;

            }

            return false;

        });
        RockSmashSpawner.pokemonSpawnedMap.entrySet().removeIf(a -> {

            if (spawnArea.getRockSmashSpawnerSettings().doesClearSpawns()) {

                Map<UUID, List<PixelmonEntity>> spawns = RockSmashSpawner.pokemonSpawnedMap.get(a.getKey());
                if (spawns.containsKey(player.getUniqueID())) {

                    List<PixelmonEntity> pokemon = spawns.get(player.getUniqueID());
                    for (PixelmonEntity entity : pokemon) {

                        if (entity.battleController == null) {

                            entity.remove();

                        }

                    }

                }

                return true;

            }

            return false;

        });

    }

}
