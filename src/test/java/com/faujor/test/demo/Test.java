package com.faujor.test.demo;


import java.io.*;

/**
 * Created by jyf on 2017/6/2.
 */
public class Test {
    public static void main(String[] args) {
        String ftpHost = "srmftp.top-china.cn";
        String ftpUserName = "srm";
        String ftpPassword = "TOPsrm@2018";
        int ftpPort = 2121;
        String ftpPath = "/SRM/WDGL/HTGL/";
       String localPath = "D:\\测试图片00001.jpg";
       //  String localPath = "D:\\temp";
        String fileName = "测试图片00003.jpg";

        //上传一个文件
        try{
            FileInputStream in=new FileInputStream(new File(localPath));
//            fileName=new String(fileName.getBytes("UTF-8"),"GBK");
            boolean test = FtpUtil.uploadFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, fileName,in);
            System.out.println(test);
        } catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println(e);
        }catch (Exception e) {
        	e.printStackTrace();
		}

//        //在FTP服务器上生成一个文件，并将一个字符串写入到该文件中
//        try {
//            InputStream input = new ByteArrayInputStream("test ftp jyf".getBytes("GBK"));
//            boolean flag = FtpUtil.uploadFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, fileName,input);;
//            System.out.println(flag);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        //下载一个文件
        //FtpUtil.downloadFtpFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, localPath, fileName);
    }
}