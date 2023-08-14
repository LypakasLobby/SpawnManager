package com.lypaka.spawnmanager.SpawnAreas.SpawnerSettings;

import java.util.Map;

public class NaturalSpawnerSettings {

    private final double autoBattleChance;
    private final boolean clearSpawns;
    private final boolean despawnAfterBattle;
    private final int despawnTimer;
    private final boolean limitSpawns;
    private final Map<String, String> messagesMap;
    private final boolean preventPixelmonSpawns;
    private final int spawnInterval;

    public NaturalSpawnerSettings (double autoBattleChance, boolean clearSpawns, boolean despawnAfterBattle, int despawnTimer, boolean limitSpawns,
                                   Map<String, String> messagesMap, boolean preventPixelmonSpawns, int spawnInterval) {

        this.autoBattleChance = autoBattleChance;
        this.clearSpawns = clearSpawns;
        this.despawnAfterBattle = despawnAfterBattle;
        this.despawnTimer = despawnTimer;
        this.limitSpawns = limitSpawns;
        this.messagesMap = messagesMap;
        this.preventPixelmonSpawns = preventPixelmonSpawns;
        this.spawnInterval = spawnInterval;

    }

    public double getAutoBattleChance() {

        return autoBattleChance;

    }

    public boolean doesClearSpawns() {

        return this.clearSpawns;

    }

    public boolean doesDespawnAfterBattle() {

        return this.despawnAfterBattle;

    }

    public int getDespawnTimer() {

        return this.despawnTimer;

    }

    public boolean doesLimitSpawns() {

        return this.limitSpawns;

    }

    public Map<String, String> getMessagesMap() {

        return this.messagesMap;

    }

    public boolean doesPreventPixelmonSpawns() {

        return this.preventPixelmonSpawns;

    }

    public int getSpawnInterval() {

        return this.spawnInterval;

    }

}
