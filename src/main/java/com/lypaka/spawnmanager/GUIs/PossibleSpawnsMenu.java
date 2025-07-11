package com.lypaka.spawnmanager.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.areamanager.Areas.Area;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.ItemStackHandler;
import com.lypaka.lypakautils.Handlers.WorldTimeHandler;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import com.lypaka.spawnmanager.ConfigGetters;
import com.lypaka.spawnmanager.GUIs.SpawnLists.PossibleSpawnsList;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.AreaSpawns;
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
            if (!spawns.getCaveSpawns().isEmpty()) {

                PossibleSpawnsList.buildCave(time, weather, spawns, m1, m2, m3);

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

    public void open() {

        PlaceholderButton placeholderButton = new PlaceholderButton();
        List<Button> buttons = new ArrayList<>();
        ItemStack borderStack = ItemStackHandler.buildFromStringID(ConfigGetters.possibleSpawnMenuBorderID);
        borderStack.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(""));
        for (Map.Entry<Integer, ItemStack> entry : this.spawnsMap.entrySet()) {

            GooeyButton b = GooeyButton.builder().display(entry.getValue()).build();
            buttons.add(b);

        }
        ChestTemplate template = ChestTemplate.builder(ConfigGetters.possibleSpawnsMenuRows)
                .rectangle(0, 0, 5, 9, placeholderButton)
                .fill(GooeyButton.builder().display(borderStack).build())
                .set(ConfigGetters.possibleSpawnsMenuMainMenuButtonSlot, getMainMenu())
                .set(ConfigGetters.possibleSpawnsMenuPrevPageButtonSlot, getPrev())
                .set(ConfigGetters.possibleSpawnsMenuNextPageButtonSlot, getNext())
                .build();

        LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, buttons, null);
        page.setTitle(FancyTextHandler.getFormattedText(ConfigGetters.possibleSpawnsMenuTitle));
        setTitle(page);

        UIManager.openUIForcefully(this.player, page);


    }

    private static void setTitle (LinkedPage page) {

        LinkedPage next = page.getNext();
        if (next != null) {

            next.setTitle(FancyTextHandler.getFormattedText(ConfigGetters.possibleSpawnsMenuTitle));
            setTitle(next);

        }

    }

    private static Button getMainMenu() {

        ItemStack item = ItemStackHandler.buildFromStringID(ConfigGetters.possibleSpawnsMenuMainMenuButtonID);
        item.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.possibleSpawnsMenuMainMenuButtonDisplayName));
        List<Text> lore = new ArrayList<>();
        for (String l : ConfigGetters.possibleSpawnsMenuMainMenuButtonLore) {

            lore.add(FancyTextHandler.getFormattedText(l));

        }
        item.set(DataComponentTypes.LORE, new LoreComponent(lore));
        return GooeyButton.builder().display(item).onClick(click -> {

            try {

                MainMenu.open(click.getPlayer());

            } catch (ObjectMappingException e) {

                throw new RuntimeException(e);

            }

        }).build();

    }

    private static Button getNext() {

        ItemStack item = ItemStackHandler.buildFromStringID(ConfigGetters.possibleSpawnsMenuNextPageButtonID);
        item.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.possibleSpawnsMenuNextPageButtonDisplayName));
        return LinkedPageButton.builder()
                .linkType(LinkType.Next)
                .display(item)
                .build();

    }

    private static Button getPrev() {

        ItemStack item = ItemStackHandler.buildFromStringID(ConfigGetters.possibleSpawnsMenuPrevPageButtonID);
        item.set(DataComponentTypes.CUSTOM_NAME, FancyTextHandler.getFormattedText(ConfigGetters.possibleSpawnsMenuPrevPageButtonDisplayName));
        return LinkedPageButton.builder()
                .linkType(LinkType.Previous)
                .display(item)
                .build();

    }

}
