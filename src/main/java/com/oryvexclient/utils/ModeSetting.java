
package com.oryvexclient.utils;
import java.util.List;
public class ModeSetting extends Setting<String> {
    private List<String> modes;
    public ModeSetting(String name, String value, List<String> modes) { super(name, value); this.modes = modes; }
    public List<String> getModes() { return modes; }
    public void cycle() { int idx = modes.indexOf(getValue()); setValue(modes.get((idx + 1) % modes.size())); }
}
