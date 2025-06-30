package com.lypaka.spawnmanager.Utils;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class MiscUtils {

    public static boolean canPlayerBattle (ServerPlayerEntity player) {

        PlayerPartyStore storage = Cobblemon.INSTANCE.getStorage().getParty(player);
        int aliveCount = 0;
        for (int i = 0; i < 6; i++) {

            Pokemon p = storage.get(i);
            if (p != null) {

                if (!p.isFainted()) aliveCount++;

            }

        }

        return aliveCount > 0;

    }

}
