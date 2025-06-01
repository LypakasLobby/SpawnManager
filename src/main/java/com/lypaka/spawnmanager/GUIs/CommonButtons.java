package com.lypaka.spawnmanager.GUIs;

import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.ItemStackHandler;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;

public class CommonButtons {

    public static ItemStack getBorderStack (String id) {

        ItemStack stack = ItemStackHandler.buildFromStringID(id);
        stack.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(""));
        return stack;

    }

}
