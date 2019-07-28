package com.newsapp.utils;

import com.newsapp.constants.Category;
import com.newsapp.constants.Country;

import java.util.ArrayList;

public class ViewUtils {

    public static ArrayList<Menu> buildMenus(String[] array){
        ArrayList<Menu> menus = new ArrayList<>();
        menus.add(new Menu(array[0], Country.UNITED_URAB_EMIRATES));
        menus.add(new Menu(array[1], Country.INDIA));
        menus.add(new Menu(array[2], Country.UNITED_STATE));
        menus.add(new Menu(array[3], Country.AUSTRALIA));
        menus.add(new Menu(array[4], Country.RUSSIA));
        menus.add(new Menu(array[5], Country.CHINA));
        menus.add(new Menu(array[6], Country.BRAZIL));
        menus.add(new Menu(array[7], Country.MALAYSIA));
        return menus;
    }

    public static ArrayList<Tab> buildTabs(String[] tabTitles) {
        ArrayList<Tab> tabs = new ArrayList<>();
        tabs.add(new Tab(tabTitles[0], Category.UNKNOWN));
        tabs.add(new Tab(tabTitles[1], Category.BUSINESS));
        tabs.add(new Tab(tabTitles[2], Category.ENTERTAINMENT));
        tabs.add(new Tab(tabTitles[3], Category.GENERAL));
        tabs.add(new Tab(tabTitles[4], Category.HEALTH));
        tabs.add(new Tab(tabTitles[5], Category.SCIENCE));
        tabs.add(new Tab(tabTitles[6], Category.SPORTS));
        tabs.add(new Tab(tabTitles[7], Category.TECHNOLOGY));
        return tabs;
    }

    public static class Tab{
        private String title;
        private Category category;

        public Tab(String title, Category category) {
            this.title = title;
            this.category = category;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }
    }

    public static class Menu{
        private String title;
        private Country type;

        Menu(String title, Country type) {
            this.title = title;
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Country getType() {
            return type;
        }

        public void setType(Country type) {
            this.type = type;
        }

    }

}
