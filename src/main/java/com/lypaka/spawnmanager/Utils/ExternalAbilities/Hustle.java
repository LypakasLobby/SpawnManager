package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.lypakautils.Handlers.RandomHandler;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.PokemonSpawn;

public class Hustle {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getName().equalsIgnoreCase("Hustle");

    }

    public static int tryHustle (int level, PokemonSpawn spawn) {

        if (!RandomHandler.getRandomChance(50)) return level;

        return spawn.getMaxLevel();

    }

}
