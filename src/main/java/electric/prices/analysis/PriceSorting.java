package electric.prices.analysis;

import electric.prices.PriceData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PriceSorting {

    // Gör om från 15min till timmar
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
    public static List<PriceData> sortByPrice (List<PriceData> prices) {
        List<PriceData> sorted = new ArrayList<>(prices);
        sorted.sort(Comparator.comparingDouble(PriceData::getOrePerKWh));
        return sorted;
    }

    //Sortera pris lågt till högt
    public static List<PriceData> groupByHour(List<PriceData> prices) {
        int perHour = entriesPerHour(prices);
        List<PriceData> hourlyPrices = new ArrayList<>();

        for (int i = 0; i < prices.size(); i += perHour) {
            int end = Math.min(i + perHour, prices.size());
            List<PriceData> hourGroup = prices.subList(i, end);

            double avgOre = PriceStatistics.averagePrice(hourGroup);

            PriceData hourBlock = new PriceData();
            hourBlock.setSEK_per_kWh(avgOre / 100.0);
            hourBlock.setTime_start(hourGroup.getFirst().getTime_start());
            hourBlock.setTime_end(hourGroup.getLast().getTime_end());

            hourlyPrices.add(hourBlock);
        }

        return hourlyPrices;
    }

    //Bästa pris (4h sammanhängande)
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
