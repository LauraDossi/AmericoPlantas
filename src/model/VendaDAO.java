package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class VendaDAO {


    public int inserir(Venda venda) {
        String sql = "INSERT INTO venda (id_cliente, id_vendedor, forma_pagamento, data_venda, total) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, venda.getIdCliente());
            stmt.setInt(2, venda.getIdUsuario());   // Note: o objeto Venda chama getIdUsuario(), mas a coluna é id_vendedor
            stmt.setString(3, venda.getFormaDePagamento());
            stmt.setString(4, venda.getData());
            stmt.setDouble(5, venda.getTotal());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public boolean editar(Venda venda) {
        String sql = "UPDATE venda SET id_cliente = ?, id_vendedor = ?, forma_pagamento = ?, data_venda = ?, total = ? WHERE id_venda = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, venda.getIdCliente());
            stmt.setInt(2, venda.getIdUsuario());
            stmt.setString(3, venda.getFormaDePagamento());
            stmt.setString(4, venda.getData());
            stmt.setDouble(5, venda.getTotal());
            stmt.setInt(6, venda.getId());

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM venda WHERE id_venda = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Venda> pesquisar(String termo) {
        String sql = "SELECT v.*, c.nome AS nome_cliente, u.nome AS nome_usuario FROM venda v " +
                "JOIN cliente c ON v.id_cliente = c.id_cliente " +
                "JOIN usuario u ON v.id_vendedor = u.id_usuario " +
                "WHERE c.nome LIKE ? OR u.nome LIKE ? OR v.forma_pagamento LIKE ?";

        ArrayList<Venda> vendas = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String filtro = "%" + termo + "%";
            stmt.setString(1, filtro);
            stmt.setString(2, filtro);
            stmt.setString(3, filtro);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vendas.add(mapear(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return vendas;
    }

    public ArrayList<Venda> listar() {
        String sql = "SELECT v.*, c.nome AS nome_cliente, u.nome AS nome_usuario FROM venda v " +
                "JOIN cliente c ON v.id_cliente = c.id_cliente " +
                "JOIN usuario u ON v.id_vendedor = u.id_usuario";

        ArrayList<Venda> vendas = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vendas.add(mapear(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return vendas;
    }

    public ArrayList<Venda> listarPorPeriodo(String dataInicio, String dataFim) {
        String sql = "SELECT v.*, c.nome AS nome_cliente, u.nome AS nome_usuario FROM venda v " +
                "JOIN cliente c ON v.id_cliente = c.id_cliente " +
                "JOIN usuario u ON v.id_vendedor = u.id_usuario " +
                "WHERE DATE(v.data_venda) BETWEEN ? AND ?";

        ArrayList<Venda> vendas = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dataInicio);
            stmt.setString(2, dataFim);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vendas.add(mapear(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return vendas;
    }

    private Venda mapear(ResultSet rs) throws Exception {
        Venda venda = new Venda();
        venda.setId(rs.getInt("id_venda"));
        venda.setIdCliente(rs.getInt("id_cliente"));
        venda.setIdUsuario(rs.getInt("id_vendedor"));
        venda.setNomeCliente(rs.getString("nome_cliente"));
        venda.setNomeUsuario(rs.getString("nome_usuario"));
        venda.setFormaDePagamento(rs.getString("forma_pagamento"));
        venda.setData(rs.getString("data_venda"));
        venda.setTotal(rs.getDouble("total"));
        return venda;
    }

    public int inserirComConexao(Connection conn, Venda venda) throws Exception {
        String sql = "INSERT INTO venda (id_cliente, id_vendedor, forma_pagamento, data_venda, total) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, venda.getIdCliente());
            stmt.setInt(2, venda.getIdUsuario());
            stmt.setString(3, venda.getFormaDePagamento());
            stmt.setString(4, venda.getData());
            stmt.setDouble(5, venda.getTotal());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        }
    }
}