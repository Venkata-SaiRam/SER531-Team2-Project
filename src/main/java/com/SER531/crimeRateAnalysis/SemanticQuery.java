package com.SER531.crimeRateAnalysis;

import com.SER531.crimeRateAnalysis.beans.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.ByteArrayOutputStream;

public class SemanticQuery {

    private int population;

    private ResultSet execSelectAndPrint(String serviceURI, String query) {
        QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI, query);
        ResultSet results = q.execSelect();
        return results;
    }

    public CrimeRates getCrimeRates(String state, String city, Boolean mock) {
        boolean flag = state == null || state == "" || state == " ";
        state = flag ? "ARIZONA" : state;
        flag = city == null || city == "" || city == " ";
        city = flag ? "Tempe" : city;
        PropertyCrimeRates propertyCrimeRates = this.getPropertyCrime(state, city, mock);
        ViolentCrimeRates violentCrimeRates = this.getViolentCrime(state, city, mock);
        CrimeRates crimeRates = new CrimeRates();
        crimeRates.setState(state);
        crimeRates.setCity(city);
        crimeRates.setPopulation(population);
        crimeRates.setPropertyCrimeRates(propertyCrimeRates);
        crimeRates.setViolentCrimeRates(violentCrimeRates);
        return crimeRates;
    }

    private ViolentCrimeRates getViolentCrime(String state, String city, Boolean mock) {
        String json = null;
        ViolentCrimeRates violentCrimeRates = new ViolentCrimeRates();
        if (mock != null && mock) {
            json = "{\"head\":{\"vars\":[\"Murder\",\"robbery\",\"Rape2\",\"Population\",\"Aggravatedassault\",\"Rape1\",\"ViolentCrime\"]},\"results\":{\"bindings\":[{\"Aggravatedassault\":{\"type\":\"literal\",\"value\":\"506\"},\"Population\":{\"type\":\"literal\",\"value\":\"178654\"},\"ViolentCrime\":{\"type\":\"literal\",\"value\":\"902\"},\"Murder\":{\"type\":\"literal\",\"value\":\"4\"},\"robbery\":{\"type\":\"literal\",\"value\":\"237\"},\"Rape1\":{\"type\":\"literal\",\"value\":\"155\"},\"Rape2\":{\"type\":\"literal\",\"value\":\"0\"}}]}}\n";
        } else {
            ResultSet results = this.execSelectAndPrint("http://34.66.37.163:3030/ViolentCrime/query", "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX geographicinfo:<http://www.semanticweb.org/GeographicalInformation/geographicinformation.owl#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX ViolentCrime: <http://www.semanticweb.org/ViolentCrime/ViolentCrime.owl#>\n" +
                    "PREFIX PropertyCrime: <http://www.semanticweb.org/PropertyCrime/PropertyCrime.owl#>\n" +
                    "SELECT  ?Murder ?robbery ?Rape2 ?Population ?Aggravatedassault ?Rape1 ?ViolentCrime   WHERE { \n" +
                    " ?UniqueID ViolentCrime:ismurder ?Murder;\n" +
                    " ViolentCrime:isrobbery ?robbery;\n" +
                    " ViolentCrime:isrape2 ?Rape2;\n" +
                    " ViolentCrime:ispopulation ?Population;\n" +
                    " ViolentCrime:isaggravatedassault ?Aggravatedassault;\n" +
                    " ViolentCrime:isrape1 ?Rape1;\n" +
                    " ViolentCrime:isviolentcrime ?ViolentCrime;\n" +
                    " ViolentCrime:isUniqueIdentifier ?CityIDValue\n" +
                    "     SERVICE <http://35.193.121.187:3030/GeographicInformation/sparql>\n" +
                    "        {\n" +
                    "  SELECT ?CityIDValue WHERE { \n" +
                    " ?UniqueID geographicinfo:iscountry ?Countryvalue;\n" +
                    " geographicinfo:isstate  ?Statevalue;\n" +
                    " geographicinfo:iscity  ?CityValue;\n" +
                    " geographicinfo:isYear   ?Yearvalue;\n" +
                    " geographicinfo:isID  ?CityIDValue.\n" +
                    "  FILTER((?CityValue)=\"" + city + "\").\r\n" +
                    "  FILTER((?Statevalue)=\"" + state + "\").\r\n" +
                    "  }\n" +
                    "  }\n" +
                    "}");
            ByteArrayOutputStream os = extractJson(results);
            json = os.toString();
        }
        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(json);
            JSONObject res = (JSONObject) obj.get("results");
            JSONArray violentCrimes = (JSONArray) res.get("bindings");
            JSONObject head = (JSONObject) violentCrimes.get(0);
            JSONObject itr = (JSONObject)head.get("Aggravatedassault");
            int temp = (Integer.parseInt((String)itr.get("value")));
            violentCrimeRates.setAggravatedAssaultCrimeValue(temp);
            itr = (JSONObject)head.get("Population");
            temp = (Integer.parseInt((String)itr.get("value")));
            population = temp;
            itr = (JSONObject)head.get("ViolentCrime");
            temp = (Integer.parseInt((String)itr.get("value")));
            violentCrimeRates.setViolentCrimeValue(temp);
            itr = (JSONObject)head.get("Murder");
            temp = (Integer.parseInt((String)itr.get("value")));
            violentCrimeRates.setMurderCrimeValue(temp);
            itr = (JSONObject)head.get("robbery");
            temp = (Integer.parseInt((String)itr.get("value")));
            violentCrimeRates.setRobberyCrimeValue(temp);
            itr = (JSONObject)head.get("Rape1");
            temp = (Integer.parseInt((String)itr.get("value")));
            violentCrimeRates.setRape1CrimeValue(temp);
            itr = (JSONObject)head.get("Rape2");
            temp = (Integer.parseInt((String)itr.get("value")));
            violentCrimeRates.setRape2CrimeValue(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return violentCrimeRates;
    }

    private PropertyCrimeRates getPropertyCrime(String state, String city, Boolean mock) {
        PropertyCrimeRates propertyCrimeRates = new PropertyCrimeRates();
        String json = null;
        if (mock != null && mock) {
            json = "{\"head\":{\"vars\":[\"PropertyCrimevalue\",\"MotorVehicleCrimevalue\",\"BurglaryCrimevalue\",\"ArsonCrimevalue\",\"LarcenyCrimevalue\"]},\"results\":{\"bindings\":[{\"PropertyCrimevalue\":{\"type\":\"literal\",\"value\":\"8144\"},\"BurglaryCrimevalue\":{\"type\":\"literal\",\"value\":\"1200\"},\"MotorVehicleCrimevalue\":{\"type\":\"literal\",\"value\":\"450\"},\"ArsonCrimevalue\":{\"type\":\"literal\",\"value\":\"38\"},\"LarcenyCrimevalue\":{\"type\":\"literal\",\"value\":\"6494\"}}]}}";
        } else {
            ResultSet results = this.execSelectAndPrint("http://104.197.185.80:3030/PropertyCrime/query",
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
                            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" +
                            "PREFIX geographicinfo:<http://www.semanticweb.org/GeographicalInformation/geographicinformation.owl#>\r\n" +
                            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\r\n" +
                            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
                            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" +
                            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\r\n" +
                            "PREFIX ViolentCrime: <http://www.semanticweb.org/ViolentCrime/ViolentCrime.owl#>\r\n" +
                            "PREFIX PropertyCrime: <http://www.semanticweb.org/PropertyCrime/PropertyCrime.owl#>\r\n" +
                            "SELECT  ?PropertyCrimevalue ?MotorVehicleCrimevalue ?BurglaryCrimevalue ?ArsonCrimevalue ?LarcenyCrimevalue WHERE { \r\n" +
                            " ?UniqueID PropertyCrime:ismotorvehicletheft ?MotorVehicleCrimevalue;\r\n" +
                            " PropertyCrime:ispropertycrime ?PropertyCrimevalue;\r\n" +
                            " PropertyCrime:isburglary ?BurglaryCrimevalue;\r\n" +
                            " PropertyCrime:isarson ?ArsonCrimevalue;\r\n" +
                            " PropertyCrime:islarcenytheft ?LarcenyCrimevalue;\r\n" +
                            " PropertyCrime:isuniqueIdentifier ?CityIDValue.\r\n" +
                            "     SERVICE <http://35.193.121.187:3030/GeographicInformation/sparql>\r\n" +
                            "        {\r\n" +
                            "  SELECT ?CityIDValue WHERE { \r\n" +
                            " ?UniqueID geographicinfo:iscountry ?Countryvalue;\r\n" +
                            " geographicinfo:isstate  ?Statevalue;\r\n" +
                            " geographicinfo:iscity  ?CityValue;\r\n" +
                            " geographicinfo:isYear   ?Yearvalue;\r\n" +
                            " geographicinfo:isID  ?CityIDValue.\r\n" +
                            "  FILTER((?CityValue)=\"" + city + "\").\r\n" +
                            "  FILTER((?Statevalue)=\"" + state + "\").\r\n" +
                            "  }\r\n" +
                            "  }\r\n" +
                            "}");
            ByteArrayOutputStream os = extractJson(results);
            json = os.toString();
        }
        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(json);
            JSONObject res = (JSONObject) obj.get("results");
            JSONArray violentCrimes = (JSONArray) res.get("bindings");
            JSONObject head = (JSONObject) violentCrimes.get(0);
            JSONObject itr = (JSONObject)head.get("PropertyCrimevalue");
            int temp = (Integer.parseInt((String)itr.get("value")));
            propertyCrimeRates.setPropertyCrimeValue(temp);
            itr = (JSONObject)head.get("MotorVehicleCrimevalue");
            temp = (Integer.parseInt((String)itr.get("value")));
            propertyCrimeRates.setMotorVehicleCrimeValue(temp);
            itr = (JSONObject)head.get("BurglaryCrimevalue");
            temp = (Integer.parseInt((String)itr.get("value")));
            propertyCrimeRates.setBurglaryCrimeValue(temp);
            itr = (JSONObject)head.get("ArsonCrimevalue");
            temp = (Integer.parseInt((String)itr.get("value")));
            propertyCrimeRates.setArsonCrimeValue(temp);
            itr = (JSONObject)head.get("LarcenyCrimevalue");
            temp = (Integer.parseInt((String)itr.get("value")));
            propertyCrimeRates.setLarcenyCrimeValue(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return propertyCrimeRates;
    }

    private ByteArrayOutputStream extractJson(ResultSet results) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ResultSetFormatter.outputAsJSON(os, results);
        return os;
    }

    public States getStates(Boolean mock) {
        String json = null;
        if(mock != null && mock){
            json = "{\"head\":{\"vars\":[\"States\"]},\"results\":{\"bindings\":[{\"States\":{\"type\":\"literal\",\"value\":\"ALABAMA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"ALASKA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"ARIZONA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"ARKANSAS\"}},{\"States\":{\"type\":\"literal\",\"value\":\"CALIFORNIA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"COLORADO\"}},{\"States\":{\"type\":\"literal\",\"value\":\"CONNECTICUT\"}},{\"States\":{\"type\":\"literal\",\"value\":\"DELAWARE\"}},{\"States\":{\"type\":\"literal\",\"value\":\"DISTRICT OF COLUMBIA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"FLORIDA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"GEORGIA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"HAWAII\"}},{\"States\":{\"type\":\"literal\",\"value\":\"IDAHO\"}},{\"States\":{\"type\":\"literal\",\"value\":\"ILLINOIS\"}},{\"States\":{\"type\":\"literal\",\"value\":\"INDIANA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"IOWA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"KANSAS\"}},{\"States\":{\"type\":\"literal\",\"value\":\"KENTUCKY\"}},{\"States\":{\"type\":\"literal\",\"value\":\"LOUISIANA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"MAINE\"}},{\"States\":{\"type\":\"literal\",\"value\":\"MARYLAND\"}},{\"States\":{\"type\":\"literal\",\"value\":\"MASSACHUSETTS\"}},{\"States\":{\"type\":\"literal\",\"value\":\"MICHIGAN\"}},{\"States\":{\"type\":\"literal\",\"value\":\"MINNESOTA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"MISSISSIPPI\"}},{\"States\":{\"type\":\"literal\",\"value\":\"MISSOURI\"}},{\"States\":{\"type\":\"literal\",\"value\":\"MONTANA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"NEBRASKA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"NEVADA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"NEW HAMPSHIRE\"}},{\"States\":{\"type\":\"literal\",\"value\":\"NEW JERSEY\"}},{\"States\":{\"type\":\"literal\",\"value\":\"NEW MEXICO\"}},{\"States\":{\"type\":\"literal\",\"value\":\"NEW YORK\"}},{\"States\":{\"type\":\"literal\",\"value\":\"NORTH CAROLINA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"NORTH DAKOTA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"OHIO\"}},{\"States\":{\"type\":\"literal\",\"value\":\"OKLAHOMA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"OREGON\"}},{\"States\":{\"type\":\"literal\",\"value\":\"PENNSYLVANIA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"RHODE ISLAND\"}},{\"States\":{\"type\":\"literal\",\"value\":\"SOUTH CAROLINA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"SOUTH DAKOTA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"TENNESSEE\"}},{\"States\":{\"type\":\"literal\",\"value\":\"TEXAS\"}},{\"States\":{\"type\":\"literal\",\"value\":\"UTAH\"}},{\"States\":{\"type\":\"literal\",\"value\":\"VERMONT\"}},{\"States\":{\"type\":\"literal\",\"value\":\"VIRGINIA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"WASHINGTON\"}},{\"States\":{\"type\":\"literal\",\"value\":\"WEST VIRGINIA\"}},{\"States\":{\"type\":\"literal\",\"value\":\"WISCONSIN\"}},{\"States\":{\"type\":\"literal\",\"value\":\"WYOMING\"}}]}}";
        }else {
            ResultSet results = this.execSelectAndPrint("http://35.193.121.187:3030/GeographicInformation/query", "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX geographicinfo:<http://www.semanticweb.org/GeographicalInformation/geographicinformation.owl#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX ViolentCrime: <http://www.semanticweb.org/ViolentCrime/ViolentCrime.owl#>\n" +
                    "PREFIX PropertyCrime: <http://www.semanticweb.org/PropertyCrime/PropertyCrime.owl#>\n" +
                    "select Distinct ?States where {\n" +
                    "  ?UniqueID geographicinfo:isstate ?States;\n" +
                    "}");

            //ResultSetFormatter.out(System.out, results);
            ByteArrayOutputStream os = extractJson(results);
            json = os.toString();
        }
        States states = new States();
        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(json);
            System.out.println(obj.toString());
            JSONObject res = (JSONObject) obj.get("results");
            JSONArray statesList = (JSONArray) res.get("bindings");
            for (int i = 0; i < statesList.size(); i++) {
                JSONObject head = (JSONObject) statesList.get(i);
                JSONObject state = (JSONObject) head.get("States");
                String stateName = (String) state.get("value");
                states.addState(stateName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return states;
    }

    public Cities getCities(String state, Boolean mock) {
        String json = null;
        if(mock != null && mock){
            json = "{\"head\":{\"vars\":[\"Cities\"]},\"results\":{\"bindings\":[{\"Cities\":{\"type\":\"literal\",\"value\":\"Apache Junction\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Avondale\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Buckeye\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Bullhead City\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Camp Verde\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Casa Grande\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Chandler\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Chino Valley\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Clarkdale\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Coolidge\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Cottonwood\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Douglas\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Eagar\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"El Mirage\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Eloy\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Flagstaff\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Florence\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Fredonia\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Gilbert\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Glendale\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Goodyear\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Holbrook\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Jerome\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Kearny\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Kingman\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Lake Havasu City\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Marana\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Maricopa\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Mesa\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Miami\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Nogales\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Oro Valley\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Page\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Paradise Valley\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Patagonia\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Payson\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Peoria\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Phoenix\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Pima\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Pinetop-Lakeside\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Prescott\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Prescott Valley\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Safford\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Sahuarita\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"San Luis\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Scottsdale\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Show Low\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Sierra Vista\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Snowflake-Taylor\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Somerton\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Springerville\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Surprise\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Tempe\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Thatcher\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Tucson\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Wellton\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Wickenburg\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Willcox\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Williams\"}},{\"Cities\":{\"type\":\"literal\",\"value\":\"Winslow\"}}]}}";
        }else {
            boolean flag = (state == null) || (state == "") || state == " ";
            state = flag ? "ALABAMA" : state;
            ResultSet results = this.execSelectAndPrint("http://35.193.121.187:3030/GeographicInformation/query", "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX geographicinfo:<http://www.semanticweb.org/GeographicalInformation/geographicinformation.owl#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX ViolentCrime: <http://www.semanticweb.org/ViolentCrime/ViolentCrime.owl#>\n" +
                    "PREFIX PropertyCrime: <http://www.semanticweb.org/PropertyCrime/PropertyCrime.owl#>\n" +
                    "select  ?Cities where {\n" +
                    "  ?UniqueID geographicinfo:isstate ?State.\n" +
                    "  ?UniqueID geographicinfo:iscity ?Cities.\n" +
                    "  FILTER((?State)=\"" + state + "\")\n" +
                    "}");
            ByteArrayOutputStream os = extractJson(results);
            json = os.toString();
        }
        Cities cities = new Cities();
        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(json);
            System.out.println(obj.toString());
            JSONObject res = (JSONObject) obj.get("results");
            JSONArray statesList = (JSONArray) res.get("bindings");
            for (int i = 0; i < statesList.size(); i++) {
                JSONObject head = (JSONObject) statesList.get(i);
                JSONObject city = (JSONObject) head.get("Cities");
                String cityName = (String) city.get("value");
                cities.addState(cityName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cities;
    }
}
