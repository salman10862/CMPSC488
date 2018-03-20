/*import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;

import java.io.IOException;
import java.net.MalformedURLException;*/
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chris on 3/16/2018.
 */
public class retrieveGasPrices {

    public static ArrayList<String> county_Urls;
    public static String BaseUrl = "https://www.gasbuddy.com";


    public static void main(String[] args) throws Exception {
        loadUrls();
        for(String county : county_Urls) {
            scrapCounties(county);
        }

    }

    public static void loadUrls() throws Exception
    {
        String filename = "/home/pratted/urls.txt";
        Scanner sc = new Scanner(new File(filename));
        county_Urls = new ArrayList<>();

        while (sc.hasNextLine()) {
            county_Urls.add(sc.nextLine());
        }

        for(String url: county_Urls){
            System.out.println(url);
        }



    }

    public static ArrayList<String> scrapCounties(String url)
    {
        ArrayList<String> cities_Urls = new ArrayList<>();

        String htmlSource = retrieveHTMLSourceCode(url);

        Pattern pattern = Pattern.compile("<a href=\"(/GasPrices/Pennsylvania/.*)\"");
        Matcher matcher = pattern.matcher(htmlSource);

        while (matcher.find()) {
            cities_Urls.add(BaseUrl + matcher.group(1).replace(" ", "%20"));
        }
        for(String curl : cities_Urls)
        {
            System.out.println(curl);
        }
        System.out.println("Done");

        return cities_Urls;
    }

    public static String retrieveHTMLSourceCode(String url)
    {
        URL current_url;
        String htmlSource = " ";

        try {
            current_url = new URL(url);
            URLConnection conn = current_url.openConnection();
            conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            Scanner br = new Scanner(new InputStreamReader(conn.getInputStream()));


            while(br.hasNextLine()) {
                htmlSource += br.nextLine() + "\n";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlSource;
    }

}
