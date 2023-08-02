package com.example.qexamworld;

import static com.example.qexamworld.DbQuery.g_usersCount;
import static com.example.qexamworld.DbQuery.g_usersList;
import static com.example.qexamworld.DbQuery.myPerformance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeaderBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderBoardFragment extends Fragment {

    private TextView totalUsers, myImgText, myScore, myRank;
    private RecyclerView usersViwe;

    private RankAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LeaderBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeaderBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaderBoardFragment newInstance(String param1, String param2) {
        LeaderBoardFragment fragment = new LeaderBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);


        ((MainActivity)getActivity()).getSupportActionBar().setTitle("LeaderBoard");



        initViews(view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersViwe.setLayoutManager(layoutManager);

        adapter = new RankAdapter(DbQuery.g_usersList);

        usersViwe.setAdapter(adapter);

//        DbQuery.getTopUsers(new MyCompleteListener() {
//            @Override
//            public void onSuccess() {
//
//
//                adapter.notifyDataSetChanged();
//                if (myPerformance.getScore() != 0){
//                    if ( !DbQuery.isMeonTopList){
//
//                    }
//
//                    myScore.setText("Score : " + myPerformance.getScore());
//                    myRank.setText("Rank - " + myPerformance.getRank());
//
//
//                }
//            }
//
//            @Override
//            public void onFailure() {
//
//            }
//        });



//        totalUsers.setText("Total Users : " + DbQuery.g_usersCount);

        return view;
    }

    private void  initViews(View view){
        totalUsers = view.findViewById(R.id.total_users);
        myImgText = view.findViewById(R.id.img_text);
        myScore = view.findViewById(R.id.total_score);
        myRank = view.findViewById(R.id.rank);
        usersViwe = view.findViewById(R.id.user_view);

    }



}