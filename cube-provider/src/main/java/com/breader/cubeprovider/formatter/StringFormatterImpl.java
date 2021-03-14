package com.breader.cubeprovider.formatter;

import com.breader.cubeprovider.model.Cube;
import com.breader.cubeprovider.model.StringFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StringFormatterImpl implements StringFormatter {
    private static final Logger logger = LoggerFactory.getLogger(StringFormatterImpl.class);

    @Override
    public String asString(Cube c) {
        logger.info(c.toString());
        return c.toString().replaceAll("\n", "<br/>");
    }
}
