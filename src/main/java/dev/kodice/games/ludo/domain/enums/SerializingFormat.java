package dev.kodice.games.ludo.domain.enums;

public enum SerializingFormat {

    DATE("MM/dd/yyyy"),
    DATE_HYPHEN("MM-dd-yyyy"),
    DATE_TIME("MM/dd/yyyy HH:mm:ss"),
	TIME("HH:mm:ss a");

    private String format;


    SerializingFormat(final String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

}