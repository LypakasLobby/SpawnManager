package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class StickyHold {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("StickyHold") || pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Sticky Hold");

    }

}
