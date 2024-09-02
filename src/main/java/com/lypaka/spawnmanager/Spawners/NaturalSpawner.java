package com.lypaka.spawnmanager.Spawners;

import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.areamanager.Regions.Region;
import com.lypaka.hostilepokemon.API.SetHostileEvent;
import com.lypaka.hostilepokemon.HostilePokemon;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.Listeners.JoinListener;
import com.lypaka.spawnmanager.API.AreaNaturalSpawnEvent;
import com.lypaka.spawnmanager.SpawnAreas.SpawnArea;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.AreaSpawns;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.PokemonSpawn;
import com.lypaka.spawnmanager.SpawnManager;
import com.lypaka.spawnmanager.Utils.ExternalAbilities.*;
import com.lypaka.spawnmanager.Utils.HeldItemUtils;
import com.lypaka.spawnmanager.Utils.SpawnBuilder;
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
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;

public class NaturalSpawner {

    private static Timer timer = null;
    private static final Map<Area, Integer> spawnAttemptMap = new HashMap<>();
    public static Map<Area, Map<UUID, List<PixelmonEntity>>> pokemonSpawnedMap = new HashMap<>();
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

                    String key = map.getKey();
                    Map<Area, List<UUID>> map2 = map.getValue();
                    for (Map.Entry<Area, List<UUID>> entry : map2.entrySet()) {

                        if (entry.getValue().size() > 0) {

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
                                ServerPlayerEntity player = JoinListener.playerMap.get(uuid);
                                int x = player.getPosition().getX();
                                int y = player.getPosition().getY();
                                int z = player.getPosition().getZ();
                                World world = player.world;
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
                                String location = "land";
                                int radius = area.getRadius();
                                int spawnX;
                                int spawnY;
                                int spawnZ;
                                if (RandomHelper.getRandomChance(50)) {

                                    spawnX = x + RandomHelper.getRandomNumberBetween(1, radius);

                                } else {

                                    spawnX = x - RandomHelper.getRandomNumberBetween(1, radius);

                                }
                                if (RandomHelper.getRandomChance(50)) {

                                    spawnZ = z + RandomHelper.getRandomNumberBetween(1, radius);

                                } else {

                                    spawnZ = z - RandomHelper.getRandomNumberBetween(1, radius);

                                }
                                BlockPos tempPOS = new BlockPos(spawnX, y, spawnZ);
                                String blockID = world.getBlockState(tempPOS).getBlock().getRegistryName().toString();
                                if (blockID.equalsIgnoreCase("air")) location = "air";
                                if (blockID.contains("water") || blockID.contains("lava")) location = "water";
                                if (y <= area.getUnderground()) location = "underground";
                                Heightmap.Type type = location.equalsIgnoreCase("water") ? Heightmap.Type.OCEAN_FLOOR : Heightmap.Type.WORLD_SURFACE;
                                if (location.equalsIgnoreCase("underground")) {

                                    spawnY = y;

                                } else {

                                    spawnY = player.world.getChunk(tempPOS).getTopBlockY(type, spawnX, spawnZ);

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
                                List<Area> areas = AreaHandler.getSortedAreas(x, y, z, world);
                                boolean spawned = false;
                                double modifier = 1.0;
                                if (ArenaTrap.applies(playersPokemon) || Illuminate.applies(playersPokemon) || NoGuard.applies(playersPokemon)) {

                                    modifier = 2.0;

                                } else if (Infiltrator.applies(playersPokemon) || QuickFeet.applies(playersPokemon) || Stench.applies(playersPokemon) || WhiteSmoke.applies(playersPokemon)) {

                                    modifier = 0.5;

                                }
                                for (int i = 0; i < areas.size(); i++) {

                                    if (spawned) break;
                                    Area currentArea = areas.get(i);
                                    SpawnArea currentSpawnArea = SpawnAreaHandler.areaMap.get(currentArea);
                                    if (currentSpawnArea.getNaturalSpawnerSettings().doesLimitSpawns()) {

                                        if (BattleRegistry.getBattle(player) != null) break;

                                    }
                                    AreaSpawns spawns = SpawnAreaHandler.areaSpawnMap.get(currentSpawnArea);
                                    if (spawns.getNaturalSpawns().size() > 0) {

                                        Map<Pokemon, Double> pokemon = SpawnBuilder.buildNaturalSpawnsList(time, weather, location, spawns, modifier);
                                        Map<Pokemon, PokemonSpawn> spawnInfoMap = SpawnBuilder.getPokemonNaturalSpawnInfo(time, weather, location, spawns);
                                        Pokemon firstPokemonSpawned = null;
                                        List<Pokemon> toSpawn = new ArrayList<>();
                                        for (Map.Entry<Pokemon, Double> p : pokemon.entrySet()) {

                                            if (firstPokemonSpawned == null) {

                                                if (RandomHelper.getRandomChance(p.getValue())) {

                                                    firstPokemonSpawned = p.getKey();
                                                    toSpawn.add(p.getKey());

                                                }

                                            } else {

                                                if (p.getKey().getSpecies() == firstPokemonSpawned.getSpecies()) {

                                                    toSpawn.add(p.getKey());

                                                }

                                            }

                                        }
                                        AreaNaturalSpawnEvent event = new AreaNaturalSpawnEvent(player, currentArea, toSpawn);
                                        MinecraftForge.EVENT_BUS.post(event);
                                        if (!event.isCanceled()) {

                                            spawned = true;
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

                                                int level = poke.getPokemonLevel();
                                                if (Hustle.applies(playersPokemon) || Pressure.applies(playersPokemon) || VitalSpirit.applies(playersPokemon)) {

                                                    level = Hustle.tryHustle(level, spawnInfoMap.get(poke));

                                                }
                                                poke.setLevel(level);
                                                poke.setLevelNum(level);

                                                HeldItemUtils.tryApplyHeldItem(poke, playersPokemon);

                                                if (RandomHelper.getRandomChance(50)) {

                                                    spawnX = x + RandomHelper.getRandomNumberBetween(1, 4);

                                                } else {

                                                    spawnX = x - RandomHelper.getRandomNumberBetween(1, 4);

                                                }
                                                if (RandomHelper.getRandomChance(50)) {

                                                    spawnZ = z + RandomHelper.getRandomNumberBetween(1, 4);

                                                } else {

                                                    spawnZ = z - RandomHelper.getRandomNumberBetween(1, 4);

                                                }

                                                BlockPos spawnPosition = new BlockPos(spawnX, spawnY, spawnZ);
                                                List<Area> areasAtSpawn = AreaHandler.getFromLocation(spawnX, spawnY, spawnZ, player.world);
                                                if (areasAtSpawn.size() == 0) continue;
                                                PixelmonEntity pixelmon = poke.getOrCreatePixelmon(world, spawnX, spawnY + 1.5, spawnZ);
                                                if (currentSpawnArea.getNaturalSpawnerSettings().doesClearSpawns()) {

                                                    Map<UUID, List<PixelmonEntity>> spawnedMap = new HashMap<>();
                                                    if (pokemonSpawnedMap.containsKey(currentArea)) {

                                                        spawnedMap = pokemonSpawnedMap.get(currentArea);

                                                    }
                                                    List<PixelmonEntity> spawnedPokemon = new ArrayList<>();
                                                    if (spawnedMap.containsKey(player.getUniqueID())) {

                                                        spawnedPokemon = spawnedMap.get(player.getUniqueID());

                                                    }

                                                    spawnedPokemon.add(pixelmon);
                                                    spawnedMap.put(player.getUniqueID(), spawnedPokemon);
                                                    pokemonSpawnedMap.put(currentArea, spawnedMap);

                                                }
                                                Pokemon finalPoke = poke;
                                                int finalSpawnX = spawnX;
                                                int finalSpawnZ = spawnZ;
                                                if (spawnInfoMap.get(poke).isHostile()) {

                                                    double defaultAtk = pixelmon.getAttributeValue(Attributes.ATTACK_DAMAGE);
                                                    double defaultSpd = pixelmon.getAttributeValue(Attributes.ATTACK_SPEED);
                                                    SetHostileEvent hostileEvent = new SetHostileEvent(player, pixelmon, defaultAtk, defaultSpd, 1.5);
                                                    MinecraftForge.EVENT_BUS.post(hostileEvent);
                                                    if (!hostileEvent.isCanceled()) HostilePokemon.setHostile(pixelmon, player, hostileEvent.getAttackDamage(), hostileEvent.getAttackSpeed(), hostileEvent.getMovementSpeed());

                                                }
                                                player.world.getServer().deferTask(() -> {


                                                    pixelmon.setSpawnLocation(pixelmon.getDefaultSpawnLocation());
                                                    pixelmon.setPosition(finalSpawnX, spawnY + 1.5, finalSpawnZ);
                                                    player.world.addEntity(pixelmon);
                                                    if (currentSpawnArea.getNaturalSpawnerSettings().doesDespawnAfterBattle()) {

                                                        spawnedPokemonUUIDs.add(pixelmon.getUniqueID());

                                                    }
                                                    pixelmon.setPositionAndUpdate(spawnPosition.getX(), spawnPosition.getY() + 1.5, spawnPosition.getZ());
                                                    if (currentSpawnArea.getNaturalSpawnerSettings().getDespawnTimer() > 0) {

                                                        pixelmon.despawnCounter = currentSpawnArea.getNaturalSpawnerSettings().getDespawnTimer();

                                                    }
                                                    if (toSpawn.size() == 1) {

                                                        // only one Pokemon spawned, so we check for the auto battle stuff
                                                        if (RandomHelper.getRandomChance(currentSpawnArea.getNaturalSpawnerSettings().getAutoBattleChance())) {

                                                            String messageType = "";
                                                            if (finalPoke.isShiny()) {

                                                                messageType = "-Shiny";

                                                            }
                                                            messageType = "Spawn-Message" + messageType;
                                                            if (!player.isCreative() && !player.isSpectator()) {

                                                                if (BattleRegistry.getBattle(player) == null) {

                                                                    String message = currentSpawnArea.getNaturalSpawnerSettings().getMessagesMap().get(messageType);
                                                                    if (!message.equalsIgnoreCase("")) {

                                                                        player.sendMessage(FancyText.getFormattedText(message.replace("%pokemon%", finalPoke.getLocalizedName())), player.getUniqueID());

                                                                    }
                                                                    WildPixelmonParticipant wpp = new WildPixelmonParticipant(pixelmon);
                                                                    PlayerParticipant pp = new PlayerParticipant(player, StorageProxy.getParty(player).getTeam(), 1);
                                                                    BattleRegistry.startBattle(new BattleParticipant[]{wpp}, new BattleParticipant[]{pp}, new BattleRules());

                                                                }

                                                            }

                                                        }

                                                    }

                                                });

                                            }

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
