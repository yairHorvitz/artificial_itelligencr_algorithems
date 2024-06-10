import java.util.Comparator;
public class ComparatorSortByFactorAndAscii implements Comparator<Variable> {
    @Override
    public int compare(Variable var1, Variable var2) {
        // Compare by factory size
        int compareByFactorySize = Integer.compare(var1.getFactorySize(), var2.getFactorySize());
        if (compareByFactorySize != 0) {
            return compareByFactorySize;
        }

        // If factory sizes are equal, compare by ASCII values of variable names
        return var1.getVarName().compareTo(var2.getVarName());
    }
}