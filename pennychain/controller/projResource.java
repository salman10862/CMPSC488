package pennychain.controller;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class projResource {
    private int id,
            amnt = 0;

    private String label;
    private Color color;
    private int rType;  // 1 if it's a population distribution center, 0 otherwise. See: AddAResourceController
    private ArrayList<projResource> constraint_list;
    private HashMap<projResource, Double> constraint_amnt;
    private HashMap<projResource, Double> constraint_dist;
    private int desired_amnt = 0;


    // For grid/map placement
    private int[][] placement;


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

    public Color getColor() {
        return color;
    }


    // *** CONSTRAINT METHODS
    public int getDesired_amnt(){return desired_amnt;}

    public void addConstraint(projResource rType, double dist , double amnt_min){
        constraint_list.add(rType);
        constraint_amnt.put(rType, amnt_min);
        constraint_dist.put(rType, dist);
    }
    public void setDesired_amnt(int amnt){ desired_amnt = amnt;}

    public void increase_amnt(){amnt++;}
    public void decrease_amnt(){amnt--;}

    public Double getConstraintAmnt(projResource rType){
        return constraint_amnt.get(rType);
    }

    public Double getConstraintDist(projResource rType){
        return constraint_dist.get(rType);
    }

    //** RESOURCE TYPE METHODS
    public int getrType(){return rType;}
    public void setrType(int type){ rType = type;}


    // *** GRID PLACEMENT METHODS
    public void initializePlacement(int x, int y){
        placement = new int[x][y];
    }

    public void placeCoordinate(int x, int y){
        placement[x][y] = 1;
    }

    public void removeCoordinate(int x, int y){
        placement[x][y] = 0;
    }

    public void blockCoordinate(int x, int y){
        placement[x][y] = -1;
    }

    public int getValueAtGrid(int x, int y){
        return placement[x][y];
    }

    public ArrayList<Integer> getCoordinates(){
        ArrayList<Integer> coordinate = new ArrayList<>(placement.length * 2);
        for(int x = 0; x < placement.length; x++){
            for(int y = 0; y< placement[x].length; y++)
                if(placement[x][y] == 1){
                    coordinate.add(x);
                    coordinate.add(y);
                }
        }

        return coordinate;
    }

}