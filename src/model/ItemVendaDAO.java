package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ItemVendaDAO {

    public boolean inserir(ItemVenda item, int idVenda) {
        String sql = "INSERT INTO item_venda (id_venda, id_produto, quantidade, valorUnitario, subTotal) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVenda);
            stmt.setInt(2, item.getProduto().getId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getValorUnitario());
            stmt.setDouble(5, item.getSubTotal());

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean inserirComConexao(Connection conn, ItemVenda item, int idVenda) throws Exception {
        String sql = "INSERT INTO item_venda (id_venda, id_produto, quantidade, valorUnitario, subTotal) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVenda);
            stmt.setInt(2, item.getProduto().getId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getValorUnitario());
            stmt.setDouble(5, item.getSubTotal());

            stmt.executeUpdate();
            return true;
        }
    }

    public List<ItemVenda> listarPorVenda(int idVenda) {
        List<ItemVenda> itens = new ArrayList<>();
        String sql = "SELECT iv.*, p.nome, p.preco, p.tipo, p.estoque, p.estado, p.dataFabricacao, p.dataValidade " +
                "FROM item_venda iv JOIN produto p ON iv.id_produto = p.id_produto WHERE iv.id_venda = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVenda);
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

                ItemVenda item = new ItemVenda(
                        produto,
                        rs.getInt("quantidade"),
                        rs.getDouble("valorUnitario")
                );

                itens.add(item);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return itens;
    }
}