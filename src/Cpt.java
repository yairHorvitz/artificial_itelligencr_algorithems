import java.util.ArrayList;
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




        }





}
