package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;

public class CuteCharm {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("CuteCharm") || pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Cute Charm");

    }

    public static void tryApplyCuteCharmEffect (Pokemon wildPokemon, Pokemon playersPokemon) {

        Gender playerPokemonGender = playersPokemon.getGender();
        if (playerPokemonGender == Gender.NONE) return;
        if (wildPokemon.getGender() == Gender.NONE) return;

        Gender opposite;
        if (playerPokemonGender == Gender.MALE) {

            opposite = Gender.FEMALE;

        } else {

            opposite = Gender.MALE;

        }

        if (wildPokemon.getForm().getMalePercentage() < 100 && wildPokemon.getForm().getMalePercentage() > 0) {

            // pokemon can be both male and female
            if (RandomHelper.getRandomChance(66.67)) {

                wildPokemon.setGender(opposite);

            }

        }

    }

}
