/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import gui.PuzzleGameFrm;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author DELL
 */
public class PuzzleGameController {

    PuzzleGameFrm pgf = new PuzzleGameFrm();
    int margin = 10;
    int size = 0,sizeBtn=60;
    JButton[][] matrixButton;
    int move = 0, time = 0;
    Timer clock;

    public PuzzleGameController() {

        playNewGame();
        JButton btnNewGame = pgf.getBtnNewGame();
        btnNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGameBtn();

            }
        });
        pgf.setVisible(true);
    }

    public void addButton() {
        size = pgf.getCboSize().getSelectedIndex() + 3;
        pgf.getPanel().removeAll();
        pgf.getPanel().setLayout(new GridLayout(size, size, margin, margin));
        pgf.getPanel().setPreferredSize(new Dimension(size * sizeBtn+(size-1)*margin, size * sizeBtn+(size-1)*margin));
        
        matrixButton = new JButton[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JButton btn = new JButton(i * size + j + 1 + "");
                btn.setSize(sizeBtn, sizeBtn);
                matrixButton[i][j] = btn;
                pgf.getPanel().add(btn);
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (checkMove(btn)) {
                            moveButton(btn);
                            if (checkWin()) {
                                clock.stop();
                                JOptionPane.showMessageDialog(null, "Win");
                                newGameBtn();


                            }
                        }
                    }
                });
            }
        }
        matrixButton[size - 1][size - 1].setText("");
        shulfeButton();
        pgf.pack();
    }

    public Point getEmptyPos() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrixButton[i][j].getText().equals("")) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    public void shulfeButton() {
        for (int i = 0; i < 1000; i++) {
            Point p = getEmptyPos();
            Random rd = new Random();
            int choice = rd.nextInt(4);

            switch (choice) {
                case 0: {//up
                    if (p.x > 0) {
                        String txt = matrixButton[p.x - 1][p.y].getText();
                        matrixButton[p.x][p.y].setText(txt);
                        matrixButton[p.x - 1][p.y].setText("");
                    }
                    break;
                }
                case 1: {//down
                    if (p.x < size - 1) {
                        String txt = matrixButton[p.x + 1][p.y].getText();
                        matrixButton[p.x][p.y].setText(txt);
                        matrixButton[p.x + 1][p.y].setText("");
                    }
                    break;
                }
                case 2: {//left
                    if (p.y > 0) {
                        String txt = matrixButton[p.x][p.y - 1].getText();
                        matrixButton[p.x][p.y].setText(txt);
                        matrixButton[p.x][p.y - 1].setText("");
                    }
                    break;
                }
                case 3: {//right;
                    if (p.y < size - 1) {
                        String txt = matrixButton[p.x][p.y + 1].getText();
                        matrixButton[p.x][p.y].setText(txt);
                        matrixButton[p.x][p.y + 1].setText("");
                    }
                    break;
                }
            }
        }
    }

    public boolean checkMove(JButton btn) {
        Point p = getEmptyPos();
        int getRow = 0;
        int getCol = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (btn.getText().equals(matrixButton[i][j].getText())) {
                    getRow = i;
                    getCol = j;
                    break;
                }
            }
        }
        if (p.x == getRow && Math.abs(p.y - getCol) == 1) {
            return true;
        }
        if (p.y == getCol && Math.abs(p.x - getRow) == 1) {
            return true;
        }
        return false;
    }

    public void moveButton(JButton btn) {
        Point p = getEmptyPos();
        String txt = btn.getText();
        matrixButton[p.x][p.y].setText(txt);
        btn.setText("");
        move++;
        pgf.getLblMoveCount().setText(move + "");
    }

    public boolean checkWin() {
        if (matrixButton[size - 1][size - 1].getText().equals("")) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (i == size - 1 && j == size - 1) {
                        return true;
                    }
                    if (!matrixButton[i][j].getText().equals(i * size + j + 1 + "")) {
                        return false;

                    }
                }
            }
            return true;
        }
        return false;
    }

    void initTimer() {
        pgf.getLblTime().setText(time + " sec");
        clock = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                time++;
                pgf.getLblTime().setText(time + " sec");

            }
        });
        clock.start();
    }

    public void playNewGame() {
        time = 0;
        initTimer();
        addButton();
        move = 0;
        pgf.getLblMoveCount().setText(move + "");
    }

    public void newGameBtn() {
        clock.stop();
        int output = JOptionPane.showConfirmDialog(pgf, "Do you want to phay new game", "Notification", JOptionPane.YES_NO_OPTION);

        switch (output) {
            case JOptionPane.YES_OPTION: {
                playNewGame();
                break;
            }
            case JOptionPane.NO_OPTION: {
                pgf.getCboSize().setSelectedIndex(size - 3);
                if (checkWin()) {
                    clock.stop();
                } else {
                    clock.start();
                }

                return;
            }
            default:{
                pgf.getCboSize().setSelectedIndex(size - 3);
                 if (checkWin()) {
                    clock.stop();
                } else {
                    clock.start();
                }
            }
        }
    }
}
