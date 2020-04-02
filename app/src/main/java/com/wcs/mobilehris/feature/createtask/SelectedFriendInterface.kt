package com.wcs.mobilehris.feature.createtask

import com.wcs.mobilehris.feature.dtltask.FriendModel

interface SelectedFriendInterface {
    fun selectedItemFriend(friendModel: FriendModel)
    fun selectedDisplayFriend(friendModel : FriendModel)
}