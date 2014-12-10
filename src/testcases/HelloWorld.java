
package testcases;
/**
 * sdkfheui
 * sdfkeuq
 * @author xiaoq_zhu
 *
 */
public class HelloWorld {
	static final byte[][] ANY_TO_EIGHT = new byte[9][];
	
	static final byte[] ONE_TO_ONE_MAPPING = ANY_TO_EIGHT[8];
	
	static final int
	BLIT_SRC = 1,     // copy source directly, else applies logic operations
	BLIT_ALPHA = 2,   // enable alpha blending
	BLIT_DITHER = 4;  // enable dithering in low color modes

	
	static final int
	ALPHA_OPAQUE = 255,           // Fully opaque (ignores any alpha data)
	ALPHA_TRANSPARENT = 0,        // Fully transparent (ignores any alpha data)
	ALPHA_CHANNEL_SEPARATE = -1,  // Use alpha channel from separate alphaData
	ALPHA_CHANNEL_SOURCE = -2,    // Use alpha channel embedded in sourceData
	ALPHA_MASK_UNPACKED = -3,     // Use transparency mask formed by bytes in alphaData (non-zero is opaque)
	ALPHA_MASK_PACKED = -4,       // Use transparency mask formed by packed bits in alphaData
	ALPHA_MASK_INDEX = -5,        // Consider source palette indices transparent if in alphaData array
	ALPHA_MASK_RGB = -6;          // Consider source RGBs transparent if in RGB888 format alphaData array
	
	static final int LSB_FIRST = 0;
	static final int MSB_FIRST = 1;
	
	private static final int
	// direct / true color formats with arbitrary masks & shifts
	TYPE_GENERIC_8 = 0,
	TYPE_GENERIC_16_MSB = 1,
	TYPE_GENERIC_16_LSB = 2,
	TYPE_GENERIC_24 = 3,
	TYPE_GENERIC_32_MSB = 4,
	TYPE_GENERIC_32_LSB = 5,
	// palette indexed color formats
	TYPE_INDEX_8 = 6,
	TYPE_INDEX_4 = 7,
	TYPE_INDEX_2 = 8,
	TYPE_INDEX_1_MSB = 9,
	TYPE_INDEX_1_LSB = 10;
	
	static void blit(int op,
			byte[] srcData, int srcDepth, int srcStride, int srcOrder,
			int srcX, int srcY, int srcWidth, int srcHeight,
			byte[] srcReds, byte[] srcGreens, byte[] srcBlues,
			int alphaMode, byte[] alphaData, int alphaStride, int alphaX, int alphaY,
			byte[] destData, int destDepth, int destStride, int destOrder,
			int destX, int destY, int destWidth, int destHeight,
			byte[] destReds, byte[] destGreens, byte[] destBlues,
			boolean flipX, boolean flipY) {
			if ((destWidth <= 0) || (destHeight <= 0) || (alphaMode == ALPHA_TRANSPARENT)) return;

			/*** Prepare scaling data ***/
			final int dwm1 = destWidth - 1;
			final int sfxi = (dwm1 != 0) ? (int)((((long)srcWidth << 16) - 1) / dwm1) : 0;
			final int dhm1 = destHeight - 1;
			final int sfyi = (dhm1 != 0) ? (int)((((long)srcHeight << 16) - 1) / dhm1) : 0;

			/*** Prepare source-related data ***/
			final int stype;
			switch (srcDepth) {
				case 8:
					stype = TYPE_INDEX_8;
					break;
				case 4:
					srcStride <<= 1;
					stype = TYPE_INDEX_4;
					break;
				case 2:
					srcStride <<= 2;
					stype = TYPE_INDEX_2;
					break;
				case 1:
					srcStride <<= 3;
					stype = (srcOrder == MSB_FIRST) ? TYPE_INDEX_1_MSB : TYPE_INDEX_1_LSB;
					break;
				default:
					//throw new IllegalArgumentException("Invalid source type");
					return;		
			}			
			int spr = srcY * srcStride + srcX;

			/*** Prepare destination-related data ***/
			final int dtype;
			switch (destDepth) {
				case 8:
					dtype = TYPE_INDEX_8;
					break;
				case 4:
					destStride <<= 1;
					dtype = TYPE_INDEX_4;
					break;
				case 2:
					destStride <<= 2;
					dtype = TYPE_INDEX_2;
					break;
				case 1:
					destStride <<= 3;
					dtype = (destOrder == MSB_FIRST) ? TYPE_INDEX_1_MSB : TYPE_INDEX_1_LSB;
					break;
				default:
					//throw new IllegalArgumentException("Invalid source type");
					return;
			}			
			int dpr = ((flipY) ? destY + dhm1 : destY) * destStride + ((flipX) ? destX + dwm1 : destX);
			final int dprxi = (flipX) ? -1 : 1;
			final int dpryi = (flipY) ? -destStride : destStride;

			/*** Prepare special processing data ***/
			int apr;
			if ((op & BLIT_ALPHA) != 0) {
				switch (alphaMode) {
					case ALPHA_MASK_UNPACKED:
					case ALPHA_CHANNEL_SEPARATE:
						if (alphaData == null) alphaMode = 0x10000;
						apr = alphaY * alphaStride + alphaX;
						break;
					case ALPHA_MASK_PACKED:
						if (alphaData == null) alphaMode = 0x10000;
						alphaStride <<= 3;
						apr = alphaY * alphaStride + alphaX;
						break;
					case ALPHA_MASK_INDEX:
					case ALPHA_MASK_RGB:
						if (alphaData == null) alphaMode = 0x10000;
						apr = 0;
						break;
					default:
						alphaMode = (alphaMode << 16) / 255; // prescale
					case ALPHA_CHANNEL_SOURCE:
						apr = 0;
						break;
				}
			} else {
				alphaMode = 0x10000;
				apr = 0;
			}
			
			/*******************************************/
			final boolean ditherEnabled = (op & BLIT_DITHER) != 0;

			/*** Blit ***/
			int dp = dpr;
			int sp = spr;
			int ap = apr;
			int destPaletteSize = 1 << destDepth;
			if ((destReds != null) && (destReds.length < destPaletteSize)) destPaletteSize = destReds.length;
			byte[] paletteMapping = null;
			boolean isExactPaletteMapping = true;
			switch (alphaMode) {
				case 0x10000:
					/*** If the palettes and formats are equivalent use a one-to-one mapping ***/
					if ((stype == dtype) &&
						(srcReds == destReds) && (srcGreens == destGreens) && (srcBlues == destBlues)) {
						paletteMapping = ONE_TO_ONE_MAPPING;
						break;
					/*** If palettes have not been supplied, supply a suitable mapping ***/
					} else if ((srcReds == null) || (destReds == null)) {
						if (srcDepth <= destDepth) {
							paletteMapping = ONE_TO_ONE_MAPPING;
						} else {
							paletteMapping = new byte[1 << srcDepth];
							int mask = (0xff << destDepth) >>> 8;
							for (int i = 0; i < paletteMapping.length; ++i) paletteMapping[i] = (byte)(i & mask);
						}
						break;
					}
				case ALPHA_MASK_UNPACKED:
				case ALPHA_MASK_PACKED:
				case ALPHA_MASK_INDEX:
				case ALPHA_MASK_RGB:
					/*** Generate a palette mapping ***/
					int srcPaletteSize = 1 << srcDepth;
					paletteMapping = new byte[srcPaletteSize];
					if ((srcReds != null) && (srcReds.length < srcPaletteSize)) srcPaletteSize = srcReds.length;
					for (int i = 0, r, g, b, index; i < srcPaletteSize; ++i) {
						r = srcReds[i] & 0xff;
						g = srcGreens[i] & 0xff;
						b = srcBlues[i] & 0xff;
						index = 0;
						int minDistance = 0x7fffffff;
						for (int j = 0, dr, dg, db, distance; j < destPaletteSize; ++j) {
							dr = (destReds[j] & 0xff) - r;
							dg = (destGreens[j] & 0xff) - g;
							db = (destBlues[j] & 0xff) - b;
							distance = dr * dr + dg * dg + db * db;
							if (distance < minDistance) {
								index = j;
								if (distance == 0) break;
								minDistance = distance;
							}
						}
						paletteMapping[i] = (byte)index;
						if (minDistance != 0) isExactPaletteMapping = false;
					}
					break;
			}
			if ((paletteMapping != null) && (isExactPaletteMapping || ! ditherEnabled)) {
				if ((stype == dtype) && (alphaMode == 0x10000)) {
					/*** Fast blit (copy w/ mapping) ***/
					switch (stype) {
						case TYPE_INDEX_8:
							for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
								for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
									destData[dp] = paletteMapping[srcData[sp] & 0xff];
									sp += (sfx >>> 16);
								}
							}
							break;					
						case TYPE_INDEX_4:
							for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
								for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
									final int v;
									if ((sp & 1) != 0) v = paletteMapping[srcData[sp >> 1] & 0x0f];
									else v = (srcData[sp >> 1] >>> 4) & 0x0f;
									sp += (sfx >>> 16);
									if ((dp & 1) != 0) destData[dp >> 1] = (byte)((destData[dp >> 1] & 0xf0) | v);
									else destData[dp >> 1] = (byte)((destData[dp >> 1] & 0x0f) | (v << 4));
								}
							}
							break;
						case TYPE_INDEX_2:
							for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
								for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
									final int index = paletteMapping[(srcData[sp >> 2] >>> (6 - (sp & 3) * 2)) & 0x03];
									sp += (sfx >>> 16);
									final int shift = 6 - (dp & 3) * 2;
									destData[dp >> 2] = (byte)(destData[dp >> 2] & ~(0x03 << shift) | (index << shift));
								}
							}
							break;					
						case TYPE_INDEX_1_MSB:
							for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
								for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
									final int index = paletteMapping[(srcData[sp >> 3] >>> (7 - (sp & 7))) & 0x01];
									sp += (sfx >>> 16);
									final int shift = 7 - (dp & 7);
									destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (index << shift));
								}
							}
							break;					
						case TYPE_INDEX_1_LSB:
							for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
								for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
									final int index = paletteMapping[(srcData[sp >> 3] >>> (sp & 7)) & 0x01];
									sp += (sfx >>> 16);
									final int shift = dp & 7;
									destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (index << shift));
								}
							}
							break;
					}
				} 
			else {
					/*** Convert between indexed modes using mapping and mask ***/
					for (int dy = destHeight, sfy = sfyi; dy > 0; --dy,
							sp = spr += (sfy >>> 16) * srcStride,
							sfy = (sfy & 0xffff) + sfyi,
							dp = dpr += dpryi) {
						for (int dx = destWidth, sfx = sfxi; dx > 0; --dx,
								dp += dprxi,
								sfx = (sfx & 0xffff) + sfxi) {
							int index;
							/*** READ NEXT PIXEL ***/
							switch (stype) {
								case TYPE_INDEX_8:
									index = srcData[sp] & 0xff;
									sp += (sfx >>> 16);
									break;					
								case TYPE_INDEX_4:
									if ((sp & 1) != 0) index = srcData[sp >> 1] & 0x0f;
									else index = (srcData[sp >> 1] >>> 4) & 0x0f;
									sp += (sfx >>> 16);
									break;					
								case TYPE_INDEX_2:
									index = (srcData[sp >> 2] >>> (6 - (sp & 3) * 2)) & 0x03;
									sp += (sfx >>> 16);
									break;					
								case TYPE_INDEX_1_MSB:
									index = (srcData[sp >> 3] >>> (7 - (sp & 7))) & 0x01;
									sp += (sfx >>> 16);
									break;					
								case TYPE_INDEX_1_LSB:
									index = (srcData[sp >> 3] >>> (sp & 7)) & 0x01;
									sp += (sfx >>> 16);
									break;
								default:
									return;
							}
							/*** APPLY MASK ***/
							switch (alphaMode) {
								case ALPHA_MASK_UNPACKED: {
									final byte mask = alphaData[ap];
									ap += (sfx >> 16);
									if (mask == 0) continue;
								} break;
								case ALPHA_MASK_PACKED: {
									final int mask = alphaData[ap >> 3] & (1 << (ap & 7));
									ap += (sfx >> 16);
									if (mask == 0) continue;
								} break;
								case ALPHA_MASK_INDEX: {
									int i = 0;
									while (i < alphaData.length) {
										if (index == (alphaData[i] & 0xff)) break;
									}
									if (i < alphaData.length) continue;
								} break;
								case ALPHA_MASK_RGB: {
									final byte r = srcReds[index], g = srcGreens[index], b = srcBlues[index];
									int i = 0;
									while (i < alphaData.length) {
										if ((r == alphaData[i]) && (g == alphaData[i + 1]) && (b == alphaData[i + 2])) break;
										i += 3;
									}
									if (i < alphaData.length) continue;
								} break;
							}
							index = paletteMapping[index] & 0xff;
					
							/*** WRITE NEXT PIXEL ***/
							switch (dtype) {
								case TYPE_INDEX_8:
									destData[dp] = (byte) index;
									break;
								case TYPE_INDEX_4:
									if ((dp & 1) != 0) destData[dp >> 1] = (byte)((destData[dp >> 1] & 0xf0) | index);
									else destData[dp >> 1] = (byte)((destData[dp >> 1] & 0x0f) | (index << 4));
									break;					
								case TYPE_INDEX_2: {
									final int shift = 6 - (dp & 3) * 2;
									destData[dp >> 2] = (byte)(destData[dp >> 2] & ~(0x03 << shift) | (index << shift));
								} break;					
								case TYPE_INDEX_1_MSB: {
									final int shift = 7 - (dp & 7);
									destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (index << shift));
								} break;
								case TYPE_INDEX_1_LSB: {
									final int shift = dp & 7;
									destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (index << shift));
								} break;					
							}
						}
					}
				}
				return;
			}
				
			/*** Comprehensive blit (apply transformations) ***/
			int alpha = alphaMode;
			int index = 0;
			int indexq = 0;
			int lastindex = 0, lastr = -1, lastg = -1, lastb = -1;
			final int[] rerr, gerr, berr;
			if (ditherEnabled) {
				rerr = new int[destWidth + 2];
				gerr = new int[destWidth + 2];
				berr = new int[destWidth + 2];
			} else {
				rerr = null; gerr = null; berr = null;
			}
			for (int dy = destHeight, sfy = sfyi; dy > 0; --dy,
					sp = spr += (sfy >>> 16) * srcStride,
					ap = apr += (sfy >>> 16) * alphaStride,
					sfy = (sfy & 0xffff) + sfyi,
					dp = dpr += dpryi) {
				int lrerr = 0, lgerr = 0, lberr = 0;
				for (int dx = destWidth, sfx = sfxi; dx > 0; --dx,
						dp += dprxi,
						sfx = (sfx & 0xffff) + sfxi) {
					/*** READ NEXT PIXEL ***/
					switch (stype) {
						case TYPE_INDEX_8:
							index = srcData[sp] & 0xff;
							sp += (sfx >>> 16);
							break;
						case TYPE_INDEX_4:
							if ((sp & 1) != 0) index = srcData[sp >> 1] & 0x0f;
							else index = (srcData[sp >> 1] >>> 4) & 0x0f;
							sp += (sfx >>> 16);
							break;
						case TYPE_INDEX_2:
							index = (srcData[sp >> 2] >>> (6 - (sp & 3) * 2)) & 0x03;
							sp += (sfx >>> 16);
							break;
						case TYPE_INDEX_1_MSB:
							index = (srcData[sp >> 3] >>> (7 - (sp & 7))) & 0x01;
							sp += (sfx >>> 16);
							break;
						case TYPE_INDEX_1_LSB:
							index = (srcData[sp >> 3] >>> (sp & 7)) & 0x01;
							sp += (sfx >>> 16);
							break;
					}

					/*** DO SPECIAL PROCESSING IF REQUIRED ***/
					int r = srcReds[index] & 0xff, g = srcGreens[index] & 0xff, b = srcBlues[index] & 0xff;
					switch (alphaMode) {
						case ALPHA_CHANNEL_SEPARATE:
							alpha = ((alphaData[ap] & 0xff) << 16) / 255;
							ap += (sfx >> 16);
							break;
						case ALPHA_MASK_UNPACKED:
							alpha = (alphaData[ap] != 0) ? 0x10000 : 0;
							ap += (sfx >> 16);
							break;						
						case ALPHA_MASK_PACKED:
							alpha = (alphaData[ap >> 3] << ((ap & 7) + 9)) & 0x10000;
							ap += (sfx >> 16);
							break;
						case ALPHA_MASK_INDEX: { // could speed up using binary search if we sorted the indices
							int i = 0;
							while (i < alphaData.length) {
								if (index == (alphaData[i] & 0xff)) break;
							}
							if (i < alphaData.length) continue;
						} break;
						case ALPHA_MASK_RGB: {
							int i = 0;
							while (i < alphaData.length) {
								if ((r == (alphaData[i] & 0xff)) &&
									(g == (alphaData[i + 1] & 0xff)) &&
									(b == (alphaData[i + 2] & 0xff))) break;
								i += 3;
							}
							if (i < alphaData.length) continue;
						} break;
					}
					if (alpha != 0x10000) {
						if (alpha == 0x0000) continue;
						switch (dtype) {
							case TYPE_INDEX_8:
								indexq = destData[dp] & 0xff;
								break;
							case TYPE_INDEX_4:
								if ((dp & 1) != 0) indexq = destData[dp >> 1] & 0x0f;
								else indexq = (destData[dp >> 1] >>> 4) & 0x0f;
								break;
							case TYPE_INDEX_2:
								indexq = (destData[dp >> 2] >>> (6 - (dp & 3) * 2)) & 0x03;
								break;
							case TYPE_INDEX_1_MSB:
								indexq = (destData[dp >> 3] >>> (7 - (dp & 7))) & 0x01;
								break;
							case TYPE_INDEX_1_LSB:
								indexq = (destData[dp >> 3] >>> (dp & 7)) & 0x01;
								break;
						}
						// Perform alpha blending
						final int rq = destReds[indexq] & 0xff;
						final int gq = destGreens[indexq] & 0xff;
						final int bq = destBlues[indexq] & 0xff;
						r = rq + ((r - rq) * alpha >> 16);
						g = gq + ((g - gq) * alpha >> 16);
						b = bq + ((b - bq) * alpha >> 16);
					}

					/*** MAP COLOR TO THE PALETTE ***/
					if (ditherEnabled) {
						// Floyd-Steinberg error diffusion
						r += rerr[dx] >> 4;
						if (r < 0) r = 0; else if (r > 255) r = 255;
						g += gerr[dx] >> 4;
						if (g < 0) g = 0; else if (g > 255) g = 255;
						b += berr[dx] >> 4;
						if (b < 0) b = 0; else if (b > 255) b = 255;
						rerr[dx] = lrerr;
						gerr[dx] = lgerr;
						berr[dx] = lberr;
					}
					if (r != lastr || g != lastg || b != lastb) {
						// moving the variable declarations out seems to make the JDK JIT happier...
						for (int j = 0, dr, dg, db, distance, minDistance = 0x7fffffff; j < destPaletteSize; ++j) {
							dr = (destReds[j] & 0xff) - r;
							dg = (destGreens[j] & 0xff) - g;
							db = (destBlues[j] & 0xff) - b;
							distance = dr * dr + dg * dg + db * db;
							if (distance < minDistance) {
								lastindex = j;
								if (distance == 0) break;
								minDistance = distance;
							}
						}
						lastr = r; lastg = g; lastb = b;
					}
					if (ditherEnabled) {
						// Floyd-Steinberg error diffusion, cont'd...
						final int dxm1 = dx - 1, dxp1 = dx + 1;
						int acc;
						rerr[dxp1] += acc = (lrerr = r - (destReds[lastindex] & 0xff)) + lrerr + lrerr;
						rerr[dx] += acc += lrerr + lrerr;
						rerr[dxm1] += acc + lrerr + lrerr;
						gerr[dxp1] += acc = (lgerr = g - (destGreens[lastindex] & 0xff)) + lgerr + lgerr;
						gerr[dx] += acc += lgerr + lgerr;
						gerr[dxm1] += acc + lgerr + lgerr;
						berr[dxp1] += acc = (lberr = b - (destBlues[lastindex] & 0xff)) + lberr + lberr;
						berr[dx] += acc += lberr + lberr;
						berr[dxm1] += acc + lberr + lberr;
					}

					/*** WRITE NEXT PIXEL ***/
					switch (dtype) {
						case TYPE_INDEX_8:
							destData[dp] = (byte) lastindex;
							break;
						case TYPE_INDEX_4:
							if ((dp & 1) != 0) destData[dp >> 1] = (byte)((destData[dp >> 1] & 0xf0) | lastindex);
							else destData[dp >> 1] = (byte)((destData[dp >> 1] & 0x0f) | (lastindex << 4));
							break;
						case TYPE_INDEX_2: {
							final int shift = 6 - (dp & 3) * 2;
							destData[dp >> 2] = (byte)(destData[dp >> 2] & ~(0x03 << shift) | (lastindex << shift));
						} break;					
						case TYPE_INDEX_1_MSB: {
							final int shift = 7 - (dp & 7);
							destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (lastindex << shift));
						} break;
						case TYPE_INDEX_1_LSB: {
							final int shift = dp & 7;
							destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (lastindex << shift));
						} break;					
					}
				}
			}
		}
	
}


