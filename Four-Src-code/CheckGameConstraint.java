import javax.swing.*;
import java.util.List;
import java.awt.*;

public class CheckGameConstraint {
    private Board board;

    // Constructor to initialize the board
    public CheckGameConstraint(Board board) {
        this.board = board;
    }

    // Method to check if the selected piece is the same as the last played piece
    public boolean isSamePiece() {
        List<GamePiece> moves = board.moves(); // Get played pieces
        if (moves.size() >= 1) { // Check if there is a previous move
            GamePiece lastPlayedPiece = moves.get(moves.size() - 1); // Get the last played piece
            GamePiece selectedPiece = board.getSelectedPiece(); // Get currently selected piece
            // Compare both name and color to check if the selected piece is the same
            if (selectedPiece.getName().equals(lastPlayedPiece.getName()) ||
                    selectedPiece.getColor().equals(lastPlayedPiece.getColor())) {
                return true; // Return true to indicate that the move is invalid
            }
        }
        return false; // Return false if the selected piece is not the same as the last played piece
    }

    // Method to check if the selected piece touches at least one other piece
    // orthogonally
    public boolean touchesAnotherPiece(List<Point> coordinates) {
        GamePiece[][] grid = board.grid;
        int gridSize = Board.getGridSize();

        // Define directions for orthogonal checks (left, right, up, down)
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        for (Point coord : coordinates) {
            int x = coord.x;
            int y = coord.y;

            // Check each orthogonal direction for adjacency
            for (int[] direction : directions) {
                int newX = x + direction[0];
                int newY = y + direction[1];

                // Ensure the new coordinates are within bounds
                if (newX >= 0 && newX < gridSize && newY >= 0 && newY < gridSize) {
                    if (grid[newY][newX] != null) {
                        return true; // At least one cell of the shape touches another piece orthogonally
                    }
                }
            }
        }
        return false; // No cells of the shape touch another piece orthogonally
    }

    // Method to check if adjacent pieces have the same type or color
    public boolean isAdjacentSimilar(int x, int y, GamePiece piece) {
        GamePiece[][] grid = board.grid;
        String shapeType = piece.getName();
        Color color = piece.getColor();

        // Check each orthogonal direction for type or color similarity
        return (x > 0 && grid[y][x - 1] != null &&
                (grid[y][x - 1].getName().equals(shapeType) || grid[y][x - 1].getColor().equals(color))) ||
                (x < Board.getGridSize() - 1 && grid[y][x + 1] != null &&
                        (grid[y][x + 1].getName().equals(shapeType) || grid[y][x + 1].getColor().equals(color)))
                ||
                (y > 0 && grid[y - 1][x] != null &&
                        (grid[y - 1][x].getName().equals(shapeType) || grid[y - 1][x].getColor().equals(color)))
                ||
                (y < Board.getGridSize() - 1 && grid[y + 1][x] != null &&
                        (grid[y + 1][x].getName().equals(shapeType) || grid[y + 1][x].getColor().equals(color)));
    }
}