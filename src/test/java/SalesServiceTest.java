package com.lab.service;

import com.lab.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class SalesServiceTest {

    private SalesService service;

    @BeforeEach
    void setUp() {
        service = new SalesService();
    }

    // ================= calculateSubtotal() =================

    @Test
    @DisplayName("Subtotal = price * quantity (normal case)")
    void testCalculateSubtotal_normal() {
        Product p = new Product("P01", "Mouse", 500, 3);
        assertEquals(1500, service.calculateSubtotal(p), 0.001);
    }

    @Test
    @DisplayName("Subtotal throws exception when product is null")
    void testCalculateSubtotal_nullProduct() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateSubtotal(null));
    }

    @Test
    @DisplayName("Subtotal with quantity = 1")
    void testCalculateSubtotal_quantityOne() {
        Product p = new Product("P02", "Keyboard", 800, 1);
        assertEquals(800, service.calculateSubtotal(p), 0.001);
    }

    // ================= calculateDiscount() =================

    @ParameterizedTest
    @DisplayName("Discount boundary value analysis")
    @CsvSource({
        "999.99, 0",
        "1000,   50",
        "4999.99, 249.9995",
        "5000,   500",
        "9999.99, 999.999",
        "10000,  1500"
    })
    void testCalculateDiscount(double subtotal, double expected) {
        assertEquals(expected, service.calculateDiscount(subtotal), 0.01);
    }

    @Test
    @DisplayName("Discount throws exception when subtotal is negative")
    void testCalculateDiscount_negative() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateDiscount(-100));
    }

    // ================= calculateShippingFee() =================

    @ParameterizedTest
    @CsvSource({
        "1999.99, 50",
        "2000,    0",
        "2000.01, 0"
    })
    void testCalculateShippingFee(double subtotal, double expected) {
        assertEquals(expected, service.calculateShippingFee(subtotal), 0.01);
    }

    // ================= calculateTotal() =================

    @Test
    @DisplayName("Total = Subtotal - Discount + Shipping")
    void testCalculateTotal_normal() {
        Product p = new Product("P03", "Monitor", 1000, 1);
        double total = service.calculateTotal(p);
        assertTrue(total > 0);
    }

    @Test
    @DisplayName("Total with low subtotal has shipping fee")
    void testCalculateTotal_withShipping() {
        Product p = new Product("P04", "Cable", 100, 5);
        double total = service.calculateTotal(p);
        assertEquals(550, total, 0.01);
    }

    // ================= classifyCustomer() =================

    @ParameterizedTest
    @CsvSource({
        "999,   REGULAR",
        "1000,  SILVER",
        "5000,  GOLD",
        "10000, VIP"
    })
    void testClassifyCustomer(double total, String expected) {
        assertEquals(expected, service.classifyCustomer(total));
    }

    // ================= Product validation (exception tests) =================

    @Test
    @DisplayName("Product constructor throws when price <= 0")
    void testProduct_invalidPrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product("P05", "Bad", 0, 5));
    }

    @Test
    @DisplayName("Product constructor throws when quantity <= 0")
    void testProduct_invalidQuantity() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product("P06", "Bad", 100, 0));
    }

    @Test
    @DisplayName("Product constructor throws when productId is null")
    void testProduct_nullProductId() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(null, "Mouse", 100, 5));
    }

    @Test
    @DisplayName("Product constructor throws when productId is empty")
    void testProduct_emptyProductId() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product("", "Mouse", 100, 5));
    }

    @Test
    @DisplayName("Product constructor throws when productName is null")
    void testProduct_nullProductName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product("P07", null, 100, 5));
    }

    @Test
    @DisplayName("Product constructor throws when productName is empty")
    void testProduct_emptyProductName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product("P08", "", 100, 5));
    }
}