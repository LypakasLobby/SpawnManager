package com.lypaka.spawnmanager.Listeners;

import com.lypaka.spawnmanager.ConfigGetters;
import com.lypaka.spawnmanager.SpawnManager;
import com.lypaka.spawnmanager.Spawners.*;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

import java.util.Timer;
import java.util.TimerTask;

@Mod.EventBusSubscriber(modid = SpawnManager.MOD_ID)
public class ServerStartedListener {

    public static boolean defaultSpawnerActive = true;

    @SubscribeEvent
    public static void onServerStarted (FMLServerStartedEvent event) {

        MinecraftForge.EVENT_BUS.register(new AreaListener());
        MinecraftForge.EVENT_BUS.register(new GrassSpawner());
        MinecraftForge.EVENT_BUS.register(new SurfSpawner());
        MinecraftForge.EVENT_BUS.register(new TickListener());

        Pixelmon.EVENT_BUS.register(new BattleEndListener());
        Pixelmon.EVENT_BUS.register(new FishSpawner());
        Pixelmon.EVENT_BUS.register(new HeadbuttSpawner());
        Pixelmon.EVENT_BUS.register(new NaturalPixelmonSpawnListener());
        Pixelmon.EVENT_BUS.register(new RockSmashSpawner());

        NaturalSpawner.startTimer();

        if (ConfigGetters.disablePixelmonsSpawner) {

            defaultSpawnerActive = false;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {

                    PixelmonSpawning.coordinator.deactivate();

                }

            }, 3000);

        }

    }

}
