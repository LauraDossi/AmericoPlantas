package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ProdutoDAO {
    public boolean inserir(Produto produto) {
        String sql = "INSERT INTO produto (nome, tipo, estoque, preco, estado, dataFabricacao, dataValidade) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getTipo());
            stmt.setInt(3, produto.getEstoque());
            stmt.setDouble(4, produto.getPreco());
            stmt.setString(5, produto.getStatus());
            stmt.setDate(6, toSqlDate(produto.getDataFabricacao()));
            stmt.setDate(7, toSqlDate(produto.getDataValidade()));
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Produto> listar() {
        ArrayList<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getInt("estoque"),
                        rs.getDouble("preco"),
                        rs.getString("estado"),
                        rs.getDate("dataFabricacao") != null ? rs.getDate("dataFabricacao").toLocalDate() : null,
                        rs.getDate("dataValidade") != null ? rs.getDate("dataValidade").toLocalDate() : null
                );
                produto.setId(rs.getInt("id_produto"));
                produtos.add(produto);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return produtos;
    }

    public boolean editar(Produto produto) {
        String sql = "UPDATE produto SET nome = ?, tipo = ?, estoque = ?, preco = ?, estado = ?, dataFabricacao = ?, dataValidade = ? " +
                "WHERE id_produto = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getTipo());
            stmt.setInt(3, produto.getEstoque());
            stmt.setDouble(4, produto.getPreco());
            stmt.setString(5, produto.getStatus());
            stmt.setDate(6, toSqlDate(produto.getDataFabricacao()));
            stmt.setDate(7, toSqlDate(produto.getDataValidade()));
            stmt.setInt(8, produto.getId());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM produto WHERE id_produto = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Produto> pesquisar(String termo) {
        ArrayList<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE nome LIKE ? OR tipo LIKE ? OR estado LIKE ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String busca = "%" + termo + "%";
            stmt.setString(1, busca);
            stmt.setString(2, busca);
            stmt.setString(3, busca);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getInt("estoque"),
                        rs.getDouble("preco"),
                        rs.getString("estado"),
                        rs.getDate("dataFabricacao") != null ? rs.getDate("dataFabricacao").toLocalDate() : null,
                        rs.getDate("dataValidade") != null ? rs.getDate("dataValidade").toLocalDate() : null
                );
                produto.setId(rs.getInt("id_produto"));
                produtos.add(produto);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return produtos;
    }

    public ArrayList<Produto> listarPorPeriodo(String dataInicio, String dataFim) {
        ArrayList<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE dataValidade BETWEEN ? AND ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dataInicio);
            stmt.setString(2, dataFim);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getInt("estoque"),
                        rs.getDouble("preco"),
                        rs.getString("estado"),
                        rs.getDate("dataFabricacao") != null ? rs.getDate("dataFabricacao").toLocalDate() : null,
                        rs.getDate("dataValidade") != null ? rs.getDate("dataValidade").toLocalDate() : null
                );
                produto.setId(rs.getInt("id_produto"));
                produtos.add(produto);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return produtos;
    }

    public boolean atualizarEstoque(int idProduto, int novoEstoque) {
        String sql = "UPDATE produto SET estoque = ? WHERE id_produto = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, novoEstoque);
            stmt.setInt(2, idProduto);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean atualizarEstoqueComConexao(Connection conn, int idProduto, int novoEstoque) throws Exception {
        String sql = "UPDATE produto SET estoque = ? WHERE id_produto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, novoEstoque);
            stmt.setInt(2, idProduto);
            return stmt.executeUpdate() > 0;
        }
    }

    private java.sql.Date toSqlDate(LocalDate date) {
        return date != null ? java.sql.Date.valueOf(date) : null;
    }
}
