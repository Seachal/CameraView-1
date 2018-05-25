package seachal.com.cameraphotoframe.crop;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import seachal.com.cameraphotoframe.R;

/**
 * Created by Oleksii Shliama (https://github.com/shliama).
 */
public class ResultActivity extends Activity {


    private static final String TAG = "ResultActivity";
    private static final String CHANNEL_ID = "3000";
    private static final int DOWNLOAD_NOTIFICATION_ID_DONE = 911;


    private ImageView  iv_result;
    private Button   bt_close;
    private  String result_path;
    private  int themeId ;

    public static void startWithUri(@NonNull Context context, @NonNull Uri uri) {
        Intent intent = new Intent(context, ResultActivity.class);

        intent.setData(uri);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        themeId = R.style.AppTheme;
        iv_result = findViewById(R.id.iv_result);
        bt_close = findViewById(R.id.bt_close);

        LocalMedia media = getIntent().getParcelableExtra("Select_media");
        if (media != null) {
            result_path = media.getCutPath();
//            if (uri != null) {
//                try {
//                    UCropView uCropView = findViewById(R.id.ucrop);
//                    uCropView.getCropImageView().setImageUri(uri, null);
//                    uCropView.getOverlayView().setShowCropFrame(false);
//                    uCropView.getOverlayView().setShowCropGrid(false);
//                    uCropView.getOverlayView().setDimmedColor(Color.TRANSPARENT);
//                } catch (Exception e) {
//                    Log.e(TAG, "setImageUri", e);
//                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//            final BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(new File(getIntent().getData().getPath()).getAbsolutePath(), options);
//
//            setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//            final ActionBar actionBar = getSupportActionBar();
//            if (actionBar != null) {
//                actionBar.setDisplayHomeAsUpEnabled(true);
//                actionBar.setTitle(getString(R.string.format_crop_result_d_d, options.outWidth, options.outHeight));
//            }
        }else{

               Uri resultUri =  MyUCrop.getOutput(getIntent());
               result_path = resultUri.getPath();
        }

        final List<LocalMedia> selectList = new ArrayList<>();
        LocalMedia  localMedia = new LocalMedia();
        localMedia.setPath(result_path);
        selectList.add(localMedia);
        //  glide
        RequestOptions options = new RequestOptions()
                .centerCrop()
//                .placeholder(R.color.color_f6)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(getApplicationContext())
                .load(result_path)
                .apply(options)
                .into(iv_result);
        iv_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                PictureSelector.create(ResultActivity.this).themeStyle(themeId).openExternalPreview(0,selectList );

            }
        });

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



}
