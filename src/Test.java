import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        ArrayList<String> a = new ArrayList<>();
        a.add("a");
        a.add("b");
        a.add("c");
        a.add("d");
        ArrayList<String> a2 = new ArrayList<>(a);
        a2.add("e");
        System.out.println(a);
        System.out.println(a2);
    }
}
