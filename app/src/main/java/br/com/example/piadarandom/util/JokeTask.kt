package br.com.example.piadarandom.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import br.com.example.piadarandom.model.Joke
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class JokeTask(private val callback: Callback) {

	private val handler = Handler(Looper.getMainLooper())
	private val executor = Executors.newSingleThreadExecutor()
	private lateinit var stream: InputStream

	interface Callback{
		fun onResult(joke: Joke)
		fun onError(error: IOException)
	}

	fun execute(url: String){

		executor.execute{
				try {

					val requestUrl = URL(url)
					val urlConnection = requestUrl.openConnection() as HttpURLConnection

					urlConnection.apply {
						readTimeout = 2000
						connectTimeout = 4000
					}

					val statusCode: Int = urlConnection.responseCode

					if(statusCode > 400){
						throw IOException("Connection Error")
					}

					stream = urlConnection.inputStream
					val jsonAsString = stream.bufferedReader().use { it.readText() }

					val joke = toJoke(jsonAsString)

					handler.post {
						callback.onResult(joke)
					}

				}catch (e: IOException){
					handler.post {
						callback.onError(e)
					}
				}finally {
					stream.close()
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