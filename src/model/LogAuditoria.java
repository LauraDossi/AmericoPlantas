package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogAuditoria {
    private int id;
    private int usuarioId;
    private String usuarioNome;
    private String acao;
    private String entidade;
    private int entidadeId;
    private String detalhes;
    private LocalDateTime dataHora;

    public LogAuditoria() {}

    public LogAuditoria(int usuarioId, String usuarioNome, String acao,
                        String entidade, int entidadeId, String detalhes) {
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.acao = acao;
        this.entidade = entidade;
        this.entidadeId = entidadeId;
        this.detalhes = detalhes;
        this.dataHora = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }
    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }
    public String getEntidade() { return entidade; }
    public void setEntidade(String entidade) { this.entidade = entidade; }
    public int getEntidadeId() { return entidadeId; }
    public void setEntidadeId(int entidadeId) { this.entidadeId = entidadeId; }
    public String getDetalhes() { return detalhes; }
    public void setDetalhes(String detalhes) { this.detalhes = detalhes; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public String getDataHoraFormatada() {
        return dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}
