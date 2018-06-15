package photography.social.com.scenicsydney.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import photography.social.com.scenicsydney.data.ScenicSydneyRepository;
import photography.social.com.scenicsydney.data.database.LocationEntry;

/**
 * ViewModel for {@link MainActivity}
 */
public class MainActivityViewModel extends ViewModel {
    private final LiveData<List<LocationEntry>> mData;

    public MainActivityViewModel(ScenicSydneyRepository scenicSydneyRepository) {
        mData = scenicSydneyRepository.getLocations();
    }

    public LiveData<List<LocationEntry>> getLocations() {
        return mData;
    }
}
