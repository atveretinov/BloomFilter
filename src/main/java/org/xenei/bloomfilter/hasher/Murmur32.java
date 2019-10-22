package org.xenei.bloomfilter.hasher;

import java.nio.ByteBuffer;

import org.xenei.bloomfilter.Hasher;
import org.xenei.bloomfilter.hasher.MurmurHash3.LongPair;

/**
 *  The MurmurHash3 algorithm was created by Austin Appleby and placed in the public domain.
 *  This java port was authored by Yonik Seeley and also placed into the public domain.
 *  The author hereby disclaims copyright to this source code.
 *  <p>
 *  This produces exactly the same hash values as the final C++
 *  version of MurmurHash3 and is thus suitable for producing the same hash values across
 *  platforms.
 *  <p>
 *  The 32 bit x86 version of this hash should be the fastest variant for relatively short keys like ids.
 *  murmurhash3_x64_128 is a good choice for longer strings or if you need more than 32 bits of hash.
 *  <p>
 *  Note - The x86 and x64 versions do _not_ produce the same results, as the
 *  algorithms are optimized for their respective platforms.
 *  <p>
 *  See http://github.com/yonik/java_util for future updates to this file.
 */
public class Murmur32 implements Hasher.Func  {

    public static final String name = "Murmur3_x86_32-SI";

    @Override
    public long applyAsLong(ByteBuffer buffer, Integer seed) {
        return MurmurHash3.murmurhash3_x86_32( buffer, 0, buffer.limit(), seed );

    }
}
