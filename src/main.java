import java.util.*;

public class main {
    public static void main(String[] args) {
        NewNetWork netWork = new NewNetWork();
        netWork.createNetWork("alarm_net.xml");
        String start = new String("B");
      //  String end = new String("E");
        HashMap<String,String> evidences = new HashMap<String,String>();
           evidences.put("A","T");
        elimination(netWork.variables,start,evidences);

      // boolean ans = bayesBall(netWork.variables, start, end, evidences);
       // if (ans == true) {
          //  System.out.println("yes");
        //} else {
          //  System.out.println("no");
       // }
       /* for (var var : netWork.variables.values()) {
            System.out.println(var.getVarName());
            for(String outcome: var.get_outcomeNames() ){
                System.out.println(outcome);
            }
            for(Variable given: var.getCpt2().givenVar){
                System.out.println("given: " + given.getVarName());
            }

            for (Map.Entry<Map<String,String>,Double> entry: var.getCpt2().getCombinations().entrySet()){
                System.out.println("key: " + entry.getKey());
                System.out.println("value: " + entry.getValue());
            }

            for(Variable parent: var.getParents()){
                System.out.println("PARENT: " + parent.getVarName());
            }
            for(Variable child: var.getChildren()){
                System.out.println("children: " + child.getVarName());
            }
            for (Double p: var.getProbabilityValues()){
                System.out.println(p);
            }
            System.out.println(var.getIsComeFromParrent());
            System.out.println(var.getIsSendParent());
            System.out.println(var.getIsSendChild());
            System.out.println(var.isEvidence());

            printCombinations(var.getCpt2().getCombinations());
        }
*/

    }
    //printing the combinations of the CPT
    public static void printCombinations(Map<Map<String, String>, Double> combinations) {
        for (Map.Entry<Map<String, String>, Double> entry : combinations.entrySet()) {
            Map<String, String> combination = entry.getKey();
            Double probability = entry.getValue();

            StringBuilder combinationString = new StringBuilder();
            for (Map.Entry<String, String> subEntry : combination.entrySet()) {
                combinationString.append(subEntry.getKey()).append(": ").append(subEntry.getValue()).append(", ");
            }

            // Remove the trailing comma and space
            if (combinationString.length() > 0) {
                combinationString.setLength(combinationString.length() - 2);
            }

            System.out.println("Combination: {" + combinationString.toString() + "} Probability: " + probability);
        }
    }

    //what are the inputs and outputs of the function should be???????????
    public static boolean bayesBall(HashMap<String,Variable> variables, String start, String end, HashMap<String,String> evidences) {
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
    public static double [] elimination(HashMap<String,Variable> variables, String query, HashMap<String,String>evidences){
        List<Variable> dependents = new ArrayList<>();//
        List<Cpt2> factors = new ArrayList<>();
        for (var var : variables.values()) {//put all the dependent variables in the factors list
            boolean ans = bayesBall(variables, query, var.getName(), evidences);
            if (ans == false) {
                dependents.add(var);
            }
        }
      /*  for (Variable var : dependents) {
            System.out.println("is dependent " + var.getVarName());
        }*/
        for (Variable var : dependents) {//create factors from the CPT field in the variable class
            Cpt2 copyCpt = new Cpt2(var.getCpt2());
            factors.add(copyCpt);
        }
        /*
        for (Cpt2 factor : factors) {
            System.out.println("factor: ");
            printCombinations(factor.getCombinations());
        }*/
        //sort the factors by the size of the factor and the ascii of the variables
        ComparatorSortByFactorAndAscii comparator = new ComparatorSortByFactorAndAscii();
        factors.sort(comparator);
        for (Cpt2 factor : factors) {
            System.out.println("factors after sort: ");
            printCombinations(factor.getCombinations());
        }
        //take the first factor and join it with the second factor
        Cpt2 newFactor = factors.get(0).joinCpt2(factors.get(1));
        newFactor = newFactor.eliminateVar("B");


        return null;
    }



}


    /*
    public static double[] elimination(HashMap<String,Variable> variables, Variable query, HashMap<String,String>givens){
        double [] ans= new double[3];// answer
        int countSum=0, countMul =0;
        //send all the variables to bayesBall and save the dependent variables
        Set<Variable> dependentVariables = new HashSet<>();
        for (Variable var : variables.values()) {
            if(bayesBall(variables, query.getName(), var.getName(), givens)){
                dependentVariables.add(var);
            }
        }
        //create factors from the CPT field in the variable class
        List <Cpt> factors =new ArrayList<>();
        createFactorsFromCpt(dependentVariables,givens);   //create Factors From Cpt field in the variable class
        List<Cpt> sortedVariables = new ArrayList<>(variables);
        ComparatorSortByFactorAndAscii comparator = new ComparatorSortByFactorAndAscii();//sort by factory size and ascii
        for (Variable var :sortedVariables) {
            Collections.sort(sortedVariables, comparator);
        }

        for(Variable var:sortedVariables) {//move on all the factors
            join(variables,var,countMul);// union all the factors that have this variable
            elimunateVar(var,countSum);
        }

        return ans;
    }

    public static void createFactorsFromCpt(Set<Variable> variables, HashMap<String,String> givens) {
        for (Variable var : variables) {
            // Copy CPT to the factor
            Cpt factor = var.getCpt();
            // Set the factor for the variable


            // Remove known values from the factor
            for (Map.Entry<String, String> given : givens.entrySet()) {
                String evidenceVar = given.getKey();
                String evidenceVal = given.getValue();

                // Check if the factor contains the evidence variable
                if (factor.containsKey(evidenceVar)) {
                    HashMap<String, Double> valueMap = factor.get(evidenceVar);
                    // Remove the values that do not match the evidence
                    valueMap.entrySet().removeIf(entry -> !entry.getKey().equals(evidenceVal));
                }
            }
        }
    }

    public static HashMap<String,HashMap<String,Double>> join(HashMap variables,Variable var,int countMul){
        //move on all the factors
        Set<Variable> existEleminationVar= new HashSet<>();//all the variables that in the factor exist the elemination var
        for (Variable var : variables.values()) {
            //union all the factors that have this variable
            if (var.getFactor().containsKey(var.getName())) {
                existEleminationVar.add(var);
            }
            //join all the variables factors in the set to one factor





        }

    }
}
*///untill here the try!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
     /*
    public  static void createFactorsFromCpt(HashMap<String,Variable> variabls,HashMap<String,String>givens)

   {

        for (Variable var: variabls.values()//copy all the cpt to the factor
             ) {
            HashMap<String,HashMap<String,Double>> factor =new HashMap<>();
            factor = var.getCpt();
            var.setFactor(factor);
        }
        //remove from all factor the all values that we know they uncorrect
        }
*/










