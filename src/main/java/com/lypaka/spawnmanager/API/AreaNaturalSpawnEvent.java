package com.lypaka.spawnmanager.API;

import com.lypaka.areamanager.Areas.Area;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

@Cancelable
public class AreaNaturalSpawnEvent extends Event {

    private final ServerPlayerEntity player;
    private final Area area;
    private List<Pokemon> pokemonSpawned;

    public AreaNaturalSpawnEvent (ServerPlayerEntity player, Area area, List<Pokemon> pokemonSpawned) {

        this.player = player;
        this.area = area;
        this.pokemonSpawned = pokemonSpawned;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public Area getArea() {

        return this.area;

    }

    public List<Pokemon> getPokemonSpawned() {

        return this.pokemonSpawned;

    }

    public void setPokemonSpawned (List<Pokemon> spawns) {

        this.pokemonSpawned = spawns;

    }
}
