package com.yoge.ad.service.search.mysql.constant;

import com.github.shyiko.mysql.binlog.event.EventType;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/23
 */
public enum OperateType {

    ADD,
    UPDATE,
    DELETE,
    OTHER;

    public static OperateType to(EventType eventType) {
        if (EventType.isUpdate(eventType))
            return UPDATE;
        if (EventType.isWrite(eventType))
            return ADD;
        if (EventType.isDelete(eventType))
            return DELETE;
        return OTHER;
    }
}
