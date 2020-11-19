package kr.huijoo.dailyinterview.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.nitrico.lastadapter.BR;
import com.github.nitrico.lastadapter.LastAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import kr.huijoo.dailyinterview.R;
import kr.huijoo.dailyinterview.model.Comment;
import kr.huijoo.dailyinterview.model.QList;

public class QuestionActivity extends AppCompatActivity {

    private Intent intent;
    private String title;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    ArrayList<Comment> commentsArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    LastAdapter adapter;
    long startTime;
    SharedPreferences pref;
    String currUserName;
    String currKey;
    ValueEventListener listener;
    Boolean found = false;

    @Override
    protected void onStart() {
        super.onStart();
        startTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!found) {
            Toast.makeText(getApplicationContext(), "작성을 포기하셨습니다 :(", Toast.LENGTH_SHORT).show();
            mDatabase.child("List").child(currKey).child("Comment").child(currUserName).setValue(
                    new Comment("작성을 포기했습니다.", currUserName)
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        listener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commentsArrayList.clear();
                QList temp = new QList();
                for (DataSnapshot fileSnapshot : dataSnapshot.child("List").getChildren()) {
                    if (fileSnapshot.child("ListTitle").getValue(String.class).equals(title)) {
                        currKey = fileSnapshot.getKey();
                        temp.setListdate(fileSnapshot.child("ListDate").getValue(String.class));
                        temp.setListimg(fileSnapshot.child("ListImg").getValue(String.class));
                        temp.setListtitle(fileSnapshot.child("ListTitle").getValue(String.class));
                        for (DataSnapshot comment : fileSnapshot.child("Comment").getChildren()) {
                            Comment tempComment = new Comment();
                            tempComment.setContent(comment.child("content").getValue(String.class));
                            tempComment.setName(comment.child("name").getValue(String.class));
                            commentsArrayList.add(tempComment);
                        }
                        adapter.notifyDataSetChanged();
                        found = false;
                        for (Comment comment : commentsArrayList) {
                            if (comment.getName().equals(currUserName)) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            findViewById(R.id.other_layout).setVisibility(View.VISIBLE);
                            findViewById(R.id.answer_layout).setVisibility(View.INVISIBLE);
                        } else {
                            findViewById(R.id.other_layout).setVisibility(View.INVISIBLE);
                            findViewById(R.id.answer_layout).setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                }
                TextView date = findViewById(R.id.tv_dateholder);
                date.setText(temp.getListdate() + " Question");
                TextView question = findViewById(R.id.tv_titleholder);
                question.setText(temp.getListtitle());
                ImageView img = findViewById(R.id.ivholder);
                Glide.with(img.getContext())
                        .load(temp.getListimg())
                        .optionalCenterCrop()
                        .into(img);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDatabase.removeEventListener(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        pref = getSharedPreferences("kr.huijoo.dailyinterview", MODE_PRIVATE);
        currUserName = pref.getString("userName", "");
        intent = getIntent();
        title = intent.getStringExtra("title");
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LastAdapter(commentsArrayList, BR.listContent)
                .map(Comment.class, R.layout.comment_item)
                .into(recyclerView);
        adapter.notifyDataSetChanged();

        final EditText editText = findViewById(R.id.answer_edittext);

        Button saveBtn = findViewById(R.id.answer_mode);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - startTime >= 180) {
                    Toast.makeText(getApplicationContext(), "답변 작성 시간을 초과하셨습니다 :(", Toast.LENGTH_SHORT).show();
                    mDatabase.child("List").child(currKey).child("Comment").child(currUserName).setValue(
                            new Comment("답변 작성 시간을 초과하였습니다.", currUserName)
                    );
                } else if (text.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "답변을 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "답변이 등록되었습니다 :)", Toast.LENGTH_SHORT).show();
                    mDatabase.child("List").child(currKey).child("Comment").child(currUserName).setValue(
                            new Comment(text, currUserName)
                    );
                }
            }
        });
    }
}