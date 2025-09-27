package com.example.lab_week_05

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab_week_05.model.ImageData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : AppCompatActivity() {
    // This interceptor will log the raw network request and response
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }



    // Create an OkHttpClient that uses the interceptor
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }



    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private val catApiService by lazy {
        retrofit.create(CatApiService::class.java)
    }

    private val apiResponseView: TextView by lazy{
        findViewById(R.id.api_response)
    }

    private val imageResultView: ImageView by lazy {
        findViewById(R.id.image_result)
    }
    private val imageLoader: ImageLoader by lazy {
        GlideLoader(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getCatImageResponse()
    }

        private fun getCatImageResponse() {
            val searchCall = catApiService.getRandomImageWithBreed(1, 1)

            searchCall.enqueue(object : retrofit2.Callback<List<ImageData>> {
                override fun onFailure(call: Call<List<ImageData>>, t: Throwable) {
                    Log.e(MAIN_ACTIVITY, "Failed to get response", t)
                }
                override fun onResponse(call: Call<List<ImageData>>, response: Response<List<ImageData>>) {
                    if (response.isSuccessful) {
                        val firstImage = response.body()?.firstOrNull()
                        val imageId = firstImage?.id

                        if (imageId != null) {
                            imageLoader.loadImage(firstImage.imageUrl.orEmpty(), imageResultView)

                            val detailsCall = catApiService.getImageDetails(imageId)

                            detailsCall.enqueue(object : retrofit2.Callback<ImageData> {
                                override fun onFailure(call: Call<ImageData>, t: Throwable) {
                                    Log.e(MAIN_ACTIVITY, "Failed to get image details", t)
                                }

                                override fun onResponse(call: Call<ImageData>, response: Response<ImageData>) {
                                    if (response.isSuccessful) {
                                        val imageDetails = response.body()
                                        val breedsObj = imageDetails?.breeds

                                        var resultText = imageDetails?.imageUrl + "\n"

                                        if (!breedsObj.isNullOrEmpty()) {
                                            val breed = breedsObj.first()
                                            resultText += "Breed Name: ${breed.name}\n"
                                            resultText += "Temperament: ${breed.temperament}\n"
                                        } else {
                                            resultText += "Breed Name: Not available\n"
                                            resultText += "Temperament: Not available\n"
                                        }
                                        apiResponseView.text = resultText
                                    } else {
                                        Log.e(MAIN_ACTIVITY, "Failed to get details response")
                                    }
                                }
                            })
                        } else {
                            Log.d(MAIN_ACTIVITY, "Did not receive a valid image ID")
                        }
                    } else {
                        Log.e(MAIN_ACTIVITY, "Initial image search was not successful")
                    }
                }
            })
        }

    companion object{
        const val MAIN_ACTIVITY = "MAIN_ACTIVITY"
    }
}