package com.example.c.remotecontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView selectedTextView, workingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedTextView = (TextView) findViewById(R.id.selectedTextView);
        workingTextView = (TextView) findViewById(R.id.workingTextView);

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

        // child를 통해서 TableRow를 얻어온다.
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        int number = 1;
        for(int i = 2; i < tableLayout.getChildCount() - 1; i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int k = 0; k < row.getChildCount(); k++) {
                Button button = (Button) row.getChildAt(k);
                button.setText("" + number);
                number++;
                button.setOnClickListener(numberListener);
            }
        }

        TableRow bottomRow = (TableRow) tableLayout.getChildAt(tableLayout.getChildCount() - 1);
        Button deleteButton = (Button) bottomRow.getChildAt(0);
        Button zeroButton = (Button) bottomRow.getChildAt(1);
        Button enterButton = (Button) bottomRow.getChildAt(2);
        deleteButton.setText("delete");
        zeroButton.setText("0");
        enterButton.setText("enter");

        zeroButton.setOnClickListener(numberListener);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = workingTextView.getText().toString();
                selectedTextView.setText(str);
                workingTextView.setText("0");
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workingTextView.setText("0");
            }
        });

        /*
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

        */
    }
}
