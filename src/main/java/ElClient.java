import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class ElClient {

    private static final String CACHE_MAPP = "cache";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ElClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<PriceData> getPrices(String elArea, LocalDate date) throws IOException, InterruptedException {
        Path cachePath = buildCachePath(elArea, date);

        // 1. Börjar kolla cache
        if (Files.exists(cachePath)) {
            IO.println("(Läser från lokal cache: " + cachePath + ")");
            String json = Files.readString(cachePath);
            return parseJson(json);
        }

        // 2. Annars, hämta från API
        String url = buildUrl(elArea, date);
        IO.println("(Hämtar från API: " + url + ")");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("API statuskod " + response.statusCode()
                    + ". Kontrollera att datum/elområde är giltigt.");
        }

        String json = response.body();

        // 3. Spara till cache innan vi returnerar
        saveToCache(cachePath, json);

        return parseJson(json);
    }

    private String buildUrl(String elArea, LocalDate datum) {
        String year = String.valueOf(datum.getYear());
        String month = String.format("%02d", datum.getMonthValue());
        String day = String.format("%02d", datum.getDayOfMonth());
        return String.format("https://www.elprisetjustnu.se/api/v1/prices/%s/%s-%s_%s.json",
                year, month, day, elArea);
    }

    private Path buildCachePath(String elomrade, LocalDate datum) throws IOException {
        Path folder = Paths.get(CACHE_MAPP);
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }
        String fileName = String.format("%s_%s.json", datum, elomrade);
        return folder.resolve(fileName);
    }

    private void saveToCache(Path path, String json) {
        try {
            Files.writeString(path, json);
        } catch (IOException e) {

            IO.println("Varning: kunde inte spara cache (" + e.getMessage() + ")");
        }
    }

    private List<PriceData> parseJson(String json) throws IOException {
        return objectMapper.readValue(json, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, PriceData.class));
    }
}