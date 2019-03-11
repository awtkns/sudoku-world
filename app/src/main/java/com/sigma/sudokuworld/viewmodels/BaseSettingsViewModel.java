package com.sigma.sudokuworld.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import com.sigma.sudokuworld.persistence.sharedpreferences.PersistenceService;

public abstract class BaseSettingsViewModel extends AndroidViewModel {
    Application mApplication;

    BaseSettingsViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
    }

    public void setAudioModeEnabled(boolean isEnabled) {
        PersistenceService.saveAudioModeEnableSetting(mApplication, isEnabled);
    }

    public void setSoundEnabled(boolean isEnabled) {
        PersistenceService.saveSoundEnabledSetting(mApplication, isEnabled);
    }

    public void setHintsEnabled(boolean isEnabled) {
        PersistenceService.saveHintsEnabledSetting(mApplication, isEnabled);
    }

    public void setRectangleModeEnabled(boolean isEnabled) {
        PersistenceService.saveRectangleModeEnabledSetting(mApplication, isEnabled);
    }

    public boolean isAudioModeEnabled() {
        return PersistenceService.loadAudioModeSetting(mApplication);
    }

    public boolean isSoundEnabled() {
        return PersistenceService.loadSoundEnabledSetting(mApplication);
    }

    public boolean isHintsEnabled() {
        return PersistenceService.loadHintsEnabledSetting(mApplication);
    }

    public boolean isRectangleModeEnabled() {
        return PersistenceService.loadRectangleModeEnabledSetting(mApplication);
    }
}
