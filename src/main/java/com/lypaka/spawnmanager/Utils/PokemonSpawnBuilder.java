package com.lypaka.spawnmanager.Utils;

import com.lypaka.lypakautils.MiscHandlers.PixelmonHelpers;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.*;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;

import java.util.*;

public class PokemonSpawnBuilder {

    public static Pokemon buildPokemonFromPokemonSpawn (PokemonSpawn spawn) {

        int level = RandomHelper.getRandomNumberBetween(spawn.getMinLevel(), spawn.getMaxLevel());
        Pokemon pokemon = PixelmonHelpers.fixPokemonIVsAndGender(PokemonBuilder.builder().species(spawn.getSpecies()).level(level).build());
        if (!spawn.getForm().equalsIgnoreCase("")) {

            pokemon.setForm(spawn.getForm());

        }

        return pokemon;

    }

    public static Map<PokemonSpawn, Double> buildSurfSpawnsList (String time, String weather, AreaSpawns spawns, double modifier) {

        List<SurfSpawn> surfSpawns = spawns.getSurfSpawns();
        Map<PokemonSpawn, Double> pokemonMap = new HashMap<>();
        if (surfSpawns.size() == 0) return pokemonMap;
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
            double hostileChance = data.containsKey("Hostile-Chance") ? Double.parseDouble(data.get("Hostile-Chance")) : 0;
            double totemChance = data.containsKey("Totem-Chance") ? Double.parseDouble(data.get("Totem-Chance")) : 0;
            double titanChance = data.containsKey("Titan-Chance") ? Double.parseDouble(data.get("Titan-Chance")) : 0;
            s.setHostileChance(hostileChance);
            s.setTotemChance(totemChance);
            s.setTitanChance(titanChance);
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

                    pokemonMap.put(spawn, chances.get(i));

                }

            }

        }

        return pokemonMap;

    }

    public static Map<PokemonSpawn, Double> buildNaturalSpawnsList (String time, String weather, String location, AreaSpawns spawns, double modifier) {

        List<NaturalSpawn> naturalSpawns = spawns.getNaturalSpawns();
        Map<PokemonSpawn, Double> pokemonMap = new HashMap<>();
        if (naturalSpawns.size() == 0) return pokemonMap;
        Map<Double, List<NaturalSpawn>> m2 = new HashMap<>();
        int groupSizeMax = 1;
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
            double hostileChance = data.containsKey("Hostile-Chance") ? Double.parseDouble(data.get("Hostile-Chance")) : 0;
            double totemChance = data.containsKey("Totem-Chance") ? Double.parseDouble(data.get("Totem-Chance")) : 0;
            double titanChance = data.containsKey("Titan-Chance") ? Double.parseDouble(data.get("Titan-Chance")) : 0;
            if (data.containsKey("Group-Size")) {

                groupSizeMax = Integer.parseInt(data.get("Group-Size"));

            }
            n.setHostileChance(hostileChance);
            n.setTotemChance(totemChance);
            n.setTitanChance(titanChance);
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

                    int groupSize = RandomHelper.getRandomNumberBetween(1, groupSizeMax);
                    for (int p = 0; p < groupSize; p++) {

                        pokemonMap.put(spawn, chances.get(i));

                    }

                }

            }

        }

        return pokemonMap;

    }

    public static Map<PokemonSpawn, Double> buildHeadbuttSpawns (String time, String weather, String blockID, AreaSpawns spawns, double modifier) {

        List<HeadbuttSpawn> headbuttSpawns = spawns.getHeadbuttSpawns();
        Map<Double, List<HeadbuttSpawn>> map = new HashMap<>();
        Map<PokemonSpawn, Double> pokemonMap = new HashMap<>();
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
            double hostileChance = data.containsKey("Hostile-Chance") ? Double.parseDouble(data.get("Hostile-Chance")) : 0;
            double totemChance = data.containsKey("Totem-Chance") ? Double.parseDouble(data.get("Totem-Chance")) : 0;
            double titanChance = data.containsKey("Titan-Chance") ? Double.parseDouble(data.get("Titan-Chance")) : 0;
            h.setHostileChance(hostileChance);
            h.setTotemChance(totemChance);
            h.setTitanChance(titanChance);
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

                pokemonMap.put(spawn, chances.get(i));

            }

        }

        return pokemonMap;

    }

    public static Map<PokemonSpawn, Double> buildRockSmashSpawns (String time, String weather, String blockID, AreaSpawns spawns, double modifier) {

        List<RockSmashSpawn> rockSmashSpawns = spawns.getRockSmashSpawns();
        Map<Double, List<RockSmashSpawn>> map = new HashMap<>();
        Map<PokemonSpawn, Double> pokemonMap = new HashMap<>();
        if (rockSmashSpawns.size() == 0) return pokemonMap;
        for (RockSmashSpawn r : rockSmashSpawns) {

            Map<String, Map<String, Map<String, String>>> spawnData = r.getSpawnData();
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
            double hostileChance = data.containsKey("Hostile-Chance") ? Double.parseDouble(data.get("Hostile-Chance")) : 0;
            double totemChance = data.containsKey("Totem-Chance") ? Double.parseDouble(data.get("Totem-Chance")) : 0;
            double titanChance = data.containsKey("Titan-Chance") ? Double.parseDouble(data.get("Titan-Chance")) : 0;
            r.setHostileChance(hostileChance);
            r.setTotemChance(totemChance);
            r.setTitanChance(titanChance);
            List<RockSmashSpawn> list = new ArrayList<>();
            if (map.containsKey(spawnChance)) list = map.get(spawnChance);
            if (stoneTypes.equalsIgnoreCase("Any")) {

                list.add(r);

            } else {

                if (stoneTypes.contains(", ")) {

                    String[] split = stoneTypes.split(", ");
                    for (String s : split) {

                        if (s.equalsIgnoreCase(blockID)) {

                            list.add(r);
                            break;

                        }

                    }

                } else {

                    if (stoneTypes.equalsIgnoreCase(blockID)) {

                        list.add(r);

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

                pokemonMap.put(spawn, chances.get(i));

            }

        }

        return pokemonMap;

    }

    public static Map<PokemonSpawn, Double> buildFishSpawns (String rod, String time, String weather, AreaSpawns spawns, double modifier) {

        List<FishSpawn> fishSpawns = spawns.getFishSpawns();
        Map<PokemonSpawn, Double> pokemonMap = new HashMap<>();

        if (fishSpawns.size() == 0) return pokemonMap;
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
            double hostileChance = data.containsKey("Hostile-Chance") ? Double.parseDouble(data.get("Hostile-Chance")) : 0;
            double totemChance = data.containsKey("Totem-Chance") ? Double.parseDouble(data.get("Totem-Chance")) : 0;
            double titanChance = data.containsKey("Titan-Chance") ? Double.parseDouble(data.get("Titan-Chance")) : 0;
            f.setHostileChance(hostileChance);
            f.setTotemChance(totemChance);
            f.setTitanChance(titanChance);
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

                    pokemonMap.put(spawn, chances.get(i));

                }

            }

        }

        return pokemonMap;

    }

    public static Map<PokemonSpawn, Double> buildGrassSpawnsList (String time, String weather, String location, AreaSpawns spawns, double modifier) {

        List<GrassSpawn> grassSpawns = spawns.getGrassSpawns();
        Map<PokemonSpawn, Double> pokemonMap = new HashMap<>();
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
            double hostileChance = data.containsKey("Hostile-Chance") ? Double.parseDouble(data.get("Hostile-Chance")) : 0;
            double totemChance = data.containsKey("Totem-Chance") ? Double.parseDouble(data.get("Totem-Chance")) : 0;
            double titanChance = data.containsKey("Titan-Chance") ? Double.parseDouble(data.get("Titan-Chance")) : 0;
            g.setHostileChance(hostileChance);
            g.setTotemChance(totemChance);
            g.setTitanChance(titanChance);
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

                    pokemonMap.put(spawn, chances.get(i));

                }

            }

        }

        return pokemonMap;

    }

}
