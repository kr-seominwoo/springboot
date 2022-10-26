package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {

    // 실무에서는 동시성문제 때문에 concurrentMap을 쓴다고 한다
    private static Map<Long, Member> store = new HashMap<>();
    // long 또한 동시성문제 때문에 AtomicLong(?)을 사용해야 한다고 한다
    private static long sequence = 0L; // 키값 생성하기 위한 시퀀스

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        // store에 없는 경우 null이 반환되기 때문에 요즘은
        // null이 반환될 수 있는 경우 Optional로 감싸서 반환한다고 한다
        // 그러면 client에서 따로 처리할 수 있는 것이 있다고 한다
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
        // stream은 데이터가 들어오는 것으로 loop를 돌린다고 생각하면된다
        // filter에서 들어오는 것으로 처리할 함수를 쓴다
        // 여기서는 getName()으로 equals를 사용하여 name이랑 같은지 확인한다
        // 그래서 같은 것이 filter가 되고 findAny()는 찾은 것을 하나라도 반환을 한다
        // 없으면 Optional에 null이 포함되서 반환이 된다
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
