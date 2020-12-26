package nl.hodc.rdwsync.tools;

import java.util.ArrayList;
import java.util.List;

public class ListCreator {
    public static List<String> getList(String uitsluiten) {
        List<String> lijstwaarde = new ArrayList<String>();

        if (!uitsluiten.contentEquals("Y")) {
            // lijstwaarde.add("A"); //Koninklijk Huis
            lijstwaarde.add("C"); // Corps Diplomatiek
            lijstwaarde.add("M"); // Motorfietsen
            lijstwaarde.add("W"); // Caravan en Aanhangwagen
            lijstwaarde.add("O"); // Grote aanhangwagens
        }

        if (!uitsluiten.contentEquals("Y")) {
            lijstwaarde.add("B");

            lijstwaarde.add("D");
            lijstwaarde.add("E");
            lijstwaarde.add("F");
            lijstwaarde.add("G");
            lijstwaarde.add("H");

            lijstwaarde.add("J");
            lijstwaarde.add("K");
            lijstwaarde.add("L");
            lijstwaarde.add("N");

            lijstwaarde.add("P");

            lijstwaarde.add("R");
            lijstwaarde.add("S");
            lijstwaarde.add("T");
            lijstwaarde.add("U");
            lijstwaarde.add("V");

            lijstwaarde.add("X");
            lijstwaarde.add("Y");
            lijstwaarde.add("Z");

            lijstwaarde.add("0");
            lijstwaarde.add("1");
            lijstwaarde.add("2");
            lijstwaarde.add("3");
            lijstwaarde.add("4");
            lijstwaarde.add("5");
            lijstwaarde.add("6");
            lijstwaarde.add("7");
            lijstwaarde.add("8");
            lijstwaarde.add("9");
        }
        return lijstwaarde;
    }
}
