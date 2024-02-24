package com.balance.balanceservice.repository;

import com.balance.balanceservice.entity.Balance;
import com.balance.balanceservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Balance getByUserId(Long id);
}
