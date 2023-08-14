package com.lypaka.spawnmanager;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;
import java.util.Map;

public class ConfigGetters {

    public static boolean disablePixelmonsSpawner;

    public static int mainMenuRows;
    public static String mainMenuTitle;
    public static Map<String, Map<String, String>> mainMenuSlotsMap;
    public static int spawnMainMenuRows;
    public static String spawnMainMenuTitle;
    public static Map<String, Map<String, String>> spawnMainMenuSlotsMap;
    public static String allSpawnsMenuFormatName;
    public static List<String> allSpawnsMenuFormatLore;
    public static int allSpawnsMenuRows;
    public static String allSpawnsMenuTitle;
    public static Map<String, Map<String, String>> allSpawnsMenuSlotsMap;
    public static String possibleSpawnsMenuFormatName;
    public static List<String> possibleSpawnsMenuFormatLore;
    public static int possibleSpawnsMenuRows;
    public static String possibleSpawnsMenuTitle;
    public static Map<String, Map<String, String>> possibleSpawnsMenuSlotsMap;

    public static void load() throws ObjectMappingException {

        disablePixelmonsSpawner = SpawnManager.configManager.getConfigNode(0, "Disable-Pixelmons-Spawner").getBoolean();

        mainMenuRows = SpawnManager.configManager.getConfigNode(2, "Main-Menu", "General", "Rows").getInt();
        mainMenuTitle = SpawnManager.configManager.getConfigNode(2, "Main-Menu", "General", "Title").getString();
        mainMenuSlotsMap = SpawnManager.configManager.getConfigNode(2, "Main-Menu", "Slots").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
        spawnMainMenuRows = SpawnManager.configManager.getConfigNode(2, "Spawn-Main-Menu", "General", "Rows").getInt();
        spawnMainMenuTitle = SpawnManager.configManager.getConfigNode(2, "Spawn-Main-Menu", "General", "Title").getString();
        spawnMainMenuSlotsMap = SpawnManager.configManager.getConfigNode(2, "Spawn-Main-Menu", "Slots").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
        allSpawnsMenuFormatName = SpawnManager.configManager.getConfigNode(2, "Spawns-All", "Format", "Display-Name").getString();
        allSpawnsMenuFormatLore = SpawnManager.configManager.getConfigNode(2, "Spawns-All", "Format", "Lore").getList(TypeToken.of(String.class));
        allSpawnsMenuRows = SpawnManager.configManager.getConfigNode(2, "Spawns-All", "General", "Rows").getInt();
        allSpawnsMenuTitle = SpawnManager.configManager.getConfigNode(2, "Spawns-All", "General", "Title").getString();
        allSpawnsMenuSlotsMap = SpawnManager.configManager.getConfigNode(2, "Spawns-All", "Slots").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
        possibleSpawnsMenuFormatName = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "Format", "Display-Name").getString();
        possibleSpawnsMenuFormatLore = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "Format", "Lore").getList(TypeToken.of(String.class));
        possibleSpawnsMenuRows = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "General", "Rows").getInt();
        possibleSpawnsMenuTitle = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "General", "Title").getString();
        possibleSpawnsMenuSlotsMap = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "Slots").getValue(new TypeToken<Map<String, Map<String, String>>>() {});

    }

}
