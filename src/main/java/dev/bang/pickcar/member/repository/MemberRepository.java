package dev.bang.pickcar.member.repository;

import dev.bang.pickcar.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);
}
