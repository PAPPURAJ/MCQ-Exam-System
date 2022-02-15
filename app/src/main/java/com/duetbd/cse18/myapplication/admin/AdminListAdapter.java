package com.duetbd.cse18.myapplication.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duetbd.cse18.myapplication.QusData;
import com.duetbd.cse18.myapplication.R;

import java.util.ArrayList;

public class AdminListAdapter extends RecyclerView.Adapter<AdminListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<QusData> arrayList;

    public AdminListAdapter(Context context, ArrayList<QusData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_admin_qus,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.question.setText(arrayList.get(position).getQuestion());
        holder.q1.setText(arrayList.get(position).getOption1());
        holder.q2.setText(arrayList.get(position).getOption2());
        holder.q3.setText(arrayList.get(position).getOption3());
        holder.q4.setText(arrayList.get(position).getOption4());

    }

    void setData(ArrayList<QusData> arrayList){
        this.arrayList=arrayList;
        notifyDataSetChanged();
    }

    void addItem(QusData qusData){
        arrayList.add(qusData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView question, q1,q2,q3,q4;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question=itemView.findViewById(R.id.singleAdminQueEdiText);
            q1=itemView.findViewById(R.id.singleAdminOp1EdiText);
            q2=itemView.findViewById(R.id.singleAdminOp2EdiText);
            q3=itemView.findViewById(R.id.singleAdminOp3EdiText);
            q4=itemView.findViewById(R.id.singleAdminOp4EdiText);
        }
    }
}
