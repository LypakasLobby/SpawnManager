package com.lypaka.spawnmanager.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.google.common.reflect.TypeToken;
import com.lypaka.areamanager.AreaManager;
import com.lypaka.areamanager.Areas.Area;
import com.lypaka.spawnmanager.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.ItemStackBuilder;
import com.lypaka.spawnmanager.GUIs.SpawnLists.AllSpawnsList;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.AreaSpawns;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.*;

public class AllSpawnsMenu {

    private final ServerPlayerEntity player;
    private final List<Area> areas;
    private final Map<Integer, ItemStack> spawnsMap;

    public AllSpawnsMenu (ServerPlayerEntity player, List<Area> areas) {

        this.player = player;
        this.areas = areas;
        this.spawnsMap = new HashMap<>();

    }

    public void build() {

        Map<UUID, Pokemon> m1 = new HashMap<>();
        Map<Pokemon, ItemStack> m2 = new HashMap<>();
        Map<Integer, UUID> m3 = new HashMap<>();
        for (Area a : this.areas) {

            AreaSpawns spawns = SpawnAreaHandler.areaSpawnMap.get(a);

            if (spawns.getNaturalSpawns().size() > 0) {

                AllSpawnsList.buildNatural(spawns, m1, m2, m3);

            }
            if (spawns.getFishSpawns().size() > 0) {

                AllSpawnsList.buildFish(spawns, m1, m2, m3);

            }
            if (spawns.getHeadbuttSpawns().size() > 0) {

                AllSpawnsList.buildHeadbutt(spawns, m1, m2, m3);

            }
            if (spawns.getRockSmashSpawns().size() > 0) {

                AllSpawnsList.buildRockSmash(spawns, m1, m2, m3);

            }
            if (spawns.getGrassSpawns().size() > 0) {

                AllSpawnsList.buildGrass(spawns, m1, m2, m3);

            }
            if (spawns.getSurfSpawns().size() > 0) {

                AllSpawnsList.buildSurf(spawns, m1, m2, m3);

            }

        }

        List<Integer> dexNums = new ArrayList<>(m3.keySet());
        Collections.sort(dexNums);
        for (int i = 0; i < dexNums.size(); i++) {

            UUID uuid = m3.get(dexNums.get(i));
            Pokemon pokemon = m1.get(uuid);
            ItemStack sprite = m2.get(pokemon);
            this.spawnsMap.put(i, sprite);

        }

    }

    public void open() throws ObjectMappingException {

        int rows = ConfigGetters.allSpawnsMenuRows;
        ChestTemplate template = ChestTemplate.builder(rows).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyText.getFormattedText(ConfigGetters.allSpawnsMenuTitle))
                .build();

        int max = 9 * rows;
        int usable = max - 9; // bottom row reserved for border and utility buttons

        Map<String, String> borderStuff = ConfigGetters.allSpawnsMenuSlotsMap.get("Border");
        String id = borderStuff.get("ID");
        String[] slotArray = borderStuff.get("Slots").split(", ");
        for (String s : slotArray) {

            page.getTemplate().getSlot(Integer.parseInt(s)).setButton(GooeyButton.builder().display(CommonButtons.getBorderStack(id)).build());

        }

        for (Map.Entry<String, Map<String, String>> entry : ConfigGetters.allSpawnsMenuSlotsMap.entrySet()) {

            if (entry.getKey().contains("Slot-")) {

                int slot = Integer.parseInt(entry.getKey().replace("Slot-", ""));
                Map<String, String> data = entry.getValue();
                String displayID = data.get("ID");
                ItemStack displayStack = ItemStackBuilder.buildFromStringID(displayID);
                if (data.containsKey("Display-Name")) {

                    displayStack.setDisplayName(FancyText.getFormattedText(data.get("Display-Name")));

                }
                if (data.containsKey("Lore")) {

                    List<String> displayLore = AreaManager.configManager.getConfigNode(2, "Spawns-All", "Slots", entry.getKey(), "Lore").getList(TypeToken.of(String.class));
                    ListNBT lore = new ListNBT();
                    List<String> subAreas = new ArrayList<>();
                    for (Area a : areas) {

                        subAreas.add(a.getDisplayName());

                    }

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
                                if (menuToOpen.equalsIgnoreCase("Main-Menu")) {

                                    try {

                                        MainMenu.open(player);

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

        for (Map.Entry<Integer, ItemStack> entry : this.spawnsMap.entrySet()) {

            page.getTemplate().getSlot(entry.getKey()).setButton(GooeyButton.builder().display(entry.getValue()).build());

        }

        if (this.spawnsMap.size() >= usable) {

            Map<String, Map<String, String>> utilityMap = AreaManager.configManager.getConfigNode(2, "Spawns-All", "Slots", "Utility").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
            String nextPageID = utilityMap.get("Next-Page").get("ID");
            ItemStack next = ItemStackBuilder.buildFromStringID(nextPageID);
            next.setDisplayName(FancyText.getFormattedText(utilityMap.get("Next-Page").get("Display-Name")));
            int slot = Integer.parseInt(utilityMap.get("Next-Page").get("Slot"));
            GooeyButton button = GooeyButton.builder()
                    .display(next)
                    .onClick(() -> {

                        try {

                            openNext(2);

                        } catch (ObjectMappingException e) {

                            e.printStackTrace();

                        }

                    })
                    .build();

            page.getTemplate().getSlot(slot).setButton(button);

        }

        UIManager.openUIForcefully(this.player, page);

    }

    // 44 usable slots per page with default settings
    // page 2 starts at index 45 on the spawnsMap (45 - 89)
    // 90 for page 3 (90 - 134)

    // auto-calculate -> (rows * 9) - 10 = usable slots per page

    // ((rows * 9) - 10 * pageNum) + 1 = max index
    // (rows * 9) - 10 * 1 = 44 + 1 = 45
    private void openNext (int pageNum) throws ObjectMappingException {

        // 45 is the starting index for page 2
        int rows = ConfigGetters.allSpawnsMenuRows;
        ChestTemplate template = ChestTemplate.builder(rows).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyText.getFormattedText(ConfigGetters.allSpawnsMenuTitle))
                .build();

        int startingIndex = ((rows * 9) - 10) + (pageNum - 1);
        int maxIndex = ((rows * 9) - 10) + startingIndex;
        int currentIndex = startingIndex;

        Map<String, String> borderStuff = ConfigGetters.allSpawnsMenuSlotsMap.get("Border");
        String id = borderStuff.get("ID");
        String[] slotArray = borderStuff.get("Slots").split(", ");
        for (String s : slotArray) {

            page.getTemplate().getSlot(Integer.parseInt(s)).setButton(GooeyButton.builder().display(CommonButtons.getBorderStack(id)).build());

        }

        for (Map.Entry<String, Map<String, String>> entry : ConfigGetters.allSpawnsMenuSlotsMap.entrySet()) {

            if (entry.getKey().contains("Slot-")) {

                int slot = Integer.parseInt(entry.getKey().replace("Slot-", ""));
                Map<String, String> data = entry.getValue();
                String displayID = data.get("ID");
                ItemStack displayStack = ItemStackBuilder.buildFromStringID(displayID);
                if (data.containsKey("Display-Name")) {

                    displayStack.setDisplayName(FancyText.getFormattedText(data.get("Display-Name")));

                }
                if (data.containsKey("Lore")) {

                    List<String> displayLore = AreaManager.configManager.getConfigNode(2, "Spawns-All", "Slots", entry.getKey(), "Lore").getList(TypeToken.of(String.class));
                    ListNBT lore = new ListNBT();
                    List<String> subAreas = new ArrayList<>();
                    for (Area a : areas) {

                        subAreas.add(a.getDisplayName());

                    }

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
                                if (menuToOpen.equalsIgnoreCase("Main-Menu")) {

                                    try {

                                        MainMenu.open(player);

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

        for (Map.Entry<Integer, ItemStack> entry : this.spawnsMap.entrySet()) {

            if (entry.getKey() >= startingIndex && entry.getKey() <= maxIndex) {

                currentIndex++;
                page.getTemplate().getSlot(entry.getKey()).setButton(GooeyButton.builder().display(entry.getValue()).build());

            }

        }

        Map<String, Map<String, String>> utilityMap = AreaManager.configManager.getConfigNode(2, "Spawns-All", "Slots", "Utility").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
        if (currentIndex >= maxIndex) {

            String nextPageID = utilityMap.get("Next-Page").get("ID");
            ItemStack next = ItemStackBuilder.buildFromStringID(nextPageID);
            next.setDisplayName(FancyText.getFormattedText(utilityMap.get("Next-Page").get("Display-Name")));
            int slot = Integer.parseInt(utilityMap.get("Next-Page").get("Slot"));
            GooeyButton button = GooeyButton.builder()
                    .display(next)
                    .onClick(() -> {

                        try {

                            openNext((pageNum + 1));

                        } catch (ObjectMappingException e) {

                            e.printStackTrace();

                        }

                    })
                    .build();

            page.getTemplate().getSlot(slot).setButton(button);

        }

        String previousPageID = utilityMap.get("Prev-Page").get("ID");
        ItemStack prev = ItemStackBuilder.buildFromStringID(previousPageID);
        prev.setDisplayName(FancyText.getFormattedText(utilityMap.get("Prev-Page").get("Display-Name")));
        int slot = Integer.parseInt(utilityMap.get("Prev-Page").get("Slot"));
        GooeyButton button = GooeyButton.builder()
                .display(prev)
                .onClick(() -> {

                    try {

                        openNext((pageNum - 1));

                    } catch (ObjectMappingException e) {

                        e.printStackTrace();

                    }

                })
                .build();
        page.getTemplate().getSlot(slot).setButton(button);

        UIManager.openUIForcefully(this.player, page);

    }

}
