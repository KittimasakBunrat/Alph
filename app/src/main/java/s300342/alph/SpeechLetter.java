package s300342.alph;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import org.w3c.dom.Text;

import java.util.Locale;

/**
 * Created by Kittimasak on 11/21/2017.
 */

public class SpeechLetter implements TextToSpeech.OnInitListener {

    TextToSpeech tts;

    public SpeechLetter(Context con) {
        tts = new TextToSpeech(con,this);
        tts.setPitch(0.5f);
        tts.setLanguage(Locale.CHINESE);
    }

    @Override
    public void onInit(int i) {
        if(i != TextToSpeech.ERROR) {
            tts.setLanguage(Locale.ENGLISH);
        }
    }

    public void speak(String string) {
        tts.speak(string, TextToSpeech.QUEUE_ADD, null);
    }
}
