package ru.kelcuprum.waterplayer.frontend.gui.components;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.buttons.base.Button;
import ru.kelcuprum.waterplayer.WaterPlayer;
import ru.kelcuprum.waterplayer.frontend.gui.TexturesHelper;
import ru.kelcuprum.waterplayer.frontend.localization.Music;
import ru.kelcuprum.waterplayer.frontend.localization.StarScript;

import static ru.kelcuprum.waterplayer.frontend.gui.components.TrackButton.confirmLinkNow;

public class CurrentTrackButton extends Button {
    protected Screen screen;
    public CurrentTrackButton(int x, int y, int width, Screen screen) {
        super(x, y, width, 40, InterfaceUtils.DesignType.FLAT, Component.empty(), null);
        this.screen = screen;
    }
    @Override
    public void onPress(){
        if(WaterPlayer.player.getAudioPlayer().getPlayingTrack() != null){
            confirmLinkNow(screen, WaterPlayer.player.getAudioPlayer().getPlayingTrack().getInfo().uri);
        }
    }
    public int getHeight(){
        return (WaterPlayer.player.getAudioPlayer().getPlayingTrack() != null) ? 42 : 40;
    }
    @Override
    public void renderText(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if(WaterPlayer.player.getAudioPlayer().getPlayingTrack() != null){
            this.active = true;
            AudioTrack track = WaterPlayer.player.getAudioPlayer().getPlayingTrack();
            ResourceLocation icon = track.getInfo().artworkUrl != null ? TexturesHelper.getTexture(track.getInfo().artworkUrl, (track.getSourceManager().getSourceName()+"_"+track.getInfo().identifier)) : new ResourceLocation("waterplayer", "textures/no_icon.png");
            guiGraphics.blit(icon, getX()+2, getY()+2, 0.0F, 0.0F, 36, 36, 36, 36);
            StringBuilder builder = new StringBuilder();
            if (!Music.isAuthorNull(track)) builder.append("«").append(Music.getAuthor(track)).append("» ");
            builder.append(Music.getTitle(track));
            if(getWidth()-50 < AlinLib.MINECRAFT.font.width(builder.toString())){
                guiGraphics.drawString(AlinLib.MINECRAFT.font, AlinLib.MINECRAFT.font.substrByWidth(FormattedText.of(builder.toString()), getWidth() - 50 - AlinLib.MINECRAFT.font.width("...")).getString()+"...", getX()+45, getY()+8, -1);
            } else {
                guiGraphics.drawString(AlinLib.MINECRAFT.font, builder.toString(), getX()+45, getY()+8, -1);
            }
            guiGraphics.drawString(AlinLib.MINECRAFT.font, track.getInfo().isStream ? WaterPlayer.localization.getLocalization("format.live") :  StarScript.getTimestamp(Music.getPosition(track)) + " / " + StarScript.getTimestamp(Music.getDuration(track)), getX()+45, getY()+30-AlinLib.MINECRAFT.font.lineHeight, -1);
            int color = WaterPlayer.player.getAudioPlayer().isPaused() ? InterfaceUtils.Colors.CLOWNFISH : track.getInfo().isStream ? InterfaceUtils.Colors.GROUPIE : InterfaceUtils.Colors.SEADRIVE;
            double state = track.getInfo().isStream ? 1 : ((double) track.getPosition() /track.getDuration());
            guiGraphics.fill(getX(), getY()+40, getX()+getWidth(), getY()+42, color-0x7f000000);
            guiGraphics.fill(getX(), getY()+40, (int) (getX()+(getWidth()*state)), getY()+42, color-0x7f000000);
        } else {
            this.active = false;
            guiGraphics.drawCenteredString(AlinLib.MINECRAFT.font, Component.translatable("waterplayer.command.now_playing.notPlaying"), getX()+(getWidth()/2),  getY()+20-(AlinLib.MINECRAFT.font.lineHeight/2), -1);
        }

    }
}
