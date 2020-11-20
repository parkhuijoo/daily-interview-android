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

/**
 * QuestionActivity.java
 * 작성자 : 박희주
 * V1.0
 */

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

    /**
     * 액티비티가 생성될 때의 시간을 초단위로 기억(3분 타이머 계산용)
     */
    @Override
    protected void onStart() {
        super.onStart();
        startTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }

    /**
     * 액티비티가 종료될 때, 아직 내 답변이 작성되지 않았다면, 작성포기 Firebase DB에 추가
     */
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

    /**
     * Firebase DB 데이터 체인지 리스너는 백그라운드에서 돌면 안되므로, Resume에서 재할당
     */
    @Override
    protected void onResume() {
        super.onResume();
        listener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 기존 리스트(어댑터에 물려있는)는 비우고 변경된 데이터들을 넣은 뒤 어댑터에 notify
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

                        // 만약 내 댓글을 작성했을 경우, 답변 작성 UI는 숨기고 다른 사람들의 답변 목록을 보여줌
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

                // 질문 UI에 데이터 주입
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

    /**
     * Firebase DB 데이터 체인지 리스너는 백그라운드에서 돌면 안되므로, Pause에서 제거
     */
    @Override
    protected void onPause() {
        super.onPause();
        mDatabase.removeEventListener(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // 현재 로그인된 User의 이름 정보 가져옴 from 프레퍼런스
        pref = getSharedPreferences("kr.huijoo.dailyinterview", MODE_PRIVATE);
        currUserName = pref.getString("userName", "");

        // Intent에서 넘어오는 질문 제목 가져옴
        intent = getIntent();
        title = intent.getStringExtra("title");

        // RecyclerView와 어댑터 생성
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LastAdapter(commentsArrayList, BR.listContent)
                .map(Comment.class, R.layout.comment_item)
                .into(recyclerView);
        adapter.notifyDataSetChanged();

        final EditText editText = findViewById(R.id.answer_edittext);

        Button saveBtn = findViewById(R.id.answer_mode);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            // 저장 버튼 클릭 리스너 부착, 3분 초과시 답변 작성 초과 DB에 저장, 그외의 경우 정상적으로 답변 등록
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