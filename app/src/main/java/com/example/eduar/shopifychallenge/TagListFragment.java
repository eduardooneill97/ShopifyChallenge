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
public class TagListFragment extends Fragment {

    private RecyclerView tagsRecyclerView;
    private TagsRecyclerViewAdapter adapter;
    private ProgressBar pb;
    private List<String> tagsList;

    public TagListFragment() {
        // Required empty public constructor
    }

    public static TagListFragment newInstance(){
        return new TagListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        TextView t = v.findViewById(R.id.title);
        t.setText(R.string.tags);

        tagsRecyclerView = v.findViewById(R.id.recycler_view);
        tagsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pb = v.findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);

        new TagFetch().execute();

        return v;
    }

    private void setupAdapter(List<String> tags){
        tagsList = tags;
        adapter = new TagsRecyclerViewAdapter();

        tagsRecyclerView.setAdapter(adapter);
    }

    private class TagFetch extends AsyncTask<Void, Void, List<String>>{

        @Override
        protected List<String> doInBackground(Void... voids) {
            return new ShopifyProductFetcher().fetchTags();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);

            pb.setVisibility(View.GONE);
            setupAdapter(strings);
        }

    }

    private class TagsRecyclerViewAdapter extends RecyclerView.Adapter<TagViewHolder>{

        @NonNull
        @Override
        public TagViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new TagViewHolder(LayoutInflater.from(getContext()), viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull TagViewHolder tagViewHolder, int i) {
            tagViewHolder.bind(tagsList.get(i));
        }

        @Override
        public int getItemCount() {
            return tagsList.size();
        }
    }

    private class TagViewHolder extends RecyclerView.ViewHolder{

        TextView tagView;

        public TagViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.tag_list_item, parent, false));

        }

        public void bind(final String tag){
            tagView = itemView.findViewById(R.id.tag_text);
            tagView.setText(tag);
            tagView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((TagSelectionCallback)getActivity()).onTagSelected(tag);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((TagSelectionCallback)getActivity()).onTagSelected(tag);
                }
            });
        }
    }

    public interface TagSelectionCallback{
        void onTagSelected(String tag);
    }
}
