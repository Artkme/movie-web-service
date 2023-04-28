package ge.artkme.webapps.api;

import ge.artkme.webapps.exception.DBManagerException;
import ge.artkme.webapps.exception.MovieAlreadyExistsException;
import ge.artkme.webapps.exception.MovieNotFoundException;
import ge.artkme.webapps.model.Movie;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class MoviesApi extends DBManager {

    public MoviesApi() throws DBManagerException {
        super();
    }

    private enum Statement implements SqlStatement {

        GET_MOVIE("{?=call movieApi.getMovie(?)}"),
        DELETE_MOVIE("{call movieApi.deleteMovie(?)}"),
        ADD_MOVIE("{call movieApi.addMovie(?, ?)}"),
        UPDATE_MOVIE("{call movieApi.updateMovie(?, ?)}");

        private final String sql;

        Statement(String sql) {
            this.sql = sql;
        }

        @Override
        public String getSql() {
            return sql;
        }
    }

    public Movie getMovie(String imdbId) throws DBManagerException, MovieNotFoundException {
        Movie movie = new Movie(imdbId, null);
        try {
            CallableStatement cstmt = getStatement(Statement.GET_MOVIE);
            cstmt.registerOutParameter(1, Types.VARCHAR);
            cstmt.setString(2, imdbId);
            cstmt.execute();

            movie.setTitle(cstmt.getString(1));
            return movie;
        } catch (SQLException e) {
            if (e.getErrorCode() == 20001) {
                throw new MovieNotFoundException("Movie doesn't exist in database with imdbId: " + movie.getImdbId());
            }
            throw new RuntimeException(e);
        }
    }

    public void addMovie(Movie movie) throws DBManagerException, MovieAlreadyExistsException {
        try {
            CallableStatement cstmt = getStatement(Statement.ADD_MOVIE);
            cstmt.setString(1, movie.getImdbId());
            cstmt.setString(2, movie.getTitle());
            cstmt.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == 20001) {
                throw new MovieAlreadyExistsException("movie already exists in database with imdbId: " + movie.getImdbId());
            }
            throw new RuntimeException(e);
        }
    }

    public void updateMovie(Movie movie) throws DBManagerException, MovieNotFoundException {
        try {
            CallableStatement cstmt = getStatement(Statement.UPDATE_MOVIE);
            cstmt.setString(1, movie.getImdbId());
            cstmt.setString(2, movie.getTitle());
            cstmt.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == 20001) {
                throw new MovieNotFoundException("Movie doesn't exist in database with imdbId: " + movie.getImdbId());
            }
            throw new RuntimeException(e);
        }
    }

    public void deleteMovie(String imdbId) throws DBManagerException, MovieNotFoundException {
        try {
            CallableStatement cstmt = getStatement(Statement.DELETE_MOVIE);
            cstmt.setString(1, imdbId);
            cstmt.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == 20001) {
                throw new MovieNotFoundException("Can't delete movie from database, because movie doesn't exist in database with imdbId: " + imdbId);
            }
            throw new RuntimeException(e);
        }
    }
}
