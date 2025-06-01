package com.lypaka.spawnmanager.Listeners;

import com.lypaka.areamanager.API.FinishedLoadingCallback;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import com.lypaka.spawnmanager.SpawnAreas.SpawnAreaHandler;

import java.io.IOException;

public class LoadListener implements FinishedLoadingCallback {

    @Override
    public void onFinishedLoading() {

        try {

            SpawnAreaHandler.loadAreas();

        } catch (IOException | ObjectMappingException e) {

            throw new RuntimeException(e);

        }

    }

}
