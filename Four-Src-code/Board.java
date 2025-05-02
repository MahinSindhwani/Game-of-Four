import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.TransferHandler.TransferSupport;


public class Board {
    private JPanel gridPanel;
    private static final int GRID_SIZE = 9;
    public GamePiece[][] grid;
    public String shapeType = "";
    public Color color;
    private GamePiece selectedPiece; // Currently selected GamePiece
    public static ArrayList<String> tags = new ArrayList<>();
    public static ArrayList<String> names = new ArrayList<>();
    public static ArrayList<Color> colors = new ArrayList<>();
    private boolean currentTurn; // true for Player 1, false for Player 2
    private JLabel turnLabel;
    public String turn;
    public List<GamePiece> playedPieces;

    // Constructor that initializes the grid panel and sets up the grid layout
    public Board() {
        grid = new GamePiece[GRID_SIZE][GRID_SIZE];
        playedPieces = new ArrayList<>();
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        initializeGrid();
        // Initialize turn and turn label
        initializeTurn();
        setupTurnLabel();
        updateTurnLabel();
    }

    // Method to create and set up the turn label
    private void setupTurnLabel() {
        turnLabel = new JLabel();
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        // Add turnLabel to the main panel or frame
    }


    private void initializeTurn() {
        currentTurn = new java.util.Random().nextBoolean();
    }

    // Method to switch the turn
    public void switchTurn() {
        currentTurn = !currentTurn;
    }

    public boolean placePiece(int x, int y, GamePiece piece) {
        if (validPlacement(x, y, piece)) {
            grid[y][x] = piece; // Place the piece on the board
            piece.disableButton(); // Disable the piece's button after placement
            switchTurn(); // Switch the turn after a successful placement
            updateTurnLabel(); // Update the turn label in the UI
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Invalid move. Try again.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false; // Indicate unsuccessful placement
        }
    }

    // Helper method for validating placement
    private boolean validPlacement(int x, int y, GamePiece piece) {
        // Simple validation to check if the spot is empty
        return grid[y][x] == null;
    }

    // Method to update the turn label text
    private void updateTurnLabel() {
        String currentPlayer = currentTurn ? "Player 1" : "Player 2";
        if (currentPlayer == "Player 1") {
            turn = "Player 2";
        } else {
            turn = "Player 1";
        }
        turnLabel.setText("Turn: " + turn);
    }

    // Method to create a button for rotating the selected shape
    public JButton createRotateButton() {
        JButton rotateButton = new JButton("Rotate Shape");
        rotateButton.addActionListener(e -> {
            if (selectedPiece != null) { // Check if a piece is selected
                selectedPiece.rotate(); // Rotate the selected piece
            }
        });
        return rotateButton;
    }

    public String saveBoardState() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                GamePiece piece = grid[i][j];
                if (piece != null) {
                    // Save piece properties: coordinates, color, and shape
                    sb.append("Piece:")
                            .append(i).append(",").append(j).append(",")
                            .append(piece.getColor().getRed()).append(",")
                            .append(piece.getColor().getGreen()).append(",")
                            .append(piece.getColor().getBlue()).append(",")
                            .append(piece.getName()).append(",").append(piece.tag).append("\n");
                }
            }
        }
        return sb.toString();
    }

    // Method to load pieces from a string representation
    public void loadPieces(List<String> pieceDataLines,List<PlayerPanel> playerPanels) {
        System.out.println("Clearing board before loading...");
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = null; // Clear the grid
                // Optionally reset panel backgrounds
                JPanel panel = (JPanel) getGridPanel().getComponent(i * Board.getGridSize() + j);
                panel.setBackground(Color.LIGHT_GRAY);
            }
        }

        System.out.println("Loading piece data...");

        for (String line : pieceDataLines) {
            String[] data = line.split(",");
            int i = Integer.parseInt(data[0]);
            int j = Integer.parseInt(data[1]);
            int red = Integer.parseInt(data[2]);
            int green = Integer.parseInt(data[3]);
            int blue = Integer.parseInt(data[4]);
            String shapeName = data[5];
            String tag = data[6];
            Color color = new Color(red, green, blue);
            this.tags.add(tag);
            this.names.add(shapeName);
            this.colors.add(color);
            
            PlayerPanel playerPanel = playerPanels.stream()
            .filter(panel -> panel.getName().equals(tag))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No matching player panel for tag: " + tag));

            GamePiece piece = new GamePiece(shapeName, color, this, tag,playerPanel); // Create new piece
            grid[i][j] = piece; // Place piece in grid
            // Set the background of the panel for visual representation
            JPanel panel = (JPanel) gridPanel.getComponent(i * GRID_SIZE + j);
            panel.setBackground(color);
        }
    }

    // Method to initialize the grid with panels
    private void initializeGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                panel.setBackground(Color.LIGHT_GRAY);
                panel.setTransferHandler(new ValueImportTransferHandler()); // Set transfer handler for drag-and-drop
                gridPanel.add(panel);
            }
        }
    }

    // Method to set the currently selected piece
    public void setSelectedPiece(GamePiece piece) {
        this.selectedPiece = piece;
    }

    // Method to get the currently selected piece
    public GamePiece getSelectedPiece() {
        return selectedPiece;
    }

    public void storePlayedPieces() {
        playedPieces.add(this.getSelectedPiece());
    }

    public List<GamePiece> moves() {
        return playedPieces;
    }

    // Method to get the grid panel
    public JPanel getGridPanel() {
        return gridPanel;
    }

    // Method to get the size of the grid
    public static int getGridSize() {
        return GRID_SIZE;
    }
    public JLabel getTurnLabel() {
        return turnLabel;
    }

    // Inner class for handling drag-and-drop functionality
    private class ValueImportTransferHandler extends TransferHandler {
            // Check if the data being imported is of the correct flavor (color selection)
            private boolean errorShown = false; // Flag to track if error has been shown
        @Override
        public boolean canImport(TransferSupport support) {
            GamePiece selectedPiece = getSelectedPiece();
        
            if (selectedPiece == null) {
                return false;
            }
            boolean isPlayer1Turn = Board.this.currentTurn;
            if ((isPlayer1Turn && selectedPiece.isPlayer1Piece()) || (!isPlayer1Turn && !selectedPiece.isPlayer1Piece())) {
                errorShown=false;
                return support.isDataFlavorSupported(GamePiece.ColorSelection.COLOR_FLAVOR);
            }
        
           // Show error message only once
        if (!errorShown) {
            JOptionPane.showMessageDialog(null, "Not your turn!", "Error", JOptionPane.ERROR_MESSAGE);
            errorShown = true;
        }

        // Cancel the drag operation immediately
        
        return false;
    
        }

        @Override
        public boolean importData(TransferSupport support) {
            // Import data if the flavor is supported
            if (!canImport(support)) {
                return false; // Return false if unsupported
            }
            try {
                // Retrieve the color and shape type from the transferable data
                Color color = (Color) support.getTransferable()
                        .getTransferData(GamePiece.ColorSelection.COLOR_FLAVOR);
                String shapeType = (String) support.getTransferable()
                        .getTransferData(GamePiece.ColorSelection.SHAPE_TYPE_FLAVOR);

                JPanel targetPanel = (JPanel) support.getComponent(); // Get the target panel for placement
                int gridX = -1, gridY = -1; // Initialize grid coordinates
                Component[] components = targetPanel.getParent().getComponents(); // Get all components from the parent

                // Loop through components to determine the grid position of the target panel
                for (int i = 0; i < components.length; i++) {
                    if (components[i] == targetPanel) {
                        gridY = i / GRID_SIZE; // Calculate grid Y position
                        gridX = i % GRID_SIZE; // Calculate grid X position
                        break; // Break out of the loop once found
                    }
                }
                PiecePlacer piecePlacer = new PiecePlacer(Board.this); // Create a new PiecePlacer instance
                boolean placed = piecePlacer.placeShape(color, shapeType, gridX, gridY, targetPanel);

                if (placed) {
                    Board.this.switchTurn(); // Switch the turn only if the piece is placed successfully
                    Board.this.updateTurnLabel(); // Update turn label after switching
                }
                return placed;
            } catch (Exception e) {
                e.printStackTrace(); // Print stack trace for any exceptions
                return false; // Return false if an error occurs
            }
        }
    }
}
