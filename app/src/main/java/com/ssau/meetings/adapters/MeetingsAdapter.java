package com.ssau.meetings.adapters;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssau.meetings.R;
import com.ssau.meetings.database.Meet;

import java.util.HashMap;
import java.util.List;

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.ViewHolder> {

    private List<Meet> meetings;
    private final HashMap<String, Meet> meetHashMap = new HashMap<>();

    public MeetingsAdapter(List<Meet> meetings) {
        this.meetings = meetings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_meet, parent, false);
        return new ViewHolder(v);
    }

    public void addItem(String key, Meet meet) {
        this.meetings.add(meet);
        meetHashMap.put(key, meet);
        notifyItemInserted(meetings.size() - 1);
    }

    public void removeItem(String key) {
        int pos = meetings.indexOf(meetHashMap.get(key));
        meetings.remove(pos);
        meetHashMap.remove(key);
        notifyItemRemoved(pos);
    }

    public void changeItem(String key, Meet meet) {
        int pos = meetings.indexOf(meetHashMap.get(key));
        meetHashMap.put(key, meet);
        meetings.remove(pos);
        meetings.add(pos, meetHashMap.get(key));
        notifyItemChanged(pos);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Meet meet = meetings.get(position);
        if (meet != null) {
            holder.title.setText(meet.title);
            holder.description.setText(meet.description);
        }
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView title;
        private AppCompatTextView description;
        private AppCompatTextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (AppCompatTextView) itemView.findViewById(R.id.meet_title);
            description = (AppCompatTextView) itemView.findViewById(R.id.meet_description);
        }
    }
}
