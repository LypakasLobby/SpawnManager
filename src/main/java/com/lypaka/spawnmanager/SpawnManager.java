package com.lypaka.spawnmanager;

import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import com.lypaka.spawnmanager.Utils.HeldItemUtils;
import net.minecraftforge.fml.common.Mod;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("spawnmanager")
public class SpawnManager {

    public static final String MOD_ID = "spawnmanager";
    public static final String MOD_NAME = "SpawnManager";
    public static final Logger logger = LogManager.getLogger("SpawnManager");
    public static BasicConfigManager configManager;

    // TODO
    // Rewrite how the spawners generate their list of possible spawns to fix NullPointers  -- DONE
    // Add optional support of Totem Pokemon and Titan Pokemon                              -- DONE
    // Check for updates on the held items                                                  -- DONE

    public SpawnManager() throws IOException, ObjectMappingException {

        Path dir = ConfigUtils.checkDir(Paths.get("./config/spawnmanager"));
        String[] files = new String[]{"spawnmanager.conf", "heldItems.conf", "guiSettings.conf"};
        configManager = new BasicConfigManager(files, dir, SpawnManager.class, MOD_NAME, MOD_ID, logger);
        configManager.init();
        ConfigGetters.load();
        HeldItemUtils.load();

    }

}
