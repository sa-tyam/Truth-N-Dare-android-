package application.greyhats.truthndare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    // levels of game : 0 - disgusting, 1 - stupid, 2 - normal, 3 - soft, 4 - sexy, 5 - hot

    ImageView bottleImageView;
    Random random;
    String jsonString;
    JSONArray tasks;
    String players;
    int players_no;
    String level;
    MediaPlayer bottleRolling, optionAppear;

    ArrayList<String> truth_array_list = new ArrayList<>();
    ArrayList<String> dare_array_list = new ArrayList<>();

    public void setTruthAndDare() throws JSONException {
        JSONObject taskPart = null;
        for (int i = 0 ; i < tasks.length(); i++ ) {
            try {
                taskPart = tasks.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (taskPart.getString("type").equals("Truth") && taskPart.getString("level").equals(level)) {
                truth_array_list.add(taskPart.getString("summary"));
            }
            if (taskPart.getString("type").equals("Dare") && taskPart.getString("level").equals(level)) {
                dare_array_list.add(taskPart.getString("summary"));
            }
        }
    }

    public void startRotation (View view ) {
        bottleImageView.setClickable(false);
        bottleRolling = MediaPlayer.create(GameActivity.this, R.raw.bottlerotating);
        bottleRolling.start();

        final int position = random.nextInt(players_no);
        int degree = (360/players_no)*position;

        bottleImageView.setRotation(0);

        bottleImageView.animate().rotationBy(360*8+degree).setDuration(4000);

        new CountDownTimer(4200, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.i("rotating", "yes");
            }

            @Override
            public void onFinish() {
                Toast.makeText(GameActivity.this, String.valueOf(position+1), Toast.LENGTH_SHORT).show();
                if (bottleRolling.isPlaying()) {
                    bottleRolling.stop();
                }
                bottleImageView.setClickable(true);
                selectOption();
            }
        }.start();
    }

    public void selectOption () {
        optionAppear = MediaPlayer.create(GameActivity.this, R.raw.optionone);
        optionAppear.start();
        String[] options = {"Truth", "Dare"};
        new AlertDialog.Builder(GameActivity.this)
                .setTitle("Select!")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ( which == 0 ) {
                            if (optionAppear.isPlaying()) {
                                optionAppear.stop();
                            }
                            optionAppear.start();
                            giveTruth();
                        } else {
                            if (optionAppear.isPlaying()) {
                                optionAppear.stop();
                            }
                            optionAppear.start();
                            giveDare();
                        }
                        if (optionAppear.isPlaying()){
                            optionAppear.stop();
                        }
                    }
                })
                .show();
    }

    public void giveTruth () {
        optionAppear = MediaPlayer.create(GameActivity.this, R.raw.optionone);
        if (optionAppear.isPlaying()) {
            optionAppear.stop();
        }
        optionAppear.start();
        int truthRandom = random.nextInt(truth_array_list.size());
        new AlertDialog.Builder(GameActivity.this)
                .setMessage(truth_array_list.get(truthRandom))
                .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("selection", "yes");
                    }
                })
                .show();
    }

    public void giveDare () {
        optionAppear = MediaPlayer.create(GameActivity.this, R.raw.optionone);
        if (optionAppear.isPlaying()) {
            optionAppear.stop();
        }
        optionAppear.start();
        int dareRandom = random.nextInt(dare_array_list.size());
        new AlertDialog.Builder(GameActivity.this)
                .setMessage(dare_array_list.get(dareRandom))
                .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("selection", "yes");
                    }
                })
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        bottleImageView = findViewById(R.id.bottleImageView);

        random = new Random();

        players =  getIntent().getStringExtra(("no_of_players"));
        level = getIntent().getStringExtra("level");
        players_no = Integer.valueOf(players);

        InputStream is = getResources().openRawResource(R.raw.jsonfile);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        jsonString = writer.toString();
        try {
            tasks = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setTruthAndDare();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}