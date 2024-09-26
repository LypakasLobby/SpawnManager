package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.lypaka.spawnmanager.SpawnAreas.Spawns.PokemonSpawn;
import com.lypaka.spawnmanager.Utils.PokemonSpawnBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;

import java.util.*;

public class MagnetPull {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("MagnetPull") || pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Magnet Pull");

    }

    public static Pokemon tryMagnetPull (Pokemon originalSpawn, Map<PokemonSpawn, Double> possibleSpawns) {

        if (!RandomHelper.getRandomChance(50)) return originalSpawn;
        Map<PokemonSpawn, Double> pokemonMap = new HashMap<>();
        Map<UUID, PokemonSpawn> m1 = new HashMap<>();
        Map<Double, UUID> m2 = new HashMap<>();
        for (Map.Entry<PokemonSpawn, Double> entry : possibleSpawns.entrySet()) {

            if (entry.getKey().getTypes().contains(Element.STEEL)) {

                if (!pokemonMap.containsKey(entry.getKey())) {

                    pokemonMap.put(entry.getKey(), entry.getValue());
                    UUID uuid = UUID.randomUUID();
                    m1.put(uuid, entry.getKey());
                    m2.put(entry.getValue(), uuid);

                }

            }

        }

        if (pokemonMap.size() > 0) {

            List<Double> chances = new ArrayList<>(m2.keySet());
            for (int i = chances.size() - 1; i >= 0; i--) {

                double chance = chances.get(i);
                if (RandomHelper.getRandomChance(chance)) {

                    UUID uuid = m2.get(chance);
                    return PokemonSpawnBuilder.buildPokemonFromPokemonSpawn(m1.get(uuid));

                }

            }

        }

        return originalSpawn;

    }

}
