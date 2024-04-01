package com.poc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.poc.DTO.Dashboard;
import com.poc.entity.Counsellor;
import com.poc.service.CounsellorService;
import com.poc.service.EnquiryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CounsellorController {
	
	@Autowired
	private CounsellorService counsellorService;
	
	@Autowired
	private EnquiryService enqService;
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest req,Model model) {
		HttpSession session=req.getSession(false);//get session
		session.invalidate();
		return "redirect:/";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("counsellor", new Counsellor());
		return "registerView";
	}
	@PostMapping("/register")
	public String handleRegister(Counsellor c,Model model) {
		boolean status=counsellorService.saveCounsellor(c);
		if(status) {
			model.addAttribute("smsg", "counsellor saved");
		}else {
				model.addAttribute("emsg", "falied to save");
			}
			return "registerView";
		}
	
	@GetMapping("/")
	public String login(Model model) {
		model.addAttribute("counsellor",new Counsellor());
		return "index";
	}
	
	@PostMapping("/login")
	public String handleLogin(Counsellor counsellor,HttpServletRequest req,  Model model) {
		
		Counsellor c=counsellorService.getCounsellor(counsellor.getEmail(),counsellor.getPwd());
		
		if(c==null) {
			model.addAttribute("emsg", "Invalid credentials");
			return "index";
		}else {
			//set counsellor-Id in session
			HttpSession session =req.getSession(true);//always new session
			session.setAttribute("cid", c.getCounsellorId());
			
			Dashboard dbinfo=enqService.getDashboardInfo(c.getCounsellorId());
			model.addAttribute("dashboard",	dbinfo);
			return "dashboard";
		}	
	}

}
