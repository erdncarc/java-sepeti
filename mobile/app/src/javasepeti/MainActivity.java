package com.example.javasepeti;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, HomepageAnonymousActivity.class);
            startActivity(intent);
            finish();
        }, 1000);


        /*
        TextView nextButton = findViewById(R.id.next);

        // Tıklama dinleyicisi
        nextButton.setOnClickListener(v -> {
            // Ekrana yazı bastır (Logcat)
            Log.d("MainActivity", "Next butonuna tıklandı");

            // Kullanıcıya gösterilen mesaj
            Toast.makeText(this, "Next clicked!", Toast.LENGTH_SHORT).show();
        });*/

        /*
        confirm basket
        basket
        restaurant home page xxxxx
        edit product
        add product
        home








        <GridLayout
                android:id="@+id/food_grid"
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:columnCount="2"
                android:orientation="horizontal"
                android:padding="16dp">

                <!-- Yemek Kartı 1 -->
                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_margin="8dp"
                    android:layout_columnWeight="1"
                    android:layout_columnSpan="1"
                    android:layout_gravity="fill"
                    android:orientation="vertical"
                    android:background="@drawable/card_background"
                    android:padding="12dp">

                    <RelativeLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content">

                        <ImageView
                            android:layout_width="match_parent"
                            android:layout_height="100dp"
                            android:scaleType="centerCrop"
                            android:src="@drawable/food1"
                            android:contentDescription="@null" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="★ 4.9"
                            android:textColor="#F7941D"
                            android:textStyle="bold"
                            android:layout_alignParentEnd="true"
                            android:layout_alignParentTop="true"
                            android:background="@android:color/white"
                            android:padding="4dp" />
                    </RelativeLayout>

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Lamb Shank"
                        android:textStyle="bold"
                        android:textSize="16sp"
                        android:layout_marginTop="8dp" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="$ 15.4"
                        android:textColor="#000"
                        android:textSize="14sp" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="430 cal\nA, F, G, L, M"
                        android:textSize="12sp"
                        android:textColor="#555" />
                </LinearLayout>

                <!-- Yemek Kartı 2 -->
                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_margin="8dp"
                    android:layout_columnWeight="1"
                    android:layout_columnSpan="1"
                    android:layout_gravity="fill"
                    android:orientation="vertical"
                    android:background="@drawable/card_background"
                    android:padding="12dp">

                    <RelativeLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content">

                        <ImageView
                            android:layout_width="match_parent"
                            android:layout_height="100dp"
                            android:scaleType="centerCrop"
                            android:src="@drawable/food1"
                            android:contentDescription="@null" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="★ 4.5"
                            android:textColor="#F7941D"
                            android:textStyle="bold"
                            android:layout_alignParentEnd="true"
                            android:layout_alignParentTop="true"
                            android:background="@android:color/white"
                            android:padding="4dp" />
                    </RelativeLayout>

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Prime Rib"
                        android:textStyle="bold"
                        android:textSize="16sp"
                        android:layout_marginTop="8dp" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="$ 31.0"
                        android:textColor="#000"
                        android:textSize="14sp" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="850 cal\nG, M, O"
                        android:textSize="12sp"
                        android:textColor="#555" />
                </LinearLayout>


                <!-- Yemek Kartı 3 -->
                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_margin="8dp"
                    android:layout_columnWeight="1"
                    android:layout_columnSpan="1"
                    android:layout_gravity="fill"
                    android:orientation="vertical"
                    android:background="@drawable/card_background"
                    android:padding="12dp">

                    <RelativeLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content">

                        <ImageView
                            android:layout_width="match_parent"
                            android:layout_height="100dp"
                            android:scaleType="centerCrop"
                            android:src="@drawable/food1"
                            android:contentDescription="@null" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="★ 4.4"
                            android:textColor="#F7941D"
                            android:textStyle="bold"
                            android:layout_alignParentEnd="true"
                            android:layout_alignParentTop="true"
                            android:background="@android:color/white"
                            android:padding="4dp" />
                    </RelativeLayout>

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Teriyaki Rice"
                        android:textStyle="bold"
                        android:textSize="16sp"
                        android:layout_marginTop="8dp" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="$ 17.2"
                        android:textColor="#000"
                        android:textSize="14sp" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="540 cal\nA, F, E, O"
                        android:textSize="12sp"
                        android:textColor="#555" />
                </LinearLayout>

                <!-- Yemek Kartı 4 -->
                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_margin="8dp"
                    android:layout_columnWeight="1"
                    android:layout_columnSpan="1"
                    android:layout_gravity="fill"
                    android:orientation="vertical"
                    android:background="@drawable/card_background"
                    android:padding="12dp">

                    <RelativeLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content">

                        <ImageView
                            android:layout_width="match_parent"
                            android:layout_height="100dp"
                            android:scaleType="centerCrop"
                            android:src="@drawable/food1"
                            android:contentDescription="@null" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="★ 4.1"
                            android:textColor="#F7941D"
                            android:textStyle="bold"
                            android:layout_alignParentEnd="true"
                            android:layout_alignParentTop="true"
                            android:background="@android:color/white"
                            android:padding="4dp" />
                    </RelativeLayout>

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Mushroom Pasta"
                        android:textStyle="bold"
                        android:textSize="16sp"
                        android:layout_marginTop="8dp" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="$ 16.9"
                        android:textColor="#000"
                        android:textSize="14sp" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="340 cal\nA, C, G"
                        android:textSize="12sp"
                        android:textColor="#555" />
                </LinearLayout>

                <!-- Yemek Kartı 5 -->
                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_margin="8dp"
                    android:layout_columnWeight="1"
                    android:layout_columnSpan="1"
                    android:layout_gravity="fill"
                    android:orientation="vertical"
                    android:background="@drawable/card_background"
                    android:padding="12dp">

                    <RelativeLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content">

                        <ImageView
                            android:layout_width="match_parent"
                            android:layout_height="100dp"
                            android:scaleType="centerCrop"
                            android:src="@drawable/food1"
                            android:contentDescription="@null" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="★ 4.0"
                            android:textColor="#F7941D"
                            android:textStyle="bold"
                            android:layout_alignParentEnd="true"
                            android:layout_alignParentTop="true"
                            android:background="@android:color/white"
                            android:padding="4dp" />
                    </RelativeLayout>

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Steak Burger"
                        android:textStyle="bold"
                        android:textSize="16sp"
                        android:layout_marginTop="8dp" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="$ 19.9"
                        android:textColor="#000"
                        android:textSize="14sp" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="600 cal\nA, G, M"
                        android:textSize="12sp"
                        android:textColor="#555" />
                </LinearLayout>

                <!-- Yemek Kartı 6 -->
                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_margin="8dp"
                    android:layout_columnWeight="1"
                    android:layout_columnSpan="1"
                    android:layout_gravity="fill"
                    android:orientation="vertical"
                    android:background="@drawable/card_background"
                    android:padding="12dp">

                    <RelativeLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content">

                        <ImageView
                            android:layout_width="match_parent"
                            android:layout_height="100dp"
                            android:scaleType="centerCrop"
                            android:src="@drawable/food1"
                            android:contentDescription="@null" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="★ 4.3"
                            android:textColor="#F7941D"
                            android:textStyle="bold"
                            android:layout_alignParentEnd="true"
                            android:layout_alignParentTop="true"
                            android:background="@android:color/white"
                            android:padding="4dp" />
                    </RelativeLayout>

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="BBQ Chicken"
                        android:textStyle="bold"
                        android:textSize="16sp"
                        android:layout_marginTop="8dp" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="$ 18.5"
                        android:textColor="#000"
                        android:textSize="14sp" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="460 cal\nA, M"
                        android:textSize="12sp"
                        android:textColor="#555" />
                </LinearLayout>


            </GridLayout>
         */
















        /*

         */


    }
}
