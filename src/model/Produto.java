package model;

import java.time.LocalDate;

public class Produto {

    private int id;
    private String nome;
    private String tipo;
    private int estoque;
    private double preco;
    private String status;
    private LocalDate dataFabricacao;
    private LocalDate dataValidade;


    public Produto(String nome, String tipo, int estoque, double preco, String status,
                   LocalDate dataFabricacao, LocalDate dataValidade) {
        this.nome = nome;
        this.tipo = tipo;
        this.estoque = estoque;
        this.preco = preco;
        this.status = status;
        this.dataFabricacao = dataFabricacao;
        this.dataValidade = dataValidade;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public int getEstoque() { return estoque; }
    public void setEstoque(int estoque) { this.estoque = estoque; }
    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getDataFabricacao() { return dataFabricacao; }
    public void setDataFabricacao(LocalDate dataFabricacao) { this.dataFabricacao = dataFabricacao; }
    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }
}
