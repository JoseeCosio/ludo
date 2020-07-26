package dev.kodice.games.ludo.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import dev.kodice.games.ludo.domain.enums.SerializingFormat;

public class JsonLocalDateTimeSerializer extends JsonSerializer<LocalDateTime>  {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SerializingFormat.DATE_TIME.getFormat());

    @Override
    public void serialize(LocalDateTime date, final JsonGenerator gen, final SerializerProvider provider)
            throws IOException, JsonProcessingException {

        if (date == null) {
            gen.writeString(StringUtils.EMPTY);
        }
        else {
            gen.writeString(date.format(formatter));
        }
    }
}
