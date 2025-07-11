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
import com.lypaka.lypakautils.API.PlayerLandMovementCallback;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.RandomHandler;
import com.lypaka.lypakautils.Handlers.WorldTimeHandler;
import com.lypaka.spawnmanager.Listeners.TickListener;
import com.lypaka.spawnmanager.SpawnAreas.SpawnArea;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.AreaSpawns;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.PokemonSpawn;
import com.lypaka.spawnmanager.Utils.ExternalAbilities.*;
import com.lypaka.spawnmanager.Utils.HeldItemUtils;
import com.lypaka.spawnmanager.Utils.MiscUtils;
import com.lypaka.spawnmanager.Utils.PokemonSpawnBuilder;
import kotlin.Unit;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class CaveSpawner implements PlayerLandMovementCallback {

    public static List<UUID> spawnedPokemonUUIDs = new ArrayList<>(); // used for battle end event listener to check for to despawn Pokemon or not

    @Override
    public void onPlayerMove (ServerPlayerEntity player, int steps) {

        if (!player.isCreative() && !player.isSpectator()) {

            if (TickListener.timeBetweenCaveSpawns.containsKey(player.getUuid())) return;
            TickListener.timeBetweenCaveSpawns.put(player.getUuid(), 0);
            int x = player.getBlockPos().getX();
            int y = player.getBlockPos().getY();
            int z = player.getBlockPos().getZ();
            World world = player.getWorld();
            List<Area> areas = AreaHandler.getSortedAreas(x, y, z, world);
            if (areas.isEmpty()) return;

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
            Pokemon toSpawn = null;
            Pokemon playersPokemon = null;
            PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
            for (int i = 0; i < 6; i++) {

                Pokemon p = party.get(i);
                if (p != null) {

                    playersPokemon = p;
                    break;

                }

            }
            List<Area> sortedAreas = AreaHandler.getSortedAreas(x, y, z, world);
            double modifier = 1.0;
            if (ArenaTrap.applies(playersPokemon) || Illuminate.applies(playersPokemon) || NoGuard.applies(playersPokemon)) {

                modifier = 2.0;

            } else if (Infiltrator.applies(playersPokemon) || QuickFeet.applies(playersPokemon) || Stench.applies(playersPokemon) || WhiteSmoke.applies(playersPokemon)) {

                modifier = 0.5;

            }

            BlockPos pos = new BlockPos(x, y - 1, z);
            BlockState state = world.getBlockState(pos);
            String blockID = Registries.BLOCK.getId(state.getBlock()).toString();
            if (blockID.equalsIgnoreCase("air")) location = "air";
            if (blockID.contains("water") || blockID.contains("lava")) location = "water";
            for (int i = 0; i < sortedAreas.size(); i++) {

                Area currentArea = sortedAreas.get(i);
                SpawnArea spawnArea = SpawnAreaHandler.areaMap.get(currentArea);
                if (!RandomHandler.getRandomChance(spawnArea.getCaveSpawnerSettings().getSpawnChance())) continue;
                if (spawnArea.getCaveSpawnerSettings().getBlockIDs().contains(blockID)) {

                    if (spawnArea.getCaveSpawnerSettings().doesAutoBattle() && BattleRegistry.INSTANCE.getBattleByParticipatingPlayer(player) != null) break;
                    if (spawnArea.getCaveSpawnerSettings().doesAutoBattle() && !MiscUtils.canPlayerBattle(player)) break;
                    AreaSpawns spawns = SpawnAreaHandler.areaSpawnMap.get(spawnArea);
                    if (!spawns.getCaveSpawns().isEmpty()) {

                        Map<PokemonSpawn, Double> pokemon = PokemonSpawnBuilder.buildCaveSpawnList(time, weather, location, spawns, modifier);
                        Map<Pokemon, PokemonSpawn> mapForHustle = new HashMap<>();
                        for (Map.Entry<PokemonSpawn, Double> p : pokemon.entrySet()) {

                            if (toSpawn == null) {

                                if (RandomHandler.getRandomChance(p.getValue())) {

                                    toSpawn = PokemonSpawnBuilder.buildPokemonFromPokemonSpawn(p.getKey());
                                    mapForHustle.put(toSpawn, p.getKey());
                                    break;

                                }

                            } else {

                                break;

                            }

                        }
                        if (Intimidate.applies(playersPokemon) || KeenEye.applies(playersPokemon)) {

                            toSpawn = Intimidate.tryIntimidate(toSpawn, playersPokemon);
                            if (toSpawn == null) continue;

                        }
                        if (FlashFire.applies(playersPokemon)) {

                            toSpawn = FlashFire.tryFlashFire(toSpawn, pokemon);

                        } else if (Harvest.applies(playersPokemon)) {

                            toSpawn = Harvest.tryHarvest(toSpawn, pokemon);

                        } else if (LightningRod.applies(playersPokemon) || Static.applies(playersPokemon)) {

                            toSpawn = LightningRod.tryLightningRod(toSpawn, pokemon);

                        } else if (MagnetPull.applies(playersPokemon)) {

                            toSpawn = MagnetPull.tryMagnetPull(toSpawn, pokemon);

                        } else if (StormDrain.applies(playersPokemon)) {

                            toSpawn = StormDrain.tryStormDrain(toSpawn, pokemon);

                        }

                        if (CuteCharm.applies(playersPokemon)) {

                            CuteCharm.tryApplyCuteCharmEffect(toSpawn, playersPokemon);

                        } else if (Synchronize.applies(playersPokemon)) {

                            Synchronize.applySynchronize(toSpawn, playersPokemon);

                        }

                        if (toSpawn == null) continue;

                        int level = toSpawn.getLevel();
                        if (Hustle.applies(playersPokemon) || Pressure.applies(playersPokemon) || VitalSpirit.applies(playersPokemon)) {

                            level = Hustle.tryHustle(level, mapForHustle.get(toSpawn));

                        }
                        toSpawn.setLevel(level);

                        HeldItemUtils.tryApplyHeldItem(toSpawn, playersPokemon);

                        int spawnX = player.getBlockX();
                        int spawnY = player.getBlockY();
                        int spawnZ = player.getBlockZ();

                        List<Area> areasAtSpawn = AreaHandler.getFromLocation(spawnX, spawnY, spawnZ, player.getWorld());
                        if (areasAtSpawn.isEmpty()) continue;
                        PokemonEntity entity = new PokemonEntity(player.getWorld(), toSpawn, CobblemonEntities.POKEMON);
                        entity.setPosition(spawnX, spawnY + 1.5, spawnZ);
                        Pokemon finalToSpawn = toSpawn;
                        player.getWorld().getServer().executeSync(() -> {

                            player.getWorld().spawnEntity(entity);
                            if (spawnArea.getCaveSpawnerSettings().doesDespawnAfterBattle()) {

                                spawnedPokemonUUIDs.add(entity.getUuid());

                            }
                            if (spawnArea.getCaveSpawnerSettings().doesAutoBattle()) {

                                String messageType = "";
                                if (finalToSpawn.getShiny()) {

                                    messageType = "-Shiny";

                                }
                                messageType = "Spawn-Message" + messageType;
                                String message = spawnArea.getCaveSpawnerSettings().getMessagesMap().get(messageType);
                                if (!message.equalsIgnoreCase("")) {

                                    player.sendMessage(FancyTextHandler.getFormattedText(message.replace("%pokemon%", finalToSpawn.getSpecies().getName())), true);

                                }
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

                        });

                    } else {

                        break;

                    }

                }

            }

        }

    }

}