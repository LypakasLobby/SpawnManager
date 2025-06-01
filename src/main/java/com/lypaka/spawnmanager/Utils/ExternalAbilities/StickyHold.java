package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.cobblemon.mod.common.pokemon.Pokemon;

// TODO Search for this and investigate why IntelliJ is telling me its unused
public class StickyHold {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getName().equalsIgnoreCase("StickyHold") || pokemon.getAbility().getName().equalsIgnoreCase("Sticky Hold");

    }

}
