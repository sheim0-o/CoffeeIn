package com.example.coffeein;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignIn extends AppCompatActivity {
    private EditText emailAuth, passwordAuth;
    public static String TAG = "SignInActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailAuth = (EditText) findViewById(R.id.emailAuthInput);
        passwordAuth = (EditText) findViewById(R.id.passwordAuthInput);
    }

    public void onClick(View v) {
        Intent toMainActivity = new Intent(this, MainActivity.class);
        Intent toRegActivity = new Intent(this, SignUp.class);
        String emailAuthText, passwordAuthText;
        switch (v.getId()){
            case(R.id.btnLogin):
                emailAuthText = emailAuth.getText().toString();
                passwordAuthText = passwordAuth.getText().toString();
                if(emailAuthText.equals("") || passwordAuthText.equals("")) {
                    Toast.makeText(this, "Введите данные!",Toast.LENGTH_LONG).show();

                }
                else
                {
                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .create();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://coffeein.mishakaw.beget.tech/api/")
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    JsonPlaceHolderApi json = retrofit.create(JsonPlaceHolderApi.class);
                    Account apiLogin = new Account(emailAuthText, passwordAuthText);
                    Call<Account> call = json.authorization(apiLogin);

                    call.enqueue(new Callback<Account>() {
                        @Override
                        public void onResponse(Call<Account> call, Response<Account> response) {
                            if(response.isSuccessful())
                            {
                                Account account = response.body();
                                toMainActivity.putExtra("id", account.getId());
                                toMainActivity.putExtra("name", account.getName());
                                toMainActivity.putExtra("surname", account.getSurname());
                                toMainActivity.putExtra("patronymic", account.getPatronymic());
                                toMainActivity.putExtra("email", account.getEmail());
                                toMainActivity.putExtra("type", account.getType());
                                toMainActivity.putExtra("image", account.getImage());
                                toMainActivity.putExtra("birthdate", account.getBirthdate());
                                toMainActivity.putExtra("gender", account.getGender());
                                startActivity(toMainActivity);
                            }
                            else if (response.code() == 404)
                            {
                                Toast.makeText(getApplicationContext(), "Неверно введены данные!",
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
            case(R.id.btnToReg):
                startActivity(toRegActivity);
                break;
        }
    }
}