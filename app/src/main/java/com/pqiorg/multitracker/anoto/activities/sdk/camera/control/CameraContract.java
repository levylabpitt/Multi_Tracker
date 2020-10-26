package com.pqiorg.multitracker.anoto.activities.sdk.camera.control;

public class CameraContract {

    public interface Presenter {
        void focusCamera();

        void showCoordinates(String str);

        void showSettings();

        void startCamera();

        void toggleCameraFlashTorch();
    }
}
