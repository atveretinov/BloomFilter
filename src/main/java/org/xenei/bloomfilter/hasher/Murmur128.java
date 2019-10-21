package org.xenei.bloomfilter.hasher;

import java.nio.ByteBuffer;

public class Murmur128 implements Hasher.Func  {

    long[] parts = null;

    @Override
    public String getName() {
        return "Murmur128-SC";
    }

    @Override
    public long applyAsLong(ByteBuffer buffer, Integer seed) {
        if (parts == null || seed == 0)
        {
            parts = hash3_x64_128( buffer, 0, buffer.limit(), seed );
            return parts[0];
        }
        else {
            parts[0] += parts[1];
        }
        return parts[0];
    }




    /**************************************
     * Methods to perform murmur 128 hash.
     **************************************/
    private long getblock(ByteBuffer key, int offset, int index) {
        int i_8 = index << 3;
        int blockOffset = offset + i_8;
        return ((long) key.get(blockOffset + 0) & 0xff) + (((long) key.get(blockOffset + 1) & 0xff) << 8) +
            (((long) key.get(blockOffset + 2) & 0xff) << 16) + (((long) key.get(blockOffset + 3) & 0xff) << 24) +
            (((long) key.get(blockOffset + 4) & 0xff) << 32) + (((long) key.get(blockOffset + 5) & 0xff) << 40) +
            (((long) key.get(blockOffset + 6) & 0xff) << 48) + (((long) key.get(blockOffset + 7) & 0xff) << 56);
    }

    private long rotl64(long v, int n) {
        return ((v << n) | (v >>> (64 - n)));
    }

    private long fmix(long k) {
        k ^= k >>> 33;
        k *= 0xff51afd7ed558ccdL;
        k ^= k >>> 33;
        k *= 0xc4ceb9fe1a85ec53L;
        k ^= k >>> 33;
        return k;
    }

    private long[] hash3_x64_128(ByteBuffer key, int offset, int length, long seed) {
        final int nblocks = length >> 4; // Process as 128-bit blocks.
        long h1 = seed;
        long h2 = seed;
        long c1 = 0x87c37b91114253d5L;
        long c2 = 0x4cf5ad432745937fL;
        // ----------
        // body
        for (int i = 0; i < nblocks; i++) {
            long k1 = getblock(key, offset, i * 2 + 0);
            long k2 = getblock(key, offset, i * 2 + 1);
            k1 *= c1;
            k1 = rotl64(k1, 31);
            k1 *= c2;
            h1 ^= k1;
            h1 = rotl64(h1, 27);
            h1 += h2;
            h1 = h1 * 5 + 0x52dce729;
            k2 *= c2;
            k2 = rotl64(k2, 33);
            k2 *= c1;
            h2 ^= k2;
            h2 = rotl64(h2, 31);
            h2 += h1;
            h2 = h2 * 5 + 0x38495ab5;
        }
        // ----------
        // tail
        // Advance offset to the unprocessed tail of the data.
        offset += nblocks * 16;
        long k1 = 0;
        long k2 = 0;
        switch (length & 15) {
        case 15:
            k2 ^= ((long) key.get(offset + 14)) << 48;
            // fallthrough
        case 14:
            k2 ^= ((long) key.get(offset + 13)) << 40;
            // fallthrough
        case 13:
            k2 ^= ((long) key.get(offset + 12)) << 32;
            // fallthrough
        case 12:
            k2 ^= ((long) key.get(offset + 11)) << 24;
            // fallthrough
        case 11:
            k2 ^= ((long) key.get(offset + 10)) << 16;
            // fallthrough
        case 10:
            k2 ^= ((long) key.get(offset + 9)) << 8;
            // fallthrough
        case 9:
            k2 ^= ((long) key.get(offset + 8)) << 0;
            k2 *= c2;
            k2 = rotl64(k2, 33);
            k2 *= c1;
            h2 ^= k2;
            // fallthrough
        case 8:
            k1 ^= ((long) key.get(offset + 7)) << 56;
            // fallthrough
        case 7:
            k1 ^= ((long) key.get(offset + 6)) << 48;
            // fallthrough
        case 6:
            k1 ^= ((long) key.get(offset + 5)) << 40;
            // fallthrough
        case 5:
            k1 ^= ((long) key.get(offset + 4)) << 32;
            // fallthrough
        case 4:
            k1 ^= ((long) key.get(offset + 3)) << 24;
            // fallthrough
        case 3:
            k1 ^= ((long) key.get(offset + 2)) << 16;
            // fallthrough
        case 2:
            k1 ^= ((long) key.get(offset + 1)) << 8;
            // fallthrough
        case 1:
            k1 ^= (key.get(offset));
            k1 *= c1;
            k1 = rotl64(k1, 31);
            k1 *= c2;
            h1 ^= k1;
            break;
        default: // 0
            // do nothing

        }
        // ----------
        // finalization
        h1 ^= length;
        h2 ^= length;
        h1 += h2;
        h2 += h1;
        h1 = fmix(h1);
        h2 = fmix(h2);
        h1 += h2;
        h2 += h1;
        return new long[] {h1, h2};
    }



}
