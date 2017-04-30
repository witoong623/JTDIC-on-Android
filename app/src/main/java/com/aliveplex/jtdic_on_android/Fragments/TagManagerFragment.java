package com.aliveplex.jtdic_on_android.Fragments;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.aliveplex.jtdic_on_android.Adapters.TagListAdapter;
import com.aliveplex.jtdic_on_android.AddTagActivity;
import com.aliveplex.jtdic_on_android.Database.AppDb;
import com.aliveplex.jtdic_on_android.Interfaces.ClickListener;
import com.aliveplex.jtdic_on_android.JTDICApplication;
import com.aliveplex.jtdic_on_android.Listeners.RecyclerTouchListener;
import com.aliveplex.jtdic_on_android.Models.Tag;
import com.aliveplex.jtdic_on_android.R;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TagManagerFragment extends Fragment {
    @BindView(R.id.tag_list)RecyclerView mRecyclerView;
    @BindView(R.id.fab_add_new_tag)FloatingActionButton mFab;
    private TagListAdapter tagListAdapter;
    private Unbinder mUnbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_manager, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        tagListAdapter = new TagListAdapter(new ArrayList<Tag>());
        mRecyclerView.setAdapter(tagListAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Tag tag = tagListAdapter.getTagList().get(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTagActivity.class);
                startActivity(intent);
            }
        });

        LoadTagListTask loadTagListTask = new LoadTagListTask(this);
        loadTagListTask.execute((Void)null);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        RefWatcher refWatcher = JTDICApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    private static class LoadTagListTask extends AsyncTask<Void, Void, List<Tag>> {
        private TagManagerFragment tagManagerFragment;

        public LoadTagListTask(TagManagerFragment tagManagerFragment) {
            this.tagManagerFragment = tagManagerFragment;
        }

        @Override
        protected List<Tag> doInBackground(Void... params) {
            List<Tag> tagList = new ArrayList<>();
            AppDb appDb = new AppDb(tagManagerFragment.getActivity());
            SQLiteDatabase db = appDb.getReadableDatabase();

            try {
                Cursor cursor = db.rawQuery("select * from tags", null);

                while (cursor.moveToNext()) {
                    Tag tag = new Tag(cursor.getString(1), cursor.getString(2));
                    tag.setId(cursor.getInt(0));
                    tagList.add(tag);
                }

                cursor.close();
            } catch (Exception e) {
                Log.d("TagManagerFragment", "Exception: " + e.getMessage());
            } finally {
                db.close();
            }

            return tagList;
        }

        @Override
        protected void onPostExecute(List<Tag> tagList) {
            tagManagerFragment.tagListAdapter.getTagList().addAll(tagList);
            tagManagerFragment.tagListAdapter.notifyDataSetChanged();
        }


    }
}
