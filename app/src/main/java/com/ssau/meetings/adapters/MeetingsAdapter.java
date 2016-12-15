package com.ssau.meetings.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssau.meetings.MeetActivity;
import com.ssau.meetings.R;
import com.ssau.meetings.database.Meet;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.ViewHolder> {

    public interface OnDeleteButtonClickListener extends View.OnClickListener {
        void onDeleteClicked(String itemId);
    }

    private List<Meet> meetings;
    private OnDeleteButtonClickListener listener;
    private final HashMap<String, Meet> meetHashMap = new HashMap<>();
    private Comparator dateComparater = new Comparator<Meet>() {
        @Override
        public int compare(Meet o1, Meet o2) {
            try {
                return o1.getStartDate().compareTo(o2.getStartDate());
            } catch (ParseException e) {
                return 0;
            }
        }
    };

    public MeetingsAdapter(List<Meet> meetings, OnDeleteButtonClickListener listener) {
        this.meetings = meetings;
        this.listener = listener;
        reSort();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_meet, parent, false);
        return new ViewHolder(v.getContext(), v);
    }

    public void addItem(String key, Meet meet) {
        meet.id = key;
        if (!meetHashMap.containsKey(key)) {
            this.meetings.add(meet);
            meetHashMap.put(key, meet);
            notifyItemInserted(meetings.size() - 1);
            reSort();
        }
    }

    public void removeItem(String key) {
        int pos = meetings.indexOf(meetHashMap.get(key));
        if (pos >= 0) {
            meetings.remove(pos);
            meetHashMap.remove(key);
            notifyItemRemoved(pos);
            reSort();
        }
    }

    public void changeItem(String key, Meet meet) {
        int pos = meetings.indexOf(meetHashMap.get(key));
        meetHashMap.put(key, meet);
        meetings.remove(pos);
        meetings.add(pos, meetHashMap.get(key));

        reSort();
    }

    private void reSort() {
        Collections.sort(meetings, dateComparater);
        notifyItemRangeChanged(0, meetings.size() - 1);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Meet meet = meetings.get(position);
        if (meet != null) {
            holder.title.setText(meet.title);
            holder.description.setText(meet.description);
            holder.deleteMeetingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteClicked(meetings.get(position).id);
                }
            });
            holder.meetCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MeetActivity.startMe(v.getContext(), meet.id);
                }
            });
            try {
                holder.date.setText(holder.parentContext.getString(R.string.meeting_date, Meet.CARD_FORMATTER.format(meet.getStartDate()), Meet.CARD_FORMATTER.format(meet.getEndDate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView title;
        private AppCompatTextView description;
        private CardView meetCard;
        private AppCompatTextView date;
        private Context parentContext;
        private AppCompatImageButton deleteMeetingButton;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            parentContext = context;
            deleteMeetingButton = (AppCompatImageButton) itemView.findViewById(R.id.delete_meeting_button);
            title = (AppCompatTextView) itemView.findViewById(R.id.meet_title);
            description = (AppCompatTextView) itemView.findViewById(R.id.meet_description);
            meetCard = (CardView) itemView.findViewById(R.id.meet_card);
            date = (AppCompatTextView) itemView.findViewById(R.id.meet_date);
        }
    }
}
