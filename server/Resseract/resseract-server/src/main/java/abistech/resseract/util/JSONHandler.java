package abistech.resseract.util;

import abistech.resseract.exception.CustomErrorReports;
import abistech.resseract.exception.ResseractException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public class JSONHandler {

    public static String serialize(Object input) throws ResseractException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            return objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new ResseractException(CustomErrorReports.INVALID_PARAMETERS, e);
        }
    }

    public static <T> T deserialize(String input, Class<T> clazz) throws ResseractException {
        try {
            if (input == null)
                return null;
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            return objectMapper.readValue(input, clazz);
        } catch (IOException e) {
            throw new ResseractException(CustomErrorReports.INVALID_PARAMETERS, e);
        }
    }

}

