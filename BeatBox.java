import java.awt.*;
import javax.swing.*;
import javax.sound.midi.*;
import java.util.*;
import java.awt.event.*;

public class BeatBox {

    // Set up panels and frames - GUI components and 
    // MIDI components
    JPanel mainPanel;
    JFrame theFrame;
    ArrayList<JCheckBox> checkboxList;
    Sequencer player;
    Sequence seq;
    Track track;

    // Instrument names and MIDI codes
    String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", 
       "Open Hi-Hat","Acoustic Snare", "Crash Cymbal", "Hand Clap", 
       "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga", 
       "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo", 
       "Open Hi Conga"};
    int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};


    // Start Program
    public static void main (String[] args) {
        new BeatBox().buildGUI();
    }


    public void buildGUI() {
        // set up GUI Frame.
        theFrame = new JFrame("BeatBox");
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // create inner panel for layout options
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // list to store checkbox components
        checkboxList = new ArrayList<JCheckBox>();
        // create layout box for button options
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        // create buttons for options, add to buttonBox
        JButton start = new JButton("Start");
        start.addActionListener(new MyStartListener());
        buttonBox.add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(new MyStopListener());
        buttonBox.add(stop);

        JButton upTempo = new JButton("Tempo Up");
        upTempo.addActionListener(new MyUpTempoListener());
        buttonBox.add(upTempo);

        JButton downTempo = new JButton("Down Tempo");
        downTempo.addActionListener(new MyDownTempoListener());
        buttonBox.add(downTempo);

        JButton clearAll = new JButton("Clear All");
        clearAll.addActionListener(new MyClearAllListener());
        buttonBox.add(clearAll);

        // create layout grid for instrument names
        // use grid to match spacing for checkboxes
        GridLayout nameGrid = new GridLayout(16,1,2,1);
        JPanel nameBox = new JPanel(nameGrid);
        //loop through instrument names, add them to name box
        for (int i = 0; i < 16; i++) {
            nameBox.add(new Label(instrumentNames[i]));
        }

        // add instrument names and option buttons to container panel
        background.add(BorderLayout.EAST, buttonBox);
        background.add(BorderLayout.WEST, nameBox);

        // add container panel to the main frame
        theFrame.getContentPane().add(background);

        // create layout for checkboxes
        GridLayout grid = new GridLayout(16,16);
        grid.setVgap(1);
        grid.setHgap(2);
        // apply grid layout to main panel
        mainPanel = new JPanel(grid);
        // add main panel to container panel
        background.add(BorderLayout.CENTER, mainPanel);

        // add checkboxes to main panel and checkbox list
        for (int i = 0; i < 256; i++) {
            JCheckBox c = new JCheckBox();
            // default value false
            c.setSelected(false);
            // add checkbox component to the list
            checkboxList.add(c);
            // add checkbox component to the GUI
            mainPanel.add(c);
        }

        setUpMidi();

        theFrame.setBounds(50,50,300,300);
        theFrame.pack();
        theFrame.setVisible(true);
    }

    public void setUpMidi() {
        try {
            // create the MIDI components
            player = MidiSystem.getSequencer();
            player.open();

            seq = new Sequence(Sequence.PPQ,4);
            track = seq.createTrack();
            player.setTempoInBPM(120);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildTrackAndStart() {
        // initialize track list for the 16 instruments
        int[] trackList = null;

        // remove any previous tracks data to start from fresh
        seq.deleteTrack(track);
        track = seq.createTrack();

        // loop through the 16 instruments
        for (int i = 0; i < 16; i++) {
            trackList = new int[16];

            // set the temp key to the current MIDI instrument code
            int key = instruments[i];

            
            for (int j = 0; j < 16; j++) {
                // check each beat of the current instrument [i].
                // Instrument [0] will check checkboxes [0-15]
                // Instrument [1] will check checkboxes [16-31] ..etc...
                JCheckBox jc = (JCheckBox) checkboxList.get(j + (16*i));

                // checks current checkbox component. sets the corresponding 
                // beat to the instrument key to be turned on.
                if (jc.isSelected()) {
                    trackList[j] = key;
                } else {
                    trackList[j] = 0;
                }
            }
            // trackList is now populated with each index as the instrument
            // number or a 0.
            makeTracks(trackList);
            // create action for event listener
            track.add(makeEvent(176,1,127,0,16));
        }

        track.add(makeEvent(192, 9, 1, 0, 15));
        try {
            player.setSequence(seq);
            player.setLoopCount(player.LOOP_CONTINUOUSLY);
            player.setTempoInBPM(120);
            player.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public class MyStartListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            buildTrackAndStart();
        }
    }

    public class MyStopListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            player.stop();
        }
    }

    public class MyUpTempoListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
	      float tempoFactor = player.getTempoFactor(); 
            player.setTempoFactor((float)(tempoFactor * 1.03));
            System.out.println(player.getTempoFactor());
        }
    }
    
    public class MyDownTempoListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
	      float tempoFactor = player.getTempoFactor(); 
            player.setTempoFactor((float)(tempoFactor * .97));
            System.out.println(player.getTempoFactor());
        }
    }

    public class MyClearAllListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            for (int i = 0; i < 256; i++) {
                checkboxList.get(i).setSelected(false);
            }
        }
    }

    public void makeTracks(int[] list) {
        for (int i = 0; i < 16; i++) {
            // list is from trackList. Includes instrument number or 0s 
            // in each index. set the key to the instrument.
            int key = list[i];

            // if not 0, add an event to the track for the corresponding 
            // instrument number.
            if (key != 0) {
                track.add(makeEvent(144, 9, key, 100, i));
                track.add(makeEvent(128, 9, key, 100, i+1));
            }
        }
    }

    public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, tick);
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return event;
    }

}