package com.lypaka.spawnmanager.Listeners;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TickListener {

    private static int tickCount = 0;
    public static Map<UUID, Integer> timeBetweenGrassSpawns = new HashMap<>();

    @SubscribeEvent
    public void onServerTick (TickEvent.ServerTickEvent event) {

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
