package com.lypaka.spawnmanager.GUIs.SpawnLists;

import com.lypaka.spawnmanager.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.*;
import com.lypaka.spawnmanager.Utils.HeldItemUtils;
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

public class AllSpawnsList {

    public static void buildNatural (AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (NaturalSpawn natural : spawns.getNaturalSpawns()) {

            String speciesName = natural.getSpecies();
            String form = natural.getForm();
            Pokemon p = PokemonBuilder.builder().species(speciesName).build();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(form);

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
            ItemStack sprite = SpriteItemHelper.getPhoto(p);
            sprite.setDisplayName(FancyText.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
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
                        .replace("%time%", String.join(", ", timeList))
                        .replace("%weather%", String.join(", ", weatherList))
                        .replace("%location%", String.join(", ", locationList))
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

    public static void buildFish (AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (FishSpawn fish : spawns.getFishSpawns()) {

            String speciesName = fish.getSpecies();
            String form = fish.getForm();
            Pokemon p = PokemonBuilder.builder().species(speciesName).build();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(form);

            }
            List<String> rodList = new ArrayList<>();
            List<String> timeList = new ArrayList<>();
            List<String> weatherList = new ArrayList<>();
            String levelRange = fish.getMinLevel() + " - " + fish.getMaxLevel();
            Map<String, Map<String, Map<String, Map<String, String>>>> data = fish.getSpawnData();
            for (Map.Entry<String, Map<String, Map<String, Map<String, String>>>> d1 : data.entrySet()) {

                String rod = d1.getKey();
                rodList.add(rod);
                Map<String, Map<String, Map<String, String>>> data2 = d1.getValue();
                for (Map.Entry<String, Map<String, Map<String, String>>> d2 : data2.entrySet()) {

                    String time = d2.getKey();
                    if (!timeList.contains(time)) timeList.add(time);
                    Map<String, Map<String, String>> data3 = d2.getValue();
                    for (Map.Entry<String, Map<String, String>> d3 : data3.entrySet()) {

                        String weather = d3.getKey();
                        if (!weatherList.contains(weather)) weatherList.add(weather);

                    }

                }

            }
            ItemStack sprite = SpriteItemHelper.getPhoto(p);
            sprite.setDisplayName(FancyText.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
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
                        .replace("%time%", String.join(", ", timeList))
                        .replace("%weather%", String.join(", ", weatherList))
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

    public static void buildHeadbutt (AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (HeadbuttSpawn headbutt : spawns.getHeadbuttSpawns()) {

            String speciesName = headbutt.getSpecies();
            String form = headbutt.getForm();
            Pokemon p = PokemonBuilder.builder().species(speciesName).build();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(form);

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
            ItemStack sprite = SpriteItemHelper.getPhoto(p);
            sprite.setDisplayName(FancyText.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
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
                        .replace("%time%", String.join(", ", timeList))
                        .replace("%weather%", String.join(", ", weatherList))
                        .replace("%location%", String.join(", ", locationList))
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

    public static void buildRockSmash (AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (RockSmashSpawn rock : spawns.getRockSmashSpawns()) {

            String speciesName = rock.getSpecies();
            String form = rock.getForm();
            Pokemon p = PokemonBuilder.builder().species(speciesName).build();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(form);

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
            ItemStack sprite = SpriteItemHelper.getPhoto(p);
            sprite.setDisplayName(FancyText.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
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
                        .replace("%time%", String.join(", ", timeList))
                        .replace("%weather%", String.join(", ", weatherList))
                        .replace("%location%", String.join(", ", locationList))
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

    public static void buildGrass (AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (GrassSpawn grass : spawns.getGrassSpawns()) {

            String speciesName = grass.getSpecies();
            String form = grass.getForm();
            Pokemon p = PokemonBuilder.builder().species(speciesName).build();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(form);

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
            ItemStack sprite = SpriteItemHelper.getPhoto(p);
            sprite.setDisplayName(FancyText.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
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
                        .replace("%time%", String.join(", ", timeList))
                        .replace("%weather%", String.join(", ", weatherList))
                        .replace("%location%", String.join(", ", locationList))
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

    public static void buildSurf (AreaSpawns spawns, Map<UUID, Pokemon> m1, Map<Pokemon, ItemStack> m2, Map<Integer, UUID> m3) {

        for (SurfSpawn surf : spawns.getSurfSpawns()) {

            String speciesName = surf.getSpecies();
            String form = surf.getForm();
            Pokemon p = PokemonBuilder.builder().species(speciesName).build();
            if (!form.equalsIgnoreCase("default")) {

                p.setForm(form);

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
            ItemStack sprite = SpriteItemHelper.getPhoto(p);
            sprite.setDisplayName(FancyText.getFormattedText(ConfigGetters.allSpawnsMenuFormatName.replace("%pokemonName%", p.getSpecies().getName())));
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
                        .replace("%time%", String.join(", ", timeList))
                        .replace("%weather%", String.join(", ", weatherList))
                        .replace("%location%", "water")
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
