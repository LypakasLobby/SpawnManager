package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class KeenEye {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("KeenEye") || pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Keen Eye");

    }

}
