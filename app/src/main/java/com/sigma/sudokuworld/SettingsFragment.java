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

        Context context = getActivity();
        boolean isAudioMode = PersistenceService.loadAudioModeSetting(context);
        boolean isSoundMode = PersistenceService.loadSoundEnabledSetting(context);
        boolean isHintsMode = PersistenceService.loadHintsEnabledSetting(context);

        if (isAudioMode) {
            mAudioModeSwitch.setChecked(true);
        } else {
            mAudioModeSwitch.setChecked(false);
        }

        if (isSoundMode) {
            mSoundSwitch.setChecked(true);
        } else {
            mSoundSwitch.setChecked(false);
        }

        if (isHintsMode) {
            mHintsSwitch.setChecked(true);
        } else {
            mHintsSwitch.setChecked(false);
        }
    }

    private void saveSettings() {
        //Saving Data
        Context context = getActivity();
        PersistenceService.saveAudioModeEnableSetting(context, mAudioModeSwitch.isChecked());
        PersistenceService.saveSoundEnabledSetting(context, mSoundSwitch.isChecked());
        PersistenceService.saveHintsEnabledSetting(context, mHintsSwitch.isChecked());
    }
}
