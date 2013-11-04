package game.ui;

import game.utility.ColoredChar;

public interface GameUI extends AutoCloseable {

    public void drawMap(ColoredChar[][] charMap);

    public char getInputChar();

    public void showAnnouncement(String msg);

    public void showMessage(String msg);

    public void showStats(String stats);

    public int getScreenWidth();

    public int getScreenHeight();

    public int getMapWidth();

    public int getMapHeight();

}
