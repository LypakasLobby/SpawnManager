package com.lypaka.spawnmanager.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.google.common.reflect.TypeToken;
import com.lypaka.areamanager.Areas.Area;
import com.lypaka.areamanager.Areas.AreaHandler;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.ItemStackBuilder;
import com.lypaka.spawnmanager.ConfigGetters;
import com.lypaka.spawnmanager.SpawnManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainMenu {

    public static void open (ServerPlayerEntity player) throws ObjectMappingException {

        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        int z = player.getPosition().getZ();
        ArrayList<Area> areas = new ArrayList<>(AreaHandler.getSortedAreas(x, y, z, player.world));
        List<Area> allAreasNoModifications = new ArrayList<>(areas);
        if (areas.size() == 0) {

            player.sendMessage(FancyText.getFormattedText("&eNot currently in any Areas!"), player.getUniqueID());
            return;

        }
        Area currentArea = areas.get(0);
        areas.removeIf(a -> a.getName().equalsIgnoreCase(currentArea.getName()));

        int rows = ConfigGetters.mainMenuRows;
        String title = ConfigGetters.mainMenuTitle;
        ChestTemplate template = ChestTemplate.builder(rows).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyText.getFormattedString(title))
                .build();

        Map<String, String> borderStuff = ConfigGetters.mainMenuSlotsMap.get("Border");
        String id = borderStuff.get("ID");
        String[] slotArray = borderStuff.get("Slots").split(", ");
        for (String s : slotArray) {

            page.getTemplate().getSlot(Integer.parseInt(s)).setButton(GooeyButton.builder().display(CommonButtons.getBorderStack(id)).build());

        }

        for (Map.Entry<String, Map<String, String>> entry : ConfigGetters.mainMenuSlotsMap.entrySet()) {

            if (entry.getKey().contains("Slot-")) {

                int slot = Integer.parseInt(entry.getKey().replace("Slot-", ""));
                Map<String, String> data = entry.getValue();
                String displayID = data.get("ID");
                ItemStack displayStack = ItemStackBuilder.buildFromStringID(displayID);
                if (data.containsKey("Display-Name")) {

                    displayStack.setDisplayName(FancyText.getFormattedText(data.get("Display-Name")));

                }
                if (data.containsKey("Lore")) {

                    List<String> displayLore = SpawnManager.configManager.getConfigNode(2, "Main-Menu", "Slots", entry.getKey(), "Lore").getList(TypeToken.of(String.class));
                    ListNBT lore = new ListNBT();
                    List<String> subAreas = new ArrayList<>();
                    for (Area a : areas) {

                        subAreas.add(a.getDisplayName());

                    }

                    for (String l : displayLore) {

                        lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(l
                                .replace("%currentArea%", currentArea.getDisplayName())
                                .replace("%subAreas%", String.join("\n", subAreas))
                        ))));

                    }

                    displayStack.getOrCreateChildTag("display").put("Lore", lore);

                }

                GooeyButton button;
                if (data.containsKey("Opens")) {

                    button = GooeyButton.builder()
                            .display(displayStack)
                            .onClick(() -> {

                                String menuToOpen = data.get("Opens");
                                if (menuToOpen.equalsIgnoreCase("Spawn-Main-Menu")) {

                                    try {

                                        SpawnMainMenu.open(player, allAreasNoModifications);

                                    } catch (ObjectMappingException e) {

                                        e.printStackTrace();

                                    }

                                }

                            })
                            .build();

                } else {

                    button = GooeyButton.builder()
                            .display(displayStack)
                            .build();

                }

                page.getTemplate().getSlot(slot).setButton(button);

            }

        }

        UIManager.openUIForcefully(player, page);

    }

}
