package com.company;
import javax.swing.JPanel;
import java.awt.*;

public class Class extends JPanel
{
    private static final long serialVersionUID = 1L;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Main.flappyBird.repaint(g);
    }
}
