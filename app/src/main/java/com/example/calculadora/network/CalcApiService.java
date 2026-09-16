package com.example.calculadora.network;

import com.example.calculadora.model.Calculo;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CalcApiService {
    @POST("api/Calculadora")
    Call<Integer> salvarCalculo(@Query("apikey") String apiKey, @Body Calculo calculo);

    @GET("api/Calculadora")
    Call<List<Calculo>> listarCalculos(@Query("apikey") String apiKey);

    @DELETE("api/Calculadora")
    Call<Void> excluirCalculo(@Query("apikey") String apiKey, @Query("id") int id);
}