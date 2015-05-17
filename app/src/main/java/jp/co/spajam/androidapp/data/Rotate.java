package jp.co.spajam.androidapp.data;

import java.io.Serializable;

/**
 * 回転を表す
 */
public class Rotate implements Serializable{
    private static final long serialVersionUID = 6255752248513019027L;

    public static final int FAST = 3; // 早い回転
    public static final int NORMAL = 2; // 普通の回転
    public static final int SLOW = 1;  // 遅い回転

    private int rotate;
    private float[] gyroValues;
    private double gyroValue;

    public Rotate(float[] gyroValues,double gyroValue,int rotate){
        this.gyroValues = gyroValues;
        this.gyroValue = gyroValue;
        this.rotate = rotate;
    }

    public int getRotate(){
        return rotate;
    }

    public float[] getGyroValues(){
        return gyroValues;
    }

    public double getGyroValue(){
        return gyroValue;
    }


}