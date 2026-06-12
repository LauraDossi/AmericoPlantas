package controller;

import model.Cliente;
import model.ClienteDAO;
import model.LogAuditoria;
import model.LogAuditoriaDAO;
import view.ClienteView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ClienteController {
    private final ClienteDAO dao;
    private final LogAuditoriaDAO logDAO;

    public ClienteController() {
        dao = new ClienteDAO();
        logDAO = new LogAuditoriaDAO();
    }

    public void salvarCliente(ClienteView view) {

        if (!view.isAdmin()) {
            view.exibirMensagem("Acesso negado! Apenas administradores podem cadastrar clientes.");
            return;
        }

        if (view.getTxtNome().getText().trim().isEmpty()) {
            view.exibirMensagem("Preencha todos os campos obrigatórios!");
            return;
        }

        String cpf = view.getTxtCpfCnpj().getText().trim();
        if (!cpf.isEmpty() && dao.campoJaExiste("cpfCnpj", cpf)) {
            view.exibirMensagem("CPF digitado já está cadastrado! Insira um CPF diferente.");
            return;
        }

        String email = view.getTxtEmail().getText().trim();
        if (!email.isEmpty() && dao.campoJaExiste("email", email)) {
            view.exibirMensagem("Email já está em uso! Insira um email diferente.");
            return;
        }

        Cliente cliente = new Cliente(
                view.getTxtNome().getText(),
                view.getTxtCpfCnpj().getText(),
                view.getTxtTelefone().getText(),
                view.getTxtEmail().getText(),
                view.getTxtCep().getText(),
                view.getTxtEndereco().getText(),
                view.getTxtNumero().getText(),
                view.getTxtDataNascimento().getText()
        );

        boolean cadastro = dao.inserir(cliente);
        if (cadastro){
            view.exibirMensagem("Cliente "+ view.getTxtNome().getText() + " cadastrado(a) com sucesso!");
            view.limparCampos();
            carregarTabela(view);
        }else{
            view.exibirMensagem("Erro ao cadastrar cliente...");
        }


    }

    public void editarCliente(ClienteView view) {

        JTable tabela = view.getTabela();

        int linha = tabela.getSelectedRow();

        if (linha < 0) {
            view.exibirMensagem("Selecione um cliente para editar!");
            return;
        }

        int id = (int) view.getModel().getValueAt(linha, 0);

        Cliente cliente = new Cliente(
                view.getTxtNome().getText(),
                view.getTxtCpfCnpj().getText(),
                view.getTxtTelefone().getText(),
                view.getTxtEmail().getText(),
                view.getTxtCep().getText(),
                view.getTxtEndereco().getText(),
                view.getTxtNumero().getText(),
                view.getTxtDataNascimento().getText()
        );

        cliente.setId(id);

        boolean editado = dao.editar(cliente);

        if (editado) {
            view.exibirMensagem("Cliente atualizado com sucesso!");
            carregarTabela(view);
            view.limparCampos();

        } else {
            view.exibirMensagem("Erro ao atualizar cliente.");
        }
    }

    public void pesquisarClientes(ClienteView view) {

        String termo = view.getTxtPesquisa().getText().toLowerCase();
        DefaultTableModel model = view.getModel();

        model.setRowCount(0);

        List<Cliente> resultado = dao.pesquisar(termo);

        for (Cliente c : resultado) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getNome(),
                    c.getCpfCnpj(),
                    c.getTelefone(),
                    c.getEmail(),
                    c.getCep(),
                    c.getEndereco(),
                    c.getNumero(),
                    c.getDataNascimento()
            });
        }
    }

    public Cliente buscarPorNome(String nome) {
        return dao.buscarPorNome(nome);
    }

    public Cliente buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public List<Cliente> listarClientes() {
        return dao.listar();
    }

    public void excluirCliente(ClienteView view) {
        if (!view.isAdmin()) {
            view.exibirMensagem("Acesso negado! Apenas administradores podem excluir clientes.");
            return;
        }

        JTable tabela = view.getTabela();
        DefaultTableModel model = view.getModel();

        int linha = tabela.getSelectedRow();

        if (linha >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    view,
                    "Tem certeza que deseja excluir este cliente?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int id = (int) model.getValueAt(linha, 0);
                String nomeCliente = (String) model.getValueAt(linha, 1);
                String cpfCliente = (String) model.getValueAt(linha, 2);

                boolean excluido = dao.excluir(id);
                if (excluido) {

                    var usuario = controller.LoginController.getUsuarioLogado();
                    if (usuario != null) {
                        LogAuditoria log = new LogAuditoria(
                                usuario.getId(),
                                usuario.getNome(),
                                "EXCLUIR",
                                "Cliente",
                                id,
                                "Nome: " + nomeCliente + ", CPF: " + (cpfCliente.isEmpty() ? "N/A" : cpfCliente)
                        );
                        logDAO.registrar(log);
                    }


                    view.exibirMensagem("Cliente excluído com sucesso!");
                    carregarTabela(view);
                    view.limparCampos();
                } else {
                    view.exibirMensagem("Cliente não encontrado!");
                }

            }
        } else {
            view.exibirMensagem("Selecione um cliente para excluir!");
        }
    }

    public void carregarClienteSelecionado(ClienteView view) {

        JTable tabela = view.getTabela();

        int linha = tabela.getSelectedRow();

        if (linha < 0) {
            return;
        }

        int id = (int) tabela.getValueAt(linha, 0);

        Cliente cliente = dao.buscarPorId(id);

        if (cliente != null) {

            view.getTxtNome().setText(cliente.getNome());
            view.getTxtCpfCnpj().setText(cliente.getCpfCnpj());
            view.getTxtTelefone().setText(cliente.getTelefone());
            view.getTxtEmail().setText(cliente.getEmail());
            view.getTxtCep().setText(cliente.getCep());
            view.getTxtEndereco().setText(cliente.getEndereco());
            view.getTxtNumero().setText(cliente.getNumero());
            view.getTxtDataNascimento().setText(cliente.getDataNascimento());
        }
    }

    public void carregarTabela(ClienteView view) {

        DefaultTableModel model = view.getModel();
        model.setRowCount(0);

        List<Cliente> clientes = dao.listar();

        for (Cliente c : clientes) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getNome(),
                    c.getCpfCnpj(),
                    c.getTelefone(),
                    c.getEmail(),
                    c.getCep(),
                    c.getEndereco(),
                    c.getNumero(),
                    c.getDataNascimento()
            });
        }
    }
}