package com.example.roman.test.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class SectorResponse {
    List<Sector> sectors;

    public SectorResponse() {
        sectors = new ArrayList<>();
    }

    public static SectorResponse parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        SectorResponse sectorResponse = gson.fromJson(response, SectorResponse.class);
        return sectorResponse;
    }

    public List<Sector> getSectors() {
        return sectors;
    }
}
