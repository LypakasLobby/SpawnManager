package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.lypakautils.Handlers.RandomHandler;

public class Intimidate {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getName().equalsIgnoreCase("Intimidate");

    }

    public static Pokemon tryIntimidate (Pokemon wildPokemon, Pokemon playerPokemon) {

        if (wildPokemon == null) return null;
        int level = playerPokemon.getLevel();
        int spawnLevel = wildPokemon.getLevel();
        if (level > spawnLevel) {

            int difference = level - spawnLevel;
            if (difference >= 5) {

                if (RandomHandler.getRandomChance(50)) {

                    wildPokemon = null;

                }

            }

        }

        return wildPokemon;

    }

}
