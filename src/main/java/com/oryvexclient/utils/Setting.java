
package com.oryvexclient.utils;
public abstract class Setting<T> {
    protected String name;
    protected T value;
    protected T defaultValue;
    public Setting(String name, T value) { this.name = name; this.value = value; this.defaultValue = value; }
    public String getName() { return name; }
    public T getValue() { return value; }
    public void setValue(T value) { this.value = value; }
    public void reset() { this.value = defaultValue; }
}
