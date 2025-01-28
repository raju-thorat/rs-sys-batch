package com.raj.spring_batch_demo.config.examples.chunk.itemReaderWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private String productId;
    private String productName;
    private String productCategory;
    private Integer productPrice;
}
