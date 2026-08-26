import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;

public class Main {
        static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ElClient client = new ElClient();
        List <PriceData> prices = null;

        String elArea = null; //går tillbaka till null varje gång programmet startas om
        boolean runs = true;

        while (runs) {
            menuLoop(elArea);
            String choice = scanner.nextLine().trim();

            switch (choice.toLowerCase()) {
                case "1":
                    // Fråga om elområde + data för elområde
                    elArea = chooseElArea(scanner);
                    prices = getData(client, elArea);
                    break;

                case "2":
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
                    break;
                case "4":
                    // Bästa laddningstid (4h sammanhängande)
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

    private static List<PriceData> getData(ElClient client, String area) {
        try {
            return client.getPrices(area, LocalDate.now(ZoneId.of("Europe/Stockholm")));
        } catch (Exception e) {
            IO.println("Kunde inte hämta prisdata: " + e.getMessage());
            return null;
        }
    }

    private static boolean requireDataLoad(List<PriceData> prices) {
        if (prices == null) {
            IO.println("Du måste välja elområde (alternativ 1) först.");
            return false;
        }
        return true;
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
