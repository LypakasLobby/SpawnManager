package com.lypaka.spawnmanager.SpawnAreas.SpawnerSettings;

public class FishSpawnerSettings {

    private final boolean clearSpawns;
    private final boolean despawnAfterBattle;
    private final int despawnTimer;

    public FishSpawnerSettings (boolean clearSpawns, boolean despawnAfterBattle, int despawnTimer) {

        this.clearSpawns = clearSpawns;
        this.despawnAfterBattle = despawnAfterBattle;
        this.despawnTimer = despawnTimer;

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

}
