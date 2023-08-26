package com.example.coffeein;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailProductFragment extends Fragment {
    private View view;
    private Product outputProduct;
    Integer amount, cost;
    TextView amountText, costText, nameText, descriptionText;
    ImageView image;
    MainActProductReceiver mainActProductReceiver;

    interface MainActProductReceiver {
        void takeProductToBasket(Product product);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_product, container, false);

        amount = 1;
        String txtName = outputProduct.getName();
        String txtStructure = outputProduct.getStructure();
        Date txtShelfLife = outputProduct.getShelfLife();
        String txtStorageConditions = outputProduct.getStorageConditions();
        Integer txtCost = outputProduct.getPrice();
        String txtImage = outputProduct.getImage();
        String txtDescription = "Состав: " + txtStructure;
        if(txtShelfLife != null) {
            SimpleDateFormat yf = new SimpleDateFormat("yyyy");
            String year = yf.format(txtShelfLife);
            txtDescription += "\nСрок годности: " + year + "." +
                    txtShelfLife.getMonth() + "." + txtShelfLife.getDay();
        }
        if(txtStorageConditions != null)
            txtDescription += "\nУсловия хранения: " + txtStorageConditions;

        amountText = (TextView)view.findViewById(R.id.countOfAmount);
        costText = (TextView)view.findViewById(R.id.cost);
        nameText = (TextView)view.findViewById(R.id.namePrchPage);
        descriptionText = (TextView)view.findViewById(R.id.descriptionPrchPage);
        image = (ImageView)view.findViewById(R.id.imgPrchPage);

        nameText.setText(txtName);
        descriptionText.setText(txtDescription);
        costText.setText(txtCost+"");
        Picasso.get().load(txtImage).into(image);

        cost = txtCost;
        changeAmountAndCost(amountText, costText);

        Button btnAmountMinus = (Button) view.findViewById(R.id.btnAmountMinus);
        btnAmountMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(amount>=2)
                    amount--;
                changeAmountAndCost(amountText, costText);
            }
        });
        Button btnAmountPlus = (Button) view.findViewById(R.id.btnAmountPlus);
        btnAmountPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(amount<=9)
                    amount++;
                changeAmountAndCost(amountText, costText);
            }
        });
        Button btnBuyOnPurPage = (Button) view.findViewById(R.id.btnBuyOnPurPage);
        btnBuyOnPurPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                mainActProductReceiver.takeProductToBasket( new Product(outputProduct.getId(), outputProduct.getName(),
                        outputProduct.getStructure(), outputProduct.getShelfLife(), outputProduct.getStorageConditions(),
                        outputProduct.getImage(), outputProduct.getPrice(), amount));
            }
        });


        return view;
    }

    void setDataInDetailCard(Product selectedProduct)
    {
        outputProduct = selectedProduct;
    }

    public void changeAmountAndCost(TextView amountText, TextView costText){
        amountText.setText(amount+"");
        costText.setText("Общая цена: " + amount*cost + " р.");
    }

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