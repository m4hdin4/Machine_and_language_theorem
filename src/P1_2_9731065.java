import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class P1_2_9731065 {
    public static void main(String[] args) {
        HashMap<String, StateNFA> states = new HashMap<>();
        ArrayList<StateNFA> endingStates = new ArrayList<>();
        HashMap<StateNFA, HashSet<StateNFA>> landa = new HashMap<>();
        StateNFA begin;
        String Key = null;
        try {
            Key = readFile("NFA_Input_2.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] peaces = Key.split("\n");
        String[][] allPeaces = new String[peaces.length][];
        for (int i = 0; i < peaces.length; i++) {
            allPeaces[i] = peaces[i].split(" ");
        }
        char[] actions = new char[allPeaces[0].length];
        for (int i = 0; i < allPeaces[0].length; i++) {
            actions[i] = allPeaces[0][i].charAt(0);
        }
        for (String s :
                allPeaces[1]) {
            states.put(s.trim(), new StateNFA(s.trim(), actions));
            landa.put(states.get(s.trim()), new HashSet<>());
        }
        begin = states.get(allPeaces[2][0].trim());
        for (String s :
                allPeaces[3]) {
            endingStates.add(states.get(s.trim()));
        }
        for (int i = 4; i < allPeaces.length; i++) {
            if (allPeaces[i][1].trim().charAt(0) == 'λ')
                landa.get(states.get(allPeaces[i][0].trim())).add(states.get(allPeaces[i][2].trim()));
            else
                states.get(allPeaces[i][0].trim()).addNextStateNFA(allPeaces[i][1].trim().charAt(0), states.get(allPeaces[i][2].trim()));
        }

        //fix missing landas
        boolean check = true;
        while (check) {
            check = false;
            HashMap<StateNFA, HashSet<StateNFA>> tempLanda = new HashMap<>(landa);
            for (StateNFA st :
                    tempLanda.keySet()) {
                for (StateNFA newSt :
                        tempLanda.get(st)) {
                    for (StateNFA newSt2 :
                            tempLanda.get(newSt)) {
                        if (!landa.get(st).contains(newSt2)) {
                            landa.get(st).add(newSt2);
                            check = true;
                        }
                    }
                }
            }
        }
        //start making DFA


        ArrayList<HashSet<StateNFA>> newStates = new ArrayList<>();
        HashSet<HashSet<StateNFA>> newEndingStates = new HashSet<>();
        HashMap<HashSet<StateNFA>, HashMap<Character, HashSet<StateNFA>>> connecting = new HashMap<>();
        HashSet<StateNFA> newBegin = new HashSet<>();
        newBegin.add(begin);
        HashSet<StateNFA> temp;
        HashSet<StateNFA> output;
        HashMap<Character, HashSet<StateNFA>> tempHashMap = new HashMap<>();
        newStates.add(newBegin);
        for (int i = 0; i < newStates.size(); i++) {
            temp = newStates.get(i);
            tempHashMap = new HashMap<>();
            for (char c :
                    actions) {
                output = new HashSet<>();
                for (StateNFA state :
                        temp) {
                    output.addAll(state.popNextStateNFA(c));
                    if (!landa.get(state).isEmpty())
                        for (StateNFA s :
                                landa.get(state)) {
                            output.addAll(s.popNextStateNFA(c));
                        }
                }
                tempHashMap.put(c, output);
                if (newStates.contains(output))
                    continue;
                if (!output.isEmpty())
                    newStates.add(output);
                for (StateNFA s :
                        endingStates) {
                    if (output.contains(s)) {
                        newEndingStates.add(output);
                        break;
                    }
                }
            }
            connecting.put(temp, tempHashMap);
        }

        //calculating the output

        String result = "";
        for (char c :
                actions) {
            result += c + " ";
        }
        result += '\n';
        for (HashSet<StateNFA> state :
                newStates) {
            result += state + " ";
        }
        result += '\n';
        result += newBegin;
        result += '\n';
        for (HashSet<StateNFA> state :
                newEndingStates) {
            result += state + " ";
        }
        result += '\n';
        for (HashSet<StateNFA> state :
                connecting.keySet()) {
            for (char action :
                    connecting.get(state).keySet()) {
                if (!connecting.get(state).get(action).isEmpty())
                    result += state + " " + action + " " + connecting.get(state).get(action) + '\n';
            }
        }
//        System.out.println(result);
        try {
            PrintWriter out = new PrintWriter("DFA_Output _2.txt");
            out.print(result);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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


class StateNFA {
    private String name;
    private HashMap<Character, HashSet<StateNFA>> nextStateNFA;

    public StateNFA(String name, char[] actions) {
        this.name = name;
        nextStateNFA = new HashMap<>();
        for (char action :
                actions) {
            nextStateNFA.put(action, new HashSet<>());
        }
    }

    public void addNextStateNFA(char action, StateNFA state) {
        nextStateNFA.get(action).add(state);
    }

    public HashSet<StateNFA> popNextStateNFA(char action) {
        return nextStateNFA.get(action);
    }

    @Override
    public String toString() {
        return name;
    }
}

