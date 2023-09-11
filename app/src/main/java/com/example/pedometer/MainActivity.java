package com.example.pedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorAdditionalInfo;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public  class MainActivity extends AppCompatActivity implements SensorEventListener {

    public boolean active = true;
    private SensorManager sensorManager;//Объект для работы с датчиком
    private int count = 0;// кол-во шагов
    private TextView text;//Ссылка на TextView
    private long lastUpdate; //Время последнего изменения состояния датчика




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        text = findViewById(R.id.textView2); // находим текст
        text.setText(String.valueOf(count));//Создаем объект для работы с датчиком
        sensorManager =(SensorManager) getSystemService(SENSOR_SERVICE); // Регистрируем класс где будет реализован метод при изменении

        //Запускаем датчик.
        sensorManager.registerListener((SensorEventListener) this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        lastUpdate = System.currentTimeMillis(); // Устанавливаем последнее время обновления

    }

    @Override
    public void onResume () {
         super.onResume(); // подписываемся на действие
        sensorManager.registerListener((SensorEventListener) this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL);// запускаем датчик
    }

    @Override
    public void onPause() { // пауза
        super.onPause(); // подписываемся на действие
        sensorManager.unregisterListener((SensorEventListener) this );

    }
    
    public void  OnStoped(View view){
        active = !active; 
        if (!active){ // если неактивно
            Button button = findViewById(R.id.button); 
            button.setText("ВОЗОБНОВИТЬ"); // присваиваем текст
        }
        else {// если активно
            Button button = findViewById(R.id.button);// находим кнопку
            button.setText("ПАУЗА");// присваиваем текст
            
        }
    }

    public void onSensorChanged(SensorEvent event) {// функция изменения датчика
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {////////////////////
            float [] values = event.values;
            float x= values [0];
            float y= values [1];
            float z= values [2];

            float accelationSquareRoot=(x* x+ y* y+ z* z)
                    /(SensorManager.GRAVITY_EARTH* SensorManager.GRAVITY_EARTH);

            long actualTime = System.currentTimeMillis();

            if ( accelationSquareRoot>=2) // Если тряска сильная
            {
                if (actualTime - lastUpdate<200){

                    return;
                }
                lastUpdate = actualTime;

                count ++;
                text.setText(String.valueOf(count));
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }
}



