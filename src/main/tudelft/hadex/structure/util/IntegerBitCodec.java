/** Copyright (c) 2012 wenli
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package tudelft.hadex.structure.util;

/**
 * Postings are usually arrays of integers which could contain
 * millions of integers which need special encoding to reduce
 * the space of storing them.
 * @author wenli
 *
 */
public abstract class IntegerBitCodec {
	
	/**
	 * Get the bit length of the number
	 * @param v the given integer
	 * @return the number of bit that is needed for storing the integer
	 */
	public static int bitLength(int v){
		return 32 - Integer.numberOfLeadingZeros(v);
	}
	
	public static int bitLength(long v){
		return 64 - Long.numberOfLeadingZeros(v);
	}
		
	/**
	 * Encode integers into compressed codes
	 * @param integers
	 * @return
	 */
	public abstract long[] blockEncode(long[] integers);


	/**
	 * Decode integers from compressed codes
	 * @param codes
	 * @return
	 */
	public abstract long[] blockDecode(long[] codes);
}
