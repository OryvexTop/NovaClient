
package com.oryvexclient.utils;
public class Animation {
    private float value, target, speed;
    public Animation(float value, float speed) { this.value = value; this.target = value; this.speed = speed; }
    public void setTarget(float target) { this.target = target; }
    public float getValue() { value += (target - value) * speed; return value; }
}
