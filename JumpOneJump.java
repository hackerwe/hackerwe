package com.baidu;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 
 * @author songxuan
 *
 */
public class JumpOneJump extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//��������
	int x0,y0,x1,y1;
	boolean flag =  true;
	public JumpOneJump(){
		super("΢����һ��");//���ø��෽��
		
		//���ô��ڴ�С
		this.setSize(350,650);
		//���ô��ھ���
		this.setLocationRelativeTo(null);
		//ȥ�����ڱ߿�
		this.setUndecorated(true);
		//���ô���͸����
		this.setOpacity(0.2f);
		//�����ö�
		this.setAlwaysOnTop(true);
		//���ô��ڿɼ�
		this.setVisible(true);
		//�����ʱ�˳�����
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//�������Ҽ���ȡ����
		
		//����һ��С���
		JLabel jlabel = new JLabel();
		this.add(jlabel);
		
		
		//��JLabel���һ������
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e)
			{
				//������������¼�Դ
				System.out.println(e);
				if(e.getButton()==MouseEvent.BUTTON1)
				{
					if(flag)
					{
						x0=e.getX();
						y0=e.getY();
						flag= false;
					}
					else
					{
						x1=e.getX();
						y1=e.getY();
						flag= true;
					    //ȡ����ֵ
						double _x =Math.abs(x0-x1);
						double _y =Math.abs(y0-y1);
						//��ƽ��
						double dis = Math.sqrt(_x*_x+_y*_y);
						//����adb ����//adb shell input touchscreen swipe 180 187 180 187
						//D:\\tiaoyitiao\\adb\\ADB\\
						double x = Math.random()*200;
						double y = Math.random()*200;
						String cmd ="adb shell input "
								+ "touchscreen swipe "+x+" "+y+
								" "+x+" "+y+" "+Math.round(dis*4.8);
						Runtime run=Runtime.getRuntime();
						try {
							//ִ������
							Process p=run.exec(cmd);
							System.out.println(cmd);
							p.waitFor();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}

				}
			}
		});
		
	}
	public static void main(String[] args)
	{
		
		new JumpOneJump();
		
	}
	
}
