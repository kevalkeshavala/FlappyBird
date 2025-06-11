import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    private int birdY = 250; // Bird's Y position
    private int velocity = 0; // Bird's velocity
    private final int GRAVITY = 1; // Gravity effect
    private final int JUMP_STRENGTH = -10; // Jumping effect
    private final int PIPE_WIDTH = 100;
    private final int PIPE_GAP = 150;
    private final int PIPE_SPEED = 5;
    
    private ArrayList<int[]> pipes = new ArrayList<>();
    private boolean gameOver = false;
    private int score = 0;
    
    public FlappyBird() {
        JFrame frame = new JFrame("Flappy Bird Clone");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(this);
        frame.setVisible(true);

        Timer timer = new Timer(20, this);
        timer.start();

        addPipe();
    }

    public void addPipe() {
        Random rand = new Random();
        int pipeHeight = rand.nextInt(200) + 100;
        pipes.add(new int[]{800, pipeHeight}); // Pipe starts at right end
    }

    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            // Apply gravity
            velocity += GRAVITY;
            birdY += velocity;

            // Move pipes left
            for (int i = 0; i < pipes.size(); i++) {
                pipes.get(i)[0] -= PIPE_SPEED;
            }

            // Add new pipe when old one moves past
            if (pipes.get(pipes.size() - 1)[0] < 500) {
                addPipe();
            }

            // Remove off-screen pipes
            if (pipes.get(0)[0] < -PIPE_WIDTH) {
                pipes.remove(0);
                score++; // Increment score when a pipe is passed
            }

            // Check collision
            checkCollision();
        }
        repaint();
    }

    public void checkCollision() {
        for (int[] pipe : pipes) {
            int pipeX = pipe[0];
            int pipeHeight = pipe[1];

            // Check collision with pipes
            if ((pipeX < 100 && pipeX + PIPE_WIDTH > 50) &&
                (birdY < pipeHeight || birdY > pipeHeight + PIPE_GAP)) {
                gameOver = true;
            }
        }

        // Check collision with ground
        if (birdY > 550 || birdY < 0) {
            gameOver = true;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Bird
        g.setColor(Color.YELLOW);
        g.fillOval(50, birdY, 30, 30);

        // Pipes
        g.setColor(Color.GREEN);
        for (int[] pipe : pipes) {
            int pipeX = pipe[0];
            int pipeHeight = pipe[1];

            g.fillRect(pipeX, 0, PIPE_WIDTH, pipeHeight);
            g.fillRect(pipeX, pipeHeight + PIPE_GAP, PIPE_WIDTH, 600);
        }

        // Score Display
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 650, 30);

        // Game Over Text
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.setColor(Color.RED);
            g.drawString("Game Over!", 300, 250);
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocity = JUMP_STRENGTH; // Bird jumps
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new FlappyBird();
    }
}
