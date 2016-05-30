package superbiayang.imageprocessing;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FilterFragment.OnFragmentInteractionListener,
        MorphologyFragment.OnFragmentInteractionListener {

    Bitmap bit1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.add(R.id.op_layout, new FilterFragment(), "ONE");
        tx.commit();
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
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openAlbumIntent.setType("image/*");
            startActivityForResult(openAlbumIntent, 0);
        } else if (id == R.id.nav_gallery) {
            if (bit1 != null) {
                ImageView iv = (ImageView) findViewById(R.id.imageView);
                ImageView iv1 = (ImageView) findViewById(R.id.imageView1);
                if (iv != null && iv1 != null) {
                    int[] pixels = new int[bit1.getHeight() * bit1.getWidth()];
                    bit1.getPixels(pixels, 0, bit1.getWidth(), 0, 0, bit1.getWidth(), bit1.getHeight());
                    int[] dst = new int[bit1.getHeight() * bit1.getWidth()];
                    bit1.setPixels(dst, 0, bit1.getWidth(), 0, 0, bit1.getWidth(), bit1.getHeight());
                    iv.setImageBitmap(bit1);
                }
            }


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case 0:
                Uri uri = data.getData();
                try {
                    ContentResolver cr = this.getContentResolver();
                    if (bit1 != null) {
                        bit1.recycle();
                    }
                    bit1 = BitmapFactory.decodeStream(cr.openInputStream(uri));

                    ImageView iv = (ImageView) findViewById(R.id.imageView);
                    if (iv != null) {
                        iv.setImageBitmap(bit1);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFilterButtonPressed(View view) {
        TextView tv = (TextView) findViewById(R.id.test_textView);
        int id = view.getId();
        switch (id) {
            case R.id.mean_blur_button:
                tv.setText("mean");
                break;
            case R.id.median_blur_button:
                EditText editText = (EditText) findViewById(R.id.median_blur_editText);
                int size = Integer.parseInt(editText.getText().toString());
                break;
            case R.id.gaussian_blur_button:
                tv.setText("gaussian");
                break;
        }
    }
}