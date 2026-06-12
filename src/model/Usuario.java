package model;

public class Usuario extends Pessoa {

    private int id;
    private String dataNascimento;
    private String tipoUsuario;
    private String login;
    private String senha;
    private String salt;
    private int tentativasFalhas;
    private String bloqueadoAte;

    public Usuario(String nome, String login, String senha, String tipoUsuario) {
        setNome(nome);
        this.login = login;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    public Usuario(String nome, String cpfCnpj, String dataNascimento, String endereco, String numero, String cep,
                   String telefone, String email, String login, String senha, String tipoUsuario) {
        super(nome, cpfCnpj, telefone, email, endereco, cep, numero);
        this.dataNascimento = dataNascimento;
        this.tipoUsuario = tipoUsuario;
        this.login = login;
        this.senha = senha;
    }

    // Getters e Setters (incluindo os novos)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }
    public int getTentativasFalhas() { return tentativasFalhas; }
    public void setTentativasFalhas(int tentativasFalhas) { this.tentativasFalhas = tentativasFalhas; }
    public String getBloqueadoAte() { return bloqueadoAte; }
    public void setBloqueadoAte(String bloqueadoAte) { this.bloqueadoAte = bloqueadoAte; }
}
