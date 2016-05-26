package net.christopherliu.readcacoo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.christopherliu.cacooapi.DiagramsListener;
import net.christopherliu.cacooapi.GetAccountInfoTask;
import net.christopherliu.cacooapi.GetDiagramsTask;
import net.christopherliu.cacooapi.InvalidKeyException;
import net.christopherliu.cacooapi.types.AccountInfo;
import net.christopherliu.cacooapi.AccountInfoListener;
import net.christopherliu.cacooapi.types.Diagram;
import net.christopherliu.system.AsyncTaskResult;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class ReadCacooActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private DiagramAdapter diagramAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_cacoo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Action button
        // TODO use for "New Diagram" functionality
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        // Drawer toggle
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize GridView
        diagramAdapter = new DiagramAdapter(this);
        GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(diagramAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                showImage(diagramAdapter.getItem(position));
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //TODO only trigger reload of images when API key changed.
    @Override
    public void onResume() {
        super.onResume();

        Toast.makeText(ReadCacooActivity.this, "Loading data from Cacoo",
                Toast.LENGTH_SHORT).show();

        // Load initial API data
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String apiKey = sharedPref.getString("api_key_text", "");
        new GetAccountInfoTask(new AccountInfoListener() {
            @Override
            public void handle(AsyncTaskResult<AccountInfo> accountInfoResult) {
                if (accountInfoResult.getError() != null) {
                    showError(accountInfoResult.getError());
                    return;
                }

                AccountInfo accountInfo = accountInfoResult.getResult();
                TextView accountNickname = (TextView) findViewById(R.id.accountNickname);
                TextView accountStatus = (TextView) findViewById(R.id.accountStatus);
                ImageView accountIcon = (ImageView) findViewById(R.id.accountIcon);
                accountNickname.setText(accountInfo.nickname);
                accountStatus.setText(accountInfo.getFriendlyPlanText());
                accountIcon.setImageBitmap(accountInfo.accountImage);
                Log.i("INFO", String.valueOf(accountInfo));
            }
        }).execute(apiKey);

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.gridView).setVisibility(View.GONE);
        new GetDiagramsTask(new DiagramsListener() {
            @Override
            public void handle(AsyncTaskResult<Diagram[]> diagramsResult) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                if (diagramsResult.getError() != null) {
                    showError(diagramsResult.getError());
                    return;
                }

                Diagram[] diagrams = diagramsResult.getResult();
                diagramAdapter.setDiagrams(diagrams);
                findViewById(R.id.gridView).setVisibility(View.VISIBLE);
            }
        }).execute(apiKey);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.read_cacoo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            //We're already in the gallery -- this is reserved for future code.
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onPressSettings(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ReadCacoo Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://net.christopherliu.readcacoo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ReadCacoo Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://net.christopherliu.readcacoo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    // Utility functions
    private void showError(Exception error) {
        final ReadCacooActivity self = this;
        //TODO make strings resources
        //TODO this error will show doubly, because we load the AccountInfo and Diagrams separately.
        if (error instanceof InvalidKeyException) {
            Snackbar.make(findViewById(android.R.id.content), "Set the Cacoo API key in settings to load your account.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO research way to combine this with onPressSettings
                            Intent intent = new Intent(self, SettingsActivity.class);
                            startActivity(intent);
                        }
                    }).show();
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Unknown error; please try again later.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Action", null).show();
        }
    }

    /**
     * As a quick stand-in, this function shows a blown up version of the image that is clicked on.
     * We should eventually add more sophisticated functionality.
     * From: http://stackoverflow.com/a/24946375/40352
     * @param diagram
     */
    private void showImage(Diagram diagram) {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(diagram.image);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }
}
