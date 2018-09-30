package com.cyut.toolbox.toolbox;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.qiscus.sdk.Qiscus;
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom;
import com.qiscus.sdk.chat.core.data.model.QiscusNonce;
import com.qiscus.sdk.chat.core.data.remote.QiscusApi;
import com.qiscus.sdk.chat.core.util.QiscusRxExecutor;


import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatroomFragment extends android.support.v4.app.Fragment {
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


        recyclerView = (RecyclerView)view.findViewById(R.id.ryv_msglist);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(view.getContext()));
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        QiscusRxExecutor.execute(QiscusApi.getInstance().requestNonce(), new QiscusRxExecutor.Listener<QiscusNonce>() {
            @Override
            public void onSuccess(QiscusNonce qiscusNonce) {
                QiscusRxExecutor.execute(QiscusApi.getInstance().getChatRooms(1, 20, true), new QiscusRxExecutor.Listener<List<QiscusChatRoom>>() {
                    @Override
                    public void onSuccess(List<QiscusChatRoom> qiscusChatRooms) {
                        //Success getting the rooms
                        adapter = new RecyclerViewAdapterMsgList(view.getContext(), qiscusChatRooms);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        //Something went wrong
                        Log.d(TAG, "onError: "+throwable);
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                //do anything if error occurred
            }
        });




        return view;
    }

}
