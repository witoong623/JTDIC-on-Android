package com.aliveplex.jtdic_on_android.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliveplex.jtdic_on_android.Models.Tag;
import com.aliveplex.jtdic_on_android.R;

import java.util.List;

/**
 * Created by Aliveplex on 21/4/2560.
 */

public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.TagViewHolder> {
    private List<Tag> tagList;

    public List<Tag> getTagList() {
        return tagList;
    }

    public TagListAdapter(List<Tag> tagList) {
        this.tagList = tagList;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_tag_item_view, parent, false);

        return new TagListAdapter.TagViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        Tag tag = tagList.get(position);
        holder.getTagName().setText(tag.getTagName());
        holder.getTagDescription().setText(tag.getDescription());
        Log.d("TagListAdapter", "Item bound.");
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        private TextView mTagName;

        public TextView getTagName() {
            return mTagName;
        }

        public TextView getTagDescription() {
            return mTagDescription;
        }

        private TextView mTagDescription;

        public TagViewHolder(View itemView) {
            super(itemView);

            mTagName = (TextView)itemView.findViewById(R.id.tag_name_tv);
            mTagDescription = (TextView)itemView.findViewById(R.id.tag_description_tv);
        }
    }
}
