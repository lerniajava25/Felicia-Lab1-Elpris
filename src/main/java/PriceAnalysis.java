import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PriceAnalysis {
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

    //Sortera pris lågt till högt
    public static List<PriceData> sortByPrice (List<PriceData> prices) {
        List<PriceData> sorted = new ArrayList<>(prices);
        sorted.sort(Comparator.comparingDouble(PriceData::getOrePerKWh));
        return sorted;
    }


    //Räkna ut bästa 4h med "sliding window"
}
