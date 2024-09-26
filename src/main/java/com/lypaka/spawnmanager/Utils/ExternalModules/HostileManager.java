package com.lypaka.spawnmanager.Utils.ExternalModules;

import com.lypaka.hostilepokemon.API.SetHostileEvent;
import com.lypaka.hostilepokemon.HostilePokemon;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.PokemonSpawn;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;

public class HostileManager {

    public static void tryHostile (PokemonSpawn spawn, PixelmonEntity pixelmon, ServerPlayerEntity player) {

        if (RandomHelper.getRandomChance(spawn.getHostileChance())) {

            double defaultAtk = pixelmon.getAttributeValue(Attributes.ATTACK_DAMAGE);
            double defaultSpd = pixelmon.getAttributeValue(Attributes.ATTACK_SPEED);
            SetHostileEvent hostileEvent = new SetHostileEvent(player, pixelmon, defaultAtk, defaultSpd, 1.5);
            MinecraftForge.EVENT_BUS.post(hostileEvent);
            if (!hostileEvent.isCanceled()) HostilePokemon.setHostile(pixelmon, player, hostileEvent.getAttackDamage(), hostileEvent.getAttackSpeed(), hostileEvent.getMovementSpeed());

        }

    }

}
