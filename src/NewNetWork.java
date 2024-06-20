
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class NewNetWork {
    static int countAdd;
    static int countMul;

    HashMap<String, Variable> variables = new HashMap<String, Variable>();
    HashMap<String, Definition> definitions = new HashMap<String, Definition>();



    public Variable getVariable(String name) {
        return variables.get(name);
    }


    public void createNetWork(String path) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            try {
                Document doc = builder.parse(new File(path));
                doc.getDocumentElement().normalize();

                // Parsing VARIABLE elements create HashMap of variables that contain all the variables
                NodeList varList = doc.getElementsByTagName("VARIABLE");
                for (int i = 0; i < varList.getLength(); i++) {
                    Node varNode = varList.item(i);
                    if (varNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element varElement = (Element) varNode;
                        String varName = varElement.getElementsByTagName("NAME").item(0).getTextContent();

                        NodeList outcomes = varElement.getElementsByTagName("OUTCOME");
                        String[] outcomeNames = new String[outcomes.getLength()];
                        for (int j = 0; j < outcomes.getLength(); j++) {
                            Node outcomeNode = outcomes.item(j);
                            if (outcomeNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element outcomeElement = (Element) outcomeNode;
                                String outcomeName = outcomeElement.getTextContent();
                                outcomeNames[j] = outcomeName;
                            }
                        }
                        Variable variable = new Variable(varName, outcomeNames);
                        variables.put(varName, variable);
                    }
                }

                // Parsing DEFINITION elements
                NodeList defList = doc.getElementsByTagName("DEFINITION");
                for (int i = 0; i < defList.getLength(); i++) {
                    Node defNode = defList.item(i);
                    if (defNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element defElement = (Element) defNode;
                        String defFor = defElement.getElementsByTagName("FOR").item(0).getTextContent();
                        //create givens array
                        NodeList givenList = defElement.getElementsByTagName("GIVEN");
                        String[] givens = new String[givenList.getLength()];
                        for (int j = 0; j < givenList.getLength(); j++) {
                            Node givenNode = givenList.item(j);
                            if (givenNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element givenElement = (Element) givenNode;
                                String givenName = givenElement.getTextContent();
                                givens[j] = givenName;
                            }
                        }
                        //create cpt
                        NodeList tableList = defElement.getElementsByTagName("TABLE");
                        if (tableList.getLength() > 0) {
                            String tableContent = tableList.item(0).getTextContent().trim();
                            String[] tableValuesStr = tableContent.split("\\s+");
                            Double[] tableValues = new Double[tableValuesStr.length];
                            for (int j = 0; j < tableValuesStr.length; j++) {
                                tableValues[j] = Double.parseDouble(tableValuesStr[j]);
                            }
                            Definition definition = new Definition(defFor, givens, tableValues);
                            definitions.put(defFor, definition);
                        }
                    }
                }

            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
//to create the network
        for (String value : variables.keySet()) { //for each variable put children and parents
            Variable var = variables.get(value);
            Definition def = definitions.get(var.getVarName());
            def.getGivens();
            for (String given : def.getGivens()) {
                Variable varParrent = variables.get(given);
                var.addParent(varParrent);
                varParrent.addChild(var);
            }

        }
        for (Variable var : variables.values()) { // for each variable put the cpt
            var.setProbabilityValues(definitions.get(var.getVarName()).getTable());

        }
        for (Variable var : variables.values()) { // for each variable put the cpt

            Cpt2 cpt = new Cpt2(var, variables);//pay attention that I have all the nececcry things to built cpt!!!!!!!!!!!!!!!!!11
            var.setCpt(cpt); //that contain the right cpt in the var!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }


    }
    public String answer(String line){
        //check if the line is elimination
        if(line.startsWith("P(")){
            int splitQueryEvidence;
            int splitEvidenceHidden;
              splitQueryEvidence= line.indexOf("|");
              splitEvidenceHidden = line.indexOf(")");
              //in this query I need also to save is wanted value
              String query= line.substring(2,splitQueryEvidence);
              String evidences =line.substring(splitQueryEvidence+1,splitEvidenceHidden);
              String hidden = line.substring(splitEvidenceHidden+2);
            HashMap<String, String> evidencesMap = new HashMap<>();
            if (evidences.length()!=0) {
                String[] evidencesArr = evidences.split(",");
                for (String evidence : evidencesArr) {
                    String[] evidenceArr = evidence.split("=");
                    evidencesMap.put(evidenceArr[0], evidenceArr[1]);
                }
            }
            Queue<String> hiddenVariables = new LinkedList<>();
            String [] hiddenArr = hidden.split("-");
            for (String hiddenVar : hiddenArr){
                hiddenVariables.add(hiddenVar);
            }
            String ans = elimination(query,evidencesMap,hiddenVariables);
            return ans;


        }
        //if the line is bayes ball
        else
        {
            int splitTypes;
           splitTypes= line.indexOf("|");
           String query= line.substring(0,splitTypes);
           int splitQuery = query.indexOf("-");
           String evidences =line.substring(splitTypes+1);
           String start = query.substring(0,splitQuery);
           String end = query.substring(splitQuery+1);
            HashMap<String, String> evidencesMap = new HashMap<>();
           if (evidences.length()!=0) {
               String[] evidencesArr = evidences.split(",");
               for (String evidence : evidencesArr) {
                   String[] evidenceArr = evidence.split("=");
                   evidencesMap.put(evidenceArr[0], evidenceArr[1]);
               }
           }
                boolean ans = bayesBall(start,end,evidencesMap);
                if(ans == true){
                    return "yes";
                }
                else
                    return "no";
        }

    }
    public boolean bayesBall(String start, String end, HashMap<String,String> evidences) {
        //print check that all the variables arent evidence and the fields comefromparrent and sendchild and sendparent are false
        for (Variable var : variables.values()) {
            if (variables.containsKey(var.getVarName())) {
                var.setRestartEvidence();
            }
        }
        for (Variable var : variables.values()) {
            if (var.getIsComeFromParrent() == true) {
                var.setComeFrom();
            }
            var.setSendChildAtStart();
            var.setSendParentAtStart();
        }
//        //print check that all the variables arent evidence and the fields comefromparrent and sendchild and sendparent are false

        boolean isIndependent = true;
        Queue<Variable> queue = new LinkedList<>();
        for (Variable var : variables.values()) {// mark all the evidence variables
            if (evidences.containsKey(var.getVarName())) {
                var.setEvidence();
            }
        }
        if (variables.get(start).getIsComeFromParrent() == true) {
            variables.get(start).setComeFrom();
        }
        for (Variable var : variables.values()) {
            var.setSendChildAtStart();
            var.setSendParentAtStart();
        }

        queue.add(variables.get(start));//enter the start variable to the queue
        while (!queue.isEmpty()) {
            Variable current = queue.poll();
            if (current.getVarName().equals(end)) {//check if the current variable is the end variable
                isIndependent = false;//print no to file
                return isIndependent;
            }
            if (current.isEvidence() == true) {//if it is evidence variable
                if (current.getIsComeFromParrent() == true) {//if it comes from parent
                    if (current.getIsSendParent() == false) {//if it didn't send to parent
                        current.setSendParent();
                        current.getParents().forEach(parent -> {
                            if (parent.getIsComeFromParrent() == true) {//mark that come from child
                                parent.setComeFrom();
                            }
                            queue.add(parent);//enter the parent to the queue
                        });


                    }

                }
            } else {                             //if it isn't evidence variable
                if (current.getIsComeFromParrent() == true) {//if it comes from parent
                    if (current.getIsSendChild() == false) {//if it didn't send to parent
                        current.setSendChild();
                        current.getChildren().forEach(child -> {
                            if (child.getIsComeFromParrent() == false) {//mark that come from parent
                                child.setComeFrom();
                            }
                            queue.add(child);//enter the child to the queue
                        });


                    }
                } else {                                       //if it isnt evidence and come from child
                    if (current.getIsSendChild() == false) {//didn't send to child
                        current.setSendChild();
                        current.getChildren().forEach(child -> {
                            if (child.getIsComeFromParrent() == false) {//mark that come from parent
                                child.setComeFrom();
                            }
                            queue.add(child);//enter the child to the queue
                        });
                    }
                    if (current.getIsSendParent() == false) {//if it didn't send to parent
                        current.setSendParent();
                        current.getParents().forEach(parent -> {
                            if (parent.getIsComeFromParrent() == true) {//mark that come from child
                                parent.setComeFrom();
                            }
                            queue.add(parent);//enter the parent to the queue
                        });

                    }


                }
            }
        }
        return isIndependent;//the variable independent
    }

    public  String elimination(String queryAndValue, HashMap<String,String>evidences,Queue<String>hiddenVariables){
        countAdd = 0;
        countMul = 0;
        int splitQuery = queryAndValue.indexOf("=");
        String query = queryAndValue.substring(0,splitQuery);
        String valueOfTheQuery = queryAndValue.substring(splitQuery+1);
        HashMap<String,String> queryMap = new HashMap<>();
        queryMap.put(query,valueOfTheQuery);
        List<Variable> independents = new ArrayList<>();//
        List<Cpt2> factors = new ArrayList<>();

        for (var var : variables.values()) {//put all the dependent variables in the factors list
            boolean ans = bayesBall( query, var.getName(), evidences);
            if (ans) {
                independents.add(var);
            }
        }
        Set<Variable> ancestor = getAncestor(query,evidences);
        //get ancestor(אב קדמון) of the query and the evidences

        for (Variable var : ancestor) {
            boolean independent = false;
            for (Variable given : var.getCpt2().getGivenVar()) {
                if (independents.contains(given)) {
                    independent = true;
                    break;
                }
            }
            if (!independent) {
                Cpt2 factor = (new Cpt2(var.getCpt2())).removeEvidence(evidences);
                if (!factor.getGivenVar().isEmpty()) {
                    factors.add(factor);
                }
            }
        }


        //print factors after remove evidence
        for (Cpt2 factor : factors) {
            System.out.println("factors: " + factor.getCombinations());
        }

        if (factors.size() == 1) {
            factors.get(0).normalize();
            double queryValue = factors.get(0).getCombinations().get(queryMap);
            return String.format("%.5f", queryValue) + ",0,0";
        }

        //elimnate hidden from the queue
        factors.sort(new ComparatorSortByFactorAndAscii());
        while (!hiddenVariables.isEmpty()) {
            String hidden = hiddenVariables.poll();
            eliminate(factors, hidden);
        }
        isolate(factors, query);

        if (factors.size() != 1)
            throw new RuntimeException("Error in elimination");

        Cpt2 finalFactor = factors.get(0);
        finalFactor.normalize();
        countAdd += finalFactor.getCombinations().size()-1;
        double queryValue = finalFactor.getCombinations().get(queryMap);
        return String.format("%.5f", queryValue) + "," + countAdd + "," + countMul;
    }

    public void eliminate(List<Cpt2> factors, String hidden) {
        int hiddenIndex = isolate(factors, hidden);
        if (hiddenIndex == -1) return;

        Cpt2 hiddenFactor = factors.remove(hiddenIndex);
        int originalSize = hiddenFactor.getCombinations().size();
        Cpt2 newFactor = hiddenFactor.eliminateVar(hidden);
        countAdd += originalSize - newFactor.getCombinations().size();

        factors.add(newFactor);
        factors.sort(new ComparatorSortByFactorAndAscii());
    }

    /**
     * Isolate the hidden variable from the factors
     */
    public int isolate(List<Cpt2> factors, String hidden) {
        while (true) {
            int firstHidden = -1;
            int secondHidden = -1;
            for (int i = 0; i < factors.size(); i++) {
                if (factors.get(i).getGivenVar().contains(variables.get(hidden))) {
                    if (firstHidden == -1) {
                        firstHidden = i;
                    } else {
                        secondHidden = i;
                        break;
                    }
                }
            }
            if (firstHidden == -1 || secondHidden == -1) return firstHidden;

            Cpt2 firstFactor = factors.remove(firstHidden);
            Cpt2 secondFactor = factors.remove(secondHidden - 1);
            Cpt2 newFactor = firstFactor.joinCpt2(secondFactor);
            countMul += newFactor.getCombinations().size();
            factors.add(newFactor);
            factors.sort(new ComparatorSortByFactorAndAscii());
        }
    }


    private Set<Variable> getAncestor(String query, HashMap<String,String> evidences) {
        Set<Variable> ancestors = new HashSet<>();
        Set<String> needToCheck = new HashSet<>();
        needToCheck.addAll(evidences.keySet());
        needToCheck.add(query);

        // Iterate over all variables that need to be checked
        for (String varName : needToCheck) {
            Variable var = variables.get(varName);
            Queue<Variable> queue = new LinkedList<>();
            queue.add(var);

            // Perform BFS to find all ancestors
            while (!queue.isEmpty()) {
                Variable current = queue.poll();
                if (!current.getIsAncestor()) {
                    current.setIsAncestor(); // Mark the current variable as an ancestor
                    ancestors.add(current); // Add the current variable to the ancestors set

                    // Add all parents of the current variable to the queue
                    for (Variable parent : current.getParents()) {
                        queue.add(parent);
                    }
                }
            }
        }

        // Reset the isAncestor flag for all variables
        for (Variable var : variables.values()) {
            var.restartIsAncestor();
        }


        return ancestors;
    }
}



