package com.lp.sidebar_master.widget;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.lp.sidebar_master.R;


/**
 * 侧滑拼音
 */

public class SideBar extends View {

    /**
     * 触摸字母索引发生变化的回调接口
     */
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    /**
     * 侧边栏字母显示
     */
    public static String[] b =
            {
                    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
                    "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                    "W", "X", "Y", "Z", "#"
            };
    /**
     * 变量 choose 用来标示当前手指触摸的字母索引在 alphabet 数组中的下标
     */
    private int choose = -1;
    /**
     * 定义画笔
     */
    private Paint paint = new Paint();
    /**
     * 当手指在 SideBar 上滑动的时候，会有一个 TextView 来显示当前手指触摸的字母索引，
     */
    private TextView mTextDialog;

    /**
     * 为SideBar设置显示字母的TextView
     *
     * @param mTextDialog
     */
    public void setmTextDialog(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context) {
        super(context);
    }

    /**
     * 绘制列表控件的方法
     * 将要绘制的字母以从上到下的顺序绘制在一个指定区域
     * 如果是进行选中的字母就进行高亮显示
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取SideBar的高度
        int height = getHeight();
        // 获取SideBar的宽度
        int width = getWidth();
        // 获得每个字母索引的高度
        int singleHeight = height / b.length;

        //绘制每一个字母的索引
        for (int i = 0; i < b.length; i++) {
            paint.setColor(Color.rgb(33, 65, 98));//设置字母颜色
            paint.setTypeface(Typeface.DEFAULT_BOLD);//设置字体
            paint.setAntiAlias(true);//抗锯齿
            paint.setTextSize(20);//设置字体大小

            // 如果当前的手指触摸索引和字母索引相同，那么字体颜色进行区分
            if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff"));
                paint.setFakeBoldText(true);
            }

            /**
             * 绘制字体，需要制定绘制的x、y轴坐标
             * x轴坐标 = 控件宽度的一半 - 字体宽度的一半
             * y轴坐标 = singleHeight * i + singleHeight
             */
            float x = width / 2 - paint.measureText(b[i]) / 2;
            float y = singleHeight * i + singleHeight;
            canvas.drawText(b[i], x, y, paint);

            // 重置画笔，准备绘制下一个字母索引
            paint.reset();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // 触摸事件的代码
        final int action = event.getAction();
        //手指触摸点在屏幕的Y坐标
        final float y = event.getY();
        // 因为currentChoosenAlphabetIndex会不断发生变化，所以用一个变量存储起来
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener changedListener = onTouchingLetterChangedListener;
        // 比例 = 手指触摸点在屏幕的y轴坐标 / SideBar的高度
        // 触摸点的索引 = 比例 * 字母索引数组的长度
        final int letterPos = (int) (y / getHeight() * b.length);

        switch (action) {
            case MotionEvent.ACTION_UP:
                // 如果手指没有触摸屏幕，SideBar的背景颜色为默认，索引字母提示控件不可见
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                choose = -1;
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
                // 其他情况，比如滑动屏幕、点击屏幕等等，SideBar会改变背景颜色，索引字母提示控件可见，同时需要设置内容
                setBackgroundResource(R.drawable.bg_sidebar);
                // 不是同一个字母索引
                if (oldChoose != letterPos) {
                    // 如果触摸点没有超出控件范围
                    if (letterPos >= 0 && letterPos < b.length) {
                        if (changedListener != null)
                            changedListener.onTouchingLetterChanged(b[letterPos]);
                        if (mTextDialog != null) {
                            mTextDialog.setText(b[letterPos]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = letterPos;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener changedListener) {
        this.onTouchingLetterChangedListener = changedListener;
    }

    /**
     * 当手指触摸的字母索引发生变化时，调用该回调接口
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String str);
    }
}
