package model;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class UsuarioDAO {

    private static String gerarSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private static String gerarHash(String senha, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String senhaComSalt = senha + salt;
            byte[] hash = md.digest(senhaComSalt.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash", e);
        }
    }

    private static boolean verificarSenha(String senhaDigitada, String saltArmazenado, String hashArmazenado) {
        return gerarHash(senhaDigitada, saltArmazenado).equals(hashArmazenado);
    }

    public Usuario autenticar(String login, String senhaDigitada) {
        String sql = "SELECT id_usuario, nome, login, senha_hash, salt, tipo_usuario, tentativas_falhas, bloqueado_ate " +
                "FROM usuario WHERE login = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Timestamp bloqueadoAte = rs.getTimestamp("bloqueado_ate");
                if (bloqueadoAte != null && bloqueadoAte.toLocalDateTime().isAfter(LocalDateTime.now())) {
                    JOptionPane.showMessageDialog(null, "Usuário bloqueado. Tente novamente após " +
                            bloqueadoAte.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                    return null;
                }

                String hashArmazenado = rs.getString("senha_hash");
                String salt = rs.getString("salt");
                boolean senhaOk;

                if (salt == null) {

                    senhaOk = senhaDigitada.equals(hashArmazenado);
                    if (senhaOk) {
                        String novoSalt = gerarSalt();
                        String novoHash = gerarHash(senhaDigitada, novoSalt);
                        atualizarSenhaHash(rs.getInt("id_usuario"), novoHash, novoSalt);
                    }
                } else {
                    senhaOk = verificarSenha(senhaDigitada, salt, hashArmazenado);
                }

                if (senhaOk) {

                    resetarTentativas(rs.getInt("id_usuario"));
                    Usuario u = new Usuario(
                            rs.getString("nome"),
                            rs.getString("login"),
                            "",
                            rs.getString("tipo_usuario")
                    );
                    u.setId(rs.getInt("id_usuario"));
                    return u;
                } else {

                    incrementarTentativas(rs.getInt("id_usuario"));
                    JOptionPane.showMessageDialog(null, "Usuário ou senha inválidos.");
                    return null;
                }
            }
            JOptionPane.showMessageDialog(null, "Usuário ou senha inválidos.");
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao autenticar: " + e.getMessage());
            return null;
        }
    }

    private void incrementarTentativas(int idUsuario) {
        String sql = "UPDATE usuario SET tentativas_falhas = tentativas_falhas + 1 WHERE id_usuario = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();

            sql = "SELECT tentativas_falhas FROM usuario WHERE id_usuario = ?";
            try (PreparedStatement stmt2 = conn.prepareStatement(sql)) {
                stmt2.setInt(1, idUsuario);
                ResultSet rs = stmt2.executeQuery();
                if (rs.next() && rs.getInt(1) >= 3) {
                    LocalDateTime bloqueio = LocalDateTime.now().plusMinutes(5);
                    sql = "UPDATE usuario SET bloqueado_ate = ? WHERE id_usuario = ?";
                    try (PreparedStatement stmt3 = conn.prepareStatement(sql)) {
                        stmt3.setTimestamp(1, Timestamp.valueOf(bloqueio));
                        stmt3.setInt(2, idUsuario);
                        stmt3.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Muitas tentativas falhas. Usuário bloqueado por 5 minutos.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetarTentativas(int idUsuario) {
        String sql = "UPDATE usuario SET tentativas_falhas = 0, bloqueado_ate = NULL WHERE id_usuario = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void atualizarSenhaHash(int idUsuario, String novoHash, String novoSalt) {
        String sql = "UPDATE usuario SET senha_hash = ?, salt = ? WHERE id_usuario = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoHash);
            stmt.setString(2, novoSalt);
            stmt.setInt(3, idUsuario);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean inserir(Usuario usuario){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = LocalDate.parse(usuario.getDataNascimento(), formato);
        String salt = gerarSalt();
        String hash = gerarHash(usuario.getSenha(), salt);

        String sql = "INSERT INTO usuario (nome, cpfCnpj, telefone, email, endereco, cep, numero, data_nasc, tipo_usuario, login, senha_hash, salt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpfCnpj());
            stmt.setString(3, usuario.getTelefone());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getEndereco());
            stmt.setString(6, usuario.getCep());
            stmt.setString(7, usuario.getNumero());
            stmt.setDate(8, java.sql.Date.valueOf(data));
            stmt.setString(9, usuario.getTipoUsuario());
            stmt.setString(10, usuario.getLogin());
            stmt.setString(11, hash);
            stmt.setString(12, salt);

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean campoJaExiste(String campo, String valor) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE " + campo + " = ?";

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

    public boolean editar(Usuario usuario){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = LocalDate.parse(usuario.getDataNascimento(), formato);


        Usuario antigo = buscarPorId(usuario.getId());
        String hash, salt;
        if (!usuario.getSenha().equals(antigo.getSenha())) {
            salt = gerarSalt();
            hash = gerarHash(usuario.getSenha(), salt);
        } else {

            salt = antigo.getSalt();
            hash = antigo.getSenha();
        }

        String sql = "UPDATE usuario SET nome = ?, cpfCnpj = ?, telefone = ?, email = ?, endereco = ?, cep = ?, numero = ?, data_nasc = ?, tipo_usuario = ?, login = ?, senha_hash = ?, salt = ? WHERE id_usuario = ?";

        try(Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpfCnpj());
            stmt.setString(3, usuario.getTelefone());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getEndereco());
            stmt.setString(6, usuario.getCep());
            stmt.setString(7, usuario.getNumero());
            stmt.setDate(8, java.sql.Date.valueOf(data));
            stmt.setString(9, usuario.getTipoUsuario());
            stmt.setString(10, usuario.getLogin());
            stmt.setString(11, hash);
            stmt.setString(12, salt);
            stmt.setInt(13, usuario.getId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public boolean excluir(int id){
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try(Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Usuario> pesquisar(String termo){
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE nome LIKE ? OR login LIKE ? OR cpfCnpj LIKE ? OR email LIKE ?";

        try(Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            String busca = "%" + termo + "%";
            stmt.setString(1, busca);
            stmt.setString(2, busca);
            stmt.setString(3, busca);
            stmt.setString(4, busca);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Usuario usuario = new Usuario(
                        rs.getString("nome"),
                        rs.getString("cpfCnpj"),
                        rs.getDate("data_nasc").toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        rs.getString("endereco"),
                        rs.getString("numero"),
                        rs.getString("cep"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("senha_hash"),
                        rs.getString("tipo_usuario")
                );
                usuario.setId(rs.getInt("id_usuario"));
                usuarios.add(usuario);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return usuarios;
    }

    public ArrayList<Usuario> listar() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getString("nome"),
                        rs.getString("cpfCnpj"),
                        rs.getDate("data_nasc").toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        rs.getString("endereco"),
                        rs.getString("numero"),
                        rs.getString("cep"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("senha_hash"),
                        rs.getString("tipo_usuario")
                );
                usuario.setId(rs.getInt("id_usuario"));
                usuarios.add(usuario);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return usuarios;
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getString("nome"),
                        rs.getString("cpfCnpj"),
                        rs.getDate("data_nasc").toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        rs.getString("endereco"),
                        rs.getString("numero"),
                        rs.getString("cep"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("senha_hash"),
                        rs.getString("tipo_usuario")
                );
                usuario.setId(rs.getInt("id_usuario"));
                return usuario;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Usuario buscarPorNome(String nome) {
        String sql = "SELECT * FROM usuario WHERE nome = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getString("nome"),
                        rs.getString("cpfCnpj"),
                        rs.getDate("data_nasc").toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        rs.getString("endereco"),
                        rs.getString("numero"),
                        rs.getString("cep"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("senha_hash"),
                        rs.getString("tipo_usuario")
                );
                usuario.setId(rs.getInt("id_usuario"));
                return usuario;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE email = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean alterarSenhaPorEmail(String email, String novaSenha) {
        String salt = gerarSalt();
        String hash = gerarHash(novaSenha, salt);
        String sql = "UPDATE usuario SET senha_hash = ?, salt = ? WHERE email = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, hash);
            stmt.setString(2, salt);
            stmt.setString(3, email);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String buscarEmailPorLogin(String login) {
        String sql = "SELECT email FROM usuario WHERE login = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("email");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String getSaltByUsuarioId(int id) {
        String sql = "SELECT salt FROM usuario WHERE id_usuario = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("salt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}