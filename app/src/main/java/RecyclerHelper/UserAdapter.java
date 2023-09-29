package RecyclerHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Srini.geek_mooc.R;;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

import Backend.UserHelper;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{

    Context context;
    ArrayList<UserHelper> users;

    public UserAdapter(Context context, ArrayList<UserHelper> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_userlist,parent,false);
        return new UserAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserHelper user = users.get(position);
        holder.vName.setText(user.getName());
        holder.vPhone.setText(user.getMobile());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView vName,vPhone;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vName = itemView.findViewById(R.id.name);
            vPhone = itemView.findViewById(R.id.mobile);
        }
    }
}
