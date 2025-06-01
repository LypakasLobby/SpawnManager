package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.lypakautils.Handlers.RandomHandler;

public class CuteCharm {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getName().equalsIgnoreCase("CuteCharm") || pokemon.getAbility().getName().equalsIgnoreCase("Cute Charm");

    }

    public static void tryApplyCuteCharmEffect (Pokemon wildPokemon, Pokemon playersPokemon) {

        Gender playerPokemonGender = playersPokemon.getGender();
        if (playerPokemonGender == Gender.GENDERLESS) return;
        if (wildPokemon.getGender() == Gender.GENDERLESS) return;

        Gender opposite;
        if (playerPokemonGender == Gender.MALE) {

            opposite = Gender.FEMALE;

        } else {

            opposite = Gender.MALE;

        }

        if (wildPokemon.getForm().getMaleRatio() < 100 && wildPokemon.getForm().getMaleRatio() > 0) {

            // pokemon can be both male and female
            if (RandomHandler.getRandomChance(66.67)) {

                wildPokemon.setGender(opposite);

            }

        }

    }

}
