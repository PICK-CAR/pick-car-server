package dev.bang.pickcar.member.repository;

import static dev.bang.pickcar.member.entity.QMember.member;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.bang.pickcar.member.dto.MemberResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    public boolean existsByEmail(String email) {
        return queryFactory.selectOne()
                .from(member)
                .where(member.email.eq(email))
                .fetchFirst() != null;
    }

    public Optional<MemberResponse> findMemberResponseById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(MemberResponse.class,
                                member.id,
                                member.email,
                                member.name,
                                member.nickname,
                                member.phoneNumber,
                                member.birthDay
                        ))
                        .from(member)
                        .where(member.id.eq(id))
                        .fetchOne()
        );
    }
}
