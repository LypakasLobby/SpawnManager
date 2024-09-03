package com.lypaka.spawnmanager.Utils;

import com.lypaka.hostilepokemon.API.SetHostileEvent;
import com.lypaka.hostilepokemon.HostilePokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;

public class HostileManager {

    public static void setHostile (PixelmonEntity pixelmon, ServerPlayerEntity player) {

        double defaultAtk = pixelmon.getAttributeValue(Attributes.ATTACK_DAMAGE);
        double defaultSpd = pixelmon.getAttributeValue(Attributes.ATTACK_SPEED);
        SetHostileEvent hostileEvent = new SetHostileEvent(player, pixelmon, defaultAtk, defaultSpd, 1.5);
        MinecraftForge.EVENT_BUS.post(hostileEvent);
        if (!hostileEvent.isCanceled()) HostilePokemon.setHostile(pixelmon, player, hostileEvent.getAttackDamage(), hostileEvent.getAttackSpeed(), hostileEvent.getMovementSpeed());

    }

}
