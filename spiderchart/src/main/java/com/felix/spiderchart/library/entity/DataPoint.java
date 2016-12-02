package com.felix.spiderchart.library.entity;

/**
 * @author Felix
 */
public class DataPoint {
    private float x;
    private float y;
    private double value;

    public DataPoint() {
    }

    public DataPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
