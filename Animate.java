import javax.swing.*;
import java.awt.*;

public class Animate {
    int x = 0;
    int y = 0;
    public static void main(String[] args) {
        Animate gui = new Animate();
        int l = Integer.parseInt(args[0]);
        gui.go(l);
        
    }

    public void go(int z) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        MyDrawPanelAnimate drawPanel = new MyDrawPanelAnimate();

        frame.getContentPane().add(drawPanel);
        frame.setSize(300,300);
        frame.setVisible(true);

        while (true) {
            int random = (int) (Math.random() * z);
            int dirX;
            int dirY;

            if (Math.random() > .5) {
                dirX = 1;
            } else {
                dirX = -1;
            }
            if (Math.random() > .5) {
                dirY = 1;
            } else {
                dirY = -1;
            }
            for (int i = 0; i < random; i++) {
                if (dirX < 0) {
                    x--;
                } else {
                    x++;
                }
                if (dirY < 0) {
                    y--;
                } else {
                    y++;
                }
                if (x < 0) {
                    x = 0;
                }
                if (y < 0) {
                    y = 0;
                }
                if (x > 300) {
                    x = 300;
                }
                if (y > 300) {
                    y = 300;
                }
                drawPanel.repaint();
                try {
                    Thread.sleep(50);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        
    }
    class MyDrawPanelAnimate extends JPanel {
        public void paintComponent(Graphics g) {
            g.setColor(Color.orange);
            g.fillOval(x,y,100,100);
        }
    }
}

