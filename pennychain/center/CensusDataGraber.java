package pennychain.center;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Chris on 4/9/2018.
 */
public class CensusDataGraber {

    private static int itemIndex = 0;
    private static int populationIndex = 0;
    private static String[] columnContents = new String[68];
    private static String[] populationContents = new String[68];
    private static String[] choices = {"POP100","P012001", "P012002", "P012003", "P012004" };
    private static String[] multipleColumnHeaders = new String[choices.length];
    //public static HashMap<String, Integer> sums = new HashMap<>();
    private static HashMap<String, String> county_Populations = new HashMap<>();

   //public static void main(String args[]) throws IOException {
     //   retrieveColumnHeaderFromArray();
        /*String[] example = retrievePopulationContentsFromArray();
        for(int i = 0; i < example.length; i++) {
            System.out.println(example[i]);
        }
        ArrayList<String> county_names = KeywordGenerator.getCountyNames();
        HashMap<String, String> pops = countyPopulations();

        for (int i = 0; i < pops.size(); i++)
        {
            System.out.println(pops.get(county_names.get(i)));
        }*/
       // sumColumnContents();
       // countyPopulations();

    //}

    public CensusDataGraber() {
        try {
            retrieveColumnHeaderFromArray();
            sumColumnContents();
        } catch (IOException e) {
            /*
            add error to log
             */
        }
    }

    public String getCounty_Populations(String county) {
        return county_Populations.get(county);
    }

    private HashMap<String,Integer> getHeadersFromFile() throws IOException {
        HashMap<String,Integer> headers = new HashMap<>();
        String[] data = CensusReader.getCensusData();

        //for loop adding the first line to the multideminsional array
        for(int y = 0; y < data.length; y++)
        {
            //fullData[holder][y]=cencusData[y];
            headers.put(data[y], y);
            //System.out.println(headers.get(data[y]));
            //System.out.println(data[y]);
        }
        //holder++;
        //System.out.println(example.retriveColumnHeader(fullData));

        //System.out.println(cencusData[1]);

        return headers;
    }

    private String[][] getColumnContentsFromFile(String[][] totalData,  int holder) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("pennychain\\center\\all_050_in_42.P12.csv"));
        String source;
        String[] data;

        //While/for loop adding all the lines of the file to multidimensional array
        while((source = br.readLine()) != null)
        {
            data = source.split(",");
            for(int y = 0; y < data.length; y++)
            {
                totalData[holder][y]= data[y];
                //System.out.println(totalData[holder][y]);
                //System.out.println(data[y]);
            }
            holder++;
        }
        return totalData;

    }

    private String[] retrieveColumnHeaderFromArray() throws IOException {

        //int column_index = 0;
        int content_index = 0;

        KeywordGenerator keys = new KeywordGenerator();
        ArrayList<String> all_keys = keys.grabKeywords();
        HashMap<String, Integer> position = keys.keyPostion();

        HashMap<String,Integer> headers = getHeadersFromFile();

        boolean succeeded = false;

        while(succeeded != true)
        {
            for (String choice: choices)
            {
                    columnContents[content_index] = all_keys.get(position.get(choice));
                    //multipleColumnContents[content_index][column_index] = all_keys.get(position.get(choice));
                    itemIndex = headers.get(all_keys.get(position.get(choice)));
                    //sums.put(choice, sumColumnContents(itemIndex));
                    //System.out.println(sums.get(choice) + "\n");
                    //column_index++;
            }
            succeeded = true;
        }

        return columnContents;
    }

    private String[] retrieveColumnContentsFromArray() throws IOException
    {
        int row_index = 1;
        int counter = 1;
        String[][] totalData = getColumnContentsFromFile(CensusReader.getFullData(), CensusReader.holder);

        while(row_index != totalData.length)
        {
            columnContents[counter] = totalData[row_index][itemIndex];
            counter++;
            row_index++;
        }
        return columnContents;
    }

    private String[] retrievePopulationHeaderFromArray() throws IOException {

        KeywordGenerator keys = new KeywordGenerator();
        ArrayList<String> all_keys = keys.grabKeywords();
        HashMap<String, Integer> position = keys.keyPostion();
        HashMap<String,Integer> headers = getHeadersFromFile();
        int content_index = 0;
        String population = all_keys.get(0);

        boolean succeeded = false;

        while(succeeded != true)
        {
            populationContents[content_index] = all_keys.get(position.get(population));
            populationIndex = headers.get(all_keys.get(position.get(population)));
            succeeded = true;
        }
        return populationContents;
    }

    private String[] retrievePopulationContentsFromArray() throws IOException {
        retrievePopulationHeaderFromArray();
        int row_index = 1;
        int counter = 1;
        String[][] totalData = getColumnContentsFromFile(CensusReader.getFullData(), CensusReader.holder);

        while(row_index != totalData.length)
        {
            populationContents[counter] = totalData[row_index][populationIndex];
            counter++;
            row_index++;
        }

        return populationContents;
    }


    private HashMap<String, String> countyPopulations() throws IOException
    {
        ArrayList<String> county_names = KeywordGenerator.getCountyNames();
        String[] county_pops = retrievePopulationContentsFromArray();
        int counter = 1;

        for(int i = 0; i < county_names.size(); i++)
        {
            county_Populations.put(county_names.get(i), county_pops[counter]);
            counter++;
        }

        return county_Populations;
    }

    private int sumColumnContents() throws IOException
    {
        KeywordGenerator keys = new KeywordGenerator();
        ArrayList<String> all_keys = keys.grabKeywords();
        HashMap<String, Integer> position = keys.keyPostion();
        HashMap<String, Integer> headers = getHeadersFromFile();

        int[] sums = new int[68];
        int count = 0;
        int choiceIndex = 0;
        int row_index = 1;
        int sum = 0;
        String[][] totalData = getColumnContentsFromFile(CensusReader.getFullData(), CensusReader.holder);
        for(String choice : choices)
        {
            multipleColumnHeaders[count] = all_keys.get(position.get(choice));
            choiceIndex = headers.get(choice);
            sums[count] = Integer.valueOf(totalData[row_index][choiceIndex]);
            count++;
        }

        for (int i = 0; i < multipleColumnHeaders.length; i++)
        {
            System.out.println(multipleColumnHeaders[i]);
            System.out.println(sums[i] + "\n");
        }

        for(int i = 0; i < sums.length; i++)
        {
            sum += sums[i];
        }

        //sum += Integer.valueOf(totalData[row_index][ItemIndex]);
        //row_index++;
        System.out.println(sum + "\n");

        return sum;
    }
}
