package superbiayang.imageprocessing;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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

import java.io.FileNotFoundException;

import processor.Grayscale;
import processor.OpenCV.Filter;
import processor.OpenCV.Morphology;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FilterFragment.OnFragmentInteractionListener,
        MorphologyFragment.OnFragmentInteractionListener,
        GrayscaleFragment.OnFragmentInteractionListener {
    Bitmap bit1;
    private CurBitmapType curBitMapType = CurBitmapType.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
                if (iv != null) {
                    int[] pixels = new int[bit1.getHeight() * bit1.getWidth()];
                    bit1.getPixels(pixels, 0, bit1.getWidth(), 0, 0, bit1.getWidth(), bit1.getHeight());
                    iv.setImageBitmap(bit1);
                }
            }

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.menu_op_filter) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.op_layout, new FilterFragment(), "fragment_filter");
            fragmentTransaction.commit();
        } else if (id == R.id.menu_op_morphology) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.op_layout, new MorphologyFragment(), "fragment_filter");
            fragmentTransaction.commit();
        } else if (id == R.id.menu_op_grayscale) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.op_layout, new GrayscaleFragment(), "fragment_filter");
            fragmentTransaction.commit();
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
                    updateCurBitmap(BitmapFactory.decodeStream(cr.openInputStream(uri)), CurBitmapType.COLOR);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }

    }

    private Bitmap getCurBitmap() {
        return bit1;
    }

    private void updateCurBitmap(Bitmap newBitmap) {
        bit1 = newBitmap;
        ImageView iv = (ImageView) findViewById(R.id.imageView);
        if (iv != null) {
            iv.setImageBitmap(bit1);
        }
    }

    private void updateCurBitmap(Bitmap newBitmap, CurBitmapType type) {
        updateCurBitmap(newBitmap);
        updateCurBitmapType(type);
    }

    private void updateCurBitmapType(CurBitmapType type) {
        curBitMapType = type;
        updateMenu();
    }

    @Override
    public void onFilterButtonPressed(View view) {
        Bitmap bitmap = getCurBitmap();
        if (bitmap == null) {
            return;
        }
        int id = view.getId();
        if (id == R.id.mean_blur_button) {
            EditText editText = (EditText) findViewById(R.id.mean_blur_editText);
            int size = Integer.parseInt(editText.getText().toString());
            updateCurBitmap(Filter.meanBlur(bitmap, size));
        } else if (id == R.id.median_blur_button) {
            EditText editText = (EditText) findViewById(R.id.median_blur_editText);
            int size = Integer.parseInt(editText.getText().toString());
            if (size % 2 == 1) {
                updateCurBitmap(Filter.medianBlur(bitmap, size));
            } else {
                //TODO: show error
            }
        } else if (id == R.id.gaussian_blur_button) {
            EditText editText = (EditText) findViewById(R.id.gaussian_blur_size_editText);
            int size = Integer.parseInt(editText.getText().toString());
            editText = (EditText) findViewById(R.id.gaussian_blur_sigma_editText);
            double sigma = Double.parseDouble(editText.getText().toString());
            if (size % 2 == 1) {
                updateCurBitmap(Filter.gaussianBlur(bitmap, size, sigma));
            } else {
                //TODO: show error
            }
        } else if (id == R.id.sobel_button) {
            updateCurBitmap(Filter.sobel(bitmap));
        } else if (id == R.id.prewitt_button) {
            updateCurBitmap(Filter.prewitt(bitmap));
        } else if (id == R.id.roberts_button) {
            updateCurBitmap(Filter.roberts(bitmap));
        }
    }

    @Override
    public void onMorphologyButtonPressed(View view) {
        Bitmap bitmap = getCurBitmap();
        if (bitmap == null) {
            return;
        }
        int id = view.getId();
        if (id == R.id.morphology_erode_button) {
            updateCurBitmap(Morphology.erode(bitmap));
        } else if (id == R.id.morphology_dilate_button) {
            updateCurBitmap(Morphology.dilate(bitmap));
        } else if (id == R.id.morphology_open_button) {
            updateCurBitmap(Morphology.open(bitmap));
        } else if (id == R.id.morphology_close_button) {
            updateCurBitmap(Morphology.close(bitmap));
        }
    }

    @Override
    public void onGrayscaleButtonPressed(View view) {
        Bitmap bitmap = getCurBitmap();
        int id = view.getId();
        Grayscale.Return ret = null;
        if (id == R.id.grayscale_red_button) {
            ret = Grayscale.generate(bitmap, Grayscale.GrayType.RED);
        } else if (id == R.id.grayscale_green_button) {
            ret = Grayscale.generate(bitmap, Grayscale.GrayType.GREEN);
        } else if (id == R.id.grayscale_blue_button) {
            ret = Grayscale.generate(bitmap, Grayscale.GrayType.BLUE);
        } else if (id == R.id.grayscale_avg_button) {
            ret = Grayscale.generate(bitmap, Grayscale.GrayType.AVG);
        } else if (id == R.id.grayscale_opencv_button) {
            ret = Grayscale.generate(bitmap, Grayscale.GrayType.OPENCV);
        } else if (id == R.id.grayscale_bio_button) {
            ret = Grayscale.generate(bitmap, Grayscale.GrayType.BIO);
        }
        updateCurBitmap(ret.bitmap, CurBitmapType.GRAY);
        ImageView histogram = (ImageView) findViewById(R.id.grayscale_histogram_imageView);
        histogram.setImageBitmap(Grayscale.Histogram(ret.histogram));
    }

    public void updateMenu() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        if (curBitMapType == CurBitmapType.COLOR) {
            menu.findItem(R.id.menu_op_grayscale).setEnabled(true);
            menu.findItem(R.id.menu_op_filter).setEnabled(true);
            menu.findItem(R.id.menu_op_morphology).setEnabled(true);
        } else if (curBitMapType == CurBitmapType.GRAY) {
            menu.findItem(R.id.menu_op_grayscale).setEnabled(false);
            menu.findItem(R.id.menu_op_filter).setEnabled(true);
            menu.findItem(R.id.menu_op_morphology).setEnabled(true);
        } else if (curBitMapType == CurBitmapType.BINARY) {
            menu.findItem(R.id.menu_op_grayscale).setEnabled(false);
            menu.findItem(R.id.menu_op_filter).setEnabled(true);
            menu.findItem(R.id.menu_op_morphology).setEnabled(true);
        }
    }

    private enum CurBitmapType {
        NONE,
        COLOR,
        GRAY,
        BINARY
    }

}
