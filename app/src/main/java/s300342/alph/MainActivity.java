package s300342.alph;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button button;
    TextView text;
    TextView text1;
    TextView text2;
    SpeechRecognizer speechRecognizer;
    ArrayList<String> letters;
    int count;

    private void compareLetters(ArrayList<String> arrayList, ArrayList<String> data) {

        String letter = arrayList.get(count);
        System.out.println("LETTER: " + letter);
            for (int i = 0; i<data.size(); i++) {
                System.out.println("FOR: " + data.get(i));
                if(letter == data.get(i)) {
                    count++;
                    text.setText("RIGHT");
                } else {
                    text.setText("WRONG");
                }
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},3);
        }

        letters = new ArrayList<String>();
        count = 0;

        for (int i = 0; i<26; i++) {
            String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZÆØÅ";
            letters.add(i, String.valueOf(s.charAt(i)));
        }

        button = findViewById(R.id.button);
        text = findViewById(R.id.text);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text5);

        button.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 3: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                } else {
                    finish();
                }
                finish();
            }
        }
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

            default:
                break;
        }
    }

    class Listener implements RecognitionListener {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            text.setText("Ready for speech");
        }

        @Override
        public void onBeginningOfSpeech() {
            text.setText("Beginning of speech");
        }

        @Override
        public void onRmsChanged(float v) {
            text.setText("onRmsChanged");
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            text.setText("onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            text.setText("onEndOfSpeech");
        }

        @Override
        public void onError(int i) {
            text.setText("onError");
        }

        @Override
        public void onResults(Bundle bundle) {
            ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i<data.size(); i++) {
                data.set(i, data.get(i).toUpperCase());
            }
            text1.setText("Results: "+ data.get(0));
            compareLetters(letters, data);
            speechRecognizer.stopListening();
            speechRecognizer.destroy();
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    }
}


