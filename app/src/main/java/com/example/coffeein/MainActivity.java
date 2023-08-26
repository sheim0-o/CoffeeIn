package com.example.coffeein;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MenuFragment.MainActProductReceiver, DetailProductFragment.MainActProductReceiver {
    MenuFragment menuFragment;
    BasketFragment basketFragment;
    ProfileFragment profileFragment;
    ArrayList<Product> productsInBasket = new ArrayList<>();
    Fragment selectedFragment = null;
    Account account;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        int txtId = getIntent().getIntExtra("id", 0);
        String txtName = getIntent().getStringExtra("name");
        String txtSurname = getIntent().getStringExtra("surname");
        String txtPatronymic = (getIntent().getStringExtra("patronymic") == null)?"":getIntent().getStringExtra("patronymic");
        String txtEmail = getIntent().getStringExtra("email");
        int txtType = getIntent().getIntExtra("type", 1);
        String txtImage = getIntent().getStringExtra("image");
        Date txtBirthdate = (Date)getIntent().getSerializableExtra("birthdate");
        String txtGender = (getIntent().getStringExtra("gender") == null)?"":getIntent().getStringExtra("gender");
        account = new Account(txtId, txtName, txtSurname, txtPatronymic, txtEmail, txtGender, txtBirthdate, txtType, txtImage);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MenuFragment()).commit();

        menuFragment = new MenuFragment();
        basketFragment = new BasketFragment();
        profileFragment = new ProfileFragment();

        basketFragment.setAccInBasketPage(account);
        profileFragment.setAccInProfilePage(account);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {

        switch (item.getItemId()) {
            case R.id.menu:
                selectedFragment = menuFragment;
                break;
            case R.id.basket:
                selectedFragment = basketFragment;
                break;
            case R.id.profile:
                selectedFragment = profileFragment;
                break;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit();

        return true;
    };

    @Override
    public void receiveSelectedProductMainAct(Product product) {
        DetailProductFragment detailProductFragment = new DetailProductFragment();
        detailProductFragment.setDataInDetailCard(product);
        selectedFragment = detailProductFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void takeProductToBasket(Product product) {
        productsInBasket.add(product);
        basketFragment.setProductsInBasketPage(productsInBasket);
        selectedFragment = menuFragment;
        Toast.makeText(getApplicationContext(), "Товар был добавлен в корзину", Toast.LENGTH_SHORT).show();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .addToBackStack(null)
                .commit();
    }
}