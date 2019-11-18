package com.example.mobileappsproject.Route;

import android.database.Cursor;

import com.example.mobileappsproject.RouteDbHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RouteContent {

    public static List<Route> ROUTES = new ArrayList<Route>();

    public static Map<Long, Route> ITEM_MAP = new HashMap<Long, Route>();


    private static void addRoute(Route route) {
        ROUTES.add(route);
        ITEM_MAP.put(route.id, route);
    }

    public static void resetRoutes() {
        ROUTES = new ArrayList<Route>();
        ITEM_MAP = new HashMap<Long, Route>();
    }

    public static class Route implements Serializable {
        public final long id;
        public final String route_name;
        public final float rating;
        public final String tags;
        public final String date;

        public Route(long id, String route_name, float rating, String tags, String date ) {
            this.id = id;
            this.route_name = route_name;
            this.rating = rating;
            this.tags = tags;
            this.date = date;
        }

        @Override
        public String toString() {
            return route_name;
        }
    }
}
