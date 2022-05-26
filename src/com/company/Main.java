package com.company;

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
    public int ticks = 0, yMotion;
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

        //add 10 columns at the beginning of the game
        for (int i = 0; i < 10; i++) {
            addColumn();
        }

        timer.start();
    }

    //method for adding columns in the game
    public void addColumn() {

        //dimensions for the columns
        int space = 290;
        int width = 100;

        //generating random column heights for variation
        int height = 50 + random.nextInt(290);

        columnCount++;

        //mathematical code and concepts built upon from resources
        //adds a top and bottom paired column for the dimensions given above with slight modifications for each iteration
        columns.add(new Rectangle(WIDTH + width + columns.size()*290, HEIGHT - height - 120, width, height));
        columns.add(new Rectangle(WIDTH + width + (columns.size() - 1)*290, 0, width, HEIGHT - height - space));
    }

    //method for painting the columns green
    public void paintColumn(Graphics g, Rectangle column) {
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    //method for moving the bird up with each mouse click
    public void upMotion() {
        //when the bird reaches the ground
        if (gameEnded) {
            //place the bird at the ground (vertically below it's crashing position)
            bird = new Rectangle(WIDTH/2-15, HEIGHT/2-15, 20, 20);
            columns.clear();
            points = 0;
            yMotion = 0;

            for (int i = 0; i < 10; i++) {
                addColumn();
            }

            gameEnded = false;
        }

        //if the user has still not clicked to start the game
        if (!gameStarted) {
            gameStarted = true;
        } else if (!gameEnded) {

            // if the bird moves up
            if (yMotion > 0) {
                yMotion = 0;
            }
            //make the bird go down (until the mouse is clicked to go up again)
            yMotion -= 10;
        }
    }

    //mathematical calculations for this method built upon resources
    @Override
    public void actionPerformed(ActionEvent e) {
        //speed of the bird (higher number correlates to higher speed)
        int speed = 8;
        ticks++;

        if (gameStarted) {

            //do not make a beeping noise initially
            beepOnCrash = false;

            //move the columns to the left of the screen incrementally
            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                column.x -= speed;
            }

            //increment yMotion for every two iterations
            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }

            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);

                //if the column passes beyond the left side of the screen, remove it from array
                if (column.x + column.width < 0) {
                    columns.remove(column);

                    //if column passes the left side of the screen, add another column to the array
                    addColumn();
                }
            }

            bird.y += yMotion;

            //the game ends if the bird hits the soil or the top of the screen
            if (bird.y > HEIGHT - 120 || bird.y < 0) {
                gameEnded = true;

                pastScore = counter;
                columnCount = 0;
                counter = 0;
            }

            //end the game if the bird touches the column
            //score is equivalent to the number of columns in the array - 9
            //for reference, the array initially has 10, so if the bird passes one column, then the score is 10-9 = 1
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

            //if the game ends, bring the bird down to the ground and beep 7 times
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

        //painting the sky
        g.setColor(new Color(184, 221, 245));
        g.fillRect(0,0, WIDTH, HEIGHT);

        //painting the grass
        g.setColor(new Color(82, 66, 47));
        g.fillRect(0, HEIGHT-120, WIDTH, 120);

        //painting the soil
        g.setColor(new Color(3, 33, 7));
        g.fillRect(0, HEIGHT-120, WIDTH, 20);

        //painting the bird
        g.setColor(Color.pink);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        //painting all columns
        for (Rectangle column : columns) {
            paintColumn(g, column);
        }

        //display the score during the game
        g.setFont(new Font("Arial", 1, 25));
        g.setColor(Color.WHITE);
        g.drawString("Score: " + counter, WIDTH-170, HEIGHT-65);

        //setting future text font size to be bigger
        g.setFont(new Font("Arial", 1, 80));

        //when the game has still not started
        if (!gameStarted) {
            g.setColor(Color.black);
            //prompt user to click
            g.drawString("Click to start game", 75, HEIGHT / 2 - 100);
        }

        //when game ends due to bird crashing, print out "game over" and the score
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

    //when the user clicks the mouse, make the bird go up and beep at the same time
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


