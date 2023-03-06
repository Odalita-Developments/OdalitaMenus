package nl.odalitadevelopments.menus.utils.version;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public enum ProtocolVersion {

    MINECRAFT_1_19_3(761),
    MINECRAFT_1_19_2(760),
    MINECRAFT_1_19_1(760),
    MINECRAFT_1_19(759),
    MINECRAFT_1_18_2(758),
    MINECRAFT_1_18_1(757),
    MINECRAFT_1_18(757),
    MINECRAFT_1_17_1(756),
    MINECRAFT_1_17(755),
    MINECRAFT_1_16_5(754),
    NOT_SUPPORTED(0);

    private final int number;

    ProtocolVersion(int number) {
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }

    public boolean isLowerOrEqual(@NotNull ProtocolVersion version) {
        return this.number <= version.getNumber();
    }

    public boolean isLower(@NotNull ProtocolVersion version) {
        return this.number < version.getNumber();
    }

    public boolean isHigherOrEqual(@NotNull ProtocolVersion version) {
        return this.number >= version.getNumber();
    }

    public boolean isHigher(@NotNull ProtocolVersion version) {
        return this.number > version.getNumber();
    }

    public boolean isEqual(@NotNull ProtocolVersion version) {
        return this.number == version.getNumber();
    }

    private static final ProtocolVersion SERVER_VERSION;

    static {
        SERVER_VERSION = getByVersionString(Bukkit.getServer().getVersion().split("\\(MC: ")[1].split("\\)")[0]);
    }

    public static @NotNull ProtocolVersion getServerVersion() {
        return SERVER_VERSION;
    }

    public static @NotNull ProtocolVersion getByVersionString(@NotNull String versionString) {
        for (ProtocolVersion protocolVersion : values()) {
            if (protocolVersion == NOT_SUPPORTED) continue;

            String version = protocolVersion.name().substring(10).replaceAll("_", ".");
            if (version.equalsIgnoreCase(versionString)) {
                return protocolVersion;
            }
        }

        return NOT_SUPPORTED;
    }
}