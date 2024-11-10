import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Cube3D extends JPanel implements KeyListener {
    private float[][] vertices = {
            {1, 1, -1}, {1, -1, -1}, {-1, -1, -1}, {-1, 1, -1},
            {1, 1, 1}, {1, -1, 1}, {-1, -1, 1}, {-1, 1, 1}
    };

    private int[][] edges = {
            {0, 1}, {1, 2}, {2, 3}, {3, 0},
            {4, 5}, {5, 6}, {6, 7}, {7, 4},
            {0, 4}, {1, 5}, {2, 6}, {3, 7}
    };

    private int[][] faces = {
            {0, 1, 2, 3}, {3, 2, 6, 7},
            {7, 6, 5, 4}, {4, 5, 1, 0},
            {1, 5, 6, 2}, {4, 0, 3, 7}
    };

    private Color[] colors = {
            Color.RED, Color.GREEN, Color.BLUE,
            Color.MAGENTA, Color.YELLOW, Color.WHITE
    };

    private float[] position = {0, 0, 0};
    private float[] rotation = {0, 0, 0};
    private float[] scaling = {1, 1, 1};

    public Cube3D() {
        JFrame frame = new JFrame("3D Cube");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(this);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(getWidth() / 2, getHeight() / 2);
        g2d.setColor(Color.BLACK);
        drawAxes(g2d);
        drawCube(g2d);
    }

    private void drawAxes(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.drawLine(-200, 0, 200, 0); // X-axis
        g2d.setColor(Color.GREEN);
        g2d.drawLine(0, -200, 0, 200); // Y-axis
        g2d.setColor(Color.BLUE);
        g2d.drawLine(0, 0, 200, 0); // Z-axis (simplified)
    }

    private void drawCube(Graphics2D g2d) {
        for (int[] edge : edges) {
            Point p1 = project(vertices[edge[0]]);
            Point p2 = project(vertices[edge[1]]);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    private Point project(float[] vertex) {
        float[] v = transform(vertex);
        float scale = 500 / (5 + v[2]); // Apply perspective
        return new Point((int) (v[0] * scale), (int) (v[1] * scale));
    }

    private float[] transform(float[] vertex) {
        float[] v = new float[3];
        System.arraycopy(vertex, 0, v, 0, 3);

        // Apply scaling
        for (int i = 0; i < 3; i++) {
            v[i] *= scaling[i];
        }

        // Apply rotation
        v = rotateX(v, rotation[0]);
        v = rotateY(v, rotation[1]);
        v = rotateZ(v, rotation[2]);

        // Apply translation
        v[0] += position[0];
        v[1] += position[1];
        v[2] += position[2];

        return v;
    }

    private float[] rotateX(float[] v, float angle) {
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        return new float[]{v[0], cos * v[1] - sin * v[2], sin * v[1] + cos * v[2]};
    }

    private float[] rotateY(float[] v, float angle) {
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        return new float[]{cos * v[0] + sin * v[2], v[1], -sin * v[0] + cos * v[2]};
    }

    private float[] rotateZ(float[] v, float angle) {
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        return new float[]{cos * v[0] - sin * v[1], sin * v[0] + cos * v[1], v[2]};
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_U -> rotation[0] += 5;
            case KeyEvent.VK_J -> rotation[0] -= 5;
            case KeyEvent.VK_I -> rotation[1] += 5;
            case KeyEvent.VK_K -> rotation[1] -= 5;
            case KeyEvent.VK_O -> rotation[2] += 5;
            case KeyEvent.VK_L -> rotation[2] -= 5;
            case KeyEvent.VK_W -> position[1] += 0.1f;
            case KeyEvent.VK_S -> position[1] -= 0.1f;
            case KeyEvent.VK_A -> position[0] -= 0.1f;
            case KeyEvent.VK_D -> position[0] += 0.1f;
            case KeyEvent.VK_Q -> position[2] += 0.1f;
            case KeyEvent.VK_E -> position[2] -= 0.1f;
            case KeyEvent.VK_R -> scaling[0] += 0.1f;
            case KeyEvent.VK_F -> scaling[0] -= 0.1f;
            case KeyEvent.VK_T -> scaling[1] += 0.1f;
            case KeyEvent.VK_G -> scaling[1] -= 0.1f;
            case KeyEvent.VK_Y -> scaling[2] += 0.1f;
            case KeyEvent.VK_H -> scaling[2] -= 0.1f;
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Cube3D::new);
    }

    private static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
