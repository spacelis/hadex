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

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

/**
 * @author wenli
 *
 */
public class TestGamma {
	
	private Random r;
	private long[][] x;
	private final int SIZE = 1000;
	
	@Before
	public void setup() {
		r = new Random(System.currentTimeMillis());
		x = new long[SIZE][SIZE];
		for(int i=0; i<SIZE; ++i) 
			for(int j=0; j<SIZE; ++j)
				x[i][j] = r.nextInt();
	}

	/**
	 * Test method for {@link tudelft.hadex.structure.util.Gamma#blockEncode(long[])}.
	 */
	@Test
	public void testBlockEncodeDesigned() {
		Gamma codec = new Gamma();
		long[] ints = new long[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
		long[] codes = codec.blockEncode(ints);
		long[] decoded = codec.blockDecode(codes);
		
		assertArrayEquals(codes, new long[]{-6466438018444421584L, 7557569039820652544L});
		assertArrayEquals(decoded, ints);
	}

	/**
	 * Test method for {@link tudelft.hadex.structure.util.Gamma#blockEncode(long[])}.
	 */
	@Test
	public void testBlockEncodeSpeed() {
		long[][] codes = new long[SIZE][];
		Gamma codec = new Gamma(2049);
		int length = 0;
		for(int i=0; i<SIZE; ++i) {
			codes[i] = codec.blockEncode(x[i]);
			length += codes[i].length;
		}
		System.out.println("Total Length: " + length + "/" + SIZE*SIZE);
	}
}
