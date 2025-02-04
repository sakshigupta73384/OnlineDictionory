package com.app.dictapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.dictapp.dto.AdminLoginDto;
import com.app.dictapp.dto.DictionaryDto;
import com.app.dictapp.model.AdminLogin;
import com.app.dictapp.model.Dictionary;
import com.app.dictapp.service.AdminLoginRepo;
import com.app.dictapp.service.DictionaryRepo;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@Autowired
	AdminLoginRepo alrepo;
	
	@Autowired
	DictionaryRepo drepo;

	@GetMapping("/")
	public String showIndex(Model model) {
		DictionaryDto dto=new DictionaryDto();
		model.addAttribute("dto", dto);
		return "index";
	}
	
	@PostMapping("/")
	public String search(@ModelAttribute DictionaryDto dto, RedirectAttributes attrib) {
		Dictionary d=drepo.getByWord(dto.getWord());
		attrib.addFlashAttribute("d", d);
		return "redirect:/";
	}
	
	@GetMapping("/adminlogin")
	public String showAdminLogin(Model model) {
		AdminLoginDto dto = new AdminLoginDto();
		model.addAttribute("dto", dto);
		return "adminlogin";
	}

	@PostMapping("/adminlogin")
	public String validate(@ModelAttribute AdminLoginDto dto, RedirectAttributes attrib,HttpSession session) {
		try {
			AdminLogin al = alrepo.findById(dto.getAdminid()).get();
			if (al != null) {
				if (al.getPassword().equals(dto.getPassword())) {
					//attrib.addFlashAttribute("msg", "Valid User");
					session.setAttribute("adminid", al.getAdminid());
					return "redirect:/admin/adminhome";
					} else {
					attrib.addFlashAttribute("msg", "Invalid User");
				}
			}
		} catch (Exception ex) {
			attrib.addFlashAttribute("msg", "Admin does not exist");
		}
		return "redirect:/adminlogin";
	}
}
