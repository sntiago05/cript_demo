package cripto.dominio;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class CriptoEnPortfolio {
    private final SimpleStringProperty symbol;
    private final SimpleDoubleProperty cantidad;

    public CriptoEnPortfolio(String symbol, Double cantidad) {
        this.symbol = new SimpleStringProperty(symbol);
        this.cantidad = new SimpleDoubleProperty(cantidad);
    }

    public String getSymbol() {
        return symbol.get();
    }

    public SimpleStringProperty symbolProperty() {
        return symbol;
    }

    public Double getCantidad() {
        return cantidad.get();
    }

    public SimpleDoubleProperty cantidadProperty() {
        return cantidad;
    }
}
