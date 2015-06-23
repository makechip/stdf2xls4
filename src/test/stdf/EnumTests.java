package test.stdf;

import static org.junit.Assert.*;

import java.util.EnumSet;

import org.junit.Test;

import com.makechip.stdf2xls4.stdf.enums.Cpu_t;
import com.makechip.stdf2xls4.stdf.enums.Data_t;
import com.makechip.stdf2xls4.stdf.enums.FTROptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.OptFlag_t;
import com.makechip.stdf2xls4.stdf.enums.ParamFlag_t;
import com.makechip.stdf2xls4.stdf.enums.PartInfoFlag_t;
import com.makechip.stdf2xls4.stdf.enums.Record_t;
import com.makechip.stdf2xls4.stdf.enums.TestFlag_t;
import com.makechip.stdf2xls4.stdf.enums.TestOptFlag_t;

public class EnumTests {

	@Test
	public void Cpu_t_test1() 
	{
		assertEquals(Cpu_t.getCpuType((byte) 0), Cpu_t.VAX);
		assertEquals(Cpu_t.getCpuType((byte) 1), Cpu_t.SUN);
		assertEquals(Cpu_t.getCpuType((byte) 2), Cpu_t.PC);
	}
	
	@Test(expected = RuntimeException.class)
	public void Cpu_t_test2()
	{
		Cpu_t.getCpuType((byte) 4);
	}
	
	@Test 
	public void Cpu_t_test3()
	{
		assertEquals(Cpu_t.SUN.getU2((byte) 1, (byte) 2), 258);
		assertEquals(Cpu_t.SUN.getU4((byte) 0, (byte) 1, (byte) 2, (byte) 3), 65536 + 512 + 3);
		assertEquals(Cpu_t.SUN.getI2((byte) 0xFF, (byte) 1), -255);
		assertEquals(Cpu_t.SUN.getI4((byte) 0xFF, (byte) 1, (byte) 2, (byte) 3), -16711165);
		assertEquals(Cpu_t.SUN.getLong((byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7), 283686952306183L);
	}
	
	@Test(expected = RuntimeException.class)
	public void Cpu_t_test4()
	{
		Cpu_t.SUN.getU2Bytes(65546);
	}
	
	@Test(expected = RuntimeException.class)
	public void Cpu_t_test4a()
	{
		Cpu_t.SUN.getU2Bytes(-1);
	}
	
	@Test
	public void Cpu_t_test5()
	{
		byte[] b = Cpu_t.SUN.getU2Bytes(258);
		assertEquals(b[0], (byte) 1);
		assertEquals(b[1], (byte) 2);
	}
	
	@Test(expected = RuntimeException.class)
	public void Cpu_t_test6()
	{
		Cpu_t.PC.getU4Bytes((long) Integer.MAX_VALUE + 1L);
	}
	
	@Test(expected = RuntimeException.class)
	public void Cpu_t_test6a()
	{
		Cpu_t.PC.getU4Bytes(-1L);
	}
	
	@Test
	public void Cpu_t_test7()
	{
	    byte[] b = Cpu_t.SUN.getU4Bytes(65536 + 512 + 3);	
	    assertEquals(b[0], (byte) 0);
	    assertEquals(b[1], (byte) 1);
	    assertEquals(b[2], (byte) 2);
	    assertEquals(b[3], (byte) 3);
	    byte[] c = Cpu_t.SUN.getI2Bytes((short) -255);
	    assertEquals(c[0], (byte) 0xFF);
	    assertEquals(c[1], (byte) 1);
	    byte[] d = Cpu_t.SUN.getI4Bytes(-16711165);
	    assertEquals(d[0], (byte) 0xFF);
	    assertEquals(d[1], (byte) 1);
	    assertEquals(d[2], (byte) 2);
	    assertEquals(d[3], (byte) 3);
	    byte[] e = Cpu_t.SUN.getR8Bytes(1.2);
	    assertEquals(e[0], (byte) 63);
	    assertEquals(e[1], (byte) -13);
	    assertEquals(e[2], (byte) 51);
	    assertEquals(e[3], (byte) 51);
	    assertEquals(e[4], (byte) 51);
	    assertEquals(e[5], (byte) 51);
	    assertEquals(e[6], (byte) 51);
	    assertEquals(e[7], (byte) 51);
	}
	
	@Test
	public void Data_t_test()
	{
		assertEquals(Data_t.U_1.getNumBytes(), 1);
		assertEquals(Data_t.U_2.getNumBytes(), 2);
		assertEquals(Data_t.U_4.getNumBytes(), 4);
		assertEquals(Data_t.I_1.getNumBytes(), 1);
		assertEquals(Data_t.I_2.getNumBytes(), 2);
		assertEquals(Data_t.I_4.getNumBytes(), 4);
		assertEquals(Data_t.R_4.getNumBytes(), 4);
		assertEquals(Data_t.R_8.getNumBytes(), 8);
		assertEquals(Data_t.C_N.getNumBytes(), 1);
		assertEquals(Data_t.B_N.getNumBytes(), 1);
		assertEquals(Data_t.D_N.getNumBytes(), 1);
		assertEquals(Data_t.N_N.getNumBytes(), 1);
	}
	
	@Test
	public void FTROptFlag_t_test()
	{
		assertEquals(FTROptFlag_t.CYCLE_CNT_INVALID.getBit(), 1);
		assertEquals(FTROptFlag_t.REL_VADDR_INVALID.getBit(), 2);
		assertEquals(FTROptFlag_t.REPEAT_CNT_INVALID.getBit(), 4);
		assertEquals(FTROptFlag_t.NUM_FAIL_INVALID.getBit(), 8);
		assertEquals(FTROptFlag_t.XY_FAIL_ADDR_INVALID.getBit(), 16);
		assertEquals(FTROptFlag_t.VEC_OFFSET_INVALID.getBit(), 32);
	}
	
	@Test
	public void OptFlag_t_test()
	{
		assertEquals(OptFlag_t.RES_SCAL_INVALID.getBit(), 1);
		assertEquals(OptFlag_t.START_INCR_IN_INVALID.getBit(), 2);
		assertEquals(OptFlag_t.NO_LO_SPEC_LIMIT.getBit(), 4);
		assertEquals(OptFlag_t.NO_HI_SPEC_LIMIT.getBit(), 8);
		assertEquals(OptFlag_t.LO_LIMIT_LLM_SCAL_INVALID.getBit(), 16);
		assertEquals(OptFlag_t.HI_LIMIT_HLM_SCAL_INVALID.getBit(), 32);
		assertEquals(OptFlag_t.NO_LO_LIMIT.getBit(), 64);
		assertEquals(OptFlag_t.NO_HI_LIMIT.getBit(), -128);
		EnumSet<OptFlag_t> s = OptFlag_t.getBits((byte) 0xFF);
		assertTrue(s.contains(OptFlag_t.RES_SCAL_INVALID));
		assertTrue(s.contains(OptFlag_t.START_INCR_IN_INVALID));
		assertTrue(s.contains(OptFlag_t.NO_LO_SPEC_LIMIT));
		assertTrue(s.contains(OptFlag_t.NO_HI_SPEC_LIMIT));
		assertTrue(s.contains(OptFlag_t.LO_LIMIT_LLM_SCAL_INVALID));
		assertTrue(s.contains(OptFlag_t.HI_LIMIT_HLM_SCAL_INVALID));
		assertTrue(s.contains(OptFlag_t.NO_LO_LIMIT));
		assertTrue(s.contains(OptFlag_t.NO_HI_LIMIT));
	}
	
	@Test 
	public void ParamFlag_t_test()
	{
	    assertEquals(ParamFlag_t.SCALE_ERROR.getBit(), 1);	
	    assertEquals(ParamFlag_t.DRIFT_ERROR.getBit(), 2);	
	    assertEquals(ParamFlag_t.OSCILLATION.getBit(), 4);	
	    assertEquals(ParamFlag_t.VALUE_HIGH.getBit(), 8);	
	    assertEquals(ParamFlag_t.VALUE_LOW.getBit(), 16);	
	    assertEquals(ParamFlag_t.ALTERNATE_PASS.getBit(), 32);	
	    assertEquals(ParamFlag_t.LO_LIMIT_EQ_PASS.getBit(), 64);	
	    assertEquals(ParamFlag_t.HI_LIMIT_EQ_PASS.getBit(), -128);	
	    EnumSet<ParamFlag_t> s = ParamFlag_t.getBits((byte) 0xFF);
	    assertTrue(s.contains(ParamFlag_t.SCALE_ERROR));	
	    assertTrue(s.contains(ParamFlag_t.DRIFT_ERROR));	
	    assertTrue(s.contains(ParamFlag_t.OSCILLATION));	
	    assertTrue(s.contains(ParamFlag_t.VALUE_HIGH));	
	    assertTrue(s.contains(ParamFlag_t.VALUE_LOW));	
	    assertTrue(s.contains(ParamFlag_t.ALTERNATE_PASS));	
	    assertTrue(s.contains(ParamFlag_t.LO_LIMIT_EQ_PASS));	
	    assertTrue(s.contains(ParamFlag_t.HI_LIMIT_EQ_PASS));	
	}
	
	@Test
	public void PartInfoFlag_t_test()
	{
		assertEquals(PartInfoFlag_t.RETEST_EXEC0.getBit(), 1);
		assertEquals(PartInfoFlag_t.RETEST_EXEC1.getBit(), 2);
		assertEquals(PartInfoFlag_t.ABNORMAL_END_OF_TEST.getBit(), 4);
		assertEquals(PartInfoFlag_t.PART_FAILED.getBit(), 8);
		assertEquals(PartInfoFlag_t.NO_PASS_FAIL_INDICATION.getBit(), 16);
		EnumSet<PartInfoFlag_t> s = PartInfoFlag_t.getBits((byte) 0x1F);
		assertTrue(s.contains(PartInfoFlag_t.RETEST_EXEC0));
		assertTrue(s.contains(PartInfoFlag_t.RETEST_EXEC1));
		assertTrue(s.contains(PartInfoFlag_t.ABNORMAL_END_OF_TEST));
		assertTrue(s.contains(PartInfoFlag_t.PART_FAILED));
		assertTrue(s.contains(PartInfoFlag_t.NO_PASS_FAIL_INDICATION));
	}
	
	@Test
	public void Record_t_test()
	{
		assertEquals(Record_t.getRecordType(99, 99), null);
		assertEquals(Record_t.PMR.getDescription(), "Pin Map Record");
	}
	
	@Test
	public void TestFlag_t_test()
	{
		assertEquals(TestFlag_t.ALARM.getBit(), 1);
		assertEquals(TestFlag_t.UNRELIABLE.getBit(), 4);
		assertEquals(TestFlag_t.TIMEOUT.getBit(), 8);
		assertEquals(TestFlag_t.NOT_EXECUTED.getBit(), 16);
		assertEquals(TestFlag_t.ABORT.getBit(), 32);
		assertEquals(TestFlag_t.NO_PASS_FAIL.getBit(), 64);
		assertEquals(TestFlag_t.FAIL.getBit(), -128);
		EnumSet<TestFlag_t> s = TestFlag_t.getBits((byte) 0xFF);
		assertTrue(s.contains(TestFlag_t.ALARM));
		assertTrue(s.contains(TestFlag_t.UNRELIABLE));
		assertTrue(s.contains(TestFlag_t.TIMEOUT));
		assertTrue(s.contains(TestFlag_t.NOT_EXECUTED));
		assertTrue(s.contains(TestFlag_t.ABORT));
		assertTrue(s.contains(TestFlag_t.NO_PASS_FAIL));
		assertTrue(s.contains(TestFlag_t.FAIL));
	}
	
	@Test
	public void TestOptFlag_t_test()
	{
		assertEquals(TestOptFlag_t.TEST_MIN_INVALID.getBit(), 1);
		assertEquals(TestOptFlag_t.TEST_MAX_INVALID.getBit(), 2);
		assertEquals(TestOptFlag_t.TEST_TIME_INVALID.getBit(), 4);
		assertEquals(TestOptFlag_t.TEST_SUMS_INVALID.getBit(), 16);
		assertEquals(TestOptFlag_t.TEST_SQRS_INVALID.getBit(), 32);
	}

}
