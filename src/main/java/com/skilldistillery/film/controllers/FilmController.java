package com.skilldistillery.film.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.skilldistillery.film.data.DatabaseAccessor;
import com.skilldistillery.film.entities.Film;

@Controller
public class FilmController {

	@Autowired
	private DatabaseAccessor dao;
	
	@RequestMapping(path = {"/", "home.do"})
	public String home(Model model) {
		return "home";
	}
	//list all films
	
	@RequestMapping(path = "GetFilmData.do", params = "idLookup", method = RequestMethod.GET)
	public ModelAndView findFilmById(int idLookup) {
		ModelAndView mv = new ModelAndView();
		Film film = dao.findFilmById(idLookup);
		mv.addObject("film", film);
		mv.setViewName("result");
		return mv;
	}
	
	@RequestMapping(path = "GetFilmData.do", params = "keyword", method = RequestMethod.GET)
	public ModelAndView findFilmsByKeyword(String keyword) {
		ModelAndView mv = new ModelAndView();
		List<Film> films = dao.findFilmsByKeyword(keyword);
		mv.addObject("films", films);
		mv.setViewName("results");
		return mv;
	}
	
	@RequestMapping(path = "NewFilm.do", method = RequestMethod.POST)
	public ModelAndView createFilm(Film film) {
		ModelAndView mv = new ModelAndView();
		Film dbFilm = dao.createFilm(film);
		mv.addObject("film", dbFilm);
		mv.setViewName("result");
		return mv;
	}
	
	@RequestMapping(path = "EditFilm.do", method = RequestMethod.POST)
	public ModelAndView saveFilm(Film film) {
		ModelAndView mv = new ModelAndView();
		boolean updated = dao.saveFilm(film);
		mv.addObject("updated", updated);
		mv.setViewName("updateResult");
		return mv;
	}
}
