import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Ex1 {
    public static void main(String[] args) {


        //create output file
        try {
            // Define file paths
            Path inputFile = Paths.get("input.txt");
            Path outputFile = Paths.get("output.txt");

            // Create the output file
            createOutputFile(outputFile);

            // Read the first line from the input file
            List<String> lines = Files.readAllLines(inputFile);
            if (lines.isEmpty()) {
                System.out.println("The input file is empty.");
                return;
            }
            String firstLine = lines.get(0);
            System.out.println("First line: " + firstLine);
            NewNetWork netWork = new NewNetWork();
            netWork.createNetWork(firstLine);
            // Read the rest of the lines and process them
            String inputForAnswer = new String();
            for (int i = 1; i < lines.size(); i++) {
                inputForAnswer = lines.get(i);
                String answer = netWork.answer(inputForAnswer.toString().trim());
                writeToFile(outputFile, answer);
              //  inputForAnswer.append(lines.get(i)).append(System.lineSeparator());
            }
            // Write the answer to the output file

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        // Method to append content to a file
        public static void writeToFile(Path file, String content) throws IOException {
            // Add a newline character before the content
            String contentWithNewLine = content + System.lineSeparator();
            Files.write(file, contentWithNewLine.getBytes(), StandardOpenOption.APPEND);
            System.out.println("Content appended to file: " + file.toString());
        }



        //read the rest lines from the file and send to answer

        //write the answer to the file


    // Method to create a new file
    public static void createOutputFile(Path file) throws IOException {
        // Delete the file if it already exists
        if (Files.exists(file)) {
            Files.delete(file);
            System.out.println("Existing file deleted: " + file.toString());
        }

        // Create the file
        Files.createFile(file);
    //    System.out.println("File created: " + file.toString());
    }


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

         //   System.out.println("Combination: {" + combinationString.toString() + "} Probability: " + probability);
        }
    }
}

