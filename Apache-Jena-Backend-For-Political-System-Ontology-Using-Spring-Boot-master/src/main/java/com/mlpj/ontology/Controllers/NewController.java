package com.mlpj.ontology.Controllers;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mlpj.ontology.JenaWork.InitJena;
import com.mlpj.ontology.Model.Job;
import net.minidev.json.JSONObject;
import org.apache.jena.atlas.json.JSON;
import org.apache.jena.atlas.json.JsonString;
import org.apache.jena.atlas.json.io.parser.JSONParser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class NewController {
    @RequestMapping(value = "/getJobs",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public void getjobs(@RequestParam("jobDataList") String jobDescription) {
        System.out.println(jobDescription);
        List<String> jobDescriptionList = new ArrayList<>();
        List<Job> jobList = new ArrayList<>();
        Job newJob = new Job();
        List<net.minidev.json.JSONObject> resultSet = InitJena.getClassesList();
        List<String> classesList = new ArrayList<>();

        for (int i = 0; i < resultSet.size(); i++) {
            JSONObject objects = resultSet.get(i);
            String key = objects.getAsString("name");
            classesList.add(key.toLowerCase());
        }

        System.out.println(classesList);
        Gson gson = new Gson();
        Type resultType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> result = gson.fromJson(String.valueOf(jobDescription), resultType);
//        Job job = gson.fromJson(String.valueOf(jobDescription), Job.class);
//        System.out.println(job.getURI());

//        System.out.println(result.get(0).get("URI").toString());
//        System.out.println(result.get(1).get("URI").toString());
//        System.out.println(result.get(2).get("URI").toString());
       // System.out.println(result.get(0).get("summary").toString());


        for(int i =0 ; result.size()>i ; i++){

            jobDescriptionList.clear();

            String str = result.get(i).get("summary").toString();
            str = str.replaceAll("[\\[\\]\\(\\)]", "").replaceAll("\\s+","");
            String[] str1 = str.split(",");

            jobDescriptionList.addAll(Arrays.asList(str1));

            System.out.println(jobDescriptionList);
            jobDescriptionList.retainAll(classesList);

            System.out.println(jobDescriptionList);

            newJob.setURI(result.get(i).get("URI").toString());

            newJob.setDescription(jobDescriptionList);
            jobList.add(newJob);
        }

    }

    @RequestMapping(value = "/calculateSimilarity",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public void calculateSimilarity(@RequestParam("cvData") List<JsonString> cvDescription , @RequestParam("jobDataList") String jobDescription) {

        List<Job> jobTermListObjects = getJobTermsList(jobDescription);

        System.out.println("#########################################");
//        System.out.println(jobTermListObjects.get(0).getURI());
//        System.out.println(jobTermListObjects.get(1).getURI());
//        System.out.println(jobTermListObjects.get(2).getURI());
//        System.out.println(jobTermListObjects.get(3).getURI());
//        System.out.println(jobTermListObjects.get(1).getDescription());
//        System.out.println(jobTermListObjects.get(0).getDescription());
//        System.out.println(jobTermListObjects.get(2).getDescription());


        List<JSONObject> resultSet = InitJena.getClassesList();
        List<String> classesList = new ArrayList<>();
        List<String> cvTermList = new ArrayList<>();

        for(JsonString text : cvDescription){
            cvTermList.add(text.toString().replace("\"", ""));
        }

        for (int i = 0; i < resultSet.size(); i++) {
            JSONObject objects = resultSet.get(i);
            String key = objects.getAsString("name");
            classesList.add(key.toLowerCase());
        }

        cvTermList.retainAll(classesList);
        System.out.println("CV Term List - " + cvTermList);

        for(Job job: jobTermListObjects){
            System.out.println("************************************************");
            System.out.println(job.getDescription());
            for(String cvTerm : cvTermList) {
                    if(job.getDescription().contains(cvTerm)){
                        System.out.println("Similarity Score = 1");
                    }else {
                        for(String jobTerm: job.getDescription()) {
                            System.out.println("This is " + cvTerm + " and this is " + jobTerm);
                            InitJena.getClassesURI(cvTerm,jobTerm);
                        }
                    }
            }
        }

    }

    public static List<Job> getJobTermsList(String jobDescription) {

//        System.out.println(jobDescription);
        //List<String> jobDescriptionList = new ArrayList<>();
        List<Job> jobList = new ArrayList<>();
        List<net.minidev.json.JSONObject> resultSet = InitJena.getClassesList();
        List<String> classesList = new ArrayList<>();

        for (int i = 0; i < resultSet.size(); i++) {
            JSONObject objects = resultSet.get(i);
            String key = objects.getAsString("name");
            classesList.add(key.toLowerCase());
        }

//        System.out.println(classesList);
        Gson gson = new Gson();
        Type resultType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> result = gson.fromJson(String.valueOf(jobDescription), resultType);

        for(int i =0 ; result.size()>i ; i++){

            List<String> jobDescriptionList = new ArrayList<>();
            Job newJob = new Job();

            String str = result.get(i).get("summary").toString();
            str = str.replaceAll("[\\[\\]\\(\\)]", "").replaceAll("\\s+","");
            String[] str1 = str.split(",");

            jobDescriptionList.addAll(Arrays.asList(str1));

//            System.out.println(jobDescriptionList);+
            jobDescriptionList.retainAll(classesList);

//            System.out.println(jobDescriptionList);

            newJob.setURI(result.get(i).get("URI").toString());
            System.out.println(jobDescriptionList);
            newJob.setDescription(jobDescriptionList);
            jobList.add(newJob);
        }
        return  jobList;
    }

}
