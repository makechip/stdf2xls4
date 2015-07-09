package test.stdf;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ StdfTest1.class, StdfTest2.class, StdfTest3.class, StdfTest4.class, 
	            StdfTest5.class, StdfApiTest1.class, EnumTests.class, OptionsTest.class })
public class RunAllTests
{


}
