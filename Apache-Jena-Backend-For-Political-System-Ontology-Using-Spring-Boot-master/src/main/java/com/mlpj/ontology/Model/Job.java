package com.mlpj.ontology.Model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Job {
    private String URI;
    private List<String> description = new ArrayList<>();

    public Job() {
    }

    public Job(String URI, String description) {
        this.URI = URI;
        this.description = Collections.singletonList(description);
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public List<String> getDescription() {
//        System.out.println("Getting");
//        System.out.println(description);
        return description;
    }

    public void setDescription(List<String> descriptionList) {
        this.description = descriptionList;

//        try{
//            this.description.clear();
//            this.description.addAll(descriptionList);
//            System.out.println("Testing" + description);
//        }catch(NullPointerException e){
//            System.out.print("NullPointerException Caught");
//        }
    }
}
