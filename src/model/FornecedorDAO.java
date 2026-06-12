package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class FornecedorDAO {
    public boolean inserir(Fornecedor fornecedor)  {
        String sql = "INSERT INTO fornecedor (nome, cpfCnpj, telefone, email, endereco, cep, numero, tiposDeProdutos)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getCpfCnpj());
            stmt.setString(3, fornecedor.getTelefone());
            stmt.setString(4, fornecedor.getEmail());
            stmt.setString(5, fornecedor.getEndereco());
            stmt.setString(6, fornecedor.getCep());
            stmt.setString(7, fornecedor.getNumero());
            stmt.setString(8, fornecedor.getTiposProdutos());

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean campoJaExiste(String campo, String valor) {
        String sql = "SELECT COUNT(*) FROM fornecedor WHERE " + campo + " = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, valor);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public ArrayList<Fornecedor> listar(){
        ArrayList<Fornecedor> fornecedores = new ArrayList<>();
        String sql = "SELECT * FROM fornecedor";

        try(Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            while (rs.next()){
                Fornecedor fornecedor = new Fornecedor(rs.getString("nome"),
                        rs.getString("cpfCnpj"), rs.getString("telefone"), rs.getString("email"),
                        rs.getString("endereco"), rs.getString("cep"),
                        rs.getString("numero"), rs.getString("tiposDeProdutos"));
                fornecedor.setId(rs.getInt("id_fornecedor"));

                fornecedores.add(fornecedor);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return fornecedores;
    }

    public boolean excluir(int id) {

        String sql = "DELETE FROM fornecedor WHERE id_fornecedor = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean editar(Fornecedor fornecedor) {

        String sql = "UPDATE fornecedor SET nome = ?, cpfCnpj = ?, telefone = ?, email = ?, endereco = ?, cep = ?, numero = ?, tiposDeProdutos = ?"+
                " WHERE id_fornecedor = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getCpfCnpj());
            stmt.setString(3, fornecedor.getTelefone());
            stmt.setString(4, fornecedor.getEmail());
            stmt.setString(5, fornecedor.getEndereco());
            stmt.setString(6, fornecedor.getCep());
            stmt.setString(7, fornecedor.getNumero());
            stmt.setString(8, fornecedor.getTiposProdutos());

            stmt.setInt(9, fornecedor.getId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Fornecedor> pesquisar(String termo) {

        ArrayList<Fornecedor> fornecedores = new ArrayList<>();

        String sql = "SELECT * FROM fornecedor WHERE nome LIKE ? OR cpfCnpj LIKE ? OR telefone LIKE ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String busca = "%" + termo + "%";

            stmt.setString(1, busca);
            stmt.setString(2, busca);
            stmt.setString(3, busca);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Fornecedor fornecedor = new Fornecedor(
                        rs.getString("nome"),
                        rs.getString("cpfCnpj"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("endereco"),
                        rs.getString("cep"),
                        rs.getString("numero"),
                        rs.getString("tiposDeProdutos")
                );

                fornecedor.setId(
                        rs.getInt("id_fornecedor")
                );

                fornecedores.add(fornecedor);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return fornecedores;
    }
}

