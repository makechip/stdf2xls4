package com.makechip.stdf2xls4.stdf;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StdfAPI
{
	private String filename;

	public StdfAPI(String filename)
	{
		this.filename = filename;
	}
	
	public Stream<StdfRecord> stream() 
	{ 
		StdfReader rdr = new StdfReader(filename);
		rdr.read();
		return(rdr.stream().map(RecordBytes::createRecord).collect(Collectors.toList()).stream());
	}
	
	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			System.out.println("Usage: java com.makechip.stdf2xls.stdf.StdfAPI <stdfFile>");
			System.exit(1);
		}
		StdfAPI api = new StdfAPI(args[0]);
	    api.stream().forEach(rec -> System.out.println(rec.toString()));
	}

}
