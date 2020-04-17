package com.zrzhen.idmaker;

import com.zrzhen.idmaker.core.Id;
import com.zrzhen.idmaker.core.IdService;
import com.zrzhen.idmaker.core.IdServiceImpl;

public class Test {


    @org.junit.Test
    public void testDb() {
        IdService idService = new IdServiceImpl();

        long id = idService.genId();
        Id ido = idService.expId(id);
        long id1 = idService.makeId(ido.getVersion(), ido.getType(),
                ido.getGenMethod(), ido.getMachine(), ido.getTime(),
                ido.getSeq());

        System.err.println(id + ":" + ido);

        //AssertJUnit.assertEquals(id, id1);
    }
}
