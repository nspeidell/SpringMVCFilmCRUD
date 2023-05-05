package com.skilldistillery.film.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skilldistillery.film.data.DatabaseAccessor;

@Controller
public class FilmController {

	@Autowired
	private DatabaseAccessor dao;
	
	@RequestMapping(path = {"/", "home.do"})
	public String home(Model model) {
		return "WEB-INF/home.jsp";
	}
	//list all films
	
}
