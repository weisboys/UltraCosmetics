package be.isach.ultracosmetics.cosmetics.deatheffects;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.type.DeathEffectType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.ItemFactory;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DeathEffectItemExplode extends DeathEffect {

    private final List<Item> items = new ArrayList<>();
    private final XSound.SoundPlayer explode;
    private final XSound.SoundPlayer hurt;

    public DeathEffectItemExplode(UltraPlayer owner, DeathEffectType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);

        this.explode = XSound.ENTITY_GENERIC_EXPLODE.record().withVolume(0.3f).withPitch(1.5f).soundPlayer().forPlayers(getPlayer());
        this.hurt = XSound.ENTITY_CHICKEN_HURT.record().withVolume(1.4f).withPitch(1.5f).soundPlayer().forPlayers(getPlayer());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void displayParticles() {
        Player player = getPlayer();
        explode.play();
        hurt.play();

        for (int i = 0; i < 30; i++) {
            items.add(ItemFactory.createUnpickableItemVariance(XMaterial.COOKED_CHICKEN, player.getLocation(), RANDOM, 1));
        }

        getUltraCosmetics().getScheduler().runAtEntityLater(getPlayer(), () -> items.forEach(Item::remove), 50);

    }

    @Override
    public void onClear() {
        items.forEach(Item::remove);
        items.clear();
    }

}
