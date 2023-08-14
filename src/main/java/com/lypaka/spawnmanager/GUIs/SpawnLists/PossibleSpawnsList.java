package com.lypaka.spawnmanager.GUIs.SpawnLists;

import com.lypaka.spawnmanager.ConfigGetters;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.*;
import com.lypaka.spawnmanager.Utils.HeldItemUtils;
import com.lypaka.lypakautils.FancyText;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.util.helpers.SpriteItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PossibleSpawnsList {

    public static void buildNatural (String playerTime, String playerWeather, String playerLocation, AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (NaturalSpawn natural : spawns.getNaturalSpawns()) {

            String speciesName = natural.getSpecies();
            String form = natural.getForm();
            Pokemon p = PokemonBuilder.builder().species(speciesName).build();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(form);

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

                                ItemStack sprite = SpriteItemHelper.getPhoto(p);
                                sprite.setDisplayName(FancyText.getFormattedText(ConfigGetters.possibleSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
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
                                            if (percent.contains("50")) formatting = "&b";
                                            if (percent.contains("100")) formatting = "&a";
                                            for (String s : entry.getValue()) {

                                                heldItems.add(FancyText.getFormattedString(formatting + percent + " -> " + s));

                                            }

                                        }

                                    } else {

                                        heldItems.add(FancyText.getFormattedString("&cNone"));

                                    }
                                    configLore.addAll(heldItems);

                                }
                                ListNBT lore = new ListNBT();
                                for (String l : configLore) {

                                    lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(l
                                            .replace("%form%", form)
                                            .replace("%levelRange%", levelRange)
                                            .replace("%spawner%", "Natural Spawner")
                                    ))));

                                }
                                sprite.getOrCreateChildTag("display").put("Lore", lore);
                                UUID rand = UUID.randomUUID();
                                m1.put(rand, p);
                                m2.put(p, sprite);
                                m3.put(p.getSpecies().getDex(), rand);

                            }

                        }

                    }

                }

            }

        }

    }

    public static void buildFish (String playerTime, String playerWeather, AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (FishSpawn fish : spawns.getFishSpawns()) {

            String speciesName = fish.getSpecies();
            String form = fish.getForm();
            Pokemon p = PokemonBuilder.builder().species(speciesName).build();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(form);

            }
            List<String> rodList = new ArrayList<>();
            String levelRange = fish.getMinLevel() + " - " + fish.getMaxLevel();
            Map<String, Map<String, Map<String, Map<String, String>>>> data = fish.getSpawnData();
            for (Map.Entry<String, Map<String, Map<String, Map<String, String>>>> d1 : data.entrySet()) {

                String rod = d1.getKey();
                rodList.add(rod);
                Map<String, Map<String, Map<String, String>>> data2 = d1.getValue();
                for (Map.Entry<String, Map<String, Map<String, String>>> d2 : data2.entrySet()) {

                    String time = d2.getKey();
                    if (time.equalsIgnoreCase(playerTime) || time.equalsIgnoreCase("Any")) {

                        Map<String, Map<String, String>> data3 = d2.getValue();
                        for (Map.Entry<String, Map<String, String>> d3 : data3.entrySet()) {

                            String weather = d3.getKey();
                            if (weather.equalsIgnoreCase(playerWeather) || weather.equalsIgnoreCase("Any")) {

                                ItemStack sprite = SpriteItemHelper.getPhoto(p);
                                sprite.setDisplayName(FancyText.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
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

                                                heldItems.add(FancyText.getFormattedString(formatting + percent + " -> " + s));

                                            }

                                        }

                                    } else {

                                        heldItems.add(FancyText.getFormattedString("&cNone"));

                                    }
                                    configLore.addAll(heldItems);

                                }
                                ListNBT lore = new ListNBT();
                                for (String l : configLore) {

                                    lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(l
                                            .replace("%form%", form)
                                            .replace("%levelRange%", levelRange)
                                            .replace("%spawner%", "Fish Spawner")
                                            .replace("%rodTypes%", String.join(", ", rodList))
                                    ))));

                                }
                                sprite.getOrCreateChildTag("display").put("Lore", lore);
                                UUID rand = UUID.randomUUID();
                                m1.put(rand, p);
                                m2.put(p, sprite);
                                m3.put(p.getSpecies().getDex(), rand);

                            }

                        }

                    }

                }

            }

        }

    }

    public static void buildHeadbutt (String playerTime, String playerWeather, AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (HeadbuttSpawn headbutt : spawns.getHeadbuttSpawns()) {

            String speciesName = headbutt.getSpecies();
            String form = headbutt.getForm();
            Pokemon p = PokemonBuilder.builder().species(speciesName).build();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(form);

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
                            ItemStack sprite = SpriteItemHelper.getPhoto(p);
                            sprite.setDisplayName(FancyText.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
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
                                        if (percent.contains("50")) formatting = "&b";
                                        if (percent.contains("100")) formatting = "&a";
                                        for (String s : entry.getValue()) {

                                            heldItems.add(FancyText.getFormattedString(formatting + percent + " -> " + s));

                                        }

                                    }

                                } else {

                                    heldItems.add(FancyText.getFormattedString("&cNone"));

                                }
                                configLore.addAll(heldItems);

                            }
                            ListNBT lore = new ListNBT();
                            for (String l : configLore) {

                                lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(l
                                        .replace("%form%", form)
                                        .replace("%levelRange%", levelRange)
                                        .replace("%spawner%", "Headbutt Spawner")
                                        .replace("%woodTypes%", String.join("\n", woodTypes))
                                ))));

                            }
                            sprite.getOrCreateChildTag("display").put("Lore", lore);
                            UUID rand = UUID.randomUUID();
                            m1.put(rand, p);
                            m2.put(p, sprite);
                            m3.put(p.getSpecies().getDex(), rand);

                        }

                    }

                }

            }

        }

    }

    public static void buildRockSmash (String playerTime, String playerWeather, AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (RockSmashSpawn rock : spawns.getRockSmashSpawns()) {

            String speciesName = rock.getSpecies();
            String form = rock.getForm();
            Pokemon p = PokemonBuilder.builder().species(speciesName).build();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(form);

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
                            ItemStack sprite = SpriteItemHelper.getPhoto(p);
                            sprite.setDisplayName(FancyText.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
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
                                        if (percent.contains("50")) formatting = "&b";
                                        if (percent.contains("100")) formatting = "&a";
                                        for (String s : entry.getValue()) {

                                            heldItems.add(FancyText.getFormattedString(formatting + percent + " -> " + s));

                                        }

                                    }

                                } else {

                                    heldItems.add(FancyText.getFormattedString("&cNone"));

                                }
                                configLore.addAll(heldItems);

                            }
                            ListNBT lore = new ListNBT();
                            for (String l : configLore) {

                                lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(l
                                        .replace("%form%", form)
                                        .replace("%levelRange%", levelRange)
                                        .replace("%spawner%", "Rock Smash Spawner")
                                        .replace("%stoneTypes%", String.join("\n", stoneTypes))
                                ))));

                            }
                            sprite.getOrCreateChildTag("display").put("Lore", lore);
                            UUID rand = UUID.randomUUID();
                            m1.put(rand, p);
                            m2.put(p, sprite);
                            m3.put(p.getSpecies().getDex(), rand);

                        }

                    }

                }

            }

        }

    }

    public static void buildGrass (String playerTime, String playerWeather, String playerLocation, AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (GrassSpawn grass : spawns.getGrassSpawns()) {

            String speciesName = grass.getSpecies();
            String form = grass.getForm();
            Pokemon p = PokemonBuilder.builder().species(speciesName).build();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(form);

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

                                ItemStack sprite = SpriteItemHelper.getPhoto(p);
                                sprite.setDisplayName(FancyText.getFormattedText(ConfigGetters.possibleSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
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
                                            if (percent.contains("50")) formatting = "&b";
                                            if (percent.contains("100")) formatting = "&a";
                                            for (String s : entry.getValue()) {

                                                heldItems.add(FancyText.getFormattedString(formatting + percent + " -> " + s));

                                            }

                                        }

                                    } else {

                                        heldItems.add(FancyText.getFormattedString("&cNone"));

                                    }
                                    configLore.addAll(heldItems);

                                }
                                ListNBT lore = new ListNBT();
                                for (String l : configLore) {

                                    lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(l
                                            .replace("%form%", form)
                                            .replace("%levelRange%", levelRange)
                                            .replace("%spawner%", "Grass Spawner")
                                    ))));

                                }
                                sprite.getOrCreateChildTag("display").put("Lore", lore);
                                UUID rand = UUID.randomUUID();
                                m1.put(rand, p);
                                m2.put(p, sprite);
                                m3.put(p.getSpecies().getDex(), rand);

                            }

                        }

                    }

                }

            }

        }

    }

    public static void buildSurf (String playerTime, String playerWeather, AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (SurfSpawn surf : spawns.getSurfSpawns()) {

            String speciesName = surf.getSpecies();
            String form = surf.getForm();
            Pokemon p = PokemonBuilder.builder().species(speciesName).build();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(form);

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

                            ItemStack sprite = SpriteItemHelper.getPhoto(p);
                            sprite.setDisplayName(FancyText.getFormattedText(ConfigGetters.possibleSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
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
                                        if (percent.contains("50")) formatting = "&b";
                                        if (percent.contains("100")) formatting = "&a";
                                        for (String s : entry.getValue()) {

                                            heldItems.add(FancyText.getFormattedString(formatting + percent + " -> " + s));

                                        }

                                    }

                                } else {

                                    heldItems.add(FancyText.getFormattedString("&cNone"));

                                }
                                configLore.addAll(heldItems);

                            }
                            ListNBT lore = new ListNBT();
                            for (String l : configLore) {

                                lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(l
                                        .replace("%form%", form)
                                        .replace("%levelRange%", levelRange)
                                        .replace("%spawner%", "Surf Spawner")
                                ))));

                            }
                            sprite.getOrCreateChildTag("display").put("Lore", lore);
                            UUID rand = UUID.randomUUID();
                            m1.put(rand, p);
                            m2.put(p, sprite);
                            m3.put(p.getSpecies().getDex(), rand);

                        }

                    }

                }

            }

        }

    }

}
