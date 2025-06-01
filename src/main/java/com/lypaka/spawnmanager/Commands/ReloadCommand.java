package com.lypaka.spawnmanager.Commands;

import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.PermissionHandler;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import com.lypaka.spawnmanager.ConfigGetters;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.SpawnManager;
import com.lypaka.spawnmanager.Spawners.NaturalSpawner;
import com.lypaka.spawnmanager.Utils.HeldItemUtils;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.IOException;

public class ReloadCommand {

    public ReloadCommand (CommandDispatcher<ServerCommandSource> dispatcher) {

        for (String a : SpawnManagerCommand.ALIASES) {

            dispatcher.register(
                    CommandManager.literal(a)
                            .then(
                                    CommandManager.literal("reload")
                                            .executes(c -> {

                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                    if (!c.getSource().getServer().isSingleplayer()) {

                                                        if (!PermissionHandler.hasPermission(player, "spawnmanager.command.admin")) {

                                                            player.sendMessage(FancyTextHandler.getFormattedText("&cYou don't have permission to use this command!"));
                                                            return 0;

                                                        }

                                                    } else {

                                                        if (!player.getName().getString().equalsIgnoreCase("Lypaka")) {

                                                            player.sendMessage(FancyTextHandler.getFormattedText("&cYou don't have permission to use this command!"));
                                                            return 0;

                                                        }

                                                    }

                                                }

                                                try {

                                                    SpawnManager.configManager.load();
                                                    ConfigGetters.load();
                                                    SpawnAreaHandler.loadAreas();
                                                    HeldItemUtils.load();
                                                    NaturalSpawner.startTimer();
                                                    c.getSource().sendMessage(FancyTextHandler.getFormattedText("&aSuccessfully reloaded SpawnManager!"));

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
