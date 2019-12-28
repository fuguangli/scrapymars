package com.automata.parser;

import java.io.InputStream;

/**
 * name fuguangli
 * date 2019/12/24
 * contact businessfgl@163.com
 */
public interface IResponseParser {
    Object parse(InputStream stream);

    Object parse(String content);
}
