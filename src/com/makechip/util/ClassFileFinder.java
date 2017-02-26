/*
 * ==========================================================================
 * Copyright (C) 2013,2014 makechip.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program
 * This license can also be found on the GNU website at
 * http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */
/**
 * ClassFileFinder.java Copyright (c) 2004 Eric West Inc. All Rights Reserved.
 */
package com.makechip.util;

import java.io.*;
import java.util.*;

/**
 * 
 * 
 * @author Eric West
 * @version $Id: ClassFileFinder.java 1 2007-09-15 21:50:34Z eric $ Locates
 *          all class
 */
public class ClassFileFinder implements Iterable<String>
{
    protected ArrayList<String> pathNodes;
    protected ArrayList<String> files;
    protected String            basePkg;

    /**
     * Contructor that uses the default path from the System class.
     * 
     * @param basePkg Only files in the basePkg will be searched; The basePkg
     *            must be in the default Class path (ie.
     *            System.getProperty("java.class.path")).
     */
    public ClassFileFinder(String basePkg)
    {
        this(System.getProperty("java.class.path"), basePkg);
    }

    /**
     * Contructor that uses the specified class path.
     * 
     * @param path the class path to search
     * @param basePkg the package to search within the specified class path.
     */
    public ClassFileFinder(String path, String basePkg)
    {
        this.basePkg = basePkg;
        pathNodes = new ArrayList<String>();
        files = new ArrayList<String>();
        String pathSep = System.getProperty("path.separator");
        StringTokenizer st = new StringTokenizer(path, pathSep);
        while (st.hasMoreTokens())
            pathNodes.add(st.nextToken());
        findFiles();
    }

    /**
     * Returns an iterator containing all of the class files found
     * 
     * @return an iterator of filenames.
     */
    public Iterator<String> iterator()
    {
        return (files.iterator());
    }

    /**
     * Searches the class path, adding files to an array.
     */
    protected void findFiles()
    {
        String base = basePkg.replace('.', File.separatorChar);
        ClassFileFilter filter = new ClassFileFilter();
        for (String node : pathNodes)
        {
            if (node.endsWith(".jar"))
            {
                files.add(node);
                continue;
            }
            String prefix = node + "/" + base;
            File file = new File(node);
            if (!file.exists()) continue;
            if (!file.canRead()) continue;
            if (filter.accept(file))
            {
                if (file.isFile()) files.add(file.getName());
                else processDirectory(node, prefix, file, filter, files);
            }
        }
    }

    /**
     * See if a File is explicitly in the search path.
     * 
     * @param file a file - typically a jar file.
     * @return true if the file is in the search path
     */
    public boolean isFileInPath(String file)
    {
        return pathNodes.contains(file);
    }

    /**
     * For a given directory, recursively look for matching files.
     * 
     * @param pathNode the path concatenated with the base package
     * @param prefix
     * @param f the directory name
     * @param filter the acceptance criterion
     * @param keep the list of accepted files
     */
    private void processDirectory(String pathNode, String prefix, File f, FileFilter filter, ArrayList<String> keep)
    {
        // String dirName = f.getName();
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; ++i)
        {
            File file = files[i];
            if (!file.exists()) continue;
            if (!file.canRead()) continue;
            if (filter.accept(file))
            {
                if (!file.isDirectory())
                {
                    try
                    {
                        if (file.getCanonicalPath().startsWith(prefix)) keep.add(file.getCanonicalPath().substring(
                                pathNode.length()));
                    }
                    catch (Exception e)
                    {
                    }
                }
                else processDirectory(pathNode, prefix, file, filter, keep);
            }
        }
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            Log.msg("usage: java com.makechip.util.ClassFileFinder <packageName>");
            System.exit(1);
        }
        ClassFileFinder f = new ClassFileFinder(args[0]);
        for (String s : f)
        {
            System.out.println(s);
        }
    }
}
