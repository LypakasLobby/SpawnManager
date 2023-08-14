package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;

public class Intimidate {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Intimidate");

    }

    public static Pokemon tryIntimidate (Pokemon wildPokemon, Pokemon playerPokemon) {

        if (wildPokemon == null) return null;
        int level = playerPokemon.getPokemonLevel();
        int spawnLevel = wildPokemon.getPokemonLevel();
        if (level > spawnLevel) {

            int difference = level - spawnLevel;
            if (difference >= 5) {

                if (RandomHelper.getRandomChance(50)) {

                    wildPokemon = null;

                }

            }

        }

        return wildPokemon;

    }

}
