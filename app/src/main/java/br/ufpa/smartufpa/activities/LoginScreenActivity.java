package br.ufpa.smartufpa.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufpa.smartufpa.R;


/**
 * First activity on app and ask for permissions
 * (login interface to be implemented);
 * Permission code:
 *      Reference - https://github.com/osmdroid/osmdroid/blob/8f0f440d4310688760e53c6b73857df8de0c2e54/OpenStreetMapViewer/src/main/java/org/osmdroid/intro/PermissionsFragment.java
 * @author kaeuchoa
 *
 */
public class LoginScreenActivity extends AppCompatActivity {
    public static final String CATEGORY_LOGIN = "smartufpa.CATEGORY_LOGIN";
    public static final String ACTION_LOGIN = "smartufpa.ACTION_LOGIN";
    public static final String TAG = LoginScreenActivity.class.getSimpleName();

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    // Views
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_activity);

        btnLogin = findViewById(R.id.btn_entrar);

        final View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == btnLogin.getId()) {
                    Intent intent = new Intent(MenuActivity.ACTION_APP_MENU);
                    intent.addCategory(MenuActivity.CATEGORY_APP_MENU);
                    startActivity(intent);
                }
            }
        };
        btnLogin.setOnClickListener(clickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Requests permissions on versions from Marshmallow and over
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions();
        }
    }

    // Ask for permission to use file storage for cache and location features

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        List<String> permissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            String[] params = permissions.toArray(new String[permissions.size()]);
            requestPermissions(params, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } // else: We already have permissions, so handle as normal
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION and WRITE_EXTERNAL_STORAGE
                Boolean location = perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                Boolean storage = perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (location && storage) {
                    // All Permissions Granted
                    Toast.makeText(this, R.string.msg_all_permissions_granted,
                            Toast.LENGTH_SHORT).show();
                } else if (location) {
                    Toast.makeText(this, R.string.msg_request_storage_permission,
                            Toast.LENGTH_LONG).show();
                } else if (storage) {
                    Toast.makeText(this, R.string.msg_request_location_permission, Toast.LENGTH_LONG).show();
                } else { // !location && !storage case
                    // Permission Denied
                    Toast.makeText(this, R.string.msg_request_both_permissions, Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}