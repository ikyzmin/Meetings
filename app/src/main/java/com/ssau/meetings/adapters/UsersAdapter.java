package com.ssau.meetings.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ssau.meetings.R;
import com.ssau.meetings.database.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Илья on 09.12.2016.
 */

public class UsersAdapter extends BaseAdapter {


    private Context context;
    private List<User> users = new ArrayList<>();

    public UsersAdapter(Context context) {
        this.context = context;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return users != null ? users.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.i_user, parent, false);
        }
        User user = (User) getItem(position);
        if (user != null) {
            AppCompatTextView userName = (AppCompatTextView) view.findViewById(R.id.user_name);
            AppCompatTextView userPosition = (AppCompatTextView) view.findViewById(R.id.user_position);
            userName.setText(user.name);
            userPosition.setText(user.position);
        }
        return view;
    }
}
