package repository;

import lombok.SneakyThrows;
import model.URLDC;
import utils.DB;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class URLS {
    private static List<URLDC> URLS = new ArrayList<URLDC>();

    @SneakyThrows
    public static void add(URLDC url) {
        url.setId((long) URLS.size() + 1);
        Date date = new Date();
        url.setCreated(date);
        URLS.add(url);
        DB.addURL(url);
    }

    public List<URLDC> getAll() {

        return URLS;
    }


}
