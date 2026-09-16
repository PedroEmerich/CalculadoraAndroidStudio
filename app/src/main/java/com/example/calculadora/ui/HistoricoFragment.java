package com.example.calculadora.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.calculadora.R;
import com.example.calculadora.adapter.CalculoAdapter;
import com.example.calculadora.model.Calculo;
import com.example.calculadora.network.CalcApiService;
import com.example.calculadora.network.RetrofitClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoricoFragment extends Fragment {
    private RecyclerView rvHistorico;
    private CalculoAdapter adapter;
    private final String API_KEY = "PDA1639";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_historico, container, false);
        rvHistorico = v.findViewById(R.id.rvHistorico);
        rvHistorico.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new CalculoAdapter(new ArrayList<>(), this::excluirRegistro);
        rvHistorico.setAdapter(adapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarHistorico();
    }

    private void carregarHistorico() {
        CalcApiService service = RetrofitClient.getClient().create(CalcApiService.class);
        service.listarCalculos(API_KEY).enqueue(new Callback<List<Calculo>>() {
            @Override
            public void onResponse(Call<List<Calculo>> call, Response<List<Calculo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Calculo> lista = response.body();
                    Collections.sort(lista, (c1, c2) -> c1.getDataHora().compareTo(c2.getDataHora()));
                    adapter.setLista(lista);
                } else {
                    Toast.makeText(getContext(), "Erro ao carregar: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Calculo>> call, Throwable t) {
                Toast.makeText(getContext(), "Falha ao carregar: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void excluirRegistro(Calculo c) {
        CalcApiService service = RetrofitClient.getClient().create(CalcApiService.class);
        service.excluirCalculo(API_KEY, c.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Registro excluído com sucesso!", Toast.LENGTH_SHORT).show();
                    carregarHistorico();
                } else {
                    Toast.makeText(getContext(), "Erro ao excluir: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Falha ao excluir: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}