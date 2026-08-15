
package com.oryvexclient.utils;
public class Animation {
    private double value, target, speed;
    public Animation(double value, double speed) { this.value = value; this.target = value; this.speed = speed; }
    public void setTarget(double target) { this.target = target; }
    public double getValue() {
        double diff = target - value;
        value += diff * speed;
        if (Math.abs(diff) < 0.01) value = target;
        return value;
    }
    public boolean isDone() { return value == target; }
}
