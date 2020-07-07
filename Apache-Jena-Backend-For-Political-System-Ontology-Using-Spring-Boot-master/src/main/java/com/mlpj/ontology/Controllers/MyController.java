package com.mlpj.ontology.Controllers;

import com.mlpj.ontology.JenaWork.InitJena;
import com.mlpj.ontology.Model.Job;
import net.minidev.json.JSONObject;
import org.apache.jena.atlas.json.JsonString;
import org.apache.jena.atlas.json.io.parser.JSONParser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

// My Imports for cosine similarity
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.text.similarity.CosineSimilarity;

import org.json.simple.*;


@RestController
public class MyController {

    @CrossOrigin
    @RequestMapping(value = "/findPolitician",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> findPolitician(@RequestBody RequestPOJO requestPOJO) {
        boolean allEmpty = true;
        System.out.println(requestPOJO);
        String queryString =
                "PREFIX pol: <http://www.semanticweb.org/viva/ontologies/2019/6/politicians#>" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "SELECT ?Y WHERE { ";

        if (requestPOJO.getDistrict() != null && !requestPOJO.getDistrict().equals("")) {
            allEmpty = false;
            queryString += "?Y pol:isElectedFrom pol:" + requestPOJO.getDistrict() + ".";
        }

        if (requestPOJO.getProvince() != null && !requestPOJO.getProvince().equals("")) {
            allEmpty = false;
            queryString += "?Z pol:isDistrictOf pol:" + requestPOJO.getProvince() + "."
                            + "?Y pol:isElectedFrom ?Z .";
        }

        if (requestPOJO.getParty() != null && !requestPOJO.getParty().equals("")) {
            allEmpty = false;
            queryString += "?Y pol:isMemberOfParty pol:" + requestPOJO.getParty() + ".";
        }

        if (requestPOJO.getPoliticalInstituteInstance() != null && !requestPOJO.getPoliticalInstituteInstance().equals("")) {
            allEmpty = false;
            queryString += "?A rdfs:subPropertyOf pol:isMemberOfInstitution. ?Y ?A pol:" + requestPOJO.getPoliticalInstituteInstance() + ".";
        }

        if (allEmpty) {
            List<JSONObject> emptyArray = new ArrayList<>();
            return emptyArray;
        }
        queryString += " }";
        System.out.println(queryString);

        List<JSONObject> resultSet = InitJena.findPoliticians(queryString);
        return resultSet;
    }

    @CrossOrigin
    @RequestMapping("/describePolitician")
    public List<JSONObject> describePolitician(@RequestParam String politician_name) {

        String queryString =
                "PREFIX pol: <http://www.semanticweb.org/viva/ontologies/2019/6/politicians#>" +
                        "SELECT ?Y ?Z WHERE { pol:" + politician_name + " ?Y ?Z .}";
        List<JSONObject> resultSet = InitJena.describePoliticians(queryString);
        System.out.println(queryString);
        return resultSet;
    }

    @CrossOrigin
    @RequestMapping("/getProvinces")
    public List<JSONObject> getProvinces() {

        String queryString =
                "PREFIX pol: <http://www.semanticweb.org/viva/ontologies/2019/6/politicians#>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "SELECT ?X WHERE { ?X rdf:type pol:Province .}";
        List<JSONObject> resultSet = InitJena.getItems(queryString);
        System.out.println(queryString);
        return resultSet;
    }

    @CrossOrigin
    @RequestMapping("/getDistricts")
    public List<JSONObject> getDistricts() {

        String queryString =
                "PREFIX pol: <http://www.semanticweb.org/viva/ontologies/2019/6/politicians#>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "SELECT ?X WHERE { ?X rdf:type pol:District .}";
        List<JSONObject> resultSet = InitJena.getItems(queryString);
        System.out.println(queryString);
        return resultSet;
    }

    @CrossOrigin
    @RequestMapping("/getParties")
    public List<JSONObject> getParties() {

        String queryString =
                "PREFIX pol: <http://www.semanticweb.org/viva/ontologies/2019/6/politicians#>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>" +
                        "SELECT ?X WHERE { ?Y rdfs:subClassOf pol:Party ." +
                                            "?X rdf:type ?Y}";
        List<JSONObject> resultSet = InitJena.getItems(queryString);
        System.out.println(queryString);
        return resultSet;
    }

    @CrossOrigin
    @RequestMapping("/getPoliticalInstituteInstances")
    public List<JSONObject> getPoliticalInstituteInstances() {

        String queryString =
                "PREFIX pol: <http://www.semanticweb.org/viva/ontologies/2019/6/politicians#>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>" +
                        "SELECT ?X WHERE { ?Y rdfs:subClassOf* pol:PoliticalInstitution ." +
                        "?X rdf:type ?Y}";
        List<JSONObject> resultSet = InitJena.getItems(queryString);
        System.out.println(queryString);
        return resultSet;
    }

    @CrossOrigin
    @RequestMapping("/getCosineSimilarity")
    public void calculateCosineSimilarity() {

        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        String firstSampleText = "Java mysql";
        String secondSampleText = "OOP Hibernate MysQl Java";

        String firstSample = firstSampleText.toLowerCase();
        String secondSample = secondSampleText.toLowerCase();

        // Calculate Cosine Similarity Using splits the strings into individual characters
        Map<CharSequence, Integer> vectorA = Arrays
                .stream(firstSample.split(""))
                .collect(Collectors.toMap(
                        character -> character, character -> 1, Integer::sum));

        Map<CharSequence, Integer> vectorB = Arrays
                .stream(secondSample.split(""))
                .collect(Collectors.toMap(
                        character -> character, character -> 1, Integer::sum));

        System.out.printf("%5.4f\n",cosineSimilarity.cosineSimilarity(vectorA, vectorB));

        // Calculate Cosine Similarity Using splits the strings into words
        Map<CharSequence, Integer> vectorA1 = Arrays
                .stream(firstSample.split(" "))
                .collect(Collectors.toMap(word -> word, word -> 1, Integer::sum));
        Map<CharSequence, Integer> vectorB1 = Arrays
                .stream(secondSample.split(" "))
                .collect(Collectors.toMap(word -> word, word -> 1, Integer::sum));

        System.out.println(vectorA1);
        System.out.println(vectorB1);

        System.out.printf("%5.4f\n",cosineSimilarity.cosineSimilarity(vectorA1, vectorB1));
    }

    @CrossOrigin
    @RequestMapping("/getOntologySimilarity")
    public void calculateOntologySimilarity() {

        String firstOntologySampleText = "Java Mysql";
        String secondOntologySampleText = "OOP mysql";

        String firstOntologySample = firstOntologySampleText.toLowerCase();
        String secondOntologySample = secondOntologySampleText.toLowerCase();

        Map<CharSequence, Integer> vectorX = Arrays
                .stream(firstOntologySample.split(" "))
                .collect(Collectors.toMap(word -> word, word -> 1, Integer::sum));
        Map<CharSequence, Integer> vectorY = Arrays
                .stream(secondOntologySample.split(" "))
                .collect(Collectors.toMap(word -> word, word -> 1, Integer::sum));

        String politician_name = "HarinFernando";

        String queryString =
                "PREFIX pol: <http://www.semanticweb.org/viva/ontologies/2019/6/politicians#>" +
                        "SELECT ?Y ?Z WHERE { pol:" + politician_name + " ?Y ?Z .}";
        List<JSONObject> resultSet = InitJena.describePoliticians(queryString);
        System.out.println(resultSet);

    }


    @RequestMapping(value = "/classesList",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public  List<JSONObject> getClassesList() {
        List<JSONObject> resultSet = InitJena.getClassesList();
        System.out.println(resultSet);
        return  resultSet;
    }

    @RequestMapping(value = "/findClasse",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public void getClasseURI(@RequestParam("classname") String className) {
        //List<JSONObject> resultSet = InitJena.getClassesURI("Netbeans","IDEs");
        InitJena.getClassesURI("Netbeans","Junit");
        //System.out.println(resultSet);
        //return  resultSet;
    }

    @RequestMapping(value = "/getJobDescription",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public void getobDescription(@RequestParam("text1") List<JsonString> jobDescription) {
//        System.out.println("Test");
       System.out.println(jobDescription);

//        JSONParser parser = new JSONParser();
//        JSONObject json = (JSONObject) parser.parse(jobDescription);

        for(JsonString text : jobDescription){
            System.out.println(text);
        }


        List<JSONObject> resultSet = InitJena.getClassesList();
        List<String> classesList = new ArrayList<>();

        for (int i = 0; i < resultSet.size(); i++) {
            JSONObject objects = resultSet.get(i);
            String key = objects.getAsString("name");
            classesList.add(key.toLowerCase());
        }

        jobDescription.retainAll(classesList);
        System.out.println(jobDescription);

    }

    @CrossOrigin
    @RequestMapping(value = "/job",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void findPolitician(@RequestBody Job job) {
        System.out.println(job);
    }


}
