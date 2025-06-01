package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.cobblemon.mod.common.pokemon.Pokemon;

public class VitalSpirit {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getName().equalsIgnoreCase("VitalSpirit") || pokemon.getAbility().getName().equalsIgnoreCase("Vital Spirit");

    }

}
