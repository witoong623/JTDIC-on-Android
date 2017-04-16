package com.aliveplex.jtdic_on_android.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliveplex.jtdic_on_android.Models.JTDicWord;
import com.aliveplex.jtdic_on_android.R;

import java.util.List;

/**
 * Created by Aliveplex on 6/4/2560.
 */

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    private List<JTDicWord> wordList;

    public List<JTDicWord> getWordList() {
        return wordList;
    }

    public WordAdapter(List<JTDicWord> words) {
        wordList = words;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_word_item_view, parent, false);

        return new WordAdapter.WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        JTDicWord word = wordList.get(position);
        holder.getKanji_kana_view().setText(word.getKanji() + "  " + word.getKana());
        holder.getType_meaning_view().setText("(" + word.getType() + ")  " + word.getMeaning());
    }

    @Override
    public int getItemCount() {
        return this.wordList.size();
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {
        private TextView kanji_kana_view;
        private TextView type_meaning_view;

        public WordViewHolder(View itemView) {
            super(itemView);
            kanji_kana_view = (TextView)itemView.findViewById(R.id.word_kanji_kana);
            type_meaning_view = (TextView)itemView.findViewById(R.id.word_type_meaning);
        }

        public TextView getType_meaning_view() {
            return type_meaning_view;
        }

        public TextView getKanji_kana_view() {
            return kanji_kana_view;
        }
    }
}
