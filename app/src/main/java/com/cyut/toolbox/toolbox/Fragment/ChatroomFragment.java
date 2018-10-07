package com.cyut.toolbox.toolbox.Fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;


import com.cyut.toolbox.toolbox.R;
import com.cyut.toolbox.toolbox.adapter.RecyclerViewAdapterMsgList;
import com.cyut.toolbox.toolbox.SimpleDividerItemDecoration;
import com.qiscus.sdk.Qiscus;
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom;
import com.qiscus.sdk.chat.core.data.remote.QiscusApi;
import com.qiscus.sdk.chat.core.util.QiscusRxExecutor;



import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatroomFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    protected static RecyclerViewAdapterMsgList adapter;

    private ArrayAdapter<String> listAdapter;
    public ChatroomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_chatroom, container, false);

        FloatingActionButton floatingActionButton=getActivity().findViewById(R.id.fab);
        floatingActionButton.setVisibility(View.GONE);
        recyclerView = (RecyclerView)view.findViewById(R.id.ryv_msglist);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(view.getContext()));
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);


        QiscusApi.getInstance().getChatRooms(1, 20, true)
                .doOnNext(chatRooms -> {
                    for (QiscusChatRoom chatRoom : chatRooms) {
                        Qiscus.getDataStore().addOrUpdate(chatRoom);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(chatRooms -> {
                    //success
                    adapter = new RecyclerViewAdapterMsgList(view.getContext(), chatRooms);
                    recyclerView.setAdapter(adapter);
                }, throwable -> {
                    //error
                });


        return view;
    }

}
