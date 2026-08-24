void main() {
    Scanner scanner = new Scanner(System.in);
    String elArea = null;
    boolean runs = true;

    while (runs) {
        menuLoop(elArea);
        String val = scanner.nextLine().trim();

        switch (val.toLowerCase()) {
            case "1":
                // Fråga om elområde + data för elområde
                break;
            case "2":
                // min/max/medel pris
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

private static void menuLoop(String elArea) {
    IO.println("========================");
    IO.println("Valt elområde: " + (elArea != null ? elArea : "inget valt"));
    IO.println("2. Min, Max och Medelpris");
    IO.println("4. Bästa laddningstid (4h sammanhängande)");
    IO.println("e. Avsluta");
    IO.println("Ditt svar: ");
}
