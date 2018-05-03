package seachal.com.cameraphotoframe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * *
 * *
 * Project_Name:CameraView-1
 *
 * @author zhangxc
 * @date 2018/4/18 下午7:48
 * *
 */

public class MainActivity extends Activity {

    private Button mButton1;
    private Button mButton2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton1 = findViewById(R.id.bt_cameraview);
        mButton2 = findViewById(R.id.bt_camerakit);

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraActivityKit.class);
                startActivity(intent);
            }
        });
    }


}
