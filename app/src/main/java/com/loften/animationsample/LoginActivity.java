package com.loften.animationsample;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * 登录
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    private ImageView img_head;
    private TextView tv_login_title;
    private ImageView img_dialog;
    private Button bt_login;
    private Button bt_register;
    private RelativeLayout rl_login;
    private ScrollView sv_main;
    private Button bt_other;

    private AnimatorSet startAnimatorSet;
    private AnimatorSet hideAnimatorSet;
    private int startAnimatorTime = 1*1000;
    private int hideAnimatorTime = 1*1000;
    private MyRelativeLayout rl_all;

    private boolean isShow = false;//登录注册模块是否显示
    private float scaleSize;//左上角头像压缩比例


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }

    private void initView() {
        img_head = (ImageView) findViewById(R.id.img_head);
        tv_login_title = (TextView) findViewById(R.id.tv_login_title);
        img_dialog = (ImageView) findViewById(R.id.img_dialog);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_register = (Button) findViewById(R.id.bt_register);
        rl_login = (RelativeLayout) findViewById(R.id.rl_login);
        sv_main = (ScrollView) findViewById(R.id.sv_main);
        bt_other = (Button) findViewById(R.id.bt_other);

        bt_login.setOnClickListener(this);
        bt_register.setOnClickListener(this);
        bt_other.setOnClickListener(this);

        img_head.post(new Runnable() {
            @Override
            public void run() {
                if (!isShow) {
                    startAnimators();
                }
            }
        });
        rl_all = (MyRelativeLayout) findViewById(R.id.rl_all);
        rl_all.setOnSlideListener(new MyRelativeLayout.OnSlideListener() {
            @Override
            public boolean onShow() {
                if (!isShow && startAnimatorSet == null) {
                    startAnimators();
                }
                return isShow;
            }

            @Override
            public boolean onHide() {
                if (isShow && hideAnimatorSet == null) {
                    closeAnimators();
                }
                return isShow;
            }
        });

        sv_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 判断 scrollView 当前滚动位置在顶部
                if(sv_main.getScrollY() == 0 && isShow){
                    rl_all.requestDisallowInterceptTouchEvent(false);
                }else if(sv_main.getScrollY() != 0 && isShow){
                    rl_all.requestDisallowInterceptTouchEvent(true);
                }else if(sv_main.getScrollY() == 0 && !isShow){
                    rl_all.requestDisallowInterceptTouchEvent(false);
                }else if(sv_main.getScrollY() !=0 && !isShow){
                    rl_all.requestDisallowInterceptTouchEvent(true);
                }

                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                Toast.makeText(this, "login", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_register:
                Toast.makeText(this, "register", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_other:
                Toast.makeText(this, "other", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void onMyClick(View view){
        Toast.makeText(this, new Random().nextInt(100)+"", Toast.LENGTH_SHORT).show();
    }

    private void startAnimators() {
        if(hideAnimatorSet != null){
            hideAnimatorSet.cancel();
            hideAnimatorSet = null;
        }
        startAnimatorSet = new AnimatorSet();

        float curTranslationX = img_head.getTranslationX();
        float curTranslationY = img_head.getTranslationY();
        float sCurTranslationY = sv_main.getTranslationY();

        scaleSize = (float) (tv_login_title.getLeft() / 1.7 / img_head.getWidth());

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(img_head, "translationX",
                curTranslationX, tv_login_title.getLeft() - img_head.getWidth() * (scaleSize + 1)).setDuration(startAnimatorTime);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(img_head, "translationY",
                curTranslationY, tv_login_title.getTop() + tv_login_title.getHeight()).setDuration(startAnimatorTime);

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(img_head, "scaleX",
                1, scaleSize).setDuration(startAnimatorTime);

        ObjectAnimator animator4 = ObjectAnimator.ofFloat(img_head, "scaleY",
                1, scaleSize).setDuration(startAnimatorTime);

        ObjectAnimator animator5 = ObjectAnimator.ofFloat(sv_main, "translationY",
                sCurTranslationY, rl_login.getHeight()).setDuration(startAnimatorTime);

        ObjectAnimator animator6 = ObjectAnimator.ofFloat(img_dialog, "rotation",
                -30f, 0, 30f);
        animator6.setRepeatMode(ValueAnimator.REVERSE);
        animator6.setRepeatCount(ValueAnimator.INFINITE);

        startAnimatorSet.play(animator1)
                .with(animator2)
                .with(animator3)
                .with(animator4)
                .with(animator5)
                .before(animator6);

        startAnimatorSet.start();

        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isShow = true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    private void closeAnimators() {
        if(startAnimatorSet != null){
            startAnimatorSet.cancel();
            startAnimatorSet = null;
        }

        hideAnimatorSet = new AnimatorSet();

        float curTranslationX = img_head.getTranslationX();
        float curTranslationY = img_head.getTranslationY();
        float sCurTranslationY = sv_main.getTranslationY();

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(img_head, "translationX",
                curTranslationX, -curTranslationX+(tv_login_title.getLeft() - img_head.getWidth() * (scaleSize + 1))).setDuration(hideAnimatorTime);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(img_head, "translationY",
                curTranslationY, -curTranslationY+(tv_login_title.getTop() + tv_login_title.getHeight())).setDuration(hideAnimatorTime);

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(img_head, "scaleX",
                scaleSize, 1).setDuration(hideAnimatorTime);

        ObjectAnimator animator4 = ObjectAnimator.ofFloat(img_head, "scaleY",
                scaleSize, 1).setDuration(hideAnimatorTime);

        ObjectAnimator animator5 = ObjectAnimator.ofFloat(sv_main, "translationY",
                sCurTranslationY, -sCurTranslationY+rl_login.getHeight()).setDuration(hideAnimatorTime);

        hideAnimatorSet.play(animator1)
                .with(animator2)
                .with(animator3)
                .with(animator4)
                .with(animator5);

        hideAnimatorSet.start();

        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isShow = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(startAnimatorSet != null){
            startAnimatorSet.cancel();
            startAnimatorSet = null;
        }
        if(hideAnimatorSet != null){
            hideAnimatorSet.cancel();
            hideAnimatorSet = null;
        }
    }

}

