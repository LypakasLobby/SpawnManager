package com.lypaka.spawnmanager.Utils;

import com.lypaka.lypakautils.MiscHandlers.PixelmonHelpers;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.*;
import com.lypaka.spawnmanager.SpawnManager;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;

import java.util.*;

public class SpawnBuilder {

    public static Map<Pokemon, PokemonSpawn> getRockSmashSpawnInfo (String time, String weather, String blockID, AreaSpawns spawns) {

        List<RockSmashSpawn> rockSmashSpawns = spawns.getRockSmashSpawns();
        Map<Pokemon, PokemonSpawn> pokemonMap = new HashMap<>();
        if (rockSmashSpawns.size() == 0) return pokemonMap;
        for (RockSmashSpawn h : rockSmashSpawns) {

            Map<String, Map<String, Map<String, String>>> spawnData = h.getSpawnData();
            Map<String, Map<String, String>> innerData;
            if (spawnData.containsKey(time)) {

                innerData = spawnData.get(time);

            } else if (spawnData.containsKey("Any")) {

                innerData = spawnData.get("Any");

            } else {

                continue;

            }

            Map<String, String> data;
            if (innerData.containsKey(weather)) {

                data = innerData.get(weather);

            } else if (innerData.containsKey("Any")) {

                data = innerData.get("Any");

            } else {

                continue;

            }

            String stoneTypes = "Any";
            if (data.containsKey("Stone-Types")) {

                stoneTypes = data.get("Stone-Types");

            }
            if (stoneTypes.equalsIgnoreCase("Any")) {

                int level = RandomHelper.getRandomNumberBetween(h.getMinLevel(), h.getMaxLevel());
                Pokemon pokemon = PokemonBuilder.builder().species(h.getSpecies()).level(level).build();
                if (!h.getForm().equalsIgnoreCase("")) {

                    pokemon.setForm(h.getForm());

                }
                pokemonMap.put(pokemon, h);

            } else {

                if (stoneTypes.contains(", ")) {

                    String[] split = stoneTypes.split(", ");
                    for (String s : split) {

                        if (s.equalsIgnoreCase(blockID)) {

                            int level = RandomHelper.getRandomNumberBetween(h.getMinLevel(), h.getMaxLevel());
                            Pokemon pokemon = PokemonBuilder.builder().species(h.getSpecies()).level(level).build();
                            if (!h.getForm().equalsIgnoreCase("")) {

                                pokemon.setForm(h.getForm());

                            }
                            pokemonMap.put(pokemon, h);
                            break;

                        }

                    }

                } else {

                    if (stoneTypes.equalsIgnoreCase(blockID)) {

                        int level = RandomHelper.getRandomNumberBetween(h.getMinLevel(), h.getMaxLevel());
                        Pokemon pokemon = PokemonBuilder.builder().species(h.getSpecies()).level(level).build();
                        if (!h.getForm().equalsIgnoreCase("")) {

                            pokemon.setForm(h.getForm());

                        }
                        pokemonMap.put(pokemon, h);

                    }

                }

            }

        }

        return pokemonMap;

    }

    public static Map<Pokemon, Double> buildRockSmashSpawns (String time, String weather, String blockID, AreaSpawns spawns, double modifier) {

        List<RockSmashSpawn> rockSmashSpawns = spawns.getRockSmashSpawns();
        Map<Double, List<RockSmashSpawn>> map = new HashMap<>();
        Map<Pokemon, Double> pokemonMap = new HashMap<>();
        if (rockSmashSpawns.size() == 0) return pokemonMap;
        for (RockSmashSpawn h : rockSmashSpawns) {

            Map<String, Map<String, Map<String, String>>> spawnData = h.getSpawnData();
            Map<String, Map<String, String>> innerData;
            if (spawnData.containsKey(time)) {

                innerData = spawnData.get(time);

            } else if (spawnData.containsKey("Any")) {

                innerData = spawnData.get("Any");

            } else {

                continue;

            }

            Map<String, String> data;
            if (innerData.containsKey(weather)) {

                data = innerData.get(weather);

            } else if (innerData.containsKey("Any")) {

                data = innerData.get("Any");

            } else {

                continue;

            }

            String stoneTypes = "Any";
            if (data.containsKey("Stone-Types")) {

                stoneTypes = data.get("Stone-Types");

            }
            double spawnChance = Double.parseDouble(data.get("Spawn-Chance")) * modifier;
            List<RockSmashSpawn> list = new ArrayList<>();
            if (map.containsKey(spawnChance)) list = map.get(spawnChance);
            if (stoneTypes.equalsIgnoreCase("Any")) {

                list.add(h);

            } else {

                if (stoneTypes.contains(", ")) {

                    String[] split = stoneTypes.split(", ");
                    for (String s : split) {

                        if (s.equalsIgnoreCase(blockID)) {

                            list.add(h);
                            break;

                        }

                    }

                } else {

                    if (stoneTypes.equalsIgnoreCase(blockID)) {

                        list.add(h);

                    }

                }

            }
            map.put(spawnChance, list);

        }

        List<Double> chances = new ArrayList<>(map.keySet());
        Collections.sort(chances);

        for (int i = chances.size() - 1; i >= 0; i--) {

            List<RockSmashSpawn> spawnList = map.get(chances.get(i));
            for (RockSmashSpawn spawn : spawnList) {

                int level = RandomHelper.getRandomNumberBetween(spawn.getMinLevel(), spawn.getMaxLevel());
                Pokemon pokemon = PixelmonHelpers.fixPokemonIVsAndGender(PokemonBuilder.builder().species(spawn.getSpecies()).level(level).build());
                if (!spawn.getForm().equalsIgnoreCase("")) {

                    pokemon.setForm(spawn.getForm());

                }

                pokemonMap.put(pokemon, chances.get(i));

            }

        }

        return pokemonMap;

    }

    public static Map<Pokemon, PokemonSpawn> getHeadbuttSpawnInfo (String time, String weather, String blockID, AreaSpawns spawns) {

        List<HeadbuttSpawn> headbuttSpawns = spawns.getHeadbuttSpawns();
        Map<Pokemon, PokemonSpawn> pokemonMap = new HashMap<>();
        if (headbuttSpawns.size() == 0) return pokemonMap;
        for (HeadbuttSpawn h : headbuttSpawns) {

            Map<String, Map<String, Map<String, String>>> spawnData = h.getSpawnData();
            Map<String, Map<String, String>> innerData;
            if (spawnData.containsKey(time)) {

                innerData = spawnData.get(time);

            } else if (spawnData.containsKey("Any")) {

                innerData = spawnData.get("Any");

            } else {

                continue;

            }

            Map<String, String> data;
            if (innerData.containsKey(weather)) {

                data = innerData.get(weather);

            } else if (innerData.containsKey("Any")) {

                data = innerData.get("Any");

            } else {

                continue;

            }

            String woodTypes = "Any";
            if (data.containsKey("Wood-Types")) {

                woodTypes = data.get("Wood-Types");

            }
            if (woodTypes.equalsIgnoreCase("Any")) {

                int level = RandomHelper.getRandomNumberBetween(h.getMinLevel(), h.getMaxLevel());
                Pokemon pokemon = PokemonBuilder.builder().species(h.getSpecies()).level(level).build();
                if (!h.getForm().equalsIgnoreCase("")) {

                    pokemon.setForm(h.getForm());

                }
                pokemonMap.put(pokemon, h);

            } else {

                if (woodTypes.contains(", ")) {

                    String[] split = woodTypes.split(", ");
                    for (String s : split) {

                        if (s.equalsIgnoreCase(blockID)) {

                            int level = RandomHelper.getRandomNumberBetween(h.getMinLevel(), h.getMaxLevel());
                            Pokemon pokemon = PokemonBuilder.builder().species(h.getSpecies()).level(level).build();
                            if (!h.getForm().equalsIgnoreCase("")) {

                                pokemon.setForm(h.getForm());

                            }
                            pokemonMap.put(pokemon, h);
                            break;

                        }

                    }

                } else {

                    if (woodTypes.equalsIgnoreCase(blockID)) {

                        int level = RandomHelper.getRandomNumberBetween(h.getMinLevel(), h.getMaxLevel());
                        Pokemon pokemon = PokemonBuilder.builder().species(h.getSpecies()).level(level).build();
                        if (!h.getForm().equalsIgnoreCase("")) {

                            pokemon.setForm(h.getForm());

                        }
                        pokemonMap.put(pokemon, h);

                    }

                }

            }

        }

        return pokemonMap;

    }

    public static Map<Pokemon, Double> buildHeadbuttSpawns (String time, String weather, String blockID, AreaSpawns spawns, double modifier) {

        List<HeadbuttSpawn> headbuttSpawns = spawns.getHeadbuttSpawns();
        Map<Double, List<HeadbuttSpawn>> map = new HashMap<>();
        Map<Pokemon, Double> pokemonMap = new HashMap<>();
        if (headbuttSpawns.size() == 0) return pokemonMap;
        for (HeadbuttSpawn h : headbuttSpawns) {

            Map<String, Map<String, Map<String, String>>> spawnData = h.getSpawnData();
            Map<String, Map<String, String>> innerData;
            if (spawnData.containsKey(time)) {

                innerData = spawnData.get(time);

            } else if (spawnData.containsKey("Any")) {

                innerData = spawnData.get("Any");

            } else {

                continue;

            }

            Map<String, String> data;
            if (innerData.containsKey(weather)) {

                data = innerData.get(weather);

            } else if (innerData.containsKey("Any")) {

                data = innerData.get("Any");

            } else {

                continue;

            }

            String woodTypes = "Any";
            if (data.containsKey("Wood-Types")) {

                woodTypes = data.get("Wood-Types");

            }
            double spawnChance = Double.parseDouble(data.get("Spawn-Chance"));
            spawnChance = spawnChance * modifier;
            List<HeadbuttSpawn> list = new ArrayList<>();
            if (map.containsKey(spawnChance)) list = map.get(spawnChance);

            if (woodTypes.equalsIgnoreCase("Any")) {

                list.add(h);

            } else {

                if (woodTypes.contains(", ")) {

                    String[] split = woodTypes.split(", ");
                    for (String s : split) {

                        if (s.equalsIgnoreCase(blockID)) {

                            list.add(h);
                            break;

                        }

                    }

                } else {

                    if (woodTypes.equalsIgnoreCase(blockID)) {

                        list.add(h);

                    }

                }

            }
            map.put(spawnChance, list);

        }

        List<Double> chances = new ArrayList<>(map.keySet());
        Collections.sort(chances);

        for (int i = chances.size() - 1; i >= 0; i--) {

            List<HeadbuttSpawn> spawnList = map.get(chances.get(i));
            for (HeadbuttSpawn spawn : spawnList) {

                int level = RandomHelper.getRandomNumberBetween(spawn.getMinLevel(), spawn.getMaxLevel());
                Pokemon pokemon = PixelmonHelpers.fixPokemonIVsAndGender(PokemonBuilder.builder().species(spawn.getSpecies()).level(level).build());
                if (!spawn.getForm().equalsIgnoreCase("")) {

                    pokemon.setForm(spawn.getForm());

                }

                pokemonMap.put(pokemon, chances.get(i));

            }

        }

        return pokemonMap;

    }

    public static Map<Pokemon, PokemonSpawn> getPokemonFishSpawnInfo (String rod, String time, String weather, AreaSpawns spawns) {

        List<FishSpawn> fishSpawns = spawns.getFishSpawns();
        Map<Pokemon, PokemonSpawn> pokemonMap = new HashMap<>();
        if (fishSpawns.size() == 0) return pokemonMap;
        for (FishSpawn f : fishSpawns) {

            int level = RandomHelper.getRandomNumberBetween(f.getMinLevel(), f.getMaxLevel());
            Pokemon pokemon = PokemonBuilder.builder().species(f.getSpecies()).level(level).build();
            if (!f.getForm().equalsIgnoreCase("")) {

                pokemon.setForm(f.getForm());

            }
            pokemonMap.put(pokemon, f);

        }

        return pokemonMap;

    }

    public static Map<Pokemon, Double> buildFishSpawns (String rod, String time, String weather, AreaSpawns spawns, double modifier) {

        List<FishSpawn> fishSpawns = spawns.getFishSpawns();
        Map<Pokemon, Double> pokemonMap = new HashMap<>();
        Map<Double, List<FishSpawn>> map = new HashMap<>();
        for (FishSpawn f : fishSpawns) {

            Map<String, Map<String, Map<String, Map<String, String>>>> spawnData = f.getSpawnData();
            Map<String, Map<String, Map<String, String>>> innerData;
            if (spawnData.containsKey(rod)) {

                innerData = spawnData.get(rod);

            } else if (spawnData.containsKey("Any")) {

                innerData = spawnData.get("Any");

            } else {

                continue;

            }

            Map<String, Map<String, String>> innerData2;
            if (innerData.containsKey(time)) {

                innerData2 = innerData.get(time);

            } else if (innerData.containsKey("Any")) {

                innerData2 = innerData.get("Any");

            } else {

                continue;

            }

            Map<String, String> data;
            if (innerData2.containsKey(weather)) {

                data = innerData2.get(weather);

            } else if (innerData2.containsKey("Any")) {

                data = innerData2.get("Any");

            } else {

                continue;

            }

            double spawnChance = Double.parseDouble(data.get("Spawn-Chance"));
            int level = RandomHelper.getRandomNumberBetween(f.getMinLevel(), f.getMaxLevel());
            Pokemon pokemon = PokemonBuilder.builder().species(f.getSpecies()).level(level).build();
            if (!f.getForm().equalsIgnoreCase("")) {

                pokemon.setForm(f.getForm());

            }
            spawnChance = spawnChance * modifier;
            List<FishSpawn> list = new ArrayList<>();
            if (map.containsKey(spawnChance)) {

                list = map.get(spawnChance);

            }
            list.add(f);
            map.put(spawnChance, list);

        }

        List<Double> chances = new ArrayList<>(map.keySet());
        Collections.sort(chances);

        for (int i = chances.size() - 1; i >= 0; i--) {

            if (RandomHelper.getRandomChance(chances.get(i))) {

                List<FishSpawn> spawnList = map.get(chances.get(i));
                for (FishSpawn spawn : spawnList) {

                    int level = RandomHelper.getRandomNumberBetween(spawn.getMinLevel(), spawn.getMaxLevel());
                    Pokemon pokemon = PixelmonHelpers.fixPokemonIVsAndGender(PokemonBuilder.builder().species(spawn.getSpecies()).level(level).build());
                    if (!spawn.getForm().equalsIgnoreCase("")) {

                        pokemon.setForm(spawn.getForm());

                    }

                    pokemonMap.put(pokemon, chances.get(i));

                }

            }

        }

        return pokemonMap;

    }

    public static Map<Pokemon, PokemonSpawn> getPokemonNaturalSpawnInfo (String time, String weather, String location, AreaSpawns spawns) {

        List<NaturalSpawn> naturalSpawns = spawns.getNaturalSpawns();
        Map<Pokemon, PokemonSpawn> pokemonMap = new HashMap<>();
        if (naturalSpawns.size() == 0) return pokemonMap;
        for (NaturalSpawn n : naturalSpawns) {

            Map<String, Map<String, Map<String, String>>> spawnData = n.getSpawnData();
            Map<String, Map<String, String>> innerData;
            if (spawnData.containsKey(time)) {

                innerData = spawnData.get(time);

            } else if (spawnData.containsKey("Any")) {

                innerData = spawnData.get("Any");

            } else {

                continue;

            }

            Map<String, String> data;
            if (innerData.containsKey(weather)) {

                data = innerData.get(weather);

            } else if (innerData.containsKey("Any")) {

                data = innerData.get("Any");

            } else {

                continue;

            }

            String locationTypes = data.get("Spawn-Location");
            boolean canSpawnHere = false;
            if (locationTypes.contains(", ")) {

                String[] split = locationTypes.split(", ");
                for (String l : split) {

                    if (l.equalsIgnoreCase(location)) {

                        canSpawnHere = true;
                        break;

                    }

                }

            } else {

                canSpawnHere = location.equalsIgnoreCase(locationTypes);

            }

            if (!canSpawnHere) continue;

            int level = RandomHelper.getRandomNumberBetween(n.getMinLevel(), n.getMaxLevel());
            Pokemon pokemon = PokemonBuilder.builder().species(n.getSpecies()).level(level).build();
            if (!n.getForm().equalsIgnoreCase("")) {

                pokemon.setForm(n.getForm());

            }

            pokemonMap.put(pokemon, n);

        }

        return pokemonMap;

    }

    public static Map<Pokemon, Double> buildNaturalSpawnsList (String time, String weather, String location, AreaSpawns spawns, double modifier) {

        List<NaturalSpawn> naturalSpawns = spawns.getNaturalSpawns();
        Map<Pokemon, Double> pokemonMap = new HashMap<>();
        if (naturalSpawns.size() == 0) return pokemonMap;
        Map<NaturalSpawn, Map<String, String>> m1 = new HashMap<>();
        Map<Double, List<NaturalSpawn>> m2 = new HashMap<>();
        for (NaturalSpawn n : naturalSpawns) {

            Map<String, Map<String, Map<String, String>>> spawnData = n.getSpawnData();
            Map<String, Map<String, String>> innerData;
            if (spawnData.containsKey(time)) {

                innerData = spawnData.get(time);

            } else if (spawnData.containsKey("Any")) {

                innerData = spawnData.get("Any");

            } else {

                continue;

            }

            Map<String, String> data;
            if (innerData.containsKey(weather)) {

                data = innerData.get(weather);

            } else if (innerData.containsKey("Any")) {

                data = innerData.get("Any");

            } else {

                continue;

            }

            String locationTypes = data.get("Spawn-Location");
            boolean canSpawnHere = false;
            if (locationTypes.contains(", ")) {

                String[] split = locationTypes.split(", ");
                for (String l : split) {

                    if (l.equalsIgnoreCase(location)) {

                        canSpawnHere = true;
                        break;

                    }

                }

            } else {

                canSpawnHere = location.equalsIgnoreCase(locationTypes);

            }

            if (!canSpawnHere) continue;


            double spawnChance = Double.parseDouble(data.get("Spawn-Chance"));
            m1.put(n, data);
            spawnChance = spawnChance * modifier;
            List<NaturalSpawn> list = new ArrayList<>();
            if (m2.containsKey(spawnChance)) {

                list = m2.get(spawnChance);
                list.add(n);

            } else {

                list.add(n);

            }
            m2.put(spawnChance, list);

        }

        List<Double> chances = new ArrayList<>(m2.keySet());
        Collections.sort(chances);

        for (int i = chances.size() - 1; i >= 0; i--) {

            if (RandomHelper.getRandomChance(chances.get(i))) {

                List<NaturalSpawn> spawnList = m2.get(chances.get(i));
                for (NaturalSpawn spawn : spawnList) {

                    int groupSize = RandomHelper.getRandomNumberBetween(1, Integer.parseInt(m1.get(spawn).get("Group-Size")));
                    for (int p = 0; p < groupSize; p++) {

                        int level = RandomHelper.getRandomNumberBetween(spawn.getMinLevel(), spawn.getMaxLevel());
                        Pokemon pokemon = PixelmonHelpers.fixPokemonIVsAndGender(PokemonBuilder.builder().species(spawn.getSpecies()).level(level).build());
                        if (!spawn.getForm().equalsIgnoreCase("")) {

                            pokemon.setForm(spawn.getForm());

                        }

                        pokemonMap.put(pokemon, chances.get(i));

                    }

                }

            }

        }

        return pokemonMap;

    }

    public static Map<Pokemon, PokemonSpawn> getPokemonGrassSpawnInfo (String time, String weather, String location, AreaSpawns spawns) {

        List<GrassSpawn> grassSpawns = spawns.getGrassSpawns();
        Map<Pokemon, PokemonSpawn> pokemonMap = new HashMap<>();
        if (grassSpawns.size() == 0) return pokemonMap;
        for (GrassSpawn g : grassSpawns) {

            Map<String, Map<String, Map<String, String>>> spawnData = g.getSpawnData();
            Map<String, Map<String, String>> innerData;
            if (spawnData.containsKey(time)) {

                innerData = spawnData.get(time);

            } else if (spawnData.containsKey("Any")) {

                innerData = spawnData.get("Any");

            } else {

                continue;

            }

            Map<String, String> data;
            if (innerData.containsKey(weather)) {

                data = innerData.get(weather);

            } else if (innerData.containsKey("Any")) {

                data = innerData.get("Any");

            } else {

                continue;

            }

            String locationTypes = data.get("Spawn-Location");
            boolean canSpawnHere = false;
            if (locationTypes.contains(", ")) {

                String[] split = locationTypes.split(", ");
                for (String l : split) {

                    if (l.equalsIgnoreCase(location)) {

                        canSpawnHere = true;
                        break;

                    }

                }

            } else {

                canSpawnHere = location.equalsIgnoreCase(locationTypes);

            }

            if (!canSpawnHere) continue;

            int level = RandomHelper.getRandomNumberBetween(g.getMinLevel(), g.getMaxLevel());
            Pokemon pokemon = PokemonBuilder.builder().species(g.getSpecies()).level(level).build();
            if (!g.getForm().equalsIgnoreCase("")) {

                pokemon.setForm(g.getForm());

            }

            pokemonMap.put(pokemon, g);

        }

        return pokemonMap;

    }

    public static Map<Pokemon, Double> buildGrassSpawnsList (String time, String weather, String location, AreaSpawns spawns, double modifier) {

        List<GrassSpawn> grassSpawns = spawns.getGrassSpawns();
        Map<Pokemon, Double> pokemonMap = new HashMap<>();
        if (grassSpawns.size() == 0) return pokemonMap;
        Map<GrassSpawn, Map<String, String>> m1 = new HashMap<>();
        Map<Double, List<GrassSpawn>> m2 = new HashMap<>();
        for (GrassSpawn g : grassSpawns) {

            Map<String, Map<String, Map<String, String>>> spawnData = g.getSpawnData();
            Map<String, Map<String, String>> innerData;
            if (spawnData.containsKey(time)) {

                innerData = spawnData.get(time);

            } else if (spawnData.containsKey("Any")) {

                innerData = spawnData.get("Any");

            } else {

                continue;

            }

            Map<String, String> data;
            if (innerData.containsKey(weather)) {

                data = innerData.get(weather);

            } else if (innerData.containsKey("Any")) {

                data = innerData.get("Any");

            } else {

                continue;

            }

            String locationTypes = data.get("Spawn-Location");
            boolean canSpawnHere = false;
            if (locationTypes.contains(", ")) {

                String[] split = locationTypes.split(", ");
                for (String l : split) {

                    if (l.equalsIgnoreCase(location)) {

                        canSpawnHere = true;
                        break;

                    }

                }

            } else {

                canSpawnHere = location.equalsIgnoreCase(locationTypes);

            }

            if (!canSpawnHere) continue;


            double spawnChance = Double.parseDouble(data.get("Spawn-Chance"));
            m1.put(g, data);
            spawnChance = spawnChance * modifier;
            List<GrassSpawn> list = new ArrayList<>();
            if (m2.containsKey(spawnChance)) {

                list = m2.get(spawnChance);

            }
            list.add(g);
            m2.put(spawnChance, list);

        }

        List<Double> chances = new ArrayList<>(m2.keySet());
        Collections.sort(chances);

        for (int i = chances.size() - 1; i >= 0; i--) {

            if (RandomHelper.getRandomChance(chances.get(i))) {

                List<GrassSpawn> spawnList = m2.get(chances.get(i));
                for (GrassSpawn spawn : spawnList) {

                    int level = RandomHelper.getRandomNumberBetween(spawn.getMinLevel(), spawn.getMaxLevel());
                    Pokemon pokemon = PixelmonHelpers.fixPokemonIVsAndGender(PokemonBuilder.builder().species(spawn.getSpecies()).level(level).build());
                    if (!spawn.getForm().equalsIgnoreCase("")) {

                        pokemon.setForm(spawn.getForm());

                    }

                    pokemonMap.put(pokemon, chances.get(i));

                }

            }

        }

        return pokemonMap;

    }

    public static Map<Pokemon, PokemonSpawn> getPokemonSurfSpawnInfo (String time, String weather, AreaSpawns spawns) {

        List<SurfSpawn> surfSpawns = spawns.getSurfSpawns();
        Map<Pokemon, PokemonSpawn> pokemonMap = new HashMap<>();
        if (surfSpawns.size() == 0) return pokemonMap;
        for (SurfSpawn s : surfSpawns) {

            Map<String, Map<String, Map<String, String>>> spawnData = s.getSpawnData();
            Map<String, Map<String, String>> innerData;
            if (spawnData.containsKey(time)) {

                innerData = spawnData.get(time);

            } else if (spawnData.containsKey("Any")) {

                innerData = spawnData.get("Any");

            } else {

                continue;

            }

            int level = RandomHelper.getRandomNumberBetween(s.getMinLevel(), s.getMaxLevel());
            Pokemon pokemon = PokemonBuilder.builder().species(s.getSpecies()).level(level).build();
            if (!s.getForm().equalsIgnoreCase("")) {

                pokemon.setForm(s.getForm());

            }

            pokemonMap.put(pokemon, s);

        }

        return pokemonMap;

    }

    public static Map<Pokemon, Double> buildSurfSpawnsList (String time, String weather, AreaSpawns spawns, double modifier) {

        List<SurfSpawn> surfSpawns = spawns.getSurfSpawns();
        Map<Pokemon, Double> pokemonMap = new HashMap<>();
        if (surfSpawns.size() == 0) return pokemonMap;
        Map<SurfSpawn, Map<String, String>> m1 = new HashMap<>();
        Map<Double, List<SurfSpawn>> m2 = new HashMap<>();
        for (SurfSpawn s : surfSpawns) {

            Map<String, Map<String, Map<String, String>>> spawnData = s.getSpawnData();
            Map<String, Map<String, String>> innerData;
            if (spawnData.containsKey(time)) {

                innerData = spawnData.get(time);

            } else if (spawnData.containsKey("Any")) {

                innerData = spawnData.get("Any");

            } else {

                continue;

            }

            Map<String, String> data;
            if (innerData.containsKey(weather)) {

                data = innerData.get(weather);

            } else if (innerData.containsKey("Any")) {

                data = innerData.get("Any");

            } else {

                continue;

            }

            double spawnChance = Double.parseDouble(data.get("Spawn-Chance"));
            m1.put(s, data);
            spawnChance = spawnChance * modifier;
            List<SurfSpawn> list = new ArrayList<>();
            if (m2.containsKey(spawnChance)) {

                list = m2.get(spawnChance);

            }
            list.add(s);
            m2.put(spawnChance, list);

        }

        List<Double> chances = new ArrayList<>(m2.keySet());
        Collections.sort(chances);

        for (int i = chances.size() - 1; i >= 0; i--) {

            if (RandomHelper.getRandomChance(chances.get(i))) {

                List<SurfSpawn> spawnList = m2.get(chances.get(i));
                for (SurfSpawn spawn : spawnList) {

                    int level = RandomHelper.getRandomNumberBetween(spawn.getMinLevel(), spawn.getMaxLevel());
                    Pokemon pokemon = PixelmonHelpers.fixPokemonIVsAndGender(PokemonBuilder.builder().species(spawn.getSpecies()).level(level).build());
                    if (!spawn.getForm().equalsIgnoreCase("")) {

                        pokemon.setForm(spawn.getForm());

                    }

                    pokemonMap.put(pokemon, chances.get(i));

                }

            }

        }

        return pokemonMap;

    }

}
