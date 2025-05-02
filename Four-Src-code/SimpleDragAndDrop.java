import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import java.util.List;

public class SimpleDragAndDrop {
    private List<Color> colorList; // List to hold available colors for the game pieces
    private List<PlayerPanel> playerPanels;
    private Board board;

    // Constructor that initializes the color list and starts the main GUI setup
    SimpleDragAndDrop(List<Color> list) {

        SimpleDragAndDrop game = this;
        this.board = new Board();
        this.colorList = list;
        main();
    }

    SimpleDragAndDrop() {
        SimpleDragAndDrop game = this;
        this.playerPanels = new ArrayList<>();
        this.board = new Board();

    }

    public void setPanels(List<PlayerPanel> panels) {
        this.playerPanels = panels;
    }

    public void setBoard(Board board) {
        this.board = board;

    }

    public Board getBoard() {
        return this.board;

    }

    public void loader() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Four");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            frame.add(board.getGridPanel(), BorderLayout.CENTER);

            // Create Player 1's panel with the associated board and color list
            PlayerPanel playerOne = this.playerPanels.get(0);
            JPanel panelOne = playerOne.createPanel(); // Create the player panel
            panelOne.add(board.createRotateButton()); // Add a rotate button to Player 1's panel
            frame.add(board.getTurnLabel(), BorderLayout.NORTH);
            frame.add(panelOne, BorderLayout.WEST); // Add Player 1's panel to the left side of the frame

            // Create Player 2's panel in the same manner as Player 1
            PlayerPanel playerTwo = this.playerPanels.get(1);
            JPanel panelTwo = playerTwo.createPanel(); // Create the player panel
            panelTwo.add(board.createRotateButton()); // Add a rotate button to Player 2's panel

            List<PlayerPanel> panels = List.of(playerOne, playerTwo);

            frame.add(panelTwo, BorderLayout.EAST); // Add Player 2's panel to the right side of the frame
            CustomButton saver = new CustomButton("Save", Color.RED); // Correct declaration

            // Add action listener without `@Override`
            saver.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    GameSaver.saveGame("Save.txt", board, panels);
                }
            });

            frame.add(saver, BorderLayout.SOUTH);
            frame.setSize(1000, 1000);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

    }

    public void actionPerformed(ActionEvent e) {
        GamePiece selectedPiece = board.getSelectedPiece();
        // if (selectedPiece != null && board.placePiece(selectedPiece.getX(),
        // selectedPiece.getY(), selectedPiece)) {
        // // Successful placement, UI will be updated by board methods
        // }
    }

    public void main() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Four");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            frame.add(this.board.getGridPanel(), BorderLayout.CENTER);
            //line added by me
            frame.add(board.getTurnLabel(), BorderLayout.NORTH);

            // Create Player 1's panel with the associated board and color list
            PlayerPanel playerOne = new PlayerPanel("Player 1", this.board, colorList, false);
            JPanel panelOne = playerOne.createPanel(); // Create the player panel
            panelOne.add(board.createRotateButton()); // Add a rotate button to Player 1's panel

            frame.add(panelOne, BorderLayout.WEST); // Add Player 1's panel to the left side of the frame

            // Create Player 2's panel in the same manner as Player 1
            PlayerPanel playerTwo = new PlayerPanel("Player 2", this.board, colorList, true);
            JPanel panelTwo = playerTwo.createPanel(); // Create the player panel
            panelTwo.add(board.createRotateButton()); // Add a rotate button to Player 2's panel
            List<PlayerPanel> panels = List.of(playerOne, playerTwo);

            frame.add(panelTwo, BorderLayout.EAST); // Add Player 2's panel to the right side of the frame
            CustomButton saver = new CustomButton("Save", Color.RED); // Correct declaration

            // Add action listener without `@Override`
            saver.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    GameSaver.saveGame("Save.txt", board, panels);
                }
            });

            frame.add(saver, BorderLayout.SOUTH);
            frame.setSize(1000, 1000);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            panelOne.revalidate();
            panelOne.repaint();
            panelTwo.revalidate();
            panelTwo.repaint();

        });
    }
}
