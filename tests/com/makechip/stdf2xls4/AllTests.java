package com.makechip.stdf2xls4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.makechip.stdf2xls4.stdf.StdfTest1;
import com.makechip.stdf2xls4.stdf.StdfTest2;
import com.makechip.stdf2xls4.stdf.StdfTest3;
import com.makechip.stdf2xls4.stdf.StdfTest4;
import com.makechip.stdf2xls4.stdf.StdfTest5;
import com.makechip.stdf2xls4.stdf.enums.EnumTests;
import com.makechip.stdf2xls4.stdfapi.StdfApiTest1;

@RunWith(Suite.class)
@SuiteClasses(
	{ 
		OptionsTest.class, 
		StdfTest1.class, 
		StdfTest2.class,
		StdfTest3.class, 
		StdfTest4.class, 
		StdfTest5.class, 
		EnumTests.class,
		StdfApiTest1.class
	})
public class AllTests
{

}