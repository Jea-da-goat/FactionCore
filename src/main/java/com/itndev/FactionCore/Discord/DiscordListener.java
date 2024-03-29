package com.itndev.FactionCore.Discord;

import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.Utils.CommonUtils;
import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.UserInfoUtils;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Locale;
import java.util.Random;

public class DiscordListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String[] cmd = e.getMessage().getContentRaw().split(" ");
        if(cmd[0].startsWith("!")) {
            cmd[0] = cmd[0].replaceFirst("!", "");
            performcommand(e.getAuthor(), cmd, e.getChannel(), e.getGuild());
        }
        cmd = null;
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent e) {
        try {
            String tag = e.getUser().getAsTag();
            String id = e.getUser().getId();
            System.out.println("[DISCORD] " + tag + "/" + id + " has left the server");
            RemoveUser(id);
            tag = null;
            id = null;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent e) {
        try {
            String tag = e.getUser().getAsTag();
            String id = e.getUser().getId();
            System.out.println("[DISCORD] " + tag + "/" + id + " has left the server");
            RemoveUser(id);
            tag = null;
            id = null;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private void RemoveUser(String DiscordID) {
        if(!AuthStorage.DISCORDID_TO_UUID.containsKey(DiscordID)) {
            return;
        }
        String UUID = AuthStorage.DISCORDID_TO_UUID.get(DiscordID);
        AuthStorage.DISCORDID_TO_UUID.remove(DiscordID);
        AuthStorage.UUID_TO_DISCORDID.remove(UUID);
        Storage.AddCommandToQueue("discord:=:auth:=:" + CommonUtils.String2Byte(UUID) + ":=:" + CommonUtils.String2Byte("NULL"));
        FactionUtils.SendFactionMessage(UUID, "puremessagesendoptiontrue", "single", "[디스코드]" + " 계정 인증이 풀렸습니다");
        UUID = null;
    }


    private void performcommand(User user, String[] args, TextChannel channel, Guild guild) {
        if(args[0].equalsIgnoreCase("연동")) {
            if(!channel.equals(BotConnect.mainchannel)) {
                channel.sendMessage("[" + user.getAsMention() + "]\n 해당 명령어는 " + ((TextChannel)BotConnect.mainchannel).getAsMention() + " 에서만 사용 가능합니다").queue();
                return;
            }
            if(args.length != 2) {
                BotConnect.mainchannel.sendMessage("[" + user.getAsMention() + "]\n 잘못된 명령어. `!연동 <닉네임>`").queue();
                return;
            }
            if(AuthStorage.DISCORDID_TO_UUID.containsKey(user.getId())) {
                BotConnect.mainchannel.sendMessage( "[" + user.getAsMention() + "]\n"
                        + "이미 해당 디스코드 계정은 `" + UserInfoUtils.getPlayerUUIDOriginName(AuthStorage.DISCORDID_TO_UUID.get(user.getId()))
                        + "` 계정과 연동되어 있습니다.\n !연동해제 로 연동을 해제하거나 인게임에서 `/연동해제` 로 연동을 해제할수 있습니다").queue();
                return;
            }
            String name = args[1].toLowerCase(Locale.ROOT);
            if(!UserInfoUtils.hasJoined(name)) {
                BotConnect.mainchannel.sendMessage("[" + user.getAsMention() + "]\n 해당 유저 `" + args[1] + "` 는 서버에 접속한적이 없습니다").queue();
                return;
            }
            String UUID = UserInfoUtils.getPlayerUUID(name);
            if(AuthStorage.hasAuth(UUID)) {
                BotConnect.mainchannel.sendMessage("[" + user.getAsMention() + "]\n 해당 유저는 이미 다른 디스코드 계정과 연동되어 있습니다.\n 인게임에서 `/연동해제` 로 연동을 해제할수 있습니다").queue();
                return;
            }
            Random random = new Random();
            Integer c = random.nextInt(999999);
            AuthStorage.AddAuth(UUID, String.valueOf(c), user.getId());
            BotConnect.mainchannel.sendMessage( "[" + user.getAsMention() + "]\n 성공적으로 연동요청을 넣었습니다.\n 연동을 인증하기 위해서는 인게임에서 `" + UserInfoUtils.getPlayerUUIDOriginName(UUID)
                    + "` 계정으로 `/연동 " + String.valueOf(c) + "` 를 입력하시면 됩니다").queue();

            name = null;
            UUID = null;
            random = null;
            c = null;
        } else if(args[0].equalsIgnoreCase("연동해제")) {
            if(!AuthStorage.DISCORDID_TO_UUID.containsKey(user.getId())) {
                BotConnect.mainchannel.sendMessage( "[" + user.getAsMention() + "]\n"
                        + "해당 계정은 연동이 되어있지 않습니다").queue();
                return;
            }
            String UUID = AuthStorage.DISCORDID_TO_UUID.get(user.getId());
            AuthStorage.DISCORDID_TO_UUID.remove(user.getId());
            AuthStorage.UUID_TO_DISCORDID.remove(UUID);
            Storage.AddCommandToQueue("discord:=:auth:=:" + CommonUtils.String2Byte(UUID) + ":=:" + CommonUtils.String2Byte("NULL"));
            FactionUtils.SendFactionMessage(UUID, "puremessagesendoptiontrue", "single", "[디스코드]" + " 계정 인증이 풀렸습니다");
            BotConnect.mainchannel.sendMessage( "[" + user.getAsMention() + "]\n"
                    + "해당 계정 `" + UserInfoUtils.getPlayerUUIDOriginName(UUID) + "` 와의 연동을 해제했습니다").queue();
            BotConnect.mainguild.removeRoleFromMember(user.getId(), BotConnect.mainguild.getRolesByName("USER", false).get(0)).queue();
            Member m = BotConnect.mainguild.retrieveMemberById(user.getId()).complete();
            m.modifyNickname(user.getName()).queue();
            UUID = null;
            m = null;
        } else if(args[0].equalsIgnoreCase("연동정보")) {
            if(!channel.equals(BotConnect.mainchannel)) {
                channel.sendMessage("[" + user.getAsMention() + "]\n 해당 명령어는 " + ((TextChannel)BotConnect.mainchannel).getAsMention() + " 에서만 사용 가능합니다").queue();
                return;
            }
            if(args.length != 2) {
                BotConnect.mainchannel.sendMessage("[" + user.getAsMention() + "]\n 잘못된 명령어. `!연동정보 <닉네임/유저태그>`").queue();
                return;
            }
            if(args[1].contains("#")) {
                String Tag = args[1];
                Member member = BotConnect.mainguild.getMemberByTag(Tag);
                if(member == null) {
                    BotConnect.mainchannel.sendMessage("[" + user.getAsMention() + "]\n 해당 유저 `" + Tag + "` (은)는 존재하지 않습니다").queue();
                    return;
                }
                String ID = member.getId();
                if(!AuthStorage.DISCORDID_TO_UUID.containsKey(ID)) {
                    BotConnect.mainchannel.sendMessage("[" + user.getAsMention() + "]\n 해당 유저 `" + Tag + "` (은)는 계정을 아직 연동하지 않았습니다").queue();
                    return;
                }
                String UUID = AuthStorage.DISCORDID_TO_UUID.get(ID);
                String Name = UserInfoUtils.getPlayerUUIDOriginName(UUID);
                BotConnect.mainchannel.sendMessage("[" + user.getAsMention() + "]\n 해당 유저 `" + Tag + "` 의 디스코드 계정은 마인크래프트 계정 `" + Name + "(UUID : " + UUID + ")` 와 연동되어 있습니다").queue();
            } else {
                String Name = args[1];
                if(!UserInfoUtils.hasJoined(Name.toLowerCase())) {
                    BotConnect.mainchannel.sendMessage("[" + user.getAsMention() + "]\n 해당 유저 `" + Name + "` (은)는 서버에 접속한적이 없습니다").queue();
                    return;
                }
                String UUID = UserInfoUtils.getPlayerUUID(Name.toLowerCase());
                String RName = UserInfoUtils.getPlayerUUIDOriginName(UUID);
                if(!AuthStorage.hasAuth(UUID)) {
                    BotConnect.mainchannel.sendMessage("[" + user.getAsMention() + "]\n 해당 유저 `" + RName + "` (은)는 계정을 아직 연동하지 않았습니다").queue();
                    return;
                }
                String DiscordID = AuthStorage.getID_FROM_UUID(UUID);
                Member member = BotConnect.mainguild.getMemberById(DiscordID);
                if(member == null) {
                    BotConnect.mainchannel.sendMessage("[" + user.getAsMention() + "]\n 해당 유저 `" + RName + "` (은)는 계정을 아직 연동하지 않았습니다").queue();
                    return;
                }
                String Tag = member.getUser().getAsTag();
                BotConnect.mainchannel.sendMessage("[" + user.getAsMention() + "]\n 해당 유저 `" + RName + "` 의 마인크래프트 계정은 디스코드 계정 `" + Tag + "` 와 연동되어 있습니다").queue();
            }
        }
    }
}
