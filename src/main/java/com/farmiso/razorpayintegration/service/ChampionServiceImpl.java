package com.farmiso.razorpayintegration.service;


import com.farmiso.razorpayintegration.entity.Champion;
import com.farmiso.razorpayintegration.repositery.ChampionRepository;
import com.farmiso.razorpayintegration.request.CreateChampionRequest;
import com.farmiso.razorpayintegration.request.UpdateChampionRequest;
import com.farmiso.razorpayintegration.response.ChampionDetailResponse;
import com.farmiso.razorpayintegration.response.CreateChampionResponse;
import com.farmiso.razorpayintegration.response.UpdateChampionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.ws.rs.BadRequestException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ChampionServiceImpl implements ChampionService {

    private final ChampionRepository championRepository;

    /**
     * This method is used to fetch details for a champion using championId
     *
     * @param id
     * @return ChampionDetailResponse
     */
    @Override
    public ChampionDetailResponse getChampionDetails(Integer id) {
        Optional<Champion> champion = championRepository.findById(id);
        if (!champion.isPresent()) {
            throw new BadRequestException("champion id does not exist");
        }
        ChampionDetailResponse championDetailResponse = ChampionDetailResponse.builder()
                .id(id)
                .amount(champion.get().getAmount())
                .name(champion.get().getName())
                .build();
        return championDetailResponse;
    }

    /**
     *This method is used to create new champion
     *
     * @param createChampionRequest
     * @return CreateChampionResponse
     */

    @Override
    public CreateChampionResponse createChampion(CreateChampionRequest createChampionRequest) {
        Champion champion = Champion.builder()
                .name(createChampionRequest.getName())
                .amount(createChampionRequest.getAmount())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        champion = championRepository.save(champion);

        return CreateChampionResponse.builder()
                .status("Champion with id " + champion.getId() + " successfully created")
                .build();
    }

    /**
     * This method is used to update champion details.
     *
     * @param updateChampionRequest
     * @return UpdateChampionResponse
     */
    @Override
    public UpdateChampionResponse updateChampionDetails(UpdateChampionRequest updateChampionRequest) {
        Optional<Champion> champion = championRepository.findById(updateChampionRequest.getChampionId());
        if (!champion.isPresent()) {
            throw new BadRequestException("Champion does not exist");
        }
        Champion championDetail = champion.get();
        if (!ObjectUtils.isEmpty(updateChampionRequest.getName()) && !updateChampionRequest.getName().trim().isEmpty()) {
            championDetail.setName(updateChampionRequest.getName());
        }
        if (!ObjectUtils.isEmpty(updateChampionRequest.getAmount())) {
            championDetail.setAmount(updateChampionRequest.getAmount());
        }
        championDetail.setUpdatedAt(LocalDateTime.now());
        championRepository.save(championDetail);
        return UpdateChampionResponse.builder()
                .status("Champion with id " + championDetail.getId() + " successfully updated")
                .build();
    }

}
