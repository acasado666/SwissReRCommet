import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.*;

public class MapPlaying {

    public static final String BOBS_CRYPTO_TXT = "src/main/resources/bobs_crypto.txt";

    public static final String BTC_2_EUR = "https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=EUR";
    public static final String ETH_2_EUR = "https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=EUR";
    public static final String XRP_2_EUR = "https://min-api.cryptocompare.com/data/price?fsym=XRP&tsyms=EUR";

    public static void main(String[] args) throws IOException {

        //1 Read file and store it into a List
        List<String> lines = readFileIntoList(BOBS_CRYPTO_TXT);
        Map<String, Double> kvs = new HashMap<>();

        //2 Creates a Map -> (CurrencyName, Value) from the lines
        createMap(lines, kvs);

        //3 looping Map over keys
        for (String currencyName : kvs.keySet()) {
            // search  for value
            Double value = kvs.get(currencyName);
            Double totalProduct;

            switch (currencyName) {
                //4 Retrives value from URL
                // and calculate Amount
                case "BTC":
                    System.out.println("---BTC---------------------------------");
                    totalProduct = getUrlAndCalculateValue(BTC_2_EUR, value);
                    String btcValueInEuros = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(totalProduct);
                    System.out.println("   Value of Bitcoins = " + btcValueInEuros + "\n");
                    break;
                case "ETH":
                    System.out.println("---ETH---------------------------------");
                    totalProduct = getUrlAndCalculateValue(ETH_2_EUR, value);
                    String ethValueInEuros = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(totalProduct);
                    System.out.println("   Value of Etherum = " + ethValueInEuros + "\n");
                    break;
                case "XRP":
                    System.out.println("---XRP---------------------------------");
                    totalProduct = getUrlAndCalculateValue(XRP_2_EUR, value);
                    String xrpValueInEuros = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(totalProduct);
                    System.out.println("   Value of Trade Ripple = " + xrpValueInEuros + "\n");
                    break;
                default:
                    System.out.println("unknown currency format");
                    break;
            }
        }
    }

    public static List<String> readFileIntoList(String fileName) throws IOException {

        Path path = Paths.get(fileName);
        System.out.println("file exists = [" + Files.exists(path) + "]");
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        return lines;
    }

    public static void createMap(List<String> lines, Map<String, Double> kvs) {
        try {
            lines.stream()
                    .map(elem -> elem.split("="))
                    .forEach(elem -> kvs.put(elem[0], Double.parseDouble(elem[1])));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
            System.out.println("File format is not correct.");
        }
    }

    public static Double getUrlAndCalculateValue(String currencyURL, Double value) throws IOException {

        String inline = "";
        Double product = 0.0;

        URL url = new URL(currencyURL);
        //Parse URL into HttpURLConnection in order to open the connection in order to get the JSON data
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //Set the request to GET or POST as per the requirements
        conn.setRequestMethod("GET");
        //Use the connect method to create the connection bridge
        conn.connect();
        //Get the response status of the Rest API
        int responsecode = conn.getResponseCode();

        //Iterating condition to if response code is not 200 then throw a runtime exception
        //else continue the actual process of getting the data
        if (responsecode != 200)
            throw new RuntimeException("HttpResponseCode: " + responsecode);
        else {
            //Response code is 200
            System.out.println("Connection success.\n");

            //Scanner functionality will read the data from the stream
            Scanner sc = new Scanner(url.openStream());

            //Only if needed in case we have more than 1 lines
            while (sc.hasNext()) {
                inline += sc.nextLine();
            }
            //search the value in the String
            String extractURLValue = inline.substring(inline.indexOf(":") + 1, inline.length() - 1);
            System.out.println("   URL Convert-Rate = " + extractURLValue + "   Number of Coins = " + value);

            product = value * Double.valueOf(extractURLValue);
            sc.close();
        }
        //Disconnect the HttpURLConnection stream
        conn.disconnect();
        return product;
    }
}
