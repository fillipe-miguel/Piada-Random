package br.com.example.piadarandom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val btn: Button = findViewById(R.id.btn_makeJoke)

		btn.setOnClickListener {
			val i = Intent(this, JokeActivity::class.java)
			startActivity(i)
		}

	}
}