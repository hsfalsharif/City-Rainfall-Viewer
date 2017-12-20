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
    public City(City copyCity) throws IllegalArgumentException {
        this(copyCity.cityName , copyCity.countryName , copyCity.averageMonthlyRainfall);
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
    public void modifyAverageMonthlyRainfall(int monthTobeEdited, double newAverageRainfall)throws IllegalArgumentException{
        if(newAverageRainfall<0||newAverageRainfall>1000||monthTobeEdited<1||monthTobeEdited>12)
            throw new IllegalArgumentException("Brother: you either entered an invalid month or average rainfall..figure it out yourself");
        averageMonthlyRainfall[monthTobeEdited-1] = newAverageRainfall;
    }


}
