package com.lypaka.spawnmanager.Commands;

import com.lypaka.areamanager.Regions.RegionHandler;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.lypaka.spawnmanager.ConfigGetters;
import com.lypaka.spawnmanager.Listeners.AreaListener;
import com.lypaka.spawnmanager.Listeners.BattleEndListener;
import com.lypaka.spawnmanager.Listeners.NaturalPixelmonSpawnListener;
import com.lypaka.spawnmanager.Listeners.ServerStartedListener;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.SpawnManager;
import com.lypaka.spawnmanager.Spawners.FishSpawner;
import com.lypaka.spawnmanager.Spawners.HeadbuttSpawner;
import com.lypaka.spawnmanager.Spawners.NaturalSpawner;
import com.lypaka.spawnmanager.Spawners.RockSmashSpawner;
import com.lypaka.spawnmanager.Utils.HeldItemUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ReloadCommand {

    public ReloadCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : SpawnManagerCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("reload")
                                            .executes(c -> {

                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                    if (!c.getSource().getServer().isSinglePlayer()) {

                                                        if (!PermissionHandler.hasPermission(player, "spawnmanager.command.admin")) {

                                                            player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                            return 0;

                                                        }

                                                    } else {

                                                        if (!player.getName().getString().equalsIgnoreCase("Lypaka")) {

                                                            player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                            return 0;

                                                        }

                                                    }

                                                }

                                                try {

                                                    SpawnManager.configManager.load();
                                                    ConfigGetters.load();
                                                    SpawnAreaHandler.loadAreas();
                                                    HeldItemUtils.load();
                                                    MinecraftForge.EVENT_BUS.register(new AreaListener());
                                                    Pixelmon.EVENT_BUS.register(new BattleEndListener());
                                                    Pixelmon.EVENT_BUS.register(new FishSpawner());
                                                    Pixelmon.EVENT_BUS.register(new HeadbuttSpawner());
                                                    Pixelmon.EVENT_BUS.register(new NaturalPixelmonSpawnListener());
                                                    Pixelmon.EVENT_BUS.register(new RockSmashSpawner());

                                                    NaturalSpawner.startTimer();

                                                    if (ServerStartedListener.defaultSpawnerActive) {

                                                        if (ConfigGetters.disablePixelmonsSpawner) {

                                                            Timer timer = new Timer();
                                                            timer.schedule(new TimerTask() {

                                                                @Override
                                                                public void run() {

                                                                    PixelmonSpawning.coordinator.deactivate();

                                                                }

                                                            }, 3000);

                                                        }

                                                    }
                                                    c.getSource().sendFeedback(FancyText.getFormattedText("&aSuccessfully reloaded SpawnManager!"), true);

                                                } catch (ObjectMappingException | IOException e) {

                                                    e.printStackTrace();

                                                }

                                                return 1;

                                            })
                            )
            );

        }

    }

}
