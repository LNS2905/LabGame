package com.example.labgame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextView balance;
    EditText go1, go2, go3;
    CheckBox cb1, cb2, cb3;
    Button btnstart, btnaddBalance, btnReplay, btnHowToPlay;
    SeekBar seekBar1, seekBar2, seekBar3;

    MediaPlayer backgroundMusic, engineSound, engineStartSound;
    SoundPool soundPool;
    int accelerateSoundId, victorySoundId;

    boolean isRacing = false; // Biến cờ để theo dõi trạng thái cuộc đua

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHowToPlay = findViewById(R.id.btnHowToPlay);
        btnReplay = findViewById(R.id.btnReplay);
        btnaddBalance = findViewById(R.id.btnAddBalance);
        balance = findViewById(R.id.balance);
        balance.setText("1000$");
        go1 = findViewById(R.id.ed_go1);
        go2 = findViewById(R.id.ed_go2);
        go3 = findViewById(R.id.ed_go3);
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        btnstart = findViewById(R.id.start);
        seekBar1 = findViewById(R.id.seekBar1);
        seekBar2 = findViewById(R.id.seekBar2);
        seekBar3 = findViewById(R.id.seekBar3);

        onClickCheckBox();
        addTextWatchers();
        btnaddBalance.setVisibility(View.INVISIBLE);
        btnstart.setOnClickListener(view -> startGame());
        btnaddBalance.setOnClickListener(view -> setBalance());
        btnReplay.setOnClickListener(view -> setReplay());
        btnHowToPlay.setOnClickListener(view -> setHowToPlay());

        // Khởi tạo MediaPlayer cho nhạc nền
        backgroundMusic = MediaPlayer.create(this, R.raw.game_background);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();

        // Khởi tạo MediaPlayer cho engine-start
        engineStartSound = MediaPlayer.create(this, R.raw.engine_start);
        engineStartSound.setOnCompletionListener(mp -> {
            // Khi engine-start kết thúc, bắt đầu lặp lại engine.mp3
            engineSound = MediaPlayer.create(MainActivity.this, R.raw.engine);
            engineSound.setLooping(true);
            engineSound.start();
        });
        engineStartSound.start();

        // Khởi tạo SoundPool
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        // Load các hiệu ứng âm thanh
        accelerateSoundId = soundPool.load(this, R.raw.supercar_acceleration, 1);
        victorySoundId = soundPool.load(this, R.raw.victory, 1);

    }

    private void onClickCheckBox() {
        cb1.setOnClickListener(view -> {
            go1.setEnabled(cb1.isChecked());
            checkValidCash();
        });
        cb2.setOnClickListener(view -> {
            go2.setEnabled(cb2.isChecked());
            checkValidCash();
        });
        cb3.setOnClickListener(view -> {
            go3.setEnabled(cb3.isChecked());
            checkValidCash();
        });
    }

    private void    addTextWatchers() {
        go1.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                checkValidCash();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        go2.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                checkValidCash();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        go3.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                checkValidCash();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void checkValidCash() {
        int currentBalance = Integer.parseInt(balance.getText().toString().replace("$", ""));
        int totalCash = 0;

        if (cb1.isChecked()) {
            String value = go1.getText().toString();
            if (!value.isEmpty()) {
                totalCash += Integer.parseInt(value);
            }
        }
        if (cb2.isChecked()) {
            String value = go2.getText().toString();
            if (!value.isEmpty()) {
                totalCash += Integer.parseInt(value);
            }
        }
        if (cb3.isChecked()) {
            String value = go3.getText().toString();
            if (!value.isEmpty()) {
                totalCash += Integer.parseInt(value);
            }
        }

        if (totalCash > currentBalance) {
            btnstart.setEnabled(false);
            Toast.makeText(this, "Total cash cannot exceed balance", Toast.LENGTH_SHORT).show();
        } else {
            btnstart.setEnabled(true);
        }
    }

    private void showWinDialog(String winningCar, int prize) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.win_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    
        TextView tvWinMessage = dialog.findViewById(R.id.tv_win_message);
        TextView tvCarWin = dialog.findViewById(R.id.tv_car_win);
        TextView tvPrize = dialog.findViewById(R.id.tv_prize);
        Button btnCollect = dialog.findViewById(R.id.btn_collect);
        ImageView ivDialog = dialog.findViewById(R.id.iv_dialog);
    
        // Kiểm tra số tiền thưởng để hiển thị thông báo thắng/thua
        if (prize > 0) {
            tvWinMessage.setText("YOU WIN!");
            ivDialog.setImageResource(R.drawable.crown);
        } else {
            tvWinMessage.setText("YOU LOSE!");
            ivDialog.setImageResource(R.drawable.sad_face);
        }
    
        tvCarWin.setText(winningCar + " Wins!");
        tvPrize.setText("+$" + prize);
    
        btnCollect.setOnClickListener(v -> dialog.dismiss());
    
        dialog.show();
    }

    private void startGame() {
        seekBar1.setProgress(0);
        seekBar2.setProgress(0);
        seekBar3.setProgress(0);
        btnstart.setEnabled(false);
        btnReplay.setVisibility(View.INVISIBLE);

        isRacing = true; // Bắt đầu cuộc đua

        // Dừng engine.mp3 khi bắt đầu cuộc đua
        if (engineSound != null && engineSound.isPlaying()) {
            engineSound.stop();
            engineSound.release();
            engineSound = null;
        }

        // Phát tiếng tăng tốc lặp lại
        engineSound = MediaPlayer.create(MainActivity.this, R.raw.supercar_acceleration);
        engineSound.setLooping(true);
        engineSound.start();

        Random random = new Random();
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                int r1 = random.nextInt(3);
                int r2 = random.nextInt(3);
                int r3 = random.nextInt(3);

                runOnUiThread(() -> {
                    seekBar1.setProgress(seekBar1.getProgress() + r1);
                    seekBar2.setProgress(seekBar2.getProgress() + r2);
                    seekBar3.setProgress(seekBar3.getProgress() + r3);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Finish");

                    

                    if (seekBar1.getProgress() >= 100 || seekBar2.getProgress() >= 100 || seekBar3.getProgress() >= 100) {
                        timer.cancel();

                        isRacing = false; // Kết thúc cuộc đua

                        // Dừng tiếng tăng tốc
                        if (engineSound != null && engineSound.isPlaying()) {
                            engineSound.stop();
                            engineSound.release();
                            engineSound = null;
                        }

                        // Xác định xe chiến thắng và số tiền thưởng
                        String winningCar = "";
                        int prize = 0;
                        if (seekBar1.getProgress() >= 100) {
                            winningCar = "Car 1";
                            prize = cb1.isChecked() ? (int) (Integer.parseInt(go1.getText().toString()) * 0.85) : 0;
                        } else if (seekBar2.getProgress() >= 100) {
                            winningCar = "Car 2";
                            prize = cb2.isChecked() ? (int) (Integer.parseInt(go2.getText().toString()) * 0.85) : 0;
                        } else if (seekBar3.getProgress() >= 100) {
                            winningCar = "Car 3";
                            prize = cb3.isChecked() ? (int) (Integer.parseInt(go3.getText().toString()) * 0.85) : 0;
                        }
                        
                        // Hiển thị dialog chiến thắng
                        showWinDialog(winningCar, prize);

                        // Phát tiếng thắng
                        if ((seekBar1.getProgress() >= 100 && cb1.isChecked()) || 
                            (seekBar2.getProgress() >= 100 && cb2.isChecked()) ||
                            (seekBar3.getProgress() >= 100 && cb3.isChecked())) {
                            soundPool.play(victorySoundId, 1, 1, 0, 0, 1);
                        } 

                        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        calculatePoint();
                        builder.show();
                        btnstart.setEnabled(true);
                        btnstart.setVisibility(View.INVISIBLE);
                        btnReplay.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, 40, 40);
    }

    private void calculatePoint() {
        int currentBalance = Integer.parseInt(balance.getText().toString().replace("$", ""));

        if (cb1.isChecked()) {
            int n1 = Integer.parseInt(go1.getText().toString());
            if (seekBar1.getProgress() >= 100) {
                currentBalance += (int) (n1 * 0.85);
            } else {
                currentBalance -= n1;
            }
        }
        if (cb2.isChecked()) {
            int n2 = Integer.parseInt(go2.getText().toString());
            if (seekBar2.getProgress() >= 100) {
                currentBalance += (int) (n2 * 0.85);
            } else {
                currentBalance -= n2;
            }
        }
        if (cb3.isChecked()) {
            int n3 = Integer.parseInt(go3.getText().toString());
            if (seekBar3.getProgress() >= 100) {
                currentBalance += (int) (n3 * 0.85);
            } else {
                currentBalance -= n3;
            }
        }

        if (currentBalance <= 0) {
            btnaddBalance.setVisibility(View.VISIBLE);
        }
        balance.setText(currentBalance + "$");
    }

    private void setBalance() {
        int currentBalance = Integer.parseInt(balance.getText().toString().replace("$", ""));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Balance");
        builder.setMessage("Do you want to add 1000$ to your balance?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Your balance has been added 1000$", Toast.LENGTH_SHORT).show();
                balance.setText("1000$");
                btnaddBalance.setVisibility(View.INVISIBLE);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void setReplay() {
        seekBar1.setProgress(0);
        seekBar2.setProgress(0);
        seekBar3.setProgress(0);
        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);
        go1.setEnabled(false);
        go2.setEnabled(false);
        go3.setEnabled(false);
        go1.setText("0");
        go2.setText("0");
        go3.setText("0");
        btnReplay.setVisibility(View.INVISIBLE);
        btnstart.setVisibility(View.       VISIBLE);

        // Bắt đầu lại âm thanh động cơ
        if (engineSound != null && engineSound.isPlaying()) {
            engineSound.stop();
            engineSound.release();
        }
        engineSound = MediaPlayer.create(MainActivity.this, R.raw.engine);
        engineSound.setLooping(true);
        engineSound.start();
    }

    private void setHowToPlay(){
        btnHowToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,HowToPlayActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();

        // Tạm dừng nhạc nền và tiếng động cơ
        backgroundMusic.pause();
        if (engineSound != null && engineSound.isPlaying()) {
            engineSound.pause();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Tiếp tục nhạc nền và tiếng động cơ
        backgroundMusic.start();
        if (engineSound != null && !engineSound.isPlaying() && isRacing) {
            engineSound.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Giải phóng MediaPlayer khi Activity bị destroy
        backgroundMusic.release();
        if (engineSound != null) {
            engineSound.release();
        }
        soundPool.release();
    }
}


