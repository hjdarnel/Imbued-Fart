package com.imbuedfart;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.SoundEffectPlayed;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import javax.sound.sampled.*;

@Slf4j
@PluginDescriptor(
        name = "Imbued Fart",
        enabledByDefault = false,
        description = "Plays a fart noise instead of the imbued heart sound"
)
public class ImbuedFartPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private ImbuedFartConfig config;

    private Clip clip;

    @Provides
    ImbuedFartConfig provideConfig(final ConfigManager configManager)
    {
        return configManager.getConfig(ImbuedFartConfig.class);
    }

    @Subscribe
    public void onCommandExecuted(CommandExecuted event)
    {
        if (event.getCommand().equals("fart"))
        {
            playFart();
        }
    }

    @Subscribe
    public void onSoundEffectPlayed(SoundEffectPlayed event)
    {
        if (event.getSoundId() == 3887)
        {
            event.consume();
            playFart();
        }
    }

    public void playFart()
    {
        try {
            if (clip != null)
            {
                clip.close();
            }

            AudioInputStream stream = AudioSystem.getAudioInputStream(ImbuedFartPlugin.class.getResource("fart.wav"));

            if (stream == null)
            {
                return;
            }

            AudioFormat format = stream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);

            clip.open(stream);

            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float volumeValue = volume.getMinimum() + ((50 + (config.volume()*5)) * ((volume.getMaximum() - volume.getMinimum()) / 100));

            volume.setValue(volumeValue);

            clip.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}