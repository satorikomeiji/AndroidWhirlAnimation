package com.example.testapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     *
     *
     */
    class WhirlView extends View {
        private Double FPS = 0.0;
        private Double FPSAVG = 0.0;
        static public final int DEFAULT_COLOR_COUNT = 4;
        private int GRID_WIDTH;
        private int GRID_HEIGHT;
        private int COLORS;
        static public final int DEFAULT_WIDTH = 10;
        static public final int DEFAULT_HEIGHT = 10;
        private int[][] arr_front;
        private int[][] arr_back;
        private int[] color_buffer;
        private long current_time;
        private long starttime;
        private long frames = 0;

        public WhirlView(Context context, int width, int height, int colors) {
            super(context);
            current_time = SystemClock.uptimeMillis();
            starttime = current_time;
            GRID_WIDTH = width;
            GRID_HEIGHT = height;
            COLORS = colors;
            arr_front = new int[width][height];
            color_buffer = new int[colors];
            Random x = new Random();
            for (int i = 0; i < colors; i++) {
                color_buffer[i] = x.nextInt();
            }
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    arr_front[i][j] = x.nextInt(colors);
                }
            }
            arr_back = arr_front.clone();
        }
        public WhirlView(Context context) {
            this(context, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_COLOR_COUNT);
        }
        @Override
        public void onDraw(Canvas canvas) {
            frames++;
            new Thread(new Runnable() {
                public void run() {
                    draw_back();
                }
            }).start();





            Paint p = new Paint();
            for (int i = 0; i < GRID_HEIGHT; i++) {
                for (int j = 0; j < GRID_WIDTH; j++) {
                    p.setColor(color_buffer[arr_front[i][j]]);
                    canvas.drawRect(10 + i * 80, 10 + j * 80, 90 + i * 80, 90 + j * 80, p);

                }
            }
            p.setColor(Color.WHITE);
            p.setTextSize(40);
            canvas.drawText("FPS:" + FPS.toString(), 120, 120, p);
            canvas.drawText("FPSAVG:" + FPSAVG.toString(), 120, 240, p);
            long now = SystemClock.uptimeMillis();
            long delta = now - current_time;
            FPS = (1.0 / delta) * 1000.0;
            current_time = now;
            long deltaavg = now - starttime;
            FPSAVG = ((double)frames / deltaavg) * 1000.0;



        }
        private void draw_back() {
            for (int i = 0; i < GRID_HEIGHT; i++) {
                for (int j = 0; j < GRID_WIDTH; j++) {
                    int t = (arr_back[i][j] + 1) % COLORS;
                    int r1 = i == 0 ? GRID_HEIGHT - 1 : i - 1;
                    int r2 = i == GRID_HEIGHT - 1 ? 0 : i + 1;
                    int c1 = j == 0 ? GRID_WIDTH -1 : j - 1;
                    int c2 = j == GRID_WIDTH - 1 ? 0 : j + 1;
                    if (arr_back[r1][c1] == t || arr_back[r2][c2] == t || arr_back[r1][c2] == t || arr_back[r2][c1] == t
                            || arr_back[r1][j] == t || arr_back[r2][j] == t || arr_back [i][c1] == t || arr_back[i][c2] == t) {
                        arr_back[i][j] = t;
                    }
                }
            }
            arr_front = arr_back.clone();
            postInvalidate();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new WhirlView(this));
    }

}

