package com.duetbd.cse18.myapplication.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duetbd.cse18.myapplication.QusData;
import com.duetbd.cse18.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class UserQusListAdapter extends RecyclerView.Adapter<UserQusListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<QusData> arrayList;

    public UserQusListAdapter(Context context, ArrayList<QusData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_user_qus,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.question.setText(arrayList.get(position).getQuestion());
        holder.rb1.setText(arrayList.get(position).getOption1());
        holder.rb2.setText(arrayList.get(position).getOption2());
        holder.rb3.setText(arrayList.get(position).getOption3());
        holder.rb4.setText(arrayList.get(position).getOption4());

        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.singleUserRadio1:
                        Toast.makeText(context, holder.rb1.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.singleUserRadio2:
                        Toast.makeText(context, holder.rb2.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.singleUserRadio3:
                        Toast.makeText(context, holder.rb3.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.singleUserRadio4:
                        Toast.makeText(context, holder.rb4.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                }

            }
        });

        holder.itemView.setOnLongClickListener(view -> {

            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("Want to delete?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteItem(position);
                        }
                    })
                    .setNegativeButton("No",null).create().show();


            return true;
        });

    }

    private void deleteItem(int position) {
        arrayList.get(position).getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });
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

        TextView question;
        RadioGroup radioGroup;
        RadioButton rb1, rb2, rb3, rb4;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question=itemView.findViewById(R.id.singleUserQueEdiText);
            radioGroup=itemView.findViewById(R.id.singleUserRadioGroup);
            rb1=itemView.findViewById(R.id.singleUserRadio1);
            rb2=itemView.findViewById(R.id.singleUserRadio2);
            rb3=itemView.findViewById(R.id.singleUserRadio3);
            rb4=itemView.findViewById(R.id.singleUserRadio4);

        }
    }
}
