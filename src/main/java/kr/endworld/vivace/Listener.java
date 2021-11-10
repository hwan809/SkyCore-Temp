package kr.endworld.vivace;

import kr.endworld.vivace.utils.ItemStackManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class Listener implements org.bukkit.event.Listener {

    public final String Q_UNABLE = "버릴 수 없음!";
    public final String S_INFINITE = "내구도 무제한 아이템!";
    public final String T_ONETOUCH = "원터치 효과 부여!";

    //1. 특수효과 1) 내구도 무제한 아이템!
    @EventHandler
    public void onPlayerDamageEvent(PlayerItemDamageEvent event) {
        Player p = event.getPlayer();
        ItemStack useditem = event.getItem();

        if (!useditem.getItemMeta().hasLore()) return;

        for (String lore : useditem.getItemMeta().getLore()) {
            String nlore = ChatColor.stripColor(lore);

            if (nlore.equals(S_INFINITE)) {
                event.setCancelled(true);
            }
        }
    }

    //1. 특수효과 2) 버릴 수 없음!
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        ItemStack dropitem = event.getItemDrop().getItemStack();

        ItemStackManager i = new ItemStackManager(Material.GOLDEN_PICKAXE, "테스트");
        i.setLore(T_ONETOUCH);
        p.getInventory().addItem(i.getItemStack());

        if (!dropitem.getItemMeta().hasLore()) return;

        for (String lore : dropitem.getItemMeta().getLore()) {
            String nlore = ChatColor.stripColor(lore);

            if (nlore.equals(Q_UNABLE)) {
                p.sendMessage(ChatColor.RED + "이 아이템은 버릴 수 없습니다!");
                event.setCancelled(true);
            }
        }

        //Bukkit.broadcastMessage(dropitem.getItemMeta().getLore().get(0));
    }

    //2. 원터치 파괴
    @EventHandler
    public void playerClickBlock(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;

        Player p = event.getPlayer();
        ItemStack useditem = p.getInventory().getItemInMainHand();

        if (!useditem.getItemMeta().hasLore()) return;

        for (String lore : useditem.getItemMeta().getLore()) {
            String nlore = ChatColor.stripColor(lore);

            if (nlore.equals(T_ONETOUCH)) {
                Block b = event.getClickedBlock();
                b.breakNaturally(useditem);
            }
        }
    }
}
