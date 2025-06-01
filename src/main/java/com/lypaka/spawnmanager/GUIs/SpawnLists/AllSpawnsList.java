package com.lypaka.spawnmanager.GUIs.SpawnLists;

import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.spawnmanager.ConfigGetters;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.*;
import com.lypaka.spawnmanager.Utils.HeldItemUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AllSpawnsList {

    public static void buildNatural (AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (NaturalSpawn natural : spawns.getNaturalSpawns()) {

            String speciesName = natural.getSpeciesName();
            String form = natural.getForm();
            Pokemon p = natural.buildAndGetPokemon();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(natural.getSpecies().getFormByName(form));

            }
            List<String> timeList = new ArrayList<>();
            List<String> weatherList = new ArrayList<>();
            List<String> locationList = new ArrayList<>();
            String levelRange = natural.getMinLevel() + " - " + natural.getMaxLevel();
            Map<String, Map<String, Map<String, String>>> data = natural.getSpawnData();
            for (Map.Entry<String, Map<String, Map<String, String>>> d1 : data.entrySet()) {

                String time = d1.getKey();
                timeList.add(time);
                Map<String, Map<String, String>> data2 = d1.getValue();
                for (Map.Entry<String, Map<String, String>> d2 : data2.entrySet()) {

                    String weather = d2.getKey();
                    if (!weatherList.contains(weather)) weatherList.add(weather);
                    Map<String, String> data3 = d2.getValue();
                    String location = data3.get("Spawn-Location");
                    if (!locationList.contains(location)) locationList.add(location);

                }

            }
            ItemStack sprite = PokemonItem.from(p);
            sprite.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
            List<String> configLore = new ArrayList<>(ConfigGetters.allSpawnsMenuFormatLore);
            configLore.removeIf(e -> e.contains("Rod Types"));
            configLore.removeIf(e -> e.contains("Wood Types"));
            configLore.removeIf(e -> e.contains("Stone Types"));
            List<String> heldItems = new ArrayList<>();
            boolean doHeldItems = false;
            for (String s : configLore) {

                if (s.contains("%heldItems%")) {

                    doHeldItems = true;
                    break;

                }

            }
            if (doHeldItems) {

                configLore.removeIf(e -> e.contains("%heldItems%"));
                if (HeldItemUtils.heldItemMap.containsKey(speciesName.toLowerCase())) {

                    Map<String, List<String>> possibleItems = HeldItemUtils.heldItemMap.get(speciesName.toLowerCase());
                    for (Map.Entry<String, List<String>> entry : possibleItems.entrySet()) {

                        String percent = entry.getKey();
                        String formatting = "&c";
                        if (percent.contains("1")) formatting = "&4&l";
                        if (percent.contains("5")) formatting = "&e&l";
                        if (percent.contains("30")) formatting = "&c";
                        if (percent.contains("50")) formatting = "&b";
                        if (percent.contains("100")) formatting = "&a";
                        for (String s : entry.getValue()) {

                            heldItems.add(FancyTextHandler.getFormattedString(formatting + percent + " -> " + s));

                        }

                    }

                } else {

                    heldItems.add(FancyTextHandler.getFormattedString("&cNone"));

                }
                configLore.addAll(heldItems);

            }
            List<Text> lore = new ArrayList<>();
            for (String l : configLore) {

                lore.add(FancyTextHandler.getFormattedText(l
                        .replace("%form%", form)
                        .replace("%levelRange%", levelRange)
                        .replace("%time%", String.join(", ", timeList))
                        .replace("%weather%", String.join(", ", weatherList))
                        .replace("%location%", String.join(", ", locationList))
                        .replace("%spawner%", "Natural Spawner")
                ));

            }
            sprite.set(DataComponentTypes.LORE, new LoreComponent(lore));
            UUID rand = UUID.randomUUID();
            m1.put(rand, p);
            m2.put(p, sprite);
            m3.put(p.getSpecies().getNationalPokedexNumber(), rand);

        }

    }

    public static void buildFish (AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (FishSpawn fish : spawns.getFishSpawns()) {

            String speciesName = fish.getSpeciesName();
            String form = fish.getForm();
            Pokemon p = fish.buildAndGetPokemon();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(fish.getSpecies().getFormByName(form));

            }
            List<String> timeList = new ArrayList<>();
            List<String> weatherList = new ArrayList<>();
            String levelRange = fish.getMinLevel() + " - " + fish.getMaxLevel();
            Map<String, Map<String, Map<String, String>>> data = fish.getSpawnData();
            for (Map.Entry<String, Map<String, Map<String, String>>> d1 : data.entrySet()) {

                String time = d1.getKey();
                if (!timeList.contains(time)) timeList.add(time);
                Map<String, Map<String, String>> data2 = d1.getValue();
                for (Map.Entry<String, Map<String, String>> d2 : data2.entrySet()) {

                    String weather = d2.getKey();
                    if (!weatherList.contains(weather)) weatherList.add(weather);

                }

            }
            ItemStack sprite = PokemonItem.from(p);
            sprite.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
            List<String> configLore = new ArrayList<>(ConfigGetters.allSpawnsMenuFormatLore);
            configLore.removeIf(e -> e.contains("Location"));
            configLore.removeIf(e -> e.contains("Wood Types"));
            configLore.removeIf(e -> e.contains("Stone Types"));
            List<String> heldItems = new ArrayList<>();
            boolean doHeldItems = false;
            for (String s : configLore) {

                if (s.contains("%heldItems%")) {

                    doHeldItems = true;
                    break;

                }

            }
            if (doHeldItems) {

                configLore.removeIf(e -> e.contains("%heldItems%"));
                if (HeldItemUtils.heldItemMap.containsKey(speciesName.toLowerCase())) {

                    Map<String, List<String>> possibleItems = HeldItemUtils.heldItemMap.get(speciesName.toLowerCase());
                    for (Map.Entry<String, List<String>> entry : possibleItems.entrySet()) {

                        String percent = entry.getKey();
                        String formatting = "&c";
                        if (percent.contains("1")) formatting = "&4&l";
                        if (percent.contains("5")) formatting = "&e&l";
                        if (percent.contains("30")) formatting = "&c";
                        if (percent.contains("50")) formatting = "&b";
                        if (percent.contains("100")) formatting = "&a";
                        for (String s : entry.getValue()) {

                            heldItems.add(FancyTextHandler.getFormattedString(formatting + percent + " -> " + s));

                        }

                    }

                } else {

                    heldItems.add(FancyTextHandler.getFormattedString("&cNone"));

                }
                configLore.addAll(heldItems);

            }
            List<Text> lore = new ArrayList<>();
            for (String l : configLore) {

                lore.add(FancyTextHandler.getFormattedText(l
                        .replace("%form%", form)
                        .replace("%levelRange%", levelRange)
                        .replace("%time%", String.join(", ", timeList))
                        .replace("%weather%", String.join(", ", weatherList))
                        .replace("%spawner%", "Fish Spawner")
                ));

            }
            sprite.set(DataComponentTypes.LORE, new LoreComponent(lore));
            UUID rand = UUID.randomUUID();
            m1.put(rand, p);
            m2.put(p, sprite);
            m3.put(p.getSpecies().getNationalPokedexNumber(), rand);

        }

    }

    public static void buildHeadbutt (AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (HeadbuttSpawn headbutt : spawns.getHeadbuttSpawns()) {

            String speciesName = headbutt.getSpeciesName();
            String form = headbutt.getForm();
            Pokemon p = headbutt.buildAndGetPokemon();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(headbutt.getSpecies().getFormByName(form));

            }
            List<String> timeList = new ArrayList<>();
            List<String> weatherList = new ArrayList<>();
            List<String> locationList = new ArrayList<>();
            List<String> woodTypes = new ArrayList<>();
            String levelRange = headbutt.getMinLevel() + " - " + headbutt.getMaxLevel();
            Map<String, Map<String, Map<String, String>>> data = headbutt.getSpawnData();
            for (Map.Entry<String, Map<String, Map<String, String>>> d1 : data.entrySet()) {

                String time = d1.getKey();
                timeList.add(time);
                Map<String, Map<String, String>> data2 = d1.getValue();
                for (Map.Entry<String, Map<String, String>> d2 : data2.entrySet()) {

                    String weather = d2.getKey();
                    if (!weatherList.contains(weather)) weatherList.add(weather);
                    Map<String, String> data3 = d2.getValue();
                    String woodType = data3.get("Wood-Types");
                    if (!woodTypes.contains(woodType)) woodTypes.add(woodType);

                }

            }
            ItemStack sprite = PokemonItem.from(p);
            sprite.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
            List<String> configLore = new ArrayList<>(ConfigGetters.allSpawnsMenuFormatLore);
            configLore.removeIf(e -> e.contains("Location"));
            configLore.removeIf(e -> e.contains("Rod Types"));
            configLore.removeIf(e -> e.contains("Stone Types"));
            List<String> heldItems = new ArrayList<>();
            boolean doHeldItems = false;
            for (String s : configLore) {

                if (s.contains("%heldItems%")) {

                    doHeldItems = true;
                    break;

                }

            }
            if (doHeldItems) {

                configLore.removeIf(e -> e.contains("%heldItems%"));
                if (HeldItemUtils.heldItemMap.containsKey(speciesName.toLowerCase())) {

                    Map<String, List<String>> possibleItems = HeldItemUtils.heldItemMap.get(speciesName.toLowerCase());
                    for (Map.Entry<String, List<String>> entry : possibleItems.entrySet()) {

                        String percent = entry.getKey();
                        String formatting = "&c";
                        if (percent.contains("1")) formatting = "&4&l";
                        if (percent.contains("5")) formatting = "&e&l";
                        if (percent.contains("30")) formatting = "&c";
                        if (percent.contains("50")) formatting = "&b";
                        if (percent.contains("100")) formatting = "&a";
                        for (String s : entry.getValue()) {

                            heldItems.add(FancyTextHandler.getFormattedString(formatting + percent + " -> " + s));

                        }

                    }

                } else {

                    heldItems.add(FancyTextHandler.getFormattedString("&cNone"));

                }
                configLore.addAll(heldItems);

            }
            List<Text> lore = new ArrayList<>();
            for (String l : configLore) {

                lore.add(FancyTextHandler.getFormattedText(l
                        .replace("%form%", form)
                        .replace("%levelRange%", levelRange)
                        .replace("%time%", String.join(", ", timeList))
                        .replace("%weather%", String.join(", ", weatherList))
                        .replace("%location%", String.join(", ", locationList))
                        .replace("%spawner%", "Headbutt Spawner")
                        .replace("%woodTypes%", String.join("\n", woodTypes))
                ));

            }
            sprite.set(DataComponentTypes.LORE, new LoreComponent(lore));
            UUID rand = UUID.randomUUID();
            m1.put(rand, p);
            m2.put(p, sprite);
            m3.put(p.getSpecies().getNationalPokedexNumber(), rand);

        }

    }

    public static void buildRockSmash (AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (RockSmashSpawn rock : spawns.getRockSmashSpawns()) {

            String speciesName = rock.getSpeciesName();
            String form = rock.getForm();
            Pokemon p = rock.buildAndGetPokemon();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(rock.getSpecies().getFormByName(form));

            }
            List<String> timeList = new ArrayList<>();
            List<String> weatherList = new ArrayList<>();
            List<String> locationList = new ArrayList<>();
            List<String> stoneTypes = new ArrayList<>();
            String levelRange = rock.getMinLevel() + " - " + rock.getMaxLevel();
            Map<String, Map<String, Map<String, String>>> data = rock.getSpawnData();
            for (Map.Entry<String, Map<String, Map<String, String>>> d1 : data.entrySet()) {

                String time = d1.getKey();
                timeList.add(time);
                Map<String, Map<String, String>> data2 = d1.getValue();
                for (Map.Entry<String, Map<String, String>> d2 : data2.entrySet()) {

                    String weather = d2.getKey();
                    if (!weatherList.contains(weather)) weatherList.add(weather);
                    Map<String, String> data3 = d2.getValue();
                    String stoneType = data3.get("Stone-Types");
                    if (!stoneTypes.contains(stoneType)) stoneTypes.add(stoneType);

                }

            }
            ItemStack sprite = PokemonItem.from(p);
            sprite.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
            List<String> configLore = new ArrayList<>(ConfigGetters.allSpawnsMenuFormatLore);
            configLore.removeIf(e -> e.contains("Location"));
            configLore.removeIf(e -> e.contains("Rod Types"));
            configLore.removeIf(e -> e.contains("Wood Types"));
            List<String> heldItems = new ArrayList<>();
            boolean doHeldItems = false;
            for (String s : configLore) {

                if (s.contains("%heldItems%")) {

                    doHeldItems = true;
                    break;

                }

            }
            if (doHeldItems) {

                configLore.removeIf(e -> e.contains("%heldItems%"));
                if (HeldItemUtils.heldItemMap.containsKey(speciesName.toLowerCase())) {

                    Map<String, List<String>> possibleItems = HeldItemUtils.heldItemMap.get(speciesName.toLowerCase());
                    for (Map.Entry<String, List<String>> entry : possibleItems.entrySet()) {

                        String percent = entry.getKey();
                        String formatting = "&c";
                        if (percent.contains("1")) formatting = "&4&l";
                        if (percent.contains("5")) formatting = "&e&l";
                        if (percent.contains("30")) formatting = "&c";
                        if (percent.contains("50")) formatting = "&b";
                        if (percent.contains("100")) formatting = "&a";
                        for (String s : entry.getValue()) {

                            heldItems.add(FancyTextHandler.getFormattedString(formatting + percent + " -> " + s));

                        }

                    }

                } else {

                    heldItems.add(FancyTextHandler.getFormattedString("&cNone"));

                }
                configLore.addAll(heldItems);

            }
            List<Text> lore = new ArrayList<>();
            for (String l : configLore) {

                lore.add(FancyTextHandler.getFormattedText(l
                        .replace("%form%", form)
                        .replace("%levelRange%", levelRange)
                        .replace("%time%", String.join(", ", timeList))
                        .replace("%weather%", String.join(", ", weatherList))
                        .replace("%location%", String.join(", ", locationList))
                        .replace("%spawner%", "Rock Smash Spawner")
                        .replace("%stoneTypes%", String.join("\n", stoneTypes))
                ));

            }
            sprite.set(DataComponentTypes.LORE, new LoreComponent(lore));
            UUID rand = UUID.randomUUID();
            m1.put(rand, p);
            m2.put(p, sprite);
            m3.put(p.getSpecies().getNationalPokedexNumber(), rand);

        }

    }

    public static void buildGrass (AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (GrassSpawn grass : spawns.getGrassSpawns()) {

            String speciesName = grass.getSpeciesName();
            String form = grass.getForm();
            Pokemon p = grass.buildAndGetPokemon();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(grass.getSpecies().getFormByName(form));

            }
            List<String> timeList = new ArrayList<>();
            List<String> weatherList = new ArrayList<>();
            List<String> locationList = new ArrayList<>();
            String levelRange = grass.getMinLevel() + " - " + grass.getMaxLevel();
            Map<String, Map<String, Map<String, String>>> data = grass.getSpawnData();
            for (Map.Entry<String, Map<String, Map<String, String>>> d1 : data.entrySet()) {

                String time = d1.getKey();
                timeList.add(time);
                Map<String, Map<String, String>> data2 = d1.getValue();
                for (Map.Entry<String, Map<String, String>> d2 : data2.entrySet()) {

                    String weather = d2.getKey();
                    if (!weatherList.contains(weather)) weatherList.add(weather);
                    Map<String, String> data3 = d2.getValue();
                    String location = data3.get("Spawn-Location");
                    if (!locationList.contains(location)) locationList.add(location);

                }

            }
            ItemStack sprite = PokemonItem.from(p);
            sprite.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
            List<String> configLore = new ArrayList<>(ConfigGetters.allSpawnsMenuFormatLore);
            configLore.removeIf(e -> e.contains("Rod Types"));
            configLore.removeIf(e -> e.contains("Wood Types"));
            configLore.removeIf(e -> e.contains("Stone Types"));
            List<String> heldItems = new ArrayList<>();
            boolean doHeldItems = false;
            for (String s : configLore) {

                if (s.contains("%heldItems%")) {

                    doHeldItems = true;
                    break;

                }

            }
            if (doHeldItems) {

                configLore.removeIf(e -> e.contains("%heldItems%"));
                if (HeldItemUtils.heldItemMap.containsKey(speciesName.toLowerCase())) {

                    Map<String, List<String>> possibleItems = HeldItemUtils.heldItemMap.get(speciesName.toLowerCase());
                    for (Map.Entry<String, List<String>> entry : possibleItems.entrySet()) {

                        String percent = entry.getKey();
                        String formatting = "&c";
                        if (percent.contains("1")) formatting = "&4&l";
                        if (percent.contains("5")) formatting = "&e&l";
                        if (percent.contains("30")) formatting = "&c";
                        if (percent.contains("50")) formatting = "&b";
                        if (percent.contains("100")) formatting = "&a";
                        for (String s : entry.getValue()) {

                            heldItems.add(FancyTextHandler.getFormattedString(formatting + percent + " -> " + s));

                        }

                    }

                } else {

                    heldItems.add(FancyTextHandler.getFormattedString("&cNone"));

                }
                configLore.addAll(heldItems);

            }
            List<Text> lore = new ArrayList<>();
            for (String l : configLore) {

                lore.add(FancyTextHandler.getFormattedText(l
                        .replace("%form%", form)
                        .replace("%levelRange%", levelRange)
                        .replace("%time%", String.join(", ", timeList))
                        .replace("%weather%", String.join(", ", weatherList))
                        .replace("%location%", String.join(", ", locationList))
                        .replace("%spawner%", "Grass Spawner")
                ));

            }
            sprite.set(DataComponentTypes.LORE, new LoreComponent(lore));
            UUID rand = UUID.randomUUID();
            m1.put(rand, p);
            m2.put(p, sprite);
            m3.put(p.getSpecies().getNationalPokedexNumber(), rand);

        }

    }

    public static void buildSurf (AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (SurfSpawn surf : spawns.getSurfSpawns()) {

            String speciesName = surf.getSpeciesName();
            String form = surf.getForm();
            Pokemon p = surf.buildAndGetPokemon();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(surf.getSpecies().getFormByName(form));

            }
            List<String> timeList = new ArrayList<>();
            List<String> weatherList = new ArrayList<>();
            String levelRange = surf.getMinLevel() + " - " + surf.getMaxLevel();
            Map<String, Map<String, Map<String, String>>> data = surf.getSpawnData();
            for (Map.Entry<String, Map<String, Map<String, String>>> d1 : data.entrySet()) {

                String time = d1.getKey();
                timeList.add(time);
                Map<String, Map<String, String>> data2 = d1.getValue();
                for (Map.Entry<String, Map<String, String>> d2 : data2.entrySet()) {

                    String weather = d2.getKey();
                    if (!weatherList.contains(weather)) weatherList.add(weather);

                }

            }
            ItemStack sprite = PokemonItem.from(p);
            sprite.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
            List<String> configLore = new ArrayList<>(ConfigGetters.allSpawnsMenuFormatLore);
            configLore.removeIf(e -> e.contains("Rod Types"));
            configLore.removeIf(e -> e.contains("Wood Types"));
            configLore.removeIf(e -> e.contains("Stone Types"));
            List<String> heldItems = new ArrayList<>();
            boolean doHeldItems = false;
            for (String s : configLore) {

                if (s.contains("%heldItems%")) {

                    doHeldItems = true;
                    break;

                }

            }
            if (doHeldItems) {

                configLore.removeIf(e -> e.contains("%heldItems%"));
                if (HeldItemUtils.heldItemMap.containsKey(speciesName.toLowerCase())) {

                    Map<String, List<String>> possibleItems = HeldItemUtils.heldItemMap.get(speciesName.toLowerCase());
                    for (Map.Entry<String, List<String>> entry : possibleItems.entrySet()) {

                        String percent = entry.getKey();
                        String formatting = "&c";
                        if (percent.contains("1")) formatting = "&4&l";
                        if (percent.contains("5")) formatting = "&e&l";
                        if (percent.contains("30")) formatting = "&c";
                        if (percent.contains("50")) formatting = "&b";
                        if (percent.contains("100")) formatting = "&a";
                        for (String s : entry.getValue()) {

                            heldItems.add(FancyTextHandler.getFormattedString(formatting + percent + " -> " + s));

                        }

                    }

                } else {

                    heldItems.add(FancyTextHandler.getFormattedString("&cNone"));

                }
                configLore.addAll(heldItems);

            }
            List<Text> lore = new ArrayList<>();
            for (String l : configLore) {

                lore.add(FancyTextHandler.getFormattedText(l
                        .replace("%form%", form)
                        .replace("%levelRange%", levelRange)
                        .replace("%time%", String.join(", ", timeList))
                        .replace("%weather%", String.join(", ", weatherList))
                        .replace("%location%", "water")
                        .replace("%spawner%", "Surf Spawner")
                ));

            }
            sprite.set(DataComponentTypes.LORE, new LoreComponent(lore));
            UUID rand = UUID.randomUUID();
            m1.put(rand, p);
            m2.put(p, sprite);
            m3.put(p.getSpecies().getNationalPokedexNumber(), rand);

        }

    }

}
