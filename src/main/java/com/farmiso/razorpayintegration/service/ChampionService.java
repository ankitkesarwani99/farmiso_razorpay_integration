package com.farmiso.razorpayintegration.service;

import com.farmiso.razorpayintegration.request.CreateChampionRequest;
import com.farmiso.razorpayintegration.request.UpdateChampionRequest;
import com.farmiso.razorpayintegration.response.ChampionDetailResponse;
import com.farmiso.razorpayintegration.response.CreateChampionResponse;
import com.farmiso.razorpayintegration.response.UpdateChampionResponse;

public interface ChampionService {
    ChampionDetailResponse getChampionDetails(Integer id);

    CreateChampionResponse createChampion(CreateChampionRequest createChampionRequest);

    UpdateChampionResponse updateChampionDetails(UpdateChampionRequest updateChampionRequest);
}
