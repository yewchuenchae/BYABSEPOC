/**
 * BEYONDSOFT.COM INC
 */
package com.schneider.imscore.util;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 特殊符号转译
 *
 * @author yulijun
 * @version $Id: StringOperationUtil.java, v 0.1 2017/10/18 15:03 yulijun Exp $$
 */
public class StringOperationUtil {

    // 需要转义的特殊字符
    private static final String[] SEARCHSTR = {"\"", "\\\"", "\\", "\\\\"};
    // 转移后的字符串
    private static final String[] REPLACEMENT = {"%20#", "%21#", "%22#", "%23#"};

    /**
     * sql查询，特殊符号转译：%,_
     *
     * @param str
     * @return
     */
    public static String sqlReplace(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        String resultStr = str;
        if (StringUtils.contains(resultStr, "_")) {
            resultStr = StringUtils.replace(resultStr, "_", "\\_");
        }
        if (StringUtils.contains(resultStr, "%")) {
            resultStr = StringUtils.replace(resultStr, "%", "\\%");
        }
        resultStr = resultStr.trim();
        return resultStr;
    }

    /**
     * 特殊字符转义，支持 \  "
     *
     * @param str
     * @return
     */
    public static String replaceAll(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        // 双引号转义
        return StringUtils.replaceEach(str, SEARCHSTR, REPLACEMENT);
    }

    /**
     * 把转义的字符解析成页面回显字符
     *
     * @param str
     * @return
     */
    public static String parseAll(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return StringUtils.replaceEach(str, REPLACEMENT, SEARCHSTR);
    }


    /**
     * 将逗号分隔的字符串变成list
     *
     * @param actionStr
     */
    public static List<String> stringToList(String actionStr) {
        List<String> list = new ArrayList<>();
        if (StringUtils.isNotBlank(actionStr)) {
            String[] actions = actionStr.split(",");
            list = Arrays.asList(actions);
        }
        List arrayList = new ArrayList(list);
        return arrayList;
    }


    /**
     * 过滤特殊字符
     * @param skus
     * @return
     */
    public static List<String> replaceSpecialChar(List<String> skus){
        String regEx="[`~!@#$%^&*()+=|{}':;',.<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、]";
        String aa = "";

        List<String> results = new ArrayList<>();
        for (String sku: skus) {
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(sku);
            String newString = m.replaceAll(aa).trim();
            results.add(newString);
        }
        return results;
    }

}
