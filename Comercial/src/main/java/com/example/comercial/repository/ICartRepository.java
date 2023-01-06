package com.example.comercial.repository;

import com.example.comercial.model.Cart;
import com.example.comercial.model.User;
import com.example.comercial.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByProductAndUser(Product product,User user);
    @Modifying
    @Transactional
    @Query(value="delete from cart a  where a.user_id= :userId", nativeQuery = true)
    void deleteAllCartByUserId(@Param("userId") Long userId);

    Iterable<Cart> findAllByUserId(Long userId);
}
