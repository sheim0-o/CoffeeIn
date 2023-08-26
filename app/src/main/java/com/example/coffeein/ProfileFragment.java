package com.example.coffeein;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileFragment extends Fragment {
    public static String TAG = "ProfileFragment";
    private Account account;
    TextView fullname, email, birthdate, gender;
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fullname = view.findViewById(R.id.profileName);
        email = view.findViewById(R.id.profileEmail);
        imageView = view.findViewById(R.id.profileImage);
        birthdate = view.findViewById(R.id.profileBirthdate);
        gender = view.findViewById(R.id.profileGender);

        String txtFullname = account.getSurname() + " " + account.getName() + " " + account.getPatronymic();
        fullname.setText(txtFullname);
        email.setText(account.getEmail());
        String txtBirthday = "Дата рождения: ";
        if(account.getBirthdate() != null) {
            SimpleDateFormat fdf = new SimpleDateFormat("yyyy.MM.dd");
            String dateOfBirthday = fdf.format(account.getBirthdate());
            txtBirthday += dateOfBirthday;
        }
        else
        {
            txtBirthday += "-";
        }
        birthdate.setText(txtBirthday);
        String txtGender ="Пол: ";
        if(!account.getGender().isEmpty()) {
            int idOfGenderString = getResources().getIdentifier(account.getGender(), "string", view.getContext().getPackageName());
            txtGender += getString(idOfGenderString);
        }
        else
        {
            txtGender += "-";
        }
        gender.setText(txtGender);
        Picasso.get().load(account.getImage()).into(imageView);

        Button btnConfirmChangesOfProfile = (Button) view.findViewById(R.id.btnConfirmChangesOfProfile);
        btnConfirmChangesOfProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent toChangeProfileActivity = new Intent(view.getContext(), ChangeProfile.class);
                toChangeProfileActivity.putExtra("id", account.getId());
                toChangeProfileActivity.putExtra("name", account.getName());
                toChangeProfileActivity.putExtra("surname", account.getSurname());
                toChangeProfileActivity.putExtra("patronymic", account.getPatronymic());
                toChangeProfileActivity.putExtra("email", account.getEmail());
                toChangeProfileActivity.putExtra("type", account.getType());
                toChangeProfileActivity.putExtra("image", account.getImage());
                toChangeProfileActivity.putExtra("birthdate", account.getBirthdate());
                toChangeProfileActivity.putExtra("gender", account.getGender());
                startActivity(toChangeProfileActivity);
            }
        });
        Button btnLogOut = (Button) view.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent toLoginActivity = new Intent(view.getContext(), SignIn.class);
                startActivity(toLoginActivity);
            }
        });

        return view;
    }

    void setAccInProfilePage(Account account)
    {
        this.account = account;
    }
}