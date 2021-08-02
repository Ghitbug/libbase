package com.gh.libbase.live;

import java.io.Serializable;
import java.util.List;

/**
 * BaseListVo
 *
 * @version 4.0.0
 * @auth GH
 * @time 2019/2/14
 * @description YjSales
 */
public class BaseListVo<T> implements Serializable {
    public List<T> data;
}
