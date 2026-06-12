package model;

public class ItemVenda {

    private int quantidade;
    private double valorUnitario;
    private double subTotal;
    private Produto produto;

    public ItemVenda(Produto produto, int quantidade, double valorUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        calcularSubtotal();
    }

    public void validarEstoque() {
        if (produto.getEstoque() < quantidade) {
            throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
        }
    }

    public void calcularSubtotal() {
        this.subTotal = quantidade * valorUnitario;
    }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public double getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(double valorUnitario) { this.valorUnitario = valorUnitario; }
    public double getSubTotal() { return subTotal; }
}
