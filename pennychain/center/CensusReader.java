package pennychain.center;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CensusReader {

    public static int itemIndex = 0;
    public static String[] columnContents = new String[68];

    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader("pennychain\\center\\all_050_in_42.P12.csv"));
        String str;
        //Taking in the file line of the file, the column 'titles'
        str = br.readLine();
        int x = 0;
        char com = ',';
        int holder = 0;
        //System.out.println(str);
        //For loop checking for how many iterations of , there are in
        //each line of the file
        for (int y = 0; y < str.length(); y++)
        {
            if (com == str.charAt(y))
            {
                x++;
            }
        }

        String[] censusData = new String[x];
        censusData = str.split(",");
        String[][] fullData = new String[68][x];

        //HashMap<String,Integer> headers = getHeadersFromFile(censusData, x);
        //holder++;
        getColumnContentsFromFile(censusData, fullData, str, br, x, holder);
        retrieveColumnHeaderFromArray(fullData, censusData, x);
        String[] example = retrieveColumnContentsFromArray(fullData, censusData);

        for(int i = 0; i < example.length; i++)
        {

            System.out.println(example[i]);
        }


        //for loop adding the first line to the multideminsional array
        /*for (int y = 0; y < x; y++) {
            fullData[holder][y] = cencusData[y];
        }
        holder++;
        //System.out.println(cencusData[1]);
        //While/for loop adding all the lines of the file to multidimensional array
        while ((str = br.readLine()) != null) {

            cencusData = str.split(",");
            for (int y = 0; y < x; y++) {
                fullData[holder][y] = cencusData[y];
                System.out.println(fullData[holder][9]);
            }
            holder++;

        }*/
        //System.out.println(cenusData.toArray());
    }
        public static HashMap<String,Integer> getHeadersFromFile(String[] data, int boundary)
        {
            HashMap<String,Integer> headers = new HashMap<>();

            //for loop adding the first line to the multideminsional array
            for(int y = 0; y < boundary; y++)
            {
                //fullData[holder][y]=cencusData[y];
                headers.put(data[y], y);
                //System.out.println(headers.get(data[y]));
            }
            //holder++;
            //System.out.println(example.retriveColumnHeader(fullData));

            //System.out.println(cencusData[1]);

            return headers;
        }

    public static String[][] getColumnContentsFromFile(String[] data, String[][] totalData, String source, BufferedReader br, int boundary, int holder) throws IOException {


        //While/for loop adding all the lines of the file to multidimensional array
        while((source = br.readLine()) != null)
        {
            data = source.split(",");
            for(int y = 0; y < boundary; y++)
            {
                totalData[holder][y]= data[y];
                //System.out.println(totalData[holder][y]);
            }
            holder++;

        }
        return totalData;

    }

    public static String[] retrieveColumnHeaderFromArray(String[][] totalData, String[] data, int boundary) throws FileNotFoundException {

        int column_index = 0;
        int content_index = 0;
        int counter = 0;

        KeywordGenerator keys = new KeywordGenerator();
        ArrayList<String> all_keys = keys.grabKeywords();

        HashMap<String,Integer> headers = getHeadersFromFile(data, boundary);

        boolean succeeded = false;

        while(succeeded != true)
        {
            if (headers.containsKey(all_keys.get(counter)))
            {
                columnContents[content_index] = all_keys.get(counter);
                itemIndex = headers.get(all_keys.get(counter));
                succeeded = true;
            }
            else
            {
                column_index++;
            }
            counter++;
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

    public static String[] retrieveColumnContentsFromArray(String[][] totalData, String[] data)
    {
        int row_index = 1;
        int counter = 1;

        while(row_index != totalData.length)
        {
            columnContents[counter] = totalData[row_index][itemIndex];
            counter++;
            row_index++;
        }

        return columnContents;
    }

}
