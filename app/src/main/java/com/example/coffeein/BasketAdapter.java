package com.example.coffeein;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final ArrayList<Product> productsInBasket;
    private Context context;
    private Product product;
    private Integer amount;
    private BasketFragmentReceiver basketFragmentReceiver;



    BasketAdapter(Context context, ArrayList<Product> productsInBasket, BasketFragmentReceiver basketFragmentReceiver) {
        this.productsInBasket = productsInBasket;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.basketFragmentReceiver = basketFragmentReceiver;
    }
    interface BasketFragmentReceiver {
        void receiveChangedProductsBasketFragment(Product product);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.products_in_basket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        this.product = productsInBasket.get(position);
        Picasso.get().load(product.getImage()).into(holder.imageView);
        holder.nameView.setText(product.getName());
        holder.amountView.setText(product.getAmount()+"");
        holder.costView.setText(product.getPrice()*product.getAmount()+ " р.");

        holder.btnAmountPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                for(int i =0;i<productsInBasket.size(); i++) {
                    if (holder.nameView.getText().toString().equals(productsInBasket.get(i).getName())) {
                        if(basketFragmentReceiver != null && productsInBasket.get(i).getAmount()<10)
                        {
                            productsInBasket.get(i).setAmount(productsInBasket.get(i).getAmount()+1);
                            basketFragmentReceiver.receiveChangedProductsBasketFragment(productsInBasket.get(i));
                            holder.amountView.setText(productsInBasket.get(i).getAmount()+"");
                            holder.costView.setText(productsInBasket.get(i).getPrice()*productsInBasket.get(i).getAmount()+ " р.");
                        }
                    }
                }
            }
        });
        holder.btnAmountMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                for(int i =0;i<productsInBasket.size(); i++) {
                    if (holder.nameView.getText().toString().equals(productsInBasket.get(i).getName())) {
                        if(basketFragmentReceiver != null && productsInBasket.get(i).getAmount()>=2)
                        {
                            productsInBasket.get(i).setAmount(productsInBasket.get(i).getAmount()-1);
                            basketFragmentReceiver.receiveChangedProductsBasketFragment(productsInBasket.get(i));
                            holder.amountView.setText(productsInBasket.get(i).getAmount()+"");
                            holder.costView.setText(productsInBasket.get(i).getPrice()*productsInBasket.get(i).getAmount()+ " р.");
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsInBasket.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView, amountView, costView;
        Button btnAmountMinus, btnAmountPlus;
        ViewHolder(View view){
            super(view);
            imageView = (ImageView)view.findViewById(R.id.imageOfProductInBasket);
            nameView = (TextView) view.findViewById(R.id.nameOfProductInBasket);
            amountView = (TextView) view.findViewById(R.id.amountInBasket);
            costView = (TextView) view.findViewById(R.id.costInBasket);
            btnAmountMinus = (Button) view.findViewById(R.id.btnAmountMinus);
            btnAmountPlus = (Button) view.findViewById(R.id.btnAmountPlus);

        }
    }
}