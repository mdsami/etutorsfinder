package com.finalproject.app.findingtutors;


import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.app.findingtutors.adapter.TeacherListAdapter;
import com.finalproject.app.findingtutors.model.Teacher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.finalproject.app.findingtutors.utils.Config.PREFS_NAME;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherListFragment extends Fragment implements ValueEventListener, SearchView.OnQueryTextListener {


    private RecyclerView listView;
    private FirebaseUser user;
    private boolean type;
    private Context context;
    private ArrayList<Teacher> arrayList;
    private SwipeRefreshLayout refreshLayout;
    private FirebaseAuth auth;

    public TeacherListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_teacher_list, container, false);

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        type = prefs.getBoolean("type", false);

        arrayList = new ArrayList<>();

        listView = rootView.findViewById(R.id.listView);

        refreshLayout = rootView.findViewById(R.id.swipeRefresh);
        refreshLayout.setRefreshing(true);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listView.setAdapter(new TeacherListAdapter(getActivity(), arrayList));
                refreshLayout.setRefreshing(false);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(layoutManager);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        if (type){
            database.child("users").child("Teacher").addValueEventListener(this);
        }

        return rootView;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot data : dataSnapshot.getChildren()){
            Teacher teacher = data.getValue(Teacher.class);
            arrayList.add(teacher);
        }
        TeacherListAdapter adapter = new TeacherListAdapter(getActivity(), arrayList);
        listView.setAdapter(adapter);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        SearchManager searchManager =
                (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_search:
                //do something
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Teacher> filterByAddress(String query){
        ArrayList<Teacher> filterList = new ArrayList<>();
        String queryLowerCase = query.toLowerCase();

        for (Teacher teacher : arrayList){
            String address = teacher.getAddress().toLowerCase();
            if (address.contains(queryLowerCase)){
                filterList.add(teacher);
            }
        }
        return filterList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        listView.setAdapter(new TeacherListAdapter(getActivity(), filterByAddress(query)));
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
