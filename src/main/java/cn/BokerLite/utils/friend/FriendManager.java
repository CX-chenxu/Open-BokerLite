package cn.BokerLite.utils.friend;

import cn.BokerLite.modules.value.Labels;

import java.util.ArrayList;

public class FriendManager {
    public static Labels<String> friendName = new Labels<>("Friend Name", "");
    public static ArrayList<String> friendsList = new ArrayList<>();

    public static void addFriend(String friendname) {
        if (!friendsList.contains(friendname)) {
            friendsList.add(friendname);
            saveFriendList();
        }
    }

    private static void saveFriendList() {
        //TODO: 保存好友列表
    }

    public static void removeFriend(String friendname) {
        if (friendsList.contains(friendname)) {
            friendsList.remove(friendname);
            saveFriendList();
        }
    }

    public static boolean isFriend(String player) {
        if (friendsList.size() < 1)
            return false;
        for (String s : friendsList) {
            if (s.equals(player))
                return true;
        }
        return false;
    }

    public static void clear() {
        if (!friendsList.isEmpty()) {
            friendsList.clear();
            saveFriendList();
        }
    }
}