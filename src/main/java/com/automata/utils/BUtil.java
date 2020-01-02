package com.automata.utils;

import com.automata.downloader.IDownloader;
import com.automata.request.Request;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * name fuguangli
 * date 2020/1/2
 * contact businessfgl@163.com
 * 二进制相关的工具
 */
public class BUtil {
    public static boolean save(IDownloader downloader, String url, String saveDir) {
        try {
            InputStream in = downloader.getBContent(new Request(url));
            if (in != null) {
//                String suffix=url.substring(url.lastIndexOf("."));
                String _dir = url.replaceAll("https:/|http:/", "");
//                _dir = _dir.substring(_dir.indexOf("/"));
                String filename = url.substring(url.lastIndexOf("/"));
                _dir = _dir.replace(filename, "");
                String fileDir = saveDir + _dir;
                File dir = new File(fileDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File outFile = new File(saveDir + _dir + filename);
                if (outFile.exists()) {
                    return true;
                }

                OutputStream out = new FileOutputStream(outFile);
                IOUtils.copy(in, out);

                in.close();
                out.close();

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isBinary(List<String> binarySuffixes, String url) {
        String suffix = url.substring(url.lastIndexOf(".") + 1).toLowerCase();
        String defaultSuf = "bmp,jpg,png,tif,gif,pcx,tga,exif,fpx,svg,psd,cdr,pcd,dxf,ufo,eps,ai,raw,wmf,webp," +
                "avi,mov,rmvb,rm,flv,mp4,3gp," +
                "pdf,doc,docx,ppt,pptx,xls,xlsx," +
                "mp3,wma,flac,aac,mmf,amr,m4a,m4r,ogg,mp2,wav,wv";
        if (binarySuffixes == null) {
            binarySuffixes = Arrays.asList(defaultSuf.split(","));
        } else {
            binarySuffixes.addAll(Arrays.asList(defaultSuf.split(",")));
        }
        return binarySuffixes.contains(suffix);
    }
}
