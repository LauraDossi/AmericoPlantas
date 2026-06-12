package model;

public class Cliente extends Pessoa {
    private int id;
    private String dataNascimento;

    public Cliente(String nome, String cpfCnpj, String telefone, String email, String cep,
                   String endereco, String numero, String dataNascimento) {
        super(nome, cpfCnpj, telefone, email, endereco, cep, numero);
        this.dataNascimento = dataNascimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}

