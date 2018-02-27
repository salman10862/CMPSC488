package javaapplication3;

/**
 *
 * @author hgfel
 */
public class JavaApplication3 {

    static final int varBound = 4; //For testing purpose: change this for more/less vars
    //later, this value will come from project data (# of potential distributories)
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int[][] dist; //distances
        int CenterNum = 1;  //total number of desired distribution centers
        int[][] testMap = new int[][]{    { 5, 8, 6, 9}, //these are placeholder values
                { 8, 5, 10, 5},//until project has map with
                { 11, 8, 5, 9},//distance data we can extract.
                { 15, 7, 6, 5} //For now, these numbers will populate dist matrix.
        };

        dist = new int[varBound][varBound];
        for(int i = 0; i < varBound; i++) {
            for(int j = 0; j < varBound; j++) {
                dist[i][j] = testMap[i][j]; //testMap will be replaced with appropriate
            }								//data from project map
        }

        String[] var = new String[varBound];

        for(int i = 0; i < varBound; i++) {
            var[i] = "X[" + i + "]";
        }

        System.out.println("Model");
        System.out.println("  Variables");

        for(int x = 0; x < varBound; x++) {
            System.out.println("    " + var[x] + " 1, >=0, >=1");
        }

        System.out.println("  End Variables");
        System.out.println("");
        System.out.println("  Equations");

        for(int x = 0; x < varBound; x++) {
            System.out.print(var[x]);
            if(x != varBound - 1) {
                System.out.print(" + ");
            }
        }
        System.out.println(" = " + CenterNum);
        System.out.println("");
        System.out.println("  Objective function");

        System.out.print("minimize ");
        for(int i = 0; i < varBound; i++) {
            for(int j = 0; j < varBound; j++) {
                System.out.print(dist[i][j] + " * " + var[i] + " + ");
            }
        }
        System.out.print("0");
    }
}