package controller;

import model.Fornecedor;
import model.FornecedorDAO;
import model.LogAuditoria;
import model.LogAuditoriaDAO;
import view.FornecedorView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class FornecedorController {

    private final FornecedorDAO dao = new FornecedorDAO();
    private final LogAuditoriaDAO logDAO = new LogAuditoriaDAO();

    public void salvarFornecedor(FornecedorView view) {

        if (!view.isAdmin()) {
            view.exibirMensagem("Acesso negado! Apenas administradores podem cadastrar fornecedores.");
            return;
        }

        if (view.getTxtNome().getText().isEmpty() ||
                view.getTxtCpfCnpj().getText().isEmpty() ||
                view.getTxtTelefone().getText().isEmpty() ||
                view.getTxtEmail().getText().isEmpty() ||
                view.getTxtEndereco().getText().isEmpty() ||
                view.getTxtNumero().getText().isEmpty()) {

            view.exibirMensagem("Preencha todos os campos obrigatórios!");
            return;
        }

        if (dao.campoJaExiste("cpfCnpj", view.getTxtCpfCnpj().getText())) {
            view.exibirMensagem("CNPJ/CPF digitado já está cadastrado! Insira um CNPJ/CPF diferente.");
            return;
        }

        if (dao.campoJaExiste("email", view.getTxtEmail().getText())) {
            view.exibirMensagem("Email já está em uso! Insira um email diferente.");
            return;
        }

        Fornecedor fornecedor = new Fornecedor(
                view.getTxtNome().getText(),
                view.getTxtCpfCnpj().getText(),
                view.getTxtTelefone().getText(),
                view.getTxtEmail().getText(),
                view.getTxtEndereco().getText(),
                view.getTxtCep().getText(),
                view.getTxtNumero().getText(),
                view.getTxtTiposProdutos().getText()
        );

        boolean cadastro = dao.inserir(fornecedor);
        if (cadastro){
            view.exibirMensagem("Fornecedor "+ view.getTxtNome().getText() + " cadastrado com sucesso!");
            atualizarTotal(view);
            view.limparCampos();
            carregarTabela(view);
        }else{
            view.exibirMensagem("Erro ao cadastrar fornecedor...");
        }
    }

    public void editarFornecedor(FornecedorView view) {

        if (!view.isAdmin()) {
            view.exibirMensagem("Acesso negado! Apenas administradores podem editar fornecedores.");
            return;
        }

        int linha = view.getTabela().getSelectedRow();

        if (linha < 0) {
            view.exibirMensagem("Selecione um fornecedor para editar!");
            return;
        }

        int id = (int) view.getModel().getValueAt(linha, 0);

        Fornecedor fornecedor = new Fornecedor(
                view.getTxtNome().getText(),
                view.getTxtCpfCnpj().getText(),
                view.getTxtTelefone().getText(),
                view.getTxtEmail().getText(),
                view.getTxtEndereco().getText(),
                view.getTxtCep().getText(),
                view.getTxtNumero().getText(),
                view.getTxtTiposProdutos().getText()
        );

        fornecedor.setId(id);

        boolean editado = dao.editar(fornecedor);

        if (editado) {
            view.exibirMensagem("Fornecedor atualizado com sucesso!");
            carregarTabela(view);
            atualizarTotal(view);
            view.limparCampos();

        } else {
            view.exibirMensagem("Erro ao atualizar fornecedor.");
        }

    }

    public void pesquisarFornecedores(FornecedorView view) {

        String termo = view.getTxtPesquisa().getText();
        DefaultTableModel model = view.getModel();

        model.setRowCount(0);

        List<Fornecedor> fornecedores = dao.pesquisar(termo);

        for (Fornecedor f : fornecedores) {
            model.addRow(new Object[]{
                    f.getId(),
                    f.getNome(),
                    f.getCpfCnpj(),
                    f.getTelefone(),
                    f.getEmail(),
                    f.getCep(),
                    f.getEndereco(),
                    f.getNumero(),
                    f.getTiposProdutos()
            });
        }
    }

    public List<Fornecedor> listarFornecedores() {
        return dao.listar();
    }

    public void excluirFornecedor(FornecedorView view) {

        if (!view.isAdmin()) {
            view.exibirMensagem("Acesso negado! Apenas administradores podem excluir fornecedores.");
            return;
        }

        int linha = view.getTabela().getSelectedRow();

        if (linha >= 0) {

            int confirm = JOptionPane.showConfirmDialog(
                    view,
                    "Tem certeza que deseja excluir este fornecedor?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                int id = (int) view.getModel().getValueAt(linha, 0);
                String nomeFornecedor = (String) view.getModel().getValueAt(linha, 1);
                String cnpjFornecedor = (String) view.getModel().getValueAt(linha, 2);

                dao.excluir(id);

                var usuario = controller.LoginController.getUsuarioLogado();
                if (usuario != null) {
                    LogAuditoria log = new LogAuditoria(
                            usuario.getId(),
                            usuario.getNome(),
                            "EXCLUIR",
                            "Fornecedor",
                            id,
                            "Nome: " + nomeFornecedor + ", CNPJ/CPF: " + (cnpjFornecedor.isEmpty() ? "N/A" : cnpjFornecedor)
                    );
                    logDAO.registrar(log);
                }

                view.exibirMensagem("Fornecedor excluído com sucesso!");

                carregarTabela(view);
                atualizarTotal(view);
                view.limparCampos();
            }

        } else {
            view.exibirMensagem("Selecione um fornecedor para excluir!");
        }
    }

    public void atualizarTotal(FornecedorView view) {
        view.getLblTotal().setText("Total: " + getTotalFornecedores() + " fornecedor(es)");
    }

    public int getTotalFornecedores() {
        return dao.listar().size();
    }

    public void carregarFornecedorSelecionado(FornecedorView view) {

        int linha = view.getTabela().getSelectedRow();

        if (linha >= 0) {

            int id = (int) view.getModel().getValueAt(linha, 0);

            List<Fornecedor> fornecedores = dao.listar();

            for (Fornecedor f : fornecedores) {

                if (f.getId() == id) {

                    view.getTxtNome().setText(f.getNome());
                    view.getTxtCpfCnpj().setText(f.getCpfCnpj());
                    view.getTxtTelefone().setText(f.getTelefone());
                    view.getTxtEmail().setText(f.getEmail());
                    view.getTxtEndereco().setText(f.getEndereco());
                    view.getTxtNumero().setText(f.getNumero());
                    view.getTxtCep().setText(f.getCep());
                    view.getTxtTiposProdutos().setText(f.getTiposProdutos());

                    break;
                }
            }
        }
    }

    public void carregarTabela(FornecedorView view) {

        DefaultTableModel model = view.getModel();
        model.setRowCount(0);

        List<Fornecedor> fornecedores = dao.listar();

        for (Fornecedor f : fornecedores) {

            model.addRow(new Object[]{
                    f.getId(),
                    f.getNome(),
                    f.getCpfCnpj(),
                    f.getTelefone(),
                    f.getEmail(),
                    f.getCep(),
                    f.getEndereco(),
                    f.getNumero(),
                    f.getTiposProdutos()
            });
        }
    }
}