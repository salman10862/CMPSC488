package pennychain.center;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Chris on 4/9/2018.
 */
public class CensusDataGraber {

    public static int itemIndex = 0;
    public static String[] columnContents = new String[68];

    public static void main(String args[]) throws FileNotFoundException {
        KeywordGenerator keys = new KeywordGenerator();
        ArrayList<String> all_keys = keys.grabKeywords();
        System.out.println(all_keys);

    }


    public static String[] retriveColumnHeader(String[][] data) throws FileNotFoundException {
        int row_index = 0;
        int column_index = 0;
        int content_index = 0;
        int counter = 0;
        int sizeOfArray = data[0].length;

        KeywordGenerator keys = new KeywordGenerator();
        ArrayList<String> all_keys = keys.grabKeywords();

        boolean succeeded = false;

        for (int i = 0; i < sizeOfArray; i++)
        {
            while (column_index != sizeOfArray - 1)
            {
                if (data[row_index][column_index] == all_keys.get(counter))
                {
                    columnContents[content_index] = data[row_index][column_index];
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
        }

        return columnContents;
    }

    public static String[] retrieveColumnContents(String[][] data)
    {
        int row_index = 1;
        int counter = 1;

        while(row_index != data.length)
        {
            columnContents[counter] = data[row_index][itemIndex];
            counter++;
            row_index++;
        }

        return columnContents;
    }
}
