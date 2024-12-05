package com.example.to_dolist.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_dolist.AddNewTask;
import com.example.to_dolist.MainActivity;
import com.example.to_dolist.R;
import com.example.to_dolist.Utls.DatabaseHandler;
import com.example.to_dolist.model.TodoModel;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<TodoModel> todoList;
    private MainActivity activity;

   private DatabaseHandler db;

    // Constructor
    public TodoAdapter( DatabaseHandler db,  MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    // Set the data for the adapter
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false); // Use correct layout name
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        db.openDatabase();
        TodoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus())); // Convert integer status to boolean
       holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   db.updateStatus(item.getId(),1);
               }
               else{
                   db.updateStatus(item.getId(),0);
               }
           }
       });


    }

    private boolean toBoolean(int n) {
        return n != 0; // Return true if status is non-zero
    }



    @Override
    public int getItemCount() {
        return (todoList == null) ? 0 : todoList.size(); // Prevent NullPointerException
    }

    // ViewHolder class


    public void setTasks(List<TodoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public Context getContext(){
        return activity ;
    }

    public void deletItem(int position){
        TodoModel item = todoList.get(position);
          db.deleteTask(item.getId());
          todoList.remove(position);
          notifyItemRemoved(position);
    }

    public void editItem(int position){
        TodoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(),AddNewTask.TAG);

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        public ViewHolder(@NonNull View view) {
            super(view);
            task = view.findViewById(R.id.todocheckbox); // Ensure this ID matches your XML
        }
    }
}
