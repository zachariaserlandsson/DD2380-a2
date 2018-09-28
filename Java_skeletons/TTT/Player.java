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
          currVal = alphaBetaRec(g, Integer.MIN_VALUE, Integer.MIN_VALUE, PLAYER_X);
          if (currVal > bestVal) {
            bestVal = currVal;
            bestIndex = i;
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

    public int alphaBetaRec(GameState gameState, int alpha, int beta, int player) {
      Vector<GameState> possibleMoves = new Vector<GameState>();
      gameState.findPossibleMoves(possibleMoves);
      int v = Integer.MIN_VALUE;

      if (possibleMoves.size() == 0) {
        if (gameState.isOWin()) {
          v = 1;
        } else if (gameState.isXWin()) {
          v = -1;
        } else {
          v = 0;
        }
        return player == PLAYER_O ? v : -1 * v;
      }

      if (player == PLAYER_X) {
        for (GameState g : possibleMoves) {
          v = Math.max(v, alphaBetaRec(g, alpha, beta, PLAYER_O));
          alpha = Math.max(alpha, v);
          if (alpha >= beta) {
            break;
          }
        }
      } else if (player == PLAYER_O) {
        for (GameState g : possibleMoves) {
          v = Math.min(v, alphaBetaRec(g, alpha, beta, PLAYER_X));
          beta = Math.min(beta, v);
          if (alpha >= beta) {
            break;
          }
        }
      } return v;
    }
}
