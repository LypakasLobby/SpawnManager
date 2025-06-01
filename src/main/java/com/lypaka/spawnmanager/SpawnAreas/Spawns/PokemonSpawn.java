package com.lypaka.spawnmanager.SpawnAreas.Spawns;


import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.pokemon.FormData;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.lypaka.lypakautils.Handlers.RandomHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class PokemonSpawn {

    private final String speciesName;
    private final String form;
    private final int minLevel;
    private final int maxLevel;
    private final List<ElementalType> types;
    private Species species;

    public PokemonSpawn (String speciesName, String form, int minLevel, int maxLevel) {

        this.speciesName = speciesName;
        this.form = form;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.types = new ArrayList<>();
        init();

    }

    // Used to get the types and store them to use with the external Abilities (like checking Flash Fire and shit) later on
    public void init() {

        this.species = PokemonSpecies.INSTANCE.getByName(this.speciesName.toLowerCase());
        if (!this.form.equalsIgnoreCase("default")) {

            FormData data = this.species.getFormByName(this.form);
            data.getTypes().forEach(this.types::add);

        } else {

            this.species.getTypes().forEach(this.types::add);

        }

    }

    public List<ElementalType> getTypes() {

        return this.types;

    }

    public String getSpeciesName() {

        return this.speciesName;

    }

    public Species getSpecies() {

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

    public Pokemon buildAndGetPokemon() {

        return this.species.create(RandomHandler.getRandomNumberBetween(this.minLevel, this.maxLevel));

    }

}
