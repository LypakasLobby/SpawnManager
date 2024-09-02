package com.lypaka.spawnmanager.Spawners;

import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.lypakautils.Listeners.JoinListener;
import com.lypaka.spawnmanager.API.AreaRockSmashSpawnEvent;
import com.lypaka.spawnmanager.SpawnAreas.SpawnArea;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.AreaSpawns;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.PokemonSpawn;
import com.lypaka.spawnmanager.Utils.ExternalAbilities.*;
import com.lypaka.spawnmanager.Utils.HeldItemUtils;
import com.lypaka.spawnmanager.Utils.SpawnBuilder;
import com.pixelmonmod.pixelmon.api.events.PokeBallImpactEvent;
import com.pixelmonmod.pixelmon.api.events.moveskills.UseMoveSkillEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.entities.pixelmon.StatueEntity;
import com.pixelmonmod.pixelmon.entities.pokeballs.OccupiedPokeBallEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.time.LocalDateTime;
import java.util.*;

public class RockSmashSpawner {

    public static List<UUID> spawnedPokemonUUIDs = new ArrayList<>();
    public static Map<Area, Map<UUID, List<PixelmonEntity>>> pokemonSpawnedMap = new HashMap<>();

    @SubscribeEvent
    public void onPokeBallHit (PokeBallImpactEvent event) {

        UUID uuid = event.getPokeBall().getOwnerId();
        if (!JoinListener.playerMap.containsKey(uuid)) return;
        ServerPlayerEntity player = JoinListener.playerMap.get(uuid);
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        int z = player.getPosition().getZ();
        World world = player.world;
        List<Area> areas = AreaHandler.getSortedAreas(x, y, z, world);
        if (areas.size() == 0) return;

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
        UUID pokemonUUID = event.getPokeBall().getPokeUUID();
        PlayerPartyStorage party = StorageProxy.getParty(player);
        Pokemon toSendOut = null;
        Pokemon firstPokemon = null;
        for (int i = 0; i < 6; i++) {

            Pokemon pokemon = party.get(i);
            if (pokemon != null) {

                firstPokemon = pokemon;
                if (pokemon.getUUID().toString().equalsIgnoreCase(pokemonUUID.toString())) {

                    toSendOut = pokemon;
                    break;

                }

            }

        }
        if (toSendOut == null) return; // This should never happen but :shrug:
        double modifier = 1.0;
        if (ArenaTrap.applies(firstPokemon) || Illuminate.applies(firstPokemon) || NoGuard.applies(firstPokemon)) {

            modifier = 2.0;

        } else if (Infiltrator.applies(firstPokemon) || QuickFeet.applies(firstPokemon) || Stench.applies(firstPokemon) || WhiteSmoke.applies(firstPokemon)) {

            modifier = 0.5;

        }
        if (event.getBlockHit().isPresent()) {

            if (event.getPokeBall() instanceof OccupiedPokeBallEntity) {

                Material blockMaterial = event.getBlockHit().get().getMaterial();
                String blockID = event.getBlockHit().get().getBlock().getRegistryName().toString();

                if (blockMaterial == Material.ROCK) {

                    for (int i = 0; i < areas.size(); i++) {

                        Area currentArea = areas.get(i);
                        SpawnArea spawnArea = SpawnAreaHandler.areaMap.get(currentArea);
                        if (spawnArea.getRockSmashSpawnerSettings().doesUsePixelmonsSystem()) continue;
                        if (spawnArea.getRockSmashSpawnerSettings().getCooldown() > 0) {

                            LocalDateTime now = LocalDateTime.now();
                            if (toSendOut.getPersistentData().contains("RockSmashCooldown")) {

                                String cooldownTimer = toSendOut.getPersistentData().get("RockSmashCooldown").getString();
                                LocalDateTime expires = LocalDateTime.parse(cooldownTimer);
                                if (!expires.isAfter(now)) continue;

                            }

                            String timeExpires = now.plusSeconds(spawnArea.getRockSmashSpawnerSettings().getCooldown()).toString();
                            toSendOut.getPersistentData().putString("RockSmashCooldown", timeExpires);

                        }
                        if (spawnArea.getRockSmashSpawnerSettings().doesRequireMove()) {

                            boolean hasAttack = false;
                            for (Attack a : toSendOut.getMoveset().attacks) {

                                if (a == null) continue;
                                if (a.getActualMove().getAttackName().equalsIgnoreCase("Rock Smash") || a.getActualMove().getAttackName().equalsIgnoreCase("RockSmash")) {

                                    hasAttack = true;
                                    break;

                                }

                            }
                            if (!hasAttack) return;

                        }

                        AreaSpawns spawns = SpawnAreaHandler.areaSpawnMap.get(spawnArea);
                        if (spawns.getRockSmashSpawns().size() > 0) {

                            Map<Pokemon, Double> pokemonMap = SpawnBuilder.buildRockSmashSpawns(time, weather, blockID, spawns, modifier);
                            Map<Pokemon, PokemonSpawn> spawnInfoMap = SpawnBuilder.getRockSmashSpawnInfo(time, weather, blockID, spawns);

                            for (Map.Entry<Pokemon, Double> pEntry : pokemonMap.entrySet()) {

                                if (RandomHelper.getRandomChance(pEntry.getValue())) {

                                    Pokemon poke = pEntry.getKey();
                                    if (Intimidate.applies(firstPokemon) || KeenEye.applies(firstPokemon)) {

                                        poke = Intimidate.tryIntimidate(poke, firstPokemon);
                                        if (poke == null) continue;

                                    }
                                    if (FlashFire.applies(firstPokemon)) {

                                        poke = FlashFire.tryFlashFire(poke, pokemonMap);

                                    } else if (Harvest.applies(firstPokemon)) {

                                        poke = Harvest.tryHarvest(poke, pokemonMap);

                                    } else if (LightningRod.applies(firstPokemon) || Static.applies(firstPokemon)) {

                                        poke = LightningRod.tryLightningRod(poke, pokemonMap);

                                    } else if (MagnetPull.applies(firstPokemon)) {

                                        poke = MagnetPull.tryMagnetPull(poke, pokemonMap);

                                    } else if (StormDrain.applies(firstPokemon)) {

                                        poke = StormDrain.tryStormDrain(poke, pokemonMap);

                                    }

                                    if (CuteCharm.applies(firstPokemon)) {

                                        CuteCharm.tryApplyCuteCharmEffect(poke, firstPokemon);

                                    } else if (Synchronize.applies(firstPokemon)) {

                                        Synchronize.applySynchronize(poke, firstPokemon);

                                    }

                                    int level = poke.getPokemonLevel();
                                    if (Hustle.applies(firstPokemon) || Pressure.applies(firstPokemon) || VitalSpirit.applies(firstPokemon)) {

                                        level = Hustle.tryHustle(level, spawnInfoMap.get(poke));

                                    }
                                    poke.setLevel(level);
                                    poke.setLevelNum(level);

                                    HeldItemUtils.tryApplyHeldItem(poke, firstPokemon);

                                    AreaRockSmashSpawnEvent spawnEvent = new AreaRockSmashSpawnEvent(player, currentArea, toSendOut, event.getBlockPosHit().get(), poke);
                                    MinecraftForge.EVENT_BUS.post(spawnEvent);
                                    if (!spawnEvent.isCanceled()) {

                                        if (spawnEvent.getToSpawn() != null) {

                                            // we're just gonna spawn the Pokemon on the player to avoid having to do RNGs and maths and shit
                                            PixelmonEntity pixelmon = spawnEvent.getToSpawn().getOrCreatePixelmon(world, x, y, z);
                                            player.world.getServer().deferTask(() -> {

                                                player.world.addEntity(pixelmon);

                                            });
                                            if (spawnArea.getRockSmashSpawnerSettings().doesRequireMove()) {

                                                if (spawnArea.getRockSmashSpawnerSettings().doesReducePP()) {

                                                    int index = -1;
                                                    for (Attack a : toSendOut.getMoveset().attacks) {

                                                        index++;
                                                        if (a == null) continue;
                                                        if (a.getActualMove().getAttackName().equalsIgnoreCase("RockSmash") || a.getActualMove().getAttackName().equalsIgnoreCase("Rock Smash")) {

                                                            if (toSendOut.getMoveset().get(index).pp >= 1) {

                                                                toSendOut.getMoveset().get(index).pp = toSendOut.getMoveset().get(index).pp - 1;
                                                                break;

                                                            } else {

                                                                return;

                                                            }

                                                        }

                                                    }

                                                }

                                            }
                                            if (spawnArea.getRockSmashSpawnerSettings().getAutoBattleChance() > 0) {

                                                if (RandomHelper.getRandomChance(spawnArea.getRockSmashSpawnerSettings().getAutoBattleChance())) {

                                                    if (BattleRegistry.getBattle(player) == null) {

                                                        WildPixelmonParticipant wpp = new WildPixelmonParticipant(pixelmon);
                                                        PlayerParticipant pp = new PlayerParticipant(player, StorageProxy.getParty(player).getTeam(), 1);
                                                        BattleRegistry.startBattle(new BattleParticipant[]{wpp}, new BattleParticipant[]{pp}, new BattleRules());

                                                    }

                                                }

                                            }
                                            if (spawnArea.getRockSmashSpawnerSettings().doesDespawnAfterBattle()) {

                                                spawnedPokemonUUIDs.add(pixelmon.getUniqueID());

                                            } else {

                                                if (spawnArea.getRockSmashSpawnerSettings().getDespawnTimer() > 0) {

                                                    pixelmon.despawnCounter = spawnArea.getRockSmashSpawnerSettings().getDespawnTimer();

                                                }

                                            }
                                            if (spawnArea.getRockSmashSpawnerSettings().doesClearSpawns()) {

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
                                            break;

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

        } else {

            if (event.getEntityHit().isPresent()) {

                if (event.getPokeBall() instanceof OccupiedPokeBallEntity) {

                    Entity hitEntity = event.getEntityHit().get();
                    if (hitEntity instanceof StatueEntity) {

                        StatueEntity statue = (StatueEntity) hitEntity;
                        String species = statue.getSpecies().getName();
                        String entityID = "pixelmon:" + species.toLowerCase();
                        for (int i = 0; i < areas.size(); i++) {

                            Area currentArea = areas.get(i);
                            SpawnArea spawnArea = SpawnAreaHandler.areaMap.get(currentArea);
                            if (spawnArea.getRockSmashSpawnerSettings().doesUsePixelmonsSystem()) continue;
                            if (!spawnArea.getRockSmashSpawnerSettings().getCustomRockSmashRockIDs().contains(entityID)) continue;
                            if (spawnArea.getRockSmashSpawnerSettings().getCooldown() > 0) {

                                LocalDateTime now = LocalDateTime.now();
                                if (toSendOut.getPersistentData().contains("RockSmashCooldown")) {

                                    String cooldownTimer = toSendOut.getPersistentData().get("RockSmashCooldown").getString();
                                    LocalDateTime expires = LocalDateTime.parse(cooldownTimer);
                                    if (!expires.isAfter(now)) continue;

                                }

                                String timeExpires = now.plusSeconds(spawnArea.getRockSmashSpawnerSettings().getCooldown()).toString();
                                toSendOut.getPersistentData().putString("RockSmashCooldown", timeExpires);

                            }
                            if (spawnArea.getRockSmashSpawnerSettings().doesRequireMove()) {

                                boolean hasAttack = false;
                                for (Attack a : toSendOut.getMoveset().attacks) {

                                    if (a == null) continue;
                                    if (a.getActualMove().getAttackName().equalsIgnoreCase("RockSmash") || a.getActualMove().getAttackName().equalsIgnoreCase("Rock Smash")) {

                                        hasAttack = true;
                                        break;

                                    }

                                }
                                if (!hasAttack) return;

                            }

                            AreaSpawns spawns = SpawnAreaHandler.areaSpawnMap.get(spawnArea);
                            if (spawns.getRockSmashSpawns().size() > 0) {

                                Map<Pokemon, Double> pokemonMap = SpawnBuilder.buildRockSmashSpawns(time, weather, entityID, spawns, modifier);
                                Map<Pokemon, PokemonSpawn> spawnInfoMap = SpawnBuilder.getRockSmashSpawnInfo(time, weather, entityID, spawns);

                                for (Map.Entry<Pokemon, Double> pEntry : pokemonMap.entrySet()) {

                                    if (RandomHelper.getRandomChance(pEntry.getValue())) {

                                        Pokemon poke = pEntry.getKey();
                                        if (Intimidate.applies(firstPokemon) || KeenEye.applies(firstPokemon)) {

                                            poke = Intimidate.tryIntimidate(poke, firstPokemon);
                                            if (poke == null) continue;

                                        }
                                        if (FlashFire.applies(firstPokemon)) {

                                            poke = FlashFire.tryFlashFire(poke, pokemonMap);

                                        } else if (Harvest.applies(firstPokemon)) {

                                            poke = Harvest.tryHarvest(poke, pokemonMap);

                                        } else if (LightningRod.applies(firstPokemon) || Static.applies(firstPokemon)) {

                                            poke = LightningRod.tryLightningRod(poke, pokemonMap);

                                        } else if (MagnetPull.applies(firstPokemon)) {

                                            poke = MagnetPull.tryMagnetPull(poke, pokemonMap);

                                        } else if (StormDrain.applies(firstPokemon)) {

                                            poke = StormDrain.tryStormDrain(poke, pokemonMap);

                                        }

                                        if (CuteCharm.applies(firstPokemon)) {

                                            CuteCharm.tryApplyCuteCharmEffect(poke, firstPokemon);

                                        } else if (Synchronize.applies(firstPokemon)) {

                                            Synchronize.applySynchronize(poke, firstPokemon);

                                        }

                                        int level = poke.getPokemonLevel();
                                        if (Hustle.applies(firstPokemon) || Pressure.applies(firstPokemon) || VitalSpirit.applies(firstPokemon)) {

                                            level = Hustle.tryHustle(level, spawnInfoMap.get(poke));

                                        }
                                        poke.setLevel(level);
                                        poke.setLevelNum(level);

                                        HeldItemUtils.tryApplyHeldItem(poke, firstPokemon);

                                        AreaRockSmashSpawnEvent spawnEvent = new AreaRockSmashSpawnEvent(player, currentArea, toSendOut, event.getBlockPosHit().get(), poke);
                                        MinecraftForge.EVENT_BUS.post(spawnEvent);
                                        if (!spawnEvent.isCanceled()) {

                                            if (spawnEvent.getToSpawn() != null) {

                                                // we're just gonna spawn the Pokemon on the player to avoid having to do RNGs and maths and shit
                                                PixelmonEntity pixelmon = spawnEvent.getToSpawn().getOrCreatePixelmon(world, x, y, z);
                                                player.world.getServer().deferTask(() -> {

                                                    player.world.addEntity(pixelmon);

                                                });
                                                if (spawnArea.getRockSmashSpawnerSettings().doesRequireMove()) {

                                                    if (spawnArea.getRockSmashSpawnerSettings().doesReducePP()) {

                                                        int index = -1;
                                                        for (Attack a : toSendOut.getMoveset().attacks) {

                                                            index++;
                                                            if (a == null) continue;
                                                            if (a.getActualMove().getAttackName().equalsIgnoreCase("RockSmash") || a.getActualMove().getAttackName().equalsIgnoreCase("Rock Smash")) {

                                                                if (toSendOut.getMoveset().get(index).pp >= 1) {

                                                                    toSendOut.getMoveset().get(index).pp = toSendOut.getMoveset().get(index).pp - 1;
                                                                    break;

                                                                } else {

                                                                    return;

                                                                }

                                                            }

                                                        }

                                                    }

                                                }
                                                if (spawnArea.getRockSmashSpawnerSettings().getAutoBattleChance() > 0) {

                                                    if (RandomHelper.getRandomChance(spawnArea.getRockSmashSpawnerSettings().getAutoBattleChance())) {

                                                        if (BattleRegistry.getBattle(player) == null) {

                                                            WildPixelmonParticipant wpp = new WildPixelmonParticipant(pixelmon);
                                                            PlayerParticipant pp = new PlayerParticipant(player, StorageProxy.getParty(player).getTeam(), 1);
                                                            BattleRegistry.startBattle(new BattleParticipant[]{wpp}, new BattleParticipant[]{pp}, new BattleRules());

                                                        }

                                                    }

                                                }
                                                if (spawnArea.getRockSmashSpawnerSettings().doesDespawnAfterBattle()) {

                                                    spawnedPokemonUUIDs.add(pixelmon.getUniqueID());

                                                } else {

                                                    if (spawnArea.getRockSmashSpawnerSettings().getDespawnTimer() > 0) {

                                                        pixelmon.despawnCounter = spawnArea.getRockSmashSpawnerSettings().getDespawnTimer();

                                                    }

                                                }
                                                if (spawnArea.getRockSmashSpawnerSettings().doesClearSpawns()) {

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
                                                break;

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

    }

    @SubscribeEvent
    public void onMoveskill (UseMoveSkillEvent event) {

        if (event.moveSkill.id.equalsIgnoreCase("rock_smash")) {

            ServerPlayerEntity player = (ServerPlayerEntity) event.pixelmon.getOwner();
            int x = event.pixelmon.getPosition().getX();
            int y = event.pixelmon.getPosition().getY();
            int z = event.pixelmon.getPosition().getZ();
            World world = event.pixelmon.world;

            List<Area> areas = AreaHandler.getSortedAreas(x, y, z, world);
            if (areas.size() == 0) return;

            Pokemon pokemon = event.pixelmon.getPokemon();
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
                if (!spawnArea.getRockSmashSpawnerSettings().doesUsePixelmonsSystem()) continue;
                if (spawnArea.getRockSmashSpawnerSettings().doesRequireMove()) {

                    boolean hasAttack = false;
                    for (Attack a : pokemon.getMoveset().attacks) {

                        if (a == null) continue;
                        if (a.getActualMove().getAttackName().equalsIgnoreCase("RockSmash") || a.getActualMove().getAttackName().equalsIgnoreCase("Rock Smash")) {

                            hasAttack = true;
                            break;

                        }

                    }
                    if (!hasAttack) return;

                }

                AreaSpawns spawns = SpawnAreaHandler.areaSpawnMap.get(spawnArea);
                if (spawns.getRockSmashSpawns().size() > 0) {

                    Map<Pokemon, Double> pokemonMap = SpawnBuilder.buildRockSmashSpawns(time, weather, "Any", spawns, modifier);
                    Map<Pokemon, PokemonSpawn> spawnInfoMap = SpawnBuilder.getRockSmashSpawnInfo(time, weather, "Any", spawns);

                    for (Map.Entry<Pokemon, Double> pEntry : pokemonMap.entrySet()) {

                        if (RandomHelper.getRandomChance(pEntry.getValue())) {

                            Pokemon poke = pEntry.getKey();
                            if (Intimidate.applies(playersPokemon) || KeenEye.applies(playersPokemon)) {

                                poke = Intimidate.tryIntimidate(poke, playersPokemon);
                                if (poke == null) continue;

                            }
                            if (FlashFire.applies(playersPokemon)) {

                                poke = FlashFire.tryFlashFire(poke, pokemonMap);

                            } else if (Harvest.applies(playersPokemon)) {

                                poke = Harvest.tryHarvest(poke, pokemonMap);

                            } else if (LightningRod.applies(playersPokemon) || Static.applies(playersPokemon)) {

                                poke = LightningRod.tryLightningRod(poke, pokemonMap);

                            } else if (MagnetPull.applies(playersPokemon)) {

                                poke = MagnetPull.tryMagnetPull(poke, pokemonMap);

                            } else if (StormDrain.applies(playersPokemon)) {

                                poke = StormDrain.tryStormDrain(poke, pokemonMap);

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

                            AreaRockSmashSpawnEvent spawnEvent = new AreaRockSmashSpawnEvent(player, currentArea, pokemon, event.pixelmon.getPosition(), poke);
                            MinecraftForge.EVENT_BUS.post(spawnEvent);
                            if (!spawnEvent.isCanceled()) {

                                if (spawnEvent.getToSpawn() != null) {

                                    // we're just gonna spawn the Pokemon on the player to avoid having to do RNGs and maths and shit
                                    PixelmonEntity pixelmon = spawnEvent.getToSpawn().getOrCreatePixelmon(world, x, y, z);
                                    player.world.getServer().deferTask(() -> {

                                        player.world.addEntity(pixelmon);

                                    });
                                    if (spawnArea.getRockSmashSpawnerSettings().doesRequireMove()) {

                                        if (spawnArea.getRockSmashSpawnerSettings().doesReducePP()) {

                                            int index = -1;
                                            for (Attack a : pokemon.getMoveset().attacks) {

                                                index++;
                                                if (a == null) continue;
                                                if (a.getActualMove().getAttackName().equalsIgnoreCase("RockSmash") || a.getActualMove().getAttackName().equalsIgnoreCase("Rock Smash")) {

                                                    if (pokemon.getMoveset().get(index).pp >= 1) {

                                                        pokemon.getMoveset().get(index).pp = pokemon.getMoveset().get(index).pp - 1;
                                                        break;

                                                    } else {

                                                        return;

                                                    }

                                                }

                                            }

                                        }

                                    }
                                    if (spawnArea.getRockSmashSpawnerSettings().getAutoBattleChance() > 0) {

                                        if (RandomHelper.getRandomChance(spawnArea.getRockSmashSpawnerSettings().getAutoBattleChance())) {

                                            if (BattleRegistry.getBattle(player) == null) {

                                                WildPixelmonParticipant wpp = new WildPixelmonParticipant(pixelmon);
                                                PlayerParticipant pp = new PlayerParticipant(player, StorageProxy.getParty(player).getTeam(), 1);
                                                BattleRegistry.startBattle(new BattleParticipant[]{wpp}, new BattleParticipant[]{pp}, new BattleRules());

                                            }

                                        }

                                    }
                                    if (spawnArea.getRockSmashSpawnerSettings().doesDespawnAfterBattle()) {

                                        spawnedPokemonUUIDs.add(pixelmon.getUniqueID());

                                    } else {

                                        if (spawnArea.getRockSmashSpawnerSettings().getDespawnTimer() > 0) {

                                            pixelmon.despawnCounter = spawnArea.getRockSmashSpawnerSettings().getDespawnTimer();

                                        }

                                    }
                                    if (spawnArea.getRockSmashSpawnerSettings().doesClearSpawns()) {

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
                                    break;

                                }

                            }

                        }

                    }

                }

            }

        }

    }
    
}
