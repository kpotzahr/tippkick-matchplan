package com.capgemini.csd.tippkick.spielplan.domain;

public enum Participant {
    EGY("Egypt"),
    ARG("Argentina"),
    AUS("Australia"),
    BEL("Belgium"),
    BRA("Brasil"),
    CRC("Costa Rica"),
    DEN("Denmark"),
    GER("Germany"),
    ENG("England"),
    FRA("France"),
    IRN("Iran"),
    ISL("Iceland"),
    JPN("Japan"),
    COL("Colombia"),
    CRO("Croatia"),
    MAR("Marocco"),
    MEX("Mexico"),
    NGA("Nigeria"),
    PAN("Panama"),
    PER("Peru"),
    POL("Poland"),
    POR("Portugal"),
    RUS("Russia"),
    KSA("Saudi Arabia"),
    SWE("Sweden"),
    SUI("Suisse"),
    SEN("Senegal"),
    SRB("Serbia"),
    ESP("Spain"),
    KOR("Korea"),
    TUN("Tunesia"),
    URU("Uruguay");

    private final String name;

    Participant(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
