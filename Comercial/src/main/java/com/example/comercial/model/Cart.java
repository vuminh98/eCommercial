package com.example.comercial.model;

import com.example.comercial.model.product.Product;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Check(constraints = "quantity >= 0")
    private Integer quantity;
    @NotNull
    @Check(constraints = "price >= 0")
    private Double price;
    @ManyToOne
    private User user;
    @ManyToOne
    private Product product;
}
