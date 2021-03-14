package com.breader.cubeprovider.formatter;

import com.breader.cubeprovider.model.Cube;
import com.breader.cubeprovider.model.JsonFormatter;
import com.breader.cubeprovider.model.Point;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JsonFormatterImpl implements JsonFormatter {
    @Override
    public String toJson(Cube c) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            List<Point> takenPoints = c.getTakenPoints();
            return mapper.writeValueAsString(takenPoints);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
