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
            Scanner fis = new Scanner(new FileInputStream("rainfall.txt"));
            int numberOfMonths = extractRainfallInformation(fis.nextLine().split("[ \t]+[ \t]*")).length;  // using ExtractRainFallInformation function which return an array that gets the length of that array
            // to know how many months are written in the txt file
            while (fis.hasNextLine() && fis.nextLine().trim() != "") // counts how many lines in the txt file
                numberOfLines++;
            fis.close();
            fis = new Scanner(new FileInputStream("rainfall.txt")); // reopen the txt file again
            cities = fileInterpreter(fis, numberOfLines);
            fis.close();

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
                                fis = new Scanner(new FileInputStream("rainfall.txt"));
                                DisplayRainfallForAll(fis, months, numberOfMonths);
                                fis.close();
                                break;
                            case 2:
                                fis = new Scanner(new FileInputStream("rainfall.txt"));
                                System.out.println("Enter city name : ");
                                String City = kb.next();
                                System.out.println("Enter country name : ");
                                String Country = kb.next();
                                DisplayRainfallForCity(fis, City, Country, months, numberOfMonths);
                                fis.close();
                                break;
                            case 3:
                                fis = new Scanner(new FileInputStream("rainfall.txt"));
                                DisplayRainfallWithAverage(fis, months, numberOfMonths);
                                fis.close();
                                break;
                            case 4:
                                modifyRainfallForaCity(kb,fis,numberOfMonths,numberOfLines);
                                break;
                            case 6:
                                cities = addCity(cities, numberOfMonths);
                                break;
                            case 7:
                                fis = new Scanner(new FileInputStream("rainfall.txt"));
                                removeCity(kb , fis , numberOfMonths);
                                fis.close();
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
    private static void modifyRainfallForaCity(Scanner kb,Scanner file,int numberOfMonths,int numberOfLines)throws IOException,IllegalArgumentException{
        file = new Scanner(new FileInputStream("rainfall.txt"));
        City[] cities = fileInterpreter(file, numberOfLines);//the array of the cities
        //Now closing the file and opening it again is a must because I used it in the above previous method
        file.close();
        file = new Scanner(new FileInputStream("rainfall.txt"));
        double [] monthlyRainfall = extractRainfallInformation(file.nextLine().split("[ \t]+[ \t]*"));
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
        updateFile(cities,false); //here the original file get modified
        System.out.println("\nRainfall file has been updated");
    }
    //The end of HackerMan's first method (Option 4)

    //option 5..
    private static void addRainfallForAllCities(City[] cities,Scanner file, Scanner kb ,int numOfLines, int numOfMonths) throws IOException,IllegalArgumentException {
        file = new Scanner(new FileInputStream("rainfall.txt"));
    }


    // option 6
    private static City[] addCity(City[] cities, int numberOfMonths) throws IllegalArgumentException {
        Scanner kb = new Scanner(System.in);
        System.out.println("Enter city name: ");
        String cityName = kb.nextLine();
        System.out.println("Enter country name: ");
        String countryName = kb.nextLine();
        for (int i = 0 ;i < cities.length ; i++ ) {
            if (cityName.equals(cities[i].getCityName()) && countryName.equals(cities[i].getCountryName()))
                throw new IllegalArgumentException("Duplicate City and Country Pair.");
        }
        double [] rainfallAverages = new double[numberOfMonths];
        for (int j = 0 ; j < numberOfMonths ; j++) {
            System.out.println("Enter month#" + (j+1) + " total rainfall value [mm]: ");
            rainfallAverages[j] = kb.nextDouble();
        }
        City [] updatedcities = new City[cities.length + 1];
        for (int i = 0 ; i < cities.length ; i++ )
            updatedcities[i] = cities[i];
        City newCity = new City(cityName , countryName , rainfallAverages);
        updatedcities[cities.length] = newCity;
        System.out.println("rainfall.txt file successfully updated . . .");
        return updatedcities;
    }
    // option 7
    public static void removeCity(Scanner kb,Scanner fis,int numberOfMonths)throws IllegalArgumentException,FileNotFoundException{
        PrintWriter write= new PrintWriter(new FileOutputStream("rainfall.txt"));
        System.out.println("Enter city name: ");
        String cityName=kb.nextLine();
        System.out.println("Enter country name: ");
        String countryName=kb.nextLine();
        boolean found = false;
        int count=0;
        int lineNumber=0;
        while(fis.hasNextLine()&&!fis.nextLine().trim().equals("")){
            String[]line=fis.nextLine().split("[ ]+[\t]*");
            if(line[0].equals(cityName)&&line[1].equals(countryName)){
                found=true;
                lineNumber=count;
            }
            count++;
        }
        if(!found)
            throw new IllegalArgumentException("No such city and country pair.");
        City[]updatedCities=new City[count-1];
        for(int i=0;fis.hasNextLine()&&!fis.nextLine().trim().equals("");i++){
            String[]line=fis.nextLine().split("[ ]+[\t]*");
            double[]rainfallAverages=new double[line.length-2];
            int count2=0;
            for(int j=2;j<line.length;j++) {
                rainfallAverages[count2]=Double.parseDouble(line[j]);
                count2++;
            }
            if(i!=lineNumber)
                updatedCities[i]=new City(line[0],line[1],rainfallAverages);
        }
        for(int i=0;fis.hasNextLine()&&!fis.nextLine().trim().equals("");i++){
            write.printf("%-20s %-20s",updatedCities[i].getCityName(),updatedCities[i].getCountryName());
            for(int j=0;j<numberOfMonths; j++)
                write.print(updatedCities[i].getAverageMonthlyRainfall()[j]+"   ");
        }
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
    //Better to use it in options 4,5,6,7
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
        write.close();
    }
    //it gives the index of the city in cities array if it exists .. if it DNE :) ->> it returns -1
    private static int getTheindexOrcheckAvilability(City [] cities, String cityName, String countryName){
        for(int i=0;i<cities.length;i++){
            if(cities[i].getCityName().equals(cityName)&&cities[i].getCountryName().equals(countryName))
                return i ;
        }
        return -1;
    }


}