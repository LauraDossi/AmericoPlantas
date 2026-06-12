package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class RelatorioDAO {

    public boolean salvar(String tipo, String dataInicio, String dataFim, String conteudo) {
        String sql = "INSERT INTO relatorio (tipo, data_inicio, data_fim, conteudo) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo);
            stmt.setString(2, dataInicio);
            stmt.setString(3, dataFim);
            stmt.setString(4, conteudo);

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String[]> listar() {
        ArrayList<String[]> relatorios = new ArrayList<>();
        String sql = "SELECT id_relatorio, tipo, data_inicio, data_fim, data_geracao FROM relatorio ORDER BY data_geracao DESC";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                relatorios.add(new String[]{
                        String.valueOf(rs.getInt("id_relatorio")),
                        rs.getString("tipo"),
                        rs.getString("data_inicio"),
                        rs.getString("data_fim"),
                        rs.getString("data_geracao")
                });
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return relatorios;
    }

    public String buscarConteudo(int id) {
        String sql = "SELECT conteudo FROM relatorio WHERE id_relatorio = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("conteudo");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
