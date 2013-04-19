package game.utility.interfaces;

public interface GameAction {
	  
	  void execute();

	  boolean canBeExecuted();
	  
	  int actionPointsLeft();

	  void setActionPointsLeft(int points);

}