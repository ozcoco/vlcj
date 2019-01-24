/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.media;

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_picture_t;
import uk.co.caprica.vlcj.binding.support.size_tByReference;
import uk.co.caprica.vlcj.enums.PictureType;

import java.io.FileOutputStream;
import java.io.OutputStream;

public final class Picture {

    private final int width;

    private final int height;

    private final int stride;

    private final PictureType type;

    private final long time;

    private final byte[] buffer;

    private final int size;

    public Picture(LibVlc libvlc, libvlc_picture_t picture) {
        this.width = libvlc.libvlc_picture_get_width(picture);
        this.height = libvlc.libvlc_picture_get_height(picture);
        this.stride = libvlc.libvlc_picture_get_stride(picture);
        this.type = PictureType.pictureType(libvlc.libvlc_picture_type(picture));
        this.time = libvlc.libvlc_picture_get_time(picture);
        this.buffer = initBuffer(libvlc, picture);
        this.size = this.buffer.length;
    }

    private byte[] initBuffer(LibVlc libvlc, libvlc_picture_t picture) {
        size_tByReference size = new size_tByReference();
        Pointer pointer = libvlc.libvlc_picture_get_buffer(picture, size);
        return pointer.getByteArray(0, size.getValue().intValue());
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int stride() {
        return stride;
    }

    public PictureType type() {
        return type;
    }

    public long time() {
        return time;
    }

    public byte[] buffer() {
        return buffer;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("width=").append(width).append(',');
        sb.append("height=").append(height).append(',');
        sb.append("stride=").append(stride).append(',');
        sb.append("type=").append(type).append(',');
        sb.append("time=").append(time).append(',');
        sb.append("size=").append(time).append(']');
        return sb.toString();
    }

}
