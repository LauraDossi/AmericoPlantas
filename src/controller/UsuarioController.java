package controller;

import model.Usuario;
import model.UsuarioDAO;
import model.LogAuditoria;
import model.LogAuditoriaDAO;
import view.UsuarioView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class UsuarioController {

    private final UsuarioDAO dao;
    private final LogAuditoriaDAO logDAO;

    public UsuarioController() {
        dao = new UsuarioDAO();
        logDAO = new LogAuditoriaDAO();
    }

    public void salvarUsuario(UsuarioView view) {

        if (!LoginController.isAdministrador()) {
            view.exibirMensagem("Acesso negado! Apenas administradores podem cadastrar usuários.");
            return;
        }

        if (view.getTxtNome().getText().isEmpty() ||
                view.getTxtCpf().getText().isEmpty() ||
                view.getTxtDataNascimento().getText().isEmpty() ||
                view.getTxtEndereco().getText().isEmpty() ||
                view.getTxtNumero().getText().isEmpty() ||
                view.getTxtTelefone().getText().isEmpty() ||
                view.getTxtEmail().getText().isEmpty() ||
                view.getTxtLogin().getText().isEmpty() ||
                view.getTxtSenha().getPassword().length == 0){

            view.exibirMensagem("Preencha todos os campos obrigatórios!");
            return;
        }

        if (dao.campoJaExiste("cpfCnpj", view.getTxtCpf().getText())) {
            view.exibirMensagem("CPF digitado já está cadastrado! Insira um CPF diferente.");
            return;
        }

        if (dao.campoJaExiste("email", view.getTxtEmail().getText())) {
            view.exibirMensagem("Email já está em uso! Insira um email diferente.");
            return;
        }

        if (dao.campoJaExiste("login", view.getTxtLogin().getText())) {
            view.exibirMensagem("Login já está em uso! Escolha um login diferente.");
            return;
        }

        String tipo = view.isAdministradorSelecionado() ? "Administrador" : "Operador";
        String senha = new String(view.getTxtSenha().getPassword());

        Usuario usuario = new Usuario(
                view.getTxtNome().getText(),
                view.getTxtCpf().getText(),
                view.getTxtDataNascimento().getText(),
                view.getTxtEndereco().getText(),
                view.getTxtNumero().getText(),
                view.getTxtCep().getText(),
                view.getTxtTelefone().getText(),
                view.getTxtEmail().getText(),
                view.getTxtLogin().getText(),
                senha,
                tipo
        );

        boolean cadastro = dao.inserir(usuario);
        if (cadastro){
            view.exibirMensagem("Usuário "+ view.getTxtNome().getText() + " cadastrado(a) com sucesso!");

            LoginController.adicionarUsuario(usuario);

            view.limparCampos();
            carregarTabela(view);
            atualizarTotal(view);
        }else{
            view.exibirMensagem("Erro ao cadastrar usuário...");
        }
    }

    public void editarUsuario(UsuarioView view) {

        int linha = view.getTabela().getSelectedRow();

        if (linha < 0) {
            view.exibirMensagem("Selecione um usuário para editar!");
            return;
        }

        int id = (int) view.getModel().getValueAt(linha, 0);

        String tipo = view.isAdministradorSelecionado() ? "Administrador" : "Operador";
        String senha = new String(view.getTxtSenha().getPassword());

        Usuario usuario = new Usuario(
                view.getTxtNome().getText(),
                view.getTxtCpf().getText(),
                view.getTxtDataNascimento().getText(),
                view.getTxtEndereco().getText(),
                view.getTxtNumero().getText(),
                view.getTxtCep().getText(),
                view.getTxtTelefone().getText(),
                view.getTxtEmail().getText(),
                view.getTxtLogin().getText(),
                senha,
                tipo
        );

        usuario.setId(id);

        if (dao.editar(usuario)) {
            view.exibirMensagem("Usuário atualizado com sucesso!");
            carregarTabela(view);
            atualizarTotal(view);
            view.limparCampos();
        } else {
            view.exibirMensagem("Erro ao atualizar usuário.");
        }
    }

    public void pesquisarUsuarios(UsuarioView view) {

        String termo = view.getTxtPesquisa().getText();
        DefaultTableModel model = view.getModel();
        model.setRowCount(0);

        List<Usuario> resultado = dao.pesquisar(termo);

        for (Usuario u : resultado) {
            model.addRow(new Object[]{
                    u.getId(),
                    u.getNome(),
                    u.getLogin(),
                    u.getCpfCnpj(),
                    u.getTelefone(),
                    u.getEmail(),
                    u.getTipoUsuario(),
                    u.getEndereco(),
                    u.getNumero(),
                    u.getCep(),
                    u.getDataNascimento()
            });
        }
    }

    public List<Usuario> listarUsuarios() {
        return dao.listar();
    }

    public Usuario buscarPorNome(String nome) {
        return dao.buscarPorNome(nome);
    }

    public Usuario buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public void excluirUsuario(UsuarioView view) {
        int linha = view.getTabela().getSelectedRow();

        if (linha < 0) {
            view.exibirMensagem("Selecione um usuário!");
            return;
        }

        int id = (int) view.getModel().getValueAt(linha, 0);
        String nomeUsuario = (String) view.getModel().getValueAt(linha, 1);
        String loginUsuario = (String) view.getModel().getValueAt(linha, 2);

        if (dao.excluir(id)) {

            var usuario = LoginController.getUsuarioLogado();
            if (usuario != null) {
                LogAuditoria log = new LogAuditoria(
                        usuario.getId(),
                        usuario.getNome(),
                        "EXCLUIR",
                        "Usuario",
                        id,
                        "Nome: " + nomeUsuario + ", Login: " + loginUsuario
                );
                logDAO.registrar(log);
            }

            view.exibirMensagem("Usuário excluído com sucesso!");

            carregarTabela(view);
            atualizarTotal(view);
            view.limparCampos();
        } else {
            view.exibirMensagem("Erro ao excluir usuário.");
        }
    }

    public void atualizarTotal(UsuarioView view) {
        view.getLblTotal().setText("Total: " + getTotalUsuarios() + " usuário(s)");
    }

    public int getTotalUsuarios() {
        return dao.listar().size();
    }

    public void carregarUsuarioSelecionado(UsuarioView view) {

        int linha = view.getTabela().getSelectedRow();

        if (linha < 0) {
            return;
        }

        int id = (int) view.getModel().getValueAt(linha, 0);

        Usuario usuario = dao.buscarPorId(id);

        if (usuario != null) {
            view.getTxtNome().setText(usuario.getNome());
            view.getTxtCpf().setText(usuario.getCpfCnpj());
            view.getTxtTelefone().setText(usuario.getTelefone());
            view.getTxtEmail().setText(usuario.getEmail());
            view.getTxtEndereco().setText(usuario.getEndereco());
            view.getTxtNumero().setText(usuario.getNumero());
            view.getTxtCep().setText(usuario.getCep());
            view.getTxtDataNascimento().setText(usuario.getDataNascimento());
            view.getTxtLogin().setText(usuario.getLogin());

            if ("Administrador".equalsIgnoreCase(usuario.getTipoUsuario())) {
                view.getRbAdministrador().setSelected(true);
            } else {
                view.getRbOperador().setSelected(true);
            }
        }
    }

    public void carregarTabela(UsuarioView view) {

        DefaultTableModel model = view.getModel();
        model.setRowCount(0);

        List<Usuario> usuarios = dao.listar();

        for (Usuario u : usuarios) {
            model.addRow(new Object[]{
                    u.getId(),
                    u.getNome(),
                    u.getLogin(),
                    u.getCpfCnpj(),
                    u.getTelefone(),
                    u.getEmail(),
                    u.getTipoUsuario(),
                    u.getEndereco(),
                    u.getNumero(),
                    u.getCep(),
                    u.getDataNascimento()
            });
        }
    }
}