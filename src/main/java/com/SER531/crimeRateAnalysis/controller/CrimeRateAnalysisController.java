package com.SER531.crimeRateAnalysis.controller;

import com.SER531.crimeRateAnalysis.SemanticQuery;
import com.SER531.crimeRateAnalysis.beans.Cities;
import com.SER531.crimeRateAnalysis.beans.CrimeRates;
import com.SER531.crimeRateAnalysis.beans.States;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class CrimeRateAnalysisController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/getStates")
    @ResponseBody
    public States getStates(@RequestParam Boolean mock, HttpServletResponse response) {
        States states = new SemanticQuery().getStates(mock);
        response.setHeader("Access-Control-Allow-Origin", "*");
        return states;
    }

    @GetMapping("/getCities")
    @ResponseBody
    public Cities getCities(@RequestParam String state, @RequestParam Boolean mock, HttpServletResponse response){
        Cities cities = new SemanticQuery().getCities(state, mock);
        response.setHeader("Access-Control-Allow-Origin", "*");
        return cities;
    }

    @GetMapping("/getCrimeRates")
    @ResponseBody
    public CrimeRates getCrimeRates(@RequestParam String state, @RequestParam String city, @RequestParam Boolean mock, HttpServletResponse response){
        CrimeRates crimeRates = new SemanticQuery().getCrimeRates(state, city, mock);
        response.setHeader("Access-Control-Allow-Origin", "*");
        return crimeRates;
    }
}
