package cripto.model;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String password;
    private Double balance;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_portfolio", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "symbol")
    @Column(name = "amount")
    private Map<Symbol, Double> portfolio;


    public User() {
        this.portfolio = new HashMap<>();
    }

    public User(String name, String password) {
        this();
        this.name = name;
        this.password = password;
    }

    public User(String name, String password, Double balance) {
        this(name, password);
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Map<Symbol, Double> getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Map<Symbol, Double> portfolio) {
        this.portfolio = portfolio;
    }
}
