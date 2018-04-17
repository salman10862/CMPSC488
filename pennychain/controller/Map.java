package pennychain.controller;

import java.awt.*;
import java.util.ArrayList;

public class Map {
    private double width,
            len;
    private int
            zoom,
            grid_size=100; // Default value for testing

    //Array for scaling ratios of GoogleMaps at the 20 zoom-in levels (index 0 -> zoom 0)
    //TODO: Implement the more exact latitude-based values (these are good in average case
    public double[] googleZoomScales = { 1128.497220,       2256.994440,
                                        4513.988880,        9027.977761,
                                        18055.955520,       36111.911040,
                                        72223.822090,       144447.644200,
                                        288895.288400,      577790.576700,
                                        1155581.153000,     2311162.307000,
                                        4622324.614000,     9244649.227000,
                                        18489298.450000,    36978596.910000,
                                        73957193.820000,    147914387.600000,
                                        295828775.300000,   591657550.500000};


    private Double latitude,
            longitude;

    private double cell_width,
                cell_length;


    private ArrayList<Point> gridCorners;    //Stores the lower right-hand coordinates of each grid cell.
    private ArrayList<Point> gridCenters;    //Stores the center of each grid cell;
    private double[] gridLats;
    private double[] gridLongs;

    public Map(int grid_size, double width, double len, int zoom, Double latitude, Double longitude){
        this.grid_size = grid_size; //Grid size MUST be divisible by 4
        this.len = len;
        this.width = width;
        this.zoom = zoom;
        this.longitude = longitude;
        this.latitude = latitude;
    }



    public void initializeGrid(){
        gridCorners = new ArrayList<>(grid_size);
        gridCenters = new ArrayList<>(grid_size);
        cell_length = len/(grid_size/2);
        cell_width = width/(grid_size/2);
        gridLats = new double[grid_size/2 * grid_size/2];
        gridLongs = new double[grid_size/2 * grid_size/2];
        for(double l=cell_length; l<=len ;l=l+cell_length){
            for(double w=cell_width; w<=width ; w=w+cell_width){
                gridCorners.add(new Point((int)w, (int)l));
                gridCenters.add(new Point((int)(w-(cell_width/2)), (int) (l-(cell_length/2))));
            }
        }
    }

    public boolean isInGrid(double y){
        Point last_point = gridCorners.get(gridCorners.size()-1);
        if(y<=last_point.getY())
            return true;
        else
            return false;
    }
    public double[] getGridCoordinates(double x, double y){
        double[] coordinates = new double[4];
        //System.out.println("Looking for cell @ pt" + x + " " + y);
        double left_x = 0;
        double left_y = 0;
        double right_x;
        double right_y;
        for (Point p:
             gridCorners) {
             right_x = p.getX();
             right_y = p.getY();
             //System.out.println("Currently examing grid cell: " + left_x + " "+ left_y + ","+ right_x + " "+ right_y + ".");
             if(left_x <= x && right_x >= x) {
                 if (left_y <= y && right_y >= y) {
                     coordinates[0] = left_x;
                     coordinates[1] = left_y;
                     coordinates[2] = right_x;
                     coordinates[3] = right_y;
                     System.out.println("CELL FOUND");
                     break;
                 }
             }
            if(right_x == width) {
                left_x = 0;
                left_y = right_y;
            }
             else {
                left_x = right_x;
            }
        }

        return coordinates;
    }

    public double[] getGridLongs() {
        return gridLongs;
    }

    public double[] getGridLats() {
        return gridLats;
    }

    public ArrayList<Point> getGridCenters() {
        return gridCenters;
    }

    public int getGrid_size(){ return grid_size; }
    public Double getLatitude(){return latitude;}
    public Double getLongitude(){return longitude;}
    public int getZoom(){return zoom;}
    public int changeGrid_size(int grid_size){ return this.grid_size = grid_size; }
    public int getSize(){return 100;} //TODO: REPLACE DUMMY VALUE
    public double getCell_width(){return cell_width;}
    public double getCell_length(){return cell_length;}
    public double getWidth() {return this.width;}
    public double getLength() {return this.len;}

    // TODO: How many trucks each minor distrubtion center needs

}
