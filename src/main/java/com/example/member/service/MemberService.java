package com.example.member.service;

import com.example.member.dto.MemberDTO;
import com.example.member.entity.MemberEntity;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void save(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toSaveEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    public MemberDTO login(MemberDTO memberDTO) {
        Optional<MemberEntity> memberEntity =memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if(memberEntity.isPresent()){
            MemberEntity memberEntity1 = memberEntity.get();
            if(memberEntity1.getMemberPassword().equals(memberDTO.getMemberPassword())){
                MemberDTO memberDTO1 = MemberDTO.toSaveDTO(memberEntity1);
                return memberDTO1;
            }else {
                return null;
            }
        }else{
            return null;
        }
//        Optional<MemberEntity> memberEntity1 =memberRepository.
//                findByMemberEmailAndMemberPassword(memberDTO.getMemberEmail(), memberDTO.getMemberPassword());
//        if(memberEntity1.isPresent()){
//            return true;
//        }else{
//            return false;
//        }
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for(MemberEntity memberEntity : memberEntityList){
            MemberDTO memberDTO = MemberDTO.toSaveDTO(memberEntity);
            memberDTOList.add(memberDTO);
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        Optional<MemberEntity> byId = memberRepository.findById(id);
        if(byId.isPresent()){
            return MemberDTO.toSaveDTO(byId.get());
        }
        return null;
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    public void update(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toUpdateEntity(memberDTO);
        memberRepository.save(memberEntity);
    }



}
