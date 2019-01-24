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

package uk.co.caprica.vlcj.player.events.media;

import uk.co.caprica.vlcj.enums.MediaParsedStatus;
import uk.co.caprica.vlcj.enums.State;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.Picture;

public interface MediaEventListener {

    /**
     * Current media meta data changed.
     *
     * @param media media that raised the event
     * @param metaType type of meta data that changed
     */
    void mediaMetaChanged(Media media, int metaType);

    /**
     * A new sub-item was added to the current media.
     *
     * @param media media that raised the event
     * @param subItem native sub-item handle
     */
    void mediaSubItemAdded(Media media, libvlc_media_t subItem);

    /**
     * The current media duration changed.
     *
     * @param media media that raised the event
     * @param newDuration new duration (number of milliseconds)
     */
    void mediaDurationChanged(Media media, long newDuration);

    /**
     * The current media parsed status changed.
     *
     * @param media media that raised the event
     * @param newStatus new parsed status
     */
    void mediaParsedChanged(Media media, MediaParsedStatus newStatus);

    /**
     * The current media was freed.
     *
     * @param media media that raised the event
     */
    void mediaFreed(Media media);

    /**
     * The current media state changed.
     *
     * @param media media that raised the event
     * @param newState new state
     */
    void mediaStateChanged(Media media, State newState);

    /**
     * A sub-item tree was added to the media.
     *
     * @param media media that raised the event
     * @param item media item
     */
    void mediaSubItemTreeAdded(Media media, libvlc_media_t item);

    /**
     *
     *  @param media
     * @param picture
     */
    void mediaThumbnailGenerated(Media media, Picture picture);

}
