package com.example.to_dolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.to_dolist.Adapter.TodoAdapter;
import com.example.to_dolist.Utls.DatabaseHandler;
import com.example.to_dolist.model.TodoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DiaolgCloseListener {
    private RecyclerView taskRecylerView;
    private TodoAdapter taskAdapter;
    private FloatingActionButton fab;
    private List<TodoModel> taskList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        db.openDatabase();

        taskList = new ArrayList<>();
        taskRecylerView = findViewById(R.id.tasksRyclerview);
        taskRecylerView.setLayoutManager(new LinearLayoutManager(this));

        taskAdapter = new TodoAdapter(db, this);
        taskRecylerView.setAdapter(taskAdapter);

        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        taskAdapter.setTasks(taskList);

        // Initialize and attach the ItemTouchHelper here
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecycleritemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecylerView);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        taskAdapter.setTasks(taskList);
        taskAdapter.notifyDataSetChanged();
    }
}
