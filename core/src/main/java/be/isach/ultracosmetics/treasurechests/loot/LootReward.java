package be.isach.ultracosmetics.treasurechests.loot;

import be.isach.ultracosmetics.cosmetics.type.CosmeticType;

import org.bukkit.inventory.ItemStack;

public class LootReward {
    private final String[] name;
    private final ItemStack stack;
    private final String message;
    private final boolean broadcast;
    private final boolean firework;
    private final CosmeticType<?> cosmetic;

    public LootReward(String[] name, ItemStack stack, String message, boolean broadcast, boolean firework, CosmeticType<?> cosmetic) {
        this.name = name;
        this.stack = stack;
        this.message = message;
        this.broadcast = broadcast;
        this.firework = firework;
        this.cosmetic = cosmetic;
    }

    public LootReward(String[] name, ItemStack stack, String message, boolean broadcast, boolean firework) {
        this(name, stack, message, broadcast, firework, null);
    }

    public String[] getName() {
        return name;
    }

    public ItemStack getStack() {
        return stack;
    }

    public String getMessage() {
        return message;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public boolean isFirework() {
        return firework;
    }

    public CosmeticType<?> getCosmetic() {
        return cosmetic;
    }
}
