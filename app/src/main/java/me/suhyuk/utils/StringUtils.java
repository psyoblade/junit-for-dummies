package me.suhyuk.utils;

/**
 * <a href="https://www.baeldung.com/java-8-lambda-expressions-tips">Lambda Expressions and Functional Interfaces: Tips and Best Practices</a>
 *
 * @FunctionalInterface 는 람다 함수 정의를 위한 인터페이스이며, 하나 이상의 구현함수를 가질 수 없으나, default 함수는 가질 수 있습니다
 * 다만, default 함수를 가지는 것은 바람직하지 않으며, 람다함수 자체로 짧고 스스로 설명가능한 구현을 지향해야 합니다
 *
 * By using the @FunctionalInterface annotation, the compiler will trigger an error
 * in response to any attempt to break the predefined structure of a functional interface.
 *
 * It is also a very handy tool to make our application architecture easier to understand for other developers.
 */
@FunctionalInterface
public interface StringUtils {
    String upper(String str);
    default String lower(String str) { return str.toLowerCase(); }
}
