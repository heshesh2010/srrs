package com.heshamapps.srrs.student;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.heshamapps.srrs.R;
import com.heshamapps.srrs.adapter.pastCoursesAdapter;
import com.heshamapps.srrs.models.Courses;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;


public class pastCoursesFragment extends Fragment {

    FirebaseFirestore firestore;
    int totalInProgress =0 ;
    int totalNotTaken=0;
    int totalPassed=0;
    ArrayList<Courses> list = new ArrayList<>();
    ArrayList<Courses> fullCoursesList = new ArrayList<>();



    @BindView(R.id.list)
    RecyclerView recyclerView ;

    @BindView(R.id.notCompletedTotalVal)
    TextView notCompletedTotalVal;

    @BindView(R.id.inProgressHoursTotalVal)
    TextView inProgressHoursTotalVal;

    @BindView(R.id.takkenHoursTotalVal)
    TextView takkenHoursTotalVal;

    public pastCoursesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        readData(new MyCallback3() {
            @Override
            public void onCallback(ArrayList<Courses> sourceCoursesList, int sourceTotalInProgress, int sourceTotalNotTaken, int sourceTotalPassed) {
                totalInProgress=sourceTotalInProgress;
                totalNotTaken=sourceTotalNotTaken;
                totalPassed=sourceTotalPassed;

                fullCoursesList.addAll(sourceCoursesList);

                notCompletedTotalVal.setText(String.valueOf(totalNotTaken));
                inProgressHoursTotalVal.setText(String.valueOf(totalInProgress));
                        takkenHoursTotalVal.setText(String.valueOf(totalPassed));

                // 2. set layoutManger
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, true)
                );

                // 3. create an adapter
                pastCoursesAdapter mAdapter = new pastCoursesAdapter(fullCoursesList,getActivity());
                // 4. set adapter
                recyclerView.setAdapter(mAdapter);
                // 5. set item animator to DefaultAnimator
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });





    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_past_courses, container, false);
            ButterKnife.bind(this,rootView);





        return rootView;
    }


    private void readData(MyCallback3 myCallback) {

        firestore.collection("courses").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Courses myObject = document.toObject(Courses.class);
                    list.add(myObject);
                    if(myObject.getStatus().contains("inProgress")){
                        totalInProgress=totalInProgress+myObject.getCourseHours();
                    }
                    else if(myObject.getStatus().contains("notTaken")){
                            totalNotTaken=totalNotTaken+myObject.getCourseHours();
                    }
                    else if(myObject.getStatus().contains("passed")){
                        totalPassed=totalPassed+myObject.getCourseHours();
                    }

                }
                myCallback.onCallback(list,totalInProgress,totalNotTaken,totalPassed);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private interface MyCallback3 {
          void onCallback(ArrayList<Courses> settings,  int sourceTotalInProgress, int sourceTotalNotTaken, int sourceTotalPassed);
    }
}
