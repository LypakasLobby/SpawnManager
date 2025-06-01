package com.lypaka.spawnmanager.Listeners;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.lypaka.areamanager.API.AreaEvents.AreaLeaveCallback;
import com.lypaka.areamanager.Areas.Area;
import com.lypaka.spawnmanager.SpawnAreas.SpawnArea;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.Spawners.FishSpawner;
import com.lypaka.spawnmanager.Spawners.HeadbuttSpawner;
import com.lypaka.spawnmanager.Spawners.NaturalSpawner;
import com.lypaka.spawnmanager.Spawners.RockSmashSpawner;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AreaListener implements AreaLeaveCallback {

    @Override
    public boolean onPlayerAreaLeave (ServerPlayerEntity player, Area area, boolean b) {

        SpawnArea spawnArea = SpawnAreaHandler.areaMap.get(area);
        FishSpawner.pokemonSpawnedMap.entrySet().removeIf(a -> {

            if (spawnArea.getFishSpawnerSettings().doesClearSpawns()) {

                Map<UUID, List<PokemonEntity>> spawns = FishSpawner.pokemonSpawnedMap.get(a.getKey());
                if (spawns.containsKey(player.getUuid())) {

                    List<PokemonEntity> pokemon = spawns.get(player.getUuid());
                    for (PokemonEntity entity : pokemon) {

                        if (!entity.isBattling()) {

                            entity.remove(Entity.RemovalReason.DISCARDED);

                        }

                    }

                }

                return true;

            }

            return false;

        });
        HeadbuttSpawner.pokemonSpawnedMap.entrySet().removeIf(a -> {

            if (spawnArea.getHeadbuttSpawnerSettings().doesClearSpawns()) {

                Map<UUID, List<PokemonEntity>> spawns = HeadbuttSpawner.pokemonSpawnedMap.get(a.getKey());
                if (spawns.containsKey(player.getUuid())) {

                    List<PokemonEntity> pokemon = spawns.get(player.getUuid());
                    for (PokemonEntity entity : pokemon) {

                        if (!entity.isBattling()) {

                            entity.remove(Entity.RemovalReason.DISCARDED);

                        }

                    }

                }

                return true;

            }

            return false;

        });
        NaturalSpawner.pokemonSpawnedMap.entrySet().removeIf(a -> {

            if (spawnArea.getNaturalSpawnerSettings().doesClearSpawns()) {

                Map<UUID, List<PokemonEntity>> spawns = NaturalSpawner.pokemonSpawnedMap.get(a.getKey());
                if (spawns.containsKey(player.getUuid())) {

                    List<PokemonEntity> pokemon = spawns.get(player.getUuid());
                    for (PokemonEntity entity : pokemon) {

                        if (!entity.isBattling()) {

                            entity.remove(Entity.RemovalReason.DISCARDED);

                        }

                    }

                }

                return true;

            }

            return false;

        });
        RockSmashSpawner.pokemonSpawnedMap.entrySet().removeIf(a -> {

            if (spawnArea.getRockSmashSpawnerSettings().doesClearSpawns()) {

                Map<UUID, List<PokemonEntity>> spawns = RockSmashSpawner.pokemonSpawnedMap.get(a.getKey());
                if (spawns.containsKey(player.getUuid())) {

                    List<PokemonEntity> pokemon = spawns.get(player.getUuid());
                    for (PokemonEntity entity : pokemon) {

                        if (!entity.isBattling()) {

                            entity.remove(Entity.RemovalReason.DISCARDED);

                        }

                    }

                }

                return true;

            }

            return false;

        });
        return true;

    }

}
