import java.util.ArrayList;
import java.util.HashMap;

public class projResource {
    int id;
    String label;
    int amnt;
    ArrayList<projResource> constraint_list;
    HashMap<projResource, Double> constraint_amnt;
}