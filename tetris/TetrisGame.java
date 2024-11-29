import javax.swing.*;

public class TetrisGame {
    public static void main(String[] args) {
        // Cria a janela principal do jogo
        JFrame frame = new JFrame("Tetris");
        // Cria o painel do jogo e adiciona à janela
        Tetris tetris = new Tetris();
        frame.add(tetris);
        // Define as configurações da janela
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
