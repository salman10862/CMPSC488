package pennychain.center;

import pennychain.db.Hash;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chris on 4/8/2018.
 */
public class KeywordGenerator {


    public static void main(String args[]) throws IOException {
        ArrayList<String> keys = grabKeywords();
        ArrayList<String> counties = getCountyNames();
        keyPostion();
        /*for(int i = 0; i < keys.size(); i++)
        {
            System.out.println(keys.get(i));
        }*/
        for (int i = 0; i < counties.size(); i++)
        {
            System.out.println(counties.get(i));
        }

    }

    public static String loadFile() throws FileNotFoundException
    {
        String filename = "pennychain\\center\\SexByAge.csv";
        Scanner input = new Scanner(new File(filename));
        String source = "";

        while(input.hasNextLine())
        {
            source += input.nextLine() + "\n";
        }
        return source;
    }

    public static String loadCounties() throws FileNotFoundException
    {
        String filename = "pennychain\\center\\all_050_in_42.P12.csv";
        Scanner input = new Scanner(new File(filename));
        String source = "";

        while(input.hasNextLine())
        {
            source += input.nextLine() + "\n";
        }
        return source;
    }

    public static ArrayList<String> grabKeywords() throws IOException {

        String keySource = loadFile();
        String[] data = CensusReader.getCensusData();
        String pop_source = "";

        for(int i = 0; i < data.length; i++)
        {
            pop_source += data[i] + ",";
        }

        ArrayList<String> keywords = new ArrayList<>();


        Pattern pattern = Pattern.compile("ME,\\w+,");
        Matcher matcher = pattern.matcher(pop_source);


        Pattern pattern2 = Pattern.compile("9,P\\d+");
        Matcher matcher2 = pattern2.matcher(keySource);

        while(matcher.find())
        {
            keywords.add(matcher.group(0).replace("ME,", "").replace(",", ""));
        }

        while(matcher2.find())
        {
            keywords.add(matcher2.group(0).replace("9,", ""));
        }

        return keywords;
    }

    public static ArrayList<String> getCountyNames() throws IOException
    {

        String countySource = loadCounties();

        ArrayList<String> county_names = new ArrayList<>();


        Pattern pattern = Pattern.compile("\\w+ \\w+");
        Matcher matcher = pattern.matcher(countySource);

        while(matcher.find())
        {
            county_names.add(matcher.group(0));
        }

        return county_names;

    }

    public static HashMap<String, Integer> keyPostion() throws IOException
    {
        ArrayList<String> keys = grabKeywords();
        HashMap<String, Integer> postions = new HashMap<>();
        for(int i = 0; i < keys.size(); i++)
        {
            postions.put(keys.get(i), i);
        }

        return postions;

    }
}

