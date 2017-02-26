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
 * ClassFinder.java Copyright (c) 2004 Eric West All Rights Reserved.
 */
package com.makechip.util;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.util.jar.*;

/**
 * Compiles a list of all classes in the class path, whether in jar files or
 * standalone class files. Scope of search is limited to all classes and
 * packages within a specified package; and classes must be inherited from a
 * specified superClass.
 * 
 * @author Eric West
 * @version $Id: ClassFinder.java 1 2007-09-15 21:50:34Z eric $
 * @param <T> The class of the super class
 */
public class ClassFinder<T> implements Iterable<Class<? extends T>>
{
    protected String                        basePkg;
    protected ClassFilter                   filter;
    protected ArrayList<Class<? extends T>> classes;
    protected Class<T>                      superClass;

    private class ClassFilter
    {
        private Class<T> supercls;
        private boolean  superclsIsInterface;

        /**
         * 
         * @param supercls
         * @param superclsIsInterface
         */
        public ClassFilter(Class<T> supercls, boolean superclsIsInterface)
        {
            this.supercls = supercls;
            this.superclsIsInterface = superclsIsInterface;
        }

        /**
         * 
         * @param c
         * @return true if the class has the correct superclass
         */
        public boolean accept(Class<?> c)
        {
            if (superclsIsInterface)
            {
                Class<?>[] ifs = c.getInterfaces();
                for (Class<?> cif : ifs)
                    if (supercls == cif) return (true);
            }
            Class<?> s = c.getSuperclass();
            while (s != null)
            {
                if (superclsIsInterface)
                {
                    Class<?>[] ifs = s.getInterfaces();
                    for (Class<?> cif : ifs)
                        if (supercls == cif) return (true);
                }
                else
                {
                    if (s == supercls) return (true);
                }
                s = s.getSuperclass();
            }
            return (false);
        }
    }

    /**
     * 
     * @param args
     */
    @SuppressWarnings("rawtypes")
    public static void main(String[] args)
    {
        ClassFinder<Enum> cf = new ClassFinder<Enum>("com.makechip.util", Enum.class, false);
        for (Class<? extends Enum> c : cf)
            Log.msg("c = " + c.getSimpleName());
        Log.msg("Test interface filter:");
        ClassFinder<Iface> cff = new ClassFinder<Iface>("com.makechip.util", Iface.class, true);
        for (Class<? extends Iface> c : cff)
            Log.msg("cf = " + c.getSimpleName());
    }

    /**
     * Constructor that uses default class path from System. The classes that
     * this Finder object finds must be subClasses of superClass.
     * 
     * @param basePkg Only this package and its sub-packages will be searched.
     * @param superClass Objects located by this finder must be subclasses of
     *            superClass.
     * @param superClassIsInterface If true, then the located class (or its
     *            superClasses) must implement the superClass interface,
     *            otherwise the located class must be a regular subclass of
     *            superClass.
     */
    public ClassFinder(String basePkg, Class<T> superClass, boolean superClassIsInterface)
    {
        this.basePkg = basePkg;
        this.superClass = superClass;
        filter = new ClassFilter(superClass, superClassIsInterface);
        findClasses();
    }

    public Iterator<Class<? extends T>> iterator()
    {
        return (classes.iterator());
    }

    /**
     * Given a file name, convert to a class name.
     * 
     * @param name file path name
     * @return converted name
     */
    protected String massageClassName(String name)
    {
        if (name.endsWith(".class"))
        {
            name = name.replace(File.separatorChar, '.');
            int start = name.indexOf(basePkg);
            if (start >= 0)
            {
                int stop = name.lastIndexOf(".class");
                name = name.substring(start, stop);
                return (name);
            }
        }
        return (null);
    }

    /**
     * Find all of the classes in the search path.
     * 
     */
    protected void findClasses()
    {
        ClassFileFinder finder = new ClassFileFinder(basePkg);
        classes = new ArrayList<Class<? extends T>>();
        for (String file : finder)
        {
            if (file.endsWith(".jar") && finder.isFileInPath(file))
            {
                try
                {
                    JarFile jar = new JarFile(new File(file));
                    Enumeration<JarEntry> enumm = jar.entries();
                    while (enumm.hasMoreElements())
                    {
                        ZipEntry entry = (ZipEntry) enumm.nextElement();
                        String className = massageClassName(entry.getName());
                        if (className != null && className.indexOf(basePkg) >= 0)
                        {
                            try
                            {
                                Class<?> c = Class.forName(className);
                                if (filter.accept(c))
                                {
                                    Class<? extends T> ces = c.asSubclass(superClass);
                                    classes.add(ces);
                                }
                            }
                            catch (Exception e)
                            {
                            }
                        }
                    }
                    jar.close();
                }
                catch (ZipException z)
                {
                }
                catch (IOException e)
                {
                    System.out.println("caught exception " + e);
                }
            }
            else if (!file.endsWith(".jar"))
            {
                try
                {
                    File f = new File(file);
                    String className = massageClassName(f.getCanonicalPath());
                    if (className != null)
                    {
                        Class<?> c = Class.forName(className);
                        if (filter.accept(c))
                        {
                            Class<? extends T> ces = c.asSubclass(superClass);
                            classes.add(ces);
                        }
                    }
                }
                catch (Exception e)
                {
                    System.out.println(e.toString());
                }
            }
        }
    }
}
