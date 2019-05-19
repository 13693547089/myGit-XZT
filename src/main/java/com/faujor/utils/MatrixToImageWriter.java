package com.faujor.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;

import com.faujor.common.ftp.FtpUtil;
import com.faujor.entity.bam.Qrimg;
import com.faujor.service.bam.QrimgService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

/**
 * 二维码的生成需要借助MatrixToImageWriter类，该类是由Google提供的，可以将该类直接拷贝到源码中使用
 */
public class MatrixToImageWriter {
	
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	private static final String ftpBasePath = "/SRM/";
	@Autowired
	private static QrimgService qrimgService;
	private MatrixToImageWriter() {
	}

	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format " + format + " to " + file);
		}
	}

	public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format " + format);
		}
	}
	/**
	 * 将二维码存到文件服务器上
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static String getQRCode(String text) throws Exception {
		//String text = "http://www.baidu.com"; // 二维码内容
		
		int width = 100; // 二维码图片宽度
		int height = 100; // 二维码图片高度
		String format = "jpg";// 二维码的图片格式
		text = text+"\r\n";
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 内容所使用字符集编码
		BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
		String fileName = UUIDUtil.getUUID().toLowerCase()+"QRCode.jpg";
		String newName=UUIDUtil.getUUID().toLowerCase()+fileName;
		String realPath=ftpBasePath.concat("WDGL/QRimg/deliveryQRImages/");
		String realName=realPath.concat(newName);
		// 生成二维码将二维码图片上传到文件服务器上
		BufferedImage image = toBufferedImage(bitMatrix);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		
		FtpUtil.uploadFile(realPath, realName, is);
		is.close();
		return realName;
	}
	/**
	 * 将二维码存到本地
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String text = "H180300003"; // 二维码内
		int width = 100; // 二维码图片宽度
		int height = 100; // 二维码图片高度
		String format = "jpg";// 二维码的图片格式
		
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 内容所使用字符集编码
		
		BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
		String path = MatrixToImageWriter.class.getResource("").getPath();
		int index = path.indexOf("/com");
		String QRimages = path.substring(0, index)+"/static/img/QRimages";
		File imagePath = new File(QRimages);
		//如果没有images文件夹就创建
		if(!imagePath.exists()){
			imagePath.mkdirs();
		}
		String fileName = UUIDUtil.getUUID()+"二维码.jpg";
	    ///D:/Users/xiazhitao/workspace/srm/target/classes/com/faujor/utils/
		// 生成二维码将二维码图片输出到本地
		 File outputFile = new File(QRimages+"/"+fileName);
		MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
		
		
	}
	/**
	 * 将生成的二维码存到本地
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static String getQRCode2(String text) throws Exception {
		//String text = "H180300003"; // 二维码内容
		Qrimg qrimg = qrimgService.queryQrimgByQrCode(text);
		if(qrimg != null){
			return qrimg.getQrUrl();
		}
		int width = 100; // 二维码图片宽度
		int height = 100; // 二维码图片高度
		String format = "jpg";// 二维码的图片格式
		String text2 = text+"\r\n";
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 内容所使用字符集编码
		
		BitMatrix bitMatrix = new MultiFormatWriter().encode(text2, BarcodeFormat.QR_CODE, width, height, hints);
		///D:/Users/xiazhitao/workspace/srm/target/classes/com/faujor/utils/
		String path = MatrixToImageWriter.class.getResource("").getPath();
		int index = path.indexOf("/com");
		String QRimages = path.substring(0, index)+"/static/img/QRimages";
		File imagePath = new File(QRimages);
		//如果没有images文件夹就创建
		if(!imagePath.exists()){
			imagePath.mkdirs();
		}
		String fileName = UUIDUtil.getUUID().toLowerCase()+"qrcode.jpg";
		// 生成二维码将二维码图片输出到本地
		 File outputFile = new File(QRimages+"/"+fileName);
		MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
		String qrurl = "/img/QRimages/"+fileName;
		Qrimg img = new Qrimg();
		img.setQrCode(text);
		img.setQrUrl(qrurl);
		boolean result = qrimgService.insertQrimg(img);
		if(result){
			return img.getQrUrl();
		}else{
			return "/img/wenhao.jpg";
		}
	}
	
	
	
}
