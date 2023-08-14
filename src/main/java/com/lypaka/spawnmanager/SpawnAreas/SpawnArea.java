package com.lypaka.spawnmanager.SpawnAreas;

import com.lypaka.areamanager.Areas.Area;
import com.lypaka.spawnmanager.SpawnAreas.SpawnerSettings.*;

public class SpawnArea {

    private final Area area;
    private final FishSpawnerSettings fishSpawnerSettings;
    private final GrassSpawnerSettings grassSpawnerSettings;
    private final HeadbuttSpawnerSettings headbuttSpawnerSettings;
    private final NaturalSpawnerSettings naturalSpawnerSettings;
    private final RockSmashSpawnerSettings rockSmashSpawnerSettings;
    private final SurfSpawnerSettings surfSpawnerSettings;

    public SpawnArea (Area area, FishSpawnerSettings fishSpawnerSettings, GrassSpawnerSettings grassSpawnerSettings, HeadbuttSpawnerSettings headbuttSpawnerSettings,
                      NaturalSpawnerSettings naturalSpawnerSettings, RockSmashSpawnerSettings rockSmashSpawnerSettings, SurfSpawnerSettings surfSpawnerSettings) {

        this.area = area;
        this.fishSpawnerSettings = fishSpawnerSettings;
        this.grassSpawnerSettings = grassSpawnerSettings;
        this.headbuttSpawnerSettings = headbuttSpawnerSettings;
        this.naturalSpawnerSettings = naturalSpawnerSettings;
        this.rockSmashSpawnerSettings = rockSmashSpawnerSettings;
        this.surfSpawnerSettings = surfSpawnerSettings;

    }

    public Area getArea() {

        return this.area;

    }

    public void create() {

        SpawnAreaHandler.areaMap.put(this.area, this);

    }

    public FishSpawnerSettings getFishSpawnerSettings() {

        return this.fishSpawnerSettings;

    }

    public GrassSpawnerSettings getGrassSpawnerSettings() {

        return grassSpawnerSettings;

    }

    public HeadbuttSpawnerSettings getHeadbuttSpawnerSettings() {

        return headbuttSpawnerSettings;

    }

    public NaturalSpawnerSettings getNaturalSpawnerSettings() {

        return naturalSpawnerSettings;

    }

    public RockSmashSpawnerSettings getRockSmashSpawnerSettings() {

        return rockSmashSpawnerSettings;

    }

    public SurfSpawnerSettings getSurfSpawnerSettings() {

        return this.surfSpawnerSettings;

    }

}
