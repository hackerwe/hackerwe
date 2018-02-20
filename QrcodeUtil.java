package com.baidu.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;


import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

public class QrcodeUtil {
	
	//需求：生产一个二维码 图片 保存在磁盘上
	//路径（不是固定的参数） 二维码里面存储的 String
	//内容（不是固定的参数）String
	public  static void qrcodeImg(String imgPath,String content)
	{
		//实例化一个Qrcode对象
		Qrcode qrcode =new Qrcode();
		//了解二维码的基本参数 
		//排错率 15%
		qrcode.setQrcodeErrorCorrect('m');
		//版本号 140 140
		qrcode.setQrcodeVersion(7);
		//编码
		qrcode.setQrcodeEncodeMode('B');
		
		
		//开始绘制二维码 创建一个图片BufferedImage
		int width = 140;
		int height = 140;
		//画板
		BufferedImage bf = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		//创建一个画笔
		Graphics2D gs=bf.createGraphics();
		//开始绘制
		
		//设置背景色
		gs.setBackground(Color.white);
		
		//设置前景色
		gs.setColor(Color.black);
		
		//绘制一个矩形
		gs.clearRect(0, 0, width, height);
		
		//根据内容绘制二维码
		//拿到内容
		try {
			//拿到内容
			byte[] contentbyte =content.getBytes("utf-8");
			boolean[][] code =qrcode.calQrcode(contentbyte);
			//如果这个地方为真，就把它涂黑
			for(int i=0;i<code.length;i++)
			{
				for(int j=0;j<code.length;j++)
				{
					if(code[j][i])
					{
						//绘制一个小黑矩形
						gs.fillRect(j*3+2, i*3+2, 3, 3);
					}
				}
			}
			gs.dispose();
			ImageIO.write(bf, "png",new File(imgPath) );
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	public static void main(String[] args) {
		qrcodeImg("E:\\自制表情包\\二维码.png","我爱你");
	}


}
