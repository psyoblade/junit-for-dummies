package me.suhyuk.test;

import me.suhyuk.junit.Maker;
import me.suhyuk.mock.*;
import me.suhyuk.test.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StubbingTest {

    @Test
    void testFoo() {
        assertTrue(true == true);
    }

    @Test
    @DisplayName("여러 외부객체를 사용하는 서비스를 목킹하는 예제")
    void testServiceUsingServices(@Mock MemberService memberService,
                                  @Mock MakerRepository makerRepository) throws MemberNotFoundException, InvalidMemberException {
        Maker maker = Maker.builder().age(10).name("테스트").build();
        when(memberService.findById(1L)).thenReturn(Optional.of(me.suhyuk.test.domain.Member.builder().name("테스트").build()));
        when(makerRepository.count()).thenReturn(1L);
        when(makerRepository.save(any())).thenReturn(maker);
        // 2개의 서비스를 사용하는 하나의 서비스를 테스트 하기 위해 객체를 생성후 메소드를 호출합니다
        MakerService makerService = new MakerService(memberService, makerRepository);
        Maker newMaker = makerService.createNewMaker(1L, maker);
        assertNotNull(newMaker.getName());
        assertEquals(newMaker.getName(), maker.getName());
        // 서비스 내부에서 참조하는 서비스의 함수가 몇 번 호출되었는지 확인합니다
        verify(memberService, never()).validate(anyLong());
        verify(memberService, times(1)).findById(anyLong());
        verify(makerRepository, times(1)).save(any(Maker.class));
        // 반드시 멤버 서비스를 호출하고, 레포지토리가 호출되는지 검증합니다 : verify 호출 순서가 실제 호출과 일치해야 합니다
        InOrder inOrder = inOrder(memberService, makerRepository);
        inOrder.verify(memberService).findById(anyLong());
        inOrder.verify(makerRepository).count();
        inOrder.verify(makerRepository).save(any(Maker.class));
        // 호출된 함수가 적절한 시간 내에 완료되었는지
        verify(memberService, timeout(100)).findById(anyLong());
        verify(makerRepository, timeout(100)).save(any(Maker.class));
    }

    // Cmd + N : Edit template 혹은 "File and Code Template" 메뉴에서 수정합니다
    @Test
    void testBDD(@Mock MemberService memberService, @Mock MakerRepository makerRepository) throws MemberNotFoundException {
        // Given
        MakerService makerService = new MakerService(memberService, makerRepository);
        me.suhyuk.test.domain.Member member = Member.builder().name("박수혁").build();
        Maker maker = Maker.builder().name("메이커").age(10).build();

        // When
        when(memberService.findById(anyLong()));
        // Execute the system under test

        // Then

        // Assert that the expected change has occurred
    }
}
