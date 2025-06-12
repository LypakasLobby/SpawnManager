package com.lypaka.spawnmanager.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.google.common.reflect.TypeToken;
import com.lypaka.areamanager.Areas.Area;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.ItemStackBuilder;
import com.lypaka.spawnmanager.ConfigGetters;
import com.lypaka.spawnmanager.GUIs.SpawnLists.PossibleSpawnsList;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.AreaSpawns;
import com.lypaka.spawnmanager.SpawnManager;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

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

        World world = this.player.world;
        String time = "Night";
        List<WorldTime> times = WorldTime.getCurrent(world);
        for (WorldTime t : times) {

            if (t.name().contains("day") || t.name().contains("dawn") || t.name().contains("morning") || t.name().contains("afternoon")) {

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

            String blockID = world.getBlockState(this.player.getPosition()).getBlock().getRegistryName().toString();
            if (blockID.equalsIgnoreCase("air")) location = "air";
            if (blockID.contains("water") || blockID.contains("lava")) location = "water";
            if (this.player.getPosition().getY() <= a.getUnderground()) location = "underground";
            AreaSpawns spawns = SpawnAreaHandler.areaSpawnMap.get(SpawnAreaHandler.areaMap.get(a));

            if (spawns.getNaturalSpawns().size() > 0) {

                PossibleSpawnsList.buildNatural(time, weather, location, spawns, m1, m2, m3);

            }
            if (spawns.getFishSpawns().size() > 0) {

                PossibleSpawnsList.buildFish(time, weather, spawns, m1, m2, m3);

            }
            if (spawns.getHeadbuttSpawns().size() > 0) {

                PossibleSpawnsList.buildHeadbutt(time, weather, spawns, m1, m2, m3);

            }
            if (spawns.getRockSmashSpawns().size() > 0) {

                PossibleSpawnsList.buildRockSmash(time, weather, spawns, m1, m2, m3);

            }
            if (spawns.getGrassSpawns().size() > 0) {

                PossibleSpawnsList.buildGrass(time, weather, location, spawns, m1, m2, m3);

            }
            if (spawns.getSurfSpawns().size() > 0) {

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

    public void open() {

        PlaceholderButton placeholderButton = new PlaceholderButton();
        List<Button> buttons = new ArrayList<>();
        ItemStack borderStack = ItemStackBuilder.buildFromStringID(ConfigGetters.allSpawnMenuBorderID);
        borderStack.setDisplayName(FancyText.getFormattedText(""));
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
        page.setTitle(FancyText.getFormattedText(ConfigGetters.possibleSpawnsMenuTitle));
        setTitle(page);

        UIManager.openUIForcefully(this.player, page);


    }

    private static void setTitle (LinkedPage page) {

        LinkedPage next = page.getNext();
        if (next != null) {

            next.setTitle(FancyText.getFormattedText(ConfigGetters.possibleSpawnsMenuTitle));
            setTitle(next);

        }

    }

    private static Button getMainMenu() {

        ItemStack item = ItemStackBuilder.buildFromStringID(ConfigGetters.possibleSpawnsMenuMainMenuButtonID);
        item.setDisplayName(FancyText.getFormattedText(ConfigGetters.possibleSpawnsMenuMainMenuButtonDisplayName));
        return GooeyButton.builder().display(item).onClick(click -> {

            try {

                MainMenu.open(click.getPlayer());

            } catch (ObjectMappingException e) {

                throw new RuntimeException(e);

            }

        }).build();

    }

    private static Button getNext() {

        ItemStack item = ItemStackBuilder.buildFromStringID(ConfigGetters.possibleSpawnsMenuNextPageButtonID);
        item.setDisplayName(FancyText.getFormattedText(ConfigGetters.possibleSpawnsMenuNextPageButtonDisplayName));
        return LinkedPageButton.builder()
                .linkType(LinkType.Next)
                .display(item)
                .build();

    }

    private static Button getPrev() {

        ItemStack item = ItemStackBuilder.buildFromStringID(ConfigGetters.possibleSpawnsMenuPrevPageButtonID);
        item.setDisplayName(FancyText.getFormattedText(ConfigGetters.possibleSpawnsMenuPrevPageButtonDisplayName));
        return LinkedPageButton.builder()
                .linkType(LinkType.Previous)
                .display(item)
                .build();

    }

}
