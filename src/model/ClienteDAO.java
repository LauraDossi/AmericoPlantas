package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ClienteDAO {

    public boolean inserir(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, cpfCnpj, telefone, email, cep, endereco, numero, data_nasc)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

             stmt.setString(1, cliente.getNome());

             String cpf = cliente.getCpfCnpj();
             if (cpf == null || cpf.trim().isEmpty()) {
                 stmt.setNull(2, java.sql.Types.VARCHAR);
             } else {
                stmt.setString(2, cpf);
             }

             stmt.setString(3, cliente.getTelefone());
             String email = cliente.getEmail();
             if (email == null || email.trim().isEmpty()) {
                 stmt.setNull(4, java.sql.Types.VARCHAR);
             } else {
                 stmt.setString(4, email);
             }
             stmt.setString(5, cliente.getCep());
             stmt.setString(6, cliente.getEndereco());
             stmt.setString(7, cliente.getNumero());

             if (cliente.getDataNascimento() != null && !cliente.getDataNascimento().trim().isEmpty()) {
                 LocalDate data = LocalDate.parse(cliente.getDataNascimento(),
                         DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                 stmt.setDate(8, java.sql.Date.valueOf(data));
             } else {
                 stmt.setNull(8, java.sql.Types.DATE);
             }

             stmt.executeUpdate();
             return true;

        } catch (Exception e) {
             throw new RuntimeException(e);
        }
    }

    public boolean campoJaExiste(String campo, String valor) {
        String sql = "SELECT COUNT(*) FROM cliente WHERE " + campo + " = ?";

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

    public ArrayList<Cliente> listar(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try(Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            while (rs.next()){
                Cliente cliente = new Cliente(rs.getString("nome"),
                        rs.getString("cpfCnpj"), rs.getString("telefone"), rs.getString("email"),
                        rs.getString("cep"), rs.getString("endereco"), rs.getString("numero"), formatarData(rs.getDate("data_nasc")));
                cliente.setId(rs.getInt("id_cliente"));
                clientes.add(cliente);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return clientes;
    }

    public boolean editar(Cliente cliente) {

        String sql = "UPDATE cliente SET nome = ?, cpfCnpj = ?, telefone = ?, email = ?, cep = ?, endereco = ?, numero = ?, data_nasc = ? WHERE id_cliente = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formato;
            LocalDate data;

            stmt.setString(1, cliente.getNome());

            String cpf = cliente.getCpfCnpj();
            if (cpf == null || cpf.trim().isEmpty()) {
                stmt.setNull(2, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(2, cpf);
            }

            stmt.setString(3, cliente.getTelefone());

            String email = cliente.getEmail();
            if (email == null || email.trim().isEmpty()) {
                stmt.setNull(4, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(4, email);
            }

            stmt.setString(5, cliente.getCep());
            stmt.setString(6, cliente.getEndereco());
            stmt.setString(7, cliente.getNumero());

            if (cliente.getDataNascimento() != null && !cliente.getDataNascimento().trim().isEmpty()) {
                formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                data = LocalDate.parse(cliente.getDataNascimento(), formato);
                stmt.setDate(8, java.sql.Date.valueOf(data));
            } else {
                stmt.setNull(8, java.sql.Types.DATE);
            }

            stmt.setInt(9, cliente.getId());


            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean excluir(int id) {

        String sql = "DELETE FROM cliente WHERE id_cliente = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Cliente> pesquisar(String termo) {

        ArrayList<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT * FROM cliente WHERE nome LIKE ?  OR cpfCnpj LIKE ?  OR telefone LIKE ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String busca = "%" + termo + "%";

            stmt.setString(1, busca);
            stmt.setString(2, busca);
            stmt.setString(3, busca);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Cliente cliente = new Cliente(
                        rs.getString("nome"),
                        rs.getString("cpfCnpj"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("cep"),
                        rs.getString("endereco"),
                        rs.getString("numero"),
                        formatarData(rs.getDate("data_nasc"))
                );

                cliente.setId(rs.getInt("id_cliente"));

                clientes.add(cliente);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return clientes;
    }

    public Cliente buscarPorId(int id) {

        String sql = "SELECT * FROM cliente WHERE id_cliente = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Cliente cliente = new Cliente(
                        rs.getString("nome"),
                        rs.getString("cpfCnpj"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("cep"),
                        rs.getString("endereco"),
                        rs.getString("numero"),
                        formatarData(rs.getDate("data_nasc"))
                );

                cliente.setId(rs.getInt("id_cliente"));

                return cliente;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public Cliente buscarPorNome(String nome) {
        String sql = "SELECT * FROM cliente WHERE nome = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("nome"),
                        rs.getString("cpfCnpj"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("cep"),
                        rs.getString("endereco"),
                        rs.getString("numero"),
                        formatarData(rs.getDate("data_nasc"))
                );
                cliente.setId(rs.getInt("id_cliente"));
                return cliente;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private String formatarData(java.sql.Date data) {
        if (data == null) return null;
        return data.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
