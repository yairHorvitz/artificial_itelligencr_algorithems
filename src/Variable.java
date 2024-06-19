import java.util.*;

public class Variable  {
    private String _VarName;
    private String[] _outcomeNames;
    private List<Variable> parents;
    private List<Variable> children;
    private Cpt2 cpt;
    private boolean sendParent = false;
    private boolean sendChild = false;
    private  boolean evidence = false;
    private boolean isAnsestor = false;
    private boolean comeFromParrent = false;
    private Double [] probabilityValues;



    public Variable(String VarName, String[] outcomeNames) {
            _VarName = VarName;
            _outcomeNames = outcomeNames;
            this.parents = new ArrayList<>();
            this.children = new ArrayList<>();
        }

    public boolean getIsAncestor() {
        return isAnsestor;
    }


    public void setIsAncestor() {
        isAnsestor = true;
    }
    public void restartIsAncestor() {
        isAnsestor = false;
    }

    public Double[] getProbabilityValues() {
        return probabilityValues;
    }

    public void setProbabilityValues(Double[] probabilityValues) {
        this.probabilityValues = probabilityValues;
    }

    public boolean getIsComeFromParrent() {
        return comeFromParrent;
    }
    public void setSendChildAtStart() {
        this.sendChild = false;
    }

    public void setSendParentAtStart(){
        this.sendParent = false;
    }
    public void setSendChild() {
        this.sendChild = true;
    }

    public void setSendParent(){
        this.sendParent = true;
    }
    public void setComeFrom(){
        if(this.getIsComeFromParrent()==false){
            this.comeFromParrent = true;}
        else {
            this.comeFromParrent = false;
        }
    }



    public Cpt2 getCpt2() {
        return cpt;
    }

    public void setCpt(Cpt2 cpt) {
        this.cpt = cpt;
    }

    public String[] get_outcomeNames() {
        return _outcomeNames;
    }
    public int getOutcomeLength(){
        return _outcomeNames.length;
    }

    public boolean  getIsSendParent() {
        return sendParent;
    }

    public boolean getIsSendChild() {
        return sendChild;
    }

    public boolean isEvidence() {
        return evidence;
    }
    public void setEvidence(){
        this.evidence = true;
    }
    public void setRestartEvidence(){
        this.evidence = false;
    }
    public String getVarName() {
        return _VarName;
    }



    public void addParent(Variable parent) {
        this.parents.add(parent);
    }

    public List<Variable> getParents() {
        return parents;
    }

    public List<Variable> getChildren() {
        return children;
    }

    public void addChild(Variable var) {
        this.children.add(var);
    }

    public String getName() {
        return _VarName;
    }

    @Override
    public String toString() {
        return _VarName;
    }

}


