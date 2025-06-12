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
import com.lypaka.spawnmanager.GUIs.SpawnLists.AllSpawnsList;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;
import com.lypaka.spawnmanager.SpawnAreas.Spawns.AreaSpawns;
import com.lypaka.spawnmanager.SpawnManager;
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

            AreaSpawns spawns = SpawnAreaHandler.areaSpawnMap.get(SpawnAreaHandler.areaMap.get(a));

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

    public void open() {

        PlaceholderButton placeholderButton = new PlaceholderButton();
        List<Button> buttons = new ArrayList<>();
        ItemStack borderStack = ItemStackBuilder.buildFromStringID(ConfigGetters.allSpawnMenuBorderID);
        borderStack.setDisplayName(FancyText.getFormattedText(""));
        for (Map.Entry<Integer, ItemStack> entry : this.spawnsMap.entrySet()) {

            GooeyButton b = GooeyButton.builder().display(entry.getValue()).build();
            buttons.add(b);

        }
        ChestTemplate template = ChestTemplate.builder(ConfigGetters.allSpawnsMenuRows)
                .rectangle(0, 0, 5, 9, placeholderButton)
                .fill(GooeyButton.builder().display(borderStack).build())
                .set(ConfigGetters.allSpawnsMenuMainMenuButtonSlot, getMainMenu())
                .set(ConfigGetters.allSpawnsMenuPrevPageButtonSlot, getPrev())
                .set(ConfigGetters.allSpawnsMenuNextPageButtonSlot, getNext())
                .build();

        LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, buttons, null);
        page.setTitle(FancyText.getFormattedText(ConfigGetters.allSpawnsMenuTitle));
        setTitle(page);

        UIManager.openUIForcefully(this.player, page);


    }

    private static void setTitle (LinkedPage page) {

        LinkedPage next = page.getNext();
        if (next != null) {

            next.setTitle(FancyText.getFormattedText(ConfigGetters.allSpawnsMenuTitle));
            setTitle(next);

        }

    }

    private static Button getMainMenu() {

        ItemStack item = ItemStackBuilder.buildFromStringID(ConfigGetters.allSpawnsMenuMainMenuButtonID);
        item.setDisplayName(FancyText.getFormattedText(ConfigGetters.allSpawnsMenuMainMenuButtonDisplayName));
        return GooeyButton.builder().display(item).onClick(click -> {

            try {

                MainMenu.open(click.getPlayer());

            } catch (ObjectMappingException e) {

                throw new RuntimeException(e);

            }

        }).build();

    }

    private static Button getNext() {

        ItemStack item = ItemStackBuilder.buildFromStringID(ConfigGetters.allSpawnsMenuNextPageButtonID);
        item.setDisplayName(FancyText.getFormattedText(ConfigGetters.allSpawnsMenuNextPageButtonDisplayName));
        return LinkedPageButton.builder()
                .linkType(LinkType.Next)
                .display(item)
                .build();

    }

    private static Button getPrev() {

        ItemStack item = ItemStackBuilder.buildFromStringID(ConfigGetters.allSpawnsMenuPrevPageButtonID);
        item.setDisplayName(FancyText.getFormattedText(ConfigGetters.allSpawnsMenuPrevPageButtonDisplayName));
        return LinkedPageButton.builder()
                .linkType(LinkType.Previous)
                .display(item)
                .build();

    }

}
