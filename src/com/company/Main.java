package com.company;
import javafx.scene.shape.Circle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

public class Main implements ActionListener, MouseListener
{
    public static Main flappyBird;
    public final int WIDTH = 900, HEIGHT = 750;
    public ArrayList<Rectangle> columns;
    public Random random;
    public Rectangle bird;
    public Class renderer;
    //ticks and yMotion used from a reference website
    public int ticks, yMotion;
    public int points;
    public boolean gameStarted;
    public boolean gameEnded;
    public int counter;
    public int columnCount;
    public int pastScore = 0;
    public boolean beepOnCrash = false;



    public Main()
    {
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);
        renderer = new Class();
        random = new Random();
        jframe.add(renderer);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.addMouseListener(this);
        jframe.setResizable(false);
        jframe.setTitle("Flappy Bird Game");
        jframe.setVisible(true);
        bird = new Rectangle(WIDTH/2-15, HEIGHT/2-15, 20, 20);

        columns = new ArrayList<Rectangle>();

        for (int i = 0; i < 10; i++) {
            addColumn(true);
        }

        timer.start();


    }



    public void addColumn(boolean start) {
        int space = 290;
        int width = 100;
        int height = 50 + random.nextInt(290);

        columnCount++;
        if (start)
        {
            //mathematical code and concepts built upon from resources
            columns.add(new Rectangle(WIDTH + width + columns.size()*290, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1)*290, 0, width, HEIGHT - height - space));
        }
        else {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 2).x, 0, width, HEIGHT - height - space));
        }


    }

    public void paintColumn(Graphics g, Rectangle column) {
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);

    }

    public void upMotion() {
        if (gameEnded) {
            bird = new Rectangle(WIDTH/2-15, HEIGHT/2-15, 20, 20);
            columns.clear();
            points = 0;
            yMotion = 0;

            for (int i = 0; i < 10; i++) {
                addColumn(true);
            }
            gameEnded = false;
        }

        if (!gameStarted) {
            gameStarted = true;
        } else if (!gameEnded) {
            if (yMotion > 0) {
                yMotion = 0;
            }
            yMotion -= 10;
        }
    }

    //mathematical calculations for this method built upon resources
    @Override
    public void actionPerformed(ActionEvent e) {
        int speed = 8;
        ticks++;

        if (gameStarted) {
            beepOnCrash = false;
            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                column.x -= speed;

            }
            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }

            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                if (column.x + column.width < 0) {
                    columns.remove(column);

                    //  addColumn(true);
                    //change to false and test
                    if (column.y == 0) {
                        addColumn(true);
                    }

                }

            }
            bird.y += yMotion;

            if (bird.y > HEIGHT - 120 || bird.y < 0) {
                gameEnded = true;
                pastScore = counter;
                columnCount = 0;
                counter = 0;
            }


            for (Rectangle column : columns) {
                if (column.intersects(bird) && !gameEnded) {
                    gameEnded = true;
                    bird.x = column.x - bird.width;
                    pastScore = counter;
                    columnCount = 0;
                    counter = 0;
                } else if (bird.x > column.getMaxX() && !gameEnded) {
                    counter = columnCount - 9;
                }
            }

            if (gameEnded) {
                bird.y = HEIGHT - 120 - bird.height;
                columnCount = 0;
                if (beepOnCrash == false) {
                    for (int i = 0; i <= 6; i++) {
                        Toolkit.getDefaultToolkit().beep();
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }


                    }
                    beepOnCrash = true;
                }

            }


        }



        renderer.repaint();

    }


    public void repaint(Graphics g){

        g.setColor(new Color(184, 221, 245));
        g.fillRect(0,0, WIDTH, HEIGHT);
        g.setColor(new Color(82, 66, 47));
        g.fillRect(0, HEIGHT-120, WIDTH, 120);
        g.setColor(new Color(3, 33, 7));
        g.fillRect(0, HEIGHT-120, WIDTH, 20);
        g.setColor(Color.pink);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        for (Rectangle column : columns) {
            paintColumn(g, column);
        }
        g.setFont(new Font("Arial", 1, 25));
        g.setColor(Color.WHITE);
        g.drawString("Score: " + counter, WIDTH-170, HEIGHT-65);

        g.setFont(new Font("Arial", 1, 80));

        if (!gameStarted) {
            g.setColor(Color.black);
            g.drawString("Click to start game", 75, HEIGHT / 2 - 100);
        }


        if (gameEnded) {
            g.setColor(Color.red.darker());
            g.drawString("Game Over :(", 100, HEIGHT / 2 - 40);
            g.setColor(Color.blue.darker());
            g.setFont(new Font("Arial", 1, 30));
            g.drawString("Your Score: " + pastScore, 105, HEIGHT / 2 );
            g.setColor(Color.black);
            g.setFont(new Font("Arial", 1, 20));
            g.drawString("Click to try again", 105, HEIGHT / 2 + 30);
        }


    }



    @Override
    public void mouseClicked(MouseEvent e) {
        upMotion();
        Toolkit.getDefaultToolkit().beep();



    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public static void main(String[] args) {
        // write your code here
        flappyBird = new Main();
    }

}


