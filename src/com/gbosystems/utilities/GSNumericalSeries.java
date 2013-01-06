package com.gbosystems.utilities;

import java.util.ArrayList;

/**
 * A class for tracking a series of numerical data.
 * 
 * @author Geoff O'Donnell
 */
public class GSNumericalSeries {

    /* Declare class members */
    private Double minimum;
    private Double average;
    private Double maximum;
    private Double stdDev;
    private ArrayList<Double> series;

    public GSNumericalSeries(){

        /* Initialize class members */
        series = new ArrayList<Double>();
        minimum = Double.NaN;
        maximum = Double.NaN;
        average = Double.NaN;
        stdDev = Double.NaN;
    }
	
    public void add(Double number){

        /* Add input to the series */
        series.add(number);

        /* Refresh series stats */
        /* Note: calculateStdDev() requires the average, so it's important to calculate that first */
        refreshMinumum();
        refreshMaximum();
        calculateAverage();
        calculateStdDev();
    }

    public Double getMinimum(){
        return minimum;
    }

    public Double getMaximum(){
        return maximum;
    }
	
    public Double getAverage(){
        return average;
    }

    public Double getStdDev(){
        return stdDev;
    }

    private void refreshMinumum(){

        if ( (minimum.equals(Double.NaN)) || (minimum > series.get(series.size() - 1)) ){
            minimum = series.get(series.size() - 1);
        }
    }
	
    private void refreshMaximum(){

        if ( (maximum.equals(Double.NaN)) || (maximum < series.get(series.size() - 1)) ){
            maximum = series.get(series.size() - 1);
        }
    }

    private void calculateAverage(){

        Double total = 0.0;

        for (Double number : series){
            total += number;
        }

        average = total / series.size();
    }
	
    private void calculateStdDev(){

        /* Declare local variables */
        Double tmp = 0.0;

        for (Double number : series){    			
                tmp += Math.pow((number - average), 2);
        }

        tmp = tmp / (series.size() - 1);

        stdDev = Math.sqrt(tmp);
    }
	
}
