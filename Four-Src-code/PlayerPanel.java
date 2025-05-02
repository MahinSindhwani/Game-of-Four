import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerPanel {
    private JPanel toolbox;
    private String name;
    private Board board;
    private List<Color> colors;
    private boolean isPlayer1;

    public PlayerPanel(String name, Board board, List<Color> list, boolean isPlayer1) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }

        this.name = name;
        this.board = board;
        this.colors = list;
        this.toolbox = new JPanel();
        this.isPlayer1 = isPlayer1;
        createNameLabel();
        // Initialize the panel immediately in constructor
        initializePanel();
    }

    public List<Color> getColors() {
        return colors;
    }

    public boolean isPlayer1Panel() {
        return isPlayer1;
    }

    private void initializePanel() {
        toolbox.setLayout(new GridLayout(6, 1));

        // Create a label to display the player's name

        // Retrieve colors from the list for use in game pieces
        Color color_1 = (Color) colors.get(0);
        Color color_2 = (Color) colors.get(1);
        Color color_3 = (Color) colors.get(2);
        Color color_4 = (Color) colors.get(3);

        // Create panels for different colors of game pieces
        JPanel containerPanel = createColorPanel(color_1);
        JPanel containerPanel1 = createColorPanel(color_2);
        JPanel containerPanel2 = createColorPanel(color_3);
        JPanel containerPanel3 = createColorPanel(color_4);

        // Add all container panels to the toolbox
        toolbox.add(containerPanel);
        toolbox.add(containerPanel1);
        toolbox.add(containerPanel2);
        toolbox.add(containerPanel3);

        toolbox.revalidate();
        toolbox.repaint();
    }

    public void createNameLabel() {
        JLabel playerLabel = new JLabel(name, SwingConstants.CENTER);
        playerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        playerLabel.setPreferredSize(new Dimension(20, 4));
        toolbox.add(playerLabel);
    }

    public JPanel createColorPanel(Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        String[] shapes = { "Big Square", "Small Square", "Rectangle", "L-Shape" };

        for (String shape : shapes) {
            boolean matchFound = false;

            // Check if the current shape, name, and color match any existing data in the
            // board
            for (int i = 0; i < Board.names.size(); i++) {
                if (Board.names.get(i).equals(shape) &&
                        Board.tags.get(i).equals(this.name) &&
                        Board.colors.get(i).equals(color)) {
                    matchFound = true;
                    break; // Exit the loop as soon as a match is found
                }
            }

            // Only add the button if no match was found
            if (!matchFound) {
                GamePiece piece = new GamePiece(shape, color, this.board, this.name,this);
                JButton button = piece.getButton();
                button.setActionCommand(shape); // Set the action command to the shape name for identification
                panel.add(button);
            }
        }

        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return panel;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(";");

        for (Object colorObj : colors) {
            Color color = (Color) colorObj;
            sb.append(color.getRed()).append(",")
                    .append(color.getGreen()).append(",")
                    .append(color.getBlue()).append(";");
        }

        return sb.toString();
    }

    public static PlayerPanel fromString(String data, Board board, boolean isPlayer1) {

        String[] parts = data.split(";");
        String name = parts[0];
        System.out.println(name);
        List<Color> colorList = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            if (parts[i].isEmpty())
                continue;
            String[] rgb = parts[i].split(",");
            int r = Integer.parseInt(rgb[0]);
            int g = Integer.parseInt(rgb[1]);
            int b = Integer.parseInt(rgb[2]);
            colorList.add(new Color(r, g, b));
        }

        return new PlayerPanel(name, board, colorList, isPlayer1);
    }

    // Modified to return the existing panel instead of creating a new one
    public JPanel createPanel() {
        return toolbox;
    }

    public JPanel getToolbox() {
        return toolbox;
    }

    public String getName() {
        return this.name;
    }
}