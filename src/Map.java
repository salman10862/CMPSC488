import java.math.BigDecimal;

public class Map {
    private int width,
                len,
                zoom,
                grid_size=100; // Default value for testing


    private Double latitude,
                        longitude;

    public Map(int grid_size, int width, int len, int zoom, Double latitude, Double longitude){
        this.grid_size = grid_size;
        this.len = len;
        this.width = width;
        this.zoom = zoom;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getGrid_size(){ return grid_size; }
    public Double getLatitude(){return latitude;}
    public Double getLongitude(){return longitude;}
    public int getZoom(){return zoom;}
    public int changeGrid_size(int grid_size){ return this.grid_size = grid_size; }

        // D-to-D_matrix
    // TODO: Row = Major Redistribution Center: Distibution's Distrubtion
    // TODO: column = minor distribution: Customer's Distribution



    // TODO: How many trucks each minor distrubtion center needs

    // TODO: Gas price for a region per distnace


    //
    //TODO: Full list of what we want to reasonably abstract from GoogleMaps API (look over KXL formats)

}
