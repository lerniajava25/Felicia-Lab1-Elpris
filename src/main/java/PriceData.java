import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceData {
    private double SEK_per_kWh;
    private String time_start;
    private String time_end;

    public PriceData() {}
    public double getSEK_per_kWh() {
        return SEK_per_kWh;
    }

    public void setSEK_per_kWh(double SEK_per_kWh) {
        this.SEK_per_kWh = SEK_per_kWh;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    //Gör om kr till öre
    public double getOrePerKWh() {
        return SEK_per_kWh * 100;
    }

    //Gör så det blir en snygg string med 2 decimaler
    @Override
    public String toString() {
        return time_start.substring(11, 16) + " - " + String.format("%.2f", getOrePerKWh()) + " öre/kWh";
    }
}
