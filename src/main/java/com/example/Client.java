package com.example;/*
 * @Author Administrator
 * @Description //TODO
 * @Date 2018/12/17/017
 **/

import java.util.stream.Stream;

public class Client {
    public static void main(String[] args) {
        String str = "11,22,33,44,55";
        Stream<Integer> integerStream = Stream.of(str.split(",")).map(x -> Integer.valueOf(x+2));
        integerStream.forEach(x ->{
            ++x;
            System.out.println(x);
        });
    }
}
