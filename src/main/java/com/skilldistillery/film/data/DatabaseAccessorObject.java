package com.skilldistillery.film.data;

import java.util.List;

import org.springframework.stereotype.Component;

import com.skilldistillery.film.entities.Actor;
import com.skilldistillery.film.entities.Film;

@Component 
public class DatabaseAccessorObject implements DatabaseAccessor {

	@Override
	public Film findFilmById(int filmId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Actor findActorById(int actorId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Film> findFilmByKeyword(String filmKeyword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Actor createActor(Actor actor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveActor(Actor actor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteActor(Actor actor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Film createFilm(Film film) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Film updateFilm(int filmId, Film film) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteFilm(int filmId) {
		// TODO Auto-generated method stub
		return false;
	}

}
