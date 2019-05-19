package com.faujor.service.common.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.entity.bam.Qrimg;
import com.faujor.service.bam.QrimgService;
import com.faujor.service.common.GetQRimg;
import com.faujor.utils.MatrixToImageWriter;
import com.faujor.utils.UUIDUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

@Service(value = "getQRimg")
public class GetQRimgImg implements GetQRimg {

	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	@Autowired
	private  QrimgService qrimgService;
	
	
	
	@Override
	public String getQRUrl(String text) {
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
		
		BitMatrix bitMatrix;
		String fileName = "";
		try {
			bitMatrix = new MultiFormatWriter().encode(text2, BarcodeFormat.QR_CODE, width, height, hints);
			///D:/Users/xiazhitao/workspace/srm/target/classes/com/faujor/utils/
			String path = MatrixToImageWriter.class.getResource("").getPath();
			int index = path.indexOf("/com");
			String QRimages = path.substring(0, index)+"/static/img/QRimages";
			File imagePath = new File(QRimages);
			//如果没有images文件夹就创建
			if(!imagePath.exists()){
				imagePath.mkdirs();
			}
			fileName = text+".jpg";
			// 生成二维码将二维码图片输出到本地
			File outputFile = new File(QRimages+"/"+fileName);
			MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
		} catch (WriterException e) {
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
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
}
