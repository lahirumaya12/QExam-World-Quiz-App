package com.example.qexamworld;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.example.qexamworld.Models.CategoryModel;
import com.example.qexamworld.Models.ProfileModel;
import com.example.qexamworld.Models.QuestionModel;
import com.example.qexamworld.Models.RankModel;
import com.example.qexamworld.Models.TestModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbQuery {

    public static FirebaseFirestore g_firestore;
    public static List<CategoryModel> catelist = new ArrayList<>() ;

    public static int g_selected = 0;
    public static List<TestModel> testList = new ArrayList<>();

    public static int g_selected_test_index = 0;

    public static  List<QuestionModel> g_quesList = new ArrayList<>();

    public static  List<RankModel> g_usersList = new ArrayList<>();

    public static  int g_usersCount = 0;

    public static boolean isMeonTopList = false;

     public static ProfileModel myprofile = new ProfileModel("NA", null);

     public  static RankModel myPerformance = new RankModel("NULL",0, -1);

     public static final int NOT_VISITED = 0;
     public static final int UNANSWERED = 1;
     public static final int ANSWERED = 2;
     public static final int REVIEW = 3;

    public static void  createUserData(String email, String name, MyCompleteListener  completeListener){

        Map<String, Object> userData = new ArrayMap<>();
        userData.put("EMAIL_ID", email);
        userData.put("NAME", name);
        userData.put("TOTAL_SCORE", 0);

        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = g_firestore.batch();

        batch.set(userDoc, userData);

        DocumentReference countDoc = g_firestore.collection("USERS").document("TOTAL_USERS");
        batch.update(countDoc, "COUNT", FieldValue.increment(1));

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();

                    }
                });
    }

    public static void getTopUsers(MyCompleteListener completeListener){

        g_usersList.clear();
        String myUID = FirebaseAuth.getInstance().getUid();

        g_firestore.collection("USERS")
                .whereGreaterThan("TOTAL_SCORE", 0)
                .orderBy("TOTAL_SCORE", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        int rank = 1;

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            g_usersList.add(new RankModel(
                                doc.getString("NAME"),
                                    doc.getLong("TOTAL_SCORE").intValue(),
                                    rank

                            ));

                            if (myUID.compareTo(doc.getId()) == 0){
                                isMeonTopList = true;
                                myPerformance.setRank(rank);
                            }

                            rank++;

                        }

                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();
                    }
                });

    }

    public static void getUsersCount(MyCompleteListener completeListener){
        g_firestore.collection("USERS").document("TOTAL_USERS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        g_usersCount = documentSnapshot.getLong("COUNT").intValue();

                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();
                    }
                });
    }

    public static void getUserData(MyCompleteListener completeListener){
        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        myprofile.setName(documentSnapshot.getString("NAME"));
                        myprofile.setEmail(documentSnapshot.getString("EMAIL_ID"));

                        myPerformance.setScore(documentSnapshot.getLong("TOTAL_SCORE").intValue());

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();

                    }
                });
    }
    public static void  loadMyScore(MyCompleteListener completeListener){

        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_SCORES")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for (int i=0; i < testList.size(); i++){
                            int top  = 0;
                            if (documentSnapshot.get(testList.get(i).getTestID()) != null){
                                top = documentSnapshot.getLong(testList.get(i).getTestID()).intValue();
                            }

                            testList.get(i).setTopScore(top);
                        }
                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();

                    }
                });

    }


    public static void saveResult(int score, MyCompleteListener completeListener){

        WriteBatch batch = g_firestore.batch();

        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid());

        batch.update(userDoc, "TOTAL_SCORE", score);

        if (score > testList.get(g_selected_test_index).getTopScore()){

            DocumentReference scoreDoc = userDoc.collection("USER_DATA").document("MY_SCORES");

            Map<String, Object> testData = new ArrayMap<>();
            testData.put(testList.get(g_selected_test_index).getTestID(),score);

            batch.set(scoreDoc, testData, SetOptions.merge());

        }

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (score > testList.get(g_selected_test_index).getTopScore()){
                            testList.get(g_selected_test_index).setTopScore(score);

                            myPerformance.setScore(score);

                            completeListener.onSuccess();

                        }
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();

                    }
                });

    }

    public static void  load(final MyCompleteListener completeListener){

        catelist.clear();

        g_firestore.collection("QUIZ").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            docList.put(doc.getId(), doc);

                        }

                        QueryDocumentSnapshot catListDoc = docList.get("Categories");

                        long caTCount = catListDoc.getLong("COUNT");

                        for (int i=1; i <= caTCount; i++){
                            String catID = catListDoc.getString("CAT" + String.valueOf(i) + "_ID");

                            QueryDocumentSnapshot catDoc = docList.get(catID);

                            int noOfTest = catDoc.getLong("NO_OF_TESTS").intValue();
                            String catName = catDoc.getString("NAME");
                            catelist.add(new CategoryModel(catID, catName, noOfTest));
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });


    }

    public static void loadquestions(MyCompleteListener completeListener){


        g_quesList.clear();
        g_firestore.collection("Questions")
        .whereEqualTo("CATEGORY", catelist.get(g_selected).getId())
        .whereEqualTo("TEST", testList.get(g_selected_test_index).getTestID())
        .get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot doc : queryDocumentSnapshots){
                    g_quesList.add(new QuestionModel(
                           doc.getString("QUESTION"),
                            doc.getString("A"),
                            doc.getString("B"),
                            doc.getString("C"),
                            doc.getString("D"),
                            doc.getLong("ANSWER").intValue(),
                            -1,
                            NOT_VISITED

                    ));
                }

                completeListener.onSuccess();

            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                completeListener.onFailure();
            }
        });

    }

    public static void loadTestData(MyCompleteListener completeListener){
        testList.clear();

        g_firestore.collection("QUIZ").document(catelist.get(g_selected).getId())
                .collection("TEST_LIST").document("TEST_INFO")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        int noOFTest = catelist.get(g_selected).getNoOfTests();

                        for(int i=1; i <= noOFTest; i++){
                            testList.add(new TestModel(
                                    documentSnapshot.getString("TEST" + String.valueOf(i) + "_ID"),
                                    0,
                                    documentSnapshot.getLong("TEST" + String.valueOf(i) + "_TIME").intValue()
                            ));
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();
                    }
                });
    }

    public static void loadData(MyCompleteListener completeListener){
        load(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                getUserData(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        getUsersCount(completeListener);
                    }

                    @Override
                    public void onFailure() {

                        completeListener.onFailure();
                    }
                });
            }

            @Override
            public void onFailure() {
                completeListener.onFailure();

            }
        });

    }

}
