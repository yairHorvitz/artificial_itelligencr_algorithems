

import java.security.Key;
import java.util.*;

public class Cpt {// the HashMap look like HashMap<HashMap<String-name of var, String-value of var>,Double>
    private String _varName;
    private String[] _givenVarName;
    private Double[] _probabilityValues;
    private Map<Map<String, String>,Double> combinations;
    private  Map<String,String> uniqeValue;
    private Map<String, Variable> _allVariables;

    //I don't know why the methods isn't work good
    public Cpt(String varName, String[] givenVarName, Double[] probabilityValues, HashMap<String, Variable> allVariables) {
        _varName = varName;
        _givenVarName = givenVarName;
        _probabilityValues = probabilityValues;
        combinations = new HashMap<>();//change that isn't the permutation with same name
          uniqeValue = new HashMap<>();
        _allVariables = allVariables;
        Variable[] _allRellevantVar;

        if (givenVarName.length != 0) {
            _allRellevantVar = new Variable[_givenVarName.length + 1];
            for (int i = 0; i < givenVarName.length; i++) {//make Hashmap wth the relevant variables pay attention why the last value appearience twice

                _allRellevantVar[i] = _allVariables.get(givenVarName[i]);
            }

        }
        else {_allRellevantVar = new Variable[1];
        }
        _allRellevantVar[_allRellevantVar.length-1] = _allVariables.get(_varName);
        int[] pointer = new int[_allRellevantVar.length];//creat array that sign which outcome is I


        for (int i = 0; i < _allRellevantVar.length; i++) {
            System.out.println(_allRellevantVar[i].getVarName());

        }
        //create cpt table for each variable with his relevant parameters and probabilities
        createCpt(_allRellevantVar,probabilityValues,pointer);

    }
    public void createCpt(Variable [] allRelevantVariables, Double [] probabilityValues,int [] pointer){

        int whichPointIbe = 0; // which variable is I
        int numOfPermutation = 0; //which number of permutation is I
        String key =""; // string that save the name of all the outcomes
        // run until I end to put in the Hashmap all the values
        while (probabilityValues.length>numOfPermutation){
            //when not all the outcomes exist and isn't outcome out of the range
            if ((pointer[whichPointIbe]<allRelevantVariables.length)&&(allRelevantVariables[whichPointIbe].getOutcomeLength()>=pointer[whichPointIbe]))
            {
                //add to uniqeValue key- varName and to the value - the current outcome
                //pay attantion if I enter the last value I think that I enter
                  uniqeValue.put(allRelevantVariables[whichPointIbe].getVarName(),allRelevantVariables[whichPointIbe].get_outcomeNames()[pointer[whichPointIbe]]);
              //  add the outcome to the key when
                key= key + allRelevantVariables[whichPointIbe].get_outcomeNames()[pointer[whichPointIbe]];// add the new name to the KEY

            }
            else
            {
                // if the amount of the outcomes smaller than the pointer outcomes we must fix it.
                if(!(allRelevantVariables[whichPointIbe].getOutcomeLength()>=pointer[whichPointIbe])){
                    key.substring(0, key.length() - allRelevantVariables[whichPointIbe].get_outcomeNames()[pointer[whichPointIbe]].length()) ;//remove the last value in the string
                    pointer[whichPointIbe]=0;//to start from the first outcome
                    whichPointIbe--;//return one step to before variable
                }
                if (pointer[whichPointIbe]==allRelevantVariables.length )//if all the outcomes exist create new Hashmap
                {
                    //check if I need this line I think that I don't enter the last value without this!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    //uniqeValue.put(allRelevantVariables[whichPointIbe].getVarName(),allRelevantVariables[whichPointIbe].get_outcomeNames()[pointer[whichPointIbe]]);
                    combinations.put(uniqeValue,probabilityValues[numOfPermutation]);
                    //HashMap<String,Double> onePermutation=new HashMap<>();
                    //onePermutation.put(key,probabilityValues[numOfPermutation]);
                    numOfPermutation++;
                   // combinations.put(key,onePermutation);//check if the key and value are good I do above more one!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    key = key.substring(0, key.length() - allRelevantVariables[whichPointIbe].get_outcomeNames()[pointer[whichPointIbe]].length()) ;//remove the last value in the string
                    pointer[pointer.length-1]++;//add the last pointer value because that what added

                }

            }

        }


    }

    public Map<Map<String, String>, Double> getCombinations() {
        return combinations;
    }
}




/*import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cpt {
    private String _varName;
    private String[] _givenVarName;
    private Double[] _probabilityValues;
    private HashMap<String, HashMap<String, Double>> combinations;
    private  HashMap<String,String> uniqeValue;!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private HashMap<String, Variable> _allVariables;

//I don't know why the methods isn't work good
    public Cpt(String varName, String[] givenVarName, Double[] probabilityValues, HashMap<String, Variable> allVariables) {
        _varName = varName;
        _givenVarName = givenVarName;
        _probabilityValues = probabilityValues;
        combinations = new HashMap<>();
      //  uniqeValue = new HashMap<>();
        _allVariables = allVariables;
        Variable[] _allRellevantVar;

        if (givenVarName.length != 0) {
            _allRellevantVar = new Variable[_givenVarName.length + 1];
            for (int i = 0; i < givenVarName.length; i++) {//make hash map wth the relvvant varibles pay atention why the last value appearience twice

                _allRellevantVar[i] = _allVariables.get(givenVarName[i]);
            }

        }
        else {_allRellevantVar = new Variable[1];
        }
        _allRellevantVar[_allRellevantVar.length-1] = _allVariables.get(_varName);
        int[] pointer = new int[_allRellevantVar.length];


        for (int i = 0; i < _allRellevantVar.length; i++) {
            System.out.println(_allRellevantVar[i].getVarName());

        }
        createCpt(_allRellevantVar,probabilityValues,pointer);

    }
public void createCpt(Variable [] allRelevantVariables, Double [] probabilityValues,int [] pointer){

    int whichPointIbe=0;
    String key ="";
    int numOfPermutation=0;
    while (probabilityValues.length>numOfPermutation){// run until i end to put in the hash map all the values
        //add the outcome to the key
        if ((key.length()<allRelevantVariables.length)&&(allRelevantVariables[whichPointIbe].getOutcomeLength()>=pointer[whichPointIbe]))
        {
          //  uniqeValue.put(allRelevantVariables[whichPointIbe].getVarName(),allRelevantVariables[whichPointIbe].get_outcomeNames()[pointer[whichPointIbe]]);

            key=key+allRelevantVariables[whichPointIbe].get_outcomeNames()[pointer[whichPointIbe]];// add the new name to the KEY

        }
        else
        {
            if(!(allRelevantVariables[whichPointIbe].getOutcomeLength()>=pointer[whichPointIbe])){//check that the amount of the outcomes isnt smaller then the pointer.
                pointer[whichPointIbe]=0;//to start from the begining outcome
                key.substring(0, key.length() - 1) ;//remove the last value in the string
                whichPointIbe--;//return one step to before varible
            }
            if (key.length()==allRelevantVariables.length )//create new Hashmap
            {
                // HashMap<HashMap<String, String>,Double> onePermutation=new HashMap<>();//if i want change the the Hashmap that contain the value of all uniqeVal
                //onePermutation.put(uniqeValue,probabilityValues[numOfPermutation])
                //combinations.put(key,onePermutation);//i think isnt nessery but if yes i need changed the combination input
                HashMap<String,Double> onePermutation=new HashMap<>();
                onePermutation.put(key,probabilityValues[numOfPermutation]);
                numOfPermutation++;
                combinations.put(key,onePermutation);//check if the key and value are good!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                key = key.substring(0, key.length() - 1) ;//remove the last value in the string
                pointer[pointer.length-1]++;

            }

        }




    }


}*/



/*
import java.util.HashMap;

public class CPTGenerator {

    public static void createCpt(Variable[] allRelevantVariables, double[] probabilityValues) {
        int[] pointer = new int[allRelevantVariables.length]; // Initialize the pointer array

        HashMap<String, Double> combinations = new HashMap<>();

        int numOfPermutation = 0;
        while (numOfPermutation < probabilityValues.length) {
            StringBuilder keyBuilder = new StringBuilder();

            for (int i = 0; i < allRelevantVariables.length; i++) {
                Variable currentVariable = allRelevantVariables[i];
                if (currentVariable.getOutcomeLength() >= pointer[i]) {
                    keyBuilder.append(currentVariable.getOutcomeNames());
                } else {
                    pointer[i] = 0; // Reset pointer
                    keyBuilder.setLength(keyBuilder.length() - currentVariable.getOutcomeNames().length());
                    i--; // Move back one step to the previous variable
                }
            }

            String key = keyBuilder.toString();
            if (key.length() == allRelevantVariables.length) {
                combinations.put(key, probabilityValues[numOfPermutation]);
                numOfPermutation++;
                pointer[pointer.length - 1]++;
            }
        }

        // Now 'combinations' contains the complete CPT
        // You can use it as needed
    }

    // Define the Variable class with relevant methods (getOutcomeLength, getOutcomeNames, etc.)
    // ...

    public static void main(String[] args) {
        // Example usage:
        // Create your 'allRelevantVariables' array and 'probabilityValues' array
        // Call createCpt(allRelevantVariables, probabilityValues);
    }
}


    private void createCpt(HashMap<String, Variable> variables) {
        List<String> varNames = new ArrayList<>();//create String list of the varNames
        for (String givenVarName : _givenVarName) {
            varNames.add(givenVarName);
            System.out.println("the name of the variables is:"+ givenVarName);
        }
        varNames.add(_varName);

        List<Variable> familyVariables = new ArrayList<>();// create Variable list of the vairebles
        for (String varName : varNames) {
            familyVariables.add(variables.get(varName));
            System.out.println("the name of the variables is:"+ varName);
        }


    }*/

/*
    private void recursiveWay(List<Variable> variables, List<String> values, List<String> names) {
        recursiveWay(familyVariables, new ArrayList<>(), new ArrayList<>());
        if (variables.size() == values.size()) {
            HashMap<String, String> combination = new HashMap<>();
            StringBuilder keyBuilder = new StringBuilder();
            for (int i = 0; i < values.size(); i++) {
                combination.put(variables.get(i).getName(), values.get(i));
                keyBuilder.append(variables.get(i).getName()).append("=").append(values.get(i)).append(",");
            }
            String key = keyBuilder.toString();

            boolean allVariablesHaveOutcomes = true;
            for (Variable variable : variables) {
                String[] outcomeNames = variable.get_outcomeNames();
                boolean hasAllOutcomes = false;
                for (String outcome : outcomeNames) {
                    if (values.contains(outcome)) {
                        hasAllOutcomes = true;
                        break;
                    }
                }
                if (!hasAllOutcomes) {
                    allVariablesHaveOutcomes = false;
                    break;
                }
            }

            if (allVariablesHaveOutcomes) {
                combinations.put(key, combination);
                pointer++;
            }
            return;
        }

        Variable variable = variables.get(values.size());
        String[] outcomeNames = variable.get_outcomeNames();
        for (String possibleValue : outcomeNames) {
            values.add(possibleValue);
            names.add(possibleValue);
            recursiveWay(variables, values, names);
            values.remove(values.size() - 1);
            names.remove(names.size() - 1);
        }
    }

    public void printCombinations() {
        for (String key : combinations.keySet()) {
            System.out.println("Combination: " + key + ", value: " + _table[pointer]); // Check this line
            pointer++; // Increment pointer after printing each combination
        }
    }


}



*/





/*import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cpt {
    private String _varName;
    private String[] _outcomeNames;
    private Double[] _table;

        public Cpt(String varName, String[] outcomeNames, Double[] table,HashMap<String, Variable> variables) {
            _varName = varName;
            _outcomeNames = outcomeNames;
            _table = table;
        }
        public void createCpt(String varName, String[] outcomeNames, Double[] table, HashMap<String, Variable> variables){
            List <String> varNames = new ArrayList<>();
            for(int i = 0; i < outcomeNames.length; i++){//create list of all the variable names by the setisfy order
                varNames.add(outcomeNames[i]);
            }
            varNames.add(varName);
            List<Variable> familyVaribles = new ArrayList<>();
            for (int i=0; i<varNames.size();i++){
                familyVaribles.add(variables.get(varNames.get(i)));//create list with the wanted varible
            }
            List<Double> probababilities =new ArrayList<>();// create list of double instead of array
            for (int i = 0; i < table.length; i++) {
                probababilities.add(table[i]);
            }
                recursiveWay(familyVaribles,probababilities,varNames);
        }

    public void recursiveWay(List<Variable> variables, List<Double> doubles, List<String> names) {
        if (variables.size() == names.size()) { // check if the amount of the names use all the var name
            HashMap<String, String> combination = new HashMap<>();//creat new hashmap to the new combination
            StringBuilder keyBuilder = new StringBuilder();//
            for (int i = 0; i < names.size(); i++) {
                combination.put(variables.get(i).getName(), names.get(i));
                keyBuilder.append(variables.get(i).getName()).append("=").append(names.get(i)).append(",");
            }
            String key = keyBuilder.toString(); // Convert StringBuilder to String
            combinations.put(key, combination); // Store the combination with the key
            System.out.println("Combination: " + combination + ", value: " + doubles.get(pointer));
            pointer++;
            return;
        }

        Variable variable = variables.get(names.size());
        for (String possibleValue : variable.getPossibleValues()) {
            names.add(possibleValue);
            recursiveWay(variables, doubles, names);
            names.remove(names.size() - 1); // Remove the last value to backtrack
        }
    }




}
*/