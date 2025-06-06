package cripto.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "criptos")
public class Cripto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Symbol symbol;
    private Double currentPrice;
    @ElementCollection
    @CollectionTable(name = "cripto_price_history", joinColumns = @JoinColumn(name = "cripto_id"))
    @Column(name = "price")
    private List<Double> historyprice;

    public Cripto() {
        this.historyprice = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public List<Double> getHistoryprice() {
        return historyprice;
    }

    public void setHistoryprice(List<Double> historyprice) {
        this.historyprice = historyprice;
    }
}
