package tech.whaleeye.misc.ajax;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class ListPage<T> implements Serializable {
    private final List<T> list;
    private final int pageSize;
    private final int pageNo;
    private final int total;

    public ListPage(List<T> list, int pageSize, int pageNo, int total) {
        this.list = list;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.total = total;
    }
}
