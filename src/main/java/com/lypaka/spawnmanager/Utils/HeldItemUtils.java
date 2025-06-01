package com.lypaka.spawnmanager.Utils;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.lypakautils.Handlers.ItemStackHandler;
import com.lypaka.lypakautils.Handlers.RandomHandler;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import com.lypaka.shadow.google.common.reflect.TypeToken;
import com.lypaka.spawnmanager.SpawnManager;
import com.lypaka.spawnmanager.Utils.ExternalAbilities.CompoundEyes;
import com.lypaka.spawnmanager.Utils.ExternalAbilities.SuperLuck;
import net.minecraft.item.ItemStack;

import java.util.*;

public class HeldItemUtils {

    public static Map<String, Map<String, List<String>>> heldItemMap = new HashMap<>();

    public static void load() throws ObjectMappingException {

        heldItemMap = SpawnManager.configManager.getConfigNode(0, "Items").getValue(new TypeToken<Map<String, Map<String, List<String>>>>() {});

    }

    public static void tryApplyHeldItem (Pokemon wildPokemon, Pokemon playersPokemon) {

        String name = wildPokemon.getSpecies().getName().toLowerCase();
        String form = wildPokemon.getForm().getName();

        String pokemon;
        if (form.equalsIgnoreCase("default")) {

            pokemon = name;

        } else {

            pokemon = name + "-" + form;

        }

        if (!heldItemMap.containsKey(pokemon)) return;

        Map<String, List<String>> possibleItems = new HashMap<>();
        for (Map.Entry<String, Map<String, List<String>>> entry : heldItemMap.entrySet()) {

            if (entry.getKey().equalsIgnoreCase(pokemon)) {

                possibleItems = entry.getValue();
                break;

            }

        }

        if (possibleItems.isEmpty()) return;

        ItemStack heldItem = null;
        List<Integer> percents = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : possibleItems.entrySet()) {

            int percent = Integer.parseInt(entry.getKey().replace("%", ""));
            percents.add(percent);

        }

        // Checking possible held items in order from rarest to most common
        Collections.sort(percents);
        for (int i = 0; i < percents.size(); i++) {

            int percent = percents.get(i);
            if (percent == 1) {

                if (CompoundEyes.applies(playersPokemon) || SuperLuck.applies(playersPokemon)) {

                    percent = 5;

                }

            } else if (percent == 5) {

                if (CompoundEyes.applies(playersPokemon) || SuperLuck.applies(playersPokemon)) {

                    percent = 20;

                }

            } else if (percent == 50) {

                if (CompoundEyes.applies(playersPokemon) || SuperLuck.applies(playersPokemon)) {

                    percent = 60;

                }

            }

            if (RandomHandler.getRandomChance(percent)) {

                List<String> ids = possibleItems.get(percent + "%");
                String id = RandomHandler.getRandomElementFromList(ids);
                heldItem = ItemStackHandler.buildFromStringID(id);
                heldItem.setCount(1);
                wildPokemon.setHeldItem$common(heldItem);
                break;

            }

        }

    }

}
