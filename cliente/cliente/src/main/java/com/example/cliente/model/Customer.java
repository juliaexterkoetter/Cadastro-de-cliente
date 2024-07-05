package com.example.cliente.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório!")
    private String name;

    @NotBlank(message = "O e-mail é obrigatório!")
    @Email(message = "O e-mail deve ser um e-mail válido!")
    private String email;

    @NotBlank(message = "O telefone é obrigatório!")
    //@Pattern(regexp = "(^$|[0-9]{11})")
    @Pattern(regexp = "(^$|\\([0-9]{2}\\) [0-9]{4}-[0-9]{4})", message = "O telefone deve ser no formato (99) 9999-9999")
    private String phone;

    @NotBlank(message = "O CPF é obrigatório!")
    @Column(unique = true, length = 14)
    @Pattern(regexp = "(^$|[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2})", message = "O CPF deve ter o formato 999.999.999-99")
    private String documentNumber;

    private LocalDate registrationDate;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setCustomer(this);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
        order.setCustomer(null);
    }
}
