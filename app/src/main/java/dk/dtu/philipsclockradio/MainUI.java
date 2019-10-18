package dk.dtu.philipsclockradio;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainUI extends AppCompatActivity implements OnTouchListener {
    public TextView timeTextView, statusTextview;
    private Button btn_hour, btn_min, btn_preset, btn_sleep, btn_al1, btn_al2, btn_snooze;
    private ImageButton btn_power;
    private ImageView circle1, circle2, circle3, circle4, circle5;

    private static ContextClockradio logik;

    private boolean displayLed1, displayLed2, displayLed3, displayLed4, displayLed5, displayBlink, musicPlaying;
    private boolean longclick = false;
    private View currentbtn;

    final Handler handler = new Handler();
    Runnable mLongPressed = new Runnable() {
        public void run() {
            onLongClick(currentbtn);
            longclick = true;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_philips_clockradio_main);

        // UI Initialiseringer
        btn_hour = (Button) findViewById(R.id.btn_hour);
        btn_min = (Button) findViewById(R.id.btn_min);
        btn_preset = (Button) findViewById(R.id.btn_preset);
        btn_power = (ImageButton) findViewById(R.id.btn_power);
        btn_sleep = (Button) findViewById(R.id.btn_sleep);
        btn_al1 = (Button) findViewById(R.id.btn_al1);
        btn_al2 = (Button) findViewById(R.id.btn_al2);
        btn_snooze = (Button) findViewById(R.id.btn_snooze);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        statusTextview = (TextView) findViewById(R.id.statusText);
        circle1 = (ImageView) findViewById(R.id.circle1);
        circle2 = (ImageView) findViewById(R.id.circle2);
        circle3 = (ImageView) findViewById(R.id.circle3);
        circle4 = (ImageView) findViewById(R.id.circle4);
        circle5 = (ImageView) findViewById(R.id.circle5);

        btn_hour.setOnTouchListener(this);
        btn_min.setOnTouchListener(this);
        btn_preset.setOnTouchListener(this);
        btn_power.setOnTouchListener(this);
        btn_sleep.setOnTouchListener(this);
        btn_al1.setOnTouchListener(this);
        btn_al2.setOnTouchListener(this);
        btn_snooze.setOnTouchListener(this);
        btn_hour.setOnTouchListener(this);

        //
        // Logik initatialisering
        //

        logik = new ContextClockradio(this);

        try{
            timeTextView.setText(savedInstanceState.getString("displayText"));
        } catch(NullPointerException e){}

        try{
            boolean[] bools = savedInstanceState.getBooleanArray("bools");
            displayLed1 = bools[0];
            displayLed2 = bools[1];
            displayLed3 = bools[2];
            displayLed4 = bools[3];
            displayLed5 = bools[4];
            displayBlink = bools[5];
            musicPlaying = bools[6];

        } catch (NullPointerException e){}

        updateUI();
    }

    // Denne metode sætter teksten på displayet (f.eks. klokken)
    public void setDisplayText(String text){
        String subtext = text;
        if(text.length() > 5){
            subtext = text.substring(0, 4);
        }
        timeTextView.setText(subtext);
    }

    // Denne metode kaldes når der bliver trykket på en knap
    private void onClick(View view) {
        if(view == btn_hour){
            logik.onClick_Hour();
        } else if(view == btn_min){
            logik.onClick_Min();
        } else if(view == btn_preset){
            logik.onClick_Preset();
        } else if(view == btn_power){
            logik.onClick_Power();
        } else if(view == btn_sleep){
            logik.onClick_Sleep();
        } else if(view == btn_al1){
            logik.onClick_AL1();
        } else if(view == btn_al2){
            logik.onClick_AL2();
        } else if(view == btn_snooze) {
            logik.onClick_Snooze();
        }
    }

    // Denne metode kaldes når der bliver holdt en knap nede i over 2 sekunder
    private void onLongClick(View view) {
        if(view == btn_hour){
            logik.onLongClick_Hour();
        } else if(view == btn_min){
            logik.onLongClick_Min();
        } else if(view == btn_preset){
            logik.onLongClick_Preset();
        } else if(view == btn_power){
            logik.onLongClick_Power();
        } else if(view == btn_sleep){
            logik.onLongClick_Sleep();
        } else if(view == btn_al1){
            logik.onLongClick_AL1();
        } else if(view == btn_al2){
            logik.onLongClick_AL2();
        } else if(view == btn_snooze) {
            logik.onLongClick_Snooze();
        }
    }

    // Denne metode gemmer alt UI state, til hvis der f.eks. laves en skærmrotation
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        boolean[] bools = new boolean[] {displayLed1, displayLed2, displayLed3, displayLed4, displayLed5, displayBlink, musicPlaying};
        savedInstanceState.putString("displayText", logik.getTime().toString().substring(10,16));
        //savedInstanceState.putString("displayText", timeTextView.getText().toString());
        savedInstanceState.putBooleanArray("bools", bools);
        super.onSaveInstanceState(savedInstanceState);
    }

    // Tænder og slukker for en LED pære i displayet (Fra 1 til 5)
    public void turnOnLED(int LEDnumber){
        switch(LEDnumber){
            case 1:
                displayLed1 = true;
                updateUI();
                break;
            case 2:
                displayLed2 = true;
                updateUI();
                break;
            case 3:
                displayLed3 = true;
                updateUI();
                break;
            case 4:
                displayLed4 = true;
                updateUI();
                break;
            case 5:
                displayLed5 = true;
                updateUI();
                break;
            default:
                break;
        }
    }

    public void turnOffLED(int LEDnumber){
        switch(LEDnumber){
            case 1:
                displayLed1 = false;
                updateUI();
                break;
            case 2:
                displayLed2 = false;
                updateUI();
                break;
            case 3:
                displayLed3 = false;
                updateUI();
                break;
            case 4:
                displayLed4 = false;
                updateUI();
                break;
            case 5:
                displayLed5 = false;
                updateUI();
                break;
            default:
                break;
        }
    }

    private void updateUI(){
        circle1.setVisibility(displayLed1 ? View.VISIBLE : View.INVISIBLE);
        circle2.setVisibility(displayLed2 ? View.VISIBLE : View.INVISIBLE);
        circle3.setVisibility(displayLed3 ? View.VISIBLE : View.INVISIBLE);
        circle4.setVisibility(displayLed4 ? View.VISIBLE : View.INVISIBLE);
        circle5.setVisibility(displayLed5 ? View.VISIBLE : View.INVISIBLE);
    }

    // Denne funktion får display-teksten (F.eks. med klokkeslættet) til at blinke.
    public void turnOnTextBlink(){
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(400);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        timeTextView.startAnimation(anim);
    }

    public void turnOffTextBlink(){
        timeTextView.clearAnimation();
    }

    public void toggleRadioPlaying(){
        musicPlaying = !musicPlaying;
        if(musicPlaying){
            statusTextview.setText("Radio: ON");
        } else {
            statusTextview.setText("Radio: OFF");
        }

    }


    // Denne metode tjekker om en knap er trykket på, eller om den er holdt nede i over 2 sekunder
    // NB: Skal ikke kaldes udefra!
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
            currentbtn = v;
        handler.postDelayed(mLongPressed, 2000);
        if(event.getAction() == MotionEvent.ACTION_UP){
            handler.removeCallbacks(mLongPressed);
            if(longclick == false) {
                onClick(v);
            }
            longclick = false;
        }
        return false;
    }

}
