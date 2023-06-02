package be.isach.ultracosmetics.util;

import be.isach.ultracosmetics.UltraCosmeticsData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

/**
 * Package: be.isach.ultracosmetics.util
 * Created by: sachalewin
 * Date: 5/08/16
 * Project: UltraCosmetics
 */
public class TextUtil {

    public static Component filterPlaceholderColors(Component placeholder) {
        if (UltraCosmeticsData.get().arePlaceholdersColored()) return placeholder;
        return stripColor(placeholder);
    }

    public static Component stripColor(Component component) {
        return Component.text(PlainTextComponentSerializer.plainText().serialize(component));
    }
}
