package orgs.androidtown.jsondata;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.List;

import orgs.androidtown.jsondata.model.User;

/**
 * Created by Jisang on 2017-10-16.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.Holder>{
    Context context;
    List<User> data;
    public ListAdapter(List<User> data, Context context){
        this.data = data;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        User user = data.get(position);
        //holder.imageView.setImageURI();
        holder.textId.setText(user.getId()+"");
        holder.textLogin.setText(user.getLogin());
        // 이미지 불러오기
        Glide.with(context)                 // 글라이드 초기화
                .load(user.getAvatar_url()) // 주소에서 이미지 가져오기
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textId;
        TextView textLogin;
        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textId = itemView.findViewById(R.id.textId);
            textLogin = itemView.findViewById(R.id.textLogin);
        }
    }
}
