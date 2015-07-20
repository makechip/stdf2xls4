package com.makechip.stdf2xls4.stdfapi;

import com.makechip.stdf2xls4.stdf.TestID;


public class TestHeader
{

	public final String testName;
	public final long testNumber;
	public final int dupNum;

	public TestHeader(TestID id)
	{
		this.testName = id.testName;
		this.testNumber = id.testNumber;
		this.dupNum = id.dupNum;
	}
	
	public TestHeader(String testName, long testNumber, int dupNum)
	{
		this(testName, testNumber, dupNum, "");
	}

	public TestHeader(String testName, long testNumber, int dupNum, String testNameSuffix)
	{
		this.testName = testName + testNameSuffix;
		this.testNumber = testNumber;
		this.dupNum = dupNum;
	}
	
	public boolean isLoLimitHeader() { return(false); }
	public boolean isHiLimitHeader() { return(false); }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("TestHeader [testName=").append(testName == null ? "" : testName);
		builder.append(", testNumber=").append(testNumber);
		builder.append(", dupNum=").append(dupNum);
		builder.append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + dupNum;
		result = prime * result + ((testName == null) ? 0 : testName.hashCode());
		result = prime * result + (int) (testNumber ^ (testNumber >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof TestHeader)) return false;
		TestHeader other = (TestHeader) obj;
		if (dupNum != other.dupNum) return false;
		if (testName == null)
		{
			if (other.testName != null) return false;
		} 
		else if (!testName.equals(other.testName)) return false;
		if (testNumber != other.testNumber) return false;
		return true;
	}
	
}
