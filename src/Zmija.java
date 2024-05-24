import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Zmija extends JPanel {
    private static final int VELICINA_BLOKA = 20;
    private static final int POCETNA_SIRINA = 800;
    private static final int POCETNA_VISINA = 600;

    private Point glava;
    private Point hrana;
    private char smjer;
    private boolean krajIgre;
    private Timer timer;
    private int bodovi;

    public Zmija() {
        setPreferredSize(new Dimension(POCETNA_SIRINA, POCETNA_VISINA));
        setBackground(Color.BLACK);

        glava = new Point(10, 10);
        smjer = 'D';
        krajIgre = false;
        bodovi = 0;

        hrana = generisiHranu();

        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!krajIgre) {
                    pomjeriZmiju();
                    repaint();
                }
            }
        });

        timer.start();

        // Postavljamo key binding za strelice
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUp");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");

        getActionMap().put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (smjer != 'D') {
                    smjer = 'U';
                }
            }
        });

        getActionMap().put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (smjer != 'U') {
                    smjer = 'D';
                }
            }
        });

        getActionMap().put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (smjer != 'R') {
                    smjer = 'L';
                }
            }
        });

        getActionMap().put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (smjer != 'L') {
                    smjer = 'R';
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GREEN);
        g.fillRect(glava.x * VELICINA_BLOKA, glava.y * VELICINA_BLOKA, VELICINA_BLOKA, VELICINA_BLOKA);

        g.setColor(Color.RED);
        g.fillRect(hrana.x * VELICINA_BLOKA, hrana.y * VELICINA_BLOKA, VELICINA_BLOKA, VELICINA_BLOKA);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Bodovi: " + bodovi, 10, 20);

        if (krajIgre) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Over!", POCETNA_SIRINA / 2 - 100, POCETNA_VISINA / 2);
        }
    }

    private void pomjeriZmiju() {
        Point novaGlava = null;

        switch (smjer) {
            case 'L':
                novaGlava = new Point(glava.x - 1, glava.y);
                break;
            case 'R':
                novaGlava = new Point(glava.x + 1, glava.y);
                break;
            case 'U':
                novaGlava = new Point(glava.x, glava.y - 1);
                break;
            case 'D':
                novaGlava = new Point(glava.x, glava.y + 1);
                break;
        }

        if (novaGlava != null && !izvanGranica(novaGlava)) {
            glava = novaGlava;
            if (novaGlava.equals(hrana)) {
                hrana = generisiHranu();
                bodovi++; // Povećavamo broj bodova kad zmija pojede hranu
            }
        } else {
            krajIgre = true;
            timer.stop();
        }
    }

    private boolean izvanGranica(Point point) {
        return (point.x < 0 || point.y < 0 || point.x >= getWidth() / VELICINA_BLOKA || point.y >= getHeight() / VELICINA_BLOKA);
    }

    private Point generisiHranu() {
        int sirina = Math.max(getWidth() / VELICINA_BLOKA, 2);
        int visina = Math.max(getHeight() / VELICINA_BLOKA, 2);
        Random random = new Random();
        int x = random.nextInt(sirina - 1) + 1;
        int y = random.nextInt(visina - 1) + 1;
        return new Point(x, y);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Zmija");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Zmija zmijaPanel = new Zmija();
                frame.getContentPane().add(zmijaPanel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
