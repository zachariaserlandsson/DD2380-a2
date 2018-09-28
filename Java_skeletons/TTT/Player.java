import java.util.*;
import java.lang.Math;

public class Player {
  int PLAYER_X = 0;
  int PLAYER_O = 1;

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
        for (GameState g : nextStates) {
          currVal = alphaBetaRec(g, Integer.MIN_VALUE, Integer.MAX_VALUE, PLAYER_X, 0);
          // System.err.println("currVal: " + currVal);
          if (currVal > bestVal) {
            bestVal = currVal;
            bestIndex = i;
            if (currVal == 1) {
              break;
            }
          }
          i++;
        }

        return nextStates.elementAt(bestIndex);

        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */
        // Random random = new Random();
        // return nextStates.elementAt(random.nextInt(nextStates.size()));
    }

    public int alphaBetaRec(GameState gameState, int alpha, int beta, int player, int depth) {
      Vector<GameState> possibleMoves = new Vector<GameState>();
      gameState.findPossibleMoves(possibleMoves);
      // System.err.println("possibleMoves.size(): " + possibleMoves.size());
      int v;

      if (possibleMoves.size() == 0 || depth > 8) {
        // System.err.println("No possible moves!");
        if (gameState.isOWin()) {
          v = 1;
        } else if (gameState.isXWin()) {
          v = -1;
        } else {
          v = 0;
        }
        int printOutput = player == PLAYER_O ? v : -1 * v;
        // System.err.println("alphaBetaRec output: " + printOutput);
        return player == PLAYER_O ? v : -1 * v;
      }

      if (player == PLAYER_X) {
        v = Integer.MIN_VALUE;
        for (GameState g : possibleMoves) {
          v = Math.max(v, alphaBetaRec(g, alpha, beta, PLAYER_O, depth++));
          alpha = Math.max(alpha, v);
          if (alpha >= beta) {
            break;
          }
        }
      } else {
        v = Integer.MAX_VALUE;
        for (GameState g : possibleMoves) {
          v = Math.min(v, alphaBetaRec(g, alpha, beta, PLAYER_X, depth++));
          beta = Math.min(beta, v);
          if (alpha >= beta) {
            break;
          }
        }
      }
      // System.err.println("alphaBetaRec output: " + v);
      return v;
    }
}
