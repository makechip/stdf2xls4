package com.makechip.stdf2xls4.stdfapi;

import com.makechip.stdf2xls4.stdf.TestID;

public class TestHeader
{
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("TestHeader [");
		if (id != null)
		{
			builder.append("id=");
			builder.append(id);
		}
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof TestHeader)) return false;
		TestHeader other = (TestHeader) obj;
		return(id == other.id);
	}

	public final TestID id;

	public TestHeader(TestID id)
	{
		this.id = id;
	}
	
	public String getTestName() { return(id.testName); }
	
	public long getTestNumber() { return(id.testNumber); }

}
