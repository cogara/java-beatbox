import javax.sound.midi.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class BeatBox {

    static JFrame f = new JFrame("My first music video");
    static MyDrawPanel m1;
    public static void main(String[] args) {
        BeatBox bb = new BeatBox();
        bb.play();
    }

    public void setUpGUI() {
        m1 = new MyDrawPanel();
        f.setContentPane(m1);
        f.setBounds(30,30,300,300);
        f.setVisible(true);
    }

    public void play() {
        setUpGUI();
        try {
            Sequencer player = MidiSystem.getSequencer();
            player.open();
            player.addControllerEventListener(m1,new int[] {127});
            Sequence seq = new Sequence(Sequence.PPQ, 4);
            Track track = seq.createTrack();     

            int r = 0;            
            for (int i = 0; i < 600; i+= 10) {
                r = (int) ((Math.random() * 50) + 25);
                track.add(makeEvent(144,1,r,100,i));
                track.add(makeEvent(176,2,127,0,i));
                track.add(makeEvent(128,1,r,100,i+9));
            }

            player.setSequence(seq);
            player.setTempoInBPM(220);
            player.start();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("playing sound");
    }

    private static MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, tick);    
        } catch (Exception e) {}
        return event;
    }


    class MyDrawPanel extends JPanel implements ControllerEventListener {
        boolean msg = false;

        public void controlChange(ShortMessage event) {
            msg = true;
            repaint();
        }
        public void paintComponent(Graphics g) {
            if (msg) {

                int red = (int) (Math.random() * 250);
                int grn = (int) (Math.random() * 250);
                int blu = (int) (Math.random() * 250);

                g.setColor(new Color(red,grn,blu));

                int ht = (int) ((Math.random() * 120) + 10);
                int width = (int) ((Math.random() * 120) + 10);
                int x = (int) ((Math.random() * 40) + 10);
                int y = (int) ((Math.random() * 40) + 10);

                g.fillRect(x, y, width, ht);
                msg = false;
            }
        }
    }

}