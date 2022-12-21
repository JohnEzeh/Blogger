package com.example.blogger.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogger.R;
import com.example.blogger.adapter.PostAdapter;
import com.example.blogger.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment {
    private PostAdapter postAdapter;
    private List<Post> postList;

    ProgressBar progress_circular;

private List<String> followingList;


    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_vine);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);

        progress_circular = view.findViewById(R.id.progress_circular);

        checkFollowing();

        return view;
    }/////////////////////////////////////////////////////////////////////////////////////////////////////////////EOMM

   private void checkFollowing(){
        followingList = new ArrayList<>();
         DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                 .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                 .child("following");

         reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 followingList.clear();
                 for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                     followingList.add(dataSnapshot.getKey());

                 }
                 readPost();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
   }


       private void readPost(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);

                     for (String id : followingList){//////found it
                         assert post != null;
                         if (post.getPublisher().equals(id)){
                             postList.add(post);
                         }
                     }
                    assert post != null;
                    postList.add(post);
                }
                postAdapter.notifyDataSetChanged();
                progress_circular.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
 }

}