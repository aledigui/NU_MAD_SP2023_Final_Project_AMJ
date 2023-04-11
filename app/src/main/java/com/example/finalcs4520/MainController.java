package com.example.finalcs4520;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainController extends AppCompatActivity implements RegisterLogInFragment.IRegister, cameraPreviewFragment.IPreviewImg, CameraFragment.ICameraPicture, ProfileFragment.IProfileTrip{

    private int screenCamera;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.MainActivityContainer, new RegisterLogInFragment(), "registerFragment")
                .addToBackStack(null)
                .commit();
    }

    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK){
                        Intent data = result.getData();
                        Uri newUri = data.getData();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.MainActivityContainer,
                                        cameraPreviewFragment.newInstance(screenCamera, newUri),
                                        "cameraPreviewFragment")
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
    );


    @Override
    public void onGalleryPressed(int screen) {
        screenCamera = screen;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        galleryLauncher.launch(intent);

    }

    @Override
    public void onCapturePressed(int i, Uri imgUri) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainActivityContainer, cameraPreviewFragment.newInstance(i, imgUri),
                        "cameraPreviewFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLoginPressed() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainActivityContainer, new ProfileFragment(), "profileFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSignUpImagePressed() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainActivityContainer, CameraFragment.newInstance(0), "cameraFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onRetakePressed(int screen) {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onUploadSignUp(Uri imgUri) {
        RegisterLogInFragment registerFragment = (RegisterLogInFragment) getSupportFragmentManager()
                .findFragmentByTag("registerFragment");
        registerFragment.setSignUpImage(imgUri);
        // popping two times to get the fragment without creating a new one
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onUploadTripPicture(Uri imgUri) {

        // TODO: upload picture of the trip

    }

    @Override
    public void onUploadProfilePicture(Uri imgUri) {
        ProfileFragment profileFragment = (ProfileFragment) getSupportFragmentManager()
                .findFragmentByTag("profileFragment");
        profileFragment.setProfilePic(imgUri);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onLogOutPressed() {
        mAuth.signOut();
        mUser = null;
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onSearchPressed() {
        // TODO: change this to search travel
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainActivityContainer, new SearchProfileFragment(), "searchFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onImgPressed() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainActivityContainer, CameraFragment.newInstance(2), "cameraFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDeletePressed(TripProfile deleteTrip) {
        ProfileFragment profileFragment = (ProfileFragment) getSupportFragmentManager()
                .findFragmentByTag("profileFragment");
        profileFragment.deleteTrip(deleteTrip);

    }

    @Override
    public void onCompleteTripPressed(TripProfile compTrip) {
        ProfileFragment profileFragment = (ProfileFragment) getSupportFragmentManager()
                .findFragmentByTag("profileFragment");
        profileFragment.completeTrip(compTrip);

    }

    @Override
    public void onAddFriendsPressed() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainActivityContainer, new SearchProfileFragment(), "searchFragment")
                .addToBackStack(null)
                .commit();
    }
}