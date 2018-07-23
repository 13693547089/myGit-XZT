package com.faujor.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {
	  private static String ftpHost="srmftp.top-china.cn";
      private static String ftpUserName="srm";
      private static String ftpPassword="TOPsrm@2018" ;
      private static int ftpPort=2121;

      
	  private static String LOCAL_CHARSET="GBK";
	  // FTP协议里面，规定文件名编码为iso-8859-1
	  private static String SERVER_CHARSET = "ISO-8859-1";
  	
      
	  
	  public static  FTPClient getFTPClient() {
	        FTPClient ftpClient = new FTPClient();
	        try {
	            ftpClient = new FTPClient();
	            ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
	            ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
	            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
	                ftpClient.disconnect();
	            } else {
	            }
	        } catch (SocketException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return ftpClient;
	    }

	    /*
	     * 从FTP服务器下载文件
	     * @param ftpPath FTP服务器中文件所在路径 格式： ftptest/aa
	     * @param localPath 下载到本地的位置 格式：H:/download
	     * @param fileName 文件名称
	     */
	    public static void downloadFtpFile(String filePath,String fileName,OutputStream os) {
	        FTPClient ftpClient = null;
	        try {
	            ftpClient = getFTPClient();
	            
	            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand(
	            		"OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
	            		LOCAL_CHARSET = "UTF-8";
	            }
	            ftpClient.setControlEncoding(LOCAL_CHARSET); // 中文支持
	            fileName=new String(fileName.getBytes(LOCAL_CHARSET),SERVER_CHARSET);
	            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
	            ftpClient.enterLocalPassiveMode();
	            ftpClient.changeWorkingDirectory(filePath);
	            
	            ftpClient.retrieveFile(fileName, os);
	            os.close();
	            os.flush();
	            ftpClient.logout();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (SocketException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	            e.printStackTrace();
	        }
	    }

	    /**
	     * @param ftpPath  FTP服务器中文件所在路径 格式： ftptest/aa
	     * @param fileName ftp文件名称
	     * @param input 文件流
	     * @return 成功返回true，否则返回false
	     */
	    public static boolean uploadFile( String ftpPath,String fileName,InputStream input) {
	    	
	        boolean success = false;
	        FTPClient ftpClient = null;
	        try {
	            int reply;
	            ftpClient = getFTPClient();
	            reply = ftpClient.getReplyCode();
	            if (!FTPReply.isPositiveCompletion(reply)) {
	                ftpClient.disconnect();
	                return success;
	            }
	            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand(
	            		"OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
	            		LOCAL_CHARSET = "UTF-8";
	            }
	            
	            ftpClient.setControlEncoding(LOCAL_CHARSET); // 中文支持
	            fileName=new String(fileName.getBytes(LOCAL_CHARSET),SERVER_CHARSET);
	            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
	            ftpClient.enterLocalPassiveMode();
	            boolean changeFlag = changeWorkingDirectory(ftpClient,ftpPath);
	            if(!changeFlag){
	            	createDirecroty(ftpClient,ftpPath);
	            }
	            ftpClient.storeFile(fileName, input);
	            input.close();
	            ftpClient.logout();
	            success = true;
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (ftpClient.isConnected()) {
	                try {
	                    ftpClient.disconnect();
	                } catch (IOException ioe) {
	                }
	            }
	        }
	        return success;
	    }
        /** * 删除文件 * 
        * @param pathname FTP服务器保存目录 * 
        * @param filename 要删除的文件名称 * 
        * @return */ 
        public static boolean deleteFile(String pathname, String filename){ 
            boolean flag = false; 
            FTPClient ftpClient=new FTPClient();
            try { 
                ftpClient = getFTPClient();
                if (FTPReply.isPositiveCompletion(ftpClient.sendCommand(
	            		"OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
	            		LOCAL_CHARSET = "UTF-8";
	            }
	            
	            ftpClient.setControlEncoding(LOCAL_CHARSET); // 中文支持
	            filename=new String(filename.getBytes(LOCAL_CHARSET),SERVER_CHARSET);
                //切换FTP目录 
                ftpClient.changeWorkingDirectory(pathname); 
                ftpClient.dele(filename); 
                ftpClient.logout();
                flag = true; 
            } catch (Exception e) { 
                e.printStackTrace(); 
            } finally {
                if(ftpClient.isConnected()){ 
                    try{
                        ftpClient.disconnect();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                } 
            }
            return flag; 
        }
        
        /**
         * 更换文件路径
         * @param ftpClient
         * @param directory
         * @return
         */
        public static boolean changeWorkingDirectory(FTPClient ftpClient,String directory) {
           boolean flag = true;
           try {
               flag = ftpClient.changeWorkingDirectory(directory);
           } catch (IOException ioe) {
               ioe.printStackTrace();
           }
           return flag;
      }
       /**
        * 创建目录
        * @param ftpClient
        * @param remote
        * @return
        * @throws IOException
        */
       public static boolean createDirecroty(FTPClient ftpClient,String remote) throws IOException {
            boolean success = true;
            String directory = remote;
            // 如果远程目录不存在，则递归创建远程服务器目录
            if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(ftpClient,new String(directory))) {
                int start = 0;
                int end = 0;
                if (directory.startsWith("/")) {
                    start = 1;
                } else {
                    start = 0;
                }
                end = directory.indexOf("/", start);
                String path = "";
                String paths = "";
                while (true) {
                    String subDirectory = remote.substring(start, end);
                    path = path + "/" + subDirectory;
                    if (!existFile(ftpClient,path)) {
                        if (makeDirectory(ftpClient,subDirectory)) {
                            changeWorkingDirectory(ftpClient,subDirectory);
                        } else {
                            changeWorkingDirectory(ftpClient,subDirectory);
                        }
                    } else {
                        changeWorkingDirectory(ftpClient,subDirectory);
                    }

                    paths = paths + "/" + subDirectory;
                    start = end + 1;
                    end = directory.indexOf("/", start);
                    // 检查所有目录是否创建完毕
                    if (end <= start) {
                        break;
                    }
                }
            }
            return success;
        }
        /**
         * 判断ftp服务器文件是否存在    
         * @param ftpClient
         * @param path
         * @return
         * @throws IOException
         */
        public static boolean existFile(FTPClient ftpClient,String path) throws IOException {
                boolean flag = false;
                FTPFile[] ftpFileArr = ftpClient.listFiles(path);
                if (ftpFileArr.length > 0) {
                    flag = true;
                }
                return flag;
            }
        //创建目录
        public static boolean makeDirectory(FTPClient ftpClient,String dir) {
            boolean flag = true;
            try {
                flag = ftpClient.makeDirectory(dir);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return flag;
        }
}
