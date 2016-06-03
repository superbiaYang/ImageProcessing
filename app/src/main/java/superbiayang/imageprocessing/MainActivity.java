package superbiayang.imageprocessing;

import android.app.Fragment;
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
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.FileNotFoundException;

import processor.Basic;
import processor.Binary;
import processor.Grayscale;
import processor.OpenCV.BinaryMorphology;
import processor.OpenCV.Filter;
import processor.OpenCV.Morphology;
import view.fragment.BasicFragment;
import view.fragment.BinaryFragment;
import view.fragment.BinaryMorphologyFragment;
import view.fragment.FilterFragment;
import view.fragment.GrayscaleFragment;
import view.fragment.MorphologyFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FilterFragment.OnFragmentInteractionListener,
        MorphologyFragment.OnFragmentInteractionListener,
        GrayscaleFragment.OnFragmentInteractionListener,
        BinaryFragment.OnFragmentInteractionListener,
        BasicFragment.OnFragmentInteractionListener,
        BinaryMorphologyFragment.OnFragmentInteractionListener {
    private static final SparseArray<String> MenuIdFragmentTag = new SparseArray<>();

    static {
        MenuIdFragmentTag.append(R.id.menu_op_basic, "BASIC_FRAGMENT");
    }

    private int[] curPic = null;
    private int curWidth = 0;
    private int curHeight = 0;
    private int[] basePic = null;
    private PicType basePicType = PicType.NONE;
    private int[] histogram = null;
    private PicType curPicType = PicType.NONE;
    private PicManager picManager = new PicManager();
    private int curFragment = -1;

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
        basePic = null;
        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openAlbumIntent.setType("image/*");
            startActivityForResult(openAlbumIntent, 0);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else {
            showProcessorFragment(id);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showProcessorFragment(int id) {
        if (id == curFragment) {
            return;
        }
        basePic = curPic;
        basePicType = curPicType;
        ImageView view = (ImageView) findViewById(R.id.base_imageView);
        if (view != null) {
            view.setImageBitmap(Bitmap.createBitmap(basePic, curWidth, curHeight, Bitmap.Config.ARGB_8888));
        }
        String tag = "fragment_" + id;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            switch (id) {
                case R.id.menu_op_basic:
                    fragment = BasicFragment.newInstance();
                    break;
                case R.id.menu_op_grayscale:
                    fragment = GrayscaleFragment.newInstance();
                    break;
                case R.id.menu_op_binary:
                    fragment = BinaryFragment.newInstance();
                    break;
                case R.id.menu_op_binary_morphology:
                    fragment = BinaryMorphologyFragment.newInstance();
                    break;
                case R.id.menu_op_filter:
                    fragment = FilterFragment.newInstance();
                    break;
                case R.id.menu_op_morphology:
                    fragment = MorphologyFragment.newInstance();
                    break;
            }
        }
        fragmentTransaction.replace(R.id.op_layout, fragment, tag);
        fragmentTransaction.commit();
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
                    picManager.init(BitmapFactory.decodeStream(cr.openInputStream(uri)));
                    updateCurPic(picManager.getColorPixels(), picManager.getWidth(), picManager.getHeight(), PicType.COLOR);
                    showProcessorFragment(R.id.menu_op_basic);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }

    }

    private void updateCurPic(int[] pixels) {
        curPic = pixels;
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(Bitmap.createBitmap(curPic, curWidth, curHeight, Bitmap.Config.ARGB_8888));
    }

    private void updateCurPic(int[] pixels, PicType type) {
        updateCurPic(pixels);

        curPicType = type;
        updateMenu();
    }

    private void updateCurPic(int[] pixels, int width, int height, PicType type) {
        curWidth = width;
        curHeight = height;
        updateCurPic(pixels, type);
    }

    public void onBasePicClicked(View view) {
        updateCurPic(basePic, basePicType);
    }

    @Override
    public void meanBlur(int size) {
        int[] dst = new int[curWidth * curHeight];
        Filter.meanBlur(curPic, dst, curWidth, curHeight, size);
        updateCurPic(dst);
    }

    @Override
    public void medianBlur(int size) {
        int[] dst = new int[curWidth * curHeight];
        Filter.medianBlur(curPic, dst, curWidth, curHeight, size);
        updateCurPic(dst);
    }

    @Override
    public void gaussianBlur(int size, double sigma) {
        int[] dst = new int[curWidth * curHeight];
        Filter.gaussianBlur(curPic, dst, curWidth, curHeight, size, sigma);
        updateCurPic(dst);
    }

    @Override
    public void sobel() {
        int[] dst = new int[curWidth * curHeight];
        Filter.sobel(basePic, dst, curWidth, curHeight);
        updateCurPic(dst);
    }

    @Override
    public void prewitt() {
        int[] dst = new int[curWidth * curHeight];
        Filter.prewitt(basePic, dst, curWidth, curHeight);
        updateCurPic(dst);
    }

    @Override
    public void roberts() {
        int[] dst = new int[curWidth * curHeight];
        Filter.roberts(basePic, dst, curWidth, curHeight);
        updateCurPic(dst);
    }

    @Override
    public void erode() {
        int[] dst = new int[curWidth * curHeight];
        Morphology.erode(curPic, dst, curWidth, curHeight);
        updateCurPic(dst);
    }

    @Override
    public void dilate() {
        int[] dst = new int[curWidth * curHeight];
        Morphology.dilate(curPic, dst, curWidth, curHeight);
        updateCurPic(dst);
    }

    @Override
    public void open() {
        int[] dst = new int[curWidth * curHeight];
        Morphology.open(curPic, dst, curWidth, curHeight);
        updateCurPic(dst);
    }

    @Override
    public void close() {
        int[] dst = new int[curWidth * curHeight];
        Morphology.close(curPic, dst, curWidth, curHeight);
        updateCurPic(dst);
    }

    @Override
    public void grayscale(Grayscale.GrayType type) {
        Grayscale.Return ret = Grayscale.generate(basePic, curWidth, curHeight, type);
        updateCurPic(ret.pixels, PicType.GRAY);
        histogram = ret.histogram;
        ImageView histogram = (ImageView) findViewById(R.id.grayscale_histogram_imageView);
        if (histogram != null) {
            histogram.setImageBitmap(Grayscale.Histogram(ret.histogram));
        }
    }

    public void updateMenu() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        if (curPicType == PicType.COLOR) {
            menu.findItem(R.id.menu_op_basic).setEnabled(true);
            menu.findItem(R.id.menu_op_binary).setEnabled(false);
            menu.findItem(R.id.menu_op_grayscale).setEnabled(true);
            menu.findItem(R.id.menu_op_filter).setEnabled(true);
            menu.findItem(R.id.menu_op_morphology).setEnabled(true);
            menu.findItem(R.id.menu_op_binary_morphology).setEnabled(false);
        } else if (curPicType == PicType.GRAY) {
            menu.findItem(R.id.menu_op_basic).setEnabled(true);
            menu.findItem(R.id.menu_op_binary).setEnabled(true);
            menu.findItem(R.id.menu_op_grayscale).setEnabled(false);
            menu.findItem(R.id.menu_op_filter).setEnabled(true);
            menu.findItem(R.id.menu_op_morphology).setEnabled(true);
            menu.findItem(R.id.menu_op_binary_morphology).setEnabled(false);
        } else if (curPicType == PicType.BINARY) {
            menu.findItem(R.id.menu_op_basic).setEnabled(true);
            menu.findItem(R.id.menu_op_binary).setEnabled(false);
            menu.findItem(R.id.menu_op_grayscale).setEnabled(false);
            menu.findItem(R.id.menu_op_filter).setEnabled(true);
            menu.findItem(R.id.menu_op_morphology).setEnabled(true);
            menu.findItem(R.id.menu_op_binary_morphology).setEnabled(true);
        }
    }

    @Override
    public int getOtusThreshold() {
        return histogram == null ? 0 : Binary.otsu(histogram);
    }

    @Override
    public void onBinaryThresholdUpdate(int min, int max) {
        updateCurPic(Binary.generate(basePic, curWidth, curHeight, min, max), PicType.BINARY);
    }

    @Override
    public void separateChannel(int channelMask) {
        int[] ret = Basic.channelSeparate(basePic, channelMask);
        updateCurPic(ret);
    }

    @Override
    public boolean isColor() {
        return curPicType == PicType.COLOR;
    }

    @Override
    public void transformDistance() {
        int[] dst = new int[curWidth * curHeight];
        BinaryMorphology.distanceTransform(basePic, dst, curWidth, curHeight);
        updateCurPic(dst, PicType.GRAY);
    }

    @Override
    public int[] skeleton() {
        int[] dst = new int[curWidth * curHeight];
        int[] skeleton = new int[curWidth * curHeight];
        BinaryMorphology.skeleton(basePic, dst, skeleton, curWidth, curHeight);
        updateCurPic(dst, PicType.BINARY);
        return skeleton;
    }

    @Override
    public void reconstruct(int[] skeleton) {
        int[] dst = new int[curWidth * curHeight];
        BinaryMorphology.reconstruct(skeleton, dst, curWidth, curHeight);
        updateCurPic(dst, PicType.BINARY);
    }

    private enum PicType {
        NONE,
        COLOR,
        GRAY,
        BINARY
    }

}
