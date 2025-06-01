package com.lypaka.spawnmanager.Listeners;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.actor.PokemonBattleActor;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.SpawnArea;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.Spawners.*;
import kotlin.Unit;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class BattleEndListener {

    //    @field:kotlin.jvm.JvmField public final val BATTLE_FLED: com.cobblemon.mod.common.api.reactive.EventObservable<com.cobblemon.mod.common.api.events.battles.BattleFledEvent> /* compiled code */
    //
    //    @field:kotlin.jvm.JvmField public final val BATTLE_VICTORY: com.cobblemon.mod.common.api.reactive.EventObservable<com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent> /* compiled code */
    //
    //    @field:kotlin.jvm.JvmField public final val BATTLE_FAINTED: com.cobblemon.mod.common.api.reactive.EventObservable<com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent> /* compiled code */

    // Are you fucking kidding me with this shit? What's wrong with BattleEndedEvent?

    public static void registerAll() {

        CobblemonEvents.BATTLE_FAINTED.subscribe(Priority.NORMAL, event -> {

            List<ServerPlayerEntity> players = new ArrayList<>();
            for (BattleActor actor : event.getBattle().getActors()) {

                if (actor instanceof PlayerBattleActor) {

                    PlayerBattleActor playerActor = (PlayerBattleActor) actor;
                    ServerPlayerEntity player = playerActor.getEntity();
                    players.add(player);

                }

            }
            if (players.isEmpty()) return Unit.INSTANCE;
            for (ServerPlayerEntity player : players) {

                int x = player.getBlockPos().getX();
                int y = player.getBlockPos().getY();
                int z = player.getBlockPos().getZ();
                List<PokemonEntity> entities = new ArrayList<>();
                for (BattleActor actor : event.getBattle().getActors()) {

                    if (actor instanceof PokemonBattleActor) {

                        PokemonBattleActor pokemonActor = (PokemonBattleActor) actor;
                        PokemonEntity pokemon = pokemonActor.getEntity();
                        entities.add(pokemon);

                    }

                }
                if (entities.isEmpty()) return Unit.INSTANCE;
                List<Area> areas = AreaHandler.getFromLocation(x, y, z, player.getWorld());
                if (areas.isEmpty()) return Unit.INSTANCE;
                Area currentArea = AreaHandler.getHighestPriorityArea(x, y, z, player.getWorld());
                String spawner = null;
                for (PokemonEntity ent : entities) {

                    if (FishSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                        spawner = "Fish";

                    } else if (GrassSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                        spawner = "Grass";

                    } else if (HeadbuttSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                        spawner = "Headbutt";

                    } else if (NaturalSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                        spawner = "Natural";

                    } else if (RockSmashSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                        spawner = "RockSmash";

                    } else if (SurfSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                        spawner = "Surf";

                    }
                    if (spawner == null) return Unit.INSTANCE;
                    SpawnArea areaSpawns = SpawnAreaHandler.areaMap.get(currentArea);
                    switch (spawner) {

                        case "Fish":
                            if (areaSpawns.getFishSpawnerSettings().doesDespawnAfterBattle()) {

                                FishSpawner.spawnedPokemonUUIDs.removeIf(entry -> {

                                    if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                        if (!ent.isBattling()) {

                                            ent.remove(Entity.RemovalReason.DISCARDED);

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

                                    if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                        if (!ent.isBattling()) {

                                            ent.remove(Entity.RemovalReason.DISCARDED);

                                        }
                                        return true;

                                    }

                                    return false;

                                });

                            }

                        case "Headbutt":
                            if (areaSpawns.getHeadbuttSpawnerSettings().doesDespawnAfterBattle()) {

                                HeadbuttSpawner.spawnedPokemonUUIDs.removeIf(entry -> {

                                    if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                        if (!ent.isBattling()) {

                                            ent.remove(Entity.RemovalReason.DISCARDED);

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

                                    if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                        if (!ent.isBattling()) {

                                            ent.remove(Entity.RemovalReason.DISCARDED);

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

                                    if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                        if (!ent.isBattling()) {

                                            ent.remove(Entity.RemovalReason.DISCARDED);

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

                                    if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                        if (!ent.isBattling()) {

                                            ent.remove(Entity.RemovalReason.DISCARDED);

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

            return Unit.INSTANCE;

        });

        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, event -> {

            List<ServerPlayerEntity> players = new ArrayList<>();
            for (BattleActor actor : event.getBattle().getActors()) {

                if (actor instanceof PlayerBattleActor) {

                    PlayerBattleActor playerActor = (PlayerBattleActor) actor;
                    ServerPlayerEntity player = playerActor.getEntity();
                    players.add(player);

                }

            }
            if (players.isEmpty()) return Unit.INSTANCE;
            for (ServerPlayerEntity player : players) {

                int x = player.getBlockPos().getX();
                int y = player.getBlockPos().getY();
                int z = player.getBlockPos().getZ();
                List<PokemonEntity> entities = new ArrayList<>();
                for (BattleActor actor : event.getBattle().getActors()) {

                    if (actor instanceof PokemonBattleActor) {

                        PokemonBattleActor pokemonActor = (PokemonBattleActor) actor;
                        PokemonEntity pokemon = pokemonActor.getEntity();
                        entities.add(pokemon);

                    }

                }
                if (entities.isEmpty()) return Unit.INSTANCE;
                List<Area> areas = AreaHandler.getFromLocation(x, y, z, player.getWorld());
                if (areas.isEmpty()) return Unit.INSTANCE;
                Area currentArea = AreaHandler.getHighestPriorityArea(x, y, z, player.getWorld());
                String spawner = null;
                for (PokemonEntity ent : entities) {

                    if (FishSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                        spawner = "Fish";

                    } else if (GrassSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                        spawner = "Grass";

                    } else if (HeadbuttSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                        spawner = "Headbutt";

                    } else if (NaturalSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                        spawner = "Natural";

                    } else if (RockSmashSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                        spawner = "RockSmash";

                    } else if (SurfSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                        spawner = "Surf";

                    }
                    if (spawner == null) return Unit.INSTANCE;
                    SpawnArea areaSpawns = SpawnAreaHandler.areaMap.get(currentArea);
                    switch (spawner) {

                        case "Fish":
                            if (areaSpawns.getFishSpawnerSettings().doesDespawnAfterBattle()) {

                                FishSpawner.spawnedPokemonUUIDs.removeIf(entry -> {

                                    if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                        if (!ent.isBattling()) {

                                            ent.remove(Entity.RemovalReason.DISCARDED);

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

                                    if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                        if (!ent.isBattling()) {

                                            ent.remove(Entity.RemovalReason.DISCARDED);

                                        }
                                        return true;

                                    }

                                    return false;

                                });

                            }

                        case "Headbutt":
                            if (areaSpawns.getHeadbuttSpawnerSettings().doesDespawnAfterBattle()) {

                                HeadbuttSpawner.spawnedPokemonUUIDs.removeIf(entry -> {

                                    if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                        if (!ent.isBattling()) {

                                            ent.remove(Entity.RemovalReason.DISCARDED);

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

                                    if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                        if (!ent.isBattling()) {

                                            ent.remove(Entity.RemovalReason.DISCARDED);

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

                                    if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                        if (!ent.isBattling()) {

                                            ent.remove(Entity.RemovalReason.DISCARDED);

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

                                    if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                        if (!ent.isBattling()) {

                                            ent.remove(Entity.RemovalReason.DISCARDED);

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

            return Unit.INSTANCE;

        });

        CobblemonEvents.BATTLE_FLED.subscribe(Priority.NORMAL, event -> {

            ServerPlayerEntity player = event.getPlayer().getEntity();
            int x = player.getBlockPos().getX();
            int y = player.getBlockPos().getY();
            int z = player.getBlockPos().getZ();
            List<PokemonEntity> entities = new ArrayList<>();
            for (BattleActor actor : event.getBattle().getActors()) {

                if (actor instanceof PokemonBattleActor) {

                    PokemonBattleActor pokemonActor = (PokemonBattleActor) actor;
                    PokemonEntity pokemon = pokemonActor.getEntity();
                    entities.add(pokemon);

                }

            }
            if (entities.isEmpty()) return Unit.INSTANCE;
            List<Area> areas = AreaHandler.getFromLocation(x, y, z, player.getWorld());
            if (areas.isEmpty()) return Unit.INSTANCE;
            Area currentArea = AreaHandler.getHighestPriorityArea(x, y, z, player.getWorld());
            String spawner = null;
            for (PokemonEntity ent : entities) {

                if (FishSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                    spawner = "Fish";

                } else if (GrassSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                    spawner = "Grass";

                } else if (HeadbuttSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                    spawner = "Headbutt";

                } else if (NaturalSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                    spawner = "Natural";

                } else if (RockSmashSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                    spawner = "RockSmash";

                } else if (SurfSpawner.spawnedPokemonUUIDs.contains(ent.getUuid())) {

                    spawner = "Surf";

                }
                if (spawner == null) return Unit.INSTANCE;
                SpawnArea areaSpawns = SpawnAreaHandler.areaMap.get(currentArea);
                switch (spawner) {

                    case "Fish":
                        if (areaSpawns.getFishSpawnerSettings().doesDespawnAfterBattle()) {

                            FishSpawner.spawnedPokemonUUIDs.removeIf(entry -> {

                                if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                    if (!ent.isBattling()) {

                                        ent.remove(Entity.RemovalReason.DISCARDED);

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

                                if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                    if (!ent.isBattling()) {

                                        ent.remove(Entity.RemovalReason.DISCARDED);

                                    }
                                    return true;

                                }

                                return false;

                            });

                        }

                    case "Headbutt":
                        if (areaSpawns.getHeadbuttSpawnerSettings().doesDespawnAfterBattle()) {

                            HeadbuttSpawner.spawnedPokemonUUIDs.removeIf(entry -> {

                                if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                    if (!ent.isBattling()) {

                                        ent.remove(Entity.RemovalReason.DISCARDED);

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

                                if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                    if (!ent.isBattling()) {

                                        ent.remove(Entity.RemovalReason.DISCARDED);

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

                                if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                    if (!ent.isBattling()) {

                                        ent.remove(Entity.RemovalReason.DISCARDED);

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

                                if (entry.toString().equalsIgnoreCase(ent.getUuid().toString())) {

                                    if (!ent.isBattling()) {

                                        ent.remove(Entity.RemovalReason.DISCARDED);

                                    }
                                    return true;

                                }

                                return false;

                            });

                        }
                        break;

                }

            }

            return Unit.INSTANCE;

        });

    }

}
