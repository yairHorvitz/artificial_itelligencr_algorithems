public class Definition {
    private String _defFor;
    private String[] _givens;
    private Double[] _table;

    public Definition(String defFor, String[] givens, Double[] table) {
        _defFor = defFor;
        _givens = givens;
        _table = table;
    }

    public String[] getGivens() {
        return _givens;
    }

    public Double[] getTable() {return _table; }

}
