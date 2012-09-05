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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author wenli
 *
 */
public class DeltaCodecTest {

	/**
	 * Test method for {@link tudelft.hadex.structure.compression.DeltaCodec#blockEncode(long[])}.
	 */
	@Test
	public void testBlockEncode() {
		DeltaCodec codec = new DeltaCodec();
		long[] ints = new long[]{-1,-2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
		long[] codes = codec.blockEncode(ints);
		long[] decoded = codec.blockDecode(codes);
		
		assertArrayEquals(ints, decoded);
	}


}
