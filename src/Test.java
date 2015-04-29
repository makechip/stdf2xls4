import java.util.EnumSet;

import com.makechip.util.Log;


public class Test
{
	static enum Test_t
	{
	    A(1),
	    B(2),
	    C(4),
	    D(8),
	    E(16),
	    F(32),
	    G(64),
	    H(128);
	    
	    private final byte bit;
	    
	    private Test_t(int bit)
	    {
	        this.bit = (byte) bit;	
	    }
	    
	    public byte getBit() { return(bit); }
	}

	public Test()
	{
	}

	public static void main(String[] args)
	{
		EnumSet<Test_t> set = EnumSet.noneOf(Test_t.class);
		set.add(Test_t.A);
		set.add(Test_t.D);
		set.add(Test_t.E);
		
		byte n = (byte) set.stream().mapToInt(b -> b.getBit()).sum();
        System.out.println("n = " + n); 
        Log.msg("min = " + (-Float.MAX_VALUE));
	}

}
