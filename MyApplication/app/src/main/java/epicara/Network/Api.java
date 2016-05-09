package epicara.Network;

import epicara.younility.model.ImageModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by nishant on 16-05-08.
 */
public interface Api {


    @Headers({"Authorization: Client-ID 0d3b867e5a368c2"})
    @GET("gallery/g/memes/{page}")
    Call<ImageModel> getMeme(
            @Path("page") int page);

    @Headers({"Authorization: Client-ID 0d3b867e5a368c2"})
    @GET("gallery/r/funny/{page}")
    Call<ImageModel> getFunny(
            @Path("page") int page);

}
