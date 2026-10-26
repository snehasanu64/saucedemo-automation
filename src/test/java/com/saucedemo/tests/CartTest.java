package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class CartTest extends BaseTest {

    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String BIKE_LIGHT = "Sauce Labs Bike Light";

    @Test(description = "TC_CART_01: Empty cart shows no badge")
    public void emptyCartHasNoBadge() {
        ProductsPage products = loginAsValidUser();
        Assert.assertFalse(products.isCartBadgeDisplayed(), "Cart badge should not be shown when cart is empty");
        Assert.assertEquals(products.getCartCount(), 0, "Cart count should be 0");
    }

    @Test(description = "TC_CART_02: Add one product, badge shows 1")
    public void addOneProduct() {
        ProductsPage products = loginAsValidUser();
        products.addToCart(BACKPACK);
        Assert.assertEquals(products.getCartCount(), 1, "Badge should show 1 after adding one product");
        Assert.assertEquals(products.getButtonText(BACKPACK), "Remove", "Button should change to Remove");
    }

    @Test(description = "TC_CART_03: Add two products, badge shows 2")
    public void addTwoProducts() {
        ProductsPage products = loginAsValidUser();
        products.addToCart(BACKPACK);
        products.addToCart(BIKE_LIGHT);
        Assert.assertEquals(products.getCartCount(), 2, "Badge should show 2 after adding two products");
    }

    @Test(description = "TC_CART_04: Remove a product from the products page")
    public void removeProduct() {
        ProductsPage products = loginAsValidUser();
        products.addToCart(BACKPACK);
        products.removeFromCart(BACKPACK);
        Assert.assertEquals(products.getCartCount(), 0, "Badge should disappear after removing the product");
        Assert.assertEquals(products.getButtonText(BACKPACK), "Add to cart", "Button should change back to Add to cart");
    }

    @Test(description = "TC_CART_05: Cart page lists the added items")
    public void cartPageListsItems() {
        ProductsPage products = loginAsValidUser();
        products.addToCart(BACKPACK);
        products.addToCart(BIKE_LIGHT);
        CartPage cart = products.openCart();
        List<String> names = cart.getItemNames();
        Assert.assertEquals(cart.getItemCount(), 2, "Cart page should list 2 items");
        Assert.assertTrue(names.contains(BACKPACK) && names.contains(BIKE_LIGHT),
                "Cart page should list both added products but showed " + names);
    }

    @Test(description = "TC_CART_06: Remove an item inside the cart page")
    public void removeItemInCartPage() {
        ProductsPage products = loginAsValidUser();
        products.addToCart(BACKPACK);
        products.addToCart(BIKE_LIGHT);
        CartPage cart = products.openCart();
        cart.removeItem(BACKPACK);
        Assert.assertEquals(cart.getItemCount(), 1, "One item should remain after removal");
        Assert.assertFalse(cart.getItemNames().contains(BACKPACK), "Removed product should not be in the cart");
    }
}
