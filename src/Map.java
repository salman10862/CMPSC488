import java.math.BigDecimal;

public class Map {
    private int width,
                len,
                grid_size=100; // Default value for testing

    //private BigDecimal latitude,
      //                  longitude;

    public Map(int grid_size, int width, int len){
        this.grid_size = grid_size;
        this.len = len;
        this.width = width;
    }

    public int getGrid_size(){ return grid_size; }
    public int changeGrid_size(int grid_size){ return this.grid_size = grid_size; }

        // D-to-D_matrix
    // TODO: Row = Major Redistribution Center: Distibution's Distrubtion
    // TODO: column = minor distribution: Customer's Distribution



    // TODO: How many trucks each minor distrubtion center needs

    // TODO: Gas price for a region per distnace


    //
    //TODO: Full list of what we want to reasonably abstract from GoogleMaps API (look over KXL formats)

}
