package com.zmx.mian.fragment.memberfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zmx.mian.R;
import com.zmx.mian.bean.members.MembersExchange;
import com.zmx.mian.util.Tools;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-03 14:32
 * 类功能：签到记录
 */

public class MembersSignFragment  extends Fragment {

    private List<String> list;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MembersSignFragment.TextAdapter adapter = new MembersSignFragment.TextAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        list = (List<String>) getArguments().getSerializable("sign");
    }

    public class TextAdapter extends RecyclerView.Adapter<MembersSignFragment.TextViewHolder> {

        @Override
        public MembersSignFragment.TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate
                    (R.layout.members_sign_fragment, parent, false);
            return new MembersSignFragment.TextViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MembersSignFragment.TextViewHolder holder, int position) {
            holder.mTextView.setText(Tools.refFormatNowDate(list.get(position),1) + "");
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public TextViewHolder(View itemView) {
            super(itemView);
            mTextView =  itemView.findViewById(R.id.s_time_text);
        }
    }


}
