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
        Cpt2 newCpt = new Cpt2(this);
        //do list of the variables that common to the two cpts
        List<Variable> commonVar = new ArrayList<>();
        for (Variable var : this.getGivenVar()) {
            if (other.getGivenVar().contains(var)) {
                commonVar.add(var);
            }
        }
        //move on all the key of cpt combinations and check if the key with the common variables have the same value in the other cpt
        for (Map.Entry<Map<String, String>, Double> entry : newCpt.getCombinations().entrySet()) {
            Map<String, String> key = entry.getKey();
            boolean isCommon = true;
            for (Variable var : commonVar) {
                if (!key.get(var.getVarName()).equals(other.getCombinations().keySet().stream().filter(e -> e.get(var.getVarName()).equals(key.get(var.getVarName()))).findFirst().get().get(var.getVarName()))) {
                    isCommon = false;
                    break;
                }
            }
            if (isCommon) {//if the key with the common variables have the same value in the other cpt create new <Map<String, String>, Double> that the key is the key of the two cpts and the value is multiplication the value of the two cpts
                Map<String, String> newKey = new HashMap<>();
                newKey.putAll(key);
                newKey.putAll(other.getCombinations().keySet().stream().filter(e -> commonVar.contains(e)).findFirst().get());
                //multiply the values of the two cpts and put the new value in the new cpt
                newCpt.getCombinations().put(newKey, entry.getValue() * other.getCombinations().get(other.getCombinations().keySet().stream().filter(e -> e.equals(newKey)).findFirst().get()));
            }


        }
        return newCpt;
    }
}


