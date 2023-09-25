package com.example.member.controller;

import com.example.member.dto.MemberDTO;
import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/member/save")
    public String save(){
        return "memberPages/memberSave";
    }
    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO){
        memberService.save(memberDTO);
        return "/memberPages/memberLogin";
    }

    @GetMapping("/member/login")
    public String login(){
        return "memberPages/memberLogin";
    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session){
        MemberDTO memberDTO1 = memberService.login(memberDTO);
        if(memberDTO1 != null){
            session.setAttribute("loginEmail", memberDTO1.getMemberEmail());
            return "/memberPages/memberMain";
        }else{
            return "memberPages/memberLogin";
        }


    }

    @GetMapping("/members")
    public String findAll(Model model){
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "/memberPages/memberList";
    }
    @GetMapping("/member/{id}")
    public String findById(@PathVariable("id") Long id, Model model){
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "memberPages/memberDetail";
    }

    @GetMapping("/member/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        memberService.deleteById(id);
        return "redirect:/members";
    }
    @GetMapping("/member/update/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model){
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "/memberPages/memberUpdate";
    }
    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO){
        memberService.update(memberDTO);
        return "redirect:/members";
    }

}
