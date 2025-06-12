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
    public static String allSpawnMenuBorderID;
    public static String allSpawnsMenuMainMenuButtonDisplayName;
    public static String allSpawnsMenuMainMenuButtonID;
    public static List<String> allSpawnsMenuMainMenuButtonLore;
    public static int allSpawnsMenuMainMenuButtonSlot;
    public static String allSpawnsMenuNextPageButtonDisplayName;
    public static String allSpawnsMenuNextPageButtonID;
    public static int allSpawnsMenuNextPageButtonSlot;
    public static String allSpawnsMenuPrevPageButtonDisplayName;
    public static String allSpawnsMenuPrevPageButtonID;
    public static int allSpawnsMenuPrevPageButtonSlot;
    public static String possibleSpawnsMenuFormatName;
    public static List<String> possibleSpawnsMenuFormatLore;
    public static int possibleSpawnsMenuRows;
    public static String possibleSpawnsMenuTitle;
    public static String possibleSpawnMenuBorderID;
    public static String possibleSpawnsMenuMainMenuButtonDisplayName;
    public static String possibleSpawnsMenuMainMenuButtonID;
    public static List<String> possibleSpawnsMenuMainMenuButtonLore;
    public static int possibleSpawnsMenuMainMenuButtonSlot;
    public static String possibleSpawnsMenuNextPageButtonDisplayName;
    public static String possibleSpawnsMenuNextPageButtonID;
    public static int possibleSpawnsMenuNextPageButtonSlot;
    public static String possibleSpawnsMenuPrevPageButtonDisplayName;
    public static String possibleSpawnsMenuPrevPageButtonID;
    public static int possibleSpawnsMenuPrevPageButtonSlot;

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
        //allSpawnsMenuSlotsMap = SpawnManager.configManager.getConfigNode(2, "Spawns-All", "Slots").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
        allSpawnMenuBorderID = SpawnManager.configManager.getConfigNode(2, "Spawns-All", "Slots", "Border", "ID").getString();
        allSpawnsMenuMainMenuButtonDisplayName = SpawnManager.configManager.getConfigNode(2, "Spawns-All", "Slots", "Main-Menu", "Display-Name").getString();
        allSpawnsMenuMainMenuButtonID = SpawnManager.configManager.getConfigNode(2, "Spawns-All", "Slots", "Main-Menu", "ID").getString();
        allSpawnsMenuMainMenuButtonLore = SpawnManager.configManager.getConfigNode(2, "Spawns-All", "Slots", "Main-Menu", "Lore").getList(TypeToken.of(String.class));
        allSpawnsMenuMainMenuButtonSlot = SpawnManager.configManager.getConfigNode(2, "Spawns-All", "Slots", "Main-Menu", "Slot").getInt();
        allSpawnsMenuNextPageButtonDisplayName = SpawnManager.configManager.getConfigNode(2, "Spawns-All", "Slots", "Next-Page", "Display-Name").getString();
        allSpawnsMenuNextPageButtonID = SpawnManager.configManager.getConfigNode(2, "Spawns-All", "Slots", "Next-Page", "ID").getString();
        allSpawnsMenuNextPageButtonSlot = SpawnManager.configManager.getConfigNode(2, "Spawns-All", "Slots", "Next-Page", "Slot").getInt();
        allSpawnsMenuPrevPageButtonDisplayName = SpawnManager.configManager.getConfigNode(2, "Spawns-All", "Slots", "Prev-Page", "Display-Name").getString();
        allSpawnsMenuPrevPageButtonID = SpawnManager.configManager.getConfigNode(2, "Spawns-All", "Slots", "Prev-Page", "ID").getString();
        allSpawnsMenuPrevPageButtonSlot = SpawnManager.configManager.getConfigNode(2, "Spawns-All", "Slots", "Prev-Page", "Slot").getInt();

        possibleSpawnsMenuFormatName = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "Format", "Display-Name").getString();
        possibleSpawnsMenuFormatLore = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "Format", "Lore").getList(TypeToken.of(String.class));
        possibleSpawnsMenuRows = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "General", "Rows").getInt();
        possibleSpawnsMenuTitle = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "General", "Title").getString();
        //possibleSpawnsMenuSlotsMap = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "Slots").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
        possibleSpawnMenuBorderID = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "Slots", "Border", "ID").getString();
        possibleSpawnsMenuMainMenuButtonDisplayName = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "Slots", "Main-Menu", "Display-Name").getString();
        possibleSpawnsMenuMainMenuButtonID = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "Slots", "Main-Menu", "ID").getString();
        possibleSpawnsMenuMainMenuButtonLore = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "Slots", "Main-Menu", "Lore").getList(TypeToken.of(String.class));
        possibleSpawnsMenuMainMenuButtonSlot = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "Slots", "Main-Menu", "Slot").getInt();
        possibleSpawnsMenuNextPageButtonDisplayName = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "Slots", "Next-Page", "Display-Name").getString();
        possibleSpawnsMenuNextPageButtonID = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "Slots", "Next-Page", "ID").getString();
        possibleSpawnsMenuNextPageButtonSlot = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "Slots", "Next-Page", "Slot").getInt();
        possibleSpawnsMenuPrevPageButtonDisplayName = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "Slots", "Prev-Page", "Display-Name").getString();
        possibleSpawnsMenuPrevPageButtonID = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "Slots", "Prev-Page", "ID").getString();
        possibleSpawnsMenuPrevPageButtonSlot = SpawnManager.configManager.getConfigNode(2, "Spawns-Possible", "Slots", "Prev-Page", "Slot").getInt();

    }

}
