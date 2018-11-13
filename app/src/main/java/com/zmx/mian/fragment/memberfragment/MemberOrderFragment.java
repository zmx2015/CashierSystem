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
import com.zmx.mian.bean.members.MembersOrder;
import com.zmx.mian.util.Tools;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-03 14:39
 * 类功能：订单记录
 */

public class MemberOrderFragment extends Fragment {

    private List<MembersOrder> mos;
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

        MemberOrderFragment.TextAdapter adapter = new MemberOrderFragment.TextAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.scrollToPosition(0);//回到顶部
        mos = (List<MembersOrder>) getArguments().getSerializable("orders");

    }

    public class TextAdapter extends RecyclerView.Adapter<MemberOrderFragment.TextViewHolder> {

        @Override
        public MemberOrderFragment.TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate
                    (R.layout.member_order_fragment, parent, false);
            return new MemberOrderFragment.TextViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MemberOrderFragment.TextViewHolder holder, int position) {
            holder.total_textview.setText("￥"+mos.get(position).getTotal() );
            holder.time_text.setText(Tools.refFormatNowDate(mos.get(position).getBuytime(),1));
            holder.order_munbers.setText("订单编号："+mos.get(position).getOrder());
        }

        @Override
        public int getItemCount() {
            return mos.size();
        }
    }
    public class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView total_textview,time_text,order_munbers;

        public TextViewHolder(View itemView) {
            super(itemView);
            total_textview =  itemView.findViewById(R.id.total_textview);
            time_text =  itemView.findViewById(R.id.time_text);
            order_munbers =  itemView.findViewById(R.id.order_munbers);
        }
    }


}
