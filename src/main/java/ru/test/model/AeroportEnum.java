package ru.test.model;

public enum AeroportEnum {
    VVO("Владивосток", "Etc/GMT+7"),
    TLV("Тель-Авив", "Etc/GMT+3"),
    LRN("Ларнака", "Etc/GMT+3"),
    UFA("Уфа","Etc/GMT+5");

    private final String city;
    private final String timeZone;

    private AeroportEnum(String city, String timeZone) {
        this.city = city;
        this.timeZone = timeZone;
    }

    public String getCity() {
        return city;
    }

    public String getTimeZone() {
        return timeZone;
    }


}
