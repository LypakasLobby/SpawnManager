package com.lypaka.spawnmanager.Listeners;

import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.spawnmanager.Spawners.NaturalSpawner;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DisconnectListener {

    // Removes player from AreaManager areas for Natural Spawner shit
    @SubscribeEvent
    public void onLeave (PlayerEvent.PlayerLoggedOutEvent event) {

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        AreaHandler.playersInArea.entrySet().forEach(entry -> {

            Map<Area, List<UUID>> map = entry.getValue();
            map.forEach((key, uuids) -> uuids.removeIf(e -> {

                if (e.toString().equalsIgnoreCase(player.getUniqueID().toString())) {

                    Map<UUID, List<PixelmonEntity>> spawns = NaturalSpawner.pokemonSpawnedMap.get(key);
                    List<PixelmonEntity> pokemon = spawns.get(player.getUniqueID());
                    for (PixelmonEntity entity : pokemon) {

                        if (entity.battleController == null) {

                            entity.remove();

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
