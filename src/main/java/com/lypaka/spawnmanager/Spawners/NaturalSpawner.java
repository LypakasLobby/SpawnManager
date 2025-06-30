package com.lypaka.spawnmanager.Spawners;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonEntities;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.BattleBuilder;
import com.cobblemon.mod.common.battles.BattleRegistry;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.RandomHandler;
import com.lypaka.lypakautils.Handlers.WorldTimeHandler;
import com.lypaka.lypakautils.LypakaUtils;
import com.lypaka.spawnmanager.SpawnAreas.SpawnArea;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.AreaSpawns;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.PokemonSpawn;
import com.lypaka.spawnmanager.Utils.ExternalAbilities.*;
import com.lypaka.spawnmanager.Utils.HeldItemUtils;
import com.lypaka.spawnmanager.Utils.PokemonSpawnBuilder;
import kotlin.Unit;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class NaturalSpawner {

    private static Timer timer = null;
    private static final Map<Area, Integer> spawnAttemptMap = new HashMap<>();
    public static Map<Area, Map<UUID, List<PokemonEntity>>> pokemonSpawnedMap = new HashMap<>();
    public static List<UUID> spawnedPokemonUUIDs = new ArrayList<>(); // used for battle end event listener to check for to despawn Pokemon or not

    public static void startTimer() {

        if (SpawnAreaHandler.areasWithNaturalSpawns == 0) return;
        if (timer != null) {

            timer.cancel();

        }

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                for (Map.Entry<String, Map<Area, List<UUID>>> map : AreaHandler.playersInArea.entrySet()) {

                    Map<Area, List<UUID>> map2 = map.getValue();
                    for (Map.Entry<Area, List<UUID>> entry : map2.entrySet()) {

                        if (!entry.getValue().isEmpty()) {

                            Area area = entry.getKey();
                            SpawnArea spawnArea = SpawnAreaHandler.areaMap.get(area);
                            int spawnIntervalCount = 1;
                            if (spawnAttemptMap.containsKey(area)) {

                                spawnIntervalCount = spawnAttemptMap.get(area);

                            }
                            if (spawnIntervalCount < spawnArea.getNaturalSpawnerSettings().getSpawnInterval()) {

                                spawnIntervalCount = spawnIntervalCount + 1;
                                spawnAttemptMap.put(area, spawnIntervalCount);
                                continue;

                            } else {

                                spawnIntervalCount = 1;
                                spawnAttemptMap.put(area, spawnIntervalCount);

                            }
                            for (UUID uuid : entry.getValue()) {

                                if (uuid == null) continue;
                                ServerPlayerEntity player = LypakaUtils.playerMap.get(uuid);
                                try {

                                    if (player == null) continue;

                                } catch (NullPointerException e) {

                                    continue;

                                }
                                int x;
                                try {

                                    x = player.getBlockPos().getX();

                                } catch (NullPointerException er) {

                                    continue;

                                }
                                int y = player.getBlockPos().getY();
                                int z = player.getBlockPos().getZ();
                                World world = player.getWorld();
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
                                String location = "land";
                                int radius = area.getRadius();
                                int spawnX;
                                int spawnY;
                                int spawnZ;
                                if (RandomHandler.getRandomChance(50)) {

                                    spawnX = x + RandomHandler.getRandomNumberBetween(1, radius);

                                } else {

                                    spawnX = x - RandomHandler.getRandomNumberBetween(1, radius);

                                }
                                if (RandomHandler.getRandomChance(50)) {

                                    spawnZ = z + RandomHandler.getRandomNumberBetween(1, radius);

                                } else {

                                    spawnZ = z - RandomHandler.getRandomNumberBetween(1, radius);

                                }
                                BlockPos pos = player.getBlockPos();
                                BlockState state = world.getBlockState(pos);
                                String blockID = Registries.BLOCK.getId(state.getBlock()).toString();
                                if (blockID.equalsIgnoreCase("air")) location = "air";
                                if (blockID.contains("water") || blockID.contains("lava")) location = "water";
                                if (y <= area.getUnderground()) location = "underground";
                                Heightmap.Type type = location.equalsIgnoreCase("water") ? Heightmap.Type.OCEAN_FLOOR : Heightmap.Type.WORLD_SURFACE;

                                if (location.equalsIgnoreCase("underground")) {

                                    spawnY = y;

                                } else {

                                    Chunk chunk = player.getWorld().getChunk(spawnX >> 4, spawnZ >> 4);
                                    Heightmap heightmap = chunk.getHeightmap(type);
                                    int relativeX = spawnX & 15;
                                    int relativeZ = spawnZ & 15;
                                    spawnY = heightmap.get(relativeX, relativeZ);

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
                                List<Area> areas = AreaHandler.getSortedAreas(x, y, z, world);
                                AtomicBoolean spawned = new AtomicBoolean(false);
                                double modifier = 1.0;
                                if (ArenaTrap.applies(playersPokemon) || Illuminate.applies(playersPokemon) || NoGuard.applies(playersPokemon)) {

                                    modifier = 2.0;

                                } else if (Infiltrator.applies(playersPokemon) || QuickFeet.applies(playersPokemon) || Stench.applies(playersPokemon) || WhiteSmoke.applies(playersPokemon)) {

                                    modifier = 0.5;

                                }
                                for (int i = 0; i < areas.size(); i++) {

                                    if (spawned.get()) break;
                                    Area currentArea = areas.get(i);
                                    SpawnArea currentSpawnArea = SpawnAreaHandler.areaMap.get(currentArea);
                                    if (currentSpawnArea.getNaturalSpawnerSettings().doesLimitSpawns()) {

                                        if (BattleRegistry.INSTANCE.getBattleByParticipatingPlayer(player) != null) break;

                                    }
                                    AreaSpawns spawns = SpawnAreaHandler.areaSpawnMap.get(currentSpawnArea);
                                    if (!spawns.getNaturalSpawns().isEmpty()) {

                                        Map<PokemonSpawn, Double> pokemon = PokemonSpawnBuilder.buildNaturalSpawnsList(time, weather, location, spawns, modifier);
                                        Map<Pokemon, PokemonSpawn> mapForHustle = new HashMap<>();
                                        Pokemon firstPokemonSpawned = null;
                                        List<Pokemon> toSpawn = new ArrayList<>();
                                        for (Map.Entry<PokemonSpawn, Double> p : pokemon.entrySet()) {

                                            if (firstPokemonSpawned == null) {

                                                if (RandomHandler.getRandomChance(p.getValue())) {

                                                    firstPokemonSpawned = PokemonSpawnBuilder.buildPokemonFromPokemonSpawn(p.getKey());
                                                    toSpawn.add(firstPokemonSpawned);
                                                    mapForHustle.put(firstPokemonSpawned, p.getKey());

                                                }

                                            } else {

                                                if (p.getKey().getSpecies().getName().equalsIgnoreCase(firstPokemonSpawned.getSpecies().getName())) {

                                                    toSpawn.add(PokemonSpawnBuilder.buildPokemonFromPokemonSpawn(p.getKey()));

                                                }

                                            }

                                        }
                                        for (Pokemon poke : toSpawn) {

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

                                            if (RandomHandler.getRandomChance(50)) {

                                                spawnX = x + RandomHandler.getRandomNumberBetween(1, 4);

                                            } else {

                                                spawnX = x - RandomHandler.getRandomNumberBetween(1, 4);

                                            }
                                            if (RandomHandler.getRandomChance(50)) {

                                                spawnZ = z + RandomHandler.getRandomNumberBetween(1, 4);

                                            } else {

                                                spawnZ = z - RandomHandler.getRandomNumberBetween(1, 4);

                                            }

                                            List<Area> areasAtSpawn = AreaHandler.getFromLocation(spawnX, spawnY, spawnZ, player.getWorld());
                                            if (areasAtSpawn.isEmpty()) continue;
                                            PokemonEntity entity = new PokemonEntity(player.getWorld(), poke, CobblemonEntities.POKEMON);
                                            if (currentSpawnArea.getNaturalSpawnerSettings().doesClearSpawns()) {

                                                Map<UUID, List<PokemonEntity>> spawnedMap = new HashMap<>();
                                                if (pokemonSpawnedMap.containsKey(currentArea)) {

                                                    spawnedMap = pokemonSpawnedMap.get(currentArea);

                                                }
                                                List<PokemonEntity> spawnedPokemon = new ArrayList<>();
                                                if (spawnedMap.containsKey(player.getUuid())) {

                                                    spawnedPokemon = spawnedMap.get(player.getUuid());

                                                }

                                                spawnedPokemon.add(entity);
                                                spawnedMap.put(player.getUuid(), spawnedPokemon);
                                                pokemonSpawnedMap.put(currentArea, spawnedMap);

                                            }
                                            Pokemon finalPoke = poke;
                                            int finalSpawnX = spawnX;
                                            int finalSpawnZ = spawnZ;
                                            player.getWorld().getServer().executeSync(() -> {

                                                entity.setPosition(finalSpawnX, spawnY + 1.5, finalSpawnZ);
                                                player.getWorld().spawnEntity(entity);
                                                spawned.set(true);
                                                if (currentSpawnArea.getNaturalSpawnerSettings().doesDespawnAfterBattle()) {

                                                    spawnedPokemonUUIDs.add(entity.getUuid());

                                                }
                                                if (currentSpawnArea.getNaturalSpawnerSettings().getDespawnTimer() > 0) {

                                                    entity.setDespawnCounter(currentSpawnArea.getNaturalSpawnerSettings().getDespawnTimer());

                                                }
                                                if (toSpawn.size() == 1) {

                                                    String messageType = "";
                                                    if (finalPoke.getShiny()) {

                                                        messageType = "-Shiny";

                                                    }
                                                    messageType = "Spawn-Message" + messageType;
                                                    String message = currentSpawnArea.getNaturalSpawnerSettings().getMessagesMap().get(messageType);
                                                    if (!message.equalsIgnoreCase("")) {

                                                        player.sendMessage(FancyTextHandler.getFormattedText(message.replace("%pokemon%", finalPoke.getSpecies().getName())), true);

                                                    }
                                                    // only one Pokemon spawned, so we check for the auto battle stuff
                                                    if (RandomHandler.getRandomChance(currentSpawnArea.getNaturalSpawnerSettings().getAutoBattleChance())) {

                                                        Timer t = new Timer();
                                                        t.schedule(new TimerTask() {

                                                            @Override
                                                            public void run() {

                                                                if (BattleRegistry.INSTANCE.getBattleByParticipatingPlayer(player) == null) {

                                                                    BattleBuilder.INSTANCE.pve(player, entity, null).ifSuccessful(function -> Unit.INSTANCE);

                                                                }
                                                            }

                                                        }, 10);

                                                    }

                                                }

                                            });

                                        }

                                    } else {

                                        break;

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }, 0, 1000L);

    }

}
