import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class main {
    public static void main(String[] args) {
        NewNetWork netWork = new NewNetWork();
        netWork.createNetWork("alarm_net.xml");
        String start = new String("B");
        String end = new String("E");
        Set<String> evidences = new HashSet<>();
         // evidences.add("A");
           evidences.add("J");
       boolean ans = bayesBall(netWork, start, end, evidences);
        if (ans == true) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }

    public static boolean bayesBall(NewNetWork netWork, String start, String end, Set<String> evidences) {
        boolean isIndependent = true;
        Queue<Variable> queue = new LinkedList<>();
        for (Variable var : netWork.variables.values()) {// mark all the evidence variables
            if (evidences.contains(var.getVarName())) {
                var.setEvidence();
            }
        }
        if (netWork.variables.get(start).getIsComeFromParrent() == true) {
            netWork.variables.get(start).setComeFrom();
        }
        // what happen if the Start variable is evidence??????????????????????????????????????????????
        queue.add(netWork.variables.get(start));//enter the start variable to the queue
        while (!queue.isEmpty()) {
            Variable current = queue.poll();
            if (current.getVarName().equals(end)) {//check if the current variable is the end variable
                isIndependent = false;//print no to file
                return isIndependent;
            }
            if (current.isEvidence() == true) {//if it is evidence variable
                if (current.getIsComeFromParrent() == true) {//if it comes from parent
                    if (current.getIsSendParent() == false) {//if it didn't send to parent
                        current.setSendParent();
                        current.getParents().forEach(parent -> {
                            if (parent.getIsComeFromParrent() == true) {//mark that come from child
                                parent.setComeFrom();
                            }
                            queue.add(parent);//enter the parent to the queue
                        });


                    }

                }
            } else {                             //if it isn't evidence variable
                if (current.getIsComeFromParrent() == true) {//if it comes from parent
                    if (current.getIsSendChild() == false) {//if it didn't send to parent
                        current.setSendChild();
                        current.getChildren().forEach(child -> {
                            if (child.getIsComeFromParrent() == false) {//mark that come from parent
                                child.setComeFrom();
                            }
                            queue.add(child);//enter the child to the queue
                        });


                    }
                } else {                                       //if it isnt evidence and come from child
                    if (current.getIsSendChild() == false) {//didn't send to child
                        current.setSendChild();
                        current.getChildren().forEach(child -> {
                            if (child.getIsComeFromParrent() == false) {//mark that come from parent
                                child.setComeFrom();
                            }
                            queue.add(child);//enter the child to the queue
                        });
                    }
                    if (current.getIsSendParent() == false) {//if it didn't send to parent
                        current.setSendParent();
                        current.getParents().forEach(parent -> {
                            if (parent.getIsComeFromParrent() == true) {//mark that come from child
                                parent.setComeFrom();
                            }
                            queue.add(parent);//enter the parent to the queue
                        });

                    }


                }
            }
        }
        return isIndependent;//the variable independent
    }
}

