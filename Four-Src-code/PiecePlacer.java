import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PiecePlacer {
    private Board board;
    private GamePiece selectedPiece;
    private CheckGameConstraint cgc;

    public PiecePlacer(Board board) {
        this.board = board;
        this.cgc = new CheckGameConstraint(board);
    }

    public boolean placeShape(Color color, String shapeType, int x, int y, JPanel targetPanel) {
        selectedPiece = board.getSelectedPiece();

        // Gather all coordinates for the shape
        List<Point> shapeCoordinates = getShapeCoordinates(x, y, shapeType, selectedPiece.getRotation());

        if (cgc.isSamePiece()) {
            JOptionPane.showMessageDialog(null, "Cannot play the same shape or color as last played",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check if any part of the shape is placed orthogonally adjacent to another
        // piece
        if (board.moves().size() > 0 && !cgc.touchesAnotherPiece(shapeCoordinates)) {
            JOptionPane.showMessageDialog(null, "Each piece must touch at least one other piece orthogonally",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check if adjacent pieces have the same type or color
        if (cgc.isAdjacentSimilar(x, y, selectedPiece)) {
            JOptionPane.showMessageDialog(null, "Cannot place a piece adjacent to a piece of the same shape or color",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int rotation = selectedPiece != null ? selectedPiece.getRotation() : 0;
        boolean placedSuccessfully = false;

        switch (shapeType) {
            case "Big Square":
                placedSuccessfully = placeBigSquare(color, x, y, targetPanel);
                if (placedSuccessfully) {
                    updateGridForShape(x, y, selectedPiece, shapeType, rotation);
                }
                break;

            case "Small Square":
                if (targetPanel.getBackground().equals(Color.LIGHT_GRAY)) {
                    targetPanel.setBackground(color);
                    board.grid[y][x] = selectedPiece;
                    placedSuccessfully = true;
                    updateGridForShape(x, y, selectedPiece, shapeType, rotation);
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot place piece there, already occupied with another piece",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "Rectangle":
                placedSuccessfully = placeRectangle(color, x, y, targetPanel, rotation);
                if (placedSuccessfully) {
                    updateGridForShape(x, y, selectedPiece, shapeType, rotation);
                }
                break;

            case "L-Shape":
                placedSuccessfully = placeLShape(color, x, y, targetPanel, rotation);
                if (placedSuccessfully) {
                    updateGridForShape(x, y, selectedPiece, shapeType, rotation);
                }
                break;
        }

        if (placedSuccessfully) {
            board.storePlayedPieces();
            selectedPiece.disableButton();
        }
        return placedSuccessfully;
    }

    // Helper method to get all coordinates occupied by the shape
    private List<Point> getShapeCoordinates(int x, int y, String shapeType, int rotation) {
        List<Point> coordinates = new ArrayList<>();
        coordinates.add(new Point(x, y)); // Add the initial point

        switch (shapeType) {
            case "Big Square":
                coordinates.add(new Point(x + 1, y));
                coordinates.add(new Point(x, y + 1));
                coordinates.add(new Point(x + 1, y + 1));
                break;

            case "Rectangle":
                if (rotation % 2 == 0) { // Vertical
                    coordinates.add(new Point(x, y + 1));
                } else { // Horizontal
                    coordinates.add(new Point(x + 1, y));
                }
                break;

            case "L-Shape":
                switch (rotation) {
                    case 0: // Downward-facing L
                        coordinates.add(new Point(x, y + 1));
                        coordinates.add(new Point(x + 1, y));
                        break;
                    case 1: // Rightward-facing L
                        coordinates.add(new Point(x + 1, y));
                        coordinates.add(new Point(x + 1, y + 1));
                        break;
                    case 2: // Upward-facing L
                        coordinates.add(new Point(x, y - 1));
                        coordinates.add(new Point(x - 1, y));
                        break;
                    case 3: // Leftward-facing L
                        coordinates.add(new Point(x, y - 1));
                        coordinates.add(new Point(x + 1, y - 1));
                        break;
                }
                break;
        }
        return coordinates;
    }

    private void updateGridForShape(int x, int y, GamePiece piece, String shapeType, int rotation) {
        switch (shapeType) {
            case "Big Square":
                board.grid[y][x] = piece;
                board.grid[y][x + 1] = piece;
                board.grid[y + 1][x] = piece;
                board.grid[y + 1][x + 1] = piece;
                break;

            case "Small Square":
                board.grid[y][x] = piece;
                break;

            case "Rectangle":
                if (rotation % 2 == 0) { // Vertical
                    board.grid[y][x] = piece;
                    board.grid[y + 1][x] = piece;
                } else { // Horizontal
                    board.grid[y][x] = piece;
                    board.grid[y][x + 1] = piece;
                }
                break;

            case "L-Shape":
                switch (rotation) {
                    case 0: // Downward-facing L
                        board.grid[y][x] = piece;
                        board.grid[y][x + 1] = piece;
                        board.grid[y + 1][x] = piece;
                        break;
                    case 1: // Rightward-facing L
                        board.grid[y][x] = piece;
                        board.grid[y + 1][x] = piece;
                        board.grid[y][x + 1] = piece;
                        break;
                    case 2: // Upward-facing L
                        board.grid[y][x] = piece;
                        board.grid[y][x - 1] = piece;
                        board.grid[y - 1][x] = piece;
                        break;
                    case 3: // Leftward-facing L
                        board.grid[y][x] = piece;
                        board.grid[y][x - 1] = piece;
                        board.grid[y + 1][x] = piece;
                        break;
                }
                break;
        }
    }

    private boolean placeBigSquare(Color color, int x, int y, JPanel targetPanel) {
        if (x < Board.getGridSize() - 1 && y < Board.getGridSize() - 1) {
            JPanel bottomPanel = (JPanel) targetPanel.getParent().getComponent((y + 1) * Board.getGridSize() + x);
            JPanel rightPanel = (JPanel) targetPanel.getParent().getComponent(y * Board.getGridSize() + (x + 1));
            JPanel bottomRightPanel = (JPanel) targetPanel.getParent()
                    .getComponent((y + 1) * Board.getGridSize() + (x + 1));

            if (!targetPanel.getBackground().equals(Color.LIGHT_GRAY) ||
                    !bottomPanel.getBackground().equals(Color.LIGHT_GRAY) ||
                    !rightPanel.getBackground().equals(Color.LIGHT_GRAY) ||
                    !bottomRightPanel.getBackground().equals(Color.LIGHT_GRAY)) {
                JOptionPane.showMessageDialog(null, "Cannot place piece there, already occupied with another piece",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            targetPanel.setBackground(color);
            bottomPanel.setBackground(color);
            rightPanel.setBackground(color);
            bottomRightPanel.setBackground(color);
            return true;
        }
        return false;
    }

    private boolean placeRectangle(Color color, int x, int y, JPanel targetPanel, int rotation) {
        if (rotation % 2 == 0) {
            if (y < Board.getGridSize() - 1) {
                JPanel bottomPanel = (JPanel) targetPanel.getParent().getComponent((y + 1) * Board.getGridSize() + x);
                if (!targetPanel.getBackground().equals(Color.LIGHT_GRAY) ||
                        !bottomPanel.getBackground().equals(Color.LIGHT_GRAY)) {
                    JOptionPane.showMessageDialog(null, "Cannot place piece there, already occupied with another piece",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                targetPanel.setBackground(color);
                bottomPanel.setBackground(color);
                return true;
            }
        } else if (rotation % 2 == 1) {
            if (x < Board.getGridSize() - 1) {
                JPanel rightPanel = (JPanel) targetPanel.getParent().getComponent(y * Board.getGridSize() + (x + 1));
                if (!targetPanel.getBackground().equals(Color.LIGHT_GRAY) ||
                        !rightPanel.getBackground().equals(Color.LIGHT_GRAY)) {
                    JOptionPane.showMessageDialog(null, "Cannot place piece there, already occupied with another piece",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                targetPanel.setBackground(color);
                rightPanel.setBackground(color);
                return true;
            }
        }
        return false;
    }

    private boolean placeLShape(Color color, int x, int y, JPanel targetPanel, int rotation) {

        switch (rotation) {
            case 0: // Original position
                if (x < Board.getGridSize() - 1 && y < Board.getGridSize() - 1) {
                    JPanel bottomPanel = (JPanel) targetPanel.getParent()
                            .getComponent((y + 1) * Board.getGridSize() + x);
                    JPanel rightPanel = (JPanel) targetPanel.getParent()
                            .getComponent(y * Board.getGridSize() + (x + 1));

                    // Check if the target, bottom, or right panel is not LIGHT_GRAY
                    if (!targetPanel.getBackground().equals(Color.LIGHT_GRAY) ||
                            !bottomPanel.getBackground().equals(Color.LIGHT_GRAY) ||
                            !rightPanel.getBackground().equals(Color.LIGHT_GRAY)) {

                        // Show error dialog if any panel is occupied
                        JOptionPane.showMessageDialog(null,
                                "Cannot place piece there, already occupied with another piece", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    // Place the L-shape piece if all panels are free
                    targetPanel.setBackground(color); // Middle
                    bottomPanel.setBackground(color); // Bottom
                    rightPanel.setBackground(color); // Right
                    return true;
                }
                break;

            case 1: // 90 degrees clockwise
                if (y < Board.getGridSize() - 1 && x < Board.getGridSize() - 1) {
                    JPanel bottomPanel = (JPanel) targetPanel.getParent()
                            .getComponent((y - 1) * Board.getGridSize() + x);
                    JPanel bottomRightPanel = (JPanel) targetPanel
                            .getParent().getComponent((y ) * Board.getGridSize() + (x + 1));

                    // Check if the target, bottom, or bottom-right panel is not LIGHT_GRAY
                    if (!targetPanel.getBackground().equals(Color.LIGHT_GRAY) ||
                            !bottomPanel.getBackground().equals(Color.LIGHT_GRAY) ||
                            !bottomRightPanel.getBackground().equals(Color.LIGHT_GRAY)) {

                        // Show error dialog if any panel is occupied
                        JOptionPane.showMessageDialog(null,
                                "Cannot place piece there, already occupied with another piece", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    // Place the L-shape piece
                    targetPanel.setBackground(color); // Middle
                    bottomPanel.setBackground(color); // Bottom
                    bottomRightPanel.setBackground(color); // Bottom-right
                    return true;
                }
                break;

            case 2: // 180 degrees
                if (x > 0 && y > 0) {
                    JPanel leftPanel = (JPanel) targetPanel.getParent().getComponent(y * Board.getGridSize() + (x - 1));
                    JPanel topPanel = (JPanel) targetPanel.getParent().getComponent((y - 1) * Board.getGridSize() + x);

                    // Check if the target, left, or top panel is not LIGHT_GRAY
                    if (!targetPanel.getBackground().equals(Color.LIGHT_GRAY) ||
                            !leftPanel.getBackground().equals(Color.LIGHT_GRAY) ||
                            !topPanel.getBackground().equals(Color.LIGHT_GRAY)) {

                        // Show error dialog if any panel is occupied
                        JOptionPane.showMessageDialog(null,
                                "Cannot place piece there, already occupied with another piece", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    // Place the L-shape piece
                    targetPanel.setBackground(color); // Middle
                    leftPanel.setBackground(color); // Left
                    topPanel.setBackground(color); // Top
                    return true;
                }
                break;

            case 3: // 270 degrees counter-clockwise
                if (x < Board.getGridSize() - 1 && y < Board.getGridSize() - 1) {
                    JPanel bottomRightPanel = (JPanel) targetPanel
                            .getParent().getComponent((y + 1) * Board.getGridSize() + (x));
                    JPanel rightPanel = (JPanel) targetPanel.getParent()
                            .getComponent(y * Board.getGridSize() + (x - 1));

                    // Check if the target, bottom-right, or right panel is not LIGHT_GRAY
                    if (!targetPanel.getBackground().equals(Color.LIGHT_GRAY) ||
                            !bottomRightPanel.getBackground().equals(Color.LIGHT_GRAY) ||
                            !rightPanel.getBackground().equals(Color.LIGHT_GRAY)) {

                        // Show error dialog if any panel is occupied
                        JOptionPane.showMessageDialog(null,
                                "Cannot place piece there, already occupied with another piece", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    // Place the L-shape piece
                    targetPanel.setBackground(color); // Middle
                    bottomRightPanel.setBackground(color); // Bottom-right
                    rightPanel.setBackground(color); // Right
                    return true;
                }
                break;
        }
        return false; // Return false if the shape couldn't be placed due to grid boundaries
    }
}

