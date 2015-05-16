package jp.co.spajam.androidapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


//以下の権限が必要
//<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
//<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

public class PhotoActivity extends ActionBarActivity {

    private static final String TAG = "PhotoActivity";
    private static final String DIR_NAME = "animal_ball";
    private static final int size_X = 800;
    private static final int size_Y = 1200;

    private static final int REQUEST_CODE_CAMERA  = 0x00000001;

    private Uri bitmapUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        //デバッグ用
        ((TextView) findViewById(R.id.title)).setText(System.currentTimeMillis() % 1000 + "");

        //ボタンが押されたら、カメラ起動
        findViewById(R.id.photoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wakeupCamera();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
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

    protected void wakeupCamera(){

        // ストレージがなければ作成、既に生成済みであればそれを使う。
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ),  DIR_NAME
        );

        if(! mediaStorageDir.exists() & ! mediaStorageDir.mkdirs()) {
            Log.e(TAG, "Failed to create directory");
            return;
        }

        // ファイル名は日付時刻
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File  bitmapFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + ".JPG");
        bitmapUri = Uri.fromFile(bitmapFile);

        //
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, bitmapUri);
        startActivityForResult(i, REQUEST_CODE_CAMERA);
    }

    public void onActivityResult(
            int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK){
            // 撮影した画像をギャラリーのインデックスに追加されるようにスキャンさせる
            String[] paths = {bitmapUri.getPath()};
            String[] mimeTypes = {"image/*"};
            MediaScannerConnection.scanFile(
                    getApplicationContext(), paths, mimeTypes, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });

            // 保存パスを表示
            ((TextView) findViewById(R.id.title)).setText(paths[0]);

            // プレビュー描画
            ImageView imgView = (ImageView) findViewById(R.id.photo);
            openPictureResize(bitmapUri, imgView, size_X, size_Y);
            // URL登録準備
        }
        // 戻るを押した場合は破棄
        else {
            //finish();
        }
    }


    public void openPictureResize(Uri uri,ImageView imgView,int x,int y){

        // 画像生成
        Bitmap bitmap = createBitmap(uri,x,y,true);

        // リサイズして表示する。(無くても良い)
        bitmap = resize(bitmap, x, y);


        imgView.setImageBitmap(bitmap);
        if(bitmap == null){
            imgView.setImageResource(R.drawable.abc_cab_background_top_material);
        }

    }

    /**
     * 画像生成
     * 表示サイズ合わせて画像生成時に可能なかぎり縮小して生成します。
     *
     * @param width 幅
     * @param height 高さ
     * @return 生成Bitmap
     */
    private Bitmap createBitmap(Uri uri, int width, int height,boolean colorless) {

        BitmapFactory.Options option = new BitmapFactory.Options();

        Bitmap bitmap  = null;
        InputStream in = null;

        // 情報のみ読み込む
        option.inJustDecodeBounds = true;
        // 参照が無いときは消してくれる
        option.inPurgeable=true;
        if(colorless){
            // 色制限
            option.inPreferredConfig = Bitmap.Config.RGB_565;
        }
        try {
            in = getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(in, null, option);
            in.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SecurityException e){
            // 写真がなくなった、など
            e.printStackTrace();
            return null;
        }
        in = null;


        if (option.outWidth < width || option.outHeight < height) {
            // 縦、横のどちらかが指定値より小さい場合は普通にBitmap生成
            try {
                in = getContentResolver().openInputStream(uri);
                // 実データをそのまま読み込む
                option.inJustDecodeBounds = false;
                if(colorless){
                    option.inPreferredConfig = Bitmap.Config.ALPHA_8;
                }
                bitmap =  BitmapFactory.decodeStream(in,null,option);
                in.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SecurityException e){
                e.printStackTrace();
                return null;
            }
            return bitmap;

        }

        float scaleWidth = ((float) width) / option.outWidth;
        float scaleHeight = ((float) height) / option.outHeight;

        int newSize = 0;
        int oldSize = 0;
        if (scaleWidth > scaleHeight) {
            newSize = width;
            oldSize = option.outWidth;
        } else {
            newSize = height;
            oldSize = option.outHeight;
        }

        // option.inSampleSizeに設定する値を求める
        // option.inSampleSizeは2の乗数のみ設定可能
        int sampleSize = 1;
        int tmpSize = oldSize;
        while (tmpSize > newSize) {
            sampleSize = sampleSize * 2;
            tmpSize = oldSize / sampleSize;
        }
        if (sampleSize != 1) {
            sampleSize = sampleSize / 2;
        }

        option.inJustDecodeBounds = false;
        option.inSampleSize = sampleSize;


        try {
            in = getContentResolver().openInputStream(uri);

            bitmap =  BitmapFactory.decodeStream(in, null, option);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bitmap;
    }


    /**
     * 画像リサイズ
     * @param bitmap 変換対象ビットマップ
     * @param newWidth 変換サイズ横
     * @param newHeight 変換サイズ縦
     * @return 変換後Bitmap
     */
    private static Bitmap resize(Bitmap bitmap, int newWidth, int newHeight) {

        if (bitmap == null) {
            return null;
        }

        int oldWidth = bitmap.getWidth();
        int oldHeight = bitmap.getHeight();

        if (oldWidth < newWidth && oldHeight < newHeight) {
            // 縦も横も指定サイズより小さい場合は何もしない
            return bitmap;
        }

        float scaleWidth = ((float) newWidth) / oldWidth;
        float scaleHeight = ((float) newHeight) / oldHeight;
        float scaleFactor = Math.min(scaleWidth, scaleHeight);

        Matrix scale = new Matrix();
        scale.postScale(scaleFactor, scaleFactor);

        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, oldWidth, oldHeight, scale, false);
        bitmap.recycle();

        return resizeBitmap;

    }


}
