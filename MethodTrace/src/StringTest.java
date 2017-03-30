import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class StringTest {
		public static void main(String[]args){
			//InfoBean
			String path = "2939 ent  11493975 ........................com.jushi.trading.activity.BaseActivity.dispatchTouchEvent (Landroid/view/MotionEvent;)Z	BaseActivity.java";
			/*
			 * 2939 ent  11493975 ........................com.jushi.trading.activity.BaseActivity.dispatchTouchEvent (Landroid/view/MotionEvent;)Z	BaseActivity.java
2939 ent  11494456 .........................com.jushi.trading.activity.BaseActivity.closeKeyWords ()V	BaseActivity.java
2939 xit  11496461 .........................com.jushi.trading.activity.BaseActivity.closeKeyWords ()V	BaseActivity.java
2939 xit  11516492 ........................com.jushi.trading.activity.BaseActivity.dispatchTouchEvent (Landroid/view/MotionEvent;)Z	BaseActivity.java
2939 ent  11519573 ........................com.jushi.trading.activity.BaseActivity.dispatchTouchEvent (Landroid/view/MotionEvent;)Z	BaseActivity.java
2939 xit  11637271 ........................com.jushi.trading.activity.BaseActivity.dispatchTouchEvent (Landroid/view/MotionEvent;)Z	BaseActivity.java*/
			System.out.println(path.replaceAll("[.]{2,}+", ""));
		}
}
