package pennychain.center;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chris on 4/8/2018.
 */
public class KeywordGenerator {


    public static void main(String args[]) throws FileNotFoundException {
        ArrayList<String> keys = grabKeywords();
        for(int i = 0; i < keys.size(); i++)
        {
            System.out.println(keys.get(i));
        }
    }

    public static String loadFile() throws FileNotFoundException {
        String filename = "pennychain\\center\\SexByAge.csv";
        Scanner input = new Scanner(new File(filename));
        String source = "";

        while(input.hasNextLine())
        {
            source += input.nextLine() + "\n";
        }
        return source;
    }

    public static ArrayList<String> grabKeywords() throws FileNotFoundException {

        String keySource = loadFile();

        ArrayList<String> keywords = new ArrayList<>();


        Pattern pattern = Pattern.compile("9,P\\d+");
        Matcher matcher = pattern.matcher(keySource);

        while(matcher.find())
        {
            keywords.add(matcher.group(0).replace("9,", ""));
        }

        return keywords;
    }
}

