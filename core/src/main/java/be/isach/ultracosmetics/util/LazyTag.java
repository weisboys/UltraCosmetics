package be.isach.ultracosmetics.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Inserting;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class LazyTag implements Tag, Inserting {
    private final Supplier<Component> supplier;

    public LazyTag(Supplier<Component> supplier) {
        this.supplier = supplier;
    }

    @Override
    public @NotNull Component value() {
        return supplier.get();
    }
}
