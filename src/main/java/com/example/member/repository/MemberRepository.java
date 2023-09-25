package com.example.member.repository;

import com.example.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
// @Repository를 쓰지 않고 jpa래파지토리를 상속받아서 따로 사용하지 않은 자동으로 repository가 적용이 된다.
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // 실행문구가 없는 추상 매서드만 정의할 수 있다.(interface)
    // jpa에서 제공하는 자동완성 문법에 맞춰서 사용해야한다. 대소문자 유의 인공지능이 아님을 명시 기존 규칙에 맞게끔 설정
    Optional<MemberEntity> findByMemberEmail(String memberEmail);

    // select  * from member_table where member_email = ? and member_password = ?
    Optional<MemberEntity> findByMemberEmailAndMemberPassword(String memberEmail, String memberPassword);
}
