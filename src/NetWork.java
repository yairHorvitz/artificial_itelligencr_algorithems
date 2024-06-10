/*
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
import java.util.HashMap;

public class NetWork {
    HashMap<String, Variable> variables = new HashMap<String, Variable>();
    HashMap<String, Definition> definitions = new HashMap<String, Definition>();

    private void createVaireble(String path) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
        DocumentBuilder builder = factory.newDocumentBuilder();

        try {
            Document doc = builder.parse(new File(path));
            doc.getDocumentElement().normalize();

         NodeList var = doc.getElementsByTagName("VARIABLE");//return nodeList of variables
            for (int i = 0; i < var.getLength(); i++) {
                Node varNode = var.item(i);
                if (varNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element varElement = (Element) varNode;
                    String VarName = varElement.getElementsByTagName("NAME").item(0).getTextContent();
                    NodeList outcomes = varElement.getElementsByTagName("OUTCOME");
                    String[] outcomeNames = new String[outcomes.getLength()];
                    for(int j =0; j<outcomes.getLength(); j++){

                        Node varOutcome = var.item(i);//why to built it again we built it before 8 lines????????????????
                        // Node varOutcome = outcomes.item(j); // Correct node list used here
                        if (varOutcome.getNodeType() == Node.ELEMENT_NODE) {
                            Element outcome = (Element) varOutcome;
                            String outcomeName = outcome.getTextContent();//take the String only from outcome
                            outcomeNames[j] = outcomeName;

                        }
                    }

                    Variable variable = new Variable(VarName, outcomeNames);
                    variables.put(VarName, variable);
                }

            }
            NodeList def = doc.getElementsByTagName("DEFINITION");
            for(int i=0;i<def.getLength();i++)
            {
                Node defNode = def.item(i);
                if (defNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element defElement =(Element)defNode;
                    String defForDiscover = defElement.getElementsByTagName("FOR").item(0).getTextContent();// until here we take the string from thr for
                    //if(defElement.) check the cases that this isn't this type in the def element!!!!!!!!!!!!1
                    NodeList given =defElement.getElementsByTagName("GIVEN");//return nodeList of givens
                    String [] givens = new String[given.getLength()];
                    for (int j = 0; j < given.getLength(); j++) {
                        Node defenaitionNode = given.item(j);
                        if(defenaitionNode.getNodeType() == Node.ELEMENT_NODE){
                            Element defGiven = (Element) defenaitionNode;
                            String defGivenName = defGiven.getTextContent();
                            givens[j] = defGivenName;
                        }
                    }
                    NodeList table = defElement.getElementsByTagName("TABLE");//return nodeList of tables but i need the numbers of value that in one table
                    Double [] tableValues = new Double[table.getLength()];
                    for (int j = 0; j < table.getLength(); j++) {
                        Node tableNode = table.item(j);
                        if(tableNode.getNodeType() == Node.ELEMENT_NODE){
                            Element tableElement = (Element) tableNode;
                            Double tableValue = Double.parseDouble(tableElement.getTextContent());
                            tableValues[j] = tableValue;

                        }
                    }
                    Definition definition = new Definition(defForDiscover, givens, tableValues);
                    definitions.put(defForDiscover, definition);
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
}
}



    private final List<String> names;
    private final double  value;

    int pointer = 0;
    public void recursive_way(List<Variable> variables,List<Double> doubles,List<String> names) {

        if(variables.size() == names.size()) {
            System.out.println("names: " + names + ", value: " + doubles.get(pointer));//to create new Hashmap
            pointer++;
            return;
        }

        Variable variable = variables.get(names.size());//
        for(int i = 0;i<variable.getPossibleValues().size();i++) {
            names.add(variable.getPossibleValues().get(i));
            recursive_way(variables,doubles,names);
            names.removeLast();
        }
    }

    public void iterative_method(List<Double> doubles) {
        Variable variable = new Variable();
        int[] pointers = new int[variable.getParents().size() + 1];
        List<Variable> variables = new Vector<>();
        variables.addAll(variable.getParents());
        variables.add(variable);



        for(int i1=0;i1<variables.get(0).getPossibleValues().size();i1++ ) {
            for(int i2=0;i2<variables.get(1).getPossibleValues().size();i2++ ) {
                for(int i3=0;i3<variables.get(2).getPossibleValues().size();i3++ ) {
                    ///

                    ///
                }
            }
        }

        for(int i=0;i<doubles.size();i++) {
            String[] names = getNames(variables,pointers);


            for(int j = 0;j<pointers.length;j++) {

            }
            pointers[pointers.length - 1]++;

            if(pointers[pointers.length - 1] >= variables.getLast().getPossibleValues().size()) {
                pointers[pointers.length - 2]++;
                pointers[pointers.length - 1] = 0;
            }
        }
    }

    public String[] getNames(List<Variable> variables,int[] pointers) {
        String[] values = new String[pointers.length];

        for(int i=0;i<pointers.length;i++){
            values[i] = variables.get(i).getPossibleValues().get(pointers[i]);
        }

        return values;
    }
}
*/
