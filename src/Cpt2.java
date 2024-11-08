import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Cpt2 {
    private Map<Map<String, String>, Double> combinations;
    List<Variable> givenVar;

    public Cpt2(Variable var, HashMap<String, Variable> allVariables) {/* i have in var this information String varName, String[] givenVarName, Double[] probabilityValues*/
        this.combinations = new HashMap<>();
        this.givenVar = new ArrayList<>();
        for (Variable parent : var.getParents()) {// enter all the parents of the variable to the givenVar
            givenVar.add(allVariables.get(parent.getVarName()));
        }
        givenVar.add(var);
        createCpt2(var);
    }

    public Cpt2(Cpt2 other) {
        this.combinations = new HashMap<>();
        this.givenVar = new ArrayList<>();
        for (Map.Entry<Map<String, String>, Double> entry : other.getCombinations().entrySet()) {
            this.combinations.put(new HashMap<>(entry.getKey()), entry.getValue());
        }
        for (Variable var : other.getGivenVar()) {
            this.givenVar.add(var);
        }

    }

    // create empty constructor
    public Cpt2() {
        this.combinations = new HashMap<>();
        this.givenVar = new ArrayList<>();
    }


    public void createCpt2(Variable var) {
        int[] pointer = new int[givenVar.size()];//creat array that sign which outcome is I
        for (int i = 0; i < givenVar.size(); i++) {
            pointer[i] = 0;
        }
        int whichVarIbe = 0; // which variable is I
        int numOfPermutation = 0; //which number of permutation is I
        Map<String, String> uniqeValue = new HashMap<>();

        while (var.getProbabilityValues().length > numOfPermutation) {
            //when not all the outcomes exist and isn't outcome out of the range
            if ((whichVarIbe < givenVar.size()) && (pointer[whichVarIbe] < givenVar.get(whichVarIbe).getOutcomeLength())) {
                uniqeValue.put(givenVar.get(whichVarIbe).getVarName(), givenVar.get(whichVarIbe).get_outcomeNames()[pointer[whichVarIbe]]);

                whichVarIbe++;
            } else {
                //when all the outcomes exist
                if (whichVarIbe == givenVar.size()) {
                    Map<String, String> uniqeValueCopy = new HashMap<>();
                    uniqeValueCopy.putAll(uniqeValue);
                    combinations.put(uniqeValueCopy, var.getProbabilityValues()[numOfPermutation]);
                    numOfPermutation++;
                    whichVarIbe--;
                    pointer[whichVarIbe]++;
                    uniqeValue.remove(givenVar.get(whichVarIbe).getVarName());

                    //pay attention if I need clear the uniqeValue
                }
                //when the outcome is out of the range
                else if (pointer[whichVarIbe] >= givenVar.get(whichVarIbe).getOutcomeLength()) {
                    pointer[whichVarIbe] = 0;
                    whichVarIbe--;
                    pointer[whichVarIbe]++;
                }

            }


        }


    }


    public Map<Map<String, String>, Double> getCombinations() {
        return combinations;
    }

    public List<Variable> getGivenVar() {
        return givenVar;
    }

    public Cpt2 joinCpt2(Cpt2 other) {
        Cpt2 newCpt = new Cpt2();
        //do list of the variables that common to the two cpts
        List<Variable> commonVar = new ArrayList<>();
        List<Variable> unCommonvar = new ArrayList<>();
        List<Variable> newGivenVar = new ArrayList<>();
        newGivenVar.addAll(this.getGivenVar());

        for (Variable var : other.getGivenVar()) {
            if (!newGivenVar.contains(var)) {
                newGivenVar.add(var);
            }
        }

        for (Variable var : this.getGivenVar()) {
            if (other.getGivenVar().contains(var)) {
                commonVar.add(var);
            }
            if (!other.getGivenVar().contains(var)) {
                unCommonvar.add(var);
            }
        }
        for (Variable var : other.givenVar) {
            if (!this.getGivenVar().contains(var)) {
                unCommonvar.add(var);
            }
        }
        //move on all the key of cpt combinations and check if the key with the common variables have the same value in the other cpt
        for (Map.Entry<Map<String, String>, Double> entry : this.getCombinations().entrySet()) {
            //built deep copy for every items
            Map<String, String> key = entry.getKey();
            //move on all the key of other cpt combinations
            for (Map.Entry<Map<String, String>, Double> otherEntry : other.getCombinations().entrySet()) {
                Map<String, String> otherKey = otherEntry.getKey();
                //create new <Map<String, String>, Double> that the key is the key of the two cpts and the value is multiplication the value of the two cpts
                //
                //if all the common variables have the same value in the two cpts
                boolean isCommon = true;
                for (Variable var : commonVar) {
                    if (!key.get(var.getVarName()).equals(otherKey.get(var.getVarName()))) {
                        isCommon = false;
                        break;
                    }
                }
                //if the key with the common variables have the same value in the other cpt create new <Map<String, String>, Double>
                if (isCommon) {
                    Map<String, String> newKey = new HashMap<>();
                    //  the key is the key of the two cpts and the value is multiplication the value of the two cpts
                    newKey.putAll(key);
                    for (Map.Entry<String, String> otherEntryKey : otherKey.entrySet()) {
                        if (!newKey.containsKey(otherEntryKey.getKey())) {
                            newKey.put(otherEntryKey.getKey(), otherEntryKey.getValue());
                        }
                    }
                    newCpt.getCombinations().put(newKey, entry.getValue() * otherEntry.getValue());
//                    NewNetWork.countMul++;
                }

            }
        }
        newCpt.getGivenVar().addAll(newGivenVar);
        System.out.println("number of multiplication: " + NewNetWork.countMul);
        //print the new cpt
        System.out.println("new cpt: " + newCpt.getCombinations().entrySet().stream().map(e -> e.getKey().entrySet().stream().map(ee -> ee.getKey() + "=" + ee.getValue()).collect(Collectors.joining(", ")) + " -> " + e.getValue()).collect(Collectors.joining("\n")));
        //print the new cpt given variables
        System.out.println("new cpt given variables: " + newCpt.getGivenVar().stream().map(Variable::getVarName).collect(Collectors.joining(", ")));

        return newCpt;
    }



    // //remove all the evidences from the factors that aren't happened

    public Cpt2 removeEvidence(HashMap<String, String> evidence) {
        Cpt2 res = new Cpt2();
        for (Variable var : this.getGivenVar()) {
            if (!evidence.containsKey(var.getVarName())) {
                res.getGivenVar().add(var);
            }
        }
        List<Map<String, String>> keysToAdd = new ArrayList<>();

        // Move on all the key of the cpt combinations
        for (Map<String, String> key : this.getCombinations().keySet()) {
            boolean containsEvidence = false;
            // Move on all the evidence
            for (String evidenceName : evidence.keySet()) {
                // If the key of the cpt combinations contains the evidence and the value of the evidence isn't the same as the value of the key, mark the key for removal
                if (key.containsKey(evidenceName)) {
                    if (key.get(evidenceName).equals(evidence.get(evidenceName))) {
                        keysToAdd.add(key);
                        break;
                    }
                    containsEvidence = true;
                }
            }
            if (!containsEvidence) {
                keysToAdd.add(key);
            }
        }

        // Remove all marked keys
        for (Map<String, String> key : keysToAdd) {
            Map<String, String> newKey = new HashMap<>(key);
            for (String evidenceVariable : evidence.keySet()) {
                newKey.remove(evidenceVariable);
            }
            res.getCombinations().put(newKey, this.getCombinations().get(key));
        }

        return res;
    }


    //eliminate the hidden variable from the cpt
    public Cpt2 eliminateVar(String removeHidden) {

        Cpt2 newCpt = new Cpt2();
        List<Variable> newGivenVar = new ArrayList<>();
        newGivenVar.addAll(this.getGivenVar());
        newGivenVar.removeIf(e -> e.getVarName().equals(removeHidden));
        newCpt.getGivenVar().addAll(newGivenVar);
        Double sumOfPermutation = 0.0;
        //move on all the cpt combinations
        while (!this.getCombinations().isEmpty()) {//mabey change with while until the key is empty
            //push the first item from the combinations
            Map.Entry<Map<String, String>, Double> entry = this.getCombinations().entrySet().iterator().next();
            Map<String, String> key = entry.getKey();
            //enter the value of the key to the sum of the permutation
            sumOfPermutation = entry.getValue();
            //remove the key from the combinations
            this.getCombinations().remove(key);

            //get the hidden variable
            Variable hiddenVar = this.getGivenVar().stream().filter(e -> e.getVarName().equals(removeHidden)).findFirst().get();
            for (String outcome : hiddenVar.get_outcomeNames()) {
                //put the key of the hidden variable with the outcome
                key.put(hiddenVar.getVarName(), outcome);
                //search the key in the combinations
                if (this.getCombinations().containsKey(key)) {
                    //add the value of the key to the sum of the permutation
                    sumOfPermutation += this.getCombinations().get(key);
                    this.getCombinations().remove(key);
//                    NewNetWork.countAdd++;
                }
            }
            key.remove(hiddenVar.getVarName());
            newCpt.getCombinations().put(key, sumOfPermutation);
            sumOfPermutation = 0.0;
        }
        //print the new cpt
        //System.out.println("number of sum: " + countsum);
        System.out.println("new cpt after elimination: " + newCpt.getCombinations().entrySet().stream().map(e -> e.getKey().entrySet().stream().map(ee -> ee.getKey() + "=" + ee.getValue()).collect(Collectors.joining(", ")) + " -> " + e.getValue()).collect(Collectors.joining("\n")));
        return newCpt;
    }

    //normalize the cpt
    public void normalize() {
        Double sum = 0.0;
        //sum all the values of the cpt
        for (Map.Entry<Map<String, String>, Double> entry : this.getCombinations().entrySet()) {
            sum += entry.getValue();
        }
        //divide all the values of the cpt by the sum
        for (Map.Entry<Map<String, String>, Double> entry : this.getCombinations().entrySet()) {
            entry.setValue(entry.getValue() / sum);
        }
    }
    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        for (Map.Entry<Map<String, String>, Double> entry : this.getCombinations().entrySet()) {
            ans.append(entry.getKey().entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(", ")).concat(" -> " + entry.getValue() + "\n"));
        }
        return ans.toString();
    }
}









