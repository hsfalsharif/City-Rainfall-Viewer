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
    public String getCityName() {
        return cityName;
    }
    public String getCountryName() {
        return countryName;
    }


}
