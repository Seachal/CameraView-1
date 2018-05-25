package seachal.com.cameraphotoframe;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.otaliastudios.cameraview.CameraLogger;
import com.wonderkiln.camerakit.CameraKitEventCallback;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import seachal.com.cameraphotoframe.crop.BaseActivity;
import seachal.com.cameraphotoframe.crop.MyUCrop;
import seachal.com.cameraphotoframe.crop.MyUcropActivity;
import seachal.com.cameraphotoframe.crop.ResultActivity;
import seachal.com.cameraphotoframe.scanner.ScannerFinderView;


/**
 * 测试camerakit库的capture图片的速度。 速度一点也不快，所以不考虑使用camerakit库。
 */

public class CameraActivityKit extends BaseActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener  {

    public static final String  TAG  = CameraActivityKit.class.getSimpleName()+"2:";

    private CameraView camera;
//    private ViewGroup controlPanel;

    private boolean mCapturingPicture;//  是否正在捕捉图像，正在onPicture中置为false。 拍照按钮根据此值判断点击可用、不可用
    private boolean mCapturingVideo;

    // To show stuff in the callback   图片尺寸信息
//    private Size mCaptureNativeSize;
    private long mCaptureTime;

    private long mSkipTime;



    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 134;
////seachal
//    private SurfaceHolder mHolder;


    private ScannerFinderView mQrCodeFinderView;
    private static final int REQUEST_CODE_CAMERA = 131;

//    select  begin
private final static String TAGS = CameraActivityKit.class.getSimpleName();
    private List<LocalMedia> selectList = new ArrayList<>();
    private RecyclerView recyclerView;
    private int maxSelectNum = 9;
    private TextView tv_select_num;
    private ImageView left_back, minus, plus;
    private RadioGroup rgb_crop, rgb_style, rgb_photo_mode;
    private int aspect_ratio_x, aspect_ratio_y;
    private CheckBox cb_voice, cb_choose_mode, cb_isCamera, cb_isGif,
            cb_preview_img, cb_preview_video, cb_crop, cb_compress,
            cb_mode, cb_hide, cb_crop_circular, cb_styleCrop, cb_showCropGrid,
            cb_showCropFrame, cb_preview_audio;
    private int themeId;
    private int chooseMode = PictureMimeType.ofAll();

//  select  end

    //   ===== urop begin
//    private static final String TAG = "SampleActivity";

    private static final int REQUEST_SELECT_PICTURE = 0x01;
    private static final int REQUEST_SELECT_PICTURE_FOR_FRAGMENT = 0x02;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";

    private RadioGroup mRadioGroupAspectRatio, mRadioGroupCompressionSettings;
    private EditText mEditTextMaxWidth, mEditTextMaxHeight;
    private EditText mEditTextRatioX, mEditTextRatioY;
    private CheckBox mCheckBoxMaxSize;
    private SeekBar mSeekBarQuality;
    private TextView mTextViewQuality;
    private CheckBox mCheckBoxHideBottomControls;
    private CheckBox mCheckBoxFreeStyleCrop;
    private Toolbar toolbar;
    private ScrollView settingsView;


//    private UCropFragment fragment;
    private boolean mShowLoader;

    private String mToolbarTitle;
    @DrawableRes
    private int mToolbarCancelDrawable;
    @DrawableRes
    private int mToolbarCropDrawable;
    // Enables dynamic coloring
    private int mToolbarColor;
    private int mStatusBarColor;
    private int mToolbarWidgetColor;

//    ==== ucrop end

    private LinearLayout llCameraControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_camera_kit);

//        select  begin
        themeId = R.style.picture_default_style;
      //  minus = (ImageView) findViewById(R.id.minus);
      //  plus = (ImageView) findViewById(R.id.plus);
      //  tv_select_num = (TextView) findViewById(R.id.tv_select_num);
        rgb_crop = (RadioGroup) findViewById(R.id.rgb_crop);
        rgb_style = (RadioGroup) findViewById(R.id.rgb_style);
        rgb_photo_mode = (RadioGroup) findViewById(R.id.rgb_photo_mode);
        cb_voice = (CheckBox) findViewById(R.id.cb_voice);
        cb_choose_mode = (CheckBox) findViewById(R.id.cb_choose_mode);
        cb_isCamera = (CheckBox) findViewById(R.id.cb_isCamera);
        cb_isGif = (CheckBox) findViewById(R.id.cb_isGif);
        cb_preview_img = (CheckBox) findViewById(R.id.cb_preview_img);
        cb_preview_video = (CheckBox) findViewById(R.id.cb_preview_video);
        cb_crop = (CheckBox) findViewById(R.id.cb_crop);
        cb_styleCrop = (CheckBox) findViewById(R.id.cb_styleCrop);
        cb_compress = (CheckBox) findViewById(R.id.cb_compress);
        cb_mode = (CheckBox) findViewById(R.id.cb_mode);
        cb_showCropGrid = (CheckBox) findViewById(R.id.cb_showCropGrid);
        cb_showCropFrame = (CheckBox) findViewById(R.id.cb_showCropFrame);
        cb_preview_audio = (CheckBox) findViewById(R.id.cb_preview_audio);
        cb_hide = (CheckBox) findViewById(R.id.cb_hide);
        cb_crop_circular = (CheckBox) findViewById(R.id.cb_crop_circular);
        rgb_crop.setOnCheckedChangeListener(this);
        rgb_style.setOnCheckedChangeListener(this);
        rgb_photo_mode.setOnCheckedChangeListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        left_back = (ImageView) findViewById(R.id.left_back);
        left_back.setOnClickListener(this);
//        minus.setOnClickListener(this);
//        plus.setOnClickListener(this);
        cb_crop.setOnCheckedChangeListener(this);
        cb_crop_circular.setOnCheckedChangeListener(this);
        cb_compress.setOnCheckedChangeListener(this);


//        /select end


        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE);

        camera = findViewById(R.id.camera);
        llCameraControl = findViewById(R.id.ll_camera_control);
        // 设置宽高
//        ViewGroup.LayoutParams  layoutParams = new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        camera.setLayoutParams(layoutParams);

        findViewById(R.id.edit).setOnClickListener(this);
        findViewById(R.id.capturePhoto).setOnClickListener(this);
        findViewById(R.id.captureVideo).setOnClickListener(this);
        findViewById(R.id.toggleCamera).setOnClickListener(this);


    }


    /**
     * message 方法是风中的toast，根据重要不重要，决定显示的时间长短
     */
    private void message(String content, boolean important) {
        int length = important ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(this, content, length).show();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit:
                edit();
                break;
            case R.id.capturePhoto:
                Log.i(TAG,"onClick begin:" + System.currentTimeMillis()) ;
                capturePhoto();
                break;
            case R.id.toggleCamera:
//                toggleCamera();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void edit() {
//        BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
//        b.setState(BottomSheetBehavior.STATE_COLLAPSED);
        boolean mode = cb_mode.isChecked();
        if (mode) {
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(CameraActivityKit.this)
                    .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(cb_choose_mode.isChecked() ?
                            PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选
                    .previewImage(cb_preview_img.isChecked())// 是否可预览图片
                    .previewVideo(cb_preview_video.isChecked())// 是否可预览视频
                    .enablePreviewAudio(cb_preview_audio.isChecked()) // 是否可播放音频
                    .isCamera(cb_isCamera.isChecked())// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                    .enableCrop(true)// 是否裁剪
                    .compress(cb_compress.isChecked())// 是否压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    //.compressSavePath(getPath())//压缩图片保存地址
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
                    .isGif(cb_isGif.isChecked())// 是否显示gif图片
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
                    .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(cb_voice.isChecked())// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled(true) // 裁剪是否可旋转图片
                    //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()//显示多少秒以内的视频or音频也可适用
                    //.recordVideoSecond()//录制视频秒数 默认60s
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    }

    /**
     * 拍照按钮调用的方法
     */
    private void capturePhoto() {
        Log.i(TAG,"capturePhoto begin:" + System.currentTimeMillis()) ;
        mSkipTime = System.currentTimeMillis();
        camera.captureImage(new CameraKitEventCallback<CameraKitImage>() {
            @Override
            public void callback(CameraKitImage event) {
                Log.i(TAG,"callback begin:" + System.currentTimeMillis()) ;
                imageCaptured(event);
            }
        });
        Log.i(TAG,"capturePhoto end:" + System.currentTimeMillis()) ;
    }

    public void imageCaptured(CameraKitImage image) {
        Log.i(TAG,"imageCaptured begin:" + System.currentTimeMillis()) ;
        byte[] jpeg = image.getJpeg();
        File file = new File(getExternalFilesDir("photos"), "photo.jpg");
        createFileWithByte(jpeg, file);
        Uri uri = Uri.fromFile(file);
        MyUCrop uCrop = MyUCrop.of(uri, Uri.fromFile(new File(getCacheDir(), "20180501")));
//        uCrop = basisConfig(uCrop);
//        uCrop = advancedConfig(uCrop);
        uCrop.withAspectRatio(300, 300);
        // setupFragment(uCrop);
        Intent intent = new Intent(CameraActivityKit.this, MyUcropActivity.class);
        uCrop.setCropIntent(intent);
        uCrop.start(this, REQUEST_CODE_CAMERA);
        Log.i(TAG,"imageCaptured end:" + System.currentTimeMillis()) ;
    }





    //region Boilerplate

    @Override
    protected void onResume() {
        super.onResume();
        camera.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        camera.destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean valid = true;
        for (int grantResult : grantResults) {
            valid = valid && grantResult == PackageManager.PERMISSION_GRANTED;
        }
        if (valid && !camera.isStarted()) {
            camera.start();
        }
    }

    //endregion
    /**
     * 根据byte数组生成文件
     *
     * @param bytes 生成文件用到的byte数组
     */
    private void createFileWithByte(byte[] bytes, File file) {
        // TODO Auto-generated method stub
        /**
         * 创建File对象，其中包含文件所在的目录以及文件的命名
         */
//        File file = new File(Environment.getExternalStorageDirectory(),
//                fileName);

        // 创建FileOutputStream对象
        FileOutputStream outputStream = null;
        // 创建BufferedOutputStream对象
        BufferedOutputStream bufferedOutputStream = null;
        try {
            // 如果文件存在则删除
            if (file.exists()) {
                file.delete();
            }
            // 在文件系统中根据路径创建一个新的空文件
            file.createNewFile();
            // 获取FileOutputStream对象
            outputStream = new FileOutputStream(file);
            // 获取BufferedOutputStream对象
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            // 往文件所在的缓冲输出流中写byte数据
            bufferedOutputStream.write(bytes);
            // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
            bufferedOutputStream.flush();
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
        } finally {
            // 关闭创建的流对象
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:  // 从相册选择
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getPath());
                    }
                    LocalMedia media = selectList.get(0);
//                    String strCut = media.getCutPath();
                    Intent intent = new Intent(this,ResultActivity.class);
                    intent.putExtra("Select_media",media);

                    startActivity(intent);
                    break;

                case REQUEST_CODE_CAMERA:  // 从相机
                    //intent.putExtra("com.yalantis.ucrop.OutputUri", uri).putExtra("com.yalantis.ucrop.CropAspectRatio", resultAspectRatio).putExtra("com.yalantis.ucrop.ImageWidth", imageWidth).putExtra("com.yalantis.ucrop.ImageHeight", imageHeight).putExtra("com.yalantis.ucrop.OffsetX", offsetX).putExtra("com.yalantis.ucrop.OffsetY", offsetY);
                    final Uri resultUri = data.getParcelableExtra(MyUCrop.EXTRA_OUTPUT_URI);

                    String cutPath = resultUri.getPath();
                    Intent intent2 = new Intent(this, ResultActivity.class);
                    intent2.setData(resultUri);
                    startActivity(intent2);
                    break;
                default:
                    break;


            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
}
