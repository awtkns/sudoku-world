package com.sigma.sudokuworld.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import com.sigma.sudokuworld.persistence.sharedpreferences.PersistenceService;

public abstract class BaseSettingsViewModel extends AndroidViewModel {
    Application mApplication;
    public boolean isAudioModeEnabled;
    public boolean isHintsEnabled;
    public boolean isSoundEnabled;
    public boolean isRectangleModeEnabled;

    public BaseSettingsViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;

        isAudioModeEnabled = PersistenceService.loadAudioModeSetting(mApplication);
        isSoundEnabled = PersistenceService.loadSoundEnabledSetting(mApplication);
        isHintsEnabled = PersistenceService.loadHintsEnabledSetting(mApplication);
        isRectangleModeEnabled = PersistenceService.loadRectangleModeEnabledSetting(mApplication);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        PersistenceService.saveAudioModeEnableSetting(mApplication, isAudioModeEnabled);
        PersistenceService.saveSoundEnabledSetting(mApplication, isSoundEnabled);
        PersistenceService.saveHintsEnabledSetting(mApplication, isHintsEnabled);
        PersistenceService.saveRectangleModeEnabledSetting(mApplication, isRectangleModeEnabled);
    }
}
