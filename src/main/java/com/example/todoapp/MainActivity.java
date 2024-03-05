package com.example.todoapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.Collections;
import java.util.List;


import android.content.DialogInterface;
import android.os.Bundle;



import com.example.todoapp.Adapter.ToDoAdapter;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    RecyclerView tasksRecyclerView;
    ToDoAdapter tasksAdapter;
    FloatingActionButton fab;

    private List<ToDoModel> taskList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        db =new DatabaseHandler(this);
        db.openDatabase();


        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tasksAdapter = new ToDoAdapter (db,MainActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);



        ItemTouchHelper itemTouchHelper= new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);
        fab = findViewById(R.id.fab);
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);

        fab.setOnClickListener(v -> AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG));



    }
    @Override
    public void handleDialogClose(DialogInterface dialog){
        int newPosition = taskList.size()-1;
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyItemChanged(newPosition);
        tasksAdapter.notifyItemRemoved(newPosition);
        tasksAdapter.notifyItemInserted(newPosition);
    }
}