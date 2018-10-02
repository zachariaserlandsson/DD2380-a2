/**
 * Custom class used for keeping value pairs of GameStates and their heuristic
 * value. Implements the Comparable interface to specify that GameStateHeuristic
 * objects should be sorted according to heuristic value.
 */
public class GameStateHeuristic implements Comparable<GameStateHeuristic> {
  public GameState gameState;
  public int hValue;

  public GameStateHeuristic(GameState gameState, int hValue) {
    this.gameState = gameState;
    this.hValue = hValue;
  }

  /**
   * Implements the compareTo function of the Comparable interface to specify
   * how the objects are to be compared to eachother.
   * @param  other The other object that this object is to be compared with.
   * @return Returns a positive integer if this object has a greater heuristic
   * value than the other object, 0 if they have the same and negative value if
   * it has a lesser heuristic value.
   */
  public int compareTo(GameStateHeuristic other) {
    return this.hValue - other.hValue;
  }
}
