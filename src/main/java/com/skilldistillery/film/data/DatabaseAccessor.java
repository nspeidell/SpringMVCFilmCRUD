package com.skilldistillery.film.data;

import java.util.List;

import com.skilldistillery.film.entities.*;

public interface DatabaseAccessor {
	
	Actor findActorById(int actorId);
	Actor createActor(Actor actor);	
	boolean saveActor(Actor actor);
	boolean deleteActor(Actor actor);
	
	Film findFilmById(int filmId);	
	Film createFilm(Film film);
	boolean deleteFilm(Film film);
	boolean updateFilm(int filmId, Film film);

	List<Actor> findActorsByFilmId(int filmId);
	
	List<Film> findFilmsByActorId(int actorId);
	List<Film> findFilmsByKeyword(String keyword);

	List<InventoryItem> findInventoryByFilmId(int filmId);

	String convertLanguage(int langId);

	String findCategory(int filmId);

}
