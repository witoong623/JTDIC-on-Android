package com.aliveplex.jtdic_on_android.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.aliveplex.jtdic_on_android.Adapters.WordAdapter;
import com.aliveplex.jtdic_on_android.Constant;
import com.aliveplex.jtdic_on_android.Database.JTDicDb;
import com.aliveplex.jtdic_on_android.Interfaces.ClickListener;
import com.aliveplex.jtdic_on_android.JTDICApplication;
import com.aliveplex.jtdic_on_android.Listeners.RecyclerTouchListener;
import com.aliveplex.jtdic_on_android.Models.JTDicWord;
import com.aliveplex.jtdic_on_android.R;
import com.aliveplex.jtdic_on_android.Utils.JapaneseUtil;
import com.aliveplex.jtdic_on_android.WordDetailActivity;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static butterknife.ButterKnife.bind;

public class SearchFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private OnFragmentDestroyListener mFragmentDestroyListener;

    @BindView(R.id.search_results_list)RecyclerView mRecyclerView;
    @BindView(R.id.search_edittext)EditText searchTextbox;
    @BindView(R.id.clear_search_edittext)Button clearSearchTextbox;
    private WordAdapter mAdapter;
    private Unbinder mUnbinder;

    public WordAdapter getRecyclerViewAdapter() {
        return mAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        mAdapter = new WordAdapter(new ArrayList<JTDicWord>());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d("MainActivity", "item count is " + mAdapter.getItemCount());
                Log.d("MainActivity", "position is " + position);
                JTDicWord selectedWord = mAdapter.getWordList().get(position);
                Intent intent = new Intent(getActivity(), WordDetailActivity.class);
                intent.putExtra(Constant.JTDICWord_ID, selectedWord.getId());
                intent.putExtra(Constant.JTDICWord_KANJI, selectedWord.getKanji());
                intent.putExtra(Constant.JTDICWord_KANA, selectedWord.getKana());
                intent.putExtra(Constant.JTDICWord_MEANING, selectedWord.getMeaning());
                intent.putExtra(Constant.JTDICWord_TYPE, selectedWord.getType());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        KeywordTextWatcher keywordTextWatcher = new KeywordTextWatcher(this);
        searchTextbox.addTextChangedListener(keywordTextWatcher);
        mFragmentDestroyListener = keywordTextWatcher;
        clearSearchTextbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTextbox.setText("");
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        RefWatcher refWatcher = JTDICApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
        mFragmentDestroyListener.onFragmentDestroy();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public interface OnFragmentDestroyListener {
        void onFragmentDestroy();
    }

    private static class KeywordTextWatcher implements TextWatcher, OnFragmentDestroyListener {
        private Timer timer;
        private final long DELAY = 1500;
        private Handler handler = new SearchAndUpdateHandler(Looper.getMainLooper());

        public KeywordTextWatcher(SearchFragment searchFragment) {
            this.searchFragment = searchFragment;
        }

        private SearchFragment searchFragment;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (timer != null) {
                timer.cancel();
            }
        }

        @Override
        public void afterTextChanged(final Editable s) {
            if (s.toString().length() == 0) {
                searchFragment.clearSearchTextbox.setVisibility(View.GONE);
                return;
            }

            searchFragment.clearSearchTextbox.setVisibility(View.VISIBLE);

            if (!JapaneseUtil.isJapanese(s.toString())) {
                return;
            }

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    String keyword = s.toString();
                    JTDicDb dbHelper = new JTDicDb(searchFragment.getActivity());
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    String queryString = null;

                    if (JapaneseUtil.isAllKana(keyword)) {
                        queryString = "select * from jtdic where yomikata like ?";
                    } else {
                        queryString = "select * from jtdic where kanji like ?";
                    }

                    Cursor c = db.rawQuery(queryString, new String[] { keyword + "%" });
                    List<JTDicWord> adapterWordList = searchFragment.getRecyclerViewAdapter().getWordList();
                    adapterWordList.clear();

                    while (c.moveToNext()) {
                        JTDicWord word = new JTDicWord(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
                        adapterWordList.add(word);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            searchFragment.getRecyclerViewAdapter().notifyDataSetChanged();
                        }
                    });

                    c.close();
                    db.close();
                }
            }, DELAY);
        }

        @Override
        public void onFragmentDestroy() {
            if (timer != null) {
                timer.cancel();
            }
        }

        private static class SearchAndUpdateHandler extends Handler {

            public SearchAndUpdateHandler(Looper looper) {
                super(looper);
            }
        }
    }
}
