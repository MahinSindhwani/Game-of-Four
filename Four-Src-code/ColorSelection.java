import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ColorSelection extends JFrame {
    private List<Color> selectedColors; // Store selected colors

    public ColorSelection() {
        // Initialize the selectedColors list
        selectedColors = new ArrayList<>();

        // Set the title of the window
        setTitle("Button Grid with Toggle Bar");

        // Set the default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600); // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
        setLocationRelativeTo(null); // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

        // Set the layout to BorderLayout
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        JTextArea textArea = new JTextArea("SELECT 4 COLORS");
        textArea.setFont(new Font("Serif", Font.PLAIN, 22));
        textArea.setEditable(false);
        panel.add(textArea);
        add(panel, BorderLayout.NORTH);

        // Create a panel with a grid layout for 16 buttons (4x4 grid)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4, 10, 10)); // 4x4 grid with gaps of 10px

        // Create a list of colors
        List<Color> colors = new ArrayList<>();
        colors.add(Color.RED); // RGB: (255, 0, 0)
        colors.add(Color.GREEN); // RGB: (0, 255, 0)
        colors.add(Color.BLUE); // RGB: (0, 0, 255)
        colors.add(new Color(255, 165, 0)); // Orange - RGB: (255, 165, 0)
        colors.add(new Color(255, 255, 0)); // Yellow - RGB: (255, 255, 0)
        colors.add(new Color(0, 255, 255)); // Cyan - RGB: (0, 255, 255)
        colors.add(new Color(255, 0, 255)); // Magenta - RGB: (255, 0, 255)
        colors.add(new Color(128, 0, 0)); // Maroon - RGB: (128, 0, 0)
        colors.add(new Color(0, 128, 128)); // Teal - RGB: (0, 128, 128)
        colors.add(new Color(0, 0, 128)); // Navy - RGB: (0, 0, 128)
        colors.add(new Color(240, 128, 128)); // Salmon - RGB: (240, 128, 128)
        colors.add(new Color(135, 206, 250)); // Sky Blue - RGB: (135, 206, 250)
        colors.add(new Color(255, 20, 147)); // Deep Pink - RGB: (255, 20, 147)
        colors.add(new Color(154, 205, 50)); // Yellow Green - RGB: (154, 205, 50)
        colors.add(new Color(255, 105, 180)); // Hot Pink - RGB: (255, 105, 180)
        colors.add(new Color(0, 0, 139)); // Dark Blue - RGB: (0, 0, 139)

        // Add 16 buttons to the panel, cycling through the colors
        for (int i = 0; i < 16; i++) {
            Color color = colors.get(i % colors.size()); // Cycle through colors
            CustomButton button = new CustomButton("", color);

            // Add ActionListener to each button
            button.addActionListener(e -> {
                // Add the button's color to the selectedColors list
                int size = selectedColors.size();
                if (size < 4) {
                    selectedColors.add(button.getButtonColor());
                    button.setBorder(new LineBorder(Color.white, 3));
                    button.setBorderPainted(true);
                    System.out.println("Color added: " + button.getButtonColor());
                }

                // Check if 4 colors have been selected
                if (selectedColors.size() == 4) {
                    setVisible(false);
                    startGame();
                }

            });
            buttonPanel.setBackground(Color.BLACK);
            buttonPanel.add(button);
        }

        buttonPanel.setBackground(Color.BLACK);

        // Create a panel for the toggle button
        JPanel togglePanel = new JPanel();
        togglePanel.setLayout(new FlowLayout());

        // Create a toggle button and add it to the panel
        JSlider scroller = new JSlider();
        togglePanel.add(scroller);

        // Add the button panel to the center of the frame
        add(buttonPanel, BorderLayout.CENTER);

        // Add the toggle button panel to the bottom of the frame
        // add(togglePanel, BorderLayout.SOUTH);

        // Set the size of the frame
        setSize(900, 600);

        // Make the frame visible
        setVisible(true);
    }

    // Method to start the game
    private void startGame() {
        SimpleDragAndDrop gameOne = new SimpleDragAndDrop(selectedColors);
    }

}
