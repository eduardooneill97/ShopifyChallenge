package com.example.eduar.shopifychallenge;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends Fragment {

    private static String TAG = "Tag";

    private RecyclerView productsRV;
    private ProductsRVAdapter adapter;
    private ProgressBar pb;
    private List<Product> products;

    public ProductListFragment() {
        // Required empty public constructor
    }

    public static ProductListFragment newInstance(String tag){
        Bundle arg = new Bundle();
        arg.putString(TAG, tag);
        ProductListFragment plf = new ProductListFragment();
        plf.setArguments(arg);
        return plf;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        TextView t = v.findViewById(R.id.title);
        t.setText(R.string.products);

        productsRV = v.findViewById(R.id.recycler_view);
        productsRV.setLayoutManager(new LinearLayoutManager(getContext()));

        pb = v.findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);

        new ProductFetch().execute();

        return v;
    }

    private void setupAdapter(List<Product> p){
        products = p;
        adapter = new ProductsRVAdapter();
        productsRV.setAdapter(adapter);
    }

    private class ProductFetch extends AsyncTask<Void, Void, List<Product>>{

        @Override
        protected List<Product> doInBackground(Void... voids) {
            return new ShopifyProductFetcher().fetchProductsFromTag(getArguments().getString(TAG));
        }

        @Override
        protected void onPostExecute(List<Product> products) {
            super.onPostExecute(products);

            pb.setVisibility(View.GONE);
            setupAdapter(products);

        }

    }

    private class ProductsRVAdapter extends RecyclerView.Adapter<ProductVH>{

        @NonNull
        @Override
        public ProductVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ProductVH(LayoutInflater.from(getContext()), viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductVH productVH, int i) {
            productVH.bind(products.get(i));
        }

        @Override
        public int getItemCount() {
            return products.size();
        }
    }

    private class ProductVH extends RecyclerView.ViewHolder{

        public ProductVH(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.product_list_item, parent, false));
        }

        public void bind(Product p){
            TextView title = itemView.findViewById(R.id.title);
            TextView inventoryLvl = itemView.findViewById(R.id.inventory_lvl);

            title.setText(p.getName());
            inventoryLvl.setText(String.valueOf(p.getInventoryLvl()));
        }
    }

}
