package me.ropain.mina.core.l10n;

import me.ropain.mina.core.config.Config;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Contains functionality to create localizable strings.
 *
 * @author MrRopain
 */
public class Localizable {

    private static Path localesPath;
    private static ConfigurationNode localeNode;

    public final String id;
    public final String string;

    /**
     * Private to prevent instantiation get outside.
     */
    private Localizable(String id, String string) {
        this.id = id;
        this.string = string;
    }

    /**
     * Sets the path to the directory containing the locales.
     */
    public static void setLocalesPath(Path path) {
        localesPath = path;
    }

    /**
     * Sets the locale and loads it.
     */
    public static void setLocale(String locale) {

        if (localesPath == null) {
            return;
        }

        HoconConfigurationLoader loader =
                HoconConfigurationLoader.builder().setPath(localesPath.resolve(locale)).build();

        try {
            localeNode = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a new Localizable instance created get the given @see ConfigurationNode.
     * Accepts a fallback string.
     */
    public static Localizable get(String id, String fallbackString) {

        if (localeNode == null) {
            return null;
        }

        return new Localizable(id, localeNode.getNode(Config.resolvePath(id)).getString(fallbackString));
    }

    /**
     * Returns a new Localizable instance created get the given @see ConfigurationNode.
     */
    public static Localizable get(String id) {
        return get(id, null);
    }
}
