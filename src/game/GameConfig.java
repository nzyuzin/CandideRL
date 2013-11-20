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

package game;

import java.awt.Font;

/**
 * GameConfig is intended for holding configuration option
 */
public class GameConfig {

    private GameConfig() { }

    public static final Font DEFAULT_FONT = new Font("DejaVu Sans Mono", Font.PLAIN, 16);
    public static final boolean FIT_TO_SCREEN = false;
    public static final String LOG_CONVERSION_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%p] %C.%M:%L - %m%n";
    public static final int MAP_WIDTH = 500;
    public static final int MAP_HEIGHT = 600;
    public static final int DEFAULT_MAP_SIZE = 100;
}
