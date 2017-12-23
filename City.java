/*
*
*               ############# FINAL ################
*
*
* */

public class City {
    private String cityName;
    private String countryName;
    private double[] averageMonthlyRainfall;



    public City(String cityName , String countryName , double[] averageMonthlyRainfall) {
        this.cityName = cityName;
        this.countryName = countryName;
        this.averageMonthlyRainfall = new double [averageMonthlyRainfall.length] ;
        this.averageMonthlyRainfall = averageMonthlyRainfall.clone();
    }

        //Get methods

    public String getCityName() {
        return cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public double[] getAverageMonthlyRainfall() {
        return averageMonthlyRainfall.clone();
    }


    public void addMonthlyAverageRainfall(double   rainfall) throws IllegalArgumentException{
        if(rainfall < 0 || rainfall>1000 )
            throw new IllegalArgumentException("Brother: you either entered an invalid month or average rainfall..figure it out yourself");

        if(this.averageMonthlyRainfall.length >= 12 )
            throw new IllegalArgumentException("All the months are filled with information");

        double[] newData = new double[this.averageMonthlyRainfall.length + 1];

        for(int i = 0 ; i < this.averageMonthlyRainfall.length ; i++ )
            newData[i] = this.averageMonthlyRainfall[i];

        newData[this.averageMonthlyRainfall.length] = rainfall;
        this.averageMonthlyRainfall = newData;
    }



    public void modifyAverageMonthlyRainfall(double rainfall ,int monthNum )throws IllegalArgumentException{

        if(rainfall<0||rainfall>1000 )
            throw new IllegalArgumentException("Brother: you either entered an invalid month or average rainfall..figure it out yourself");

        averageMonthlyRainfall[monthNum-1] = rainfall;
    }





    public String toString() {
        String out  = String.format("[City name: %-10s , Country name: %-10s , Average monthly rainfall =[",this.cityName , this.countryName);
        for(int i=0;i<this.averageMonthlyRainfall.length-1;i++){
            out+= averageMonthlyRainfall[i] + ", ";
        }
        out += averageMonthlyRainfall[averageMonthlyRainfall.length-1] + "] ]";
        return out;
    }
    public boolean equals(City that) {
        if (that == null)
            return false;
        return this.cityName.equals(that.cityName) && this.countryName.equals(that.countryName);
    }
}
