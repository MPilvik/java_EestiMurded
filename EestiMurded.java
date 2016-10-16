import java.util.Scanner;

public class EestiMurded {

    public static void main(String args[]) {
        System.out.println("See on Eesti murdeid tutvustav projekt");

        Scanner input = new Scanner(System.in);
        System.out.println("Kas soovid kõigepealt näha murrete kaarti? (jah/ei)");
        String vastus = input.next().toLowerCase();

        if (vastus.equals("jah")) {
            System.out.println("Varsti saab!");
        } else if (vastus.equals("ei")) {
            System.out.println("Hästi!");
        }
    }
}
