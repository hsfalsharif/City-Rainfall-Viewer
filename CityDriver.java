import java.util.Scanner;
import java.io.*;
import java.util.InputMismatchException;
public class CityDriver {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner kb = new Scanner(System.in);
        Scanner fis = new Scanner(new FileInputStream("rainfall.txt"));
        PrintWriter write = new PrintWriter(new FileOutputStream("rainfall.txt", true));
        boolean cont = true;
        int choice = -1;
        do {
            System.out.println("1. Display Rainfall Infomation for all cities.\n" +
                    "2. Display Rainfall Infomation for a particular city.\n" +
                    "3. Display total rainfall for each city.\n" +
                    "4. Modify a particular rainfall average for a particular city and country pair.\n" +
                    "5. Add monthly rainfall average for the current next month for all cities. \n" +
                    "6. Add New city.\n" +
                    "7. Delete a city . \n" +
                    "8. Exit.\n");
            try {
                choice = kb.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error : " + e);
                kb.nextLine();
            }
            if (choice < 1 || choice > 8)
                System.out.println("please enter an integer between 1 - 8 ");
            else if( choice == 1){
                while(fis.hasNextLine()){
                    Scanner stringScanner = new Scanner(fis.nextLine());
                }
            }
        } while (cont);
    }
    public void DisplayRainFallForAll(){

    //



    }
}

