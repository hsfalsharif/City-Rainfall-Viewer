import java.util.Scanner;
import java.io.*;
import java.util.InputMismatchException;
public class CityDriver {
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);

        boolean cont = true;
        int choice = -1;
        int NumberOfLines = 1;
        City[] Cities;
        String[] months = {"Jan", "Feb", "March", "April", "May", "Jun", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};

        try {
            Scanner fis = new Scanner(new FileInputStream("rainfall.txt"));
            PrintWriter write = new PrintWriter(new FileOutputStream("rainfall.txt", true));


            int NumberOfMonths = ExtractRainfallInformation(fis.nextLine().split("[ ]+[\t]*")).length;  // using ExtractRainFallInformation function which return an array that gets the length of that array
            // to know how many months are written in the txt file
            while (fis.hasNextLine() && fis.nextLine().trim() != "") // count how many lines in the txt file
                NumberOfLines++;
            fis.close();
            fis = new Scanner(new FileInputStream("rainfall.txt")); // reopen the txt file again
            Cities = FileInterpreter(fis, NumberOfLines);
            fis.close();


            do {
                System.out.println("1. Display Rainfall Information for all cities.\n" +
                        "2. Display Rainfall Information for a particular city.\n" +
                        "3. Display total rainfall for each city.\n" +
                        "4. Modify a particular rainfall average for a particular city and country pair.\n" +
                        "5. Add monthly rainfall average for the current next month for all cities. \n" +
                        "6. Add New city.\n" +
                        "7. Delete a city . \n" +
                        "8. Exit.\n");
                System.out.print("Please select your choice : ");
                choice = kb.nextInt();
                if (choice > 0 && choice < 9) // all options should be inside the switch statement
                    switch (choice) {
                        case 1:
                            fis = new Scanner(new FileInputStream("rainfall.txt"));
                            DisplayRainfallForAll(fis, months, NumberOfMonths);
                            fis.close();
                            break;
                        case 2:

                            fis = new Scanner(new FileInputStream("rainfall.txt"));
                            System.out.println("Enter city name : ");
                            String City = kb.next();
                            System.out.println("Enter country name : ");
                            String Country = kb.next();
                            DisplayRainfallForCity(fis, City , Country , months, NumberOfMonths);
                            fis.close();
                            break;
                        case 3 :
                            fis = new Scanner(new FileInputStream("rainfall.txt"));
                            DisplayRainfallWithAverage(fis, months, NumberOfMonths);
                            fis.close();
                            break;
                        case 6 :
                            fis = new Scanner(new FileInputStream("rainfall.txt"));
                            Cities =addCity(Cities , NumberOfMonths);
                            for(int i = 0 ; i < Cities.length ; i++)
                                System.out.println(Cities[i].getCityName());
                            fis.close();
                            break;
                    }
                    else
                        System.out.print("Invalid choice ...");
                WaitForEnter(kb);    // wait for the user to press Enter to continue the while loop as in the documentation
            } while (cont);
        }catch (InputMismatchException e) {
                System.out.println("Error : " + e.getMessage());
                kb.nextLine();
                WaitForEnter(kb);
        }catch (IOException e){    //we must handle the IOException inside the main as stated in the documentation
                System.out.println("Error : " + e.getMessage());

        }catch (IllegalArgumentException e){
                System.out.println("Error : " + e.getMessage());
            }
    }

    // option number 1 ; we print from the txt file directly to the screen as stated in the documentation

    public static void  DisplayRainfallForAll(Scanner file,String[] months , int numberOfMonths) throws IllegalArgumentException {

        if(!file.hasNextLine())
            throw new IllegalArgumentException("file is empty");

        if(numberOfMonths == 0) //No double mean no rainfall information ;the txt file contains cities without rainfall information ;
            throw new IllegalArgumentException("There is no rainfall information in rainfall.txt");
        String Body , line[], header;
        header = StringifyHeader(numberOfMonths , months);
        System.out.println( header + "\n"); // print "City Country Jan Feb..."
        while (file.hasNextLine()) {
            line = file.nextLine().split("[ ]+[\t]*");
            Body = StringifyBody(line);
            System.out.println(Body); // print "Arusha    Tanzania  22.0" to the reset of the txt file

        }
    }
    //option 2
    public static void DisplayRainfallForCity(Scanner file,String City , String Country,String[] months , int numberOfMonths) {
        if (!file.hasNextLine())
            throw new IllegalArgumentException("file is empty");

        if (numberOfMonths == 0) //No double mean no rainfall information ;the txt file contains cities without rainfall information ;
            throw new IllegalArgumentException("There is no rainfall information in rainfall.txt");
        String Header = StringifyHeader(numberOfMonths, months);
        String Body , line[];

         // print "City Country Jan Feb..."
        boolean found = false;
        while (file.hasNextLine()) {
            line = file.nextLine().split("[ ]+[\t]*");

            if(line[0].equals(City)  && line[1].equals(Country) ){
                Body = StringifyBody(line);
                System.out.println(Header + "\n");
                System.out.println(Body); // print "Arusha    Tanzania  22.0" to the reset of the txt file
                found = true;

            }
        }
        if(!found)
            System.out.print("Invalid city and country pair");


    }

//option 3

    public static void  DisplayRainfallWithAverage(Scanner file,String[] months , int numberOfMonths) throws IllegalArgumentException {

        if(!file.hasNextLine())
            throw new IllegalArgumentException("file is empty");

        if(numberOfMonths == 0) //No double mean no rainfall information ;the txt file contains cities without rainfall information ;
            throw new IllegalArgumentException("There is no rainfall information in rainfall.txt");

        String Body , line[];
        double average = 0 , data[] ; //

        System.out.println(StringifyHeader(numberOfMonths , months,"Total (mm)") + "\n"); // print "City Country Jan Feb..."
        while (file.hasNextLine()) {
            line = file.nextLine().split("[ ]+[\t]*");
            data = ExtractRainfallInformation(line);

            for(int i = 0 ; i < data.length ; i++){
                average += data[i];
            }
            average = average / numberOfMonths;
            Body = StringifyBody(line ,average);

            System.out.println(Body); // print "Arusha    Tanzania  22.0" to the reset of the txt file

        }
    }
// option 6
    public static City[] addCity(City[] Cities, int NumberOfMonths) throws IllegalArgumentException {
        Scanner kb = new Scanner(System.in);
        System.out.println("Enter city name: ");
        String cityName = kb.nextLine();
        System.out.println("Enter country name: ");
        String countryName = kb.nextLine();
        for (int i = 0 ;i < Cities.length ; i++ ) {
            if (cityName.equals(Cities[i].getCityName()) && countryName.equals(Cities[i].getCountryName()))
                throw new IllegalArgumentException("Duplicate City and Country Pair.");
        }
        double [] rainfallAverages = new double[NumberOfMonths];
        for (int j = 0 ; j < NumberOfMonths ; j++) {
            System.out.println("Enter month#" + (j+1) + " average rainfall value [mm]: ");
            rainfallAverages[j] = kb.nextDouble();
        }
        City [] updatedCities = new City[Cities.length + 1];
        for (int i = 0 ; i < Cities.length ; i++ )
            updatedCities[i] = Cities[i];
        City newCity = new City(cityName , countryName , rainfallAverages);
        updatedCities[Cities.length] = newCity;
        System.out.println("rainfall.txt file successfully updated . . .");
        return updatedCities;
    }


    //                              Helper Functions
    // return formatted string contains "City    Country     Jan Feb...."
    private static String StringifyHeader(int numberOfMonths , String[] MonthsNames , String... args){
        String output = String.format("%-20s %-20s" , "City" , "Country");

        for(int i = 0 ; i < numberOfMonths ; i++)
            output += String.format("%-10s" ,MonthsNames[i] ); // print months in the first line : Jan Feb ....

        for (String element: args) {
            output += String.format("%-10s" ,element );
        }
        return output ;
    }

    // return formatted string contains "cityname countryname 44 22 44 5 6775 56"
    private static String StringifyBody(String[] line , double... args){


        String output = String.format("%-20s %-20s" , line[0] , line[1]);

        for(int i = 0; i < line.length - 2 ;i++){
            output += String.format("%-10s",line[2 + i]); // Skip the first two element City  &  Country

        }

        for (double element: args) {
            output += String.format("%-10.1f" ,element );
        }

        return output;
    }
    // wait for the user to press Enter ; Notice : we must clear the buffer before and after;
    private static void WaitForEnter(Scanner scn ){
        scn.nextLine();
        System.out.println("\n\nPress Enter key to continue . . .");
        scn.nextLine();
    }

    // return double array contains rainfall information [33.0, 20.0,63.0]
    private static double[] ExtractRainfallInformation(String[] line){
        int index = line.length - 2;
        double[] out = new double[ index ]; // Without the first two elements(City & Country)

        for(int i = 0; i < index ;i++){
            out[i] = Double.parseDouble(line[i + 2]);
        }

        return out;
    }

    // This function analyze the txt file to generate an array of references each of then point to a different City object
    public static City[] FileInterpreter(Scanner file,int NumberOfLines){
        City[] Cities = new City[NumberOfLines];

        for(int i = 0 ; file.hasNextLine(); i++){
            String line = file.nextLine();
            Scanner string = new Scanner(line);
            String cityName = string.next();
            String countryName = string.next();

            double[] data = ExtractRainfallInformation(line.split("[ ]+[\t]*"));

            Cities[i] = new City(cityName , countryName , data);
            string.close();
        }

        return Cities;
    }
}