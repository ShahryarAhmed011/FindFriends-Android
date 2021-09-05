package com.neurotech.findfriends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NumbersListAdapter  extends RecyclerView.Adapter<NumbersListAdapter.ViewHolder>{

    ArrayList<ContactNumber> mContactNumbersList;
    public Context context;



    public NumbersListAdapter(Context context,ArrayList<ContactNumber> mContactNumber) {

        this.context = context;
        this.mContactNumbersList = mContactNumber;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.number_list_item, parent, false);
        NumbersListAdapter.ViewHolder holder = new NumbersListAdapter.ViewHolder(view);

        return holder;
    }


    private int selectedPos = RecyclerView.NO_POSITION;
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final ContactNumber contactNumber = mContactNumbersList.get(position);

        holder.mDisplayName.setText(contactNumber.getmDisplayName());
        holder.mNumber.setText(contactNumber.getmPhoneNumber());




    }

    @Override
    public int getItemCount() {
        return mContactNumbersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView mDisplayName;
        public TextView mNumber;


        public ViewHolder(View itemView) {
            super(itemView);

            mDisplayName = itemView.findViewById(R.id.display_name_list_item);
            mNumber = itemView.findViewById(R.id.phone_number_list_item);

        }
    }

}
