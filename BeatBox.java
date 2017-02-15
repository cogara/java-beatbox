import javax.sound.midi.*;

public class BeatBox {
    public static void main(String[] args) {
        BeatBox bb = new BeatBox();
        bb.play();
    }

    private MidiEvent turnOn(int note, int time) throws InvalidMidiDataException {
        ShortMessage temp = new ShortMessage();
        temp.setMessage(192, 1, 50, 0);
        temp.setMessage(144, 1, note, 100);
       
        return new MidiEvent(temp, time);
    }

    private MidiEvent turnOff(int note, int time) throws InvalidMidiDataException {
        ShortMessage temp = new ShortMessage();
        
        temp.setMessage(128, 1, note, 100);
        return new MidiEvent(temp, time);
    }

    public void play() {
        try {
            Sequencer player = MidiSystem.getSequencer();
            player.open();

            Sequence seq = new Sequence(Sequence.PPQ, 4);

            Track track = seq.createTrack();

            ShortMessage inst = new ShortMessage();
            inst.setMessage(192, 1, 50, 0);
            MidiEvent changeInst = new MidiEvent(inst, 1);
            track.add(changeInst);

            // ShortMessage a = new ShortMessage();
            // a.setMessage(144, 1, 44, 100);
            // MidiEvent noteOn = new MidiEvent(a, 1);
            // track.add(noteOn);

            // ShortMessage b = new ShortMessage();
            // b.setMessage(128, 1, 44, 100);
            // MidiEvent noteOff = new MidiEvent(b, 16);
            // track.add(noteOff);

            track.add(turnOn(44,1));
            track.add(turnOff(44,2));
            track.add(turnOn(44,3));
            track.add(turnOff(44,4));
            track.add(turnOn(44,5));
            track.add(turnOff(44,6));
            track.add(turnOn(44,7));
            track.add(turnOff(44,8));
            track.add(turnOn(44,9));
            track.add(turnOff(44,16));




            player.setSequence(seq);

            player.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("playing sound");
    }
}