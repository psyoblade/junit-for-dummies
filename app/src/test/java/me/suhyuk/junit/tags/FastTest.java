package me.suhyuk.junit.tags;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 어디에 쓸 수 있는가 - 메소드
@Retention(RetentionPolicy.RUNTIME) // 이 어노테이션이 런타임시에도 유지되어야 함
@Test // 테스트 어노테이션으로 사용할 것이며
@Tag("fast") // 메타 태그를 fast 등록합니다
public @interface FastTest { // FastTest 어노테이션은 @Test 와 @Tag 2개를 메타 어노테이션으로 사용하여 컴포즈 어노테이션을 생성할 수 있다
}
