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

import java.util.Arrays;

/**
 * Varying-length Integer Encoder store integers in non-fixed
 * length of 8-bit blocks (a.k.a bytes). This first bit is
 * of each 8-bit block is used for indicating whether another
 * block is required for decoding an integer and the rest 7
 * bits are used for store a part of the integer. In this class
 * 0 means another block is required while 1 means no more block
 * is required.
 * 
 * @author wenli
 *
 */
public class VarInt8Codec extends IntegerCodec{

	private final long mask7bit = 0x7F;
	
	private final long[] mask = new long[] {
			0x7F00000000000000L,
			0x007F000000000000L,
			0x00007F0000000000L,
			0x0000007F00000000L,
			0x000000007F000000L,
			0x00000000007F0000L,
			0x0000000000007F00L,
			0x000000000000007FL,
	};
	
	private final long[] cbit = new long[] {
			0x8000000000000000L,
			0x0080000000000000L,
			0x0000800000000000L,
			0x0000008000000000L,
			0x0000000080000000L,
			0x0000000000800000L,
			0x0000000000008000L,
			0x0000000000000080L,
	};
	
	private long[] buffer;
	private int pos;
	
	/**
	 * Create an Vary-length Integer Encoder
	 * @param buffsize The size of inner buffer for storing the data
	 */
	public VarInt8Codec(int buffsize){
		buffer = new long[buffsize];
	}
	
	public VarInt8Codec(){
		this(1024);
	}
	
	private void reset(){
		pos = 0;
		Arrays.fill(buffer, 0);
	}
	
	/**
	 * Encoding long integers into multiple bytes.
	 * The first bit for each byte is used to indicate whether there is another byte
	 * is needed for this integer. 0 means requiring while 1 means no.
	 * @see tudelft.hadex.structure.compression.IntegerCodec
	 */
	@Override
	public long[] blockEncode(long[] integers) {
		reset();
		for(long x: integers){
			while(x != 0) {
				buffer[pos >> 6] |= ((x & mask7bit) << (56 - (pos & 0x3f)));
				pos += 8;
				x >>>= 7;
			}
			buffer[(pos - 8) >> 6 ] |= cbit[(((pos - 8) & 0x3f) >> 3)];
		}
		return Arrays.copyOf(buffer, (pos >> 6) + 1);
	}

	/** FIXME if there are more then 8 bytes for codes
	 * Decode the integer from codes
	 * @see tudelft.hadex.structure.compression.IntegerCodec
	 */
	@Override
	public long[] blockDecode(long[] codes) {
		reset();
		int bytepos = 0;
		int st = -1;
		int idx = 0;
		while((bytepos >>> 3) < codes.length) {
			buffer[idx] |= bitloopmove((codes[bytepos >>> 3] & mask[bytepos & 0x7]), 70 - (bytepos - st) * 7 - (((bytepos & 0x7) + 1) * 8 - 1));
			if((codes[bytepos >>> 3] & cbit[bytepos & 0x7]) != 0){
				++idx;
				st = bytepos;
			}
			++bytepos;
		}
		return Arrays.copyOf(buffer, idx);
	}

}
