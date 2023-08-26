package com.example.coffeein;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {
    private EditText nameReg, surnameReg, emailReg, passwordReg1, passwordReg2;
    public static String TAG = "SignUpActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameReg = (EditText) findViewById(R.id.nameRegInput);
        surnameReg = (EditText) findViewById(R.id.surnameRegInput);
        emailReg = (EditText) findViewById(R.id.emailRegInput);
        passwordReg1 = (EditText) findViewById(R.id.password1RegInput);
        passwordReg2 = (EditText) findViewById(R.id.password2RegInput);
    }

    public void onClick(View v) {
        Intent toMainActivity = new Intent(this, MainActivity.class);
        Intent toLoginActivity = new Intent(this, SignIn.class);
        String nameRegText, surnameRegText, emailRegText, passwordReg1Text, passwordReg2Text;
        switch (v.getId()){
            case(R.id.btnReg):
                nameRegText = nameReg.getText().toString();
                surnameRegText = surnameReg.getText().toString();
                emailRegText = emailReg.getText().toString();
                passwordReg1Text = passwordReg1.getText().toString();
                passwordReg2Text = passwordReg2.getText().toString();

                if(nameRegText.equals("") || surnameRegText.equals("") ||
                        emailRegText.equals("") || passwordReg1Text.equals("") || passwordReg2Text.equals("")) {
                    Toast.makeText(this, "Введите данные!",Toast.LENGTH_LONG).show();
                }
                else if (!passwordReg1Text.equals(passwordReg2Text))
                {
                    Toast.makeText(this, "Пароли не совпадают!",Toast.LENGTH_LONG).show();
                }
                else if (passwordReg1Text.length() < 6)
                {
                    Toast.makeText(this, "Пароль слишком короткий!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://coffeein.mishakaw.beget.tech/api/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    JsonPlaceHolderApi json = retrofit.create(JsonPlaceHolderApi.class);
                    Account apiLogin = new Account(nameRegText, surnameRegText, emailRegText, passwordReg1Text, "user.png");
                    Call<Account> call = json.registration(apiLogin);

                    call.enqueue(new Callback<Account>() {
                        @Override
                        public void onResponse(Call<Account> call, Response<Account> response) {
                            if(response.isSuccessful())
                            {
                                Account account = response.body();
                                toMainActivity.putExtra("id", account.getId());
                                toMainActivity.putExtra("name", account.getName());
                                toMainActivity.putExtra("surname", account.getSurname());
                                toMainActivity.putExtra("email", account.getEmail());
                                toMainActivity.putExtra("type", account.getType());
                                toMainActivity.putExtra("image", account.getImage());
                                startActivity(toMainActivity);
                            }
                            else if (response.code() == 404)
                            {Toast.makeText(getApplicationContext(), "Неверно введены данные!" + response.code(),
                                    Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Ошибка " + response.code(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Account> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "t - " + t.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case(R.id.btnToLogin):
                startActivity(toLoginActivity);
                break;
        }
    }
}