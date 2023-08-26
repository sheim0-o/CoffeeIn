package com.example.coffeein;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private final List<Product> products;
    private Context context;
    private Product product;
    private MenuFragmentReceiver menuFragmentReceiver;

    interface MenuFragmentReceiver {
        void receiveSelectedProductMenuFragment(Product product);
    }

    MenuAdapter(Context context, List<Product> products, MenuFragmentReceiver menuFragmentReceiver) {
        this.products = products;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.menuFragmentReceiver = menuFragmentReceiver;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.products, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.product = products.get(position);
        Picasso.get().load(product.getImage()).into(holder.flagView);
        holder.nameView.setText(product.getName());
        holder.descriptionView.setText(product.getStructure());
        holder.costView.setText(product.getPrice()+ " р.");

        holder.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for(int i =0;i<products.size(); i++) {
                    if (holder.nameView.getText().toString().equals(products.get(i).getName())) {
                        if(menuFragmentReceiver != null)
                        {
                            menuFragmentReceiver.receiveSelectedProductMenuFragment(products.get(i));
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView flagView;
        final TextView nameView, descriptionView, costView;
        final Button button;
        ViewHolder(View view){
            super(view);
            flagView = (ImageView)view.findViewById(R.id.imageOfProduct);
            nameView = (TextView) view.findViewById(R.id.nameOfProduct);
            descriptionView = (TextView) view.findViewById(R.id.description);
            costView = (TextView) view.findViewById(R.id.cost);

            button = (Button) view.findViewById(R.id.btnBuy);
        }
    }
}