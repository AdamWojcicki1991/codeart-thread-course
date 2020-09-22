package com.codeart.thread.week7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CorrectnessLesson {
    private static final Logger logger = LoggerFactory.getLogger(CorrectnessLesson.class);

    public static void main(String[] args) {
        Store store = new Store();

        Set<Integer> ids = new HashSet<>();

        ids.add(store.add("book"));
        ids.add(store.add("bike"));
        ids.add(store.add("toy"));
        ids.add(store.add("t-shirt"));
        ids.add(store.add("jeans"));
        ids.add(store.add("coat"));
        ids.add(store.add("shoes"));

        logger.info("Ids size correctness: {}", ids.size() == 7);
        logger.info("Store size: {}", store.size());
        logger.info("Store: {}", store.inventory);
    }

    static class Store {
        private final Map<Integer, String> inventory = new ConcurrentHashMap<>();

        public Integer add(String name) {
            int id = inventory.size();
            inventory.put(id, name);
            return id;
        }

        public String getById(Integer id) {
            return inventory.get(id);
        }

        public Integer size() {
            return inventory.size();
        }
    }
}
