package com.intuit.schedulingapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.codemodel.JCodeModel;

import org.apache.http.HttpResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.Jackson2Annotator;
import org.jsonschema2pojo.SchemaGenerator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.rules.RuleFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextToSpeech speaker;
    private static final int REQUEST_CODE = 1234;
    Dialog match_text_dialog;
    ListView textlist;
    ArrayList<String> matches_text;
    Button Start,Training;
    MediaRecorder recorder;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //temp code

        Hits[] answers=new Hits[20];

        answers=mockSTS();

        final Hits[]responseans=answers;

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        //builderSingle.setIcon();
        builderSingle.setTitle("Pick any answer:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.select_dialog_item);
        for(Hits ans:answers) {
            arrayAdapter.add(Html.fromHtml("<a href=\"#\">"+ans.getDoc().getTitle().getClean()+"</a>")+
            "description:\n"+
            Html.fromHtml((ans.getDoc().getBest_answer().getText()).substring(0,100)+"...")+
            "\nclick to view more");
        }

        builderSingle.setNegativeButton(
                "close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Hits selectedans=responseans[which];
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                MainActivity.this);
                        builderInner.setMessage(Html.fromHtml(selectedans.getDoc().getBest_answer().getText()));
                        builderInner.setTitle(selectedans.getDoc().getTitle().getClean());
                        builderInner.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builderInner.show();
                    }
                });
        builderSingle.show();

        //end temp


        Training = (Button)findViewById(R.id.training);

        Training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String filename = "test_my_words.mp3";
                String filename =
                        Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/myWords.3gp";
                // show filename
                System.out.println("File is: " + filename);

                try {
                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    recorder.setOutputFile(filename);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    recorder.prepare();

                } catch (Exception e) {
                    recorder.reset();
                    e.printStackTrace();
                }


                recorder.start();

            }
        });

        Button stop=(Button)findViewById(R.id.stopButton);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recorder.stop();
                recorder.release();
            }
        });

        Button stsBtn=(Button)findViewById(R.id.stsBtn);
        stsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpResponse response= HTTPConnector.configureHTTP();
                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
            }
        });

        Start = (Button)findViewById(R.id.start_reg);

        speaker=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    speaker.setLanguage(Locale.US);
                    speaker.speak("Welcome to TurboTax Voice Commands", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });


        Start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        speaker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status != TextToSpeech.ERROR) {
                                    speaker.setLanguage(Locale.US);
                                    speaker.setSpeechRate(1.0f);
                                    /*speaker.speak("What would you like to do  ", TextToSpeech.QUEUE_FLUSH, null);

                                    speaker.playSilentUtterance(700, TextToSpeech.QUEUE_ADD, null);
                                    speaker.speak("One ,Call customer care ", TextToSpeech.QUEUE_ADD, null);
                                    speaker.playSilentUtterance(700, TextToSpeech.QUEUE_ADD, null);
                                    speaker.speak("Two ,Take a picture", TextToSpeech.QUEUE_ADD, null);
                                    speaker.playSilentUtterance(1000, TextToSpeech.QUEUE_ADD, null);
*/
                                    speaker.speak("You may Speak now", TextToSpeech.QUEUE_ADD, null,"Speak");

                                    speaker.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                                        @Override
                                        public void onUtteranceCompleted(String utteranceId) {

                                            if (isConnected()) {

                                                try {
                                                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                                                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
                                                    intent.putExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES,80);
                                                    startActivityForResult(intent, REQUEST_CODE);
                                                } catch (ActivityNotFoundException e) {
                                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://market.android.com/details?id=com.google.android.apps.googlevoice"));
                                                    startActivity(browserIntent);

                                                }

                                            } else {
                                                Toast.makeText(getApplicationContext(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });

                    }
                });







        /*
        Intent scheduledTask=new Intent(this, TimeService.class);
        scheduledTask.putExtra("Time",0L);
        startService(scheduledTask);
        */
    }

    public  boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net!=null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            matches_text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            Toast.makeText(getApplicationContext(),matches_text.get(0),Toast.LENGTH_SHORT).show();

            /*String[] answers=new String[20];

            answers=mockSTS();


            AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
            //builderSingle.setIcon();
            builderSingle.setTitle("Pick any answer:-");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    MainActivity.this,
                    android.R.layout.select_dialog_item);
            for(String ans:answers) {
                arrayAdapter.add(ans);
            }

            builderSingle.setNegativeButton(
                    "cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            builderSingle.setAdapter(
                    arrayAdapter,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strName = arrayAdapter.getItem(which);
                            AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                    MainActivity.this);
                            builderInner.setMessage(strName);
                            builderInner.setTitle("Opening");
                            builderInner.setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builderInner.show();
                        }
                    });
            builderSingle.show();
*/
            /*for(String result:matches_text){
                String res=result.toLowerCase();
                if(res.equalsIgnoreCase("one")||res.contains("open sos")||res.contains("call customer care")||res.contains("customer care")){
                    Log.d("SOS tag", "SOS/Customer/Customer care word found");
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    speaker.speak("Nice ! Calling customer care",TextToSpeech.QUEUE_FLUSH,null);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    speaker.speak("Do you want to call to customer care",TextToSpeech.QUEUE_FLUSH,null);
                    builder.setMessage("Do you want to call to customer care?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                    break;
                }
                else if(res.equalsIgnoreCase("two")||res.contains("picture")||res.contains("screenshot")||res.contains("take screenshot")){
                    Log.d("SOS tag", "Picture/screenshot/take screenshot word found");
                }
            }
            if (matches_text.get(0).equalsIgnoreCase("Open SOS")){
                Intent scheduledTask=new Intent(getApplicationContext(), TimeService.class);
                scheduledTask.putExtra("Time",0L);
                startService(scheduledTask);
            }
            else {
                match_text_dialog = new Dialog(MainActivity.this);
                match_text_dialog.setContentView(R.layout.dialog_matches_frag);
                match_text_dialog.setTitle("Select Matching Text");
                textlist = (ListView)match_text_dialog.findViewById(R.id.list);

                ArrayAdapter<String> adapter =    new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, matches_text);
                textlist.setAdapter(adapter);
                textlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Log.d("You have said ", matches_text.get(position));
                        match_text_dialog.hide();

                    }
                });
                match_text_dialog.show();
            }*/



        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Hits[] mockSTS() {
        List<String>results = new ArrayList<String>();
        Gson gson = new Gson();
        Response mapped=new Response();
        try {
        /*JCodeModel codeModel = new JCodeModel();


        URL source = new URL("file:///android_asset/test/sampleResponse.json");

        GenerationConfig config = new DefaultGenerationConfig() {
            @Override
            public boolean isGenerateBuilders() { // set config option by overriding method
                return true;
            }
        };

        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(), new SchemaStore()), new SchemaGenerator());
        mapper.generate(codeModel, "Hit", "com.intuit.schedulingapp", source);

                codeModel.build(new File("output"));

*/
            ObjectMapper mapp=new ObjectMapper();
            mapp.generateJsonSchema(Response.class);
            File f1=new File("file:///android_asset/test/sampleResponse.txt");

            Log.d("f1",""+f1.exists());

            String originalPath = "file:///android_asset/test/sampleResponse.txt";
            String localizedPath = "file:///android_asset/test/sampleResponse.txt";

            URI uri=URI.create("file:///android_asset/test/sampleResponse.txt");
            AssetManager am=getAssets();
            //InputStream inputs = am.open(uri.getPath());
            //File file = createFileFromInputStream(inputs);
            //Log.d("file check",""+file.exists());
            String path;
            String localizedAssetPath = localizedPath.replace("file:///android_asset/",  "");

            final String sdDir = getAssets().list("test")[0];
            //InputStream ips = getAssets().open("sampleResponse.txt");
            //InputStreamReader isr=new InputStreamReader(ips);
            gson=new GsonBuilder().create();
            mapped = gson.fromJson(MockSTSResponse.sample, Response.class);

            Log.d("hit",mapped.toString());
            Log.d("sdDir",sdDir);
            try {
                InputStream stream = getResources().getAssets().open(localizedAssetPath);
                stream.close();
                path= localizedPath;
            }
            catch (Exception e) {
                path= originalPath;

            }

            Log.d("path",path);
            //Gson gson = new GsonBuilder().create();
            //Hit h=gson.fromJson(new InputStreamReader());
            //Gson gs=    mapp.readValues(,Hit.class);




        //ObjectMapper mapper = new ObjectMapper();

            //File json = new File("file:///android_asset/test/sampleResponse.json");
            //InputStream inputStream = getResources().getAssets().open("test/sampleResponse.txt");

            //URL url = getClass().getResource("/assets/test/sampleResponse.json");
              //  File f = new File(url.toURI());

            //File file = createFileFromInputStream(inputStream);
            //Log.d("file",""+file.exists());

            //ans=mapper.readValue(inputStream, Hit.class);

           // Log.d("ans",ans.toString());
        }catch (Exception ie){
            ie.printStackTrace();
        }
        //Logger.info(plane);

        //Log.d("best ans",ans.getSegmented().get(0));
        //Best_answer ans= new Best_answer();


        return mapped.getHits();
    }
    private File createFileFromInputStream(InputStream inputStream) {

        try{
            File f = new File("file:///android_asset/test/sample");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                Log.d("length",length+"");
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        }catch (IOException e) {
            //Logging exception
        }

        return null;
    }

}
