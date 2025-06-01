package com.lypaka.spawnmanager;

import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import com.lypaka.spawnmanager.Commands.SpawnManagerCommand;
import com.lypaka.spawnmanager.Listeners.ServerStartedListener;
import com.lypaka.spawnmanager.Utils.HeldItemUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SpawnManager implements ModInitializer {

    public static final String MOD_ID = "spawnmanager";
    public static final String MOD_NAME = "SpawnManager";
    public static final Logger logger = LogManager.getLogger(MOD_NAME);
    public static BasicConfigManager configManager;

    @Override
    public void onInitialize() {

        Path dir = ConfigUtils.checkDir(Paths.get("./config/spawnmanager"));
        String[] files = new String[]{"heldItems.conf", "guiSettings.conf"};
        configManager = new BasicConfigManager(files, dir, SpawnManager.class, MOD_NAME, MOD_ID, logger);
        configManager.init();
        try {

            ConfigGetters.load();
            HeldItemUtils.load();

        } catch (ObjectMappingException e) {

            throw new RuntimeException(e);

        }

        ServerLifecycleEvents.SERVER_STARTED.register(new ServerStartedListener());
        SpawnManagerCommand.register();

    }

}
