package com.lypaka.spawnmanager.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.google.common.reflect.TypeToken;
import com.lypaka.areamanager.Areas.Area;
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

import java.util.List;
import java.util.Map;

public class SpawnMainMenu {

    public static void open (ServerPlayerEntity player, List<Area> areas) throws ObjectMappingException {

        int rows = ConfigGetters.spawnMainMenuRows;
        String title = ConfigGetters.spawnMainMenuTitle;
        ChestTemplate template = ChestTemplate.builder(rows).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyText.getFormattedText(title))
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
                ItemStack displayStack = ItemStackBuilder.buildFromStringID(displayID);
                if (data.containsKey("Display-Name")) {

                    displayStack.setDisplayName(FancyText.getFormattedText(data.get("Display-Name")));

                }
                if (data.containsKey("Lore")) {

                    List<String> displayLore = SpawnManager.configManager.getConfigNode(2, "Spawn-Main-Menu", "Slots", entry.getKey(), "Lore").getList(TypeToken.of(String.class));
                    ListNBT lore = new ListNBT();


                    for (String l : displayLore) {

                        lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(l))));

                    }

                    displayStack.getOrCreateChildTag("display").put("Lore", lore);

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
