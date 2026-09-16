package com.example.calculadora.model;

import com.google.gson.annotations.SerializedName;

public class Calculo {
    @SerializedName("id")
    private Integer id;

    @SerializedName("valorA")
    private double valorA;

    @SerializedName("valorB")
    private double valorB;

    @SerializedName("resultado")
    private double resultado;

    @SerializedName("operacao")
    private String operacao;

    @SerializedName("dataCalculo")
    private String dataCalculo;

    public Calculo(double valorA, double valorB, double resultado, String operacao, String dataCalculo) {
        this.valorA = valorA;
        this.valorB = valorB;
        this.resultado = resultado;
        this.operacao = operacao;
        this.dataCalculo = dataCalculo;
    }

    public Integer getId() { return id; }
    public double getValorA() { return valorA; }
    public double getValorB() { return valorB; }
    public double getResultado() { return resultado; }
    public String getOperacao() { return operacao; }
    public String getDataHora() { return dataCalculo; }
}