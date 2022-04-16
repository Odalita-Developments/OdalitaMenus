package nl.tritewolf.tritemenus.utils;

import nl.tritewolf.tritemenus.utils.callback.RequestTypeCallback;
import nl.tritewolf.tritemenus.utils.callback.ReturnableTypeCallback;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final ItemStack stack;
    private ItemMeta meta;

    public ItemBuilder(ItemStack stack) {
        this.stack = stack;
        this.meta = this.stack.getItemMeta();
    }

    public ItemBuilder(ItemStack stack, String displayname) {
        this.stack = stack;
        this.meta = this.stack.getItemMeta();
        setDisplayName(displayname);
    }

    public ItemBuilder(Material material) {
        this.stack = new ItemStack(material);
        this.meta = this.stack.getItemMeta();
    }

    public ItemBuilder(Material material, String displayname) {
        this.stack = new ItemStack(material);
        this.meta = this.stack.getItemMeta();
        setDisplayName(displayname);
    }

    public ItemBuilder setDisplayName(String displayName) {
        this.meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        this.stack.setDurability(durability);
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        this.stack.setType(material);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.stack.setAmount(amount);
        return this;
    }

    public ItemBuilder setData(Byte data) {
        this.stack.getData().setData(data);
        return this;
    }

    public ItemBuilder setBannerColor(DyeColor color) {
        BannerMeta meta = (BannerMeta) this.meta;
        meta.setBaseColor(DyeColor.WHITE);
        this.meta = meta;
        return this;
    }

    public ItemBuilder addToLore(String lore) {
        ArrayList<String> loreList = this.meta.getLore() == null ? new ArrayList<>() : (ArrayList<String>) this.meta.getLore();
        loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
        this.meta.setLore(loreList);
        return this;
    }

    public ItemBuilder removeFromLore(String lore) {
        ArrayList<String> loreList = new ArrayList<>();
        for (String s : this.meta.getLore()) {
            if (!s.equals(ChatColor.translateAlternateColorCodes('&', lore))) {
                loreList.add(ChatColor.translateAlternateColorCodes('&', s));
            }
        }
        this.meta.setLore(loreList);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        ArrayList<String> normal = new ArrayList<>();
        for (String s : lore) normal.add(ChatColor.translateAlternateColorCodes('&', s));

        this.meta.setLore(normal);
        return this;
    }

    public ItemBuilder setLore(ArrayList<String> lore) {
        ArrayList<String> normal = new ArrayList<>();
        for (String s : lore) normal.add(ChatColor.translateAlternateColorCodes('&', s));

        this.meta.setLore(normal);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        return setLore(new ArrayList<>(lore));
    }

    public ItemBuilder setLore(ReturnableTypeCallback<List<String>> lore) {
        return setLore(lore.call());
    }

    public ItemBuilder setLore(RequestTypeCallback<List<String>, List<String>> lore) {
        return setLore(lore.request(new ArrayList<>()));
    }

    public ItemBuilder setArmorColor(Color color) {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) this.meta;
        leatherArmorMeta.setColor(color);
        this.meta = leatherArmorMeta;
        return this;
    }

    public ItemBuilder removeLore() {
        this.meta.setLore(null);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder removeEnchantments() {
        for (Enchantment enchantment : this.meta.getEnchants().keySet()) {
            this.meta.removeEnchant(enchantment);
        }
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... flag) {
        this.meta.addItemFlags(flag);
        return this;
    }

    public ItemBuilder removeItemFlags(ItemFlag... flags) {
        this.meta.removeItemFlags(flags);
        return this;
    }

    public ItemBuilder glow(boolean glow) {
        if (glow) {
            if (this.stack.getType() == Material.BOW) {
                addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            } else {
                addEnchantment(Enchantment.ARROW_INFINITE, 1);
            }

            addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    public ItemBuilder glow() {
        return glow(true);
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder setUnbreakable() {
        return setUnbreakable(true);
    }

    public ItemStack build() {
        this.stack.setItemMeta(this.meta);
        return this.stack;
    }

    public String prettifyMaterial(Material material) {
        StringBuilder builder = new StringBuilder();
        String name = material.name().toLowerCase();

        if (name.contains("_")) {
            int arg = 0;
            for (String s : name.split("_")) {
                arg++;
                builder.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1));
                if (arg < name.split("_").length) builder.append(" ");
            }
            return builder.toString();
        }

        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    public String asPreview(ChatColor itemColor) {
        StringBuilder builder = new StringBuilder();

        builder.append("&7").append(this.stack.getAmount() > 1 ? this.stack.getAmount() + "x " : "");
        builder.append(itemColor).append(!this.meta.getDisplayName().equalsIgnoreCase("") ? this.meta.getDisplayName() : prettifyMaterial(this.stack.getType())).append(" ");
        builder.append("&7").append(!this.stack.getEnchantments().isEmpty() ? "(Enchanted)" : "");

        return builder.toString();
    }
}
