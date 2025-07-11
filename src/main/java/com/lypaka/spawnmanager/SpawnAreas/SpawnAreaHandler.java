package com.lypaka.spawnmanager.SpawnAreas;

import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Regions.Region;
import com.lypaka.areamanager.Regions.RegionHandler;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ComplexConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import com.lypaka.shadow.google.common.reflect.TypeToken;
import com.lypaka.spawnmanager.SpawnAreas.SpawnerSettings.*;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.*;
import com.lypaka.spawnmanager.SpawnManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpawnAreaHandler {

    public static Map<Area, SpawnArea> areaMap = new HashMap<>();
    public static Map<SpawnArea, AreaSpawns> areaSpawnMap = new HashMap<>();
    public static int areasWithNaturalSpawns = 0;

    public static void loadAreas() throws IOException, ObjectMappingException {

        SpawnManager.logger.info("[SpawnManager] Loading data..");
        areasWithNaturalSpawns = 0;
        String[] files = new String[]{"settings.conf"};
        for (Map.Entry<String, Region> regionMap : RegionHandler.regionMap.entrySet()) {

            String regionName = regionMap.getKey();
            Region region = regionMap.getValue();
            List<Area> areas = region.getAreas();
            for (Area area : areas) {

                String areaName = area.getName();
                SpawnManager.logger.info("Creating spawn files for area: " + areaName);
                Path dir = ConfigUtils.checkDir(Paths.get("./config/spawnmanager/regions/" + regionName + "/" + areaName));
                BasicConfigManager bcm = new BasicConfigManager(files, dir, SpawnManager.class, SpawnManager.MOD_NAME, SpawnManager.MOD_ID, SpawnManager.logger);
                bcm.init();

                boolean autoCaveBattle = true;
                List<String> caveBlockIDs = new ArrayList<>();
                boolean despawnAfterCaveBattle = true;
                Map<String, String> caveMessages = new HashMap<>();
                caveMessages.put("Spawn-Message", "&eA wild &b%pokemon%&e appeared!");
                caveMessages.put("Spawn-Message-Shiny", "&eA wild &4%pokemon%&e appeared!");
                double caveSpawnChance = 0.35;
                if (bcm.getConfigNode(0, "Cave-Spawner").isVirtual()) {

                    bcm.getConfigNode(0, "Cave-Spawner", "Auto-Battle").setValue(autoCaveBattle);
                    bcm.getConfigNode(0, "Cave-Spawner", "Block-IDs").setValue(caveBlockIDs);
                    bcm.getConfigNode(0, "Cave-Spawner", "Despawn-After-Battle").setValue(despawnAfterCaveBattle);
                    bcm.getConfigNode(0, "Cave-Spawner", "Messages").setValue(caveMessages);
                    bcm.getConfigNode(0, "Cave-Spawner", "Spawn-Attempt-Chance").setValue(caveSpawnChance);
                    bcm.getConfigNode(0, "Cave-Spawns").setValue(new ArrayList<>());
                    bcm.save();

                } else {

                    autoCaveBattle = bcm.getConfigNode(0, "Cave-Spawner", "Auto-Battle").getBoolean();
                    caveBlockIDs = bcm.getConfigNode(0, "Cave-Spawner", "Block-IDs").getList(TypeToken.of(String.class));
                    despawnAfterCaveBattle = bcm.getConfigNode(0, "Cave-Spawner", "Despawn-After-Battle").getBoolean();
                    caveMessages = bcm.getConfigNode(0, "Cave-Spawner", "Messages").getValue(new TypeToken<Map<String, String>>() {});
                    caveSpawnChance = bcm.getConfigNode(0, "Cave-Spawner", "Spawn-Attempt-Chance").getDouble();

                }
                CaveSpawnerSettings caveSpawnerSettings = new CaveSpawnerSettings(autoCaveBattle, caveBlockIDs, despawnAfterCaveBattle, caveMessages, caveSpawnChance);

                boolean clearFishSpawns = bcm.getConfigNode(0, "Fish-Spawner", "Clear-Spawns").getBoolean();
                boolean despawnAfterFishBattle = bcm.getConfigNode(0, "Fish-Spawner", "Despawn-After-Battle").getBoolean();
                int fishDespawnTimer = bcm.getConfigNode(0, "Fish-Spawner", "Despawn-Timer").getInt();
                FishSpawnerSettings fishSpawnerSettings = new FishSpawnerSettings(clearFishSpawns, despawnAfterFishBattle, fishDespawnTimer);

                boolean autoGrassBattle = bcm.getConfigNode(0, "Grass-Spawner", "Auto-Battle").getBoolean();
                List<String> blockIDs = bcm.getConfigNode(0, "Grass-Spawner", "Block-IDs").getList(TypeToken.of(String.class));
                boolean despawnAfterGrassBattle = bcm.getConfigNode(0, "Grass-Spawner", "Despawn-After-Battle").getBoolean();
                Map<String, String> grassMessages = bcm.getConfigNode(0, "Grass-Spawner", "Messages").getValue(new TypeToken<Map<String, String>>() {});
                double grassSpawnChance = bcm.getConfigNode(0, "Grass-Spawner", "Spawn-Attempt-Chance").getDouble();
                GrassSpawnerSettings grassSpawnerSettings = new GrassSpawnerSettings(autoGrassBattle, blockIDs, despawnAfterGrassBattle, grassMessages, grassSpawnChance);

                double headbuttAutoBattleChance = bcm.getConfigNode(0, "Headbutt-Spawner", "Auto-Battle-Chance").getDouble();
                boolean clearHeadbuttSpawns = bcm.getConfigNode(0, "Headbutt-Spawner", "Clear-Spawns").getBoolean();
                int headbuttCooldown = bcm.getConfigNode(0, "Headbutt-Spawner", "Cooldown").getInt();
                List<String> customHeadbuttTreeIDs = bcm.getConfigNode(0, "Headbutt-Spawner", "Custom-Headbutt-Tree-IDs").getList(TypeToken.of(String.class));
                boolean despawnHeadbuttAfterBattle = bcm.getConfigNode(0, "Headbutt-Spawner", "Despawn-After-Battle").getBoolean();
                int headbuttDespawnTimer = bcm.getConfigNode(0, "Headbutt-Spawner", "Despawn-Timer").getInt();
                boolean reduceHeadbuttPP = bcm.getConfigNode(0, "Headbutt-Spawner", "Reduce-PP").getBoolean();
                boolean requireHeadbuttMove = bcm.getConfigNode(0, "Headbutt-Spawner", "Require-Move").getBoolean();
                boolean usePixelmonsHeadbuttSystem = bcm.getConfigNode(0, "Headbutt-Spawner", "Use-Pixelmons-System").getBoolean();
                HeadbuttSpawnerSettings headbuttSpawnerSettings = new HeadbuttSpawnerSettings(headbuttAutoBattleChance, clearHeadbuttSpawns, headbuttCooldown, customHeadbuttTreeIDs,
                        despawnHeadbuttAfterBattle, headbuttDespawnTimer, reduceHeadbuttPP, requireHeadbuttMove, usePixelmonsHeadbuttSystem);

                double naturalAutoBattleChance = bcm.getConfigNode(0, "Natural-Spawner", "Auto-Battle-Chance").getDouble();
                boolean clearNaturalSpawns = bcm.getConfigNode(0, "Natural-Spawner", "Clear-Spawns").getBoolean();
                boolean despawnNaturalAfterBattle = bcm.getConfigNode(0, "Natural-Spawner", "Despawn-After-Battle").getBoolean();
                int naturalDespawnTimer = bcm.getConfigNode(0, "Natural-Spawner", "Despawn-Timer").getInt();
                boolean limitSpawns = bcm.getConfigNode(0, "Natural-Spawner", "Limit-Spawns").getBoolean();
                Map<String, String> messages = bcm.getConfigNode(0, "Natural-Spawner", "Messages").getValue(new TypeToken<Map<String, String>>() {});
                boolean preventPixelmonSpawns = bcm.getConfigNode(0, "Natural-Spawner", "Prevent-Pixelmon-Spawns").getBoolean();
                int spawnInterval = bcm.getConfigNode(0, "Natural-Spawner", "Spawn-Interval").getInt();
                NaturalSpawnerSettings naturalSpawnerSettings = new NaturalSpawnerSettings(naturalAutoBattleChance, clearNaturalSpawns, despawnNaturalAfterBattle, naturalDespawnTimer, limitSpawns, messages, preventPixelmonSpawns, spawnInterval);

                double rockSmashAutoBattleChance = bcm.getConfigNode(0, "Rock-Smash-Spawner", "Auto-Battle-Chance").getDouble();
                boolean clearRockSmashSpawns = bcm.getConfigNode(0, "Rock-Smash-Spawner", "Clear-Spawns").getBoolean();
                int rockSmashCooldown = bcm.getConfigNode(0, "Rock-Smash-Spawner", "Cooldown").getInt();
                List<String> customRockSmashIDs = bcm.getConfigNode(0, "Rock-Smash-Spawner", "Custom-Rock-Smash-Rock-IDs").getList(TypeToken.of(String.class));
                boolean despawnRockSmashAfterBattle = bcm.getConfigNode(0, "Rock-Smash-Spawner", "Despawn-After-Battle").getBoolean();
                int rockSmashDespawnTimer = bcm.getConfigNode(0, "Rock-Smash-Spawner", "Despawn-Timer").getInt();
                boolean reduceRockSmashPP = bcm.getConfigNode(0, "Rock-Smash-Spawner", "Reduce-PP").getBoolean();
                boolean requireRockSmashMove = bcm.getConfigNode(0, "Rock-Smash-Spawner", "Require-Move").getBoolean();
                boolean usePixelmonsRockSmashSystem = bcm.getConfigNode(0, "Rock-Smash-Spawner", "Use-Pixelmons-System").getBoolean();
                RockSmashSpawnerSettings rockSmashSpawnerSettings = new RockSmashSpawnerSettings(rockSmashAutoBattleChance, clearRockSmashSpawns, rockSmashCooldown, customRockSmashIDs,
                        despawnRockSmashAfterBattle, rockSmashDespawnTimer, reduceRockSmashPP, requireRockSmashMove, usePixelmonsRockSmashSystem);

                boolean autoSurfBattle = bcm.getConfigNode(0, "Surf-Spawner", "Auto-Battle").getBoolean();
                List<String> surfBlockIDs = bcm.getConfigNode(0, "Surf-Spawner", "Block-IDs").getList(TypeToken.of(String.class));
                boolean despawnAfterSurfBattle = bcm.getConfigNode(0, "Surf-Spawner", "Despawn-After-Battle").getBoolean();
                Map<String, String> surfMessages = bcm.getConfigNode(0, "Surf-Spawner", "Messages").getValue(new TypeToken<Map<String, String>>() {});
                double surfSpawnChance = bcm.getConfigNode(0, "Surf-Spawner", "Spawn-Attempt-Chance").getDouble();
                SurfSpawnerSettings surfSpawnerSettings = new SurfSpawnerSettings(autoSurfBattle, surfBlockIDs, despawnAfterSurfBattle, surfMessages, surfSpawnChance);

                SpawnArea a = new SpawnArea(area, caveSpawnerSettings, fishSpawnerSettings, grassSpawnerSettings, headbuttSpawnerSettings, naturalSpawnerSettings, rockSmashSpawnerSettings, surfSpawnerSettings);
                a.create();

                //AreaManager.areaConfigManager.put(area, bcm);

                List<String> caveSpawns = bcm.getConfigNode(0, "Cave-Spawns").getList(TypeToken.of(String.class));
                ComplexConfigManager ccm = new ComplexConfigManager(caveSpawns, "cave-spawns", "caveSpawnTemplate.conf", dir, SpawnManager.class, SpawnManager.MOD_NAME, SpawnManager.MOD_ID, SpawnManager.logger);
                ccm.init();
                List<CaveSpawn> caveSpawnList = new ArrayList<>();
                for (int i = 0; i < caveSpawns.size(); i++) {

                    String species = ccm.getConfigNode(i, "Pokemon-Data", "Species").getString();
                    String form = ccm.getConfigNode(i, "Pokemon-Data", "Form").getString();
                    int minLevel = ccm.getConfigNode(i, "Pokemon-Data", "Min-Level").getInt();
                    int maxLevel = ccm.getConfigNode(i, "Pokemon-Data", "Max-Level").getInt();
                    Map<String, Map<String, Map<String, String>>> spawnData = ccm.getConfigNode(i, "Spawn-Data").getValue(new TypeToken<Map<String, Map<String, Map<String, String>>>>() {});

                    CaveSpawn caveSpawn = new CaveSpawn(species, form, minLevel, maxLevel, spawnData);
                    caveSpawnList.add(caveSpawn);

                }

                List<String> fishSpawns = bcm.getConfigNode(0, "Fish-Spawns").getList(TypeToken.of(String.class));
                ComplexConfigManager fcm = new ComplexConfigManager(fishSpawns, "fish-spawns", "fishSpawnTemplate.conf", dir, SpawnManager.class, SpawnManager.MOD_NAME, SpawnManager.MOD_ID, SpawnManager.logger);
                fcm.init();
                List<FishSpawn> fishSpawnsList = new ArrayList<>();
                for (int i = 0; i < fishSpawns.size(); i++) {

                    String species = fcm.getConfigNode(i, "Pokemon-Data", "Species").getString();
                    String form = fcm.getConfigNode(i, "Pokemon-Data", "Form").getString();
                    int minLevel = fcm.getConfigNode(i, "Pokemon-Data", "Min-Level").getInt();
                    int maxLevel = fcm.getConfigNode(i, "Pokemon-Data", "Max-Level").getInt();
                    Map<String, Map<String, Map<String, String>>> spawnData = fcm.getConfigNode(i, "Spawn-Data").getValue(new TypeToken<Map<String, Map<String, Map<String, String>>>>() {});

                    FishSpawn fishSpawn = new FishSpawn(species, form, minLevel, maxLevel, spawnData);
                    fishSpawnsList.add(fishSpawn);

                }

                List<String> grassSpawns = bcm.getConfigNode(0, "Grass-Spawns").getList(TypeToken.of(String.class));
                ComplexConfigManager gcm = new ComplexConfigManager(grassSpawns, "grass-spawns", "grassSpawnTemplate.conf", dir, SpawnManager.class, SpawnManager.MOD_NAME, SpawnManager.MOD_ID, SpawnManager.logger);
                gcm.init();
                List<GrassSpawn> grassSpawnsList = new ArrayList<>();
                for (int i = 0; i < grassSpawns.size(); i++) {

                    String species = gcm.getConfigNode(i, "Pokemon-Data", "Species").getString();
                    String form = gcm.getConfigNode(i, "Pokemon-Data", "Form").getString();
                    int minLevel = gcm.getConfigNode(i, "Pokemon-Data", "Min-Level").getInt();
                    int maxLevel = gcm.getConfigNode(i, "Pokemon-Data", "Max-Level").getInt();
                    Map<String, Map<String, Map<String, String>>> spawnData = gcm.getConfigNode(i, "Spawn-Data").getValue(new TypeToken<Map<String, Map<String, Map<String, String>>>>() {});

                    GrassSpawn grassSpawn = new GrassSpawn(species, form, minLevel, maxLevel, spawnData);
                    grassSpawnsList.add(grassSpawn);

                }

                List<String> headbuttSpawns = bcm.getConfigNode(0, "Headbutt-Spawns").getList(TypeToken.of(String.class));
                ComplexConfigManager hcm = new ComplexConfigManager(headbuttSpawns, "headbutt-spawns", "headbuttSpawnTemplate.conf", dir, SpawnManager.class, SpawnManager.MOD_NAME, SpawnManager.MOD_ID, SpawnManager.logger);
                hcm.init();
                List<HeadbuttSpawn> headbuttSpawnsList = new ArrayList<>();
                for (int i = 0; i < headbuttSpawns.size(); i++) {

                    String species = hcm.getConfigNode(i, "Pokemon-Data", "Species").getString();
                    String form = hcm.getConfigNode(i, "Pokemon-Data", "Form").getString();
                    int minLevel = hcm.getConfigNode(i, "Pokemon-Data", "Min-Level").getInt();
                    int maxLevel = hcm.getConfigNode(i, "Pokemon-Data", "Max-Level").getInt();
                    Map<String, Map<String, Map<String, String>>> spawnData = hcm.getConfigNode(i, "Spawn-Data").getValue(new TypeToken<Map<String, Map<String, Map<String, String>>>>() {});

                    HeadbuttSpawn headbuttSpawn = new HeadbuttSpawn(species, form, minLevel, maxLevel, spawnData);
                    headbuttSpawnsList.add(headbuttSpawn);

                }

                List<String> naturalSpawns = bcm.getConfigNode(0, "Natural-Spawns").getList(TypeToken.of(String.class));
                ComplexConfigManager ncm = new ComplexConfigManager(naturalSpawns, "natural-spawns", "naturalSpawnTemplate.conf", dir, SpawnManager.class, SpawnManager.MOD_NAME, SpawnManager.MOD_ID, SpawnManager.logger);
                ncm.init();
                List<NaturalSpawn> naturalSpawnsList = new ArrayList<>();
                for (int i = 0; i < naturalSpawns.size(); i++) {

                    String species = ncm.getConfigNode(i, "Pokemon-Data", "Species").getString();
                    String form = ncm.getConfigNode(i, "Pokemon-Data", "Form").getString();
                    int minLevel = ncm.getConfigNode(i, "Pokemon-Data", "Min-Level").getInt();
                    int maxLevel = ncm.getConfigNode(i, "Pokemon-Data", "Max-Level").getInt();

                    Map<String, Map<String, Map<String, String>>> spawnData = ncm.getConfigNode(i, "Spawn-Data").getValue(new TypeToken<Map<String, Map<String, Map<String, String>>>>() {});
                    NaturalSpawn naturalSpawn = new NaturalSpawn(species, form, minLevel, maxLevel, spawnData);
                    naturalSpawnsList.add(naturalSpawn);

                }

                if (!naturalSpawnsList.isEmpty()) {

                    areasWithNaturalSpawns++;

                }

                List<String> rockSmashSpawns = bcm.getConfigNode(0, "Rock-Smash-Spawns").getList(TypeToken.of(String.class));
                ComplexConfigManager rcm = new ComplexConfigManager(rockSmashSpawns, "rock-smash-spawns", "rockSmashTemplate.conf", dir, SpawnManager.class, SpawnManager.MOD_NAME, SpawnManager.MOD_ID, SpawnManager.logger);
                rcm.init();
                List<RockSmashSpawn> rockSmashSpawnsList = new ArrayList<>();
                for (int i = 0; i < rockSmashSpawns.size(); i++) {

                    String species = rcm.getConfigNode(i, "Pokemon-Data", "Species").getString();
                    String form = rcm.getConfigNode(i, "Pokemon-Data", "Form").getString();
                    int minLevel = rcm.getConfigNode(i, "Pokemon-Data", "Min-Level").getInt();
                    int maxLevel = rcm.getConfigNode(i, "Pokemon-Data", "Max-Level").getInt();

                    Map<String, Map<String, Map<String, String>>> spawnData = rcm.getConfigNode(i, "Spawn-Data").getValue(new TypeToken<Map<String, Map<String, Map<String, String>>>>() {});
                    RockSmashSpawn rockSmashSpawn = new RockSmashSpawn(species, form, minLevel, maxLevel, spawnData);
                    rockSmashSpawnsList.add(rockSmashSpawn);

                }

                List<String> surfSpawns = bcm.getConfigNode(0, "Surf-Spawns").getList(TypeToken.of(String.class));
                ComplexConfigManager scm = new ComplexConfigManager(surfSpawns, "surf-spawns", "surfSpawnTemplate.conf", dir, SpawnManager.class, SpawnManager.MOD_NAME, SpawnManager.MOD_ID, SpawnManager.logger);
                scm.init();
                List<SurfSpawn> surfSpawnsList = new ArrayList<>();
                for (int i = 0; i < surfSpawns.size(); i++) {

                    String species = scm.getConfigNode(i, "Pokemon-Data", "Species").getString();
                    String form = scm.getConfigNode(i, "Pokemon-Data", "Form").getString();
                    int minLevel = scm.getConfigNode(i, "Pokemon-Data", "Min-Level").getInt();
                    int maxLevel = scm.getConfigNode(i, "Pokemon-Data", "Max-Level").getInt();
                    Map<String, Map<String, Map<String, String>>> spawnData = scm.getConfigNode(i, "Spawn-Data").getValue(new TypeToken<Map<String, Map<String, Map<String, String>>>>() {});

                    SurfSpawn surfSpawn = new SurfSpawn(species, form, minLevel, maxLevel, spawnData);
                    surfSpawnsList.add(surfSpawn);

                }

                AreaSpawns spawns = new AreaSpawns(a, caveSpawnList, fishSpawnsList, grassSpawnsList, headbuttSpawnsList, naturalSpawnsList, rockSmashSpawnsList, surfSpawnsList);
                areaSpawnMap.put(a, spawns);

            }

        }

    }

}
