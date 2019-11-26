package com.SER531.crimeRateAnalysis.controller;

import com.SER531.crimeRateAnalysis.SemanticQuery;
import com.SER531.crimeRateAnalysis.beans.Cities;
import com.SER531.crimeRateAnalysis.beans.CrimeRates;
import com.SER531.crimeRateAnalysis.beans.States;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@Controller
public class CrimeRateAnalysisController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/getStates")
    @ResponseBody
    public States getStates(@RequestParam Boolean mock) {
        States states = new SemanticQuery().getStates(mock);
        return states;
    }

    @GetMapping("/getCities")
    @ResponseBody
    public Cities getCities(@RequestParam String state, @RequestParam Boolean mock){
        Cities cities = new SemanticQuery().getCities(state, mock);
        return cities;
    }

    @GetMapping("/getCrimeRates")
    @ResponseBody
    public CrimeRates getCities(@RequestParam String state, @RequestParam String city, @RequestParam Boolean mock){
        CrimeRates crimeRates = new SemanticQuery().getCrimeRates(state, city, mock);
        return crimeRates;
    }
}
