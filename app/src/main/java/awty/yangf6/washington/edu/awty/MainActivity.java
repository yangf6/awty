package awty.yangf6.washington.edu.awty;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {
    private boolean started;

    PendingIntent alarmIntent = null;
    BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            String phn = intent.getStringExtra("phn");
            String msg = intent.getStringExtra("msg");
            Toast.makeText(MainActivity.this, phn + ": " + msg, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button start = (Button) findViewById(R.id.btn);
        final EditText message = (EditText) findViewById(R.id.msg);
        final EditText phoneNumber = (EditText) findViewById(R.id.phn);
        final EditText interval = (EditText) findViewById(R.id.freq);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPositiveNumber = interval.getText().toString().matches("^[1-9]+[0-9]*$");
                boolean messageHasValue = !message.getText().toString().equals("");
                boolean numberHasValue = !phoneNumber.getText().toString().equals("");

                registerReceiver(alarmReceiver, new IntentFilter("SoundDaAlarm"));
                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                if (started) {
                    started = false;
                    start.setText("Start");

                    am.cancel(alarmIntent);
                    alarmIntent.cancel();

                } else if (isPositiveNumber && messageHasValue && numberHasValue) {
                    start.setText("Stop");
                    started = true;
                    int milliSeconds = Integer.parseInt(interval.getText().toString()) * 1000 * 60;


                    Intent i = new Intent();
                    i.putExtra("phn", phoneNumber.getText().toString());
                    i.putExtra("msg", message.getText().toString());
//                    i.setAction("SoundDaAlarm");

                    alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, i, 0);
                    am.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + milliSeconds, milliSeconds, alarmIntent);
                }
            }
        });
    }

}

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
