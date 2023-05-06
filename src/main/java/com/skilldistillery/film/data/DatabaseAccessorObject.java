package com.skilldistillery.film.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.skilldistillery.film.entities.Actor;
import com.skilldistillery.film.entities.Film;
import com.skilldistillery.film.entities.InventoryItem;

@Component 
public class DatabaseAccessorObject implements DatabaseAccessor {

	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=US/Mountain";
	private static final String USER = "student";
	private static final String PWD = "student";

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Actor findActorById(int actorId) {
		Actor actor = null;
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PWD);
			String sql = "SELECT * FROM actor WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet actorResult = stmt.executeQuery();
			if (actorResult.next()) {
				String firstName = actorResult.getString("first_name");
				String lastName = actorResult.getString("last_name");
				actor = new Actor(actorId, firstName, lastName);
				actor.setFilms(findFilmsByActorId(actorId));
			}
			actorResult.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actor;
	}

	@Override
	public Actor createActor(Actor actor) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PWD);
			conn.setAutoCommit(false); // START TRANSACTION
			String sql = "INSERT INTO actor (first_name, last_name) " + " VALUES (?,?)";
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, actor.getFirstName());
			stmt.setString(2, actor.getLastName());
			int updateCount = stmt.executeUpdate();
			if (updateCount == 1) {
				ResultSet keys = stmt.getGeneratedKeys();
				if (keys.next()) {
					int newActorId = keys.getInt(1);
					actor.setId(newActorId);
					if (actor.getFilms() != null && actor.getFilms().size() > 0) {
						sql = "INSERT INTO film_actor (film_id, actor_id) VALUES (?,?)";
						stmt = conn.prepareStatement(sql);
						for (Film film : actor.getFilms()) {
							stmt.setInt(1, film.getFilmId());
							stmt.setInt(2, newActorId);
							updateCount = stmt.executeUpdate();
						}
					}
				}
			} else {
				actor = null;
			}
			conn.commit(); // COMMIT TRANSACTION
			stmt.close();
			if (!conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
					conn.close();
				} catch (SQLException sqle2) {
					System.err.println("Error trying to rollback");
				}
			}
			throw new RuntimeException("Error inserting actor " + actor);
		}
		return actor;
	}

	@Override
	public boolean saveActor(Actor actor) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PWD);
			conn.setAutoCommit(false); // START TRANSACTION
			String sql = "UPDATE actor SET first_name=?, last_name=? " + " WHERE id=?";
			int actorId = actor.getId();
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, actor.getFirstName());
			stmt.setString(2, actor.getLastName());
			stmt.setInt(3, actorId);
			int updateCount = stmt.executeUpdate();
			if (updateCount == 1) {
				// Replace actor's film list
				sql = "DELETE FROM film_actor WHERE actor_id = ?";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, actorId);
				updateCount = stmt.executeUpdate();
				sql = "INSERT INTO film_actor (film_id, actor_id) VALUES (?,?)";
				stmt = conn.prepareStatement(sql);
				for (Film film : actor.getFilms()) {
					stmt.setInt(1, film.getFilmId());
					stmt.setInt(2, actorId);
					updateCount = stmt.executeUpdate();
				}
				conn.commit(); // COMMIT TRANSACTION
				stmt.close();
				if (!conn.isClosed()) {
					conn.close();
				}
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
					conn.close();
				} // ROLLBACK TRANSACTION ON ERROR
				catch (SQLException sqle2) {
					System.err.println("Error trying to rollback");
				}
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteActor(Actor actor) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PWD);
			conn.setAutoCommit(false); // START TRANSACTION
			String sql = "DELETE FROM film_actor WHERE actor_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actor.getId());
			int updateCount = stmt.executeUpdate();
			sql = "DELETE FROM actor WHERE id = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actor.getId());
			updateCount = stmt.executeUpdate();
			conn.commit(); // COMMIT TRANSACTION
			stmt.close();
			if (!conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
					conn.close();
				} catch (SQLException sqle2) {
					System.err.println("Error trying to rollback");
				}
			}
			return false;
		}
		return true;
	}

	@Override
	public Film findFilmById(int filmId) {
		Film film = null;
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PWD);
			String sql = "SELECT * FROM film WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet filmResult = stmt.executeQuery();
			while (filmResult.next()) {
				String title = filmResult.getString("title");
				String desc = filmResult.getString("description");
				int releaseYear = filmResult.getInt("release_year");
				int langId = filmResult.getInt("language_id");
				String language = convertLanguage(langId);
				int rentDur = filmResult.getInt("rental_duration");
				double rate = filmResult.getDouble("rental_rate");
				int length = filmResult.getInt("length");
				double repCost = filmResult.getDouble("replacement_cost");
				String rating = filmResult.getString("rating");
				String features = filmResult.getString("special_features");
				String category = findCategory(filmId);
				film = new Film(filmId, title, desc, releaseYear, langId, language, rentDur, rate, length, repCost,
						rating, features, category);
				film.setCast(findActorsByFilmId(filmId));
				film.setInventoryItems(findInventoryByFilmId(filmId));
			}
			filmResult.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}
	
	@Override
	public Film createFilm(Film film) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PWD);
			conn.setAutoCommit(false); // START TRANSACTION
			String sql = "INSERT INTO film (title, description, release_year, language_id, rental_duration, "
					+ "rental_rate, length, replacement_cost, rating) "
					+ "VALUES (?,?,?,?,?,?,?,?,?)";
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, film.getTitle());
			stmt.setString(2, film.getDesc());
			stmt.setInt(3, film.getReleaseYear());
			stmt.setInt(4, 1);
			stmt.setInt(5, film.getRentDur());
			stmt.setDouble(6, film.getRate());
			stmt.setInt(7, film.getLength());
			stmt.setDouble(8, film.getRepCost());
			stmt.setString(9, film.getRating());
//			stmt.setString(10, film.getFeatures());
			int updateCount = stmt.executeUpdate();
			if (updateCount == 1) {
				ResultSet keys = stmt.getGeneratedKeys();
				if (keys.next()) {
					int newFilmId = keys.getInt(1);
					film.setFilmId(newFilmId);
					if (film.getCast() != null && film.getCast().size() > 0) {
						sql = "INSERT INTO film_actor (film_id, actor_id) VALUES (?,?)";
						stmt = conn.prepareStatement(sql);
						for (Actor actor : film.getCast()) {
							stmt.setInt(1, newFilmId);
							stmt.setInt(2, actor.getId());
							updateCount = stmt.executeUpdate();
						}
					}
				}
			} else {
				film = null;
			}
			conn.commit(); // COMMIT TRANSACTION
			stmt.close();
			if (!conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
					conn.close();
				} catch (SQLException sqle2) {
					System.err.println("Error trying to rollback");
				}
			}
			throw new RuntimeException("Error inserting film " + film);
		}
		return film;
	}

	@Override
	public boolean deleteFilm(int filmId) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PWD);
			conn.setAutoCommit(false); // START TRANSACTION
			String sql = "DELETE FROM film_actor WHERE film_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			int updateCount = stmt.executeUpdate();
			sql = "DELETE FROM film WHERE id = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			updateCount = stmt.executeUpdate();
			conn.commit(); // COMMIT TRANSACTION
			stmt.close();
			if (!conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
					conn.close();
				} catch (SQLException sqle2) {
					System.err.println("Error trying to rollback");
				}
			}
			return false;
		}
		return true;
	}
	
	@Override
	public boolean updateFilm(int filmId, Film userEditedFilm) {
		Connection conn = null;
		try {
			System.out.println(userEditedFilm.displayFullDetails());
			conn = DriverManager.getConnection(URL, USER, PWD);
			conn.setAutoCommit(false); // START TRANSACTION
			String sql = "UPDATE film SET title=?, description=?, release_year=?, rental_duration=?, "
					+ "rental_rate=?, length=?, replacement_cost=?, rating=? " + "WHERE id=?";
			Film existingFilm = findFilmById(filmId);
			PreparedStatement stmt = conn.prepareStatement(sql);
			if (userEditedFilm.getTitle() != null && !userEditedFilm.getTitle().equals("")) {
				stmt.setString(1, userEditedFilm.getTitle());
			}
			else {
				stmt.setString(1, existingFilm.getTitle());
			}
			if (userEditedFilm.getDesc() != null && !userEditedFilm.getDesc().equals("")) {
				stmt.setString(2, userEditedFilm.getDesc());
			}
			else {
				stmt.setString(2, existingFilm.getDesc());
			}
			if (userEditedFilm.getReleaseYear() != 0) {
				stmt.setInt(3, userEditedFilm.getReleaseYear());
			}
			else {
				stmt.setInt(3, existingFilm.getReleaseYear());
			}
			if (userEditedFilm.getRentDur() != 0) {
				stmt.setInt(4, userEditedFilm.getRentDur());
			}
			else {
				stmt.setInt(4, existingFilm.getRentDur());
			}
			if (userEditedFilm.getRate() != 0) {
				stmt.setDouble(5, userEditedFilm.getRate());
			}
			else {
				stmt.setDouble(5, existingFilm.getRate());
			}
			if (userEditedFilm.getLength() != 0) {
				stmt.setInt(6, userEditedFilm.getLength());
			}
			else {
				stmt.setInt(6, existingFilm.getLength());
			}
			if (userEditedFilm.getRepCost() != 0) {
				stmt.setDouble(7, userEditedFilm.getRepCost());
			}
			else {
				stmt.setDouble(7, existingFilm.getRepCost());
			}
			if (userEditedFilm.getRating() != null && !userEditedFilm.getRating().equals("")) {
				stmt.setString(8, userEditedFilm.getRating());
			}
			else {
				stmt.setString(8, existingFilm.getRating());
			}
			stmt.setInt(9, filmId);
			stmt.executeUpdate();
//			if (updateCount == 1) {
//				// Replace film's cast
//				sql = "DELETE FROM film_actor WHERE film_id = ?";
//				stmt = conn.prepareStatement(sql);
//				stmt.setInt(1, filmId);
//				updateCount = stmt.executeUpdate();
//				sql = "INSERT INTO film_actor (film_id, actor_id) VALUES (?,?)";
//				stmt = conn.prepareStatement(sql);
//				if (film.getCast() != null && film.getCast().size() > 0) {
//				for (Actor actor : film.getCast()) {
//					stmt.setInt(1, filmId);
//					stmt.setInt(2, actor.getId());
//					updateCount = stmt.executeUpdate();
//				} }
				conn.commit(); // COMMIT TRANSACTION
				stmt.close();
				if (!conn.isClosed()) {
					conn.close();
				}
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
					conn.close();
				} // ROLLBACK TRANSACTION ON ERROR
				catch (SQLException sqle2) {
					System.err.println("Error trying to rollback");
				}
			}
			return false;
		}
		return true;
	}


	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PWD);
			String sql = "SELECT actor.* " + "FROM actor " + "JOIN film_actor ON actor.id = film_actor.actor_id "
					+ "JOIN film ON film_actor.film_id = film.id " + "WHERE film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet castResult = stmt.executeQuery();
			while (castResult.next()) {
				int actorId = castResult.getInt("id");
				String firstName = castResult.getString("first_name");
				String lastName = castResult.getString("last_name");
				Actor actor = new Actor(actorId, firstName, lastName);
				actors.add(actor);
			}
			castResult.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actors;
	}

	@Override
	public List<Film> findFilmsByActorId(int actorId) {
		List<Film> films = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PWD);
			String sql = "SELECT film.* " + "FROM film " + "JOIN film_actor ON film.id = film_actor.film_id "
					+ "WHERE film_actor.actor_id = ? " + "ORDER BY film.release_year";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet filmographyResult = stmt.executeQuery();
			while (filmographyResult.next()) {
				int filmId = filmographyResult.getInt("id");
				Film film = findFilmById(filmId);
				films.add(film);
			}
			filmographyResult.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return films;
	}

	@Override
	public List<Film> findFilmsByKeyword(String keyword) {
		List<Film> films = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PWD);
			String sql = "SELECT * " + "FROM film " + "WHERE title LIKE ? " + "OR description LIKE ? "
					+ "ORDER BY title;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%" + keyword + "%");
			stmt.setString(2, "%" + keyword + "%");
			ResultSet keywordFilmResult = stmt.executeQuery();
			while (keywordFilmResult.next()) {
				int filmId = keywordFilmResult.getInt("id");
				Film film = findFilmById(filmId);
				films.add(film);
			}
			keywordFilmResult.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return films;
	}

	@Override
	public List<InventoryItem> findInventoryByFilmId(int filmId) {
		List<InventoryItem> inventoryItems = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PWD);
			String sql = "SELECT inventory_item.* " + "FROM film "
					+ "JOIN inventory_item ON film.id = inventory_item.film_id " + "WHERE film.id = ? "
					+ "ORDER BY inventory_item.id;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet inventoryResult = stmt.executeQuery();
			while (inventoryResult.next()) {
				int id = inventoryResult.getInt("id");
				int storeId = inventoryResult.getInt("store_id");
				String condition = inventoryResult.getString("media_condition");
				Timestamp timestamp = inventoryResult.getTimestamp("last_update");
				InventoryItem item = new InventoryItem(id, filmId, storeId, condition, timestamp);
				inventoryItems.add(item);
			}
			inventoryResult.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return inventoryItems;
	}

	@Override
	public String convertLanguage(int langId) {
		String language = null;
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PWD);
			String sql = "SELECT language.* " + "FROM film " + "JOIN language ON film.language_id = language.id "
					+ "WHERE language.id = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, langId);
			ResultSet languageResult = stmt.executeQuery();
			if (languageResult.next()) {
				language = languageResult.getString("name");
			}
			languageResult.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return language;
	}

	@Override
	public String findCategory(int filmId) {
		String category = null;
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PWD);
			String sql = "SELECT category.name " + "FROM film "
					+ "JOIN film_category ON film.id = film_category.film_id "
					+ "JOIN category ON film_category.category_id = category.id " + "WHERE film.id = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet categoryResult = stmt.executeQuery();
			if (categoryResult.next()) {
				category = categoryResult.getString("name");
			}
			categoryResult.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return category;
	}
}
