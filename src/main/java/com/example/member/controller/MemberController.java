package com.example.member.controller;

import com.example.member.dto.MemberDTO;
import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/save")
    public String save(){
        return "memberPages/memberSave";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO){
        memberService.save(memberDTO);
        return "/memberPages/memberLogin";
    }
    @PostMapping("/dup-check")
    public ResponseEntity emailCheck(@RequestBody MemberDTO memberDTO){
        boolean result = memberService.emailCheck(memberDTO.getMemberEmail());
        if(result){
            return new ResponseEntity<>("사용가능", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("사용불가능", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "redirectURI", defaultValue = "/member/mypage") String redirectURI,
                        Model model){
        model.addAttribute("redirectURI", redirectURI);
        return "memberPages/memberLogin";
    }

//    @PostMapping("/login")
//    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session,
//                        @RequestParam("redirectURI") String redirectURI){
//        MemberDTO memberDTO1 = memberService.login(memberDTO);
//        if(memberDTO1 != null){
//            session.setAttribute("loginEmail", memberDTO1.getMemberEmail());
////            return "/memberPages/memberMain";
//            // 사용자가 로그인 성공하면, 직전에 요청한 페이지로 이동시킴
//            // 별도로 요청한 페이지가 없다면 정상적으로 myPage로 이동시킴.(redirect:/member/mypage)
//            return "redirect:" + redirectURI;
//        }else{
//            return "memberPages/memberLogin";
//        }
//    }
    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session,
                        @RequestParam("redirectURI") String redirectURI) {
        boolean loginResult = memberService.login(memberDTO);
        if (loginResult) {
            session.setAttribute("loginEmail", memberDTO.getMemberEmail());
//            return "memberPages/memberMain";
            // 사용자가 로그인 성공하면, 직전에 요청한 페이지로 이동시킴.
            // 별도로 요청한 페이지가 없다면 정상적으로 myPage로 이동시킴.(redirect:/member/mypage)
            return "redirect:" + redirectURI;
        } else {
            return "memberPages/memberLogin";
        }
    }
    @GetMapping("/mypage")
    public String myPage(){
        return "/memberPages/memberMain";
    }
    @GetMapping
    public String findAll(Model model){
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "/memberPages/memberList";
    }
//    @GetMapping("/{id}")
//    public String findById(@PathVariable("id") Long id, Model model){
//        MemberDTO memberDTO = memberService.findById(id);
//        model.addAttribute("member", memberDTO);
//        return "memberPages/memberDetail";
//    }
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        try {
            MemberDTO memberDTO = memberService.findById(id);
            model.addAttribute("member", memberDTO);
            return "memberPages/memberDetail";
        } catch (NoSuchElementException e) {
            return "memberPages/NotFound";
        } catch (Exception e) {
            return "memberPages/NotFound";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        memberService.deleteById(id);
        return "redirect:/members";
    }
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model){
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "/memberPages/memberUpdate";
    }
    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO, HttpSession session){
        memberService.update(memberDTO);
        session.removeAttribute("loginEmail");
        return "redirect:/members";
    }
    // update (axios)
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody MemberDTO memberDTO, HttpSession session) {
        memberService.update(memberDTO);
        session.removeAttribute("loginEmail");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/axios/{id}")
    public @ResponseBody MemberDTO detail(@PathVariable("id") Long id){
        MemberDTO memberDTO = memberService.findById(id);
        if(memberDTO != null){
            return memberDTO;
        }else{
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAxios(@PathVariable("id") Long id) {
        memberService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
