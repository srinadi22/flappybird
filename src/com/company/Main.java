package com.company;
import javafx.scene.shape.Circle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class Main implements ActionListener
{
    public static Main flappyBird;
    public final int WIDTH = 1200, HEIGHT = 800;

    public Class renderer;

    public Rectangle bird;

    public int ticks, yMotion;

    public ArrayList<Rectangle> columns;

    public Random rand;

    public Main()
    {
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);
        renderer = new Class();
        rand = new Random();
        jframe.add(renderer);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.setResizable(false);
        jframe.setTitle("Flappy Bird Game");
        jframe.setVisible(true);
        bird = new Rectangle(WIDTH/2-10, HEIGHT/2-10, 20, 20);

        columns = new ArrayList<Rectangle>();



        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);


        timer.start();


    }

    public void addColumn(boolean start) {
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);

        if (start)
        {
            columns.add(new Rectangle(WIDTH + width + columns.size()*300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1)*300, 0, width, HEIGHT - height - space));
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

    @Override
    public void actionPerformed(ActionEvent e) {
        int speed = 9;
        ticks++;

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

        /*for (Rectangle column : columns ) {
            if (column.intersects(bird)) {
               // gameOver = true;
            }
        }

         */

        renderer.repaint();

    }



    public void repaint(Graphics g){
        g.setColor(Color.blue);
        g.fillRect(0,0, WIDTH, HEIGHT);
        g.setColor(Color.ORANGE);
        g.fillRect(0, HEIGHT-120, WIDTH, 120);
        g.setColor(Color.green);
        g.fillRect(0, HEIGHT-120, WIDTH, 20);
        g.setColor(Color.pink);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);
        for (Rectangle column : columns) {
            paintColumn(g, column);
        }

    }

    public static void main(String[] args) {
	// write your code here
        flappyBird = new Main();
    }



}


