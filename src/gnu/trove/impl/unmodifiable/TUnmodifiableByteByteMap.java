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

package gnu.trove.impl.unmodifiable;


//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////

////////////////////////////////////////////////////////////
// THIS IS AN IMPLEMENTATION CLASS. DO NOT USE DIRECTLY!  //
// Access to these methods should be through TCollections //
////////////////////////////////////////////////////////////


import gnu.trove.TByteCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TByteByteIterator;
import gnu.trove.map.TByteByteMap;
import gnu.trove.procedure.TByteByteProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.set.TByteSet;

import java.io.Serializable;
import java.util.Map;


public class TUnmodifiableByteByteMap implements TByteByteMap, Serializable {
	private static final long serialVersionUID = -1034234728574286014L;

	private final TByteByteMap m;

	public TUnmodifiableByteByteMap( TByteByteMap m ) {
		if ( m == null )
			throw new NullPointerException();
		this.m = m;
	}

	public int size()                       { return m.size(); }
	public boolean isEmpty()                { return m.isEmpty(); }
	public boolean containsKey( byte key )   { return m.containsKey( key ); }
	public boolean containsValue( byte val ) { return m.containsValue( val ); }
	public byte get( byte key)                { return m.get( key ); }

	public byte put( byte key, byte value ) { throw new UnsupportedOperationException(); }
	public byte remove( byte key ) { throw new UnsupportedOperationException(); }
	public void putAll( TByteByteMap m ) { throw new UnsupportedOperationException(); }
	public void putAll( Map<? extends Byte, ? extends Byte> map ) { throw new UnsupportedOperationException(); }
	public void clear() { throw new UnsupportedOperationException(); }

	private transient TByteSet keySet = null;
	private transient TByteCollection values = null;

	public TByteSet keySet() {
		if ( keySet == null )
			keySet = TCollections.unmodifiableSet( m.keySet() );
		return keySet;
	}
	public byte[] keys() { return m.keys(); }
	public byte[] keys( byte[] array ) { return m.keys( array ); }

	public TByteCollection valueCollection() {
		if ( values == null )
			values = TCollections.unmodifiableCollection( m.valueCollection() );
		return values;
	}
	public byte[] values() { return m.values(); }
	public byte[] values( byte[] array ) { return m.values( array ); }

	public boolean equals(Object o) { return o == this || m.equals(o); }
	public int hashCode()           { return m.hashCode(); }
	public String toString()        { return m.toString(); }
	public byte getNoEntryKey()      { return m.getNoEntryKey(); }
	public byte getNoEntryValue()    { return m.getNoEntryValue(); }

	public boolean forEachKey( TByteProcedure procedure ) {
		return m.forEachKey( procedure );
	}
	public boolean forEachValue( TByteProcedure procedure ) {
		return m.forEachValue( procedure );
	}
	public boolean forEachEntry( TByteByteProcedure procedure ) {
		return m.forEachEntry( procedure );
	}

	public TByteByteIterator iterator() {
		return new TByteByteIterator() {
			TByteByteIterator iter = m.iterator();

			public byte key() { return iter.key(); }
			public byte value() { return iter.value(); }
			public void advance() { iter.advance(); }
			public boolean hasNext() { return iter.hasNext(); }
			public byte setValue( byte val ) { throw new UnsupportedOperationException(); }
			public void remove() { throw new UnsupportedOperationException(); }
		};
	}

	public byte putIfAbsent( byte key, byte value ) { throw new UnsupportedOperationException(); }
	public void transformValues( TByteFunction function ) { throw new UnsupportedOperationException(); }
	public boolean retainEntries( TByteByteProcedure procedure ) { throw new UnsupportedOperationException(); }
	public boolean increment( byte key ) { throw new UnsupportedOperationException(); }
	public boolean adjustValue( byte key, byte amount ) { throw new UnsupportedOperationException(); }
	public byte adjustOrPutValue( byte key, byte adjust_amount, byte put_amount ) { throw new UnsupportedOperationException(); }
}