import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;

import static java.lang.System.exit;

public class MiniMusicPlayer {
    static JFrame f = new JFrame("Пищалка");
    static MyDrawPanel ml;

    public static void main (String[] args) {
        MiniMusicPlayer mini = new MiniMusicPlayer();
        mini.go();
        System.out.println("main is over");
    }

    public void setUpGUI() {
    ml = new MyDrawPanel();

    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setContentPane(ml);
    f.setBounds(30,30,300,300);
    f.setVisible(true);
    //f.setBackground(new Color(117, 187, 253));
    }

    public void go() {

        setUpGUI();

        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.addControllerEventListener(ml, new int[] {127});
            Sequence seq = new Sequence(Sequence.PPQ, 4);
            Track track = seq.createTrack();

            int r = 80;

            for (int i = 0; i < 48; i+=4) {
                r += (int)(Math.random()*25);

                track.add(makeEvent (144,1,r,100,i));
                track.add(makeEvent(176,1,127,0,i));
                track.add(makeEvent (128,1,r,100,i+2));
                r = 80;
            }

            sequencer.setSequence(seq);
            sequencer.start();
            sequencer.setTempoInBPM(120);

        } catch (Exception ex) {ex.printStackTrace();}
        System.out.println("go is over");
    }

    public static MidiEvent makeEvent (int comd, int chan, int one, int two, int tick) {

        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage (comd, chan, one, two);
            event = new MidiEvent (a, tick);

        } catch (Exception e) {}
        return event;
    }

    class MyDrawPanel extends JPanel implements ControllerEventListener {
        boolean msg = false;
        public double a;
        public int count;

        public void controlChange (ShortMessage event) {

            msg = true;
            repaint();
            count++;

            if (count == 12){
                try {
                    Thread.sleep(3000);
                    //exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void paintComponent (Graphics g) {

            if (msg) {
                Graphics2D g2 = (Graphics2D) g;

                int red = (int) (Math.random() * 250);
                int green = (int) (Math.random() * 250);
                int blue = (int) (Math.random() * 250);

                g.setColor(new Color(0, 0, 0));
                g.fillRect(0,0,this.getWidth(),this.getHeight());

                int ht = 25;
                int width = 25;

                int x = (int) (125 + 85 * (Math.sin(a)));
                int y = (int) (125 + 85 * (Math.cos(a)));

                int xl = (int) (x + 30 * (Math.sin(-a)));
                int yl = (int) (y + 30 * (Math.cos(-a)));

                g.setColor(new Color(65, 120, 255));
                g.fillOval(x, y, ht, width);

                g.setColor(new Color(red, green, blue));
                g.fillOval(120, 120, 50, 50);
                g.fillRect(123,123,44,44);

                g.setColor(new Color(255, 255, 255));
                g.fillOval(xl, yl, 10, 10);

                a+= 0.523;
                msg = false;
            }
        }
    }
}
