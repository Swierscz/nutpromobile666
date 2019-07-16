package pl.wat.nutpromobile.fragments.training;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TrainingFragmentViewModel extends ViewModel {

    TrainingFragmentRepository trainingFragmentRepository = new TrainingFragmentRepository();
    MutableLiveData<String> timer;
    MutableLiveData<String> getTimer(){
        if(timer == null) timer = new MutableLiveData<>();
        return timer;
    }


}
