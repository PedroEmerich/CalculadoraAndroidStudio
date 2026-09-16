package com.example.calculadora.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.calculadora.R;
import com.example.calculadora.model.Calculo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalculoAdapter extends RecyclerView.Adapter<CalculoAdapter.ViewHolder> {
    private List<Calculo> lista;
    private OnDeleteClickListener listener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Calculo calculo);
    }

    public CalculoAdapter(List<Calculo> lista, OnDeleteClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calculo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Calculo c = lista.get(position);
        holder.tvValA.setText(String.format(Locale.US, "%.2f", c.getValorA()));
        holder.tvOp.setText(c.getOperacao());
        holder.tvValB.setText(String.format(Locale.US, "%.2f", c.getValorB()));
        holder.tvRes.setText(String.format(Locale.US, "%.2f", c.getResultado()));
        holder.tvData.setText(formatarData(c.getDataHora()));
        holder.btnExcluir.setOnClickListener(v -> listener.onDeleteClick(c));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    private String formatarData(String dataString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
            Date date = inputFormat.parse(dataString);
            return outputFormat.format(date);
        } catch (Exception e) {
            return dataString;
        }
    }

    public void setLista(List<Calculo> novaLista) {
        this.lista = novaLista;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvValA, tvOp, tvValB, tvRes, tvData;
        ImageButton btnExcluir;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvValA = itemView.findViewById(R.id.tvValA);
            tvOp = itemView.findViewById(R.id.tvOp);
            tvValB = itemView.findViewById(R.id.tvValB);
            tvRes = itemView.findViewById(R.id.tvRes);
            tvData = itemView.findViewById(R.id.tvData);
            btnExcluir = itemView.findViewById(R.id.btnExcluir);
        }
    }
}