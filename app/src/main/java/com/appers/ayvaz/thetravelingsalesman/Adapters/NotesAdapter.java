package com.appers.ayvaz.thetravelingsalesman.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.R;

/**
 * Created by D on 12/05/2015.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    //// TODO: 12/05/2015  replace with correct data structure
    private String[] notes;
    private String[] noteTitles;

    public static class ViewHolder extends  RecyclerView.ViewHolder {
        private TextView mNumber;
        private TextView mTitle;
        private TextView mContent;

        public ViewHolder(View itemView) {
            super(itemView);
            mNumber = (TextView) itemView.findViewById(R.id.note_no);
            mTitle = (TextView) itemView.findViewById(R.id.note_title);
            mContent = (TextView) itemView.findViewById(R.id.note_content);

        }
    }

    public NotesAdapter() {
        notes = new String[]{"Lorem ipsum", "Phasellus"};
        noteTitles = new String[]{"Callout note", "Callout note 2"};

    }
    @Override
    public int getItemCount() {
        return notes.length;
    }

    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_note_card, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitle.setText(noteTitles[position]);
        holder.mNumber.setText(Integer.toString(position+1));
        holder.mContent.setText(notes[position]);
    }


}
