package gg.nova.client;

public final class NovaModule {

    public enum Category {
        PVP,
        HUD,
        UTILITY
    }

    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;

    public NovaModule(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggle() {
        enabled = !enabled;
    }
}