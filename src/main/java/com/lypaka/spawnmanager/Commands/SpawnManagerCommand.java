package com.lypaka.spawnmanager.Commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import java.util.Arrays;
import java.util.List;

public class SpawnManagerCommand {

    public static List<String> ALIASES = Arrays.asList("spawnmanager", "spawns", "sman");

    public static void register() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            new MenuCommand(dispatcher);
            new ReloadCommand(dispatcher);

        });

    }

}
