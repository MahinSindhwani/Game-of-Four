import java.io.*;
import java.util.*;
import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameSaver {

    public static void saveGame(String filePath, Board board, List<PlayerPanel> playerPanels) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Save board state
            writer.write("BoardState:\n" + board.saveBoardState()); // Save board pieces
            writer.newLine();

            // Save player panels
            for (PlayerPanel panel : playerPanels) {
                writer.write("PlayerPanel:" + panel.toString());
                writer.newLine();
            }

            System.out.println("Game saved successfully!");

        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    public static void loadGame(String filePath) {
        SimpleDragAndDrop game = new SimpleDragAndDrop();
        List<PlayerPanel> playerPanels = new ArrayList<>();
        Board board = new Board();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            List<String> pieceDataLines = new ArrayList<>();

            // Reading lines from the file and parsing PlayerPanels and pieces
            while ((line = reader.readLine()) != null) {

                if (line.startsWith("Piece:")) {
                    pieceDataLines.add(line.substring("Piece:".length()).trim());
                } else if (line.startsWith("PlayerPanel:")) {
                    String data = line.substring("PlayerPanel:".length()).trim();
                    // Assuming the boolean value isPlayer1 needs to be passed to identify each
                    // player panel
                    boolean isPlayer1 = data.contains("Player 1");
                    PlayerPanel panel = PlayerPanel.fromString(data, board, isPlayer1);
                    playerPanels.add(panel);
                    System.out.println("Added PlayerPanel. Total panels: " + playerPanels.size());
                }
            }

            // Validate that player panels exist
            if (playerPanels.isEmpty()) {
                throw new IllegalStateException("No player panels were loaded from the file");
            }

            // Load the board state and store names, tags, and colors for comparison
            board.loadPieces(pieceDataLines,playerPanels);
            game.setBoard(board);

            // Iterate over each PlayerPanel and create panels for unmatched pieces
            for (PlayerPanel panel : playerPanels) {
                panel.getToolbox().removeAll(); // Clear existing components

                for (Color color : panel.getColors()) {
                    JPanel colorPanel = panel.createColorPanel(color);
                    panel.getToolbox().add(colorPanel);
                }
            }

            // Set up the game UI
            game.setPanels(playerPanels);
            game.loader();
            System.out.println("Game loaded successfully!");

        } catch (IOException e) {
            System.err.println("Error reading game file: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load game: " + e.getMessage(), e);
        }
    }

}
