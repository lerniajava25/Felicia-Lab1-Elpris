## Implementation

Programmet är en interaktiv CLI-applikation skriven i Java, byggd med Maven.
Den hämtar spotpriser på el från det öppna API:et elprisetjustnu.se och låter
användaren analysera priserna för valt elområde (SE1–SE4) och dagens datum.

Projektet är uppdelat i fem klasser, alla i paketet `electric.prices`, varav de två sista i `electric.prices.analysis`:

- **PriceData** -  Innehåller information om ett enskilt elpris.
  Det deserialiseras direkt från API:ets JSON-svar via Jackson.
- **ElPriceApiClient** - Ansvarar för nätverksanrop (via `java.net.http.HttpClient`)
  och lokal filcachning. Den kollar först om data för valt datum/område redan
  finns lagrat,  innan ett nytt API-anrop görs.
- **Main** - Menylogik och användar-input. Här används även funktionerna från de andra klasserna. Menyn körs i en loop fram till att användaren avslutar programmet.

- **PriceStatistics** -  Beräknar min-, max- och medelpris för en
satt lista av priser.
- **PriceSorting** -  Hanterar sådant som ska vara i heltimmar (istället för
  15 min som kommer från API:et). Innehåller en funktion som gör om 15-minutersposter
  till heltimmar, den används i sin tur både för sortering av priser (lägst till högst), samt en sliding window-algoritm som hittar de 4 sammanhängande timmarna med lägst totalpris.

## Reflektion

Innan har jag mest kodat i JavaScript (och HTML/CSS om det räknas). Den stora skillnaden skulle jag säga är att det jag arbetat med innan varit mycket mer förlåtande (om man ex. glömt ett tecken eller liknande). Det har gjort att jag kodat mycket mer “steg för steg”. Jag har gjort klart en mycket mindre del åt gången för att på så vis veta var koden går sönder. Den stora mängden dokumentation är ännu en skillnad. Dokumentationen gjorde det enklare att hitta lösningar på problem, men då blev det istället svårare att avgöra vilken som är den bästa/modernare lösningen.

## Källor för filcachning (VG-krav)

Jag har använt mig av officiell dokumentation från Oracle:
- https://docs.oracle.com/javase/tutorial/essential/io/fileio.html 
- https://docs.oracle.com/en/java/javase/24/docs/api/java.base/java/nio/file/Files.html 
Den skulle jag säga är väldigt tillförlitlig, eftersom det kommer
direkt från Oracle.

Jag kollade även på lösningar med atomiskt filbyte (temp-fil + rename) via
Stack Overflow, ex. 
https://stackoverflow.com/questions/25238284/how-to-guarantee-atomic-move-or-exception-of-a-file-in-java 
Det är definitift mindre tillförlitligt än Oracles dokumentation (speciellt eftersom en
del svar är ganska gamla), men gav en bra bild av hur det används i
praktiken. Jag valde sedan att köra på en enklare lösning, utan atomiskt filbyte och temp-fil.

