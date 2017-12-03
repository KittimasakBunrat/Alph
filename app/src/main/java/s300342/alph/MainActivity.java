package s300342.alph;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton button;
    Button button1;
    TextView text;
    TextView text1;
    TextView text2;
    SpeechRecognizer speechRecognizer;
    ArrayList<String> letters;
    int count;
    SpeechLetter sl;
    Animation au;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sl = new SpeechLetter(this);
        letters = new ArrayList<String>();
        count = 0;

        for (int i = 0; i<26; i++) {
            String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZÆØÅ";
            letters.add(i, String.valueOf(s.charAt(i)));
        }

        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);
        text = findViewById(R.id.text);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);

        text2.setText(letters.get(count));
        au = AnimationUtils.loadAnimation(this, R.anim.bounce);
        au.setRepeatCount(Animation.INFINITE);
        au.setDuration(1000);
        text2.startAnimation(au);


        next(button1, 0, false);
        button.setOnClickListener(this);
        button1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text1.setText("Trykk på mikrofon for å snakke");
        text.setText(currentAlphabet());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button:
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
                speechRecognizer.setRecognitionListener(new Listener());
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "nb-NO");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
                speechRecognizer.startListening(intent);
                break;

            case R.id.button1:
                next(button1, 0, false);
                count++;
                clear();
                break;

            case R.id.text2:
                sl.speak(letters.get(count));
                break;

            default:
                break;
        }
    }

    private void compareLetters(ArrayList<String> arrayList, ArrayList<String> data) {
        for (int i = 0; i<data.size(); i++) {
            if(arrayList.get(count).equals(data.get(i)) || data.get(i).contains(arrayList.get(count))) {
                dialog(R.layout.correct);
                next(button1, 1, true);
                text1.setText("Bra jobbet!");
                break;
            } else {
                text1.setText("Prøv med ett ord med samme forbokstav!");
                dialog(R.layout.wrong);
                break;
            }
        }
    }

    public void next(Button b, int i,boolean bool) {
        b.setAlpha(i);
        b.setClickable(bool);
        if(count == 28) {
            count = 0;
        }
    }

    public void clear() {
        text2.setText(letters.get(count));
        text.setText(currentAlphabet());
        text1.setText("");
        text2.startAnimation(au);
    }

    public void dialog(int i) {
        final Dialog pictureDialog = new Dialog(this);
        pictureDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        pictureDialog.setContentView(getLayoutInflater().inflate(i, null));
        pictureDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pictureDialog.show();
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                pictureDialog.cancel();
            }
        }.start();
    }

    public String currentAlphabet() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < letters.size(); i++) {
            if(i == count) {
                sb.append(" > " + letters.get(i) + " < ");
            } else {
                sb.append(letters.get(i));
            }
        }
        return sb.toString();
    }

    class Listener implements RecognitionListener {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float v) {
            text1.setText("Snakk!");
            button.setImageDrawable(getResources().getDrawable(R.drawable.microphone1));
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int i) {
            text1.setText("Prøv igjen!");
            button.setImageDrawable(getResources().getDrawable(R.drawable.microphone));
        }

        @Override
        public void onResults(Bundle bundle) {
            ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i<data.size(); i++) {
                data.set(i, data.get(i).toUpperCase());
            }
            compareLetters(letters, data);
            speechRecognizer.stopListening();
            speechRecognizer.destroy();
            button.setImageDrawable(getResources().getDrawable(R.drawable.microphone));
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    }
}


