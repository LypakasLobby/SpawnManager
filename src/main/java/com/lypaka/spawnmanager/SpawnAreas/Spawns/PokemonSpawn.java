package com.lypaka.spawnmanager.SpawnAreas.Spawns;

public abstract class PokemonSpawn {

    private final String species;
    private final String form;
    private final int minLevel;
    private final int maxLevel;

    public PokemonSpawn (String species, String form, int minLevel, int maxLevel) {

        this.species = species;
        this.form = form;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;

    }

    public String getSpecies() {

        return this.species;

    }

    public String getForm() {

        return this.form;

    }

    public int getMinLevel() {

        return this.minLevel;

    }

    public int getMaxLevel() {

        return this.maxLevel;

    }

}
