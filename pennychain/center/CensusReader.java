package pennychain.center;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CensusReader {

    public static int x = 0;
    public static int holder = 0;

    public static String[] getCensusData() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("pennychain\\center\\all_050_in_42.P12.csv"));
        String str;
        //Taking in the file line of the file, the column 'titles'
        str = br.readLine();
        char com = ',';

        //System.out.println(str);
        //For loop checking for how many iterations of , there are in
        //each line of the file
        for (int y = 0; y < str.length(); y++) {
            if (com == str.charAt(y)) {
                x++;
            }
        }

        String[] censusData = new String[x];
        censusData = str.split(",");

        //HashMap<String,Integer> headers = getHeadersFromFile(censusData, x);
        //holder++;


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

        return censusData;
    }

    public static String[][] getFullData()
    {
        String[][] fullData = new String[68][x];
        return fullData;
    }

    public static String getStringSource() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("pennychain\\center\\all_050_in_42.P12.csv"));
        String str;
        //Taking in the file line of the file, the column 'titles'
        str = br.readLine();
        return str;
    }

}
