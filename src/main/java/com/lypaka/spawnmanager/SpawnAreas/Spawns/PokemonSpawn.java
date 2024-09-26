package com.lypaka.spawnmanager.SpawnAreas.Spawns;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;

import java.util.ArrayList;
import java.util.List;

public abstract class PokemonSpawn {

    private final String species;
    private final String form;
    private final int minLevel;
    private final int maxLevel;
    private double hostileChance;
    private double totemChance;
    private double titanChance;
    private final List<Element> types;

    public PokemonSpawn (String species, String form, int minLevel, int maxLevel, double hostileChance, double totemChance, double titanChance) {

        this.species = species;
        this.form = form;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.hostileChance = 0;
        this.totemChance = 0;
        this.titanChance = 0;
        this.types = new ArrayList<>();
        init();

    }

    // Used to get the types and store them to use with the external Abilities (like checking Flash Fire and shit) later on
    public void init() {

        Pokemon pokemon = PokemonBuilder.builder().species(this.species).build();
        if (!this.form.equalsIgnoreCase("default")) {

            pokemon.setForm(this.form);

        }
        this.types.addAll(pokemon.getForm().getTypes());

    }

    public double getHostileChance() {

        return this.hostileChance;

    }

    public void setHostileChance (double chance) {

        this.hostileChance = chance;

    }

    public double getTotemChance() {

        return this.totemChance;

    }

    public void setTotemChance (double chance) {

        this.totemChance = chance;

    }

    public double getTitanChance() {

        return this.titanChance;

    }

    public void setTitanChance (double chance) {

        this.titanChance = chance;

    }

    public List<Element> getTypes() {

        return this.types;

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
