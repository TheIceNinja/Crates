package net.theiceninja.crates.api.chest;

public enum ChestType {

    RARE("&#17F73C&lRare"),
    EPIC("&#C117F7&lEpic");

    private final String prefix;

    ChestType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
