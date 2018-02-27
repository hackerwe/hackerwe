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
	//定义坐标
	int x0,y0,x1,y1;
	boolean flag =  true;
	public JumpOneJump(){
		super("微信跳一跳");//调用父类方法
		
		//设置窗口大小
		this.setSize(350,650);
		//设置窗口居中
		this.setLocationRelativeTo(null);
		//去除窗口边框
		this.setUndecorated(true);
		//设置窗口透明度
		this.setOpacity(0.2f);
		//设置置顶
		this.setAlwaysOnTop(true);
		//设置窗口可见
		this.setVisible(true);
		//点击×时退出窗口
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//点击鼠标右键获取坐标
		
		//创建一个小面板
		JLabel jlabel = new JLabel();
		this.add(jlabel);
		
		
		//给JLabel添加一个监听
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e)
			{
				//参数添加鼠标的事件源
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
					    //取绝对值
						double _x =Math.abs(x0-x1);
						double _y =Math.abs(y0-y1);
						//开平方
						double dis = Math.sqrt(_x*_x+_y*_y);
						//定义adb 命令//adb shell input touchscreen swipe 180 187 180 187
						//D:\\tiaoyitiao\\adb\\ADB\\
						double x = Math.random()*200;
						double y = Math.random()*200;
						String cmd ="adb shell input "
								+ "touchscreen swipe "+x+" "+y+
								" "+x+" "+y+" "+Math.round(dis*4.8);
						Runtime run=Runtime.getRuntime();
						try {
							//执行命令
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
