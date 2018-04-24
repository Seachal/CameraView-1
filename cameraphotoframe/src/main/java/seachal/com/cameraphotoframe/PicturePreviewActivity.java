package seachal.com.cameraphotoframe;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.yalantis.ucrop.view.UCropView;

import java.io.FileOutputStream;
import java.lang.ref.WeakReference;


/**
 * 图片预览页面
 */
public class PicturePreviewActivity extends Activity {
    private static final String TAG = "ResultActivity";
    private static WeakReference<byte[]> image;

    /**
     *  其他activty调用此方法， 传递数据
     * @param im
     */
    public static void setImage(@Nullable byte[] im) {
        image = im != null ? new WeakReference<>(im) : null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_preview_new);
//        final ImageView imageView = findViewById(R.id.image);

        final long delay = getIntent().getLongExtra("delay", 0);
        final int nativeWidth = getIntent().getIntExtra("nativeWidth", 0);
        final int nativeHeight = getIntent().getIntExtra("nativeHeight", 0);
        byte[] b = image == null ? null : image.get();
        if (b == null) {
            finish();
            return;
        }

        Bitmap bitmap = Bytes2Bimap(b);
        try {
            UCropView uCropView = findViewById(R.id.ucrop);
            uCropView.getCropImageView().setImageBitmap(bitmap);
            uCropView.getOverlayView().setShowCropFrame(false);
            uCropView.getOverlayView().setShowCropGrid(false);
            uCropView.getOverlayView().setDimmedColor(Color.TRANSPARENT);
        } catch (Exception e) {
            Log.e(TAG, "setImageUri", e);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(new File(getIntent().getData().getPath()).getAbsolutePath(), options);
//


//        CameraUtils.decodeBitmap(b, 1000, 1000, new CameraUtils.BitmapCallback() {
//            @Override
//            public void onBitmapReady(Bitmap bitmap) {
//             //   imageView.setImageBitmap(bitmap);
//
//                saveBitmapToSDCard(bitmap,"camaraview1n");
//
//
//            }
//        });

    }

    private static float getApproximateFileMegabytes(Bitmap bitmap) {
        return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024 / 1024;
    }


    /**
     * 保存bitmap到SD卡
     * @param bitmap
     * @param imagename
     */
    public static String saveBitmapToSDCard(Bitmap bitmap, String imagename) {
        String path = "/0/" + "img-" + imagename + ".jpg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
            }

            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

}
