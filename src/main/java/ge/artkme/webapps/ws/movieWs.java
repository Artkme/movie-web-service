package ge.artkme.webapps.ws;

import ge.artkme.webapps.api.MoviesApi;
import ge.artkme.webapps.exception.DBManagerException;
import ge.artkme.webapps.exception.MovieAlreadyExistsException;
import ge.artkme.webapps.exception.MovieNotFoundException;
import ge.artkme.webapps.model.Movie;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/movies")
public class movieWs implements MoviesWsDao {

    @Override
    public Response getMovie(String imdbId) {
        try (MoviesApi api = new MoviesApi()) {
            Movie movie = api.getMovie(imdbId);
            return Response.status(Response.Status.OK).entity(movie).build();
        } catch (MovieNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (DBManagerException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response addMovie(Movie movie) {
        try (MoviesApi api = new MoviesApi()) {
            api.addMovie(movie);
            return Response.status(Response.Status.OK).entity("Movie added successfully").build();
        } catch (MovieAlreadyExistsException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (DBManagerException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response updateMovie(Movie movie) {
        try (MoviesApi api = new MoviesApi()) {
            api.updateMovie(movie);
            return Response.status(Response.Status.OK).entity("Movie updated successfully").build();
        } catch (MovieNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (DBManagerException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response deleteMovie(String imdbId) {
        try (MoviesApi api = new MoviesApi()) {
            api.deleteMovie(imdbId);
            return Response.status(Response.Status.OK).entity("Movie deleted successfully").build();
        } catch (MovieNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (DBManagerException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
