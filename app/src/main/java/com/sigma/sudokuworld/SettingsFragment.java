package com.sigma.sudokuworld;
import com.sigma.sudokuworld.persistence.sharedpreferences.PersistenceService;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

public class SettingsFragment extends Fragment {
    private Switch mAudioModeSwitch;
    private Switch mSoundSwitch;
    private Switch mHintsSwitch;
    private Switch mRectangleSwitch;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStace) {
        mView = inflater.inflate(R.layout.fragment_settings, container, false);
        loadSettings();
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveSettings();
    }

    private void loadSettings(){
        mAudioModeSwitch = mView.findViewById(R.id.audioModeSwitch);
        mSoundSwitch = mView.findViewById(R.id.soundSwitch);
        mHintsSwitch = mView.findViewById(R.id.hintsSwitch);
        mRectangleSwitch = mView.findViewById(R.id.rectangleMode);

        Context context = getActivity();
        mAudioModeSwitch.setChecked(PersistenceService.loadAudioModeSetting(context));
        mSoundSwitch.setChecked(PersistenceService.loadSoundEnabledSetting(context));
        mHintsSwitch.setChecked(PersistenceService.loadHintsEnabledSetting(context));
        mRectangleSwitch.setChecked(PersistenceService.loadRectangleModeEnabledSetting(context));
    }

    private void saveSettings() {
        //Saving Data
        Context context = getActivity();
        PersistenceService.saveAudioModeEnableSetting(context, mAudioModeSwitch.isChecked());
        PersistenceService.saveSoundEnabledSetting(context, mSoundSwitch.isChecked());
        PersistenceService.saveHintsEnabledSetting(context, mHintsSwitch.isChecked());
        PersistenceService.saveRectangleModeEnabledSetting(context, mRectangleSwitch.isChecked());
    }
}
