package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductsTest extends BaseTest {

    @Test(description = "TC_PROD_01: Six products are shown")
    public void sixProductsShown() {
        ProductsPage products = loginAsValidUser();
        Assert.assertEquals(products.getProductCount(), 6, "Expected 6 products on the page");
    }

    @Test(description = "TC_PROD_02: Sort by price low to high")
    public void sortPriceLowToHigh() {
        ProductsPage products = loginAsValidUser();
        products.sortBy("Price (low to high)");
        List<Double> actual = products.getProductPrices();
        List<Double> expected = new ArrayList<>(actual);
        Collections.sort(expected);
        Assert.assertEquals(actual, expected, "Prices are not sorted low to high");
    }

    @Test(description = "TC_PROD_03: Sort by price high to low")
    public void sortPriceHighToLow() {
        ProductsPage products = loginAsValidUser();
        products.sortBy("Price (high to low)");
        List<Double> actual = products.getProductPrices();
        List<Double> expected = new ArrayList<>(actual);
        expected.sort(Collections.reverseOrder());
        Assert.assertEquals(actual, expected, "Prices are not sorted high to low");
    }

    @Test(description = "TC_PROD_04: Sort by name Z to A")
    public void sortNameZToA() {
        ProductsPage products = loginAsValidUser();
        products.sortBy("Name (Z to A)");
        List<String> actual = products.getProductNames();
        List<String> expected = new ArrayList<>(actual);
        expected.sort(Collections.reverseOrder());
        Assert.assertEquals(actual, expected, "Names are not sorted Z to A");
    }
}
