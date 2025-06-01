package com.lypaka.spawnmanager.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.lypaka.areamanager.Areas.Area;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.ItemStackHandler;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import com.lypaka.shadow.google.common.reflect.TypeToken;
import com.lypaka.spawnmanager.ConfigGetters;
import com.lypaka.spawnmanager.SpawnManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpawnMainMenu {

    public static void open (ServerPlayerEntity player, List<Area> areas) throws ObjectMappingException {

        int rows = ConfigGetters.spawnMainMenuRows;
        String title = ConfigGetters.spawnMainMenuTitle;
        ChestTemplate template = ChestTemplate.builder(rows).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyTextHandler.getFormattedText(title))
                .build();

        Map<String, String> borderStuff = ConfigGetters.spawnMainMenuSlotsMap.get("Border");
        String id = borderStuff.get("ID");
        String[] slotArray = borderStuff.get("Slots").split(", ");
        for (String s : slotArray) {

            page.getTemplate().getSlot(Integer.parseInt(s)).setButton(GooeyButton.builder().display(CommonButtons.getBorderStack(id)).build());

        }

        for (Map.Entry<String, Map<String, String>> entry : ConfigGetters.spawnMainMenuSlotsMap.entrySet()) {

            if (entry.getKey().contains("Slot-")) {

                int slot = Integer.parseInt(entry.getKey().replace("Slot-", ""));
                Map<String, String> data = entry.getValue();
                String displayID = data.get("ID");
                ItemStack displayStack = ItemStackHandler.buildFromStringID(displayID);
                if (data.containsKey("Display-Name")) {

                    displayStack.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(data.get("Display-Name")));

                }
                if (data.containsKey("Lore")) {

                    List<String> displayLore = SpawnManager.configManager.getConfigNode(1, "Spawn-Main-Menu", "Slots", entry.getKey(), "Lore").getList(TypeToken.of(String.class));
                    List<Text> lore = new ArrayList<>();

                    for (String l : displayLore) {

                        lore.add(FancyTextHandler.getFormattedText(l));

                    }

                    displayStack.set(DataComponentTypes.LORE, new LoreComponent(lore));

                }

                GooeyButton button;
                if (data.containsKey("Opens")) {

                    button = GooeyButton.builder()
                            .display(displayStack)
                            .onClick(() -> {

                                String menuToOpen = data.get("Opens");
                                if (menuToOpen.equalsIgnoreCase("Spawns-Possible")) {

                                    PossibleSpawnsMenu possibleSpawns = new PossibleSpawnsMenu(player, areas);
                                    possibleSpawns.build();
                                    try {

                                        possibleSpawns.open();

                                    } catch (ObjectMappingException e) {

                                        e.printStackTrace();

                                    }

                                } else if (menuToOpen.equalsIgnoreCase("Spawns-All")) {

                                    AllSpawnsMenu allSpawns = new AllSpawnsMenu(player, areas);
                                    allSpawns.build();
                                    try {

                                        allSpawns.open();

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
