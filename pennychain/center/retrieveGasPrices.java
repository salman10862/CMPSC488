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
        int i = 1;
        for(String county : county_Urls) {
            System.out.println("SCRAPING COUNTY" + "\n" + county);
            Double totalPrices = 0.0;
            Double avgGasPrice = 0.0;
            ArrayList<Double> allGasPrices = new ArrayList<>();
            ArrayList<String> cities = scrapCounty(county);
            for(String city: cities)
            {
                System.out.println("SCRAPING CITY" + "\n" + city);
                getGasPriceOfCity(city, allGasPrices);
            }
            for(Double price: allGasPrices)
            {
                totalPrices += price;
            }
            avgGasPrice = totalPrices/allGasPrices.size();
            System.out.println(county + " " + avgGasPrice);
            i++;
            if(i > 3)
            {
                break;
            }
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

    }

    public static ArrayList<String> scrapCounty(String url)
    {
        ArrayList<String> cities_Urls = new ArrayList<>();

        String htmlSource = retrieveHTMLSourceCode(url);

        Pattern pattern = Pattern.compile("<a href=\"(/GasPrices/Pennsylvania/.*)\"");
        Matcher matcher = pattern.matcher(htmlSource);

        while (matcher.find()) {
            cities_Urls.add(BaseUrl + matcher.group(1).replace(" ", "%20"));
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

    public static void getGasPriceOfCity(String url, ArrayList<Double> allGasPrices)
    {
        String htmlSource = retrieveHTMLSourceCode(url);
        Pattern pattern = Pattern.compile("p.a = \\[(.*)]");
        Matcher matcher = pattern.matcher(htmlSource);
        while (matcher.find()) {
            String gasPrice = matcher.group(1);
            String[] totalCountyPrices =  gasPrice.split(",");
            for(String price: totalCountyPrices)
            {
                if(price.compareTo("0") == 0){
                    continue;
                }
                price = price.replace("\"", "");
                allGasPrices.add(Double.valueOf(price));
            }
        }
    }

}
