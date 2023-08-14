package com.lypaka.spawnmanager.Spawners;

import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.spawnmanager.API.AreaFishSpawnEvent;
import com.lypaka.spawnmanager.SpawnAreas.SpawnArea;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.AreaSpawns;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.PokemonSpawn;
import com.lypaka.spawnmanager.Utils.ExternalAbilities.*;
import com.lypaka.spawnmanager.Utils.HeldItemUtils;
import com.lypaka.spawnmanager.Utils.SpawnBuilder;
import com.pixelmonmod.pixelmon.api.events.FishingEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class FishSpawner {

    public static List<UUID> spawnedPokemonUUIDs = new ArrayList<>();
    public static Map<Area, Map<UUID, List<PixelmonEntity>>> pokemonSpawnedMap = new HashMap<>();

    @SubscribeEvent
    public void onCast (FishingEvent.Cast event) {

        ServerPlayerEntity player = event.player;
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        int z = player.getPosition().getZ();
        World world = player.world;
        List<Area> areas = AreaHandler.getSortedAreas(x, y, z, world);
        if (areas.size() == 0) return;

        Pokemon firstPokemon = null;
        PlayerPartyStorage party = StorageProxy.getParty(player);
        for (int i = 0; i < 6; i++) {

            Pokemon p = party.get(i);
            if (p != null) {

                firstPokemon = p;
                break;

            }

        }
        if (StormDrain.applies(firstPokemon) || SuctionCups.applies(firstPokemon)) {

            event.setChanceOfNothing((float) (event.getChanceOfNothing() - (event.getChanceOfNothing() * 0.25)));

        }

    }

    @SubscribeEvent
    public void onFish (FishingEvent.Reel event) {

        if (event.isPokemon()) {

            ServerPlayerEntity player = event.player;
            int x = player.getPosition().getX();
            int y = player.getPosition().getY();
            int z = player.getPosition().getZ();
            World world = player.world;
            List<Area> areas = AreaHandler.getSortedAreas(x, y, z, world);
            if (areas.size() == 0) return;
            String rod = event.getRodType().name();
            if (rod.contains("old")) {

                rod = "Old";

            } else if (rod.contains("good")) {

                rod = "Good";

            } else if (rod.contains("super")) {

                rod = "Super";

            }
            String time = "Night";
            List<WorldTime> times = WorldTime.getCurrent(world);
            for (WorldTime t : times) {

                if (t.name().contains("day") || t.name().contains("dawn") || t.name().contains("morning") || t.name().contains("afternoon")) {

                    time = "Day";
                    break;

                }

            }
            String weather = "Clear";
            if (world.isRaining()) {

                weather = "Rain";

            } else if (world.isThundering()) {

                weather = "Storm";

            }

            Pokemon playersPokemon = null;
            PlayerPartyStorage party = StorageProxy.getParty(player);
            for (int i = 0; i < 6; i++) {

                Pokemon p = party.get(i);
                if (p != null) {

                    playersPokemon = p;
                    break;

                }

            }
            double modifier = 1.0;
            if (ArenaTrap.applies(playersPokemon) || Illuminate.applies(playersPokemon) || NoGuard.applies(playersPokemon)) {

                modifier = 2.0;

            } else if (Infiltrator.applies(playersPokemon) || QuickFeet.applies(playersPokemon) || Stench.applies(playersPokemon) || WhiteSmoke.applies(playersPokemon)) {

                modifier = 0.5;

            }
            for (int i = 0; i < areas.size(); i++) {

                Area currentArea = areas.get(i);
                SpawnArea spawnArea = SpawnAreaHandler.areaMap.get(currentArea);
                AreaSpawns spawns = SpawnAreaHandler.areaSpawnMap.get(spawnArea);
                if (spawns.getFishSpawns().size() > 0) {

                    Map<Pokemon, Double> pokemon = SpawnBuilder.buildFishSpawns(rod, time, weather, spawns, modifier);
                    Map<Pokemon, PokemonSpawn> spawnInfoMap = SpawnBuilder.getPokemonFishSpawnInfo(rod, time, weather, spawns);
                    for (Map.Entry<Pokemon, Double> p : pokemon.entrySet()) {

                        if (RandomHelper.getRandomChance(p.getValue())) {

                            Pokemon poke = p.getKey();
                            if (Intimidate.applies(playersPokemon) || KeenEye.applies(playersPokemon)) {

                                poke = Intimidate.tryIntimidate(poke, playersPokemon);
                                if (poke == null) continue;

                            }
                            if (FlashFire.applies(playersPokemon)) {

                                poke = FlashFire.tryFlashFire(poke, pokemon);

                            } else if (Harvest.applies(playersPokemon)) {

                                poke = Harvest.tryHarvest(poke, pokemon);

                            } else if (LightningRod.applies(playersPokemon) || Static.applies(playersPokemon)) {

                                poke = LightningRod.tryLightningRod(poke, pokemon);

                            } else if (MagnetPull.applies(playersPokemon)) {

                                poke = MagnetPull.tryMagnetPull(poke, pokemon);

                            } else if (StormDrain.applies(playersPokemon)) {

                                poke = StormDrain.tryStormDrain(poke, pokemon);

                            }

                            if (CuteCharm.applies(playersPokemon)) {

                                CuteCharm.tryApplyCuteCharmEffect(poke, playersPokemon);

                            } else if (Synchronize.applies(playersPokemon)) {

                                Synchronize.applySynchronize(poke, playersPokemon);

                            }

                            int level = poke.getPokemonLevel();
                            if (Hustle.applies(playersPokemon) || Pressure.applies(playersPokemon) || VitalSpirit.applies(playersPokemon)) {

                                level = Hustle.tryHustle(level, spawnInfoMap.get(poke));

                            }
                            poke.setLevel(level);
                            poke.setLevelNum(level);

                            HeldItemUtils.tryApplyHeldItem(poke, playersPokemon);

                            AreaFishSpawnEvent spawnEvent = new AreaFishSpawnEvent(player, currentArea, rod, poke);
                            MinecraftForge.EVENT_BUS.post(spawnEvent);
                            if (!spawnEvent.isCanceled()) {

                                if (spawnEvent.getPokemon() != null) {

                                    PixelmonEntity fishedUpPokemon = (PixelmonEntity) event.optEntity.get();
                                    fishedUpPokemon.getPokemon().setSpecies(spawnEvent.getPokemon().getSpecies(), false);
                                    fishedUpPokemon.getPokemon().setForm(spawnEvent.getPokemon().getForm());
                                    fishedUpPokemon.getPokemon().setLevel(spawnEvent.getPokemon().getPokemonLevel());
                                    if (spawnArea.getFishSpawnerSettings().doesDespawnAfterBattle()) {

                                        spawnedPokemonUUIDs.add(fishedUpPokemon.getUniqueID());

                                    } else {

                                        if (spawnArea.getFishSpawnerSettings().getDespawnTimer() > 0) {

                                            fishedUpPokemon.despawnCounter = spawnArea.getFishSpawnerSettings().getDespawnTimer();

                                        }

                                    }
                                    if (spawnArea.getFishSpawnerSettings().doesClearSpawns()) {

                                        Map<UUID, List<PixelmonEntity>> spawnedMap = new HashMap<>();
                                        if (pokemonSpawnedMap.containsKey(currentArea)) {

                                            spawnedMap = pokemonSpawnedMap.get(currentArea);

                                        }
                                        List<PixelmonEntity> spawnedPokemon = new ArrayList<>();
                                        if (spawnedMap.containsKey(player.getUniqueID())) {

                                            spawnedPokemon = spawnedMap.get(player.getUniqueID());

                                        }

                                        spawnedPokemon.add(fishedUpPokemon);
                                        spawnedMap.put(player.getUniqueID(), spawnedPokemon);
                                        pokemonSpawnedMap.put(currentArea, spawnedMap);

                                    }

                                } else {

                                    event.setCanceled(true);

                                }
                                break;

                            }

                        }

                    }

                }

            }

        }

    }

}
