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
    public String getCityName() {
        return cityName;
    }
    public String getCountryName() {
        return countryName;
    }
    public String toString() {
        String out  = String.format("%s %s  ",this.cityName , this.countryName);
        for (double monthAverage: this.averageMonthlyRainfall) {
            out += monthAverage + " ";
        }
        return out;
    }
    public boolean equals(City that) {
        if (that == null)
            return false;
        return this.cityName.equals(that.cityName) && this.countryName.equals(that.countryName);
    }
    public double[] getAverageMonthlyRainfall() {
        return averageMonthlyRainfall.clone();
    }


}
