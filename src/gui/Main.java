package gui;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setMinimumSize(new Dimension(650, 800));
        frame.setLocationRelativeTo(null);

        Board board = new Board();
        frame.add(board);
        frame.setVisible(true);




    }


}
