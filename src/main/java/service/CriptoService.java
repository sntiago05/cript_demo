package service;

import cripto.model.*;
import cripto.repository.TransactionalRepository;
import cripto.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDateTime;

public class CriptoService {

    private final UserRepository userRepository;
    private final TransactionalRepository transactionalRepository;

    public CriptoService(UserRepository userRepository, TransactionalRepository transactionalRepository) {
        this.userRepository = userRepository;
        this.transactionalRepository = transactionalRepository;
    }

    public CriptoService(EntityManager em) {
        this.userRepository = new UserRepository(em);
        this.transactionalRepository = new TransactionalRepository(em);
    }

    public void buyCripto(Integer userId, Symbol symbol, Double amount, Double price) throws Exception {
        EntityManager em = userRepository.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            User user = userRepository.findById(userId);
            if (user == null) throw new Exception("User not found");

            double cost = amount * price;
            if (user.getBalance() < cost) throw new Exception("No balance enough");

            user.setBalance(user.getBalance() - cost);
            user.getPortfolio().put(symbol, user.getPortfolio().getOrDefault(symbol, 0.0) + amount);

            userRepository.saveWithoutTransaction(user);

            Transactional transaction = new Transactional(user, symbol, amount, price);
            transaction.setTimestamp(LocalDateTime.now());

            transactionalRepository.saveWithoutTransaction(transaction);

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();  // importante cerrar siempre el EntityManager
        }
    }

    public void sellCripto(Integer userId, Symbol symbol, Double amount, Double price) throws Exception {
        EntityManager em = userRepository.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            User user = userRepository.findById(userId);
            if (user == null) throw new Exception("User not found");

            double owned = user.getPortfolio().getOrDefault(symbol, 0.0);
            if (owned < amount) throw new Exception("Not enough crypto owned");

            user.getPortfolio().put(symbol, owned - amount);
            user.setBalance(user.getBalance() + amount * price);

            userRepository.saveWithoutTransaction(user);

            Transactional transaction = new Transactional(user, symbol, -amount, price);
            transaction.setTimestamp(LocalDateTime.now());

            transactionalRepository.saveWithoutTransaction(transaction);

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
