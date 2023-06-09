package com.skilldistillery.film.controllers;

import java.lang.ProcessBuilder.Redirect;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	@RequestMapping(path = {"editFilmForm.do"}, method= RequestMethod.GET)
public ModelAndView editFilmForm(int filmId) {
		Film film = dao.findFilmById(filmId);
		ModelAndView mv = new ModelAndView();
		mv.addObject("film", film);
		mv.setViewName ("editFilm");		
	return mv;
}
	
	@RequestMapping(path = {"SubmitEditFilm.do"}, params = "filmId", method= RequestMethod.POST)
	public ModelAndView editFilm(ModelAndView mv, Film film, int filmId, RedirectAttributes redir) {
		Film userEditedFilm = film;
		boolean updated = dao.updateFilm(filmId, userEditedFilm);
		System.out.println(updated);
		mv.addObject("updated", updated);
//		model.setViewName("updateResult");
		redir.addFlashAttribute("updated", updated);
		mv.setViewName ("redirect:updateFilm.do");
		return mv;
	}

	@RequestMapping(path = {"updateFilm.do"}, method= RequestMethod.GET)
	public ModelAndView editFormRoute() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("updateResult");
		return mv;
	}
	
	@RequestMapping(path = {"deleteFilm.do"}, params = "filmId", method= RequestMethod.GET)
	public ModelAndView deleteFilm(int filmId) {
		ModelAndView mv = new ModelAndView();
		boolean deleted = dao.deleteFilm(filmId);
		System.out.println(deleted);
		mv.addObject("deleted", deleted);
		mv.setViewName("deleteResult");
		return mv;
	}
	
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
}