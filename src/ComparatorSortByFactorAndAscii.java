import java.util.Comparator;
import java.util.stream.Collectors;

public class ComparatorSortByFactorAndAscii implements Comparator<Cpt2> {

    @Override
    public int compare(Cpt2 factor1, Cpt2 factor2) {
        // Compare by factory size
        int compareByFactorySize = Integer.compare(factor1.getCombinations().size(), factor2.getCombinations().size());
        if (compareByFactorySize != 0) {
            return compareByFactorySize;
        } else {
            // If factory sizes are equal, compare by ASCII values of variable names
            factor1.getGivenVar().sort(Comparator.comparing(Variable::getVarName));
            String givenVarName1 = factor1.getGivenVar().stream().map(Variable::getVarName).collect(Collectors.joining());

            factor2.getGivenVar().sort(Comparator.comparing(Variable::getVarName));
            String givenVarName2 = factor2.getGivenVar().stream().map(Variable::getVarName).collect(Collectors.joining());

            // Compare the concatenated varName strings
            return givenVarName1.compareTo(givenVarName2);
        }
    }
}