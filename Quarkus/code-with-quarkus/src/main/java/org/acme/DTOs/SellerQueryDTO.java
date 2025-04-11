package org.acme.DTOs;

import java.util.List;

public class SellerQueryDTO {
    private SellerDTO seller;
    private List<SaleDTO> sales;

    public SellerDTO getSeller() {
        return seller;
    }

    public void setSeller(SellerDTO seller) {
        this.seller = seller;
    }

    public List<SaleDTO> getSales() {
        return sales;
    }

    public void setSales(List<SaleDTO> sales) {
        this.sales = sales;
    }
}
