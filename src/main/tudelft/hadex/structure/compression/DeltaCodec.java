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

package tudelft.hadex.structure.compression;

/**
 * DeltaCodec is used to save bit length of a set
 * of integers by encoding integers as differential
 * between each pair of consecutive stored integers.
 * This is especially good for integers sorted ascendantly.
 * 
 * @author wenli
 *
 */
public class DeltaCodec extends IntegerCodec {
	
	/**
	 * The first code for the array of integers is the first element
	 * in the array while the rest codes are differential between
	 * each pair of consecutively stored integers.
	 * Ex 1,2,3 encoded as 1,1,1
	 * @see tudelft.hadex.structure.util.IntegerCodec#blockEncode(long[])
	 */
	@Override
	public long[] blockEncode(long[] integers) {
		long[] codes = new long[integers.length];
		codes[0] = integers[0];
		for(int i=1; i<integers.length; ++i){
			codes[i] = integers[i] - integers[i - 1];
		}
		return codes;
	}

	/**
	 * Decode DeltaCodec encoded integers.
	 * @see tudelft.hadex.structure.util.IntegerCodec#blockDecode(long[])
	 */
	@Override
	public long[] blockDecode(long[] codes) {
		long[] integers = new long[codes.length];
		integers[0] = codes[0];
		for(int i=1; i<codes.length; ++i){
			integers[i] = integers[i-1] + codes[i];
		}
		return integers;
	}

}
