package com.lypaka.spawnmanager.Listeners;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.spawnmanager.Spawners.NaturalSpawner;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DisconnectListener implements ServerPlayConnectionEvents.Disconnect {

    @Override
    public void onPlayDisconnect (ServerPlayNetworkHandler serverPlayNetworkHandler, MinecraftServer minecraftServer) {

        ServerPlayerEntity player = (ServerPlayerEntity) serverPlayNetworkHandler.getPlayer();
        AreaHandler.playersInArea.entrySet().forEach(entry -> {

            Map<Area, List<UUID>> map = entry.getValue();
            map.forEach((key, uuids) -> uuids.removeIf(e -> {

                if (e.toString().equalsIgnoreCase(player.getUuid().toString())) {

                    if (NaturalSpawner.pokemonSpawnedMap.containsKey(key)) {

                        Map<UUID, List<PokemonEntity>> spawns = NaturalSpawner.pokemonSpawnedMap.get(key);
                        if (spawns != null) {

                            List<PokemonEntity> pokemon = spawns.get(player.getUuid());
                            for (PokemonEntity entity : pokemon) {

                                if (!entity.isBattling()) {

                                    entity.remove(Entity.RemovalReason.DISCARDED);

                                }

                            }

                        }

                    }

                    return true;

                }

                return false;

            }));

            entry.setValue(map);

        });

    }

}
