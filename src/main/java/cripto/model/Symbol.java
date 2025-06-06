package cripto.model;

public enum Symbol {

    BTC(1, "bitcoin", 50000.0),
    ETH(2, "ethereum", 3000.0),
    USDT(3, "usdt", 1.0),
    LUN(4, "luna", 100.0),
    DOG(5, "dogecoin", 0.5);

    private int code;
    private String name;
    private Double initialPrice; // Atributo para el precio inicial

    // Constructor para inicializar el código, nombre y el precio inicial
    Symbol(int code, String name, Double initialPrice) {
        this.code = code;
        this.name = name;
        this.initialPrice = initialPrice;
    }

    // Métodos getter
    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Double getInitialPrice() {
        return initialPrice; // Retorna el precio inicial de la cripto
    }
}
