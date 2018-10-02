import java.util.*;
import java.lang.Math;

public class Player {
  private int PLAYER_X = 1;
  private int PLAYER_O = 2;
  private int MAX_DEPTH = 8;

    /**
     * Performs a move
     *
     * @param gameState
     *            the current state of the board
     * @param deadline
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState gameState, final Deadline deadline) {
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);

        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }

        int bestIndex = -1;
        int bestVal = Integer.MIN_VALUE;
        int currVal;
        int i = 0;
        /**
         * Iterate ove all the possible next states. Change the player to player
         * O since it's currently player X's turn. Update Alpha to make pruning
         * possible in future calls to alphaBetaRec.
         */
        for (GameState g : nextStates) {
          currVal = alphaBetaRec(g, bestVal, Integer.MAX_VALUE, PLAYER_O, 0);
          if (currVal >= bestVal) {
            bestVal = currVal;
            bestIndex = i;
          }
          i++;
        }

        // Returns the best possible move found when iterating over the possible moves.
        return nextStates.elementAt(bestIndex);
    }

    /**
     * Implementation of MiniMax algorithm using Alpha-Beta pruning and move
     * ordering using the heuristic function below.
     * @param  gameState The current state of the game.
     * @param  alpha Value of Alpha in the Alpha-Beta pruning algorithm.
     * @param  beta Value of Beta in the Alpha-Beta pruning algorithm.
     * @param  player int value denoting the current player (X or O).
     * @param  depth int denoting our current recursion depth. If depth exceeds
     * our specified limit, we return a heuristic value instead of a 'real' one.
     * @return Returns an integer value representing the value of nodes in the
     * MiniMax tree.
     */
    public int alphaBetaRec(GameState gameState, int alpha, int beta, int player, int depth) {
      int v = -1;

      // If we're at max allowed depth, we return the value of the current state
      // using our heuristic function.
      if (depth > MAX_DEPTH) {
        v = evaluateBoard(gameState);
        return v;
      }

      Vector<GameState> possibleMoves = new Vector<GameState>();
      gameState.findPossibleMoves(possibleMoves);

      /**
       * If we're in a terminal game state, we need to check if it's a win for
       * either player or if it's a draw, and set return value accordingly.
       */
      if (possibleMoves.size() == 0) {
        if (gameState.isOWin()) {
          v = Integer.MIN_VALUE;
        } else if (gameState.isXWin()) {
          v = Integer.MAX_VALUE;
        } else if (gameState.isEOG()) {
          v = 0;
        }
        return v;
      }

      GameStateHeuristic[] reorderedMoves = new GameStateHeuristic[possibleMoves.size()];
      int i = 0;
      /**
       * Iterating over the possible moves and evaluating them using the heuristic
       * function in order to order them according to heuristic value (i.e. move
       * ordering).
       */
      for (GameState g : possibleMoves) {
        reorderedMoves[i] = new GameStateHeuristic(g, evaluateBoard(g));
        i++;
      }
      // Sorts them descending if player X is playing (since player X is maximizing)
      // and ascending for player O.
      if (player == PLAYER_X) {
        Arrays.sort(reorderedMoves, Collections.reverseOrder());
      } else {
        Arrays.sort(reorderedMoves);
      }

      /**
       * Implementation of mini-max algorithm with alpha-beta pruning.
       */
      if (player == PLAYER_X) {
        v = Integer.MIN_VALUE;
        for (GameState g : possibleMoves) {
          v = Math.max(v, alphaBetaRec(g, alpha, beta, PLAYER_O, depth++));
          if (v >= beta) {
            break;
          }
          alpha = Math.max(alpha, v);
        }
      } else {
        v = Integer.MAX_VALUE;
        for (GameState g : possibleMoves) {
          v = Math.min(v, alphaBetaRec(g, alpha, beta, PLAYER_X, depth++));
          if (alpha >= v) {
            break;
          }
          beta = Math.min(beta, v);
        }
      }
      return v;
    }

    /**
     * Heuristic evaluation function for tic-tac-toe. Evaluates a board by iterating
     * over rows, columns and diagonals and assigning values according to how
     * many X marks there are in that row/column/diagonal (1, 10, 100) and 0 if
     * there are none or if there are >0 O marks there.
     * @param  gameState The board to evaluate.
     * @return An integer value denoting the heuristic value of the given board.
     */
    public int evaluateBoard(GameState gameState) {
      Double totSum = 0.0;

      /**
       * Iterates over all the rows of the game board.
       */
      Double currStreak;
      for (int i = 0; i < gameState.BOARD_SIZE; i++) {
        currStreak = 0.0;
        for (int j = 0; j < gameState.BOARD_SIZE; j++) {
          if (gameState.at(i, j) == PLAYER_O) {
            currStreak = 0.0;
            break;
          } else if (gameState.at(i, j) == PLAYER_X) {
            currStreak += 1.0;
          }
        }
        totSum += currStreak == 0.0 ? 0.0 : Math.pow(10, currStreak-1);
      }

      /**
       * Iterates over all the columns of the game board.
       */
      for (int j = 0; j < gameState.BOARD_SIZE; j++) {
        currStreak = 0.0;
        for (int i = 0; i < gameState.BOARD_SIZE; i++) {
          if (gameState.at(i, j) == PLAYER_O) {
            currStreak = 0.0;
            break;
          } else if (gameState.at(i, j) == PLAYER_X) {
            currStreak += 1.0;
          }
        }
        totSum += currStreak == 0.0 ? 0.0 : Math.pow(10, currStreak-1);
      }

      /**
       * Traverses the diagonal from (0,0) to (3,3).
       */
      currStreak = 0.0;
      for (int i = 0; i < gameState.BOARD_SIZE; i++) {
        if (gameState.at(i, i) == PLAYER_O) {
          currStreak = 0.0;
          break;
        } else if (gameState.at(i, i) != PLAYER_X) {
          currStreak += 1.0;
        }
      }
      totSum += currStreak == 0.0 ? 0.0 : Math.pow(10, currStreak-1);

      /**
       * Traverses the diagonal from (3,0) to (0,3).
       */
      currStreak = 0.0;
      int j;
      for (int i = gameState.BOARD_SIZE-1; i >= 0; i--) {
        j = gameState.BOARD_SIZE - (1+i);
        if (gameState.at(i, j) == PLAYER_O) {
          currStreak = 0.0;
          break;
        } else if (gameState.at(i, j) == PLAYER_X) {
          currStreak += 1.0;
        }
      }
      totSum += currStreak == 0.0 ? 0.0 : Math.pow(10, currStreak-1);

      return (int)(Math.round(totSum));
    }
}
