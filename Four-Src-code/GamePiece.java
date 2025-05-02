import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class GamePiece {
    private String name;
    private int rotation;
    private JButton button;
    private Color color;
    private Board board;
    public String tag;
    private PlayerPanel playerPanel;

    // Constructor for initializing a GamePiece with a name, color, and board
    public GamePiece(String name, Color color, Board board, String tag,PlayerPanel playerPanel) {
        this.board = board;
        this.color = color;
        this.name = name;
        this.rotation = 0;
        this.tag = tag;
        this.playerPanel=playerPanel;
        button = new JButton();
        button.setBackground(Color.WHITE);

        // Set the transfer handler to handle drag-and-drop functionality
        button.setTransferHandler(new ValueExportTransferHandler(color, name));

        // Load and set the icon based on the shape and color
        ImageIcon icon = new ImageIcon(createShapeImage(50, 50, color, this.name));
        Image scaledImage = icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));

        // Mouse listener for handling drag actions on the button
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                board.setSelectedPiece(GamePiece.this);
                JButton button = (JButton) e.getSource();
                TransferHandler handler = button.getTransferHandler();
                handler.exportAsDrag(button, e, TransferHandler.COPY); // Start the drag operation
            }
        });
    }

    // Method to rotate the game piece
    public void rotate() {
        rotation = (rotation + 1) % 4;
        updateIcon(); // Update the icon to reflect the new rotation
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    // Method to update the button icon based on the current shape and color
    private void updateIcon() {
        Color color = this.color;
        ImageIcon icon = new ImageIcon(createShapeImage(50, 50, color, this.name)); // Create new icon
        Image scaledImage = icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH); // Scale the icon
        button.setIcon(new ImageIcon(scaledImage));
    }

    // Method to create an image representing the shape of the piece
    private Image createShapeImage(int width, int height, Color color, String name) {
        // Create a buffered image to draw the shape
        BufferedImage shapeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = shapeImage.createGraphics(); // Get graphics context
        g2d.setColor(color); // Set the drawing color

        // Draw the shape based on the name of the piece
        if (name.equals("L-Shape")) {
            drawLShape(g2d, width, height); // Draw L-Shape
        } else if (name.equals("Rectangle")) {
            drawRectangle(g2d, width, height); // Draw Rectangle
        } else if (name.equals("Big Square")) {
            g2d.fillRect(0, 0, width, height); // Draw Big Square
        } else {
            // Draw default shape (assumed to be a small square)
            g2d.fillRect(0, height / 4, width / 4, height / 4); // Left vertical part
            g2d.fillRect(0, 0, width / 2, height / 2); // Top horizontal part
        }

        g2d.dispose(); // Release the graphics context
        return shapeImage; // Return the created shape image
    }

    // Method to get the current rotation state
    public int getRotation() {
        return this.rotation;
    }

    // Method to draw the L-Shape based on the current rotation
    private void drawLShape(Graphics2D g2d, int width, int height) {
        switch (rotation) {
            case 0: // Original position
                g2d.fillRect(0, height / 2, width / 2, height / 2); // Vertical part
                g2d.fillRect(0, 0, width, height / 2); // Horizontal part
                break;
            case 1: // 90 degrees clockwise
                g2d.fillRect(0, 0, width / 2, height); // Vertical part
                g2d.fillRect(width / 2, height / 2, width / 2, height / 2); // Horizontal part
                break;
            case 2: // 180 degrees
                g2d.fillRect(width / 2, 0, width / 2, height / 2); // Vertical part
                g2d.fillRect(0, height / 2, width, height / 2); // Horizontal part
                break;
            case 3: // 270 degrees
                g2d.fillRect(0, 0, width, height / 2); // Horizontal part
                g2d.fillRect(width / 2, height / 2, width / 2, height); // Vertical part
                break;
        }
    }

    // Method to draw the Rectangle shape based on the current rotation
    private void drawRectangle(Graphics2D g2d, int width, int height) {
        int flag = rotation % 2; // Determine the flag based on rotation
        switch (flag) {
            case 0: // Original position
                g2d.fillRect(0, height / 2, width / 2, height / 2); // Top half
                g2d.fillRect(0, 0, width / 2, height / 2); // Bottom half
                break;
            case 1: // 90 degrees clockwise
                g2d.fillRect(0, 0, width / 2, height / 2); // Top left
                g2d.fillRect(width / 2, 0, width / 2, height / 2); // Top right
                break;
        }
    }

    public String getName() {
        return this.name;
    }
    public boolean isPlayer1Piece() {
        return playerPanel.isPlayer1Panel();
    }

    public Color getColor() {
        return this.color;
    }

    // Method to get the button associated with the GamePiece
    public JButton getButton() {
        return button; // Return the button
    }

    // Method to disable the button (e.g., when the piece is placed)
    public void disableButton() {
        button.setVisible(false); // Hide the button
    }

    // Inner class for handling the transfer of game piece data during drag-and-drop
    private class ValueExportTransferHandler extends TransferHandler {
        private Color color; // Color of the piece
        private String shapeType; // Type of shape

        // Constructor for the transfer handler
        public ValueExportTransferHandler(Color color, String shapeType) {
            this.color = color; // Set color
            this.shapeType = shapeType; // Set shape type
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            return new ColorSelection(color, shapeType); // Create and return a ColorSelection object
        }

        @Override
        public int getSourceActions(JComponent c) {
            return COPY_OR_MOVE; // Define source actions as copy or move
        }
    }

    // Static inner class for defining the transferable color and shape type data
    public static class ColorSelection implements Transferable {
        public static final DataFlavor COLOR_FLAVOR = new DataFlavor(Color.class, "Color");
        public static final DataFlavor SHAPE_TYPE_FLAVOR = new DataFlavor(String.class, "ShapeType");

        private Color color; // Color of the shape
        private String shapeType; // Type of the shape

        // Constructor for ColorSelection
        public ColorSelection(Color color, String shapeType) {
            this.color = color; // Set color
            this.shapeType = shapeType; // Set shape type
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { COLOR_FLAVOR, SHAPE_TYPE_FLAVOR }; // Return supported flavors
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            // Check if the provided flavor is supported
            return flavor.equals(COLOR_FLAVOR) || flavor.equals(SHAPE_TYPE_FLAVOR);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            // Provide the requested data based on the flavor
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor); // Throw exception if unsupported
            }
            if (flavor.equals(COLOR_FLAVOR)) {
                return color; // Return the color
            } else if (flavor.equals(SHAPE_TYPE_FLAVOR)) {
                return shapeType; // Return the shape type
            }
            return null; // Fallback return
        }
    }
}
