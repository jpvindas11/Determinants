package main;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Determinants");

        Game game = new Game();
        window.add(game);

        window.setSize(1440, 810);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.pack();

        game.start_thread();
    }
}
