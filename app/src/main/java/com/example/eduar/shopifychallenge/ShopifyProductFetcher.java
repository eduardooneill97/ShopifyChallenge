package com.example.eduar.shopifychallenge;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShopifyProductFetcher {

    private static String DEFAULT_URL = "https://shopicruit.myshopify.com/admin/products.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
    private static String INVENTORY_LVL_ID_URL = "https://shopicruit.myshopify.com/admin/inventory_levels.json?access_token=c32313df0d0ef512ca64d5b336a0d7c6";
    private OkHttpClient client = new OkHttpClient();

    public List<String> fetchTags(){

        String url = Uri.parse(DEFAULT_URL)
                .buildUpon()
                .appendQueryParameter("fields", "tags")
                .build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonResponse = new JSONObject(response.body().string());
            JSONArray jsonProductArray = jsonResponse.getJSONArray("products");

            HashMap<String, String> tagsMap = new HashMap<>();
            for(int i = 0; i<jsonProductArray.length(); i++){
                JSONObject product = jsonProductArray.getJSONObject(i);
                String[] tags = product.getString("tags").split(", ");
                for(String t: tags)
                    tagsMap.put(t, t);
            }

            ArrayList<String> tagsList  = new ArrayList<String>(tagsMap.keySet());
            Collections.sort(tagsList);
            return tagsList;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Product> fetchProductsFromTag(String tag){
        ArrayList<Product> products = new ArrayList<>();

        String url = Uri.parse(DEFAULT_URL)
                .buildUpon()
                .appendQueryParameter("fields", "tags,id,title,variants")
                .build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try{
            Response response = client.newCall(request).execute();
            JSONObject jsonResponse = new JSONObject(response.body().string());
            JSONArray jsonProductArray = jsonResponse.getJSONArray("products");

            for(int i = 0; i<jsonProductArray.length(); i++){
                JSONObject jsonProductObject = jsonProductArray.getJSONObject(i);

                if(jsonProductObject.getString("tags").contains(tag)){
                    Product newProduct = new Product(jsonProductObject.getInt("id"), jsonProductObject.getString("title"));
                    JSONArray jsonVariantArray = jsonProductObject.getJSONArray("variants");

                    int level = 0;
                    for(int j = 0; j<jsonVariantArray.length(); j++){
                        JSONObject jsonVariant = jsonVariantArray.getJSONObject(j);
                        level += getVariantInventoryLvl(String.valueOf(jsonVariant.getInt("inventory_item_id")));
                    }
                    newProduct.setInventoryLvl(level);

                    products.add(newProduct);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return products;
    }

    private int getVariantInventoryLvl(String inventoryItemID){
        int count = 0;

        String url = Uri.parse(INVENTORY_LVL_ID_URL)
                .buildUpon()
                .appendQueryParameter("inventory_item_ids", inventoryItemID)
                .build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonResponse = new JSONObject(response.body().string());
            JSONArray jsonInventoryLvlsArray = jsonResponse.getJSONArray("inventory_levels");

            for(int i = 0; i<jsonInventoryLvlsArray.length(); i++){
                JSONObject jsonInvLvl = jsonInventoryLvlsArray.getJSONObject(i);
                if(!jsonInvLvl.isNull("available"))
                    count+=jsonInvLvl.getInt("available");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return count;
    }

}
