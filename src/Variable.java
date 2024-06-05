import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Variable  {
    private String _VarName;
    private String[] _outcomeNames;
    private Set<Variable> parents;
    private Set<Variable> children;
    private List<Double> cpt; // Conditional Probability Table
    private boolean sendParent = false;
    private boolean sendChild = false;
    private  boolean evidence = false;
    private boolean comeFromParrent = false;


    public boolean getIsComeFromParrent() {
        return comeFromParrent;
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
    public Variable(String VarName, String[] outcomeNames) {
            _VarName = VarName;
            _outcomeNames = outcomeNames;
            this.parents = new HashSet<>();
            this.children = new HashSet<>();
        }
        /*
    public void setMarkComeFromParents() {
        for (Variable parent : parents) {
            if (parent.getIsComeFromParrent() == false) {
                parent.setComeFrom();
            }
        }
    }
    public void setMarkFromChild() {
        for (Variable child : children) {
            if (child.getIsComeFromParrent() == true) {
                child.setComeFrom();
            }
        }
    }*/

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
    public String getVarName() {
        return _VarName;
    }

    public void addParent(Variable parent) {
        this.parents.add(parent);
    }

    public Set<Variable> getParents() {
        return parents;
    }

    public Set<Variable> getChildren() {
        return children;
    }

    public void addChild(Variable var) {
        this.children.add(var);
    }
}



