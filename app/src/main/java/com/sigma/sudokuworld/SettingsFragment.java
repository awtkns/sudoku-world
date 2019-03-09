package com.sigma.sudokuworld;
import com.sigma.sudokuworld.persistence.sharedpreferences.PersistenceService;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

public class SettingsFragment extends Fragment {
    private Switch mAudioModeSwitch;
    private Switch mSoundSwitch;
    private Switch mHintsSwitch;
    private Switch mRectangleSwitch;

    private Button mCancelButton;
    private Button mSaveButton;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStace) {
        mView = inflater.inflate(R.layout.fragment_settings, container, false);
        loadSettings();

        mCancelButton = mView.findViewById(R.id.settingsCancelButton);
        mSaveButton = mView.findViewById(R.id.settingsSaveButton);

        mCancelButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MenuActivity) getActivity()).closeFragment();
                }
        });

        mSaveButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                ((MenuActivity) getActivity()).closeFragment();
            }
        });


        return mView;
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
        mRectangleSwitch.setChecked(PersistenceService.loadRectagleModeEnabledSetting(context));
    }

    private void saveSettings() {
        //Saving Data
        Context context = getActivity();
        PersistenceService.saveAudioModeEnableSetting(context, mAudioModeSwitch.isChecked());
        PersistenceService.saveSoundEnabledSetting(context, mSoundSwitch.isChecked());
        PersistenceService.saveHintsEnabledSetting(context, mHintsSwitch.isChecked());
        PersistenceService.saveRectagleModeEnabledSetting(context, mRectangleSwitch.isChecked());
    }
}
