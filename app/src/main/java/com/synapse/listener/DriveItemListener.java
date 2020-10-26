package com.synapse.listener;

public interface DriveItemListener {
    void onRefresh();
    void onItemClick(String itemID,String itemName,String itemType);
    void onItemDelete(String itemName,String itemID,String itemType);

}
