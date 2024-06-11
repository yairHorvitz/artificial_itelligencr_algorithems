import java.util.*;

public class main {
    public static void main(String[] args) {
        NewNetWork netWork = new NewNetWork();
        netWork.createNetWork("alarm_net.xml");
        String start = new String("B");
        String end = new String("E");
        Set<String> evidences = new HashSet<>();
         // evidences.add("A");
           evidences.add("J");
       boolean ans = bayesBall(netWork, start, end, evidences);
        if (ans == true) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }
    //what are the inputs and outputs of the function should be???????????
    public static boolean bayesBall(NewNetWork netWork, String start, String end, Set<String> evidences) {
        boolean isIndependent = true;
        Queue<Variable> queue = new LinkedList<>();
        for (Variable var : netWork.variables.values()) {// mark all the evidence variables
            if (evidences.contains(var.getVarName())) {
                var.setEvidence();
            }
        }
        if (netWork.variables.get(start).getIsComeFromParrent() == true) {
            netWork.variables.get(start).setComeFrom();
        }
        // what happen if the Start variable is evidence??????????????????????????????????????????????
        queue.add(netWork.variables.get(start));//enter the start variable to the queue
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
    public static double[] elimination(NewNetWork netWork, Variable query, HashMap<String,String>givens){
        double [] ans= new double[3];// answer
        int countSum=0, countMul =0;

        bayesBall(netWork, query.getName(),givens);//The type is set and not hashmap decide after that what better

        createFactorsFromCpt(netWork.variables,givens);   //create Factors From Cpt field in the variable class
        List<Cpt> sortedVariables = new ArrayList<>(netWork.variables.values());
        ComparatorSortByFactorAndAscii comparator = new ComparatorSortByFactorAndAscii();//sort by factory size and ascii
        for (Variable var :sortedVariables) {
            Collections.sort(sortedVariables, comparator);
        }

        for(Variable var:sortedVariables) {//move on all the factors
            join(netWork.variables,var,countMul);// union all the factors that have this variable
            elimunateVar(var,countSum);
        }

        return ans;
    }

    public static void createFactorsFromCpt(HashMap<String, Variable> variables, HashMap<String, String> givens) {
        for (Variable var : variables.values()) {
            // Copy CPT to the factor
            HashMap<String, HashMap<String, Double>> factor = var.getCpt();
            // Set the factor for the variable
            var.setFactor(factor);

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








}

