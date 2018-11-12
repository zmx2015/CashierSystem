package com.zmx.mian.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zmx.mian.R;
import com.zmx.mian.bean.GoodsItemRankingBean;
import com.zmx.mian.bean.members.MembersList;
import com.zmx.mian.ui.util.GlideCircleTransform;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-08-26 15:34
 * 类功能：会员列表适配器
 */
public class MembersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<MembersList> lists;

    public MembersListAdapter(Context context, List<MembersList> lists) {
        mContext = context;
        this.lists = lists;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MembersListAdapter.MyViewHolder(mLayoutInflater.inflate(R.layout.members_list_adapter, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MembersListAdapter.MyViewHolder viewholder = (MembersListAdapter.MyViewHolder) holder;
        MembersList m = lists.get(position);

        if(m.getWechatName() == null){

        viewholder.members_name.setText("水果大户");

        }else{

            viewholder.members_name.setText(m.getWechatName());

        }
        viewholder.members_number.setText("会员账号："+m.getAccount()+"   积分："+m.getIntegral());
        Glide.with(mContext).load(m.getWechatImg()).transform(new GlideCircleTransform(mContext)).error(R.drawable.icon_login_account).into(viewholder.members_head);

    }

    @Override
    public int getItemCount() {

            return lists == null ? 0 : lists.size();

    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView  members_name, members_number;
        ImageView members_head;

        public MyViewHolder(View itemView) {
            super(itemView);
            members_head = itemView.findViewById(R.id.members_head);
            members_name = (TextView) itemView.findViewById(R.id.members_name);
            members_number = (TextView) itemView.findViewById(R.id.members_number);
        }
    }


}
