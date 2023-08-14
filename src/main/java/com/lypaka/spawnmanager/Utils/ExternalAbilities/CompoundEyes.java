package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class CompoundEyes {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("CompoundEyes") || pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Compound Eyes");

    }

}
