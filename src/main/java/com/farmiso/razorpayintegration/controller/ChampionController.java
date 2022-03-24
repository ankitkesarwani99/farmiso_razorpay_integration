package com.farmiso.razorpayintegration.controller;

import com.farmiso.razorpayintegration.request.CreateChampionRequest;
import com.farmiso.razorpayintegration.request.UpdateChampionRequest;
import com.farmiso.razorpayintegration.response.ChampionDetailResponse;
import com.farmiso.razorpayintegration.response.CreateChampionResponse;
import com.farmiso.razorpayintegration.response.GenericResponse;
import com.farmiso.razorpayintegration.response.UpdateChampionResponse;
import com.farmiso.razorpayintegration.service.ChampionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.Valid;

@RestController
@EnableWebMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@CrossOrigin
@RequestMapping("/api/v1/champion")
public class ChampionController {
    private final ChampionService championService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> getChampionDetail(@PathVariable("id") Integer championId) {
        log.info("Get details for champion id : {}", championId);
        GenericResponse<ChampionDetailResponse> genericResponse = new GenericResponse<>();
        ChampionDetailResponse championDetailResponse = championService.getChampionDetails(championId);
        genericResponse.setData(championDetailResponse);
        HttpStatus httpStatus = HttpStatus.OK;
        return new ResponseEntity<>(genericResponse, httpStatus);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> createChampion(@RequestBody @Valid CreateChampionRequest createChampionRequest) {
        log.info("Create Champion Request : {}", createChampionRequest);
        GenericResponse<CreateChampionResponse> genericResponse = new GenericResponse();
        CreateChampionResponse createChampionResponse = championService.createChampion(createChampionRequest);
        genericResponse.setData(createChampionResponse);
        HttpStatus httpStatus = HttpStatus.OK;
        return new ResponseEntity<>(genericResponse, httpStatus);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> updateChampionDetails(@RequestBody @Valid UpdateChampionRequest updateChampionRequest) {
        log.info("Update Champion Request : {}", updateChampionRequest);
        GenericResponse<UpdateChampionResponse> genericResponse = new GenericResponse();
        UpdateChampionResponse updateChampionResponse = championService.updateChampionDetails(updateChampionRequest);
        genericResponse.setData(updateChampionResponse);
        HttpStatus httpStatus = HttpStatus.OK;
        return new ResponseEntity<>(genericResponse, httpStatus);
    }
}

