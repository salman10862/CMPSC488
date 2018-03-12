import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class projResource {
    private int id,
                amnt;

    private String label;;
    private Color color;
    private ArrayList<projResource> constraint_list;
    private HashMap<projResource, Double> constraint_amnt;
    private HashMap<projResource, Double> constraint_dist;


    public projResource(String label, Color color){
        this.label = label;
        this.color = color;
    }

    public String getLabel(){
        return label;
    }

    public void changeLabel(String label){
        this.label = label;
    }

    public ArrayList<projResource> getConstraint_list() {
        return constraint_list;
    }

    // Adds a constraint where a certain amount of the resource needs to be within a certain distance
    // negative distance = distance not a concern
    public void addConstraint(projResource rType, double dist , double amnt_min){
        constraint_list.add(rType);
        constraint_amnt.put(rType, amnt_min);
        constraint_dist.put(rType, dist);
    }

    public Double getConstraintAmnt(projResource rType){
        return constraint_amnt.get(rType);
    }

    public Double getConstraintDist(projResource rType){
        return constraint_dist.get(rType);
    }


}