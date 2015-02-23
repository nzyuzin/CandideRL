/*
 * This file is part of CandideRL.
 *
 * CandideRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CandideRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CandideRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.nzyuzin.candiderl.game;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Properties;

/**
 * GameConfig is intended for holding configuration options.
 *
 * In order to get access to configuration options through configuration file add them to internal enum.
 */
public class GameConfig {

    private GameConfig() { }

    static {
        try {
            ConfigHelper.read();
        } catch (ConfigurationException e) {
            throw new RuntimeException("Error reading configuration", e);
        }
    }

    public static final Font DEFAULT_FONT = new Font(ConfigHelper.DEFAULT_FONT.getValue(), Font.PLAIN, 16);
    public static final boolean FIT_TO_SCREEN =
            Boolean.parseBoolean(ConfigHelper.FIT_TO_SCREEN.getValue());
    public static final int MAP_WIDTH = Integer.parseInt(ConfigHelper.MAP_WIDTH.getValue());
    public static final int MAP_HEIGHT = Integer.parseInt(ConfigHelper.MAP_HEIGHT.getValue());
    public static final int DEFAULT_MAP_SIZE = 100;
    public static final int VIEW_DISTANCE_LIMIT =
            Integer.parseInt(ConfigHelper.VIEW_DISTANCE_LIMIT.getValue());
    public static final int DEFAULT_MAP_WINDOW_WIDTH =
            Integer.parseInt(ConfigHelper.DEFAULT_MAP_WINDOW_WIDTH.getValue());
    public static final int DEFAULT_MAP_WINDOW_HEIGHT =
            Integer.parseInt(ConfigHelper.DEFAULT_MAP_WINDOW_HEIGHT.getValue());
    public static final boolean CALCULATE_FIELD_OF_VIEW =
            Boolean.parseBoolean(ConfigHelper.CALCULATE_FIELD_OF_VIEW.getValue());
    public static final boolean RANDOM_MAP = Boolean.parseBoolean(ConfigHelper.RANDOM_MAP.getValue());
    public static final boolean BUILD_MAP_FROM_FILE = Boolean.parseBoolean(ConfigHelper.BUILD_MAP_FROM_FILE.getValue());
    public static final boolean SPAWN_MOBS = Boolean.parseBoolean(ConfigHelper.SPAWN_MOBS.getValue());

    public static void write() {
        // Runtime configuration is planned so this method will be used in future
        // to save configuration between sessions.
        ConfigHelper.write();
    }

    private enum ConfigHelper {
        DEFAULT_FONT("default_font", "DejaVu Sans Mono"),
        FIT_TO_SCREEN("fit_to_screen", "false"),
        MAP_WIDTH("map_width", "600"),
        MAP_HEIGHT("map_height", "500"),
        VIEW_DISTANCE_LIMIT("view_distance_limit", "10"),
        DEFAULT_MAP_WINDOW_WIDTH("default_map_window_width", "80"),
        DEFAULT_MAP_WINDOW_HEIGHT("default_map_window_height", "25"),
        CALCULATE_FIELD_OF_VIEW("calculate_field_of_view", "true"),
        RANDOM_MAP("random_map", "true"),
        BUILD_MAP_FROM_FILE("build_map_from_file", "false"),
        SPAWN_MOBS("spawn_mobs", "true");

        private String property;
        private String defValue;

        private ConfigHelper(String property, String defValue) {
            this.property = property;
            this.defValue = defValue;
        }

        public String getValue() {
            return configuration.getProperty(this.property);
        }

        private static Properties defaultConfiguration = new Properties();

        static {
            for (ConfigHelper property : ConfigHelper.values()) {
                defaultConfiguration.put(property.property, property.defValue);
            }
        }

        private static final String CONFIGURATION_FILE_HEADER = "CandideRL Configuration File";

        private static final Path pathToConfig = Paths.get("config.properties");
        private static Properties configuration;

        public static void read() throws ConfigurationException {
            try {
                if (Files.exists(pathToConfig)) {
                    try (InputStream inStream = Files.newInputStream(pathToConfig)) {
                        configuration = new Properties();
                        configuration.load(inStream);
                        validateConfigurationProperties();
                        for (String property : defaultConfiguration.stringPropertyNames()) {
                            if (!configuration.containsKey(property))
                                configuration.put(property, defaultConfiguration.getProperty(property));
                        }
                    }
                } else {
                    configuration = defaultConfiguration;
                    try (OutputStream outputStream = Files.newOutputStream(pathToConfig)) {
                        configuration.store(outputStream, CONFIGURATION_FILE_HEADER);
                    }
                }
            } catch (IOException e) {
                throw new ConfigurationException(e);
            }
        }

        public static void write() {
            try (OutputStream outputStream = Files.newOutputStream(pathToConfig)) {
                configuration.store(outputStream, CONFIGURATION_FILE_HEADER);
            } catch (IOException e) {
                throw new RuntimeException("Error writing configuration", e);
            }
        }

        private static boolean validateConfigurationProperties() throws ConfigurationException {
            // TODO: include check for type of values specified, e.g. check that boolean property has boolean value
            for (String propertyName : configuration.stringPropertyNames()) {
                if (!defaultConfiguration.containsKey(propertyName)) {
                    throw new ConfigurationException(String.format("Wrong configuration property:%n%s=%s",
                            propertyName, configuration.getProperty(propertyName)));
                }
            }
            return true;
        }
    }
}
