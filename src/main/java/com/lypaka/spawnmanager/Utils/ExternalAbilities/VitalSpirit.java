package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class VitalSpirit {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("VitalSpirit") || pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Vital Spirit");

    }

}
