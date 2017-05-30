package ch.aymenfurter.imops.cxfrs;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface ImageOperationService {

	@POST
	@Path(value = "/enhance/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEnhancedImage(@FormParam("base64Image") String base64Image);
	
}
