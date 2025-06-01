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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PossibleSpawnsList {

    public static void buildNatural (String playerTime, String playerWeather, String playerLocation, AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (NaturalSpawn natural : spawns.getNaturalSpawns()) {

            String speciesName = natural.getSpeciesName();
            String form = natural.getForm();
            Pokemon p = natural.buildAndGetPokemon();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(natural.getSpecies().getFormByName(form));

            }
            String levelRange = natural.getMinLevel() + " - " + natural.getMaxLevel();
            Map<String, Map<String, Map<String, String>>> data = natural.getSpawnData();
            for (Map.Entry<String, Map<String, Map<String, String>>> d1 : data.entrySet()) {

                String time = d1.getKey();
                if (time.equalsIgnoreCase(playerTime)) {

                    Map<String, Map<String, String>> data2 = d1.getValue();
                    for (Map.Entry<String, Map<String, String>> d2 : data2.entrySet()) {

                        String weather = d2.getKey();
                        if (weather.equalsIgnoreCase(playerWeather)) {

                            Map<String, String> data3 = d2.getValue();
                            String location = data3.get("Spawn-Location");
                            if (location.contains(playerLocation)) {

                                double spawnChance = Double.parseDouble(d2.getValue().get("Spawn-Chance"));
                                DecimalFormat df = new DecimalFormat("#.##");
                                String spawnChanceDisplay = df.format(spawnChance * 100) + "%";
                                ItemStack sprite = PokemonItem.from(p);
                                sprite.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.possibleSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
                                List<String> configLore = new ArrayList<>(ConfigGetters.possibleSpawnsMenuFormatLore);
                                configLore.removeIf(e -> e.contains("Wood Types"));
                                configLore.removeIf(e -> e.contains("Stone Types"));
                                configLore.removeIf(e -> e.contains("Time")); // removing time, weather and location because that information is irrelevent here
                                configLore.removeIf(e -> e.contains("Weather"));
                                configLore.removeIf(e -> e.contains("Location"));
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
                                            .replace("%spawner%", "Natural Spawner")
                                            .replace("%spawnChance%", spawnChanceDisplay)
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

                }

            }

        }

    }

    public static void buildFish (String playerTime, String playerWeather, AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (FishSpawn fish : spawns.getFishSpawns()) {

            String speciesName = fish.getSpeciesName();
            String form = fish.getForm();
            Pokemon p = fish.buildAndGetPokemon();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(fish.getSpecies().getFormByName(form));

            }
            List<String> rodList = new ArrayList<>();
            String levelRange = fish.getMinLevel() + " - " + fish.getMaxLevel();
            Map<String, Map<String, Map<String, String>>> data = fish.getSpawnData();
            for (Map.Entry<String, Map<String, Map<String, String>>> d1 : data.entrySet()) {

                String time = d1.getKey();
                if (time.equalsIgnoreCase(playerTime) || time.equalsIgnoreCase("Any")) {

                    Map<String, Map<String, String>> data2 = d1.getValue();
                    for (Map.Entry<String, Map<String, String>> d2 : data2.entrySet()) {
                        
                        String weather = d2.getKey();
                        if (weather.equalsIgnoreCase(playerWeather) || weather.equalsIgnoreCase("Any")) {

                            double spawnChance = Double.parseDouble(d2.getValue().get("Spawn-Chance"));
                            DecimalFormat df = new DecimalFormat("#.##");
                            String spawnChanceDisplay = df.format(spawnChance * 100) + "%";
                            ItemStack sprite = PokemonItem.from(p);
                            sprite.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
                            List<String> configLore = new ArrayList<>(ConfigGetters.allSpawnsMenuFormatLore);
                            configLore.removeIf(e -> e.contains("Location"));
                            configLore.removeIf(e -> e.contains("Wood Types"));
                            configLore.removeIf(e -> e.contains("Stone Types"));
                            configLore.removeIf(e -> e.contains("Time")); // removing time, weather and location because that information is irrelevent here
                            configLore.removeIf(e -> e.contains("Weather"));
                            configLore.removeIf(e -> e.contains("Location"));
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
                                        .replace("%spawner%", "Fish Spawner")
                                        .replace("%rodTypes%", String.join(", ", rodList))
                                        .replace("%spawnChance%", spawnChanceDisplay)
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

            }

        }

    }

    public static void buildHeadbutt (String playerTime, String playerWeather, AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (HeadbuttSpawn headbutt : spawns.getHeadbuttSpawns()) {

            String speciesName = headbutt.getSpeciesName();
            String form = headbutt.getForm();
            Pokemon p = headbutt.buildAndGetPokemon();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(headbutt.getSpecies().getFormByName(form));

            }
            List<String> woodTypes = new ArrayList<>();
            String levelRange = headbutt.getMinLevel() + " - " + headbutt.getMaxLevel();
            Map<String, Map<String, Map<String, String>>> data = headbutt.getSpawnData();
            for (Map.Entry<String, Map<String, Map<String, String>>> d1 : data.entrySet()) {

                String time = d1.getKey();
                if (time.equalsIgnoreCase(playerTime) || time.equalsIgnoreCase("Any")) {

                    Map<String, Map<String, String>> data2 = d1.getValue();
                    for (Map.Entry<String, Map<String, String>> d2 : data2.entrySet()) {

                        String weather = d2.getKey();
                        if (weather.equalsIgnoreCase(playerWeather) || weather.equalsIgnoreCase("Any")) {

                            Map<String, String> data3 = d2.getValue();
                            String woodType = data3.get("Wood-Types");
                            if (!woodTypes.contains(woodType)) woodTypes.add(woodType);
                            double spawnChance = Double.parseDouble(d2.getValue().get("Spawn-Chance"));
                            DecimalFormat df = new DecimalFormat("#.##");
                            String spawnChanceDisplay = df.format(spawnChance * 100) + "%";
                            ItemStack sprite = PokemonItem.from(p);
                            sprite.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
                            List<String> configLore = new ArrayList<>(ConfigGetters.allSpawnsMenuFormatLore);
                            configLore.removeIf(e -> e.contains("Location"));
                            configLore.removeIf(e -> e.contains("Rod Types"));
                            configLore.removeIf(e -> e.contains("Stone Types"));
                            configLore.removeIf(e -> e.contains("Time")); // removing time, weather and location because that information is irrelevent here
                            configLore.removeIf(e -> e.contains("Weather"));
                            configLore.removeIf(e -> e.contains("Location"));
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
                                        .replace("%spawner%", "Headbutt Spawner")
                                        .replace("%woodTypes%", String.join("\n", woodTypes))
                                        .replace("%spawnChance%", spawnChanceDisplay)
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

            }

        }

    }

    public static void buildRockSmash (String playerTime, String playerWeather, AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (RockSmashSpawn rock : spawns.getRockSmashSpawns()) {

            String speciesName = rock.getSpeciesName();
            String form = rock.getForm();
            Pokemon p = rock.buildAndGetPokemon();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(rock.getSpecies().getFormByName(form));

            }
            List<String> stoneTypes = new ArrayList<>();
            String levelRange = rock.getMinLevel() + " - " + rock.getMaxLevel();
            Map<String, Map<String, Map<String, String>>> data = rock.getSpawnData();
            for (Map.Entry<String, Map<String, Map<String, String>>> d1 : data.entrySet()) {

                String time = d1.getKey();
                if (time.equalsIgnoreCase(playerTime) || time.equalsIgnoreCase("Any")) {

                    Map<String, Map<String, String>> data2 = d1.getValue();
                    for (Map.Entry<String, Map<String, String>> d2 : data2.entrySet()) {

                        String weather = d2.getKey();
                        if (weather.equalsIgnoreCase(playerWeather) || weather.equalsIgnoreCase("Any")) {

                            Map<String, String> data3 = d2.getValue();
                            String stoneType = data3.get("Stone-Types");
                            if (!stoneTypes.contains(stoneType)) stoneTypes.add(stoneType);
                            double spawnChance = Double.parseDouble(d2.getValue().get("Spawn-Chance"));
                            DecimalFormat df = new DecimalFormat("#.##");
                            String spawnChanceDisplay = df.format(spawnChance * 100) + "%";
                            ItemStack sprite = PokemonItem.from(p);
                            sprite.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
                            List<String> configLore = new ArrayList<>(ConfigGetters.allSpawnsMenuFormatLore);
                            configLore.removeIf(e -> e.contains("Location"));
                            configLore.removeIf(e -> e.contains("Rod Types"));
                            configLore.removeIf(e -> e.contains("Wood Types"));
                            configLore.removeIf(e -> e.contains("Time")); // removing time, weather and location because that information is irrelevent here
                            configLore.removeIf(e -> e.contains("Weather"));
                            configLore.removeIf(e -> e.contains("Location"));
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
                                        .replace("%spawner%", "Rock Smash Spawner")
                                        .replace("%stoneTypes%", String.join("\n", stoneTypes))
                                        .replace("%spawnChance%", spawnChanceDisplay)
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

            }

        }

    }

    public static void buildGrass (String playerTime, String playerWeather, String playerLocation, AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (GrassSpawn grass : spawns.getGrassSpawns()) {

            String speciesName = grass.getSpeciesName();
            String form = grass.getForm();
            Pokemon p = grass.buildAndGetPokemon();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(grass.getSpecies().getFormByName(form));

            }
            String levelRange = grass.getMinLevel() + " - " + grass.getMaxLevel();
            Map<String, Map<String, Map<String, String>>> data = grass.getSpawnData();
            for (Map.Entry<String, Map<String, Map<String, String>>> d1 : data.entrySet()) {

                String time = d1.getKey();
                if (time.equalsIgnoreCase("Any") || time.equalsIgnoreCase(playerTime)) {

                    Map<String, Map<String, String>> data2 = d1.getValue();
                    for (Map.Entry<String, Map<String, String>> d2 : data2.entrySet()) {

                        String weather = d2.getKey();
                        if (weather.equalsIgnoreCase("Any") || weather.equalsIgnoreCase(playerWeather)) {

                            Map<String, String> data3 = d2.getValue();
                            String location = data3.get("Spawn-Location");
                            if (location.equalsIgnoreCase("Any") || location.contains(playerLocation)) {

                                double spawnChance = Double.parseDouble(d2.getValue().get("Spawn-Chance"));
                                DecimalFormat df = new DecimalFormat("#.##");
                                String spawnChanceDisplay = df.format(spawnChance * 100) + "%";
                                ItemStack sprite = PokemonItem.from(p);
                                sprite.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.possibleSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
                                List<String> configLore = new ArrayList<>(ConfigGetters.possibleSpawnsMenuFormatLore);
                                configLore.removeIf(e -> e.contains("Rod Types"));
                                configLore.removeIf(e -> e.contains("Wood Types"));
                                configLore.removeIf(e -> e.contains("Stone Types"));
                                configLore.removeIf(e -> e.contains("Time")); // removing time, weather and location because that information is irrelevent here
                                configLore.removeIf(e -> e.contains("Weather"));
                                configLore.removeIf(e -> e.contains("Location"));
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
                                            .replace("%spawner%", "Grass Spawner")
                                            .replace("%spawnChance%", spawnChanceDisplay)
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

                }

            }

        }

    }

    public static void buildSurf (String playerTime, String playerWeather, AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (SurfSpawn surf : spawns.getSurfSpawns()) {

            String speciesName = surf.getSpeciesName();
            String form = surf.getForm();
            Pokemon p = surf.buildAndGetPokemon();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(surf.getSpecies().getFormByName(form));

            }
            String levelRange = surf.getMinLevel() + " - " + surf.getMaxLevel();
            Map<String, Map<String, Map<String, String>>> data = surf.getSpawnData();
            for (Map.Entry<String, Map<String, Map<String, String>>> d1 : data.entrySet()) {

                String time = d1.getKey();
                if (time.equalsIgnoreCase(playerTime)) {

                    Map<String, Map<String, String>> data2 = d1.getValue();
                    for (Map.Entry<String, Map<String, String>> d2 : data2.entrySet()) {

                        String weather = d2.getKey();
                        if (weather.equalsIgnoreCase(playerWeather)) {

                            double spawnChance = Double.parseDouble(d2.getValue().get("Spawn-Chance"));
                            DecimalFormat df = new DecimalFormat("#.##");
                            String spawnChanceDisplay = df.format(spawnChance * 100) + "%";
                            ItemStack sprite = PokemonItem.from(p);
                            sprite.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.possibleSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
                            List<String> configLore = new ArrayList<>(ConfigGetters.possibleSpawnsMenuFormatLore);
                            configLore.removeIf(e -> e.contains("Rod Types"));
                            configLore.removeIf(e -> e.contains("Wood Types"));
                            configLore.removeIf(e -> e.contains("Stone Types"));
                            configLore.removeIf(e -> e.contains("Time")); // removing time, weather and location because that information is irrelevent here
                            configLore.removeIf(e -> e.contains("Weather"));
                            configLore.removeIf(e -> e.contains("Location"));
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
                                        .replace("%spawner%", "Surf Spawner")
                                        .replace("%spawnChance%", spawnChanceDisplay)
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

            }

        }

    }

}
