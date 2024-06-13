import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cpt2 {
    private Map<Map<String, String>,Double> combinations;
    List <Variable> givenVar;

public Cpt2(Variable var , HashMap<String, Variable> allVariables) {/* i have in var this information String varName, String[] givenVarName, Double[] probabilityValues*/
    this.combinations = new HashMap<>();
    this.givenVar = new ArrayList<>();
    for (Variable parent : var.getParents()) {// enter all the parents of the variable to the givenVar
        givenVar.add(allVariables.get(parent.getVarName()));
    }
    givenVar.add(var);
    createCpt2(var);
    }
    public void createCpt2(Variable var){
        int[] pointer = new int[givenVar.size()];//creat array that sign which outcome is I
        for (int i = 0; i < givenVar.size(); i++) {
            pointer[i] = 0;
        }
        int whichVarIbe = 0; // which variable is I
        int numOfPermutation = 0; //which number of permutation is I
        Map<String,String> uniqeValue = new HashMap<>();

        while (var.getProbabilityValues().length>numOfPermutation)
        {
            //when not all the outcomes exist and isn't outcome out of the range
           if((whichVarIbe<givenVar.size()) &&( pointer[whichVarIbe]<givenVar.get(whichVarIbe).getOutcomeLength())){
               uniqeValue.put(givenVar.get(whichVarIbe).getVarName(),givenVar.get(whichVarIbe).get_outcomeNames()[pointer[whichVarIbe]]);

               whichVarIbe++;
           }
           else {
               //when all the outcomes exist
                if(whichVarIbe==givenVar.size()){
                    Map<String,String> uniqeValueCopy = new HashMap<>();
                    uniqeValueCopy.putAll(uniqeValue);
                    combinations.put(uniqeValueCopy,var.getProbabilityValues()[numOfPermutation]);
                    numOfPermutation++;
                    whichVarIbe--;
                    pointer[whichVarIbe]++;
                    uniqeValue.remove(givenVar.get(whichVarIbe).getVarName());

                    //pay attention if I need clear the uniqeValue
                  }
                //when the outcome is out of the range
               else if (pointer[whichVarIbe]>=givenVar.get(whichVarIbe).getOutcomeLength()){
                     pointer[whichVarIbe]=0;
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
}



