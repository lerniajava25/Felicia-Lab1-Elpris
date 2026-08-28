package electric.prices.analysis;

import electric.prices.PriceData;

import java.util.List;

public class PriceStatistics {
    //Räkna ut min-pris
    public static double minPrice (List<PriceData> prices) {
    double min = prices.getFirst().getOrePerKWh(); //Börjar med första talet i listan
        for (PriceData p : prices) {
            if (p.getOrePerKWh() < min ) {
                min = p.getOrePerKWh();
            }
        }
        return min;
    }
    //Räkna ut max-pris
    public static double maxPrice (List<PriceData> prices) {
        double max = prices.getFirst().getOrePerKWh(); //Börjar med första talet i listan
        for (PriceData p : prices) {
            if (p.getOrePerKWh() > max ) {
                max = p.getOrePerKWh();
            }
        }
        return max;
    }

    //Räkna ut medelpris = medelvärdet av talen
    public static double averagePrice (List<PriceData> prices) {
        double sum = 0;
        for (PriceData p : prices) {
            sum += p.getOrePerKWh();
        }
        return sum / prices.size();
    }
}
