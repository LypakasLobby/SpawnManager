package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.cobblemon.mod.common.pokemon.Pokemon;

public class SuctionCups {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getName().equalsIgnoreCase("SuctionCups") || pokemon.getAbility().getName().equalsIgnoreCase("Suction Cups");

    }

}
