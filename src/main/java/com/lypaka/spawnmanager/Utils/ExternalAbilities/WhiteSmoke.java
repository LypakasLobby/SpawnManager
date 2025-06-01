package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.cobblemon.mod.common.pokemon.Pokemon;

public class WhiteSmoke {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getName().equalsIgnoreCase("WhiteSmoke") || pokemon.getAbility().getName().equalsIgnoreCase("White Smoke");

    }

}
