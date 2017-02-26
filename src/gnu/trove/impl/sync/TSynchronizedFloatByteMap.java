///////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2008, Robert D. Eden All Rights Reserved.
// Copyright (c) 2009, Jeff Randall All Rights Reserved.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
///////////////////////////////////////////////////////////////////////////////

package gnu.trove.impl.sync;


//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////

////////////////////////////////////////////////////////////
// THIS IS AN IMPLEMENTATION CLASS. DO NOT USE DIRECTLY!  //
// Access to these methods should be through TCollections //
////////////////////////////////////////////////////////////


import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TFloatByteIterator;
import gnu.trove.map.TFloatByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TFloatByteProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;


public class TSynchronizedFloatByteMap implements TFloatByteMap, Serializable {
	private static final long serialVersionUID = 1978198479659022715L;

	private final TFloatByteMap m;     // Backing Map
	final Object      mutex;	// Object on which to synchronize

	public TSynchronizedFloatByteMap( TFloatByteMap m ) {
		if ( m == null )
			throw new NullPointerException();
		this.m = m;
		mutex = this;
	}

	public TSynchronizedFloatByteMap( TFloatByteMap m, Object mutex ) {
		this.m = m;
		this.mutex = mutex;
	}

	public int size() {
		synchronized( mutex ) { return m.size(); }
	}
	public boolean isEmpty(){
		synchronized( mutex ) { return m.isEmpty(); }
	}
	public boolean containsKey( float key ) {
		synchronized( mutex ) { return m.containsKey( key ); }
	}
	public boolean containsValue( byte value ){
		synchronized( mutex ) { return m.containsValue( value ); }
	}
	public byte get( float key ) {
		synchronized( mutex ) { return m.get( key ); }
	}

	public byte put( float key, byte value ) {
		synchronized( mutex ) { return m.put( key, value ); }
	}
	public byte remove( float key ) {
		synchronized( mutex ) { return m.remove( key ); }
	}
	public void putAll( Map<? extends Float, ? extends Byte> map ) {
		synchronized( mutex ) { m.putAll( map ); }
	}
	public void putAll( TFloatByteMap map ) {
		synchronized( mutex ) { m.putAll( map ); }
	}
	public void clear() {
		synchronized( mutex ) { m.clear(); }
	}

	private transient TFloatSet keySet = null;
	private transient TByteCollection values = null;

	public TFloatSet keySet() {
		synchronized( mutex ) {
			if ( keySet == null )
				keySet = new TSynchronizedFloatSet( m.keySet(), mutex );
			return keySet;
		}
	}
	public float[] keys() {
		synchronized( mutex ) { return m.keys(); }
	}
	public float[] keys( float[] array ) {
		synchronized( mutex ) { return m.keys( array ); }
	}

	public TByteCollection valueCollection() {
		synchronized( mutex ) {
			if ( values == null )
				values = new TSynchronizedByteCollection( m.valueCollection(), mutex );
			return values;
		}
	}
	public byte[] values() {
		synchronized( mutex ) { return m.values(); }
	}
	public byte[] values( byte[] array ) {
		synchronized( mutex ) { return m.values( array ); }
	}

	public TFloatByteIterator iterator() {
		return m.iterator(); // Must be manually synched by user!
	}

	// these are unchanging over the life of the map, no need to lock
	public float getNoEntryKey() { return m.getNoEntryKey(); }
	public byte getNoEntryValue() { return m.getNoEntryValue(); }

	public byte putIfAbsent( float key, byte value ) {
		synchronized( mutex ) { return m.putIfAbsent( key, value ); }
	}
	public boolean forEachKey( TFloatProcedure procedure ) {
		synchronized( mutex ) { return m.forEachKey( procedure ); }
	}
	public boolean forEachValue( TByteProcedure procedure ) {
		synchronized( mutex ) { return m.forEachValue( procedure ); }
	}
	public boolean forEachEntry( TFloatByteProcedure procedure ) {
		synchronized( mutex ) { return m.forEachEntry( procedure ); }
	}
	public void transformValues( TByteFunction function ) {
		synchronized( mutex ) { m.transformValues( function ); }
	}
	public boolean retainEntries( TFloatByteProcedure procedure ) {
		synchronized( mutex ) { return m.retainEntries( procedure ); }
	}
	public boolean increment( float key ) {
		synchronized( mutex ) { return m.increment( key ); }
	}
	public boolean adjustValue( float key, byte amount ) {
		synchronized( mutex ) { return m.adjustValue( key, amount ); }
	}
	public byte adjustOrPutValue( float key, byte adjust_amount, byte put_amount ) {
		synchronized( mutex ) { return m.adjustOrPutValue( key, adjust_amount, put_amount ); }
	}

	public boolean equals( Object o ) {
		synchronized( mutex ) { return m.equals( o ); }
	}
	public int hashCode() {
		synchronized( mutex ) { return m.hashCode(); }
	}
	public String toString() {
		synchronized( mutex ) { return m.toString(); }
	}
	private void writeObject( ObjectOutputStream s ) throws IOException {
		synchronized( mutex ) { s.defaultWriteObject(); }
	}
}
