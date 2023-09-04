package ru.test.deserializer;

import com.google.gson.*;
import ru.test.model.Ticket;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TicketDeserializerFromJson implements JsonDeserializer {

    private static Date getDate(JsonObject asJsonObject, String direction) {

        String dateString = asJsonObject.get(direction + "_date").getAsString() + " " + asJsonObject.get(direction + "_time").getAsString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");
        Date parseDate = null;
        try {
            parseDate = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return parseDate;
    }

    @Override
    public Object deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject asJsonObject = jsonElement.getAsJsonObject();
        Ticket ticket = new Ticket();
        ticket.setOrigin(asJsonObject.get("origin").getAsString());
        ticket.setOriginName(asJsonObject.get("origin_name").getAsString());
        ticket.setDestination(asJsonObject.get("destination").getAsString());
        ticket.setDestinationName(asJsonObject.get("destination_name").getAsString());
        ticket.setDepartureDateTime(getDate(asJsonObject, "departure"));
        ticket.setArrivalDateTime(getDate(asJsonObject, "arrival"));
        ticket.setCarrier(asJsonObject.get("carrier").getAsString());
        ticket.setStops(asJsonObject.get("stops").getAsInt());
        ticket.setPrice(asJsonObject.get("price").getAsInt());

        return ticket;
    }
}
