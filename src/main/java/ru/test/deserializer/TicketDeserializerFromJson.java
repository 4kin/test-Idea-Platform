package ru.test.ideaplatform;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TicketDeserializerFromJson implements JsonDeserializer {

    private static Date getDate(JsonObject asJsonObject, String direction) {
//        String originString;
//        if (direction.equals("departure")){
//             originString = asJsonObject.get("origin").getAsString();
//        } else {
//            originString = asJsonObject.get("destination").getAsString();
//        }
//
//        TimeZone timeZone = TimeZone.getTimeZone(AeroportEnum.valueOf(originString).getTimeZone());
        String dateString = asJsonObject.get(direction + "_date").getAsString() + " " + asJsonObject.get(direction + "_time").getAsString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy hh:mm");
//        simpleDateFormat.setTimeZone(timeZone);
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

//        System.out.println(ticket);
        return ticket;
    }
}
