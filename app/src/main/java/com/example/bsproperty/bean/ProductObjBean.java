package com.example.bsproperty.bean;

import java.io.Serializable;

public class ProductObjBean extends BaseResponse {

    private ProductBean data;

    public ProductBean getData() {
        return data;
    }

    public void setData(ProductBean data) {
        this.data = data;
    }
}
