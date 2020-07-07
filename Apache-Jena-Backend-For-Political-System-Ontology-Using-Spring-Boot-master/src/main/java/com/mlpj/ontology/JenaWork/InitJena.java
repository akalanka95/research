package com.mlpj.ontology.JenaWork;

import net.minidev.json.JSONObject;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shared.JenaException;
import org.apache.jena.util.FileManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class InitJena {

    private static QueryExecution qe;
    private static String ontoFile = "TechnologyNew.owl";

    public static OntModel readOWL() {
        OntModel ontoModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
        try {
            InputStream in = FileManager.get().open(ontoFile);
            try {
                ontoModel.read(in, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LOGGER.info("Ontology " + ontoFile + " loaded.");
        } catch (JenaException je) {
            System.err.println("ERROR" + je.getMessage());
            je.printStackTrace();
            System.exit(0);
        }
        return ontoModel;
    }

    public static ResultSet execQuery(String queryString) {

        OntModel ontoModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
        try {
            InputStream in = FileManager.get().open(ontoFile);
            try {
                ontoModel.read(in, null);

                Query query = QueryFactory.create(queryString);

                //Execute the query and obtain results
                qe = QueryExecutionFactory.create(query, ontoModel);
                ResultSet results = qe.execSelect();

                // Output query results
                //ResultSetFormatter.out(System.out, results, query);

                return results;

            } catch (Exception e) {
                e.printStackTrace();
            }
            LOGGER.info("Ontology " + ontoFile + " loaded.");
        } catch (JenaException je) {
            System.err.println("ERROR" + je.getMessage());
            je.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    public static List<JSONObject> findPoliticians(String queryString) {
        ResultSet resultSet = execQuery(queryString);
        List<JSONObject> list = new ArrayList<>();
        int x=0;
        while (resultSet.hasNext()) {
            x++;
            JSONObject obj = new JSONObject();
            QuerySolution solution = resultSet.nextSolution();
            obj.put("id",x);
            obj.put("politician_name",solution.get("Y").toString().split("#")[1]);
            list.add(obj);
        }

        // Important ‑ free up resources used running the query
        //qe.close();
        return list;
    }

    public static List<JSONObject> describePoliticians(String queryString) {
        ResultSet resultSet = execQuery(queryString);
        List<JSONObject> list = new ArrayList<>();
        while (resultSet.hasNext()) {
            JSONObject obj = new JSONObject();
            QuerySolution solution = resultSet.nextSolution();
            if (solution.get("Z").toString().split("#")[1].equals("NamedIndividual")){
                continue;
            }
            obj.put("property",solution.get("Y").toString().split("#")[1]);
            obj.put("object",solution.get("Z").toString().split("#")[1]);
            list.add(obj);
        }

        // Important ‑ free up resources used running the query
        //qe.close();
        return list;
    }

    public static List<JSONObject> getItems(String queryString) {
        ResultSet resultSet = execQuery(queryString);
        List<JSONObject> list = new ArrayList<>();
        int x=0;
        while (resultSet.hasNext()) {
            x++;
            JSONObject obj = new JSONObject();
            QuerySolution solution = resultSet.nextSolution();
            obj.put("id",x);
            obj.put("item",solution.get("X").toString().split("#")[1]);
            list.add(obj);
        }

        // Important ‑ free up resources used running the query
        //qe.close();
        return list;
    }

    public static List<JSONObject> getClassesList() {

        OntModel ontoModel = readOWL();
        List<JSONObject> list=new ArrayList();
            try {
                Iterator classIter = ontoModel.listClasses();
                while (classIter.hasNext()) {
                    OntClass ontClass = (OntClass) classIter.next();
                    JSONObject obj = new JSONObject();
                    obj.put("name",ontClass.getLocalName());
                    obj.put("uri",ontClass.getURI());
                    list.add(obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            LOGGER.info("Ontology " + ontoFile + " loaded.");
        return list;
    }

    public static void getClassesURI(String classNameA , String classNameB ) {

        System.out.println("77777777777777" + classNameA + " " + classNameB  );


        OntModel ontoModel = readOWL();
        List<String> clasessListA =new ArrayList();
        List<String> clasessListB =new ArrayList();
        //String classURI = "http://cis.com/technology-ontology#".concat(className);
        //System.out.println(classURI);
        //OntClass personne = ontoModel.getOntClass(classURI);
        //System.out.println(personne.getSuperClass().getLocalName());

        while(!classNameA.equals("IT_Technology") ){
            String classURI = "http://cis.com/technology-ontology#".concat(classNameA);
            System.out.println(classURI);
            OntClass personne = ontoModel.getOntClass(classURI);
            classNameA = personne.getSuperClass().getLocalName();
            clasessListA.add(classNameA);
            System.out.println(classNameA);
        }

        while(!classNameB.equals("IT_Technology") ){
            String classURI = "http://cis.com/technology-ontology#".concat(classNameB);
            OntClass personne = ontoModel.getOntClass(classURI);
            classNameB = personne.getSuperClass().getLocalName();
            clasessListB.add(classNameB);
            System.out.println(classNameB);
        }

        Collections.reverse(clasessListA);
        Collections.reverse(clasessListB);

        System.out.println("Class A ----------------------------------------");

        for (String nameA : clasessListA) {
            System.out.println(nameA);
        }

        System.out.println("Class B ----------------------------------------");
        for (String nameB : clasessListB) {
            System.out.println(nameB);
        }

        for (String nameA : clasessListA) {
            for (String nameB : clasessListB) {

            }
        }

     //Calculate Document Similarity

//        Iterator subIter = personne.listSubClasses();
//        while (subIter.hasNext()) {
//            OntClass sub = (OntClass) subIter.next();
//            JSONObject obj = new JSONObject();
//            obj.put("URI",sub.getURI());
//            list.add(obj);
//        }
    }
}
