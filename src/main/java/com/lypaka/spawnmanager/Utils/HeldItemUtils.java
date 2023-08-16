package com.lypaka.spawnmanager.Utils;

import com.google.common.reflect.TypeToken;
import com.lypaka.lypakautils.MiscHandlers.ItemStackBuilder;
import com.lypaka.spawnmanager.SpawnManager;
import com.lypaka.spawnmanager.Utils.ExternalAbilities.CompoundEyes;
import com.lypaka.spawnmanager.Utils.ExternalAbilities.SuperLuck;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import net.minecraft.item.ItemStack;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.*;

public class HeldItemUtils {

    public static Map<String, Map<String, List<String>>> heldItemMap = new HashMap<>();

    public static void load() throws ObjectMappingException {

        heldItemMap = SpawnManager.configManager.getConfigNode(1, "Items").getValue(new TypeToken<Map<String, Map<String, List<String>>>>() {});

    }

    public static void tryApplyHeldItem (Pokemon wildPokemon, Pokemon playersPokemon) {

        String name = wildPokemon.getLocalizedName().toLowerCase();
        String form = wildPokemon.getForm().getLocalizedName();

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

            if (RandomHelper.getRandomChance(percent)) {

                List<String> ids = possibleItems.get(percent + "%");
                String id = RandomHelper.getRandomElementFromList(ids);
                heldItem = ItemStackBuilder.buildFromStringID(id);
                heldItem.setCount(1);
                wildPokemon.setHeldItem(heldItem);
                break;

            }

        }

    }

}
