package com.example.fedexrouting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Thomas Bridges, Charles Porter, Joel Bernhardt
 * Technocrats
 * FedExRouting
 *
 * Contributions
 * Charles Porter - 100%
 *
 * Research was done by Charles Porter
 *
 * login is the login page for the driver.
 *
 */

public class login extends AppCompatActivity {

    EditText user, pass;
    Button logButton;


    /*
    Sets up the login page.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //the text fields and button on the login page
        user = findViewById(R.id.usernameInput);
        pass = findViewById(R.id.passwordInput);
        logButton = findViewById(R.id.buttonLogin);


        //compares the data in the text field with the info from the database of the user logins
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Used to access the database
                DataBaseHelper dataBaseHelper = new DataBaseHelper(login.this);

                try{
                    if(String.valueOf(user.getText()).length()<1) {
                        Toast.makeText(login.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    }else{
                        Boolean checkUserPass = dataBaseHelper.checkUserPass(String.valueOf(user.getText()), String.valueOf(pass.getText()));
                        if(checkUserPass == true) {
                            Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (Exception e) {
                    Toast.makeText(login.this, "Error during login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


