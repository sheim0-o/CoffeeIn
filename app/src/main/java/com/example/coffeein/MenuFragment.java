package com.example.coffeein;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuFragment extends Fragment implements MenuAdapter.MenuFragmentReceiver {
    ArrayList<Product> products = new ArrayList<Product>();
    public static String TAG = "MenuFragment";
    private MenuAdapter.MenuFragmentReceiver menuFragmentReceiver;
    private View view;
    MenuAdapter adapter;
    private MainActProductReceiver mainActProductReceiver;

    interface MainActProductReceiver {
        void receiveSelectedProductMainAct(Product product);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(products.isEmpty())
            setInitialData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);

        menuFragmentReceiver = new MenuAdapter.MenuFragmentReceiver() {
            @Override
            public void receiveSelectedProductMenuFragment(Product product) {
                mainActProductReceiver.receiveSelectedProductMainAct(product);
            }
        };

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listMenu);
        adapter = new MenuAdapter(view.getContext(), products, menuFragmentReceiver);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void setInitialData(){
        Gson gson = new GsonBuilder()
                .setLenient().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://coffeein.mishakaw.beget.tech/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Product>> call = jsonPlaceHolderApi.getProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(view.getContext(), response.code(),
                            Toast.LENGTH_SHORT).show();
                }
                List<Product> posts = response.body();
                for(Product product:posts){
                    int idTxt = product.getId();
                    String nameTxt = product.getName();
                    String structureTxt = product.getStructure();
                    Date shelfLifeTxt = product.getShelfLife();
                    String storageConditionsTxt = product.getStorageConditions();
                    String imageTxt = product.getImage();
                    int priceTxt = product.getPrice();

                    products.add(new Product(idTxt, nameTxt, structureTxt, shelfLifeTxt, storageConditionsTxt, imageTxt, priceTxt));
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(view.getContext(), t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void receiveSelectedProductMenuFragment(Product product) {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActProductReceiver) {
            mainActProductReceiver = (MainActProductReceiver) context;
        }
    }
    @Override
    public void onDetach() {
        mainActProductReceiver = null;
        super.onDetach();
    }
}