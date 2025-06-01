package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.cobblemon.mod.common.pokemon.Pokemon;

public class Synchronize {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getName().equalsIgnoreCase("Synchronize");

    }

    public static void applySynchronize (Pokemon wildPokemon, Pokemon playersPokemon) {

        if (wildPokemon == null) return;
        wildPokemon.setNature(playersPokemon.getNature());

    }

}
