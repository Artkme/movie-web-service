package ge.artkme.webapps.ws;

import ge.artkme.webapps.model.Movie;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/movies")
public interface MoviesWsDao {

    @GET
    @Path("/getinfo")
    @Produces(MediaType.APPLICATION_JSON)
    Response getMovie(@QueryParam("imdbId") String imdbId);

    @POST
    @Path("/addmovie")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response addMovie(Movie movie);

    @PUT
    @Path("/updatemovie")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateMovie(Movie movie);

    @DELETE
    @Path("/deletemovie")
    Response deleteMovie(@QueryParam("imdbId") String imdbId);

}
