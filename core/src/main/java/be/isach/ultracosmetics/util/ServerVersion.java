package be.isach.ultracosmetics.util;

import org.bukkit.Bukkit;

/**
 * Created by Sacha on 6/03/16.
 */
public enum ServerVersion {

    // Do not supply a mapping version when there is no NMS module.
    v1_8("1.8.8", 8, null, 0),
    v1_9("1.9.4", 9, null, 0),
    v1_10("1.10.2", 10, null, 0),
    v1_11("1.11.2", 11, null, 0),
    v1_12("1.12.2", 12, null, 0),
    v1_13("1.13.2", 13, null, 0),
    v1_14("1.14.4", 14, null, 0),
    v1_15("1.15.2", 15, null, 0),
    v1_16("1.16.5", 16, null, 0),
    v1_17("1.17.1", 17, null, 0),
    v1_18("1.18.2", 18, null, 0),
    v1_19("1.19.4", 19, "3009edc0fff87fa34680686663bd59df", 3),
    v1_20("1.20.2", 20, "3478a65bfd04b15b431fe107b3617dfc", 2),
    NEW("???", 0, null, 0),
    ;

    private final String name;
    private final int id;
    // mappingsVersion is a random string that is changed whenever NMS changes
    // which is more often than actual NMS revisions happen. You can find this
    // value by checking the source code of this method:
    // org.bukkit.craftbukkit.util.CraftMagicNumbers#getMappingsVersion
    // https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/util/CraftMagicNumbers.java#223
    // getMappingsVersion was added in 1.13.2, earlier versions don't have it.
    private final String mappingsVersion;
    // The NMS revision the corresponding module is built for, or 0 for no module.
    private final int nmsRevision;

    private ServerVersion(String name, int id, String mappingsVersion, int nmsRevision) {
        this.name = name;
        this.id = id;
        this.mappingsVersion = mappingsVersion;
        this.nmsRevision = nmsRevision;
    }

    public String getName() {
        return name;
    }

    public String getMappingsVersion() {
        return mappingsVersion;
    }

    public int getNMSRevision() {
        return nmsRevision;
    }

    public static ServerVersion earliest() {
        return values()[0];
    }

    public static ServerVersion byId(int id) {
        if (id == 0) return null;
        for (ServerVersion version : values()) {
            if (id == version.id) {
                return version;
            }
        }
        return null;
    }

    public boolean isAtLeast(ServerVersion version) {
        return this.compareTo(version) >= 0;
    }

    public boolean offhandAvailable() {
        return isAtLeast(v1_9);
    }

    public boolean isMobChipAvailable() {
        return isAtLeast(v1_13) && this != NEW && !isMobchipEdgeCase();
    }

    public boolean isNmsSupported() {
        return nmsRevision > 0;
    }

    public String getNmsVersion() {
        //noinspection UnnecessaryToStringCall
        return toString() + "_R" + nmsRevision;
    }

    public static boolean isMobchipEdgeCase() {
        String rawVersion = Bukkit.getVersion();
        String version = rawVersion.substring(rawVersion.lastIndexOf(" ") + 1, rawVersion.length() - 1);
        return version.equals("1.19");
    }

}
