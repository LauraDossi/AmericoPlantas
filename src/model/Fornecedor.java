package model;

public class Fornecedor extends Pessoa{

    private int id;
    private String tiposProdutos;

    public Fornecedor(String nome, String cpfCnpj, String telefone, String email, String endereco, String cep, String numero) {
        super(nome, cpfCnpj, telefone, email, endereco, cep, numero);
        this.tiposProdutos = "";
    }

    public Fornecedor(String nome, String cpfCnpj, String telefone, String email, String endereco, String cep, String numero, String tiposProdutos) {
        super(nome, cpfCnpj, telefone, email, endereco, cep, numero);
        this.tiposProdutos = tiposProdutos;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTiposProdutos() { return tiposProdutos; }
    public void setTiposProdutos(String tiposProdutos) { this.tiposProdutos = tiposProdutos; }
}