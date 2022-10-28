package com.jyjportfolio.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jyjportfolio.service.EmailService;
import com.jyjportfolio.service.TistoryService;

@Controller
public class HomeController {
	
	@Autowired
	private TistoryService tistoryService;
	
	@Autowired
	private EmailService emailService;
	
    @GetMapping("/")
    public String index(HttpServletRequest request) {

    	
    	try {
    		 request.setAttribute("posts", tistoryService.getPosts());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
  	return "introduce/index";
    }
    

    @SuppressWarnings("unchecked")
	@PostMapping({"/sendMail"})
    public String sendMail(Model model, @RequestParam String email, @RequestParam String message) {
    	
    	
    	try {
    		emailService.sendEmail(email, message);
    		
		} catch (Exception e) {
			e.printStackTrace();
			
		}
    	model.addAttribute("msg",true);
    	return "introduce/index :: #msgSubmit";
    }
   

}
