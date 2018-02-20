package com.baidu.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;


import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

public class QrcodeUtil {
	
	//��������һ����ά�� ͼƬ �����ڴ�����
	//·�������ǹ̶��Ĳ����� ��ά������洢�� String
	//���ݣ����ǹ̶��Ĳ�����String
	public  static void qrcodeImg(String imgPath,String content)
	{
		//ʵ����һ��Qrcode����
		Qrcode qrcode =new Qrcode();
		//�˽��ά��Ļ������� 
		//�Ŵ��� 15%
		qrcode.setQrcodeErrorCorrect('m');
		//�汾�� 140 140
		qrcode.setQrcodeVersion(7);
		//����
		qrcode.setQrcodeEncodeMode('B');
		
		
		//��ʼ���ƶ�ά�� ����һ��ͼƬBufferedImage
		int width = 140;
		int height = 140;
		//����
		BufferedImage bf = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		//����һ������
		Graphics2D gs=bf.createGraphics();
		//��ʼ����
		
		//���ñ���ɫ
		gs.setBackground(Color.white);
		
		//����ǰ��ɫ
		gs.setColor(Color.black);
		
		//����һ������
		gs.clearRect(0, 0, width, height);
		
		//�������ݻ��ƶ�ά��
		//�õ�����
		try {
			//�õ�����
			byte[] contentbyte =content.getBytes("utf-8");
			boolean[][] code =qrcode.calQrcode(contentbyte);
			//�������ط�Ϊ�棬�Ͱ���Ϳ��
			for(int i=0;i<code.length;i++)
			{
				for(int j=0;j<code.length;j++)
				{
					if(code[j][i])
					{
						//����һ��С�ھ���
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
		qrcodeImg("E:\\���Ʊ����\\��ά��.png","�������ͷ");
	}


}
