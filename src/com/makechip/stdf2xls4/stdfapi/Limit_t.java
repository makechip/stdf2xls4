package com.makechip.stdf2xls4.stdfapi;

public enum Limit_t
{
    LO_LIMIT("LoLimit"),
    HI_LIMIT("HiLimit");
    
    public final String name;
    
    private Limit_t(String name) { this.name = name; }
}
