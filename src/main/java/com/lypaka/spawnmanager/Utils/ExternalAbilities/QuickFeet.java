package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.cobblemon.mod.common.pokemon.Pokemon;

public class QuickFeet {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getName().equalsIgnoreCase("QuickFeet") || pokemon.getAbility().getName().equalsIgnoreCase("Quick Feet");

    }

}
