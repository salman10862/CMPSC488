package pennychain.center;

import com.sun.xml.internal.ws.api.ha.StickyFeature;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Chris on 4/9/2018.
 */
public class CensusDataGraber {

    public static int itemIndex = 0;
    public static String[] columnContents = new String[68];

    public static String[] retriveColumnHeader(String[][] data) throws FileNotFoundException {
        int row_index = 0;
        int counter = 0;
        int sizeOfArray = data.length;

        KeywordGenerator keys = new KeywordGenerator();
        ArrayList<String> all_keys = keys.grabKeywords();

        while(sizeOfArray != 0)
        {
            if(data[row_index][counter] == all_keys.get(counter))
            {
                columnContents[counter] = data[row_index][counter];
                break;
            }
            else
            {
                counter++;
                itemIndex++;
                sizeOfArray--;
            }
        }

        return columnContents;
    }

    public static String[] retrieveColumnContents(String[][] data, int row_count)
    {
        int row_index = 1;
        int counter = 1;

        while(row_index != row_count)
        {
            columnContents[counter] = data[row_index][itemIndex];
            counter++;
            row_index++;
        }

        return columnContents;
    }
}
