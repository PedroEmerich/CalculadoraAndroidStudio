package com.example.calculadora.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.calculadora.R;
import com.example.calculadora.model.Calculo;
import com.example.calculadora.network.CalcApiService;
import com.example.calculadora.network.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalculadoraFragment extends Fragment {
    private TextInputLayout tilValorA, tilValorB;
    private TextInputEditText etValorA, etValorB;
    private TextView tvResultado;
    private final String API_KEY = "PDA1639";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calculadora, container, false);

        tilValorA = v.findViewById(R.id.tilValorA);
        tilValorB = v.findViewById(R.id.tilValorB);
        etValorA = v.findViewById(R.id.etValorA);
        etValorB = v.findViewById(R.id.etValorB);
        tvResultado = v.findViewById(R.id.tvResultado);

        v.findViewById(R.id.btnSomar).setOnClickListener(view -> executarOperacao("+"));
        v.findViewById(R.id.btnSubtrair).setOnClickListener(view -> executarOperacao("-"));
        v.findViewById(R.id.btnMultiplicar).setOnClickListener(view -> executarOperacao("*"));
        v.findViewById(R.id.btnDividir).setOnClickListener(view -> executarOperacao("/"));

        return v;
    }

    private void executarOperacao(String op) {
        if (!validar()) return;

        double a = Double.parseDouble(etValorA.getText().toString());
        double b = Double.parseDouble(etValorB.getText().toString());
        double res = 0;

        switch (op) {
            case "+": res = a + b; break;
            case "-": res = a - b; break;
            case "*": res = a * b; break;
            case "/":
                if (b == 0) {
                    tilValorB.setError("Divisão por zero!");
                    return;
                }
                res = a / b;
                break;
        }

        tvResultado.setText(String.format(Locale.US, "Resultado: %.2f", res));
        enviarParaAPI(a, b, res, op);
    }

    private boolean validar() {
        boolean ok = true;
        tilValorA.setError(null);
        tilValorB.setError(null);

        if (TextUtils.isEmpty(etValorA.getText())) {
            tilValorA.setError("Informe o Valor A");
            ok = false;
        }
        if (TextUtils.isEmpty(etValorB.getText())) {
            tilValorB.setError("Informe o Valor B");
            ok = false;
        }
        return ok;
    }

    private void enviarParaAPI(double a, double b, double res, String op) {
        String data = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(new Date());
        Calculo calculo = new Calculo(a, b, res, op, data);

        CalcApiService service = RetrofitClient.getClient().create(CalcApiService.class);
        service.salvarCalculo(API_KEY, calculo).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Dados armazenados com sucesso, ID de Armazenamento " + response.body(), Toast.LENGTH_LONG).show();
                    limparCampos();
                } else {
                    Toast.makeText(getContext(), "Erro ao salvar: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getContext(), "Falha de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limparCampos() {
        etValorA.setText("");
        etValorB.setText("");
        etValorA.requestFocus();
    }
}