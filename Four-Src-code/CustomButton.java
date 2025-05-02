import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class CustomButton extends JButton {

    private Color buttonColor;
    private String buttonText;

    public CustomButton(String text, Color color) {
        this.buttonText = text;
        this.buttonColor = color;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setPreferredSize(new Dimension(150, 50));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(buttonColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        g2.setColor(getForeground());
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(buttonText)) / 2;
        int y = (getHeight() + fm.getHeight()) / 2 - fm.getDescent();
        g2.drawString(buttonText, x, y);

        super.paintComponent(g);
    }

    public void setButtonColor(Color color) {
        this.buttonColor = color;
        repaint();
    }

    public void setButtonText(String text) {
        this.buttonText = text;
        repaint();
    }

    public Color getButtonColor() {
        return this.buttonColor;
    } // AAAAAAAAAAAA
}
