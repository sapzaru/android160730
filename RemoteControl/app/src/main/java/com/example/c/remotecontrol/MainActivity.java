package com.example.c.remotecontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView selectedTextView, workingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedTextView = (TextView) findViewById(R.id.selectedTextView);
        workingTextView = (TextView) findViewById(R.id.workingTextView);

        Button zeroButton = (Button) findViewById(R.id.zeroButton);
        Button oneButton = (Button) findViewById(R.id.oneButton);
        Button enterButton = (Button) findViewById(R.id.enterButton);

        View.OnClickListener numberListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기서 v는 자신 (버튼)
                Button btn = (Button) v;
                String working = workingTextView.getText().toString();
                String str = btn.getText().toString();

                if (working.equals("0")) {
                    workingTextView.setText(str);
                } else {
                    workingTextView.setText(working + str);
                }
            }
        };

        zeroButton.setOnClickListener(numberListener);
        oneButton.setOnClickListener(numberListener);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = workingTextView.getText().toString();
                selectedTextView.setText(str);
                workingTextView.setText("0");
            }
        });
    }
}
