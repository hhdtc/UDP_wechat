import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
/**
 * the clas handles translation.
 * The translation depends on Google scripts and Google translation.
 */
public class Translator {

    // public static void main(String[] args) throws IOException {
    //     String text = "Hello world!";
    //     //Translated text: Hallo Welt!
    //     System.out.println("Translated text: " + translate("en", "zh", text));
    // }

    public static String translate(String sourcelanguage, String targetlanguage, String text) throws IOException {
        // INSERT YOU URL HERE
        String urlString = "https://script.google.com/macros/s/AKfycbx7DaXylZwsXiWglBHbbIrvINDYdwLNpTT_km1SpOd3BUM8uJMN/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + targetlanguage +
                "&source=" + sourcelanguage;
        URL url = new URL(urlString);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

}