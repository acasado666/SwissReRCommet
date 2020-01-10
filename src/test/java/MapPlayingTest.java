import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MapPlayingTest {

    public static final String MOCK_BOBS_CRYPTO = "src\\test\\resources\\bobs_crypto.txt";
    public static final String MOCK_BOBS_CORRUPTED = "src\\test\\resources\\bobs_crypto_corrupted.txt";
    public static final String MOCK_BOBS_CRYPTO_FAKE = "bobs_crypto.txt";
    public static final String BTC_2_EUR = "https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=EUR";
    public static final String BTC_2_EUR_FAKE = "https:/fake.com/price?fsym=BTC&tsyms=EUR";
    public static final List<String> FORMATTED_LINES = Arrays.asList(new String[]{"first=10", "second=5"});
    public static final List<String> UN_FORMATTED_LINES = Arrays.asList(new String[]{"first->10", "second:5"});


    @Test
    public void test_createMap_OK() {
        Map<String, Double> kvs = new HashMap<>();
        MapPlaying.createMap(FORMATTED_LINES, kvs);
    }

    @Test
    public void test_createMap_ERROR() {
        Map<String, Double> kvs = new HashMap<>();
        MapPlaying.createMap(UN_FORMATTED_LINES, kvs);
    }

    @Test
    public void test_readFileIntoList_OK() throws IOException {

        List<String> lines = MapPlaying.readFileIntoList(MOCK_BOBS_CRYPTO);
        assertEquals(lines.size(), 3);
    }

    @Test
    public void test_readFileIntoList_nofile_ERROR() {

        assertThrows(NoSuchFileException.class, () -> {
            MapPlaying.readFileIntoList(MOCK_BOBS_CRYPTO_FAKE);
        });
    }

    @Test
    public void test_getUrlValue_OK() throws IOException {

        Double totalProduct = MapPlaying.getUrlAndCalculateValue(BTC_2_EUR, 10.0);
        assertNotNull(totalProduct);
    }

    @Test
    public void test_getUrlValue_wrongUrl_ERROR() {

        assertThrows(IllegalArgumentException.class, () -> {
            String BTC_2_EUR = BTC_2_EUR_FAKE;
            MapPlaying.getUrlAndCalculateValue(BTC_2_EUR, 10.0);
        });
    }
}
