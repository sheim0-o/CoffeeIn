package com.example.coffeein;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangeProfile extends AppCompatActivity {
    EditText nameView, surnameView, patronymicView, emailView;
    DatePicker birthdateView;
    RadioGroup genderView; RadioButton selectedGender;

    int txtId, txtType;
    String txtName, txtSurname, txtPatronymic, txtEmail, txtImage, txtGender;
    Date txtBirthdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_profile);

        txtId = getIntent().getIntExtra("id", 0);
        txtName = getIntent().getStringExtra("name");
        txtSurname = getIntent().getStringExtra("surname");
        txtPatronymic = (getIntent().getStringExtra("patronymic") == null)?"":getIntent().getStringExtra("patronymic");
        txtEmail = getIntent().getStringExtra("email");
        txtType = getIntent().getIntExtra("type", 1);
        txtImage = getIntent().getStringExtra("image");
        txtBirthdate = ((Date)getIntent().getSerializableExtra("birthdate") == null)?new Date():(Date)getIntent().getSerializableExtra("birthdate");
        txtGender = (getIntent().getStringExtra("gender") == null)?"":getIntent().getStringExtra("gender");

        nameView = (EditText) findViewById(R.id.nameChangeProfileInput);
        surnameView = (EditText) findViewById(R.id.surnameChangeProfileInput);
        patronymicView = (EditText) findViewById(R.id.patronymicChangeProfileInput);
        emailView = (EditText) findViewById(R.id.emailChangeProfileInput);
        birthdateView = findViewById(R.id.birthdateChangeProfile);
        genderView = findViewById(R.id.genderChangeProfile);

        nameView.setText(txtName);
        surnameView.setText(txtSurname);
        patronymicView.setText(txtPatronymic);
        emailView.setText(txtEmail);
        SimpleDateFormat yf = new SimpleDateFormat("yyyy");
        int yearOfBirthdate = Integer.parseInt(yf.format(txtBirthdate));
        SimpleDateFormat df = new SimpleDateFormat("dd");
        int dayOfBirthdate = Integer.parseInt(df.format(txtBirthdate));
        birthdateView.updateDate(yearOfBirthdate, txtBirthdate.getMonth(), dayOfBirthdate);

        int idOfGender = getResources().getIdentifier("radio_"+txtGender, "id", getPackageName());
        genderView.check(idOfGender);
    }

    public void onClick(View v) {
        Intent toMainActivity = new Intent(this, MainActivity.class);
        Account updateAcc = new Account(txtId, txtName, txtSurname, txtPatronymic, txtEmail, txtGender, txtBirthdate, txtType, txtImage);
        switch (v.getId()) {
            case (R.id.btnConfirmChangesOfProfile):
                if(nameView.getText().toString().equals("") || surnameView.getText().toString().equals("") || emailView.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Введите данные!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    updateAcc.setName(nameView.getText().toString());
                    updateAcc.setSurname(surnameView.getText().toString());
                    updateAcc.setPatronymic(patronymicView.getText().toString());
                    updateAcc.setEmail(emailView.getText().toString());
                    long dateTime = birthdateView.getCalendarView().getDate();
                    updateAcc.setBirthdate(new Date(dateTime));
                    int selectedId = genderView.getCheckedRadioButtonId();
                    selectedGender = (RadioButton) findViewById(selectedId);
                    if(selectedGender != null) {
                        updateAcc.setGender(selectedGender.getTag().toString());
                    }

                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .setDateFormat("yyyy-MM-dd")
                            .create();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://coffeein.mishakaw.beget.tech/api/")
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    JsonPlaceHolderApi json = retrofit.create(JsonPlaceHolderApi.class);
                    Call<Account> call = json.updateAcc(updateAcc);

                    call.enqueue(new Callback<Account>() {
                        @Override
                        public void onResponse(Call<Account> call, Response<Account> response) {
                            if(response.isSuccessful())
                            {
                                toMainActivity.putExtra("id", updateAcc.getId());
                                toMainActivity.putExtra("name", updateAcc.getName());
                                toMainActivity.putExtra("surname", updateAcc.getSurname());
                                toMainActivity.putExtra("patronymic", updateAcc.getPatronymic());
                                toMainActivity.putExtra("email", updateAcc.getEmail());
                                toMainActivity.putExtra("type", updateAcc.getType());
                                toMainActivity.putExtra("image", updateAcc.getImage());
                                toMainActivity.putExtra("birthdate", updateAcc.getBirthdate());
                                toMainActivity.putExtra("gender", updateAcc.getGender());
                                Toast.makeText(getApplicationContext(), "Данные успешно обновлены!",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(toMainActivity);
                            }
                            else if (response.code() == 400)
                            {
                                Toast.makeText(getApplicationContext(), "Данные неполные!",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Не удалось обновить данные. Ошибка " + response.code(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Account> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Ошибка - " + t.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case (R.id.btnCancelChangesOfProfile):
                toMainActivity.putExtra("id", updateAcc.getId());
                toMainActivity.putExtra("name", updateAcc.getName());
                toMainActivity.putExtra("surname", updateAcc.getSurname());
                toMainActivity.putExtra("patronymic", updateAcc.getPatronymic());
                toMainActivity.putExtra("email", updateAcc.getEmail());
                toMainActivity.putExtra("type", updateAcc.getType());
                toMainActivity.putExtra("image", updateAcc.getImage());
                toMainActivity.putExtra("birthdate", updateAcc.getBirthdate());
                toMainActivity.putExtra("gender", updateAcc.getGender());
                startActivity(toMainActivity);
                break;
        }
    }
}