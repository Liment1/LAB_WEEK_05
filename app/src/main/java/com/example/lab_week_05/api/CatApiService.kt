import com.example.lab_week_05.model.ImageData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatApiService {
     @GET("images/search")
    fun getRandomImageWithBreed(
        @Query("has_breeds") hasBreeds: Int,
        @Query("limit") limit: Int
    ): Call<List<ImageData>>

    @GET("images/{image_id}")
    fun getImageDetails(
        @Path("image_id") id: String
    ): Call<ImageData> // Note: This returns a single ImageData, not a List
}
