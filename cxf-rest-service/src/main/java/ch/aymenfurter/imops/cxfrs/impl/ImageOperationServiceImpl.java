package ch.aymenfurter.imops.cxfrs.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import ch.aymenfurter.imops.cxfrs.ImageOperationService;
import ch.aymenfurter.imops.cxfrs.pojo.ImageOperationResponse;

public class ImageOperationServiceImpl implements ImageOperationService {
	final static Logger LOG = Logger.getLogger(ImageOperationServiceImpl.class);
	
	public Response getEnhancedImage(String base64Image) {
		Response response = null;

		synchronized (this) {
			File sourceFile = null;
			File fileScaled = null;
			File fileRepaired = null;
			ImageOperationResponse resp = new ImageOperationResponse();
			byte[] decodedImg = Base64.getDecoder().decode(base64Image.getBytes(StandardCharsets.UTF_8));
			String respBase64 = "";
			try {
				sourceFile = File.createTempFile("img", ".png", new File("/tmp"));
				FileUtils.writeByteArrayToFile(sourceFile, decodedImg);

				if (sourceFile.length() <= 100000) {
				
					String sourceFileURL = sourceFile.getAbsolutePath();
					Runtime r = Runtime.getRuntime();
					Process p = r.exec("python3 /opt/apps/neuralenhance/neural-enhance-master/enhance.py --type=photo --zoom=4  " + sourceFileURL);
					String fileNameScaled4x = FilenameUtils.removeExtension(sourceFileURL);
					fileNameScaled4x = fileNameScaled4x  + "_ne4x.png";
					
					p.waitFor();
					Process p2 = r.exec("python3 /opt/apps/neuralenhance/neural-enhance-master/enhance.py --type=photo --zoom=1 --model=repair  " + fileNameScaled4x);
					String fileNameRepaired = FilenameUtils.removeExtension(sourceFileURL);
					fileNameRepaired = fileNameRepaired  + "_ne4x_ne1x.png";
					p2.waitFor();
					fileRepaired = new File(fileNameRepaired);
					fileScaled = new File(fileNameScaled4x);
					byte[] encoded = Base64.getEncoder().encode((FileUtils.readFileToByteArray(fileRepaired)));
					respBase64 = new String(encoded, StandardCharsets.UTF_8);
				} else {
					throw new IllegalArgumentException();
				}
			} catch (IOException | InterruptedException e) {
				LOG.error(e);
			} finally {
				sourceFile.delete();
				fileRepaired.delete();
				fileScaled.delete();
			}

			resp.setMessage("OK");
			resp.setImg(respBase64);
			response = Response.ok(resp).status(Status.OK).build();
		}

		return response;
	}
}
