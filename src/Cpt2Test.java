import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class Cpt2Test {

//    @org.junit.jupiter.api.Test
    void joinCpt2() {
        NewNetWork net = new NewNetWork();
        net.createNetWork("alarm_net.xml");
        Cpt2 cpt1 = net.getVariable("B").getCpt2();
        System.out.println(cpt1.getCombinations());
        Cpt2 cpt2 = net.getVariable("A").getCpt2();
        System.out.println(cpt2.getCombinations());
        Cpt2 ans1 = cpt1.joinCpt2(cpt2);

        System.out.println(ans1.getCombinations());
        System.out.println(ans1.getGivenVar());
    }

//    @org.junit.jupiter.api.Test
    void removeEvidence() {
        NewNetWork net = new NewNetWork();
        net.createNetWork("alarm_net.xml");
//        Cpt2 cpt1 = net.getVariable("A").getCpt2();
//        HashMap<String, String> evidence = new HashMap<>();
//        evidence.put("B", "T");
//        Cpt2 ans1 = cpt1.removeEvidence(evidence);
//        System.out.println(cpt1.getCombinations());
//        System.out.println(ans1.getCombinations());
//        System.out.println(ans1.getGivenVar());

        Cpt2 cpt2 = net.getVariable("M").getCpt2();
        HashMap<String, String> evidence2 = new HashMap<>();
        evidence2.put("M", "T");
        Cpt2 ans2 = cpt2.removeEvidence(evidence2);
        System.out.println(cpt2.getCombinations());
        System.out.println(ans2.getCombinations());
        System.out.println(ans2.getGivenVar());
    }

    @org.junit.jupiter.api.Test
    void eliminateVar() {
//        NewNetWork net = new NewNetWork();
//        net.createNetWork("alarm_net.xml");
//        Cpt2 cpt1 = net.getVariable("A").getCpt2();
//        System.out.println(cpt1.getCombinations());
//        Cpt2 ans1 = cpt1.eliminateVar("B");
//        System.out.println(cpt1.getCombinations());
//        System.out.println(ans1.getCombinations());
//        System.out.println(ans1.getGivenVar());
    }

    @org.junit.jupiter.api.Test
    void normalize() {
        NewNetWork net = new NewNetWork();
        net.createNetWork("alarm_net.xml");
        Cpt2 cpt1 = net.getVariable("B").getCpt2();
        cpt1.normalize();
        System.out.println(cpt1.getCombinations());
    }
}