package com.lypaka.spawnmanager.SpawnAreas.Spawns;

import com.lypaka.spawnmanager.SpawnAreas.SpawnArea;

import java.util.List;

public class AreaSpawns {

    private final SpawnArea area;
    private final List<CaveSpawn> caveSpawns;
    private final List<FishSpawn> fishSpawns;
    private final List<GrassSpawn> grassSpawns;
    private final List<HeadbuttSpawn> headbuttSpawns;
    private final List<NaturalSpawn> naturalSpawns;
    private final List<RockSmashSpawn> rockSmashSpawns;
    private final List<SurfSpawn> surfSpawns;

    public AreaSpawns (SpawnArea area, List<CaveSpawn> caveSpawns, List<FishSpawn> fish, List<GrassSpawn> grass, List<HeadbuttSpawn> headbutt, List<NaturalSpawn> natural, List<RockSmashSpawn> rockSmash, List<SurfSpawn> surf) {

        this.area = area;
        this.caveSpawns = caveSpawns;
        this.fishSpawns = fish;
        this.grassSpawns = grass;
        this.headbuttSpawns = headbutt;
        this.naturalSpawns = natural;
        this.rockSmashSpawns = rockSmash;
        this.surfSpawns = surf;

    }

    public SpawnArea getArea() {

        return this.area;

    }

    public List<CaveSpawn> getCaveSpawns() {

        return this.caveSpawns;

    }

    public List<FishSpawn> getFishSpawns() {

        return this.fishSpawns;

    }

    public List<GrassSpawn> getGrassSpawns() {

        return this.grassSpawns;

    }

    public List<HeadbuttSpawn> getHeadbuttSpawns() {

        return this.headbuttSpawns;

    }

    public List<NaturalSpawn> getNaturalSpawns() {

        return this.naturalSpawns;

    }

    public List<RockSmashSpawn> getRockSmashSpawns() {

        return this.rockSmashSpawns;

    }

    public List<SurfSpawn> getSurfSpawns() {

        return this.surfSpawns;

    }

}
