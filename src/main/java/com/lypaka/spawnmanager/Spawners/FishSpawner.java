package com.lypaka.spawnmanager.Spawners;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.lypakautils.Handlers.RandomHandler;
import com.lypaka.lypakautils.Handlers.WorldTimeHandler;
import com.lypaka.spawnmanager.SpawnAreas.SpawnArea;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.AreaSpawns;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.PokemonSpawn;
import com.lypaka.spawnmanager.Utils.ExternalAbilities.*;
import com.lypaka.spawnmanager.Utils.HeldItemUtils;
import com.lypaka.spawnmanager.Utils.PokemonSpawnBuilder;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.*;

public class FishSpawner {

    public static List<UUID> spawnedPokemonUUIDs = new ArrayList<>();
    public static Map<Area, Map<UUID, List<PokemonEntity>>> pokemonSpawnedMap = new HashMap<>();
    public static Map<UUID, Pokemon> fishSpawnerMap = new HashMap<>();

    public static void registerCast() {

        // TODO Return to later when I am smarter

        /*
        CobblemonEvents.POKEROD_CAST_PRE.subscribe(Priority.NORMAL, event -> {

            ServerPlayerEntity player = (ServerPlayerEntity) event.getBobber().getPlayerOwner();
            int x = player.getBlockPos().getX();
            int y = player.getBlockPos().getY();
            int z = player.getBlockPos().getZ();
            World world = player.getWorld();
            List<Area> areas = AreaHandler.getSortedAreas(x, y, z, world);
            if (areas.isEmpty()) return;

            Pokemon firstPokemon = null;
            PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
            for (int i = 0; i < 6; i++) {

                Pokemon p = party.get(i);
                if (p != null) {

                    firstPokemon = p;
                    break;

                }

            }
            if (StormDrain.applies(firstPokemon) || SuctionCups.applies(firstPokemon)) {

                ItemStack bait = event.getBait();
                int chance = event.getBobber().getPokemonSpawnChance(bait);
                RodBaitComponent baitChance = bait.get(CobblemonItemComponents.INSTANCE.getBAIT());
                RodBaitComponent newComponent = new RodBaitComponent(baitChance.getBait(), bait);
                event.setChanceOfNothing((float) (event.getChanceOfNothing() - (event.getChanceOfNothing() * 0.25)));

            }

            return Unit.INSTANCE;

        });
         */

    }

    // TODO Verify if this is even necessary since we're not on unstable ass Pixelmon anymore
    /*
    @SubscribeEvent
    public void onCatch (FishingEvent.Catch event) {

        if (event.plannedSpawn.getOrCreateEntity() instanceof PixelmonEntity) {

            PixelmonEntity pixelmon = (PixelmonEntity) event.plannedSpawn.getOrCreateEntity();
            UUID uuid = pixelmon.getUniqueID();
            fishSpawnerMap.put(uuid, pixelmon.getPokemon());
            jankySpawnMap.put(event.player.getUniqueID(), pixelmon.getPokemon());

        }

    }
     */

    public static void registerReel() {

        CobblemonEvents.BOBBER_SPAWN_POKEMON_MODIFY.subscribe(Priority.NORMAL, event -> {

            ServerPlayerEntity player = (ServerPlayerEntity) event.getRod().getHolder();
            int x = player.getBlockPos().getX();
            int y = player.getBlockPos().getY();
            int z = player.getBlockPos().getZ();
            World world = player.getWorld();
            List<Area> areas = AreaHandler.getSortedAreas(x, y, z, world);
            if (areas.isEmpty()) return Unit.INSTANCE;
            String time = "Night";
            List<String> times = WorldTimeHandler.getCurrentTimeValues(world);
            for (String t : times) {

                if (t.equalsIgnoreCase("DAY") || t.equalsIgnoreCase("DAWN") || t.equalsIgnoreCase("MORNING")) {

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
            PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
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
                if (!spawns.getFishSpawns().isEmpty()) {

                    Map<PokemonSpawn, Double> pokemon = PokemonSpawnBuilder.buildFishSpawns(time, weather, spawns, modifier);
                    Map<Pokemon, PokemonSpawn> mapForHustle = new HashMap<>();
                    for (Map.Entry<PokemonSpawn, Double> p : pokemon.entrySet()) {

                        if (RandomHandler.getRandomChance(p.getValue())) {

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

                            int level = poke.getLevel();
                            if (Hustle.applies(playersPokemon) || Pressure.applies(playersPokemon) || VitalSpirit.applies(playersPokemon)) {

                                level = Hustle.tryHustle(level, mapForHustle.get(poke));

                            }
                            poke.setLevel(level);
                            HeldItemUtils.tryApplyHeldItem(poke, playersPokemon);
                            event.getPokemon().setPokemon(poke);

                        }

                    }

                }

            }

            return Unit.INSTANCE;

        });

    }

}
