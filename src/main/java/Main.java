import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;

public class Main {
        static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ElClient client = new ElClient();
        List <PriceData> prices = null;
        String elArea = loadLastElArea();

        if (elArea != null) {
            prices = getData(client, elArea);
        }

        boolean runs = true;

        while (runs) {
            menuLoop(elArea);
            String choice = scanner.nextLine().trim();

            switch (choice.toLowerCase()) {
                case "1":
                    // Fråga om elområde + data för elområde
                    elArea = chooseElArea(scanner);

                    if (elArea != null) {
                        prices = getData(client, elArea);
                        saveLastElArea(elArea);
                    }

                    break;

                case "2":
                    // min, max och medelpris
                    if (requireDataLoad(prices)) {
                        double min = PriceAnalysis.minPrice(prices);
                        double max = PriceAnalysis.maxPrice(prices);
                        double avg = PriceAnalysis.averagePrice(prices);

                        IO.println("Lägsta pris:  " + String.format("%.2f", min) + " öre/kWh");
                        IO.println("Högsta pris:  " + String.format("%.2f", max) + " öre/kWh");
                        IO.println("Medelpris:    " + String.format("%.2f", avg) + " öre/kWh");
                    }

                    break;
                case "3":
                    // Sortera priser (lågt till högt)
                    if (requireDataLoad(prices)) {
                        List<PriceData> sorted = PriceAnalysis.sortByPrice(prices);
                        for (PriceData p : sorted) {
                            IO.println(p.toString());
                        }
                    }
                    break;
                case "4":
                    // Bästa laddningstid (4h sammanhängande)
                    if (requireDataLoad(prices)) {
                       try { List<PriceData> bestPrice = BestChargePrice.bestChargingWindow(prices, 4);

                        double avgWindow = PriceAnalysis.averagePrice(bestPrice);

                        String startTime = bestPrice.getFirst().startTime();
                        String endTime = bestPrice.get(bestPrice.size() - 1).endTime();

                        IO.println("Bästa laddningstid (4h): " + startTime + "-" + endTime + " - " +
                                " Medelpris: " +  String.format("%.2f", avgWindow) + " öre/kWh");
                    } catch (IllegalArgumentException e) {
                           IO.println("Kunde inte beräkna bästa laddningstid: för lite prisdata tillgänglig ("
                                   + e.getMessage() + ")");
                       }
                    }
                    break;
                case "e":
                    runs = false;
                    break;
                default:
                    IO.println("Ogiltigt val, försök igen.");
            }
        }
        scanner.close();
    }

    private static String loadLastElArea() {
        Path path = Paths.get("last_area.txt");

        try {
            if (Files.exists(path)) {
                return Files.readString(path).trim();
            }
        } catch (IOException e) {
            IO.println("Kunde inte läsa senast valda elområde: " + e.getMessage());
        }

        return null;
    }

    private static void saveLastElArea(String elArea) {
        Path path = Paths.get("last_area.txt");

        try {
            Files.writeString(path, elArea);
        } catch (IOException e) {
            IO.println("Kunde inte spara elområde: " + e.getMessage());
        }
    }

    private static String chooseElArea (Scanner scanner){
        String[] validArea = {"SE1", "SE2", "SE3", "SE4"};
        while (true) {
            IO.println("Vilket elområde? (SE1/SE2/SE3/SE4): ");
            String choice = scanner.nextLine().trim().toUpperCase();
            for (String g : validArea) {
                if (g.equals(choice)) return choice;
            }
            IO.println("Ogiltigt elområde, försök igen.");
        }
    }

    private static boolean requireDataLoad(List<PriceData> prices) {
        if (prices == null || prices.isEmpty()) {
            IO.println("Du måste välja elområde (alternativ 1) först.");
            return false;
        }
        return true;
    }

    private static List<PriceData> getData(ElClient client, String area) {
        try {
            return client.getPrices(
                    area,
                    LocalDate.now(ZoneId.of("Europe/Stockholm"))
            );
        } catch (Exception e) {
            IO.println("Kunde inte hämta prisdata: " + e.getMessage());
            return null;
        }
    }

    private static void menuLoop(String elArea) {
        IO.println("\nElpriser – Analysverktyg");
        IO.println("========================");
        IO.println("Valt elområde: " + (elArea != null ? elArea : "inget valt"));
        IO.println("1. Välj elområde (SE1, SE2, SE3, SE4)");
        IO.println("2. Min, Max och Medelpris");
        IO.println("3. Sortera priser (lägst till högst)");
        IO.println("4. Bästa laddningstid (4h sammanhängande)");
        IO.println("Ditt svar: ");
    }
}
