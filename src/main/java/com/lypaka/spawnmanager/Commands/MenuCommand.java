package com.lypaka.spawnmanager.Commands;

import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import com.lypaka.spawnmanager.GUIs.MainMenu;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class MenuCommand {

    public MenuCommand(CommandDispatcher<ServerCommandSource> dispatcher) {

        for (String a : SpawnManagerCommand.ALIASES) {

            dispatcher.register(
                    CommandManager.literal(a)
                            .then(
                                    CommandManager.literal("menu")
                                            .executes(c -> {

                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                    try {

                                                        MainMenu.open(player);

                                                    } catch (ObjectMappingException e) {

                                                        e.printStackTrace();

                                                    }

                                                }

                                                return 0;

                                            })
                            )
            );

        }

    }

}
