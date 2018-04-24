//package seachal.com.cameraphotoframe;
//
//
//import android.content.Context;
//import android.support.annotation.Nullable;
//import android.util.AttributeSet;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
///**
// * 图片，视频预览时， 上滑 显示图片信息的view。
// */
//public class MessageView extends LinearLayout {
//
//    private TextView message;
//    private TextView title;
//
//    public MessageView(Context context) {
//        this(context, null);
//    }
//
//    public MessageView(Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public MessageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        setOrientation(VERTICAL);
//        inflate(context, R.layout.control_view, this);
//        ViewGroup content = findViewById(R.id.content);
//        inflate(context, R.layout.spinner_text, content);
//        title = findViewById(R.id.title);
//        message = (TextView) content.getChildAt(0);
//    }
//
//    public void setTitle(String title) {
//        this.title.setText(title);
//    }
//
//    public void setMessage(String message) {
//        this.message.setText(message);
//    }
//}
