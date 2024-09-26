package com.lypaka.spawnmanager.Utils.ExternalModules;

import com.lypaka.lypakautils.FancyText;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.PokemonSpawn;
import com.lypaka.totempokemon.API.TotemSpawnEvent;
import com.lypaka.totempokemon.ConfigGetters;
import com.lypaka.totempokemon.Helpers.NBTHelpers;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;

public class TotemManager {

    public static void tryTotem (PokemonSpawn spawn, PixelmonEntity pixelmon, ServerPlayerEntity player) {

        if (RandomHelper.getRandomChance(spawn.getTotemChance())) {

            TotemSpawnEvent totemSpawnEvent = new TotemSpawnEvent(player, pixelmon.getPokemon());
            MinecraftForge.EVENT_BUS.post(totemSpawnEvent);
            if (!totemSpawnEvent.isCanceled()) {

                NBTHelpers.setTotem(pixelmon);
                player.sendMessage(FancyText.getFormattedText(ConfigGetters.spawnMessage.replace("%pokemonName%", pixelmon.getPokemon().getSpecies().getName())), player.getUniqueID());

            }

        }

    }

}
