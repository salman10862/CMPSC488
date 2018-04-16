package pennychain.controller;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class projResource {
    private int id,
            amnt = 0;

    private String label;
    private String color_s;
    private int rType;  // 1 if it's a population distribution center, 0 otherwise. See: AddAResourceController, -1 for Results
    private ArrayList<String> constraint_list;
    private HashMap<String, Double> constraint_amnt;
    private int desired_amnt = 0;


    // For grid/map placement
    private int[][] placement;
    private int[][] demand;


    public projResource(String label, Color color){
        this.label = label;
        this.color_s = color.toString();
    }

    public String getLabel(){
        return label;
    }

    public void changeLabel(String label){
        this.label = label;
    }

    public ArrayList<String> getConstraint_list() {
        return constraint_list;
    }
    public void addToConstraints(String name, Double amnt){ constraint_list.add(name); constraint_amnt.put(name, amnt);}
    public void removeConstraint(String name){constraint_list.remove(name); constraint_amnt.remove(name);}
    public Double getConstraint_amnt(String type) {return constraint_amnt.get(type);}

    // Adds a constraint where a certain amount of the resource needs to be within a certain distance
    // negative distance = distance not a concern

    public Color getColor() {
        return Color.valueOf(color_s);
    }


    // *** CONSTRAINT METHODS
    public int getDesired_amnt(){return desired_amnt;}


    public void setDesired_amnt(int amnt){ desired_amnt = amnt;}

    public void increase_amnt(){amnt++;}
    public void decrease_amnt(){amnt--;}

    /*public Double getConstraintAmnt(projResource rType){
        return constraint_amnt.get(rType);
    }
    public Double getConstraintDist(projResource rType){
        return constraint_dist.get(rType);
    }*/

    //** RESOURCE TYPE METHODS
    public int getrType(){return rType;}
    public void setrType(int type){ rType = type;}


    // *** GRID PLACEMENT METHODS
    public void initializePlacement(int x, int y){
        placement = new int[x][y];
        demand = new int[x][y];
    }

    public int getDemandForCell(int x, int y){
        return demand[x][y];
    }

    public void setDemandForCell(int x, int y, int amnt){
        demand[x][y] = amnt;
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

    // ***

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

    public ArrayList<Integer> getBlockedCoordinates(){
        ArrayList<Integer> block_coordinate = new ArrayList<>(placement.length * 2);
        for(int x = 0; x < placement.length; x++){
            for(int y = 0; y< placement[x].length; y++)
                if(placement[x][y] == -1){
                    block_coordinate.add(x);
                    block_coordinate.add(y);
                }
        }
        return block_coordinate;
    }

    public int[][] getPlacement() {
        return placement;
    }

}