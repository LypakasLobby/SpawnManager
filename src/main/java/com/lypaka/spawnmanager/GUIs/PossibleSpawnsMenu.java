package com.lypaka.spawnmanager.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.areamanager.Areas.Area;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.ItemStackHandler;
import com.lypaka.lypakautils.Handlers.WorldTimeHandler;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import com.lypaka.shadow.google.common.reflect.TypeToken;
import com.lypaka.spawnmanager.ConfigGetters;
import com.lypaka.spawnmanager.GUIs.SpawnLists.PossibleSpawnsList;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.AreaSpawns;
import com.lypaka.spawnmanager.SpawnManager;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class PossibleSpawnsMenu {

    private final ServerPlayerEntity player;
    private final List<Area> areas;
    private final Map<Integer, ItemStack> spawnsMap;

    public PossibleSpawnsMenu (ServerPlayerEntity player, List<Area> areas) {

        this.player = player;
        this.areas = areas;
        this.spawnsMap = new HashMap<>();

    }

    public void build() {

        World world = this.player.getWorld();
        String time = "Night";
        List<String> times = WorldTimeHandler.getCurrentTimeValues(world);
        for (String t : times) {

            if (t.equalsIgnoreCase("day") || t.equalsIgnoreCase("dawn") || t.equalsIgnoreCase("morning")) {

                time = "Day";
                break;

            }

        }
        String weather = "Clear";
        if (world.isRaining()) {

            weather = "Rain";

        } else if (world.isThundering()) {

            weather = "Storm";

        }
        String location = "land";
        Map<UUID, Pokemon> m1 = new HashMap<>();
        Map<Pokemon, ItemStack> m2 = new HashMap<>();
        Map<Integer, UUID> m3 = new HashMap<>();
        for (Area a : this.areas) {

            BlockPos pos = player.getBlockPos();
            BlockState state = world.getBlockState(pos);
            String blockID = Registries.BLOCK.getId(state.getBlock()).toString();
            if (blockID.equalsIgnoreCase("air")) location = "air";
            if (blockID.contains("water") || blockID.contains("lava")) location = "water";
            if (this.player.getY() <= a.getUnderground()) location = "underground";
            AreaSpawns spawns = SpawnAreaHandler.areaSpawnMap.get(SpawnAreaHandler.areaMap.get(a));

            if (!spawns.getNaturalSpawns().isEmpty()) {

                PossibleSpawnsList.buildNatural(time, weather, location, spawns, m1, m2, m3);

            }
            if (!spawns.getFishSpawns().isEmpty()) {

                PossibleSpawnsList.buildFish(time, weather, spawns, m1, m2, m3);

            }
            if (!spawns.getHeadbuttSpawns().isEmpty()) {

                PossibleSpawnsList.buildHeadbutt(time, weather, spawns, m1, m2, m3);

            }
            if (!spawns.getRockSmashSpawns().isEmpty()) {

                PossibleSpawnsList.buildRockSmash(time, weather, spawns, m1, m2, m3);

            }
            if (!spawns.getGrassSpawns().isEmpty()) {

                PossibleSpawnsList.buildGrass(time, weather, location, spawns, m1, m2, m3);

            }
            if (!spawns.getSurfSpawns().isEmpty()) {

                PossibleSpawnsList.buildSurf(time, weather, spawns, m1, m2, m3);

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

        int rows = ConfigGetters.possibleSpawnsMenuRows;
        ChestTemplate template = ChestTemplate.builder(rows).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyTextHandler.getFormattedText(ConfigGetters.possibleSpawnsMenuTitle))
                .build();

        int max = 9 * rows;
        int usable = max - 9; // bottom row reserved for border and utility buttons

        Map<String, String> borderStuff = ConfigGetters.possibleSpawnsMenuSlotsMap.get("Border");
        String id = borderStuff.get("ID");
        String[] slotArray = borderStuff.get("Slots").split(", ");
        for (String s : slotArray) {

            page.getTemplate().getSlot(Integer.parseInt(s)).setButton(GooeyButton.builder().display(CommonButtons.getBorderStack(id)).build());

        }

        for (Map.Entry<String, Map<String, String>> entry : ConfigGetters.possibleSpawnsMenuSlotsMap.entrySet()) {

            if (entry.getKey().contains("Slot-")) {

                int slot = Integer.parseInt(entry.getKey().replace("Slot-", ""));
                Map<String, String> data = entry.getValue();
                String displayID = data.get("ID");
                ItemStack displayStack = ItemStackHandler.buildFromStringID(displayID);
                if (data.containsKey("Display-Name")) {

                    displayStack.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(data.get("Display-Name")));

                }
                if (data.containsKey("Lore")) {

                    List<String> displayLore = SpawnManager.configManager.getConfigNode(1, "Spawns-Possible", "Slots", entry.getKey(), "Lore").getList(TypeToken.of(String.class));
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

            Map<String, Map<String, String>> utilityMap = SpawnManager.configManager.getConfigNode(1, "Spawns-Possible", "Slots", "Utility").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
            String nextPageID = utilityMap.get("Next-Page").get("ID");
            ItemStack next = ItemStackHandler.buildFromStringID(nextPageID);
            next.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(utilityMap.get("Next-Page").get("Display-Name")));
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
        int rows = ConfigGetters.possibleSpawnsMenuRows;
        ChestTemplate template = ChestTemplate.builder(rows).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyTextHandler.getFormattedText(ConfigGetters.possibleSpawnsMenuTitle))
                .build();

        int startingIndex = ((rows * 9) - 10) + (pageNum - 1);
        int maxIndex = ((rows * 9) - 10) + startingIndex;
        int currentIndex = startingIndex;

        Map<String, String> borderStuff = ConfigGetters.possibleSpawnsMenuSlotsMap.get("Border");
        String id = borderStuff.get("ID");
        String[] slotArray = borderStuff.get("Slots").split(", ");
        for (String s : slotArray) {

            page.getTemplate().getSlot(Integer.parseInt(s)).setButton(GooeyButton.builder().display(CommonButtons.getBorderStack(id)).build());

        }

        for (Map.Entry<String, Map<String, String>> entry : ConfigGetters.possibleSpawnsMenuSlotsMap.entrySet()) {

            if (entry.getKey().contains("Slot-")) {

                int slot = Integer.parseInt(entry.getKey().replace("Slot-", ""));
                Map<String, String> data = entry.getValue();
                String displayID = data.get("ID");
                ItemStack displayStack = ItemStackHandler.buildFromStringID(displayID);
                if (data.containsKey("Display-Name")) {

                    displayStack.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(data.get("Display-Name")));

                }
                if (data.containsKey("Lore")) {

                    List<String> displayLore = SpawnManager.configManager.getConfigNode(1, "Spawns-Possible", "Slots", entry.getKey(), "Lore").getList(TypeToken.of(String.class));
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

        Map<String, Map<String, String>> utilityMap = SpawnManager.configManager.getConfigNode(1, "Spawns-Possible", "Slots", "Utility").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
        if (currentIndex >= maxIndex) {

            String nextPageID = utilityMap.get("Next-Page").get("ID");
            ItemStack next = ItemStackHandler.buildFromStringID(nextPageID);
            next.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(utilityMap.get("Next-Page").get("Display-Name")));
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
        ItemStack prev = ItemStackHandler.buildFromStringID(previousPageID);
        prev.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(utilityMap.get("Prev-Page").get("Display-Name")));
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
