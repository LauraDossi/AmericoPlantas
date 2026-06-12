package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogAuditoriaDAO {

    public void registrar(LogAuditoria log) {
        String sql = "INSERT INTO log_auditoria (usuario_id, usuario_nome, acao, entidade, entidade_id, detalhes, data_hora) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, log.getUsuarioId());
            stmt.setString(2, log.getUsuarioNome());
            stmt.setString(3, log.getAcao());
            stmt.setString(4, log.getEntidade());
            stmt.setInt(5, log.getEntidadeId());
            stmt.setString(6, log.getDetalhes());
            stmt.setTimestamp(7, Timestamp.valueOf(log.getDataHora()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar log: " + e.getMessage(), e);
        }
    }

    public List<LogAuditoria> listar() {
        List<LogAuditoria> logs = new ArrayList<>();
        String sql = "SELECT * FROM log_auditoria ORDER BY data_hora DESC";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LogAuditoria log = new LogAuditoria();
                log.setId(rs.getInt("id_log"));
                log.setUsuarioId(rs.getInt("usuario_id"));
                log.setUsuarioNome(rs.getString("usuario_nome"));
                log.setAcao(rs.getString("acao"));
                log.setEntidade(rs.getString("entidade"));
                log.setEntidadeId(rs.getInt("entidade_id"));
                log.setDetalhes(rs.getString("detalhes"));
                log.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                logs.add(log);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar logs: " + e.getMessage(), e);
        }
        return logs;
    }
}