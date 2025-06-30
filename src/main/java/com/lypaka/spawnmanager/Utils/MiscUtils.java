package com.lypaka.spawnmanager.Utils;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import net.minecraft.entity.player.ServerPlayerEntity;

public class MiscUtils {

    public static boolean canPlayerBattle (ServerPlayerEntity player) {

        int aliveCount = 0;
        PlayerPartyStorage storage = StorageProxy.getParty(player);
        for (int i = 0; i < 6; i++) {

            Pokemon p = storage.get(i);
            if (p != null) {

                if (!p.isFainted()) aliveCount++;

            }

        }

        return aliveCount > 0;

    }

}
