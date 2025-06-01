package com.lypaka.spawnmanager.Utils.ExternalAbilities;

import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.lypakautils.Handlers.RandomHandler;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.PokemonSpawn;
import com.lypaka.spawnmanager.Utils.PokemonSpawnBuilder;

import java.util.*;

public class LightningRod {

    public static boolean applies (Pokemon pokemon) {

        if (pokemon == null) return false;
        return pokemon.getAbility().getName().equalsIgnoreCase("LightningRod") || pokemon.getAbility().getName().equalsIgnoreCase("Lightning Rod");

    }

    public static Pokemon tryLightningRod (Pokemon originalSpawn, Map<PokemonSpawn, Double> possibleSpawns) {

        if (!RandomHandler.getRandomChance(50)) return originalSpawn;
        Map<PokemonSpawn, Double> pokemonMap = new HashMap<>();
        Map<UUID, PokemonSpawn> m1 = new HashMap<>();
        Map<Double, UUID> m2 = new HashMap<>();
        for (Map.Entry<PokemonSpawn, Double> entry : possibleSpawns.entrySet()) {

            if (entry.getKey().getTypes().contains(ElementalTypes.INSTANCE.getELECTRIC())) {

                if (!pokemonMap.containsKey(entry.getKey())) {

                    pokemonMap.put(entry.getKey(), entry.getValue());
                    UUID uuid = UUID.randomUUID();
                    m1.put(uuid, entry.getKey());
                    m2.put(entry.getValue(), uuid);

                }

            }

        }

        if (!pokemonMap.isEmpty()) {

            List<Double> chances = new ArrayList<>(m2.keySet());
            for (int i = chances.size() - 1; i >= 0; i--) {

                double chance = chances.get(i);
                if (RandomHandler.getRandomChance(chance)) {

                    UUID uuid = m2.get(chance);
                    return PokemonSpawnBuilder.buildPokemonFromPokemonSpawn(m1.get(uuid));

                }

            }

        }

        return originalSpawn;

    }

}
