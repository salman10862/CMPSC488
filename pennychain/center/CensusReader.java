package pennychain.center;

import pennychain.db.Hash;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;

public class CensusReader {


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

        String[] cencusData = new String[x];
        cencusData = str.split(",");
        String[][] fullData = new String[68][x];

        HashMap<String,Integer> headers = getHeaders(cencusData, x);
        holder++;
        getColumnContents(cencusData, fullData, str, br, x, holder);


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
        public static HashMap<String,Integer> getHeaders(String[] data, int boundary)
        {
            HashMap<String,Integer> headers = new HashMap<>();

            //CensusDataGraber example = new CensusDataGraber();
            //for loop adding the first line to the multideminsional array
            for(int y = 0; y < boundary; y++)
            {
                //fullData[holder][y]=cencusData[y];
                headers.put(data[y], y);
                System.out.println(headers.get(data[y]));
            }
            //holder++;
            //System.out.println(example.retriveColumnHeader(fullData));

            //System.out.println(cencusData[1]);

            return headers;
        }

    public static String[][] getColumnContents(String[] data, String[][] totalData, String source, BufferedReader br, int boundary, int holder) throws IOException {


        //While/for loop adding all the lines of the file to multidimensional array
        while((source = br.readLine()) != null)
        {
            data = source.split(",");
            for(int y = 0; y < boundary; y++)
            {
                totalData[holder][y]= data[y];
                System.out.println(totalData[holder][y]);
            }
            holder++;

        }
        System.out.println(totalData[0][59]);
        return totalData;

    }

}
