package com.example.coffeein;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BasketFragment extends Fragment implements BasketAdapter.BasketFragmentReceiver {
    ArrayList<Product> productsInBasket = new ArrayList<>();
    Account account;
    BasketAdapter adapter;
    RecyclerView recyclerView;

    BasketAdapter.BasketFragmentReceiver basketFragmentReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        TextView totalCostView = view.findViewById(R.id.totalCost);
        int totalCost = 0;
        for(Product product:productsInBasket)
        {
            totalCost+=product.getPrice()*product.getAmount();
        }
        totalCostView.setText(totalCost+"");

        basketFragmentReceiver = new BasketAdapter.BasketFragmentReceiver() {
            @Override
            public void receiveChangedProductsBasketFragment(Product receivedProduct) {
                TextView totalCostView = view.findViewById(R.id.totalCost);
                int totalCost = 0;
                for(Product product:productsInBasket)
                {
                    if(product.getId() == receivedProduct.getId())
                    {
                        product.setAmount(receivedProduct.getAmount());
                    }
                    totalCost+=product.getPrice()*product.getAmount();
                }
                totalCostView.setText(totalCost+"");
            }
        };
        if(productsInBasket.size() != 0) {
            recyclerView = (RecyclerView) view.findViewById(R.id.listBasket);
            adapter = new BasketAdapter(view.getContext(), productsInBasket, basketFragmentReceiver);
            recyclerView.setAdapter(adapter);
        }

        Button btnBuyOnBasketPage = view.findViewById(R.id.btnBuyOnBasketPage);
        btnBuyOnBasketPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://coffeein.mishakaw.beget.tech/api/")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                JsonPlaceHolderApi json = retrofit.create(JsonPlaceHolderApi.class);
                Orders apiOrder = new Orders(account.getId(), new Date(), productsInBasket);
                Call<Orders> call = json.createNewOrder(apiOrder);

                call.enqueue(new Callback<Orders>() {
                    @Override
                    public void onResponse(Call<Orders> call, Response<Orders> response) {
                        if(response.isSuccessful())
                        {
                            productsInBasket.clear();
                            adapter = new BasketAdapter(view.getContext(), productsInBasket, basketFragmentReceiver);
                            recyclerView.setAdapter(adapter);
                            totalCostView.setText("0");
                            Toast.makeText(view.getContext(), "Заказ был оформлен! Скоро вам на почту придет письмо с информацией о заказе!", Toast.LENGTH_LONG).show();
                        }
                        else if (response.code() == 400)
                        {
                            Toast.makeText(view.getContext(), "Невозможно создать заказ. Данные неполные.", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(view.getContext(), "Невозможно создать заказ.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Orders> call, Throwable t) {
                        Toast.makeText(view.getContext(), "Ошибка - " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        return view;
    }

    void setProductsInBasketPage(ArrayList<Product> products)
    {
        productsInBasket = products;
    }
    void setAccInBasketPage(Account account)
    {
        this.account = account;
    }

    @Override
    public void receiveChangedProductsBasketFragment(Product product) { }
}