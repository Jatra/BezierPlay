package uk.co.jatra.bezierplay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class MainActivity extends AppCompatActivity implements DisplayView.ProgressListener, CompoundButton.OnCheckedChangeListener{

    private TextView percentage;
    @Bind(R.id.showLevel1Lines) CheckBox showLevel1Lines;
    @Bind(R.id.showLevel2Lines) CheckBox showLevel2Lines;
    @Bind(R.id.showLevel3Lines) CheckBox showLevel3Lines;
    @Bind(R.id.showLevel1Points) CheckBox showLevel1Points;
    @Bind(R.id.showLevel2Points) CheckBox showLevel2Points;
    @Bind(R.id.showLevel3Points) CheckBox showLevel3Points;
    @Bind(R.id.display_view) DisplayView displayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        ((CheckBox)findViewById(R.id.showLevel1Points))
//                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        displayView.setShowingMovingControlPoints1(isChecked);
//                    }
//                });
//        ((CheckBox)findViewById(R.id.showLevel2Points))
//                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        displayView.setShowingMovingControlPoints2(isChecked);
//                    }
//                });
//        ((CheckBox)findViewById(R.id.showLevel3Points))
//                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        displayView.setShowingMovingControlPoints3(isChecked);
//                    }
//                });
//        ((CheckBox)findViewById(R.id.showLevel1Lines))
//                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        displayView.setShowControlLine1(isChecked);
//                    }
//                });
//        ((CheckBox)findViewById(R.id.showLevel2Lines))
//                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        displayView.setShowControlLine2(isChecked);
//                    }
//                });
//        ((CheckBox)findViewById(R.id.showLevel3Lines))
//                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        displayView.setShowControlLine3(isChecked);
//                    }
//                });
        percentage = (TextView)findViewById(R.id.percentage);

        displayView.setProgressListener(this);
    }

    @Override
    public void fractionProgress(float fraction) {
        percentage.setText(String.format("%d %% along line", (int)(100*fraction)));
    }

    @OnCheckedChanged({R.id.showLevel1Points, R.id.showLevel2Points, R.id.showLevel3Points, R.id.showLevel1Lines, R.id.showLevel2Lines, R.id.showLevel3Lines})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        displayView.showMovingControlPoints(showLevel1Points.isChecked(), showLevel2Points.isChecked(), showLevel3Points.isChecked());
        displayView.showMovingControlLines(showLevel1Lines.isChecked(), showLevel2Lines.isChecked(), showLevel3Lines.isChecked());
    }
}
