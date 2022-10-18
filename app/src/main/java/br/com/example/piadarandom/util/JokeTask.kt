package br.com.example.piadarandom.util

import android.os.Handler
import android.os.Looper
import br.com.example.piadarandom.model.Joke
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class JokeTask(private val callback: Callback) {

	private val handler = Handler(Looper.getMainLooper())
	private val executor = Executors.newSingleThreadExecutor()

	interface Callback{
		fun onResult(joke: Joke)
	}

	fun execute(url: String){

		executor.execute{
				try {

					val requestUrl = URL(url)
					val urlConnection = requestUrl.openConnection() as HttpURLConnection
					urlConnection.apply {
						readTimeout = 2000
						connectTimeout = 2000
					}

					val statusCode: Int = urlConnection.responseCode
					if(statusCode > 400){
						throw IOException("Erro de conexão")
					}

					val stream = urlConnection.inputStream
					val jsonAsString = stream.bufferedReader().use { it.readText() }

					val joke = toJoke(jsonAsString)

					handler.post {
						callback.onResult(joke)
					}

				}catch (e: IOException){

				}
		}
	}

	private fun toJoke(jsonString: String): Joke{

		val jsonRoot = JSONObject(jsonString)

		val question = jsonRoot.getString("question")
		val answer = jsonRoot.getString("answer")

		return Joke(question, answer)
	}

}