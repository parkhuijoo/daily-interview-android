package kr.huijoo.dailyinterview.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import kr.huijoo.dailyinterview.R;
import kr.huijoo.dailyinterview.activity.QuestionActivity;
import kr.huijoo.dailyinterview.activity.SignInActivity;
import kr.huijoo.dailyinterview.model.Monthly;
import kr.huijoo.dailyinterview.model.QList;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    ArrayList<Monthly> monthlyArrayList;
    ArrayList<QList> todayArrayList;
    ValueEventListener listener;

    public HomeFragment() {}

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences pref = getActivity().getSharedPreferences("kr.huijoo.dailyinterview", MODE_PRIVATE);
        if (pref.getString("userName", "").equals("")) {
            startActivity(new Intent(getActivity(), SignInActivity.class));
        }
        listener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                monthlyArrayList = new ArrayList<>();
                for (DataSnapshot fileSnapshot : dataSnapshot.child("Monthly").getChildren()) {
                    Monthly temp = new Monthly();
                    temp.setQuestion(fileSnapshot.child("question").getValue(String.class));
                    temp.setImg(fileSnapshot.child("img").getValue(String.class));
                    temp.setAnswer(fileSnapshot.child("answer").getValue(String.class));
                    monthlyArrayList.add(temp);
                }
                if (!monthlyArrayList.isEmpty()) {
                    TextView q1text1 = getView().findViewById(R.id.q1text1);
                    q1text1.setText("Q: " + monthlyArrayList.get(0).getQuestion());
                    TextView q1text2 = getView().findViewById(R.id.q1text2);
                    q1text2.setText("A: " + monthlyArrayList.get(0).getAnswer());
                    ImageView img1 = getView().findViewById(R.id.img1);
                    Glide.with(img1.getContext())
                            .load(monthlyArrayList.get(0).getImg())
                            .optionalCenterCrop()
                            .into(img1);
                    CardView today1 = getView().findViewById(R.id.today1);
                    today1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog.Builder(requireContext())
                                    .setMessage(monthlyArrayList.get(0).getAnswer())
                                    .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    });

                    TextView q2text1 = getView().findViewById(R.id.q2text1);
                    q2text1.setText("Q: " + monthlyArrayList.get(1).getQuestion());
                    TextView q2text2 = getView().findViewById(R.id.q2text2);
                    q2text2.setText("A: " + monthlyArrayList.get(1).getAnswer());
                    ImageView img2 = getView().findViewById(R.id.img2);
                    Glide.with(img2.getContext())
                            .load(monthlyArrayList.get(1).getImg())
                            .optionalCenterCrop()
                            .into(img2);
                    CardView today2 = getView().findViewById(R.id.today2);
                    today2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog.Builder(requireContext())
                                    .setMessage(monthlyArrayList.get(1).getAnswer())
                                    .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    });
                }

                todayArrayList = new ArrayList<>();
                for (DataSnapshot fileSnapshot : dataSnapshot.child("List").getChildren()) {
                    QList temp = new QList();
                    temp.setListdate(fileSnapshot.child("ListDate").getValue(String.class));
                    temp.setListimg(fileSnapshot.child("ListImg").getValue(String.class));
                    temp.setListtitle(fileSnapshot.child("ListTitle").getValue(String.class));
                    todayArrayList.add(0, temp);
                }
                TextView todayQuestion = getView().findViewById(R.id.today_question);
                todayQuestion.setText(todayArrayList.get(0).getListtitle());
                ImageView todayImg = getView().findViewById(R.id.today_img);
                Glide.with(todayImg.getContext())
                        .load(todayArrayList.get(0).getListimg())
                        .optionalCenterCrop()
                        .into(todayImg);
                LinearLayout todayCard = getView().findViewById(R.id.today_card);
                todayCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(
                                new Intent(getActivity(), QuestionActivity.class)
                                        .putExtra("title", todayArrayList.get(0).getListtitle())
                        );
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mDatabase.removeEventListener(listener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}