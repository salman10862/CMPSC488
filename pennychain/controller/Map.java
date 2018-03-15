package pennychain.controller;

import java.awt.*;
import java.util.ArrayList;

public class Map {
    private int width,
            len,
            zoom,
            grid_size=100; // Default value for testing
    
    public int[][] map_data; //This is the matrix definining existing infrastructure and invalid locations on the grid 
                                    //(current implementation is -1 for invalid locations, 0 for potential locations, 
                                    //and 1 for those already being used, but this can be adjusted to accomodate additional options later)
                              //TODO: We need to populate this matrix with values indicated by our "invisible grid" on the map

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


    private ArrayList<Point> gridCorners;    //Stores the lower right-hand coordinates of each grid cell.
    private ArrayList<Point> gridCenters;    //Stores the center of each grid cell;

    public Map(int grid_size, int width, int len, int zoom, Double latitude, Double longitude){
        this.grid_size = grid_size; //Grid size MUST be divisible by 4
        this.len = len;
        this.width = width;
        this.zoom = zoom;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    //TODO: Bring up computation concerns with group
    //Concern: These are all based on ints: But these are all based on pixel values,
    // So graphically representing them is an issue anyways.
    public void initializeGrid(){
        gridCorners = new ArrayList<>(grid_size);
        gridCenters = new ArrayList<>(grid_size);
        int cell_length = len/(grid_size/2);
        int cell_width = width/(grid_size/2);
        for(int l=cell_length; l<=len ;l=l+cell_length){
            for(int w=cell_width; w<=width ; w=w+cell_width){
                gridCorners.add(new Point(l,w));
                gridCenters.add(new Point(l/2, w/2));
            }
        }
    }

    public int getGrid_size(){ return grid_size; }
    public Double getLatitude(){return latitude;}
    public Double getLongitude(){return longitude;}
    public int getZoom(){return zoom;}
    public int changeGrid_size(int grid_size){ return this.grid_size = grid_size; }
    public int getSize(){return 100;} //TODO: REPLACE DUMMY VALUE
    public int getWidth() {return this.width;}
    public int getLength() {return this.len;}

    // D-to-D_matrix
    // TODO: Row = Major Redistribution Center: Distibution's Distrubtion
    // TODO: column = minor distribution: Customer's Distribution
            //ASIDE (from Jason): Not 100% sure, but these may be what I indicated in comments near the map_data declaration. Let me know if its something else :^)



    // TODO: How many trucks each minor distrubtion center needs

    // TODO: Gas price for a region per distnace


    //
    //TODO: Full list of what we want to reasonably abstract from GoogleMaps API (look over KXL formats)

}
