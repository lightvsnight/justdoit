package com.sample.utils;

import java.util.Set;
import java.util.function.Consumer;

/**
 * TODO 写明类的作用
 *
 * @author Li.HongKun
 * @project authserver
 * @title 数组遍历组件
 * @package com.until
 * @since 2023/9/3 15:20
 */
public class ListHandleConsumer<T> implements Consumer<T> {

    private String[] redirectUris;

    public ListHandleConsumer(String[] redirectUris) {
        this.redirectUris = redirectUris;
    }

    @Override
    public void accept(T o) {
        if(o instanceof Set){
            for (String redirectUri: redirectUris) {
                ((Set<String>) o).add(redirectUri);
            }
        }
    }
}
