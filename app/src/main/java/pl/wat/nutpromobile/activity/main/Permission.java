package pl.wat.nutpromobile.activity.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import pl.wat.nutpromobile.R;

public class Permission {
    private MainActivity mainActivity;

    public Permission(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    public void startPermissionRequest(){
        if (ContextCompat.checkSelfPermission(
                mainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
        )
                != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    mainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
            )
            ) {
                createSecondPermissionExplanationDialog();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                //J.S Information launched and permission request started after dialog dismiss
                createPermissionExplanationDialog();

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == mainActivity.getResources().getInteger(R.integer.request_localization_permissions)) {
            // If request is cancelled, the result arrays are empty.

            if ((grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        mainActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                ) {

                } else {
                    createPermissionExplanationDialogAfterNeverAskAgain();
                }
            }

        }else if(requestCode == 666666){
            // Add other 'if' lines to check for other
            // permissions this app might request.
        }else{
            //Ignore all other requests
        }

    }

    private void createPermissionExplanationDialog(){
        createPermissionDialog("All permits are necessary for the proper functioning of the application.\n " +
                        "If you don't grant them, you won't be able to read sensors data etc."
                , this::requestPermissions);
    }

    private void createSecondPermissionExplanationDialog(){
        createPermissionDialog("You've previously canceled permission granting. Please grant all permissins, this time"
                , this::requestPermissions);
    }

    private void createPermissionExplanationDialogAfterNeverAskAgain(){
        final int settingsPermissionCode = mainActivity.getResources().getInteger(R.integer.request_settings_permissions);
        createPermissionDialog("You've previously check never ask again button, you must change app preferences in settings"
                ,() ->{
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", mainActivity.getPackageName(), null);
                    intent.setData(uri);
                    mainActivity.startActivityForResult(intent, settingsPermissionCode);
                });
    }

    private void createPermissionDialog(String message, PermissionBehaviour permissionBehaviour){
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setMessage(message);
        builder.setNeutralButton("OK", (dialog, which) -> {
            dialog.cancel();
            permissionBehaviour.work();
        });
        builder.create();
        builder.show();
    }



    interface PermissionBehaviour{
        void work();
    }

    private void requestPermissions(){
        final int requestCode = mainActivity.getResources().getInteger(R.integer.request_localization_permissions); //TODO przenieść
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};

        ActivityCompat.requestPermissions(mainActivity,
                permission,
                requestCode
        );
    }
}
