package tudelft.hadex.structure.util;

import java.util.Arrays;

public class Gamma extends IntegerBitCodec{

	private long[] buffer;
	private int pos;
	
	public Gamma(int buffsize){
		buffer = new long[buffsize];
	}
	
	public Gamma(){
		this(1024);
	}
	
	private void reset(){
		pos = 0;
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
			len = bitLength(x) * 2 - 1;
			if((64 - (pos & 0x3f))  > len){
				buffer[pos>>6] |= (x << (64 - (pos & 0x3f) - len));
				pos += len;
			}
			else{
				buffer[pos>>6] |= (x >>> (len - (64 - (pos & 0x3f))));
				pos += len;
				buffer[pos>>6] |= (x << (64 - (pos & 0x3f)));
			}
		}
		return Arrays.copyOf(buffer, (pos >> 6) + 1);
	}

	/**
	 * Gamma Codec Decoder
	 * @param codes Compressed codes for integers
	 * @see tudelft.hadex.structure.util.IntegerBitCodec#blockDecode(long[])
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
				len = Long.numberOfLeadingZeros(code)* 2 + 1;
				buffer[idxint] = (code >>> (64 - len));
				if(len > (64 - (pos & 0x3f))){
					buffer[idxint] |= (codes[pos+1] >>> 128 - len - (pos & 0x3f));
				}
				idxint++;
				pos += len;
			}
			else if(((pos >> 6) + 1) < codes.length){
				len = (Long.numberOfLeadingZeros(codes[(pos >> 6) + 1]) + (64 - pos & 0x3f)) * 2 + 1;
				buffer[idxint] = codes[(pos >> 6) + 1] >>> (128 - len - (pos & 0x3f));
				idxint++;
				pos += len;
			}
			else break;
		}
		
		return Arrays.copyOf(buffer, idxint);
	}
}
