package com.schneider.imscore.util;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.net.InetAddress;

/**
 * @author liyuan
 * @createDate 2019/10/09 11:36
 * @Description
 */
@Slf4j
public class Geoip2Util {
    /**
     * 根据ip地址获取国家
     * @param ip
     * @return
     */
    public static String getCountryByIp(String ip) {
        if (StringUtils.isBlank(ip)){
            return "";
        }
        String name = "";
        ClassPathResource resource = new ClassPathResource("/GeoLite2-Country.mmdb");
        try {
            File file = resource.getFile();
            DatabaseReader reader = new DatabaseReader.Builder(file).build();
            InetAddress ipAddress = InetAddress.getByName(ip);
            CountryResponse country = reader.country(ipAddress);
            Country country1 = country.getCountry();
             name = country1.getName();
        } catch (Exception e) {
            log.error("获取ip失败",e);
        }
        return name;
    }
}
