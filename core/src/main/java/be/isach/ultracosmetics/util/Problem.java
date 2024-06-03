package be.isach.ultracosmetics.util;

import be.isach.ultracosmetics.UltraCosmeticsData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

public enum Problem {
    // Fatal
    BAD_CONFIG(true, "config.yml is invalid, please run it through a YAML checker.", "invalid-config"),
    BAD_MESSAGES(true, "Messages file is invalid, please run it through a YAML checker.", "invalid-messages-file"),

    // Non-fatal
    BAD_MC_VERSION(false, "This version of Minecraft is not fully supported, please check for an update.", "unsupported-mc-version"),
    NMS_LOAD_FAILURE(false, "Couldn't load support for this MC version, please report this issue.", "nms-load-failure"),
    BAD_MAPPINGS_VERSION(false, "Server internals seem to have changed since this build was created, you will likely experience issues.", "bad-mappings-version") {
        @Override
        public Component getSummary() {
            return super.getSummary().append(Component.text(" (Expected version: " + UltraCosmeticsData.get().getServerVersion().getName() + ")"));
        }
    },
    TALL_DISGUISES_DISABLED(false, "TallSelfDisguises is disabled in LibsDisguises players.yml. This may cause morph self view to not behave as intended.", "tall-disguises-disabled"),
    SQL_INIT_FAILURE(false, "SQL failed to connect, using flatfile support instead.", "sql-init-failure"),
    WORLDGUARD_HOOK_FAILURE(false, "Failed to hook into WorldGuard.", "worldguard-hook-failure"),
    SQL_MIGRATION_REQUIRED(false, "MySQL config settings have changed, please review them. SQL storage is disabled until then.", "sql-migration-required"),
    ;

    private static final String PROBLEMS_WIKI = "https://github.com/UltraCosmetics/UltraCosmetics/wiki/Problems#";
    private final boolean severe;
    private final String description;
    private final String wikiTitle;

    private Problem(boolean severe, String description, String wikiTitle) {
        this.severe = severe;
        this.description = description;
        this.wikiTitle = wikiTitle;
    }

    public boolean isSevere() {
        return severe;
    }

    public String getDescription() {
        return description;
    }

    public String getWikiURL() {
        return PROBLEMS_WIKI + wikiTitle;
    }

    public Component getSummary() {
        return Component.text(description).clickEvent(ClickEvent.openUrl(getWikiURL()));
    }
}
