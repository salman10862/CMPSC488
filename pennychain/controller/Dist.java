package pennychain.controller;

public class Dist {
    private int size = 0;
    private double[][] district_data;

    public int getSize() {
        return size;
    }

    public double[][] getDistData(){
        district_data = new double[size][size];
        return district_data;
    }
}
