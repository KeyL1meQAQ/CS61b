package synthesizer;

import edu.princeton.cs.introcs.StdAudio;
import edu.princeton.cs.introcs.StdDraw;

public class GuitarHero {

    public static void main(String[] args) {
        GuitarString[] strings = new GuitarString[37];
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

        for (int i = 0; i < 37; i++) {
            double frequency = 440 * Math.pow(2, (i - 24) / 12);
            strings[i] = new GuitarString(frequency);
        }

        while (true) {
            /* check if the user has typed a key; if so, process it */

            if (StdDraw.hasNextKeyTyped()) {
                GuitarString string;
                char key = StdDraw.nextKeyTyped();
                int position = keyboard.indexOf(key);
                if (position == -1) {
                    continue;
                }
                string = strings[position];
                string.pluck();
            }

            double sample = 0;
            for (int i = 0; i < 37; i++) {
                sample += strings[i].sample();
            }
            StdAudio.play(sample);

            for (int i = 0; i < 37; i++) {
                strings[i].tic();
            }
        }
    }
}
