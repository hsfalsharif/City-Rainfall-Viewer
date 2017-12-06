import java.util.Scanner;
import java.io.*;
import java.util.InputMismatchException;
public class CityDriver {
    public static void main(String[] args)  {
        Scanner kb = new Scanner(System.in);

        boolean cont = true;
        int choice = -1;
        int NumberOfLines = 1;

        String[] months = {"Jan","Feb","March","April","May","Jun","July","Aug","Sept","Oct" ,"Nov","Dec"};
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
                Scanner fis = new Scanner(new FileInputStream("rainfall.txt"));
                PrintWriter write = new PrintWriter(new FileOutputStream("rainfall.txt", true));


                int NumberOfDataEntry = ExtructRainFallInformation(fis.nextLine()).length;  // using ExctuctRainFallInformation function which return an array the get the length of that array
                                                                                            // to know how mb=any months are writtin in the txt file
                while(fis.hasNextLine() && fis.nextLine().trim() != "") // count how many lines in the txt file
                    NumberOfLines++;

                fis = new Scanner(new FileInputStream("rainfall.txt")); // reopen the txt file again

                System.out.print("Please select your choice : ");
                choice = kb.nextInt();
                if(choice > 0 && choice < 9) // all options should be inside the switch statement
                    switch(choice){
                        case 1:
                            DisplayRainfallForAll(fis,months,NumberOfDataEntry);
                            break;


                    }
                WaitForEnter(kb);    // wait for the user to press Enter to continue the while loop as in the documentation
            } catch (InputMismatchException e) {
                System.out.println("Error : " + e);
                kb.nextLine();
                WaitForEnter(kb);
            }catch (IOException e){    //we must handle the IOException inside the main as stated in the documentation
                System.out.println("Error :" + e);
            }
            catch (IllegalArgumentException e){
                System.out.println("Error :" + e);
            }


        } while (cont);
    }


    // wait for the user to press Enter ; Notice : we must clear the buffer before and after;
    private static void WaitForEnter(Scanner scn ){
        scn.nextLine();
        System.out.println("\n\nPress Enter key to continue . . .");
        scn.nextLine();
    }
    // option number 1 ; we print from the txt file directly to the screen as stated in the documentation

    public static void  DisplayRainfallForAll(Scanner file,String[] months , int numberOfMonths) throws IllegalArgumentException {

        if(!file.hasNextLine())
            throw new IllegalArgumentException("file is empty");

        if(numberOfMonths == 0) //No double mean no rainfall information ;the txt file contains cities without rainfall information ;
            throw new IllegalArgumentException("There is no rainfall information in rainfall.txt");

        System.out.println(StringfiyHeader(numberOfMonths , months)); // print "City Country Jan Feb..."
        while (file.hasNextLine()) {
            System.out.println(StringfiyBody(file.nextLine())); // print "Arusha    Tanzania  22.0" to the reset of the txt file

        }
    }
    //                              Helper Functoins
    // return formated string contains "City    Country     Jan Feb...."
    private static String StringfiyHeader(int numberOfMonths , String[] MonthsNames){
        String output = String.format("%-20s %-20s" , "City" , "Country");

        for(int i = 0 ; i < numberOfMonths ; i++)
            output += String.format("%-10s" ,MonthsNames[i] ); // print months in the first line : Jan Feb ....

        return output;
    }

    // return formated string contains "cityname countryname 44 22 44 5 6775 56"
    private static String StringfiyBody(String line){

        Scanner string = new Scanner(line);
        String City = string.next();
        String Country = string.next();
        String output = String.format("%-20s %-20s" , City , Country);



        for(int i = 0; string.hasNextDouble() ;i++){
            output += String.format("%-10.1f",string.nextDouble());

        }
        string.close();
        return output;
    }
    // return double array contains rainfall information [33.0, 20.0,63.0]
    private static double[] ExtructRainFallInformation(String line){
        int counter = 0;
        double[] tmp = new double[12];
        Scanner string = new Scanner(line);
        String dummy = string.next();
        dummy = string.next();
        while(string.hasNextDouble()){
            tmp[counter] = string.nextDouble();
            counter++;
        }
        if(counter >= 12)
            return tmp;

        double[] out = new double[counter];
        for(int i = 0 ; i < counter ; i++)
            out[i] = tmp[i];

        return out;
    }

    // This function analyze the txt file to generate an array of references each of then point to a different City object
    public static City[] FileInterpreter(Scanner file,int NumberOflines , int NumberOfDataEntry){
        City[] Citys = new City[NumberOflines];
        double[] data = new double[NumberOfDataEntry]; // Monthly rain information

        for(int i = 0 ; file.hasNextLine(); i++){
            Scanner string = new Scanner(file.nextLine());
            String cityName = string.next();
            String countryName = string.next();
            for(int j = 0 ; file.hasNextDouble(); j++){ // generate an array of rainfall information from the txt file
                data[j] = file.nextDouble();
            }

            Citys[i] = new City(cityName , countryName , data);
        }

        return Citys;
    }
}

