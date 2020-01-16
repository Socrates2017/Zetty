package com.zrzhen.zetty.cms.service;

import com.zrzhen.zetty.cms.dao.BookDao;

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
