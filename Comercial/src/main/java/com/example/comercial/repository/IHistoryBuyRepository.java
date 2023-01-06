package com.example.comercial.repository;

import com.example.comercial.model.HistoryBuy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHistoryBuyRepository extends JpaRepository<HistoryBuy, Long> {
}
