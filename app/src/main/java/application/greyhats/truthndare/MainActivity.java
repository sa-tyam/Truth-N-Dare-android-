package application.greyhats.truthndare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public void startGame (View view ) {

        String[] colors = {"disgusting", "stupid", "normal", "soft", "sexy", "hot"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a level");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText numberEditText = findViewById(R.id.editTextNumberSigned);
                Intent myIntent = new Intent(MainActivity.this, GameActivity.class);
                myIntent.putExtra("no_of_players", numberEditText.getText().toString());
                myIntent.putExtra("level", String.valueOf(which));
                startActivity(myIntent);
            }
        });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}