package tudelft.hadex.structure.compression;

import java.util.Arrays;

public class GammaCodec extends IntegerCodec{
	
	private long[] mask = new long[]{
			0xFFFFFFFFFFFFFFFFL, 0x7FFFFFFFFFFFFFFFL, 0x3FFFFFFFFFFFFFFFL, 0x1FFFFFFFFFFFFFFFL,
			0xFFFFFFFFFFFFFFFL, 0x7FFFFFFFFFFFFFFL, 0x3FFFFFFFFFFFFFFL, 0x1FFFFFFFFFFFFFFL,
			0xFFFFFFFFFFFFFFL, 0x7FFFFFFFFFFFFFL, 0x3FFFFFFFFFFFFFL, 0x1FFFFFFFFFFFFFL,
			0xFFFFFFFFFFFFFL, 0x7FFFFFFFFFFFFL, 0x3FFFFFFFFFFFFL, 0x1FFFFFFFFFFFFL,
			0xFFFFFFFFFFFFL, 0x7FFFFFFFFFFFL, 0x3FFFFFFFFFFFL, 0x1FFFFFFFFFFFL,
			0xFFFFFFFFFFFL, 0x7FFFFFFFFFFL, 0x3FFFFFFFFFFL, 0x1FFFFFFFFFFL,
			0xFFFFFFFFFFL, 0x7FFFFFFFFFL, 0x3FFFFFFFFFL, 0x1FFFFFFFFFL,
			0xFFFFFFFFFL, 0x7FFFFFFFFL, 0x3FFFFFFFFL, 0x1FFFFFFFFL,
			0xFFFFFFFFL, 0x7FFFFFFFL, 0x3FFFFFFFL, 0x1FFFFFFFL,
			0xFFFFFFFL, 0x7FFFFFFL, 0x3FFFFFFL, 0x1FFFFFFL,
			0xFFFFFFL, 0x7FFFFFL, 0x3FFFFFL, 0x1FFFFFL,
			0xFFFFFL, 0x7FFFFL, 0x3FFFFL, 0x1FFFFL,
			0xFFFFL, 0x7FFFL, 0x3FFFL, 0x1FFFL,
			0xFFFL, 0x7FFL, 0x3FFL, 0x1FFL,
			0xFFL, 0x7FL, 0x3FL, 0x1FL,
			0xFL, 0x7L, 0x3L, 0x1L,
			0x0L
	};
	
	private long[] buffer;
	private int pos;
	
	public GammaCodec(int buffsize){
		buffer = new long[buffsize];
	}
	
	public GammaCodec(){
		this(1024);
	}
	
	private void reset(){
		pos = 0;
		Arrays.fill(buffer, 0);
	}
	
	/** 
	 * Gamma Codec of integers are used especially for those integers that
	 * are hard to predict the maximum and it favors small integers.
	 * Ex. (1011) = 0001010
	 * @see tudelft.hadex.structure.util.IntegerEncoding#blockEncode(long[])
	 */
	@Override
	public long[] blockEncode(long[] longs) {
		reset();
		int len;
		for(long x: longs){
			len = bitLength(x);
			pos += len - 1;       // make the zero paddings
			if((64 - (pos & 0x3f))  >= len){
				buffer[pos>>6] |= (x << (64 - (pos & 0x3f) - len));
				pos += len;
			}
			else{
				buffer[pos>>6] |= (x >>> (len + (pos & 0x3f) - 64));
				pos += len;
				buffer[pos>>6] |= (x << (64 - (pos & 0x3f)));
			}
		}
		return Arrays.copyOf(buffer, (pos >> 6) + 1);
	}

	/**
	 * Gamma Codec Decoder
	 * @param codes Compressed codes for integers
	 * @see tudelft.hadex.structure.compression.IntegerCodec#blockDecode(long[])
	 */
	@Override
	public long[] blockDecode(long[] codes) {
		reset();
		
		int idxint = 0;
		long code = 0;
		int len = 0;
		while((pos >> 6) < codes.length){
			code = (codes[pos >> 6] << (pos & 0x3f));
			if(code!=0){
				len = Long.numberOfLeadingZeros(code) + 1;
				pos += len - 1;
				if((pos & 0x3f) + len <= 64) {
					buffer[idxint] = ((codes[pos >> 6] & mask[pos & 0x3f]) >>> (64 - (pos & 0x3f) - len));
				}
				else{
					buffer[idxint] = ((codes[pos >> 6] & mask[pos & 0x3f]) << ((pos & 0x3f) + len - 64));
					buffer[idxint] |= (codes[(pos >> 6)+1] >>> 128 - len - (pos & 0x3f));
				}
				idxint++;
				pos += len;
			}
			else if(((pos >> 6) + 1) < codes.length){
				len = (Long.numberOfLeadingZeros(codes[(pos >> 6) + 1]) + (64 - pos & 0x3f)) + 1;
				pos += len - 1;
				if((pos & 0x3f) + len <= 64) {
					buffer[idxint] = ((codes[pos >> 6] & mask[pos & 0x3f]) >>> (64 - (pos & 0x3f) - len));
				}
				else{
					buffer[idxint] = ((codes[pos >> 6] & mask[pos & 0x3f]) << ((pos & 0x3f) + len - 64));
					buffer[idxint] |= (codes[(pos >> 6)+1] >>> 128 - len - (pos & 0x3f));
				}
				idxint++;
				pos += len;
			}
			else break;
		}
		
		return Arrays.copyOf(buffer, idxint);
	}
}
