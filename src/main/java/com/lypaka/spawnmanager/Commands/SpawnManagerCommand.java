package com.lypaka.spawnmanager.Commands;

import com.lypaka.spawnmanager.SpawnManager;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = SpawnManager.MOD_ID)
public class SpawnManagerCommand {

    public static List<String> ALIASES = Arrays.asList("spawnmanager", "spawns", "sman");

    @SubscribeEvent
    public static void onCommandRegistration (RegisterCommandsEvent event) {

        new MenuCommand(event.getDispatcher());
        new ReloadCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());

    }

}
