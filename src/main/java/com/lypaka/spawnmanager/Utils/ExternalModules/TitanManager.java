package com.lypaka.spawnmanager.Utils.ExternalModules;

import com.lypaka.lypakautils.FancyText;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.PokemonSpawn;
import com.lypaka.titanpokemon.API.TitanSpawnEvent;
import com.lypaka.titanpokemon.ConfigGetters;
import com.lypaka.titanpokemon.Titans.Titan;
import com.lypaka.titanpokemon.Titans.TitanHandler;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;

public class TitanManager {

    public static void tryTitan (PokemonSpawn spawn, PixelmonEntity pokemon, ServerPlayerEntity player) {

        if (RandomHelper.getRandomChance(spawn.getTitanChance())) {

            Titan titan = TitanHandler.getFromPokemon(pokemon.getPokemon());
            if (titan == null) return;
            int titanLevel = TitanHandler.getTitanDefeatCount(player, titan.getID());
            TitanSpawnEvent titanSpawnEvent = new TitanSpawnEvent(player, pokemon.getPokemon(), titan, titanLevel);
            MinecraftForge.EVENT_BUS.post(titanSpawnEvent);
            if (!titanSpawnEvent.isCanceled()) {

                int playersHighestLevel = StorageProxy.getParty(player).getHighestLevel();
                int modifier = 15;
                titanLevel = titanSpawnEvent.getTitanLevel();
                if (titanLevel == 2) modifier = 25;
                if (titanLevel >= 3) modifier = 50;
                int mod = modifier + playersHighestLevel;
                pokemon.getPokemon().setLevel(mod);
                TitanHandler.setTitanPokemon(pokemon, titanLevel);
                if (ConfigGetters.lockToUUID) {

                    TitanHandler.lockTitan(pokemon.getPokemon(), player);

                }
                player.sendMessage(FancyText.getFormattedText(ConfigGetters.message
                        .replace("%titanID%", TitanHandler.getPrettyName(titan))
                        .replace("%pokemonName%", pokemon.getSpecies().getName())
                ), player.getUniqueID());

            }

        }

    }

}
