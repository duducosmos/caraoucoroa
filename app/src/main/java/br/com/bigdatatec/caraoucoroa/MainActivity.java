package br.com.bigdatatec.caraoucoroa;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private AnimationDrawable giraMoeda;
    private ImageView moeda;
    private TextView texto;
    private boolean touched = false;
    private RadioGroup radioCaraCoroaGroup;
    private RadioButton radioCaraCoraButton;
    private String escolha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioCaraCoroaGroup = (RadioGroup) findViewById(R.id.caracoroagroup);

        texto = (TextView) findViewById(R.id.textView);
        moeda = (ImageView) findViewById(R.id.imageView);
        moeda.setImageResource(R.drawable.moeda035);
        moeda.postInvalidate();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("38CFBE22A69549342697AA9C2D44700E")
                .build();


        mAdView.loadAd(adRequest);



    }

    private int cara_ou_coroa(){
        Random r= new Random(System.currentTimeMillis());
        if(r.nextBoolean()){
            return 35;
        }
        else{
            return 16;

        }

    }


    private void stopAnimation(final AnimationDrawable animation){

        animation.start();

        final int cara_coroa = cara_ou_coroa();


        long totalDuration = 0;

        for(int i=0; i < animation.getNumberOfFrames(); i++){
            totalDuration += animation.getDuration(i);
        }
        totalDuration += 5 * totalDuration;

        for(int i=1; i < cara_coroa; i++){
            totalDuration += animation.getDuration(i);
        }

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        animation.stop();
                        animation.selectDrawable(cara_coroa);

                        if(cara_coroa == 16){
                            if(escolha == getString(R.string.coroa)){
                                texto.setText("Coroa, você venceu");
                            }
                            else{
                                texto.setText("Coroa, você Perdeu");
                            }

                        }
                        else{
                            if(escolha == getString(R.string.cara)){
                                texto.setText("Cara, você venceu");
                            }
                            else{
                                texto.setText("Cara, você Perdeu");
                            }
                        }

                        touched = false;

                    }
                });

            }
        };
        timer.schedule(timerTask,totalDuration);

    }

    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(touched == false){
                int selectedID = radioCaraCoroaGroup.getCheckedRadioButtonId();
                radioCaraCoraButton = (RadioButton) findViewById(selectedID);

                escolha = radioCaraCoraButton.getText().toString();
                texto.setText(escolha);
                moeda.setImageResource(0);
                moeda.setBackgroundResource(R.drawable.girarmoeda);
                giraMoeda = (AnimationDrawable) moeda.getBackground();
                stopAnimation(giraMoeda);
                touched = true;
            }
            return true;
        }
        return  super.onTouchEvent(event);
    }
}
