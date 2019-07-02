package pl.wat.nutpromobile.activity.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import pl.wat.nutpromobile.R;

class Permission {
    Permission(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private MainActivity mainActivity;

    //https://stackoverflow.com/questions/34040355/how-to-check-the-multiple-permission-at-single-request-in-android-m#
    //w przypadku implementacji więcej dangerous requestów


    void startPermissionRequest() {
        if (Build.VERSION.SDK_INT >= 23) {
            checkAllPermissions();
        } else {

        }
    }

    private void checkAllPermissions() {
        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        checkPermission(Manifest.permission.READ_CONTACTS);
    }

    private void checkPermission(String permission) {
        if (ContextCompat.checkSelfPermission(mainActivity, permission)
                != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(mainActivity, permission)) {
                createSecondPermissionExplanationDialog(permission);
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                //J.S Information launched and permission request started after dialog dismiss
                createPermissionExplanationDialog(permission);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }


    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == getPermissionRequestCode(permissions[0])) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(mainActivity, permissions[0])) {

                } else {
                    createPermissionExplanationDialogAfterNeverAskAgain();
                }
            }
        }
    }

    int getPermissionRequestCode(String permission) {
        if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION))
            return mainActivity.getResources().getInteger(R.integer.request_fine_location_permissions);
        else if (permission.equals(Manifest.permission.READ_CONTACTS)) {
            return mainActivity.getResources().getInteger(R.integer.request_read_contacts_permisson);
        } else {
            return 0;
        }
    }


    private void createPermissionExplanationDialog(String permission) {
        createPermissionDialog("Permission: " + permission + " are necessary for the proper functioning of the application.\n " +
                        "If you don't grant it, you won't be able to read sensors data etc."
                , () -> requestPermissions(permission));
    }

    private void createSecondPermissionExplanationDialog(String permission) {
        createPermissionDialog("You've previously canceled " + permission + " grant. Please do it this time"
                , () -> requestPermissions(permission));
    }

    private void createPermissionExplanationDialogAfterNeverAskAgain() {
        final int settingsPermissionCode = mainActivity.getResources().getInteger(R.integer.request_settings_permissions);
        createPermissionDialog("You've previously check never ask again button for some permission, you must change app preferences in settings"
                , () -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", mainActivity.getPackageName(), null);
                    intent.setData(uri);
                    mainActivity.startActivityForResult(intent, settingsPermissionCode);
                });
    }

    private void createPermissionDialog(String message, PermissionBehaviour permissionBehaviour) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setMessage(message);
        builder.setNeutralButton("OK", (dialog, which) -> {
            dialog.cancel();
            permissionBehaviour.work();
        });
        builder.create();
        builder.show();
    }


    interface PermissionBehaviour {
        void work();
    }

    private void requestPermissions(String permission) {
        int requestCode = getPermissionRequestCode(permission);


        ActivityCompat.requestPermissions(mainActivity,
                new String[]{permission},
                requestCode
        );
    }
}
