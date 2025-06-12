package com.lypaka.spawnmanager.Spawners;

import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.spawnmanager.API.AreaFishSpawnEvent;
import com.lypaka.spawnmanager.SpawnAreas.SpawnArea;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.AreaSpawns;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.PokemonSpawn;
import com.lypaka.spawnmanager.Utils.ExternalAbilities.*;
import com.lypaka.spawnmanager.Utils.ExternalModules.HostileManager;
import com.lypaka.spawnmanager.Utils.ExternalModules.TitanManager;
import com.lypaka.spawnmanager.Utils.ExternalModules.TotemManager;
import com.lypaka.spawnmanager.Utils.HeldItemUtils;
import com.lypaka.spawnmanager.Utils.PokemonSpawnBuilder;
import com.pixelmonmod.pixelmon.api.events.FishingEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

import java.util.*;

public class FishSpawner {

    public static List<UUID> spawnedPokemonUUIDs = new ArrayList<>();
    public static Map<Area, Map<UUID, List<PixelmonEntity>>> pokemonSpawnedMap = new HashMap<>();
    public static Map<UUID, Pokemon> fishSpawnerMap = new HashMap<>();
    public static Map<UUID, Pokemon> jankySpawnMap = new HashMap<>();

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
    public void onCatch (FishingEvent.Catch event) {

        if (event.plannedSpawn.getOrCreateEntity() instanceof PixelmonEntity) {

            PixelmonEntity pixelmon = (PixelmonEntity) event.plannedSpawn.getOrCreateEntity();
            UUID uuid = pixelmon.getUniqueID();
            fishSpawnerMap.put(uuid, pixelmon.getPokemon());
            jankySpawnMap.put(event.player.getUniqueID(), pixelmon.getPokemon());

        }

    }

    @SubscribeEvent
    public void onFish (FishingEvent.Reel event) {

        String rod = event.getRodType().name().replace("Rod", "");
        ServerPlayerEntity player = event.player;
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        int z = player.getPosition().getZ();
        World world = player.world;
        List<Area> areas = AreaHandler.getSortedAreas(x, y, z, world);
        if (areas.size() == 0) return;
        jankySpawnMap.entrySet().removeIf(entry -> {

            if (entry.getKey().toString().equalsIgnoreCase(player.getUniqueID().toString())) {

                String time = "Night";
                List<WorldTime> times = WorldTime.getCurrent(world);
                for (WorldTime t : times) {

                    if (t.name().contains("DAY") || t.name().contains("DAWN") || t.name().contains("MORNING") || t.name().contains("AFTERNOON")) {

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

                        Map<PokemonSpawn, Double> pokemon = PokemonSpawnBuilder.buildFishSpawns(rod, time, weather, spawns, modifier);
                        Map<Pokemon, PokemonSpawn> mapForHustle = new HashMap<>();
                        for (Map.Entry<PokemonSpawn, Double> p : pokemon.entrySet()) {

                            if (RandomHelper.getRandomChance(p.getValue())) {

                                Pokemon poke = PokemonSpawnBuilder.buildPokemonFromPokemonSpawn(p.getKey());
                                mapForHustle.put(poke, p.getKey());
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

                                    level = Hustle.tryHustle(level, mapForHustle.get(poke));

                                }
                                poke.setLevel(level);
                                poke.setLevelNum(level);

                                HeldItemUtils.tryApplyHeldItem(poke, playersPokemon);

                                AreaFishSpawnEvent spawnEvent = new AreaFishSpawnEvent(player, currentArea, rod, poke);
                                MinecraftForge.EVENT_BUS.post(spawnEvent);
                                if (!spawnEvent.isCanceled()) {

                                    if (spawnEvent.getPokemon() != null) {

                                        PixelmonEntity fishedUpPokemon;// = (PixelmonEntity) event.optEntity.orElse(new PixelmonEntity(player.world, spawnEvent.getPokemon()));
                                        try {

                                            fishedUpPokemon = (PixelmonEntity) event.optEntity.orElse(new PixelmonEntity(player.world, spawnEvent.getPokemon()));

                                        } catch (ClassCastException e) {

                                            event.setCanceled(true); // should despawn the item that spawned
                                            fishedUpPokemon = new PixelmonEntity(player.world, spawnEvent.getPokemon());

                                        }

                                        fishedUpPokemon.setPosition(event.fishHook.getPosX(), event.fishHook.getPosY(), event.fishHook.getPosZ());
                                        fishedUpPokemon.setSpawnLocation(fishedUpPokemon.getDefaultSpawnLocation());
                                        player.world.addEntity(fishedUpPokemon);
                                        WildPixelmonParticipant wpp = new WildPixelmonParticipant(fishedUpPokemon);
                                        PlayerParticipant pp = new PlayerParticipant(player, StorageProxy.getParty(player).getTeam(), 1);
                                        BattleRegistry.startBattle(new BattleParticipant[]{wpp}, new BattleParticipant[]{pp}, new BattleRules());

                                    } else {

                                        event.setCanceled(true);

                                    }
                                    break;

                                }

                            }

                        }

                    }

                }

                return true;

            }

            return false;

        });

    }

}
