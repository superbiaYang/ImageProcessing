package superbiayang.imageprocessing;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;

import processor.Algebra;
import processor.Basic;
import processor.Binary;
import processor.Contrast;
import processor.Geometry;
import processor.Grayscale;
import processor.OpenCV.BinaryMorphology;
import processor.OpenCV.Filter;
import processor.OpenCV.GrayscaleMorphology;
import processor.OpenCV.Morphology;
import view.fragment.AlgebraFragment;
import view.fragment.BasicFragment;
import view.fragment.BinaryFragment;
import view.fragment.BinaryMorphologyFragment;
import view.fragment.ContrastFragment;
import view.fragment.FilterFragment;
import view.fragment.GeometryFragment;
import view.fragment.GrayscaleFragment;
import view.fragment.GrayscaleMorphologyFragment;
import view.fragment.MorphologyFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FilterFragment.OnFragmentInteractionListener,
        MorphologyFragment.OnFragmentInteractionListener,
        GrayscaleFragment.OnFragmentInteractionListener,
        BinaryFragment.OnFragmentInteractionListener,
        BasicFragment.OnFragmentInteractionListener,
        BinaryMorphologyFragment.OnFragmentInteractionListener,
        GrayscaleMorphologyFragment.OnFragmentInteractionListener,
        ContrastFragment.OnFragmentInteractionListener,
        GeometryFragment.OnFragmentInteractionListener,
        AlgebraFragment.OnFragmentInteractionListener,
        View.OnTouchListener {
    private static final SparseArray<String> MenuIdFragmentTag = new SparseArray<>();
    private static final int OPEN_PIC_MENU = 0;
    private static final int OPEN_PIC_ALGEBRA = 1;

    static {
        MenuIdFragmentTag.append(R.id.menu_op_basic, "BASIC_FRAGMENT");
    }

    private PicInfo curPic = null;
    private PicInfo basePic = null;
    private int[] histogram = null;
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

        ImageView view = (ImageView) findViewById(R.id.imageView);
        view.setOnTouchListener(this);

        basePic = new PicInfo();
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_gallery) {
            Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openAlbumIntent.setType("image/*");
            startActivityForResult(openAlbumIntent, OPEN_PIC_MENU);
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
        basePic.copy(curPic);
        ImageView baseView = (ImageView) findViewById(R.id.base_imageView);
        if (baseView != null) {
            baseView.setImageBitmap(
                    Bitmap.createBitmap(
                            basePic.getPixels(), basePic.getWidth(),
                            basePic.getHeight(), Bitmap.Config.ARGB_8888
                    )
            );
        }
        String tag = "fragment_" + id;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        ImageView curView = (ImageView) findViewById(R.id.imageView);
        curView.setOnTouchListener(null);
        if (fragment == null) {
            switch (id) {
                case R.id.menu_op_basic:
                    fragment = BasicFragment.newInstance();
                    curView.setOnTouchListener(this);
                    break;
                case R.id.menu_op_contrast:
                    fragment = ContrastFragment.newInstance();
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
                case R.id.menu_op_grayscale_morphology:
                    fragment = GrayscaleMorphologyFragment.newInstance();
                    break;
                case R.id.menu_op_geometry:
                    fragment = GeometryFragment.newInstance();
                    break;
                case R.id.menu_op_algebra:
                    fragment = AlgebraFragment.newInstance();
                    break;
            }
        }
        fragmentTransaction.replace(R.id.op_layout, fragment, tag);
        fragmentTransaction.commit();
        curFragment = id;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case OPEN_PIC_MENU:
                try {
                    Uri uri = data.getData();
                    ContentResolver cr = this.getContentResolver();
                    initCurPic(BitmapFactory.decodeStream(cr.openInputStream(uri)));
                    showProcessorFragment(R.id.menu_op_basic);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case OPEN_PIC_ALGEBRA:
                try {
                    Uri uri = data.getData();
                    ContentResolver cr = this.getContentResolver();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    AlgebraFragment fragment = (AlgebraFragment) fragmentManager.findFragmentByTag("fragment_" + curFragment);
                    fragment.setBitmap(BitmapFactory.decodeStream(cr.openInputStream(uri)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    private void initCurPic(Bitmap bitmap) {
        curPic = new PicInfo(bitmap);
        curPic.setPicType(PicInfo.PicType.COLOR);
        updateMenu();
        showCurPic();
    }

    private void showCurPic() {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        assert imageView != null;
        imageView.setImageBitmap(
                Bitmap.createBitmap(
                        curPic.getPixels(), curPic.getWidth(),
                        curPic.getHeight(), Bitmap.Config.ARGB_8888
                )
        );
    }

    private void updateCurPic(int[] pixels) {
        curPic.setPixels(pixels);
        showCurPic();
    }

    private void updateCurPic(int[] pixels, PicInfo.PicType type) {
        curPic.setPicType(type);
        updateCurPic(pixels);
        updateMenu();
    }

    private void updateCurPic(int[] pixels, int width, int height, PicInfo.PicType type) {
        curPic.setWidth(width);
        curPic.setHeight(height);
        updateCurPic(pixels, type);
    }

    public void onBasePicClicked(View view) {
        curPic.copy(basePic);
        showCurPic();
        updateMenu();
    }

    @Override
    public void meanBlur(int size) {
        int[] dst = new int[curPic.getPixelsNum()];
        Filter.meanBlur(curPic.getPixels(), dst, curPic.getWidth(), curPic.getHeight(), size);
        updateCurPic(dst);
    }

    @Override
    public void medianBlur(int size) {
        int[] dst = new int[curPic.getPixelsNum()];
        Filter.medianBlur(curPic.getPixels(), dst, curPic.getWidth(), curPic.getHeight(), size);
        updateCurPic(dst);
    }

    @Override
    public void gaussianBlur(int size, double sigma) {
        int[] dst = new int[curPic.getPixelsNum()];
        Filter.gaussianBlur(curPic.getPixels(), dst, curPic.getWidth(), curPic.getHeight(), size, sigma);
        updateCurPic(dst);
    }

    @Override
    public void sobel() {
        int[] dst = new int[basePic.getPixelsNum()];
        Filter.sobel(basePic.getPixels(), dst, basePic.getWidth(), basePic.getHeight());
        updateCurPic(dst);
    }

    @Override
    public void prewitt() {
        int[] dst = new int[basePic.getPixelsNum()];
        Filter.prewitt(basePic.getPixels(), dst, basePic.getWidth(), basePic.getHeight());
        updateCurPic(dst);
    }

    @Override
    public void roberts() {
        int[] dst = new int[basePic.getPixelsNum()];
        Filter.roberts(basePic.getPixels(), dst, basePic.getWidth(), basePic.getHeight());
        updateCurPic(dst);
    }

    @Override
    public void erode() {
        int[] dst = new int[curPic.getPixelsNum()];
        Morphology.erode(curPic.getPixels(), dst, curPic.getWidth(), curPic.getHeight());
        updateCurPic(dst);
    }

    @Override
    public void conditionalDilate() {
        int[] dst = new int[curPic.getPixelsNum()];
        BinaryMorphology.conditionalDilation(
                curPic.getPixels(), basePic.getPixels(),
                dst, curPic.getWidth(), curPic.getHeight());
        updateCurPic(dst);
    }

    @Override
    public void dilate() {
        int[] dst = new int[curPic.getPixelsNum()];
        Morphology.dilate(curPic.getPixels(), dst, curPic.getWidth(), curPic.getHeight());
        updateCurPic(dst);
    }

    @Override
    public void open() {
        int[] dst = new int[curPic.getPixelsNum()];
        Morphology.open(curPic.getPixels(), dst, curPic.getWidth(), curPic.getHeight());
        updateCurPic(dst);
    }

    @Override
    public void close() {
        int[] dst = new int[curPic.getPixelsNum()];
        Morphology.close(curPic.getPixels(), dst, curPic.getWidth(), curPic.getHeight());
        updateCurPic(dst);
    }

    @Override
    public Bitmap grayscale(Grayscale.GrayType type) {
        Grayscale.Return ret = Grayscale.generate(
                basePic.getPixels(), basePic.getWidth(), basePic.getHeight(), type);
        updateCurPic(ret.pixels, PicInfo.PicType.GRAY);
        histogram = ret.histogram;
        return Grayscale.Histogram(ret.histogram);
    }

    @Override
    public Bitmap equalize() {
        Grayscale.Return ret = Grayscale.equalize(
                curPic.getPixels(), curPic.getWidth(), curPic.getHeight(), histogram);
        updateCurPic(ret.pixels);
        histogram = ret.histogram;
        return Grayscale.Histogram(ret.histogram);
    }

    public void updateMenu() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        if (curPic.getPicType() == PicInfo.PicType.COLOR) {
            menu.findItem(R.id.menu_op_basic).setEnabled(true);
            menu.findItem(R.id.menu_op_geometry).setEnabled(true);
            menu.findItem(R.id.menu_op_contrast).setEnabled(true);
            menu.findItem(R.id.menu_op_binary).setEnabled(false);
            menu.findItem(R.id.menu_op_grayscale).setEnabled(true);
            menu.findItem(R.id.menu_op_filter).setEnabled(true);
            menu.findItem(R.id.menu_op_morphology).setEnabled(true);
            menu.findItem(R.id.menu_op_binary_morphology).setEnabled(false);
            menu.findItem(R.id.menu_op_grayscale_morphology).setEnabled(false);
            menu.findItem(R.id.menu_op_algebra).setEnabled(true);
        } else if (curPic.getPicType() == PicInfo.PicType.GRAY) {
            menu.findItem(R.id.menu_op_basic).setEnabled(true);
            menu.findItem(R.id.menu_op_contrast).setEnabled(true);
            menu.findItem(R.id.menu_op_geometry).setEnabled(true);
            menu.findItem(R.id.menu_op_binary).setEnabled(true);
            menu.findItem(R.id.menu_op_grayscale).setEnabled(false);
            menu.findItem(R.id.menu_op_filter).setEnabled(true);
            menu.findItem(R.id.menu_op_morphology).setEnabled(true);
            menu.findItem(R.id.menu_op_binary_morphology).setEnabled(false);
            menu.findItem(R.id.menu_op_grayscale_morphology).setEnabled(true);
            menu.findItem(R.id.menu_op_algebra).setEnabled(true);
        } else if (curPic.getPicType() == PicInfo.PicType.BINARY) {
            menu.findItem(R.id.menu_op_basic).setEnabled(true);
            menu.findItem(R.id.menu_op_contrast).setEnabled(true);
            menu.findItem(R.id.menu_op_geometry).setEnabled(true);
            menu.findItem(R.id.menu_op_binary).setEnabled(false);
            menu.findItem(R.id.menu_op_grayscale).setEnabled(false);
            menu.findItem(R.id.menu_op_filter).setEnabled(true);
            menu.findItem(R.id.menu_op_morphology).setEnabled(true);
            menu.findItem(R.id.menu_op_binary_morphology).setEnabled(true);
            menu.findItem(R.id.menu_op_grayscale_morphology).setEnabled(true);
            menu.findItem(R.id.menu_op_algebra).setEnabled(true);
        }
    }

    @Override
    public int getOtusThreshold() {
        return histogram == null ? 0 : Binary.otsu(histogram);
    }

    @Override
    public void onBinaryThresholdUpdate(int min, int max) {
        updateCurPic(Binary.generate(
                basePic.getPixels(), basePic.getWidth(), curPic.getHeight(), min, max), PicInfo.PicType.BINARY);
    }

    @Override
    public void separateChannel(int channelMask) {
        int[] ret = Basic.channelSeparate(basePic.getPixels(), channelMask);
        updateCurPic(ret);
    }

    @Override
    public boolean isColor() {
        return curPic.getPicType() == PicInfo.PicType.COLOR;
    }

    @Override
    public void transformDistance() {
        int[] dst = new int[curPic.getPixelsNum()];
        BinaryMorphology.distanceTransform(basePic.getPixels(), dst, basePic.getWidth(), basePic.getHeight());
        updateCurPic(dst, PicInfo.PicType.GRAY);
    }

    @Override
    public int[] skeleton() {
        int[] dst = new int[basePic.getPixelsNum()];
        int[] skeleton = new int[basePic.getPixelsNum()];
        BinaryMorphology.skeleton(basePic.getPixels(), dst, skeleton, basePic.getWidth(), basePic.getHeight());
        updateCurPic(dst, PicInfo.PicType.BINARY);
        return skeleton;
    }

    @Override
    public void reconstruct(int[] skeleton) {
        int[] dst = new int[curPic.getPixelsNum()];
        BinaryMorphology.reconstruct(skeleton, dst, curPic.getWidth(), curPic.getHeight());
        updateCurPic(dst, PicInfo.PicType.BINARY);
    }

    @Override
    public void standardGradient() {
        int[] dst = new int[basePic.getPixelsNum()];
        GrayscaleMorphology.standardGradient(basePic.getPixels(), dst, basePic.getWidth(), basePic.getHeight());
        updateCurPic(dst);
    }

    @Override
    public void externalGradient() {
        int[] dst = new int[basePic.getPixelsNum()];
        GrayscaleMorphology.externalGradient(basePic.getPixels(), dst, basePic.getWidth(), basePic.getHeight());
        updateCurPic(dst);
    }

    @Override
    public void internalGradient() {
        int[] dst = new int[basePic.getPixelsNum()];
        GrayscaleMorphology.internalGradient(basePic.getPixels(), dst, basePic.getWidth(), basePic.getHeight());
        updateCurPic(dst);
    }

    @Override
    public void externalEdge() {
        int[] dst = new int[basePic.getPixelsNum()];
        BinaryMorphology.externalEdge(basePic.getPixels(), dst, basePic.getWidth(), basePic.getHeight());
        updateCurPic(dst);
    }

    @Override
    public void internalEdge() {
        int[] dst = new int[basePic.getPixelsNum()];
        BinaryMorphology.internalEdge(basePic.getPixels(), dst, basePic.getWidth(), basePic.getHeight());
        updateCurPic(dst);
    }

    @Override
    public void standardEdge() {
        int[] dst = new int[basePic.getPixelsNum()];
        BinaryMorphology.standardEdge(basePic.getPixels(), dst, basePic.getWidth(), basePic.getHeight());
        updateCurPic(dst);
    }

    @Override
    public void OBR() {
        int[] dst = new int[basePic.getPixelsNum()];
        GrayscaleMorphology.OBR(basePic.getPixels(), dst, basePic.getWidth(), basePic.getHeight(), 5);
        updateCurPic(dst);
    }

    @Override
    public void CBR() {
        int[] dst = new int[basePic.getPixelsNum()];
        GrayscaleMorphology.CBR(basePic.getPixels(), dst, basePic.getWidth(), basePic.getHeight(), 5);
        updateCurPic(dst);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getX() < 0 || event.getX() >= v.getWidth() ||
                event.getY() < 0 || event.getY() >= v.getHeight()) {
            String info = "width: -, height: -, zoom: -%";
            TextView text = (TextView) findViewById(R.id.size_info_textView);
            if (text != null) {
                text.setText(info);
            }

            info = "X: -, Y: -";
            text = (TextView) findViewById(R.id.coordinate_textView);
            if (text != null) {
                text.setText(info);
            }

            info = "R: -, G: -, B: -";
            text = (TextView) findViewById(R.id.color_textView);
            if (text != null) {
                text.setText(info);
            }

            return true;
        }

        int real_x = (int) (event.getX() * curPic.getWidth() / v.getWidth());
        int real_y = (int) (event.getY() * curPic.getHeight() / v.getHeight());

        int pixel = curPic.getPixels()[real_x + real_y * curPic.getWidth()];
        int r = Color.red(pixel);
        int g = Color.green(pixel);
        int b = Color.blue(pixel);


        String info = "width: " + v.getWidth()
                + ", height: " + v.getHeight()
                + ", zoom: " + v.getWidth() * 100 / curPic.getWidth() + "%";
        TextView text = (TextView) findViewById(R.id.size_info_textView);
        if (text != null) {
            text.setText(info);
        }

        info = "X: " + (int) event.getX() + ", Y: " + (int) event.getY();
        text = (TextView) findViewById(R.id.coordinate_textView);
        if (text != null) {
            text.setText(info);
        }

        info = "R: " + r + ", G: " + g + ", B: " + b;
        text = (TextView) findViewById(R.id.color_textView);
        if (text != null) {
            text.setText(info);
        }

        return true;
    }

    @Override
    public void linearContrast(double k, double b) {
        int[] dst = new int[basePic.getPixelsNum()];
        Contrast.linear(basePic.getPixels(), dst, k, b);
        updateCurPic(dst);
    }

    @Override
    public void logContrast(double a, double b, double c) {
        int[] dst = new int[basePic.getPixelsNum()];
        Contrast.log(basePic.getPixels(), dst, a, b, c);
        updateCurPic(dst);
    }

    @Override
    public void powContrast(double a, double b, double c) {
        int[] dst = new int[basePic.getPixelsNum()];
        Contrast.pow(basePic.getPixels(), dst, a, b, c);
        updateCurPic(dst);
    }

    @Override
    public void rotate(int degree, int choice) {
        PicInfo ret = null;
        switch (choice) {
            case R.id.nearest_neighbor_radioButton:
                ret = Geometry.nearestNeighborRotate(basePic, degree);
                break;
            case R.id.bilinear_interpolation_radioButton:
                ret = Geometry.bilinearInterpolationRotate(basePic, degree);
                break;
        }
        if (ret != null) {
            updateCurPic(ret.getPixels(), ret.getWidth(), ret.getHeight(), ret.getPicType());
        }
    }

    @Override
    public void resize(int width, int height, int choice) {
        PicInfo ret = null;
        switch (choice) {
            case R.id.nearest_neighbor_radioButton:
                ret = Geometry.nearestNeighborResize(basePic, width, height);
                break;
            case R.id.bilinear_interpolation_radioButton:
                ret = Geometry.bilinearInterpolationResize(basePic, width, height);
                break;
        }
        if (ret != null) {
            updateCurPic(ret.getPixels(), ret.getWidth(), ret.getHeight(), ret.getPicType());
        }
    }

    @Override
    public void openAlgebraBitmap() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, OPEN_PIC_ALGEBRA);
    }

    @Override
    public void add(PicInfo pic) {
        PicInfo ret = Algebra.add(basePic, pic);
        if (ret != null) {
            updateCurPic(ret.getPixels(), ret.getWidth(), ret.getHeight(), ret.getPicType());
        }
    }

    @Override
    public void sub(PicInfo pic) {
        PicInfo ret = Algebra.sub(basePic, pic);
        if (ret != null) {
            updateCurPic(ret.getPixels(), ret.getWidth(), ret.getHeight(), ret.getPicType());
        }
    }

    @Override
    public void multi(PicInfo pic) {
        PicInfo ret = Algebra.multi(basePic, pic);
        if (ret != null) {
            updateCurPic(ret.getPixels(), ret.getWidth(), ret.getHeight(), ret.getPicType());
        }
    }
}
