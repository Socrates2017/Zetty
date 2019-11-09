package com.zrzhen.zetty.service;

import com.zrzhen.zetty.dao.BookDao;

/**
 * @author chenanlian
 */
public class BookService {

    public static boolean checkCreator(Long book) {
        Long userid = UserService.getUserid();
        long count = BookDao.checkUser(book, userid);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
}
