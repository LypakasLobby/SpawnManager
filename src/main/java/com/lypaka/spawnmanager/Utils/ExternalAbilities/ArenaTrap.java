package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class ArenaTrap {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("ArenaTrap") || pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Arena Trap");

    }

}
