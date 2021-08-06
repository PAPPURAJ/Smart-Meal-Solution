package com.blogspot.rajbtc.hallmanagement.admin.meallist;

public class MealData {
    private  String name;
    private long cost;

    public MealData() {
    }

    public MealData(String name, String cost) {
        this.name = name;
        try{
            this.cost=Long.parseLong(cost);
        }catch (Exception e){
            this.cost=0;
        }
    }

    public String getName() {
        return name;
    }

    public long getCost() {
        return cost;
    }
}
