package com.homidev.egypt.ehgezmal3ab.forgot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.homidev.egypt.ehgezmal3ab.ConnectionManager;
import com.homidev.egypt.ehgezmal3ab.R;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Button button = (Button) findViewById(R.id.forgotButton);
        final EditText editText = (EditText) findViewById(R.id.forgotUsername);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "You have to insert a username", Toast.LENGTH_LONG).show();
                }else{
                    ConnectionManager.getConnectionManager().sendFrogtPasswordUsername(editText.getText().toString());
                }
            }
        });
    }
}
