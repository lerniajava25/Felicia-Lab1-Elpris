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
    private static int entriesPerHour(List<PriceData> prices) { //göra om från 15min intervaller till 1h
        String start = prices.getFirst().getTime_start();
        String end = prices.getFirst().getTime_end();

        int startMinutes = Integer.parseInt(start.substring(14, 16));
        int endMinutes = Integer.parseInt(end.substring(14, 16));

        int minutesPerEntry = endMinutes - startMinutes;
        if (minutesPerEntry <= 0) {
            minutesPerEntry += 60;
        }
        return 60 / minutesPerEntry;
    }

    public static List<PriceData> bestChargingWindow(List<PriceData> prices, int hours) {
        int entriesPerHour = entriesPerHour(prices);
        int windowSize = hours * entriesPerHour;

        if (prices.size() < windowSize) {
            throw new IllegalArgumentException("Fler priser krävs");
        }

        double bestSum = 0;
        for (int i = 0; i < windowSize; i++) {
            bestSum += prices.get(i).getOrePerKWh();
        }
        int bestStartIndex = 0;

        double currentSum = bestSum;
        for (int i = 1; i <= prices.size() - windowSize; i++) {
            currentSum = currentSum
                    - prices.get(i - 1).getOrePerKWh()
                    + prices.get(i + windowSize - 1).getOrePerKWh();

            if (currentSum < bestSum) {
                bestSum = currentSum;
                bestStartIndex = i;
            }
        }

        return prices.subList(bestStartIndex, bestStartIndex + windowSize);
    }

}
