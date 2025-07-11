package com.lypaka.spawnmanager.SpawnAreas.Spawns;

import java.util.Map;

public class CaveSpawn extends PokemonSpawn {

    private final Map<String, Map<String, Map<String, String>>> spawnData;

    public CaveSpawn (String species, String form, int minLevel, int maxLevel, Map<String, Map<String, Map<String, String>>> spawnData) {

        super(species, form, minLevel, maxLevel);
        this.spawnData = spawnData;

    }

    public Map<String, Map<String, Map<String, String>>> getSpawnData() {

        return this.spawnData;

    }

}