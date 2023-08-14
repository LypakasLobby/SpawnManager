package com.lypaka.spawnmanager.API;

import com.lypaka.areamanager.Areas.Area;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class AreaGrassSpawnEvent extends Event {

    private final ServerPlayerEntity player;
    private final Area area;
    private Pokemon pokemon;

    public AreaGrassSpawnEvent (ServerPlayerEntity player, Area area, Pokemon pokemon) {

        this.player = player;
        this.area = area;
        this.pokemon = pokemon;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public Area getArea() {

        return this.area;

    }

    public Pokemon getPokemon() {

        return this.pokemon;

    }

    public void setPokemon (Pokemon pokemon) {

        this.pokemon = pokemon;

    }

}
