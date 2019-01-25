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

package uk.co.caprica.vlcj.component;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.directaudio.AudioCallback;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

/**
 * Implementation for a type-safe builder for creating media player components.
 */
public final class MediaPlayerComponentBuilder implements
    MediaPlayerComponentBuilders.Factory,
    MediaPlayerComponentBuilders.MediaPlayers,
    MediaPlayerComponentBuilders.Embedded,
    MediaPlayerComponentBuilders.Audio,
    MediaPlayerComponentBuilders.Direct,
    MediaPlayerComponentBuilders.VideoFormat,
    MediaPlayerComponentBuilders.AudioFormat,
    MediaPlayerComponentBuilders.DirectVideo,
    MediaPlayerComponentBuilders.DirectAudio {

    /**
     * Each component implementation class must have a declared String[] field with this name.
     */
    private static final String DEFAULT_ARGS_FIELD_NAME = "DEFAULT_FACTORY_ARGUMENTS";

    private String[] factoryArgs;
    private String[] extraFactoryArgs;

    private MediaPlayerFactory mediaPlayerFactory;
    private FullScreenStrategy fullScreenStrategy;
    private Component videoSurfaceComponent;
    private Window overlay;
    private InputEvents inputEvents;

    private BufferFormatCallback bufferFormatCallback;
    private RenderCallback renderCallback;
    private boolean lockBuffers;

    private String audioFormat;
    private int audioRate;
    private int audioChannels;
    private AudioCallback audioCallback;

    /**
     *
     *
     * @return
     */
    public static MediaPlayerComponentBuilders.Factory mediaPlayerComponentBuilder() {
        return new MediaPlayerComponentBuilder();
    }

    @Override
    public MediaPlayerComponentBuilders.FactoryArgs withFactoryArgs(String... factoryArgs) {
        this.factoryArgs = factoryArgs;
        return this;
    }

    @Override
    public MediaPlayerComponentBuilders.FactoryArgs withExtraFactoryArgs(String... extraFactoryArgs) {
        this.extraFactoryArgs = extraFactoryArgs;
        return this;
    }

    @Override
    public MediaPlayerComponentBuilders.MediaPlayers withFactory(MediaPlayerFactory mediaPlayerFactory) {
        this.mediaPlayerFactory = mediaPlayerFactory;
        return this;
    }

    @Override
    public MediaPlayerComponentBuilders.Embedded embedded() {
        return this;
    }

    @Override
    public MediaPlayerComponentBuilders.Embedded withFullScreenStrategy(FullScreenStrategy fullScreenStrategy) {
        this.fullScreenStrategy = fullScreenStrategy;
        return this;
    }

    @Override
    public MediaPlayerComponentBuilders.Embedded withVideoSurfaceComponent(Component videoSurfaceComponent) {
        this.videoSurfaceComponent = videoSurfaceComponent;
        return this;
    }

    @Override
    public MediaPlayerComponentBuilders.Embedded withOverlay(Window overlay) {
        this.overlay = overlay;
        return this;
    }

    @Override
    public MediaPlayerComponentBuilders.Embedded withInputEvents(InputEvents inputEvents) {
        this.inputEvents = inputEvents;
        return this;
    }

    @Override
    public EmbeddedMediaPlayerComponent embeddedMediaPlayerComponent() {
        return new EmbeddedMediaPlayerComponent(
            getMediaPlayerFactory(EmbeddedMediaPlayerComponent.class),
            this.videoSurfaceComponent,
            this.fullScreenStrategy,
            this.inputEvents,
            this.overlay
        );
    }

    @Override
    public EmbeddedMediaListPlayerComponent embeddedMediaListPlayerComponent() {
        return new EmbeddedMediaListPlayerComponent(
            getMediaPlayerFactory(EmbeddedMediaListPlayerComponent.class),
            this.videoSurfaceComponent,
            this.fullScreenStrategy,
            this.inputEvents,
            this.overlay
        );
    }

    @Override
    public MediaPlayerComponentBuilders.Audio audio() {
        return this;
    }

    @Override
    public AudioMediaPlayerComponent audioMediaPlayerComponent() {
        return new AudioMediaPlayerComponent(
            getMediaPlayerFactory(AudioMediaPlayerComponent.class)
        );
    }

    @Override
    public AudioMediaListPlayerComponent audioMediaListPlayerComponent() {
        return new AudioMediaListPlayerComponent(
            getMediaPlayerFactory(AudioMediaListPlayerComponent.class)
        );
    }

    @Override
    public MediaPlayerComponentBuilders.Direct direct() {
        return this;
    }

    @Override
    public MediaPlayerComponentBuilders.DirectVideo withFormat(BufferFormatCallback bufferFormatCallback) {
        this.bufferFormatCallback = bufferFormatCallback;
        return this;
    }

    @Override
    public MediaPlayerComponentBuilders.DirectVideo withCallback(RenderCallback renderCallback) {
        this.renderCallback = renderCallback;
        return this;
    }

    @Override
    public MediaPlayerComponentBuilders.DirectVideo lockBuffers(boolean lockBuffers) {
        this.lockBuffers = lockBuffers;
        return this;
    }

    @Override
    public DirectMediaPlayerComponent directMediaPlayerComponent() {
        return new DirectMediaPlayerComponent(
            getMediaPlayerFactory(DirectMediaPlayerComponent.class),
            this.bufferFormatCallback,
            this.renderCallback,
            this.lockBuffers
        );
    }

    @Override
    public MediaPlayerComponentBuilders.DirectAudio withFormat(String format, int rate, int channels) {
        this.audioFormat = format;
        this.audioRate = rate;
        this.audioChannels = channels;
        return this;
    }

    @Override
    public MediaPlayerComponentBuilders.DirectAudio withCallback(AudioCallback audioCallback) {
        this.audioCallback = audioCallback;
        return this;
    }

    @Override
    public DirectAudioPlayerComponent directAudioPlayerComponent() {
        return new DirectAudioPlayerComponent(
            getMediaPlayerFactory(DirectAudioPlayerComponent.class),
            this.audioFormat,
            this.audioRate,
            this.audioChannels,
            this.audioCallback
        );
    }

    private MediaPlayerFactory getMediaPlayerFactory(Class<?> componentType) {
        if (this.mediaPlayerFactory == null) {
            List<String> allArgs = new ArrayList<String>();
            if (this.factoryArgs == null) {
                allArgs.addAll(Arrays.asList(getDefaultArgs(componentType)));
            } else {
                allArgs.addAll(Arrays.asList(this.factoryArgs));
            }
            if (this.extraFactoryArgs != null) {
                allArgs.addAll(Arrays.asList(this.extraFactoryArgs));
            }
            this.mediaPlayerFactory = new MediaPlayerFactory(allArgs);
        }
        return this.mediaPlayerFactory;
    }

    private String[] getDefaultArgs(Class<?> componentType) {
        try {
            Field field = componentType.getDeclaredField(DEFAULT_ARGS_FIELD_NAME);
            return (String[])field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private MediaPlayerComponentBuilder() {
    }

}
