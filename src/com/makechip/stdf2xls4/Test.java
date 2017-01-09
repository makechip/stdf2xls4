package com.makechip.stdf2xls4;

import java.util.LinkedHashMap;

import com.makechip.util.Log;

public class Test
{

    public static void main(String[] args)
    {
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        m.put("A", "A");
        m.put("E", "E");
        m.put("B", "B");
        m.put("C", "C");
        m.put("D", "D");
        m.put("B", "B");
        
        for (String s: m.keySet())
        {
            Log.msg("key = " + s);
        }

    }

}
