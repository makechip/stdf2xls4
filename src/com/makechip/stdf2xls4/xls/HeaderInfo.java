// Copyright 2011,2012 makechip.com
// This file is part of stdf2xls.
// 
// stdf2xls is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// stdf2xls is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with stdf2xls.  If not, see <http://www.gnu.org/licenses/>.

package com.makechip.stdf2xls4.xls;

public final class HeaderInfo
{
    private String customer;
    private String device;
    private String sow;
    private String po;
    private String tester;
    private String job;
    private String ctrlSerials;
    private String loadboard;
    private String testProgram;
    
    public HeaderInfo(String customer, String device, String sow, String po, String tester,
                      String job, String ctrlSerials, String loadboard, String testProgram)
    {
        this.customer = customer;
        this.device = device;
        this.sow = sow;
        this.po = po;
        this.tester = tester;
        this.job = job;
        this.ctrlSerials = ctrlSerials;
        this.loadboard = loadboard;
        this.testProgram = testProgram;
    }
    
    public String getCustomer() { return(customer); }
    public String getDevice() { return(device); }
    public String getSow() { return(sow); }
    public String getPo() { return(po); }
    public String getTester() { return(tester); }
    public String getJob() { return(job); }
    public String getCtrlSerials() { return(ctrlSerials); }
    public String getLoadBoard() { return(loadboard); }
    public String getTestProgram() { return(testProgram); }

}
