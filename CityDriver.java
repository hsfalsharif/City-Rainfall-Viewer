import java.util.Scanner;
import java.io.*;
import java.util.InputMismatchException;
public class CityDriver {
    public static void main(String[] args) throws IOException {
        Scanner kb = new Scanner(System.in);
        int choice = 0;
        int numberOfLines = 1; //because we used the first line in extractRainfallInformation...
        City[] cities;
        String[] months = {"Jan", "Feb", "March", "April", "May", "Jun", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};

        try {
            Scanner file = new Scanner(new FileInputStream("rainfall.txt"));
            int numberOfMonths = extractRainfallInformation(file.nextLine().split("[ \t]+[ \t]*")).length;  // using ExtractRainFallInformation function which return an array that gets the length of that array
            // to know how many months are written in the txt file
            while (file.hasNextLine() && file.nextLine().trim() != "") // counts how many lines in the txt file
                numberOfLines++;
            file.close();
            file = new Scanner(new FileInputStream("rainfall.txt")); // reopen the txt file again
            cities = fileInterpreter(file, numberOfLines);
            file.close();

            do {
                try {
                    System.out.println("1. Display Rainfall Information for all cities.\n" +
                            "2. Display Rainfall Information for a particular city.\n" +
                            "3. Display total rainfall for each city.\n" +
                            "4. Modify a particular rainfall total for a particular city and country pair.\n" +
                            "5. Add monthly rainfall total for the current next month for all cities. \n" +
                            "6. Add New city.\n" +
                            "7. Delete a city . \n" +
                            "8. Exit.\n");
                    System.out.print("Please select your choice : ");
                    choice = kb.nextInt();
                    if (choice > 0 && choice < 9) // all options should be inside the switch statement
                        switch (choice) {
                            case 1:
                                file = new Scanner(new FileInputStream("rainfall.txt"));
                                DisplayRainfallForAll(file, months, numberOfMonths);
                                file.close();
                                break;
                            case 2:
                                file = new Scanner(new FileInputStream("rainfall.txt"));
                                System.out.println("Enter city name : ");
                                String City = kb.next();
                                System.out.println("Enter country name : ");
                                String Country = kb.next();
                                DisplayRainfallForCity(file, City, Country, months, numberOfMonths);
                                file.close();
                                break;
                            case 3:
                                file = new Scanner(new FileInputStream("rainfall.txt"));
                                DisplayRainfallWithAverage(file, months, numberOfMonths);
                                file.close();
                                break;
                            case 4:
                                modifyRainfallForaCity(cities,kb,file);
                                break;
                            case 5:
                                addRainfallForAllCities(file,kb,numberOfLines);
                                break;
                            case 6:
                                addCity(cities, numberOfMonths);
                                break;
                            case 7:
                                file = new Scanner(new FileInputStream("rainfall.txt"));
                                removeCity(kb,file,numberOfLines);
                                break;
                            case 8:
                                System.exit(9);

                        }
                    else
                        System.out.print("Invalid choice ...");
                    WaitForEnter(kb);// wait for the user to press Enter to continue the while loop as in the documentation
                } catch (InputMismatchException e) {
                    System.out.println("Error : " + e);
                    WaitForEnter(kb);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error : " + e);
                    WaitForEnter(kb);
                }
            } while (choice != 8);
        } catch (IOException e) {    //we must handle the IOException inside the main as stated in the documentation
            System.out.println("Error : " + e);
        }

    }
    // option number 1 ; we print from the txt file directly to the screen as stated in the documentation
    public static void DisplayRainfallForAll(Scanner file,String[] months , int numberOfMonths) throws IllegalArgumentException {

        if(!file.hasNextLine())
            throw new IllegalArgumentException("file is empty");

        if(numberOfMonths == 0) //No double mean no rainfall information ;the txt file contains cities without rainfall information ;
            throw new IllegalArgumentException("There is no rainfall information in rainfall.txt\n");
        String Body , line[], header;
        header = StringifyHeader(numberOfMonths , months);
        System.out.println( header + "\n"); // print "City Country Jan Feb..."
        while (file.hasNextLine()) {
            line = file.nextLine().split("[ \t]+[ \t]*");
            Body = StringifyBody(line);
            System.out.println(Body); // print "Arusha    Tanzania  22.0" to the reset of the txt file

        }
    }
    //option 2
    public static void DisplayRainfallForCity(Scanner file,String City , String Country,String[] months , int numberOfMonths) {
        if (!file.hasNextLine())
            throw new IllegalArgumentException("file is empty");

        if (numberOfMonths == 0) //No double mean no rainfall information ;the txt file contains cities without rainfall information ;
            throw new IllegalArgumentException("There is no rainfall information in rainfall.txt\n");
        String Header = StringifyHeader(numberOfMonths, months);
        String Body , line[];

        // print "City Country Jan Feb..."
        boolean found = false;
        while (file.hasNextLine()) {
            line = file.nextLine().split("[ \t]+[ \t]*");

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
            throw new IllegalArgumentException("There is no rainfall information in rainfall.txt\n");

        String Body , line[];
        double total = 0 , data[] ; //

        System.out.println(StringifyHeader(numberOfMonths , months,"Total (mm)") + "\n"); // print "City Country Jan Feb..."
        while (file.hasNextLine()) {

            line = file.nextLine().split("[ \t]+[ \t]*");
            data = extractRainfallInformation(line);
            for(int i = 0 ; i < data.length ; i++){
                total += data[i];
            }
            Body = StringifyBody(line ,total);

            System.out.println(Body); // print "Arusha    Tanzania  22.0" to the reset of the txt file
            total = 0;
        }
    }
    //Option 4 .. developed by the hackerMan!
    private static void modifyRainfallForaCity(City[] cities,Scanner kb,Scanner file)throws IOException,IllegalArgumentException{
        //Now closing the file and opening it again is a must because I used it in the above previous method
        file = new Scanner(new FileInputStream("rainfall.txt"));
        double [] monthlyRainfall = extractRainfallInformation(file.nextLine().split("[ \t]+[ \t]*"));//toBeRemoved
        int numberOfMonths = monthlyRainfall.length;
        if (numberOfMonths <= 0)
            throw new IllegalArgumentException("There is no rainfall information in rainfall.txt");
        System.out.print("Enter the name of the city: ");
        String cityName = kb.next();
        System.out.print("Enter the name of the country: ");
        String countryName = kb.next();
        int cityIndex = getTheindexOrcheckAvilability(cities,cityName,countryName);
        if(cityIndex == -1)
            throw new IllegalArgumentException("Error: city and country pair is not found in text file.");

        System.out.printf("Enter the number of month [1 - %d]:", numberOfMonths);
        int chosenMonth = kb.nextInt();
        if (chosenMonth < 1 || chosenMonth > numberOfMonths)
            throw new IllegalArgumentException("Invalid month number.");
        System.out.printf("Enter the new rainfall total for month#%d:", chosenMonth);
        double newRainFallAverage = kb.nextDouble();
        if (newRainFallAverage < 0 || newRainFallAverage > 1000)
            throw new IllegalArgumentException("Invalid rainfall average");
        System.out.println("Before modification:\t" + cities[cityIndex]);
        cities[cityIndex].modifyAverageMonthlyRainfall(chosenMonth,newRainFallAverage); //here the modification occurs
        System.out.println("\nAfter  modification:\t" + cities[cityIndex]);
        updateFile(cities,false);

    }
    //The end of HackerMan's first method (Option 4)

    //option 5..
    private static void addRainfallForAllCities(Scanner file, Scanner kb,int numberOfLines) throws IOException,IllegalArgumentException {
        file = new Scanner(new FileInputStream("rainfall.txt"));
        City [] cities = new City[numberOfLines];
        int numOfMonths = 0;
        file.next();file.next(); //to skip the city name and the country name
        while(file.hasNextDouble()) {
            file.nextDouble();
            numOfMonths++;
        }
        double [] monthlyRainfall = new double[numOfMonths+1]; //1 for the month that will be added..
        if(numOfMonths>12)
            throw new IllegalArgumentException("All the months are filled with information");
        file.close();
        file = new Scanner(new FileInputStream("rainfall.txt"));
        //making the array .. I can not use fileInterpreter because the array it will give me lacks a space for the new month
        String cityName,countryName;
        for(int k=0;k<cities.length;k++){
            cityName = file.next();
            countryName = file.next();
            for(int j=0;j<monthlyRainfall.length-1;j++)
                monthlyRainfall[j] = file.nextDouble();
            cities[k] = new City(cityName,countryName,monthlyRainfall);
        }
        file.close();
        System.out.println("Enter the average rainfall values for month#"+(numOfMonths+1));
        double newRainfall;
        for(int i=0;i<cities.length;i++){
            System.out.printf("Enter average rainfall value [mm] of Month#%d for the city %s of %s:",numOfMonths+1,cities[i].getCityName(),cities[i].getCountryName());
            newRainfall = kb.nextDouble();
            System.out.println();
            if(newRainfall<0||newRainfall>1000) throw new IllegalArgumentException("Invalid rainfall average");
            cities[i].modifyAverageMonthlyRainfall(monthlyRainfall.length,newRainfall);
        }
        updateFile(cities,false);
    }
    //..the end of option 5


    // option 6
    private static void addCity(City[] cities, int numberOfMonths) throws IllegalArgumentException,IOException {
        Scanner kb = new Scanner(System.in);
        System.out.print("Enter city name:");
        String cityName = kb.next();
        System.out.print("Enter country name:");
        String countryName = kb.next();
        if(getTheindexOrcheckAvilability(cities,cityName,countryName)!=-1)
            throw new IllegalArgumentException("Duplicate City and Country Pair.");
        double [] rainfallAverages = new double[numberOfMonths];
        for (int j = 0 ; j < numberOfMonths ; j++) {
            System.out.println("Enter month#" + (j+1) + " total rainfall value [mm]: ");
            rainfallAverages[j] = kb.nextDouble();
        }
        City [] updatedCities = new City[cities.length + 1];
        for (int i = 0 ; i < cities.length ; i++ )
            updatedCities[i] = cities[i];
        City newCity = new City(cityName , countryName , rainfallAverages);
        updatedCities[cities.length] = newCity;
        updateFile(updatedCities,false);
    }
    // option 7
    public static void removeCity(Scanner kb,Scanner file,int numberOfLines)throws IllegalArgumentException,IOException{
        System.out.println("Enter city name: ");
        String cityName = kb.next();
        System.out.println("Enter country name: ");
        String countryName = kb.next();
        boolean found = false;
        City[] outCities =  fileInterpreter(file,numberOfLines);
        int count = 0;
        int lineNumber = 0;
        int count2 = 0;
        City dummyCity = new City(cityName,countryName);
        for (int i = 0 ; i < outCities.length ; i++) {
            if (outCities[i].equals(dummyCity)) {
                found = true;
                lineNumber = count;
                count = outCities.length;
            }
            count++;
        }
        City[] updatedCities = new City[outCities.length-1];
        if (!found)
            throw new IllegalArgumentException("No such city and country pair.");
        for (int i = lineNumber; i < outCities.length ; i++){
            if(i != lineNumber) {
                updatedCities[count2] = outCities[i];
                count2++;
            }
        }
        updateFile(updatedCities,true);
    }

    //
    //
    //Helper Functions
    // return formatted string contains "City    Country     Jan Feb...."
    private static String StringifyHeader(int numberOfMonths , String[] MonthsNames , String... args) {//args is for total and any additions ..in case..
        String output = String.format("%-20s %-20s" , "City" , "Country");

        for(int i = 0 ; i < numberOfMonths ; i++)
            output += String.format("%-10s" ,MonthsNames[i] ); // print months in the first line : Jan Feb ....

        for (String element: args) { //args = ['total']
            output += String.format("%-10s" ,element );
        }
        return output ;
    }

    // return formatted string contains "cityName countryName 44 22 44 5 6775 56"
    private static String StringifyBody(String[] line , double... args){

        String output = String.format("%-20s %-20s" , line[0] , line[1]);

        for(int i = 0; i < line.length - 2 ;i++){
            output += String.format("%-10s",line[2 + i]); // Skip the first two element City  &  Country

        }
        for (double element: args) { //args is again for the total
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
    private static double[] extractRainfallInformation(String[] line){
        int index = line.length - 2;
        double[] out = new double[ index ]; // Without the first two elements(City & Country)

        for(int i = 0; i < index ;i++){
            out[i] = Double.parseDouble(line[i + 2]);// "3.55" => 3.55
        }

        return out;
    }

    // This function analyze the txt file to generate an array of references each of then point to a different City object
    private static City[] fileInterpreter(Scanner file,int numberOfLines){
        City[] cities = new City [numberOfLines];
        for(int i = 0 ; file.hasNextLine(); i++){
            String line = file.nextLine();
            Scanner string = new Scanner(line); //String Scanner to consume the line into city and country and rainfall...
            String cityName = string.next();
            String countryName = string.next();
            double[] data = extractRainfallInformation(line.split("[ \t]+[ \t]*")); //to create the array of monthly rainfall
            cities[i] = new City(cityName , countryName , data);
            string.close();
        }
        file.close();
        return cities;
    }
    //Better to use it in options 4,6,7
    private static void updateFile(City [] cities , boolean append) throws IOException{
        PrintWriter write = new PrintWriter(new FileOutputStream("rainfall.txt",append)); //to append , use append = true || to reset the full file append = false
        for(City obj:cities){
            String out = "";
            out += String.format("%-20s %-20s",obj.getCityName(), obj.getCountryName());
            write.print(out);
            for(double rainfall:obj.getAverageMonthlyRainfall())
                write.printf("%-10.1f",rainfall);
            write.println();
        }
        System.out.print("\nrainfall.txt file has been updated...");
        write.close();
    }
    //it gives the index of the city in cities array if it exists .. if it DNE :) ->> it returns -1
    private static int getTheindexOrcheckAvilability(City [] cities, String cityName, String countryName){
        for(int i=0;i<cities.length;i++){
            if(cities[i].getCityName().equalsIgnoreCase(cityName)&&cities[i].getCountryName().equalsIgnoreCase(countryName))
                return i ;
        }
        return -1;
    }

}



