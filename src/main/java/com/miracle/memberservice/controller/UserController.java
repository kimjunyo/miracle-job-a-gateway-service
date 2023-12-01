package com.miracle.memberservice.controller;

import com.miracle.memberservice.dto.request.ResumeRequestDto;
import com.miracle.memberservice.dto.response.JobResponseDto;
import com.miracle.memberservice.dto.response.ResumeListResponseDto;
import com.miracle.memberservice.service.AdminService;
import com.miracle.memberservice.service.UserService;
import com.miracle.memberservice.util.PageMoveWithMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AdminService adminService;

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    // [임시] 일반공고 상세페이지 이동
    @GetMapping("/normalPost")
    public String mzPostDatail(){ return "user/normal-post"; }

    // [임시] 이력서 생성 폼으로 이동
    @GetMapping("/resume/form")
    public String createResume(HttpSession session, Model model){
        PageMoveWithMessage pmwm = userService.formResume(session);
        Map<String, List<?>> allJobsAndStacks = adminService.getAllJobsAndStacks(session);
        model.addAttribute("info", pmwm.getData());
        model.addAttribute("jobs", allJobsAndStacks.get("jobs"));
        model.addAttribute("stacks", allJobsAndStacks.get("stacks"));
        return pmwm.getPageName();
    }

    // 이력서 생성
    @PostMapping("/resume")
    public String addResume(RedirectAttributes redirectAttributes, HttpSession session, ResumeRequestDto resumeRequestDto){
        PageMoveWithMessage pmwm = userService.addResume(session, resumeRequestDto);
        redirectAttributes.addAttribute("errorMessage", pmwm.getErrorMessage());
        return pmwm.getPageName();
    }

    // [임시] 이력서 목록으로 이동
    @GetMapping("/resumes")
    public String resumeList(HttpSession session, Model model){
        PageMoveWithMessage pmwm = userService.resumeList(session);

        List<JobResponseDto> jobs = adminService.getAllJobs(session);
        model.addAttribute("resumeList", pmwm.getData());
        model.addAttribute("jobs",jobs);
        model.addAttribute("errorMessage", pmwm.getErrorMessage());

        return pmwm.getPageName();
    }

    // [임시] 자소서 목록으로 이동
    @GetMapping("/cover-letter")
    public String covetLetterList(){ return "user/cover-letter"; }

    @GetMapping("/coverLetterForm")
    public String createCoverLetter(){ return "user/coverLetterForm"; }
}
