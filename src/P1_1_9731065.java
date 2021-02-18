import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class P1_1_9731065 {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        HashMap<String, StateDFA> states = new HashMap<>();
        ArrayList<StateDFA> endingStates = new ArrayList<>();
        StateDFA current;
        StateDFA begin;
        {
            String Key = readFile("DFA_Input_1.txt");
            String[] peaces = Key.split("\n");
            String[][] allPeaces = new String[peaces.length][];
            for (int i = 0; i < peaces.length; i++) {
                allPeaces[i] = peaces[i].split(" ");
            }
            for (String s :
                    allPeaces[1]) {
                states.put(s.trim(), new StateDFA(s.trim()));
            }
            begin = states.get(allPeaces[2][0].trim());
            for (String s :
                    allPeaces[3]) {
                endingStates.add(states.get(s.trim()));
            }
            for (int i = 4; i < allPeaces.length; i++) {
                states.get(allPeaces[i][0].trim()).addNextState(allPeaces[i][1].trim().charAt(0) , states.get(allPeaces[i][2].trim()));
            }
        }
        {
            String inputSerri = scanner.next();
            boolean flag;

            flag = true;
            current = begin;
            for (Character c :
                    inputSerri.toCharArray()) {
                current = current.popNextState(c);
                if (current == null) {
                    flag = false;
                    break;
                }
            }
            if (flag && endingStates.contains(current))
                System.out.println("ACCEPTED");
            else
                System.out.println("FAILED");
        }
    }

    private static String readFile(String filename) throws IOException {
        String content = null;
        File file = new File(filename); // For example, foo.txt
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return content;
    }
}

class StateDFA {
    private String name;
    private HashMap<Character, StateDFA> nextState;

    public StateDFA(String name) {
        this.name = name;
        nextState = new HashMap<>();
    }

    public void addNextState(char action, StateDFA state) {
        nextState.put(action, state);
    }

    public StateDFA popNextState(char action) {
        return nextState.get(action);
    }

    public HashMap<Character, StateDFA> getNextState() {
        return nextState;
    }
}