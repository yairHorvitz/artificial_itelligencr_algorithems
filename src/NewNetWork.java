
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
    int countAdd;
    int countMul;

    HashMap<String, Variable> variables = new HashMap<String, Variable>();
    HashMap<String, Definition> definitions = new HashMap<String, Definition>();

//public static  void main (String[] args) {
  //  NewNetWork netWork = new NewNetWork();
    //netWork.createNetWork("alarm_net.xml");

//}



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
            double [] ans = elimination(query,evidencesMap,hiddenVariables);


        String [] arr = line.split("\\|");
        System.out.println(Arrays.toString(arr));
        }
        //if the line is bayes ball
        else
        {
            int splitTypes;
           splitTypes= line.indexOf("|");
           String query= line.substring(0,splitTypes);
           String evidences =line.substring(splitTypes+1);
           String start = query.substring(0,1);
           String end = query.substring(2,3);
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




        return "get";
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

        // what happen if the Start variable is evidence??????????????????????????????????????????????
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

    public  double [] elimination(String queryAndValue, HashMap<String,String>evidences,Queue<String>hiddenVariables){
        String query = queryAndValue.substring(0,1);
        String valueOfTheQuery = queryAndValue.substring(2,3);
        HashMap<String,String> queryMap = new HashMap<>();
        queryMap.put(query,valueOfTheQuery);
        List<Variable> dependents = new ArrayList<>();//
        List<Cpt2> factors = new ArrayList<>();

        for (var var : variables.values()) {//put all the dependent variables in the factors list
            boolean ans = bayesBall( query, var.getName(), evidences);
            if (ans == false) {
                dependents.add(var);
            }
        }
        //print dependents
        for (Variable var : dependents) {
            System.out.println("dependents: " + var.getVarName());
        }
        Set<Variable> ancestor = new HashSet<>();
        //get ancestor(אב קדמון) of the query and the evidences
        ancestor = getAncestor(query,evidences);


        //enter to factor list all the variable that contain in the dependents and in the ancestor
        for (Variable var : dependents) {
            if (ancestor.contains(var)) {
                Cpt2 copyCpt = new Cpt2(var.getCpt2());
                factors.add(copyCpt);
            }
        }

        //remove all the evidences from the factors that aren't happened
        for (Cpt2 factor : factors) {
            factor.removeEvidence(evidences);
        }
        //print factors after remove evidence
        for (Cpt2 factor : factors) {
            System.out.println("factors: " + factor.getCombinations());
        }

        //elimnate hidden from the queue
        while (!hiddenVariables.isEmpty()) {
            String hidden = hiddenVariables.poll();
            //print check if the hidden equals to "A"
            if(hidden.equals("A")){
                System.out.println("they same!!!!!!!!!!!!!!!!!!!!!!!");
            }
            else{
                System.out.println("they not same!!!!!!!!!!!!!!!!!!!!!!!");
            }
            System.out.println("hidden size: " + hiddenVariables.size());
            PriorityQueue<Cpt2> priorityQueue = new PriorityQueue<>( new ComparatorSortByFactorAndAscii());
            //extract the relevant factors with the hidden variable to the priority queue
            // Extract the relevant factors with the hidden variable to the priority queue
            List<Cpt2> factorsToRemove = new ArrayList<>();
            for (Cpt2 factor : factors) {
                // Check if the all varName in factor.givenVar contains the hidden variable
                for (Variable var : factor.getGivenVar()) {
                    if (var.getVarName().equals(hidden)) {
                        priorityQueue.add(factor);
                        factorsToRemove.add(factor);  // Mark for removal
                        break;
                    }
                }

            }

            // Remove marked factors from the original list
            factors.removeAll(factorsToRemove);
            //multiply all the factors by the join
            System.out.println(priorityQueue.size()+ " first time");
            while (priorityQueue.size() > 1) {
                Cpt2 newFactor = priorityQueue.poll();
                newFactor = newFactor.joinCpt2(priorityQueue.poll());
                priorityQueue.add(newFactor);
            }
               //eliminate the last factor that stay in the priority queue
            System.out.println(priorityQueue.size() + " second time");
            Cpt2 newFactor = priorityQueue.poll();
            newFactor = newFactor.eliminateVar(hidden);
            factors.add(newFactor);
        }
        //after I stay only with the query variable factors
        //multiply all the factors by the join
        while (factors.size() > 1) {
            Cpt2 newFactor = factors.get(0);
            newFactor = newFactor.joinCpt2(factors.get(1));
            factors.remove(0);
            factors.remove(0);
            factors.add(newFactor);
        }
        //normelize
        factors.get(0).normalize();
        //choose the right value
        double queryValue = factors.get(0).getCombinations().get(queryMap);





        //extract (לחלץ) the rellevant factors with the hidden variable
        //while the hidden variables prayority queue put the comparator instance in the () is more than one factor
        //eliminate the hidden variable from the factors
        //return the factor to the list
        //do it until the queue is empty
        //stay only with the query variable factors
        //multiply all the factors by the join
        //normelize
        // choose the right value
        // create string with the right values of the query variable and multiply by the join and countsum




        //take the first factor and join it with the second factor
     //   Cpt2 newFactor = factors.get(0).joinCpt2(factors.get(1));
       // newFactor = newFactor.eliminateVar("B");


        return null;
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

//        // Print the variables to be checked
//        for (String varName : needToCheck) {
//            System.out.println("The start permutation of need to check: " + varName);
//        }

        // Print the ancestors
//        for (Variable var : ancestors) {
//            System.out.println("The start permutation of ancestors: " + var.getVarName());
//        }

        return ancestors;
    }
}



