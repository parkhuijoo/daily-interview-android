package kr.huijoo.dailyinterview.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import kr.huijoo.dailyinterview.R;
import kr.huijoo.dailyinterview.model.User;

public class SignInActivity extends AppCompatActivity {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Button btn;
    EditText name;
    EditText birthDate;
    boolean already = false;
    boolean canLogin = false;
    String tryName = "";
    String tryBirth = "";
    ArrayList<User> nowUserList = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nowUserList.clear();
                for (DataSnapshot fileSnapshot : dataSnapshot.child("User").getChildren()) {
                    nowUserList.add(
                            new User(
                                    fileSnapshot.child("birth").getValue(String.class),
                                    fileSnapshot.child("name").getValue(String.class),
                                    fileSnapshot.child("company").getValue(String.class),
                                    fileSnapshot.child("position").getValue(String.class)
                            )
                    );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });

        pref = getSharedPreferences("kr.huijoo.dailyinterview", MODE_PRIVATE);
        editor = pref.edit();

        btn = findViewById(R.id.sign_in_btn);
        name = findViewById(R.id.user_name);
        birthDate = findViewById(R.id.birth_date);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryName = name.getText().toString();
                tryBirth = birthDate.getText().toString();
                if (tryName.isEmpty() || tryBirth.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tryBirth.length() != 6) {
                    Toast.makeText(getApplicationContext(), "생일 정보가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String targetBirth = "";

                for (User user : nowUserList) {
                    if (user.getName().equals(tryName)) {
                        already = true;
                        targetBirth = user.getBirth();
                        break;
                    }
                }

                if (already && targetBirth.equals(tryBirth)) {
                    canLogin = true;
                }

                if (already && canLogin) {
                    Toast.makeText(getApplicationContext(), "기존 계정으로 로그인 했습니다!", Toast.LENGTH_SHORT).show();
                    editor.putString("userName", name.getText().toString());
                    editor.apply();
                    finish();
                } else if (already && !canLogin) {
                    Toast.makeText(getApplicationContext(), "생일 정보를 다시 확인하세요!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "새로운 계정으로 회원가입 했습니다!", Toast.LENGTH_SHORT).show();
                    editor.putString("userName", name.getText().toString());
                    editor.apply();
                    User forSave = new User(birthDate.getText().toString(), name.getText().toString(), "희망 회사", "희망 직렬");
                    mDatabase.child("User").child(name.getText().toString()).setValue(forSave);
                    finish();
                }
            }
        });
    }
}