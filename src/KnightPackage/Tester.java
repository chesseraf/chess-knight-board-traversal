package KnightPackage;
public class Tester {
    public static void main(String[] args) {
        // test the singleAnswer class methods
        Line l = SingleAnswer.solve8x8line();
        System.out.println("Random 8x8 line:\n" + l);
        Line l2 = SingleAnswer.solve8x8endPointLine(0, 0, 7, 6);
        System.out.println("8x8 line from (0,0) to (7,6): \n" + l2);

        Line l3 = SingleAnswer.solve8x8loop();
        System.out.println("Random 8x8 loop: \n" + l3);

    }
}
