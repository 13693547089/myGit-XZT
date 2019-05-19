package com.faujor.test.bam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;

import com.faujor.dao.master.bam.OrderMapper;
import com.faujor.dao.master.bam.ReceiveMapper;
import com.faujor.entity.bam.OrderRele;
import com.faujor.entity.bam.ReceMate;
import com.faujor.entity.bam.Receive;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ReceiveTest
{

	@Autowired
	private ReceiveMapper mapper;
	@Autowired
	private OrderMapper OrderMapper;

	@Test
	public void testquery()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		Receive rece = new Receive();
		map.put("createId", "1");
		map.put("start", 1);
		map.put("end", 5);
		map.put("rece", rece);
		List<Receive> list = mapper.queryReceiveByPage(map);
		for (Receive r : list)
		{
			System.out.println(r);
		}

		int i = mapper.queryReceiveByPageCount(map);
		System.out.println(i);
	}

	@Test
	public void testDelete()
	{
		String[] receIds = new String[]
		{ "0631fca5d9284d5298ba43d330141d73", "249e302f7fe643919be6fe821edb97f5" };
		int i = mapper.deleteReceiveByReceId(receIds);
		System.out.println(i);
	}

	@Test
	public void testInsert()
	{
		Receive rece = new Receive();
		rece.setCreateId("1");
		int i = mapper.addReceive(rece);
		System.out.println(i);

	}

	@Test
	public void testInsert2()
	{
		ReceMate r = new ReceMate();
		r.setOrderId("111");
		int i = mapper.addReceMate(r);
		System.out.println(i);

	}

	@Test
	public void testquery2()
	{
		Receive receive = mapper.queryReceiveByReceId("2e577f8b31df43f996c2f57c7b801c6c");
		System.out.println(receive);

	}

	@Test
	public void testquery3()
	{
		List<ReceMate> list = mapper.queryReceMatesByReceId("2e577f8b31df43f996c2f57c7b801c6c");
		for (ReceMate rm : list)
		{
			System.out.println(rm);
		}

	}

	@Test
	public void testquery4()
	{

		OrderRele rele = OrderMapper.queryOrderReleByFid("f0f5d2daa9084088a5e08434ac337126");
		System.out.println(rele);
	}
	@Test
	public void testquery5() throws Exception
	{
		
		
		String urlString = "ftp://srm:TOPsrm%402018@srmftp.top-china.cn:2121//SRM/WDGL/QRimg/deliveryQRImages/91b02726c64f4e9ab65958efa053f2b13a9b4f4e9747467fb75d60b542b72904QRCode.jpg";
		// 构造URL  
        URL url = new URL(urlString);  
        // 打开连接  
        URLConnection con = url.openConnection();  
        //设置请求超时为5s  
        con.setConnectTimeout(5*1000);  
        // 输入流  
        InputStream is = con.getInputStream();
        //ServletOutputStream outputStream = response.getOutputStream();
        //IOUtils.copy(is, outputStream);
        byte[] bs = readInputStream(is);
       /* string str = Convert.ToBase64String(bs);*/
        /*src='data:image/jpeg|png|gif;base64,${pl.base64String}'*/
       /* BASE64Encoder encoder = new BASE64Encoder();
        encoder.encode(bs);*/
        /*File file = new File("E:/test2/");
        if(!file.exists()){  
            //创建文件  
        	file.createNewFile();  
        }
      //创建输出流    
        FileOutputStream outStream = new FileOutputStream(file);    
        //写入数据    
        outStream.write(bs);*/
       /* String str = Base64.byteArrayToBase64(bs);
        System.out.println(str);*/
        
	}
	public byte[] readInputStream(InputStream inStream) throws Exception{    
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();    
        //创建一个Buffer字符串    
        byte[] buffer = new byte[1024];    
        //每次读取的字符串长度，如果为-1，代表全部读取完毕    
        int len = 0;    
        //使用一个输入流从buffer里把数据读取出来    
        while( (len=inStream.read(buffer)) != -1 ){    
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度    
            outStream.write(buffer, 0, len);    
        }    
        //关闭输入流    
        inStream.close();    
        //把outStream里的数据写入内存    
        return outStream.toByteArray();    
}  
	
	@Test
	public void testquery6()
	{
		String urlString = "ftp://srm:TOPsrm%402018@srmftp.top-china.cn:2121//SRM/WDGL/QRimg/deliveryQRImages/91b02726c64f4e9ab65958efa053f2b13a9b4f4e9747467fb75d60b542b72904QRCode.jpg";
		System.out.println(urlString);
		// 构造URL  
        /*URL url = new URL(urlString);  
        // 打开连接  
        URLConnection con = url.openConnection();  
        //设置请求超时为5s  
        con.setConnectTimeout(5*1000);  
        // 输入流  
        InputStream is = con.getInputStream();*/
        /*File dirFile = new File("E:\\test3");
        if(!dirFile.exists()){ 
          //文件路径不存在时，自动创建目录
          dirFile.mkdir();
        }
        FileOutputStream os = new FileOutputStream("E:\\test3\\123.jpg"); 
        byte[] buffer = new byte[4 * 1024];  
        int read;  
        while ((read = is.read(buffer)) > 0) {  
           os.write(buffer, 0, read);  
        }  
        os.close();  
        is.close();*/
	}
	@Test
	public void test7(){
		//String urlString = "ftp://srm:TOPsrm%402018@srmftp.top-china.cn:2121//SRM/WDGL/QRimg/deliveryQRImages/ea91718d26e74d4085cc18c7f9c6f8a90166913c19774554bcbce742c3fecfcdQRCode.jpg";
		//String string = "https://imgsa.baidu.com/baike/pic/item/d50735fae6cd7b892ced252c072442a7d8330e9f.jpg";
		String str2 = "ftp://srm:TOPsrm%402018@srmftp.top-china.cn:2121//SRM/WDGL/QRimg/deliveryQRImages/ea91718d26e74d4085cc18c7f9c6f8a90166913c19774554bcbce742c3fecfcdQRCode.jpg";
		String str = ImageToBase64ByOnline(str2); 
		System.out.println(str);
	}
	
	public  String ImageToBase64ByOnline(String imgURL) {  
        ByteArrayOutputStream data = new ByteArrayOutputStream();  
        try {  
            // 创建URL  
            URL url = new URL(imgURL);
            
            byte[] by = new byte[1024];  
            // 创建链接  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            conn.setRequestMethod("GET");  
            conn.setConnectTimeout(5000); 
            InputStream is = conn.getInputStream();  
            // 将内容读取内存中  
            int len = -1;  
            while ((len = is.read(by)) != -1) {  
                data.write(by, 0, len);  
            }  
            // 关闭流  
            is.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
//        // 对字节数组Base64编码  
//        BASE64Encoder encoder = new BASE64Encoder();  
//         String str = encoder.encode(data.toByteArray()); 
         return "";
    }   




}
