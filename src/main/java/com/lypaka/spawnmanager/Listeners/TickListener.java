package com.lypaka.spawnmanager.Listeners;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TickListener implements ServerTickEvents.EndTick {

    private static int tickCount = 0;
    public static Map<UUID, Integer> timeBetweenGrassSpawns = new HashMap<>();

    @Override
    public void onEndTick (MinecraftServer minecraftServer) {

        tickCount++;
        if (tickCount < 20) return;
        tickCount = 0;
        timeBetweenGrassSpawns.entrySet().removeIf(entry -> {

            int count = entry.getValue();
            count++;
            if (count >= 2) {

                return true;

            } else {

                entry.setValue(count);
                return false;

            }

        });

    }

}
