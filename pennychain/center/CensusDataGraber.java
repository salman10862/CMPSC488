package pennychain.center;

import pennychain.db.Hash;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Chris on 4/9/2018.
 */
public class CensusDataGraber {

    public static int itemIndex = 0;
    public static String[] columnContents = new String[68];
    public static String[][] multipleColumnContents = new String[68][68];
    public static HashMap<String, Integer> sums = new HashMap<>();
    public static String[] choices = {"POP100", "P012001", "P012002", "P012003", "P012004"};

    public static void main(String args[]) throws IOException {
        retrieveColumnHeaderFromArray();
        String[] example = retrieveColumnContentsFromArray();
        /*for(int i = 0; i < example.length; i++) {
            System.out.println(example[i]);
        }*/

    }


    public static HashMap<String,Integer> getHeadersFromFile() throws IOException {
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

    public static String[][] getColumnContentsFromFile(String[][] totalData,  int holder) throws IOException {

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

    public static String[] retrieveColumnHeaderFromArray() throws IOException {

        int column_index = 0;
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
                    multipleColumnContents[content_index][column_index] = all_keys.get(position.get(choice));
                    itemIndex = headers.get(all_keys.get(position.get(choice)));
                    sums.put(choice, sumColumnContents(itemIndex));
                    System.out.println(sums.get(choice) + "\n");
                    column_index++;
            }
            succeeded = true;
        }

        /*for (int i = 0; i < sizeOfArray; i++)
        {
            while (column_index != sizeOfArray - 1)
            {
                if (totalData[row_index][column_index] == all_keys.get(counter))
                {
                    columnContents[content_index] = totalData[row_index][column_index];
                    succeeded = true;
                    break;
                }
                else
                {
                    column_index++;
                    itemIndex++;
                    sizeOfArray--;
                }
            }
            counter++;
        }*/

        return columnContents;
    }

    public static String[] retrieveColumnContentsFromArray() throws IOException {
        int row_index = 1;
        int counter = 1;
        int sum = 0;
        int count = 1;
        int column_index = 0;
        String[][] totalData = getColumnContentsFromFile(CensusReader.getFullData(), CensusReader.holder);

        while(row_index != totalData.length)
        {
            columnContents[counter] = totalData[row_index][itemIndex];
            sum += Integer.valueOf(totalData[row_index][itemIndex]);
            counter++;
            row_index++;
        }
        /*while(column_index != choices.length) {
            sums[count][column_index] = String.valueOf(sum);
            column_index++;
        }

        //System.out.println(String.valueOf(sum) + "\n");
        for(int i = 0; i < sums.length; i++) {
            System.out.println(sums[1][i]);
        }*/

        return columnContents;
    }

    public static int sumColumnContents(int ItemIndex) throws IOException
    {
        int row_index = 1;
        int sum = 0;
        String[][] totalData = getColumnContentsFromFile(CensusReader.getFullData(), CensusReader.holder);

        while(row_index != totalData.length)
        {
            sum += Integer.valueOf(totalData[row_index][ItemIndex]);
            row_index++;
        }
        //System.out.println(sum + "\n");

        return sum;
    }
}
