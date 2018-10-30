package com.seldetronics.abrosans.chroma;


public class Chroma_Device {
    public String Type = "Chromaticity";

    Chroma_Device(String Start_name) {
        Type = Start_name;
    }

    ;

    @Override
    public String toString() {
        return Type;
    }

}
