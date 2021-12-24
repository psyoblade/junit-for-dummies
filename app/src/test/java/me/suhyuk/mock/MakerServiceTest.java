package me.suhyuk.mock;

import me.suhyuk.junit.Maker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // @Mock 객체를 자동 생성하기 위해 반드시 명시해야 합니다
class MakerServiceTest {

    // -- 아래와 같이 인터페이스만 가진 객체에 대해서 빈 껍질을 구현해서 객체를 만드는 과정을 Mockito 가 대신 해준다고 보면 됩니다
    @Test
    @DisplayName("Mockito 가 하는 일을 굳이 내가 해보기")
    void createMakerService() {
        MemberService memberService = new MemberService() {
            @Override
            public void validate(Long memberId) throws InvalidMemberException {
            }

            @Override
            public Optional<Member> findById(Long memberId) throws MemberNotFoundException {
                return Optional.empty();
            }
        };
        MakerRepository makerRepository = new MakerRepository() {
            @Override
            public List<Maker> findAll() {
                return null;
            }

            @Override
            public List<Maker> findAll(Sort sort) {
                return null;
            }

            @Override
            public List<Maker> findAllById(Iterable<Long> longs) {
                return null;
            }

            @Override
            public <S extends Maker> List<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends Maker> S saveAndFlush(S entity) {
                return null;
            }

            @Override
            public void deleteInBatch(Iterable<Maker> entities) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public Maker getOne(Long aLong) {
                return null;
            }

            @Override
            public <S extends Maker> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends Maker> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public Page<Maker> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Maker> S save(S entity) {
                return null;
            }

            @Override
            public Optional<Maker> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(Maker entity) {

            }

            @Override
            public void deleteAll(Iterable<? extends Maker> entities) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends Maker> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends Maker> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Maker> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends Maker> boolean exists(Example<S> example) {
                return false;
            }
        };
        MakerService makerService = new MakerService(memberService, makerRepository);
        assertNotNull(makerService);
    }

    // -- 모키토 mock 함수를 이용한 모키토 객체 생성
    @Test
    @DisplayName("모키토를 써서 객체 생성해보기")
    void testCreateServiceUsingMockito() {
        MemberService memberService = Mockito.mock(MemberService.class);
        MakerRepository makerRepository = Mockito.mock(MakerRepository.class);
        MakerService makerService = new MakerService(memberService, makerRepository);
        assertNotNull(makerService);
    }

    // -- 모키토 어노테이션을 사용하는 방법 - 클래스 전역적으로 사용하는 경우
    // -- 단순히 @Mock 은 선언만 하지 생성은 해주지 않습니다 - @ExtendWith(MockitoExtension.class)
    @Mock MemberService memberService;
    @Mock MakerRepository makerRepository;
    @Test
    @DisplayName("어노테이션을 이용한 모키토")
    void testCreateServiceUsingMockitoAnnotation() {
        MakerService makerService = new MakerService(memberService, makerRepository);
        assertNotNull(makerService);
    }

    // -- 모키노 인자로 전달받고 싶은 경우
    @Test
    @DisplayName("인자로 어노테이션 객체를 받는 경우")
    void testCreateServiceUsingParameterAnnotation(@Mock MemberService memberService,
                                                   @Mock MakerRepository makerRepository) {
        MakerService makerService = new MakerService(memberService, makerRepository);
        assertNotNull(makerService);
    }

    @Test
    @DisplayName("엄격한 vs 유연한 테스트")
    void testLenientAndStrict(@Mock Foo foo) {
        Mockito.lenient()
                .when(foo.getInt())
                .thenReturn(10);
        assertFalse(foo.getBoolean());
        // lenient 경우는 사용하지 않아도 오류가 없으나

        when(foo.getInt()).thenReturn(10);
        assertFalse(foo.getBoolean());
        // when 에서 선언하고 사용하지 않으면 UnnecessaryStubbingException 이 발생하게 됩니다
    }

    @Test
    @DisplayName("모키토 객체의 행동읠 정의하는 방법")
    void testMockAction() throws MemberNotFoundException {
        Member oldMember = Member.builder().name("예전이름").build();
        Maker oldMaker = Maker.builder().name("박수혁").age(10).build();
        Mockito.lenient() // Loosely stubbing jupiter 이전 버전 stubbing default 값을 사용합니다
                .when(memberService.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(oldMember));
        Mockito.lenient() // Strict stubbing default 값을 사용합니다
                .when(makerRepository.save(any(Maker.class)))
                .thenReturn(oldMaker);
        MakerService makerService = new MakerService(memberService, makerRepository);
        Long memberId = 1L;
        Maker newMaker = makerService.createNewMaker(memberId, oldMaker);
        assertEquals(oldMaker.getName(), newMaker.getName());
    }

    @Test
    @DisplayName("모키도 객체 확인해보기")
    void testMockObject(@Mock MemberService memberService) throws MemberNotFoundException {
        Member newMember = Member.builder().name("신입사원").build();
        when(memberService.findById(0L)).thenReturn(Optional.ofNullable(newMember));
        Member member0 = memberService.findById(0L).orElseThrow(() -> new IllegalArgumentException("입력오류0"));
        assertEquals(newMember.getName(), member0.getName());
        Member other = Member.builder().name("미입사자").build();
        Member member1 = memberService.findById(1L).orElse(other);
        assertEquals(other.getName(), member1.getName());
    }

    @ParameterizedTest
    @DisplayName("인자 매칭을 이용한 목킹")
    @ValueSource(longs = {1L, 2L, 3L, 4L}) // 반드시 @ParameterizedTest 가 있어야 하고 첫번째 인자로만 전달됩니다
    void testMockAnyObject(long id) throws MemberNotFoundException {
        when(memberService.findById(anyLong())).thenReturn(Optional.of(Member.builder().name("아무개").build()));
        assertEquals("아무개", memberService.findById(id).get().getName());
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, -2L})
    @DisplayName("예외를 던지는 모키토")
    void testMockThrowException(long id) throws MemberNotFoundException {
        assertThrows(IllegalArgumentException.class, () -> memberService.findById(id));
    }

    @Test
    @DisplayName("아규먼트 매처를 활용한 리스트 갯수 확인")
    void testArgumentMatcher() {
        LinkedList mockedList = Mockito.mock(LinkedList.class);
        when(mockedList.get(anyInt())).thenReturn("first");
        verify(mockedList).add(argThat(someString -> someString.toString().length() > 2));
    }

    @Test
    @DisplayName("수행한 작업들을 확인합니다")
    void testVerifyMockedList() {
        // 인터페이스가 아니라 실제 객체를 목킹합니다
        List mockedList = mock(List.class);
        // 한 번 아래와 같이 명령이 수행된 것을 기억하고 있다가
        mockedList.add("one");
        mockedList.clear();
        // 수행여부를 확인합니다
        verify(mockedList).add("one");
        verify(mockedList).clear();
    }

    @Test
    @DisplayName("리스트 스터빙 하고, 검증하기")
    void testStubbingLinkedList() {
        LinkedList mockedList = mock(LinkedList.class);
        // 스터빙(stubbing): 미리 준비된 답변을 제공하는 행위
        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenThrow(new RuntimeException());
        // 객체 반환을 확인 - 스터빙 하지 않은 경우는 널을 반환합니다
        assertEquals("first", mockedList.get(0));
        assertThrows(RuntimeException.class, () -> mockedList.get(1));
        assertNull(mockedList.get(999));
        // 마지막으로 수행된 작업을 검증합니다
        verify(mockedList).get(0);
    }

    boolean isValid(Object arg) {
        String str = arg.toString();
        return str != null && !str.isEmpty();
    }

    @Test
    @DisplayName("인자 매처를 통한 목킹")
    void testArgumentMatchers() {
        LinkedList mockedList = mock(LinkedList.class);
        // 임의의 정수값에 대한 목킹 테스트
        when(mockedList.get(anyInt())).thenReturn("element");
        assertEquals("element", mockedList.get(0));
        // 임의의 매칭함수를 이용하여 검증하는 방법
        when(mockedList.contains(argThat(o -> isValid(o)))).thenReturn(true);
        assertTrue(mockedList.contains(anyInt()));
        assertThrows(NullPointerException.class, () -> mockedList.contains(null));
        // 수행한 내역을 검증합니다
        verify(mockedList).get(anyInt());
        verify(mockedList).contains(anyInt());
        // 람다 매칭함수를 이용한 검증 방법
        mockedList.add("123456789");
        verify(mockedList).add(argThat(o -> o.toString().length() > 8));
    }

    @Test
    @DisplayName("호출 횟수를 검증")
    void testVerifyExactNumberOfInvocations() {
        LinkedList mockedList = mock(LinkedList.class);
        mockedList.add("once");
        mockedList.add("twice");
        mockedList.add("twice");
        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        verify(mockedList).add("once");
        verify(mockedList, times(1)).add("once");
        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");
        verify(mockedList, never()).add("never happend");

        verify(mockedList, atMostOnce()).add("once");
        verify(mockedList, atLeastOnce()).add("three times");
        verify(mockedList, atLeast(2)).add("three times");
        verify(mockedList, atMost(3)).add("three times");
    }

    @Test
    @DisplayName("호출된 메소드/목객체 순서를 검증")
    void testVerifyInOrder() {
        List singleMock = mock(List.class);
        singleMock.add("was added first");
        singleMock.add("was added second");

        InOrder inOrder = inOrder(singleMock);
        inOrder.verify(singleMock).add("was added first");
        inOrder.verify(singleMock).add("was added second");

        List firstMock = mock(List.class);
        List secondMock = mock(List.class);
        firstMock.add("was called first");
        secondMock.add("was called second");

        InOrder inOrders = inOrder(firstMock, secondMock);
        inOrders.verify(firstMock).add("was called first");
        inOrders.verify(secondMock).add("was called second");
    }

    @Test
    @DisplayName("예외를 던지는 목객체")
    void testThrowException(@Mock MemberService memberService) throws InvalidMemberException {
        doThrow(new InvalidMemberException("오류멤버")).when(memberService).validate(0L);
        assertThrows(InvalidMemberException.class, () -> memberService.validate(0L));
        // 0L 에 대해서만 예외를 호출하도록 스터빙하고, 다른 값은 아무런 동작도 하지 않습니다
        memberService.validate(1L);
    }

    @Test
    @DisplayName("순차적인 반환값 스터빙")
    void testConsecutiveCalls() {
        List mockList = mock(List.class);
        when(mockList.add(0))
                .thenThrow(new IllegalArgumentException(""))
                .thenReturn(true);
        // 처음 호출은 예외를 던지고, 두 번째는 성공하는 스터빙
        assertThrows(IllegalArgumentException.class, () -> mockList.add(0));
        assertTrue(mockList.add(0));
    }

}