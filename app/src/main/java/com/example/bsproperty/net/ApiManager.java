package com.example.bsproperty.net;

/**
 * Created by yezi on 2018/1/27.
 */

public class ApiManager {

    private static final String HTTP = "http://";
    private static final String IP = "192.168.55.103";
    private static final String PROT = ":8080";
    private static final String HOST = HTTP + IP + PROT;
    private static final String API = "/api";
    private static final String USER = "/user";
    private static final String SHOP = "/shop";
    private static final String PRODUCT = "/product";
    private static final String ORDER = "/order";


    public static final String USER_RG = HOST + API + USER + "/rg";
    public static final String USER_LOGIN = HOST + API + USER + "/login";

    public static final String SHOP_LIST = HOST + API + SHOP + "/list";
    public static final String SHOP_LIKE = HOST + API + SHOP + "/like";
    public static final String SHOP_MORE = HOST + API + SHOP + "/more";

    public static final String PRODUCT_ADD = HOST + API + PRODUCT + "/add";

    public static final String ORDER_ADD = HOST + API + ORDER + "/add";
    public static final String ORDER_LIST = HOST + API + ORDER + "/list";
    public static final String ORDER_DEL = HOST + API + ORDER + "/del";

}
