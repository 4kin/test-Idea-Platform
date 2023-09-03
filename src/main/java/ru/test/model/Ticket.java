package ru.test.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Ticket {
    private String  origin;
    private String originName;
    private String  destination;
    private String destinationName;
    private Date departureDateTime;
    private Date arrivalDateTime;
    private String carrier;
    private Integer stops;
    private Integer price;
}
