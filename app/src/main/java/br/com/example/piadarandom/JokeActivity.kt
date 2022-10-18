package br.com.example.piadarandom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import br.com.example.piadarandom.model.Joke
import br.com.example.piadarandom.util.JokeTask

class JokeActivity : AppCompatActivity(), JokeTask.Callback {

	private lateinit var tvQuestion: TextView
	private lateinit var tvAnswer: TextView
	private lateinit var progressBar: ProgressBar

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_joke)


		val toolbar: Toolbar = findViewById(R.id.joke_toolbar)
		setSupportActionBar(toolbar)
		supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_arrow_left_24)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		val btnMakeAnother: Button = findViewById(R.id.btn_makeJoke)
		tvQuestion = findViewById(R.id.tv_question)
		tvAnswer = findViewById(R.id.tv_answer)
		progressBar = findViewById(R.id.progress_circular)


		JokeTask(this).execute("https://api-charadas.herokuapp.com/puzzle?lang=ptbr")


		btnMakeAnother.setOnClickListener {
			progressBar.visibility = View.VISIBLE
			JokeTask(this).execute("https://api-charadas.herokuapp.com/puzzle?lang=ptbr")

		}

	}

	// Para retornar clicando na setinha
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == android.R.id.home) {
			finish()
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onResult(joke: Joke) {
		tvQuestion.text = joke.question
		tvAnswer.text = joke.answer
		progressBar.visibility = View.GONE
	}
}