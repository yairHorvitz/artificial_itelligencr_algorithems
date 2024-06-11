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
public class NewNetWork {

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

                            Cpt cpt = new Cpt(defFor, givens, tableValues, variables);//pay attention that there isnt duplication in the definition mabey it isnt neccesery and it can be a function in definitition !!!!!!!!!!!!!!!!!!!!!!!!!!!!1
                           // variables.get(defFor).setCpt(cpt.getCombinations());
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
        for(String value : variables.keySet()) { //for each variable put children and parents
            Variable var = variables.get(value);
            Definition def = definitions.get(var.getVarName());
            def.getGivens();
            for(String given : def.getGivens()) {
                Variable varParrent = variables.get(given);
                var.addParent(varParrent);
                varParrent.addChild(var);
            }

        }
        for(String value : variables.keySet()) { // for each variable put the cpt
            Variable var = variables.get(value);
            Definition def = definitions.get(var.getVarName());
          //  var.setCpt(def.getTable());

        }




    }


}



