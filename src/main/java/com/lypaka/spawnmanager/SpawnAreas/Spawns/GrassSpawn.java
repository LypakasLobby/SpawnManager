package com.lypaka.spawnmanager.SpawnAreas.Spawns;

import java.util.Map;

public class GrassSpawn extends PokemonSpawn {

    private final Map<String, Map<String, Map<String, String>>> spawnData;

    public GrassSpawn (String species, String form, int minLevel, int maxLevel, Map<String, Map<String, Map<String, String>>> spawnData) {

        super(species, form, minLevel, maxLevel, 0, 0, 0);
        this.spawnData = spawnData;

    }

    public Map<String, Map<String, Map<String, String>>> getSpawnData() {

        return this.spawnData;

    }

}