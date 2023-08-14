package com.lypaka.spawnmanager.Listeners;

import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.SpawnArea;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.AreaSpawns;
import com.lypaka.spawnmanager.Spawners.*;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class BattleEndListener {

    @SubscribeEvent
    public void onBattleEnd (BattleEndEvent event) {

        WildPixelmonParticipant wpp;
        PlayerParticipant pp;
        BattleController bcb = event.getBattleController();

        if (bcb.participants.get(0) instanceof WildPixelmonParticipant && bcb.participants.get(1) instanceof PlayerParticipant) {

            wpp = (WildPixelmonParticipant) bcb.participants.get(0);
            pp = (PlayerParticipant) bcb.participants.get(1);

        } else if (bcb.participants.get(0) instanceof PlayerParticipant && bcb.participants.get(1) instanceof WildPixelmonParticipant) {

            wpp = (WildPixelmonParticipant) bcb.participants.get(1);
            pp = (PlayerParticipant) bcb.participants.get(0);

        } else {

            return;

        }

        ServerPlayerEntity player = pp.player;
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        int z = player.getPosition().getZ();
        PixelmonEntity pixelmon = wpp.controlledPokemon.get(0).entity;
        List<Area> areas = AreaHandler.getFromLocation(x, y, z, player.world);
        if (areas.size() == 0) return;

        Area currentArea = AreaHandler.getHighestPriorityArea(x, y, z, player.world);
        String spawner = null;
        if (FishSpawner.spawnedPokemonUUIDs.contains(pixelmon.getUniqueID())) {

            spawner = "Fish";

        } else if (GrassSpawner.spawnedPokemonUUIDs.contains(pixelmon.getUniqueID())) {

            spawner = "Grass";

        } else if (HeadbuttSpawner.spawnedPokemonUUIDs.contains(pixelmon.getUniqueID())) {

            spawner = "Headbutt";

        } else if (NaturalSpawner.spawnedPokemonUUIDs.contains(pixelmon.getUniqueID())) {

            spawner = "Natural";

        } else if (RockSmashSpawner.spawnedPokemonUUIDs.contains(pixelmon.getUniqueID())) {

            spawner = "RockSmash";

        } else if (SurfSpawner.spawnedPokemonUUIDs.contains(pixelmon.getUniqueID())) {

            spawner = "Surf";

        }
        if (spawner == null) return;
        SpawnArea areaSpawns = SpawnAreaHandler.areaMap.get(currentArea);
        switch (spawner) {

            case "Fish":
                if (areaSpawns.getFishSpawnerSettings().doesDespawnAfterBattle()) {

                    FishSpawner.spawnedPokemonUUIDs.removeIf(entry -> {

                        if (entry.toString().equalsIgnoreCase(pixelmon.getUniqueID().toString())) {

                            if (pixelmon.battleController == null) {

                                pixelmon.remove();

                            }
                            return true;

                        }

                        return false;

                    });

                }
                break;

            case "Grass":
                if (areaSpawns.getGrassSpawnerSettings().doesDespawnAfterBattle()) {

                    GrassSpawner.spawnedPokemonUUIDs.removeIf(entry -> {

                        if (entry.toString().equalsIgnoreCase(pixelmon.getUniqueID().toString())) {

                            if (pixelmon.battleController == null) {

                                pixelmon.remove();

                            }
                            return true;

                        }

                        return false;

                    });

                }

            case "Headbutt":
                if (areaSpawns.getHeadbuttSpawnerSettings().doesDespawnAfterBattle()) {

                    HeadbuttSpawner.spawnedPokemonUUIDs.removeIf(entry -> {

                        if (entry.toString().equalsIgnoreCase(pixelmon.getUniqueID().toString())) {

                            if (pixelmon.battleController == null) {

                                pixelmon.remove();

                            }
                            return true;

                        }

                        return false;

                    });

                }
                break;

            case "Natural":
                if (areaSpawns.getNaturalSpawnerSettings().doesDespawnAfterBattle()) {

                    NaturalSpawner.spawnedPokemonUUIDs.removeIf(entry -> {

                        if (entry.toString().equalsIgnoreCase(pixelmon.getUniqueID().toString())) {

                            if (pixelmon.battleController == null) {

                                pixelmon.remove();

                            }
                            return true;

                        }

                        return false;

                    });

                }
                break;

            case "RockSmash":
                if (areaSpawns.getRockSmashSpawnerSettings().doesDespawnAfterBattle()) {

                    RockSmashSpawner.spawnedPokemonUUIDs.removeIf(entry -> {

                        if (entry.toString().equalsIgnoreCase(pixelmon.getUniqueID().toString())) {

                            if (pixelmon.battleController == null) {

                                pixelmon.remove();

                            }
                            return true;

                        }

                        return false;

                    });

                }
                break;

            case "Surf":
                if (areaSpawns.getSurfSpawnerSettings().doesDespawnAfterBattle()) {

                    SurfSpawner.spawnedPokemonUUIDs.removeIf(entry -> {

                        if (entry.toString().equalsIgnoreCase(pixelmon.getUniqueID().toString())) {

                            if (pixelmon.battleController == null) {

                                pixelmon.remove();

                            }
                            return true;

                        }

                        return false;

                    });

                }
                break;

        }

    }

}
