package com.lypaka.spawnmanager.Spawners;

import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.lypakautils.API.PlayerMovementEvent;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.spawnmanager.API.AreaSurfSpawnEvent;
import com.lypaka.spawnmanager.SpawnAreas.SpawnArea;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.AreaSpawns;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.PokemonSpawn;
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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SurfSpawner {

    public static List<UUID> spawnedPokemonUUIDs = new ArrayList<>(); // used for battle end event listener to check for to despawn Pokemon or not

    @SubscribeEvent
    public void onWaterMove (PlayerMovementEvent.Swim event) {

        ServerPlayerEntity player = event.getPlayer();
        if (!player.isCreative() && !player.isSpectator()) {

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
            Pokemon toSpawn = null;
            Pokemon playersPokemon = null;
            PlayerPartyStorage party = StorageProxy.getParty(player);
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

            String blockID = world.getBlockState(player.getPosition()).getBlock().getRegistryName().toString();
            for (int i = 0; i < sortedAreas.size(); i++) {

                Area currentArea = sortedAreas.get(i);
                SpawnArea spawnArea = SpawnAreaHandler.areaMap.get(currentArea);
                if (spawnArea.getSurfSpawnerSettings().getBlockIDs().contains(blockID)) {

                    if (spawnArea.getSurfSpawnerSettings().doesAutoBattle() && BattleRegistry.getBattle(player) != null) break;
                    AreaSpawns spawns = SpawnAreaHandler.areaSpawnMap.get(spawnArea);
                    if (spawns.getSurfSpawns().size() > 0) {

                        Map<Pokemon, Double> pokemon = SpawnBuilder.buildSurfSpawnsList(time, weather, spawns, modifier);
                        Map<Pokemon, PokemonSpawn> spawnInfoMap = SpawnBuilder.getPokemonSurfSpawnInfo(time, weather, spawns);
                        for (Map.Entry<Pokemon, Double> p : pokemon.entrySet()) {

                            if (toSpawn == null) {

                                if (RandomHelper.getRandomChance(p.getValue())) {

                                    toSpawn = p.getKey();
                                    break;

                                }

                            }

                        }
                        AreaSurfSpawnEvent surfSpawnEvent = new AreaSurfSpawnEvent(player, currentArea, toSpawn);
                        MinecraftForge.EVENT_BUS.post(surfSpawnEvent);
                        if (!surfSpawnEvent.isCanceled()) {

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

                            int level = toSpawn.getPokemonLevel();
                            if (Hustle.applies(playersPokemon) || Pressure.applies(playersPokemon) || VitalSpirit.applies(playersPokemon)) {

                                level = Hustle.tryHustle(level, spawnInfoMap.get(toSpawn));

                            }
                            toSpawn.setLevel(level);
                            toSpawn.setLevelNum(level);

                            HeldItemUtils.tryApplyHeldItem(toSpawn, playersPokemon);

                            int spawnX = player.getPosition().getX();
                            int spawnY = player.getPosition().getY();
                            int spawnZ = player.getPosition().getZ();

                            BlockPos spawnPosition = new BlockPos(spawnX, spawnY, spawnZ);
                            List<Area> areasAtSpawn = AreaHandler.getFromLocation(spawnX, spawnY, spawnZ, player.world);
                            if (areasAtSpawn.size() == 0) continue;
                            PixelmonEntity pixelmon = toSpawn.getOrCreatePixelmon(world, spawnX, spawnY + 1.5, spawnZ);
                            Pokemon finalToSpawn = toSpawn;
                            player.world.getServer().deferTask(() -> {

                                pixelmon.setSpawnLocation(pixelmon.getDefaultSpawnLocation());
                                player.world.addEntity(pixelmon);
                                if (spawnArea.getSurfSpawnerSettings().doesDespawnAfterBattle()) {

                                    spawnedPokemonUUIDs.add(pixelmon.getUniqueID());

                                }
                                pixelmon.setPositionAndUpdate(spawnPosition.getX(), spawnPosition.getY() + 1.5, spawnPosition.getZ());
                                if (spawnArea.getSurfSpawnerSettings().doesAutoBattle()) {

                                    String messageType = "";
                                    if (finalToSpawn.isShiny()) {

                                        messageType = "-Shiny";

                                    }
                                    messageType = "Spawn-Message" + messageType;
                                    if (BattleRegistry.getBattle(player) == null) {

                                        String message = spawnArea.getSurfSpawnerSettings().getMessagesMap().get(messageType);
                                        if (!message.equalsIgnoreCase("")) {

                                            player.sendMessage(FancyText.getFormattedText(message.replace("%pokemon%", finalToSpawn.getLocalizedName())), player.getUniqueID());

                                        }
                                        WildPixelmonParticipant wpp = new WildPixelmonParticipant(pixelmon);
                                        PlayerParticipant pp = new PlayerParticipant(player, StorageProxy.getParty(player).getTeam(), 1);
                                        BattleRegistry.startBattle(new BattleParticipant[]{wpp}, new BattleParticipant[]{pp}, new BattleRules());

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
