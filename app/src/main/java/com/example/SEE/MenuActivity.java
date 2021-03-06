package com.example.SEE;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MenuActivity extends thread{
    TextToSpeech tts;
    SpeechRecognizer speechRecog;
    String choice;
    String choices[]={"general","object","search"};
    ImageButton btn;
    StorageReference storageRef;
    StorageReference videoRef;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    int mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initializeTextToSpeech();
        initializeSpeechRecognizer();
        mode=getIntent().getIntExtra("mode",3);

        btn=findViewById(R.id.menuBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                speechRecog.startListening(intent);

            }
        });


    }
    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecog = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecog.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result_arr = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

//                    headerObject.setText(result_arr.get(0));
                    choice = result_arr.get(0).toLowerCase();
                        List<String> list = Arrays.asList(choices);
                        if (list.contains(choice.toLowerCase())) {
                            processResult(result_arr.get(0));

                        }
                    // speak("Please state your choice clearly");

                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }
    private void initializeTextToSpeech() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (tts.getEngines().size() == 0 ){
                    Toast.makeText(getBaseContext(), getString(R.string.app_name),Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    tts.setLanguage(Locale.US);
//                    speak("Hello there, I am ready to start our conversation");
                }
                if(mode==2)
                    speak("To Proceed to object search, say general");

                else{
                    speak("Click on the screen and state your choice to proceed to general, " +
                            "search or object mode");

                }
            }
        });
    }
    private void speak(String message) {
        if(Build.VERSION.SDK_INT >= 21){
            tts.speak(message, TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH,null);
        }
    }
    private void processResult(String result_message) {
        result_message = result_message.toLowerCase();
        String myObjects;

        if (result_message.indexOf("general") != -1) {
                ProceedToDetection();

        } else if (result_message.indexOf("search") != -1) {
            if(mode!=2)
                ProceedToPersonal();

        } else if (result_message.indexOf("object") != -1) {

                dispatchTakeVideoIntent();
        }


        speechRecog.stopListening();

    }

    private void ProceedToPersonal() {
        Intent i = new Intent(this, DetectorCustomizedActivity.class);
        startActivity(i);
    }

    public void ProceedToDetection(){
        Intent i = new Intent(this, DetectorActivity.class);

        startActivity(i);
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);

        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE) {
//            if (resultCode == RESULT_OK) {
            Uri selectedVideoUri = data.getData();
            storageRef = FirebaseStorage.getInstance().getReference();
            videoRef = storageRef.child("videos");
            System.out.println("starting upload");

            uploadData(selectedVideoUri);
        }
    }

    private void uploadData(Uri videoUri) {
        System.out.println("starting upload");
        if(videoUri != null){
            UploadTask uploadTask = videoRef.putFile(videoUri);

            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                        Toast.makeText(getBaseContext(), "Upload Complete", Toast.LENGTH_SHORT).show();
//                    progressBarUpload.setVisibility(View.INVISIBLE);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    updateProgress(taskSnapshot);
                }
            });
        }else {
            Toast.makeText(getBaseContext(), "Nothing to upload", Toast.LENGTH_SHORT).show();
        }

    }



}