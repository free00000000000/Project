package com.example.project.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import timber.log.Timber;
import uk.co.senab.photoview.PhotoView;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.project.FTPManager;
import com.example.project.OpenCVCallback;
import com.example.project.R;
import com.example.project.views.QuadrilateralSelectionImageView;

import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class UploadImageActivity extends AppCompatActivity {
    QuadrilateralSelectionImageView mSelectionImageView;
    Button mButton;

    Bitmap mBitmap;
    Bitmap mResult;

    MaterialDialog mResultDialog;
    OpenCVCallback mOpenCVLoaderCallback;

    private static final int MAX_HEIGHT = 500;

    private static int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        setTitle("Step 1");

        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        }


        mSelectionImageView = findViewById(R.id.polygonView);
        mButton = findViewById(R.id.button);

        mResultDialog = new MaterialDialog.Builder(this)
                .title("Scan Result")
                .positiveText("Next")
                .negativeText("Cancel")
                .customView(R.layout.dialog_document_scan_result, false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        // TODO Saving
                        File Img = saveImageToExternalStorage(mResult);
                        Toast.makeText(getApplicationContext(), "Saved successfully, Check gallery", Toast.LENGTH_SHORT).show();
                        mResult = null;

                        try {
                            Log.d("myTag", "ftp connecting");
                            FTPManager ftpm = new FTPManager();
                            if (ftpm.connect()) {
                                if (ftpm.uploadFile(Img)) {
                                    ftpm.closeFTP();
                                    Toast.makeText(getApplicationContext(), "ftp upload success", Toast.LENGTH_SHORT).show();
                                    Log.d("myTag", "ftp upload success");
                                    // TODO: remove img
                                    goNext(Img.getName());
                                }
                                ftpm.closeFTP();
                            }
                        } catch (Exception e) {
                            Log.d("myTag", e.getMessage()+" ");
                            Log.d("myTag", "ftp failed");
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        mResult = null;
                    }
                })
                .build();

//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//
//        });

        mOpenCVLoaderCallback = new OpenCVCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS: {
                        break;
                    }

                    default: {
                        super.onManagerConnected(status);
                    }
                }
            }
        };
        initOpenCV();

    }

    public void Button_onclick(View v) {
        List<PointF> points = mSelectionImageView.getPoints();

        if (mBitmap != null) {
            Mat orig = new Mat();
            org.opencv.android.Utils.bitmapToMat(mBitmap, orig);

            Mat transformed = perspectiveTransform(orig, points);
            mResult = applyThreshold(transformed);

            if (mResultDialog.getCustomView() != null) {
                PhotoView photoView = mResultDialog.getCustomView().findViewById(R.id.imageView);
                photoView.setImageBitmap(mResult);
                mResultDialog.show();
            }
            orig.release();
            transformed.release();
        }
    }

    public void goNext(String img_name) {
        // TODO: remove jpg
        Intent intent = new Intent(this, SendToServerActivity.class);
        intent.putExtra("img_name", img_name);
        Log.d("myTag", "Next");
        startActivity(intent);

    }

    private boolean checkPermission() {
        int storageResult = ContextCompat.checkSelfPermission(UploadImageActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraResult = ContextCompat.checkSelfPermission(UploadImageActivity.this, android.Manifest.permission.CAMERA);
        if (storageResult == PackageManager.PERMISSION_GRANTED && cameraResult == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "storage and camera permission granted", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(UploadImageActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                ActivityCompat.shouldShowRequestPermissionRationale(UploadImageActivity.this, android.Manifest.permission.CAMERA)) {
            Toast.makeText(getApplicationContext(), "Write External Storage & Camera permission allows us to do store images & take photos. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(UploadImageActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }

    }

    private File saveImageToExternalStorage(Bitmap finalBitmap) {
        File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "DocScanner");
        if (!myDir.exists()) {
            myDir.mkdir();
            Toast.makeText(getApplicationContext(), "Directory not exist, create it", Toast.LENGTH_SHORT).show();
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
//        Log.d("myTag", file.getPath());
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            //Toast.makeText(getApplicationContext(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/DocScanner", Toast.LENGTH_SHORT).show();
            out.flush();
            out.close();
        }
        catch (Exception e) {
            Log.e("ERROR", "ERROR IN CODE: " + e.toString());
            Toast.makeText(UploadImageActivity.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
        return file;
    }

    private Mat perspectiveTransform(Mat src, List<PointF> points) {
        Point point1 = new Point(points.get(0).x, points.get(0).y);
        Point point2 = new Point(points.get(1).x, points.get(1).y);
        Point point3 = new Point(points.get(2).x, points.get(2).y);
        Point point4 = new Point(points.get(3).x, points.get(3).y);
        Point[] pts = {point1, point2, point3, point4};
        return fourPointTransform(src, sortPoints(pts));
    }

    private Point[] sortPoints(Point[] src) {
        ArrayList<Point> srcPoints = new ArrayList<>(Arrays.asList(src));
        Point[] result = {null, null, null, null};

        Comparator<Point> sumComparator = new Comparator<Point>() {
            @Override
            public int compare(Point lhs, Point rhs) {
                return Double.valueOf(lhs.y + lhs.x).compareTo(rhs.y + rhs.x);
            }
        };
        Comparator<Point> differenceComparator = new Comparator<Point>() {
            @Override
            public int compare(Point lhs, Point rhs) {
                return Double.valueOf(lhs.y - lhs.x).compareTo(rhs.y - rhs.x);
            }
        };

        result[0] = Collections.min(srcPoints, sumComparator);        // Upper left has the minimal sum
        result[2] = Collections.max(srcPoints, sumComparator);        // Lower right has the maximal sum
        result[1] = Collections.min(srcPoints, differenceComparator); // Upper right has the minimal difference
        result[3] = Collections.max(srcPoints, differenceComparator); // Lower left has the maximal difference

        return result;
    }

    /**
     * NOTE:
     * Based off of http://www.pyimagesearch.com/2014/08/25/4-point-opencv-getperspective-transform-example/
     *
     * @param src
     * @param pts
     * @return
     */
    private Mat fourPointTransform(Mat src, Point[] pts) {
        double ratio = src.size().height / (double) MAX_HEIGHT;

        Point ul = pts[0];
        Point ur = pts[1];
        Point lr = pts[2];
        Point ll = pts[3];

        double widthA = Math.sqrt(Math.pow(lr.x - ll.x, 2) + Math.pow(lr.y - ll.y, 2));
        double widthB = Math.sqrt(Math.pow(ur.x - ul.x, 2) + Math.pow(ur.y - ul.y, 2));
        double maxWidth = Math.max(widthA, widthB) * ratio;

        double heightA = Math.sqrt(Math.pow(ur.x - lr.x, 2) + Math.pow(ur.y - lr.y, 2));
        double heightB = Math.sqrt(Math.pow(ul.x - ll.x, 2) + Math.pow(ul.y - ll.y, 2));
        double maxHeight = Math.max(heightA, heightB) * ratio;

        Mat resultMat = new Mat(Double.valueOf(maxHeight).intValue(), Double.valueOf(maxWidth).intValue(), CvType.CV_8UC4);

        Mat srcMat = new Mat(4, 1, CvType.CV_32FC2);
        Mat dstMat = new Mat(4, 1, CvType.CV_32FC2);
        srcMat.put(0, 0, ul.x * ratio, ul.y * ratio, ur.x * ratio, ur.y * ratio, lr.x * ratio, lr.y * ratio, ll.x * ratio, ll.y * ratio);
        dstMat.put(0, 0, 0.0, 0.0, maxWidth, 0.0, maxWidth, maxHeight, 0.0, maxHeight);

        Mat M = Imgproc.getPerspectiveTransform(srcMat, dstMat);
        Imgproc.warpPerspective(src, resultMat, M, resultMat.size());

        srcMat.release();
        dstMat.release();
        M.release();

        return resultMat;
    }

    private Bitmap applyThreshold(Mat src) {
        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);

        // Some other approaches
//        Imgproc.adaptiveThreshold(src, src, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, 15);
//        Imgproc.threshold(src, src, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);

        Imgproc.GaussianBlur(src, src, new Size(5, 5), 0);
        Imgproc.adaptiveThreshold(src, src, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 21, 15);

        Bitmap bm = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
        org.opencv.android.Utils.matToBitmap(src, bm);

        return bm;
    }

    private void initOpenCV() {
        if (!OpenCVLoader.initDebug()) {
            Timber.d("Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mOpenCVLoaderCallback);
        } else {
            Timber.d("OpenCV library found inside package. Using it!");
            mOpenCVLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initOpenCV();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_upload_image_value, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_gallery) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        } else if (id == R.id.action_camera) {
            // TODO Camera
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.joshuabutton.documentscanner.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Toast.makeText(UploadImageActivity.this, "Saved in " + currentPhotoPath, Toast.LENGTH_SHORT).show();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                if(mBitmap.getHeight() < mBitmap.getWidth()) {  // If the image rotation is wrong
                    Log.i("ROTATED", "ROTATED");
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
                }
                else {
                    Log.i("NOT ROTATED", "NOT ROTATED");
                }
                mSelectionImageView.setImageBitmap(getResizedBitmap(mBitmap, MAX_HEIGHT));
                List<PointF> points = findPoints();
                mSelectionImageView.setPoints(points);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            try {
                File file = new File(currentPhotoPath);
                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                if(mBitmap.getHeight() < mBitmap.getWidth()) {  // If the image rotation is wrong
                    Log.i("ROTATED", "ROTATED");
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
                }
                else {
                    Log.i("NOT ROTATED", "NOT ROTATED");
                }
                if (mBitmap != null) {
                    mSelectionImageView.setImageBitmap(getResizedBitmap(mBitmap, MAX_HEIGHT));
                    Toast.makeText(UploadImageActivity.this, "Showing image", Toast.LENGTH_SHORT).show();
                    List<PointF> points = findPoints();
                    mSelectionImageView.setPoints(points);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(UploadImageActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Bitmap getResizedBitmap(Bitmap bitmap, int maxHeight) {
        double ratio = bitmap.getHeight() / (double) maxHeight;
        int width = (int) (bitmap.getWidth() / ratio);
        return Bitmap.createScaledBitmap(bitmap, width, maxHeight, false);
    }

    private List<PointF> findPoints() {
        List<PointF> result = null;

        Mat image = new Mat();
        Mat orig = new Mat();

        Log.i("ORIGINAL", "Height = " + mBitmap.getHeight() + " Width = " + mBitmap.getWidth());
        org.opencv.android.Utils.bitmapToMat(getResizedBitmap(mBitmap, MAX_HEIGHT), image);
        org.opencv.android.Utils.bitmapToMat(mBitmap, orig);
        Log.i("RESIZED", "Height = " + image.height() + " Width = " + image.width());

        Mat lines = edgeDetection(image);
        Point[] outer = findOuterLines(lines);

        if (outer != null) {
            result = new ArrayList<>();
            result.add(new PointF(Double.valueOf(outer[0].x).floatValue(), Double.valueOf(outer[0].y).floatValue()));
            result.add(new PointF(Double.valueOf(outer[1].x).floatValue(), Double.valueOf(outer[1].y).floatValue()));
            result.add(new PointF(Double.valueOf(outer[2].x).floatValue(), Double.valueOf(outer[2].y).floatValue()));
            result.add(new PointF(Double.valueOf(outer[3].x).floatValue(), Double.valueOf(outer[3].y).floatValue()));
        } else {
            Timber.d("Can't find rectangle!");
        }

        lines.release();
        image.release();
        orig.release();

        return result;
    }

    /**
     * Detect the edges in the given Mat
     * @param src A valid Mat object
     * @return A Mat processed to find edges
     */
    private Mat edgeDetection(Mat src) {
        Mat edges = new Mat();
        Mat lines = new Mat();
        Imgproc.cvtColor(src, edges, Imgproc.COLOR_BGR2GRAY);
        Imgproc.adaptiveThreshold(edges, edges, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 15);
        Imgproc.Canny(edges, edges, 50, 150);
        Imgproc.HoughLines(edges, lines, 1, Math.PI/180, 120);
        return lines;
    }

    /**
     * Find the outer 4 lines in the given Mat.
     *
     * @param lines A valid Mat
     * @return The outer 4 lines as a Mat
     */
    private Point[] findOuterLines(Mat lines) {
        // Standard Hough Line Transform
        double ymin = 1000000, xmin = 1000000, ymax = -1000000, xmax = -1000000;
        Point vmax = new Point(0,0), vmin = new Point(0,0), hmax = new Point(0,0), hmin = new Point(0,0);
        Point left1 = new Point(), left2 = new Point(), right1 = new Point(), right2 = new Point();
        Point top1 = new Point(), top2 = new Point(), bottom1 = new Point(), bottom2 = new Point();
        // Find the lines
        for(int i = 0; i< lines.rows(); i++) {
            double rho = lines.get(i,0)[0],
                    theta = lines.get(i, 0)[1];
            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a*rho, y0 = b*rho;
            Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
            Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));

            // Check vertical
            if ((theta > 3 && theta < 3.2) || theta < 0.1) {
                if (Math.abs(rho) > xmax) {
                    xmax = Math.abs(rho);
                    vmax = new Point(lines.get(i, 0));
                    left1 = pt1;
                    left2 = pt2;
                }
                if(Math.abs(rho) < xmin) {
                    xmin = Math.abs(rho);
                    vmin = new Point(lines.get(i, 0));
                    right1 = pt1;
                    right2 = pt2;
                }
            }
            // Check horizontal
            else if(theta > 1.4 && theta < 1.7) {
                if(Math.abs(rho) < ymin) {
                    ymin = Math.abs(rho);
                    top1 = pt1;
                    top2 = pt2;
                    hmin = new Point(lines.get(i, 0));
                    Log.i("HMIN", hmin.x+","+hmin.y);
                }
                if(Math.abs(rho) > ymax) {
                    ymax = Math.abs(rho);
                    hmax = new Point(lines.get(i,0));
                    bottom1 = pt1;
                    bottom2 = pt2;
                }
            }
        }

        Point approx[] = new Point[4];
        approx[0] = intersection(hmin, vmax); // top right
        approx[1] = intersection(hmax, vmax); // bottom right
        approx[2] = intersection(hmax, vmin); // bottom left
        approx[3] = intersection(hmin, vmin); // top left

        Log.i("APPROX", "approx = " + Double.toString(hmin.x) +","+ Double.toString(hmin.y) + "   "+Double.toString(hmax.x) +"," + Double.toString(hmax.y)
                + "   " + Double.toString(vmin.x) + "," +Double.toString(vmin.y) + "   "+Double.toString(vmax.x) + "," + Double.toString(vmax.y));

        return approx;
    }

    private Point intersection(Point line1, Point line2) {
        double rho1 = line1.x, theta1 = line1.y;
        double rho2 = line2.x, theta2 = line2.y;
        // new Mat(rows, col, type, data)
        double[][] data = new double[][]{{Math.cos(theta1), Math.sin(theta1)},
                {Math.cos(theta2), Math.sin(theta2)}};
        double ct1 = Math.cos(theta1), ct2 = Math.cos(theta2);
        double st1 = Math.sin(theta1), st2 = Math.sin(theta2);
        // determinant (rearranged matrix for inverse)
        double d = ct1*st2 - st1*ct2;
        double x = (st2*rho1-st1*rho2)/d;
        double y = (-ct2*rho1+ct1*rho2)/d;

        return new Point(x, y);
    }



}
