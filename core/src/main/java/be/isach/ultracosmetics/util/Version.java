package be.isach.ultracosmetics.util;

/**
 * Version.
 *
 * @author iSach
 * @since 08-16-2015
 */
public class Version implements Comparable<Version> {

    // only numbers (ex. 2.6.1)
    private final String version;
    // classifier (ex. RELEASE)
    private final String classifier;
    private final String gitHash;


    public Version(String version, String classifier, String gitHash) {
        if (version == null) {
            throw new IllegalArgumentException("Version can not be null");
        }
        this.version = version;
        this.classifier = classifier;
        this.gitHash = gitHash;
    }

    public Version(String version) {
        this(version, "RELEASE", null);
    }

    public final String numbersOnly() {
        return this.version;
    }

    public String versionWithClassifier() {
        return version + "-" + classifier;
    }

    public String versionClassifierCommit() {
        StringBuilder builder = new StringBuilder(version);
        builder.append('-').append(classifier);
        if (gitHash != null) {
            builder.append(" (commit ").append(gitHash).append(')');
        }
        return builder.toString();
    }

    @Override
    public int compareTo(Version otherVersion) {
        String[] thisParts = this.numbersOnly().split("\\.");
        String[] thatParts = otherVersion.numbersOnly().split("\\.");
        int length = Math.max(thisParts.length, thatParts.length);
        for (int i = 0; i < length; i++) {
            int thisPart = i < thisParts.length ?
                    Integer.parseInt(thisParts[i]) : 0;
            int thatPart = i < thatParts.length ?
                    Integer.parseInt(thatParts[i]) : 0;
            int cmp = Integer.compare(thisPart, thatPart);
            if (cmp != 0) {
                return cmp;
            }
        }
        // release > dev build of same version
        return Boolean.compare(this.isRelease(), otherVersion.isRelease());
    }

    public boolean isDev() {
        return classifier.toLowerCase().contains("dev");
    }

    public boolean isRelease() {
        return !isDev();
    }

    @Override
    public boolean equals(Object that) {
        return this == that || that != null && this.getClass() == that.getClass() && this.compareTo((Version) that) == 0;
    }

    @Override
    public int hashCode() {
        return version.hashCode();
    }

    @Override
    public String toString() {
        return numbersOnly();
    }
}
