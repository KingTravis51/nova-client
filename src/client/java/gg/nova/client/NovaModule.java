package gg.nova.client;

public final class NovaModule {
    public enum Category { PVP, PERFORMANCE, HUD, VISUALS, COSMETICS, UTILITY }

    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;
    private int intensity = 50;
    private int scale = 100;
    private boolean background = true;

    public NovaModule(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void toggle() { enabled = !enabled; }
    public int getIntensity() { return intensity; }
    public void setIntensity(int value) { intensity = Math.max(0, Math.min(100, value)); }
    public int getScale() { return scale; }
    public void setScale(int value) { scale = Math.max(50, Math.min(150, value)); }
    public boolean hasBackground() { return background; }
    public void setBackground(boolean value) { background = value; }
    public void cycleIntensity() { setIntensity(intensity >= 100 ? 0 : intensity + 10); }
    public void cycleScale() { setScale(scale >= 150 ? 50 : scale + 10); }
}