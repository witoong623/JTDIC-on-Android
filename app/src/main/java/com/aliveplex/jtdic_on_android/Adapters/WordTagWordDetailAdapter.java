package com.aliveplex.jtdic_on_android.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliveplex.jtdic_on_android.Models.Tag;
import com.aliveplex.jtdic_on_android.R;

import java.util.List;

/**
 * Created by Aliveplex on 23/4/2560.
 */

public class WordTagWordDetailAdapter extends RecyclerView.Adapter<WordTagWordDetailAdapter.WordTagWordDetailViewHolder> {
    public List<Tag> getTagList() {
        return tagList;
    }

    private List<Tag> tagList;

    public WordTagWordDetailAdapter(List<Tag> taglist) {
        this.tagList = taglist;
    }

    @Override
    public WordTagWordDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_singel_textview, parent, false);

        return new WordTagWordDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordTagWordDetailViewHolder holder, int position) {
        Tag tag = tagList.get(position);
        holder.getTextView().setText(tag.getTagName());
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public static class WordTagWordDetailViewHolder extends RecyclerView.ViewHolder {
        public TextView getTextView() {
            return textView;
        }

        private TextView textView;

        public WordTagWordDetailViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.single_textview);
        }
    }
}
