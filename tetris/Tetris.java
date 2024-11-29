import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Tetris extends JPanel implements ActionListener {
    private final int WIDTH = 10, HEIGHT = 20;
    private final int BLOCK_SIZE = 30;
    private Timer timer;
    private boolean[][] grid;
    private Tetromino currentTetromino;

    public Tetris() {
        // Configura o painel do jogo
        setPreferredSize(new Dimension(WIDTH * BLOCK_SIZE, HEIGHT * BLOCK_SIZE));
        setBackground(Color.BLACK);
        grid = new boolean[WIDTH][HEIGHT];
        timer = new Timer(500, this);
        timer.start();

        // Configura o controle do teclado
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT: moveTetromino(-1, 0); break;
                    case KeyEvent.VK_RIGHT: moveTetromino(1, 0); break;
                    case KeyEvent.VK_DOWN: moveTetromino(0, 1); break;
                    case KeyEvent.VK_UP: rotateTetromino(); break;
                    case KeyEvent.VK_SPACE: dropTetromino(); break;
                }
                repaint();
            }
        });
        setFocusable(true);
        spawnTetromino();
    }

    private void spawnTetromino() {
        // Gera uma nova peça aleatória
        currentTetromino = Tetromino.getRandomTetromino();
    }

    // Função para verificar se a peça pode mover-se
    private boolean canMove(int dx, int dy) {
        for (Point p : currentTetromino.shape) {
            int newX = currentTetromino.x + p.x + dx;
            int newY = currentTetromino.y + p.y + dy;
            if (newX < 0 || newX >= WIDTH || newY >= HEIGHT || (newY >= 0 && grid[newX][newY])) {
                return false;
            }
        }
        return true;
    }

    private void moveTetromino(int dx, int dy) {
        if (canMove(dx, dy)) {
            currentTetromino.x += dx;
            currentTetromino.y += dy;
        } else if (dy > 0) {
            lockTetromino();
            spawnTetromino();
            if (!canMove(0, 0)) {
                JOptionPane.showMessageDialog(this, "Game Over!");
                System.exit(0);
            }
        }
    }

    private void dropTetromino() {
        while (canMove(0, 1)) {
            currentTetromino.y++;
        }
        lockTetromino();
        spawnTetromino();
    }

    private void rotateTetromino() {
        Tetromino rotated = currentTetromino.rotate();
        if (canMove(0, 0)) {
            currentTetromino = rotated;
        } else {
            attemptMoveIfBlocked(rotated);
        }
    }

    private void attemptMoveIfBlocked(Tetromino rotated) {
        if (canMove(-1, 0)) {
            currentTetromino = rotated;
            currentTetromino.x--;
        } else if (canMove(1, 0)) {
            currentTetromino = rotated;
            currentTetromino.x++;
        } else if (canMove(0, -1)) {
            currentTetromino = rotated;
            currentTetromino.y--;
        }
    }

    private void lockTetromino() {
        for (Point p : currentTetromino.shape) {
            if (currentTetromino.y + p.y >= 0) {
                grid[currentTetromino.x + p.x][currentTetromino.y + p.y] = true;
            }
        }
        clearLines();
    }

    private void clearLines() {
        for (int y = HEIGHT - 1; y >= 0; y--) {
            if (isLineFull(y)) {
                removeLine(y);
                y++; // Recheck this line after removal
            }
        }
    }

    private boolean isLineFull(int y) {
        for (int x = 0; x < WIDTH; x++) {
            if (!grid[x][y]) return false;
        }
        return true;
    }

    private void removeLine(int row) {
        if (row < 0 || row >= HEIGHT) {
            return; // Verifique se a linha está dentro dos limites
        }

        // Mova todas as linhas acima da linha removida para baixo
        for (int r = row; r > 0; r--) {
            for (int c = 0; c < WIDTH; c++) {
                grid[c][r] = grid[c][r - 1];
            }
        }

        // Limpe a primeira linha
        for (int c = 0; c < WIDTH; c++) {
            grid[c][0] = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveTetromino(0, 1);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawGhostTetromino(g);
        drawTetromino(g, currentTetromino);
        drawLines(g);
    }

    private void drawGrid(Graphics g) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (grid[x][y]) {
                    g.setColor(Color.WHITE);
                    g.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }

    private void drawGhostTetromino(Graphics g) {
        Tetromino ghost = new Tetromino(currentTetromino.shape, new Color(255, 255, 255, 50));
        ghost.x = currentTetromino.x;
        ghost.y = currentTetromino.y;

        while (canMoveTetromino(ghost, 0, 1)) {
            ghost.y++;
        }

        g.setColor(ghost.color);
        for (Point p : ghost.shape) {
            g.fillRect((ghost.x + p.x) * BLOCK_SIZE, (ghost.y + p.y) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        }
    }

    private boolean canMoveTetromino(Tetromino tetromino, int dx, int dy) {
        for (Point p : tetromino.shape) {
            int newX = tetromino.x + p.x + dx;
            int newY = tetromino.y + p.y + dy;
            if (newX < 0 || newX >= WIDTH || newY >= HEIGHT || (newY >= 0 && grid[newX][newY])) {
                return false;
            }
        }
        return true;
    }

    private void drawTetromino(Graphics g, Tetromino tetromino) {
        g.setColor(tetromino.color);
        for (Point p : tetromino.shape) {
            g.fillRect((tetromino.x + p.x) * BLOCK_SIZE, (tetromino.y + p.y) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        }
    }

    private void drawLines(Graphics g) {
        g.setColor(Color.GRAY);
        for (int i = 0; i < WIDTH; i++) {
            g.drawLine(i * BLOCK_SIZE, 0, i * BLOCK_SIZE, HEIGHT * BLOCK_SIZE);
        }
        for (int j = 0; j < HEIGHT; j++) {
            g.drawLine(0, j * BLOCK_SIZE, WIDTH * BLOCK_SIZE, j * BLOCK_SIZE);
        }
    }
}
