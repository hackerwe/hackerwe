package newJump;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Hylun(https://gitee.com/hylun)
 * 2018/1/2
 */
public class JumpOneJump {

    private static String adb = "";
    private static float rate = 1.37f;
    private static boolean running = false;
    private static boolean restart = true;
    private static int step = 0;

    public static void main(String[] args) {
        String ANDROID_HOME = System.getenv("ANDROID_HOME");
        if (ANDROID_HOME == null) ANDROID_HOME = new File("").getAbsolutePath();
//        adb = ANDROID_HOME + "/platform-tools/adb";
        adb="adb ";
        showView();
    }

    private static final String path = JumpOneJump.class.getResource("").getFile();
    private static final JLabel error = new JLabel("������ֻ���360��QQ����");
    private static final String start = "��ʼ��Ϸ", stop = "ֹͣ����";
    private static final JButton button = new JButton(start);

    private static void showView() {
        JFrame view = new JFrame("��һ��������");
        view.setSize(220, 220);
        view.setResizable(false);
        view.setLocationRelativeTo(null);
        view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 0, 10);
        view.getContentPane().setLayout(layout);

        view.getContentPane().add(new JLabel("����ֻ�USB����ģʽ"));
        view.getContentPane().add(new JLabel("Ȼ���΢����һ����Ϸ"));

        view.getContentPane().add(new JLabel("�ʵ�����ʱ��ϵ��"));
        JTextField rateField = new JTextField(rate + "", 3);
        view.getContentPane().add(rateField);

        JCheckBox checkBox = new JCheckBox("�Զ����¿�ʼ��Ϸ");
        checkBox.setSelected(true);
        checkBox.addActionListener(e -> restart = checkBox.isSelected());
        view.getContentPane().add(checkBox);

        error.setForeground(Color.red);
        view.getContentPane().add(error);

        button.addActionListener(ae -> {
            if (!running) {
                new Thread(() -> {
                    try {
                        setRate(rateField.getText());
                        JumpOneJump.run();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        String msg = "����ͼƬ������ȷ���Ѵ���һ��";
                        if (step == 0) msg = "ʱ��ϵ��������Ϊ1-3֮�������";
                        if (step == 1) msg = "adb���������adb.exe���̺�����";
                        stop(msg);
                    }
                }).start();
            } else {
                stop("=====��ֹͣ����=====");
            }
        });
        view.getContentPane().add(button);
        view.setVisible(true);
    }

    private static void stop(String msg) {
        running = false;
        button.setText(start);
        error.setText(msg);
    }

    private static void setRate(String text) throws Exception {
        step = 0;
        float f = Float.parseFloat(text);
        if (f < 1 || f > 3) throw new Exception("error number");
        rate = f;
    }

    private static void run() throws Exception {
        running = true;
        button.setText(stop);
        error.setText("=====����������=====");
        while (running) {
            step = 1;//����
            File pic = getScreenPic();
            step = 2;//����ͼƬ
            String res = scanPic(pic);
            step = 3;//��һ��
            exec(adb + " shell input swipe " + res);
            step = 4;//��ͣ2-3������
            Thread.sleep(2000 + new Random().nextInt(500));
        }
    }

    private static String scanPic(File pic) throws Exception {
        BufferedImage bi = ImageIO.read(pic);
        //��ȡͼ��Ŀ�Ⱥ͸߶�
        int width = bi.getWidth();
        int height = bi.getHeight();
        int x1 = 0, y1 = 0, x2 = 0, y2 = 0,r = width/1080;
        //ɨ���ȡ����λ��
        for (int i = 50; i < width; i++) {
            for (int flag = 0, j = height * 3 / 4; j > height / 3; j -= 5) {
                if (!colorDiff(bi.getRGB(i,j),55<<16|58<<8|100)) flag++;
                if (flag > 3) {
                    x1 = i + 13*r;
                    y1 = j + 2*r;
                    break;
                }
            }
            if (x1 > 0) break;
        }
        Graphics2D g2d = bi.createGraphics();
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(3f));
        //ɨ��Ŀ���
        for (int i = height / 3; i < y1; i++) {
            int p1 = bi.getRGB(99, i);
            for (int j = 100; j < width; j++) {
                if (colorDiff(bi.getRGB(j,i),p1)) {
                    if(Math.abs(j-x1)<50*r) {//�����ͼ��
                        j = j + 50*r;
                    }else {
                        x2 = j;
                        y2 = i;
                        break;
                    }
                }
            }
            if (x2 > 0) {//�ҵ���Ŀ��鶥��
                int p2 = bi.getRGB(x2, y2 - 10),j,max = -1;
                for (; i < y1-50*r;i += 5 ) {
                    for (j = x2; colorDiff(bi.getRGB(j,i),p2) && j<x2+200*r;) j++;
                    if(max < 0 && j-x2>0) x2 = x2 + (j-x2)/2;//�������������
                    if(max < j - x2) max = j - x2;//�ҵ�Ŀ�������
                    else break;
                }
                g2d.drawLine(x2,y2,x2,i);
                y2 = i - 5;
                g2d.drawLine(x2-max,y2,x2+max,y2);
                break;
            }
        }
        g2d.drawLine(x1,y1,x2,y2);
        ImageIO.write(bi, "png", new FileOutputStream(pic));//�����ͼƬ
        double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        if (x1 < 50 || y1 < 50 || x2 < 50 || y2 < 50 || distance < 100) {
            if (!restart) throw new Exception("scan error:" + x1 + "|" + y1 + "|" + x2 + "|" + y2);
            int x = width / 2, y = height * 3 / 4, z = 9 * height / 10, i = y;//��ȡ��ʼ��ťλ�ã��Զ����¿�ʼ
            while( (i+=20) < z) if (bi.getRGB(x, i) == -1 && bi.getRGB(x + 20, i + 20) == -1) break;
            if (i == y-20 || i == z) throw new Exception("scan error:game not start");
            return x + " " + i + " " + x  + " " + i + " 100";
        }
        if (distance < 150) distance = 150;
        return x1 + " " + y1 + " " + x2 + " " + y2 + " " + (int) (distance * rate);
    }

    private static boolean colorDiff(int c1, int c2){
        int c11 = c1 >> 16 & 0xFF,c12 = c1 >> 8 & 0xFF,c13 = c1 & 0xFF;
        int c21 = c2 >> 16 & 0xFF,c22 = c2 >> 8 & 0xFF,c23 = c2 & 0xFF;
        return Math.abs(c11 - c21) > 5 || Math.abs(c12 - c22) > 5 || Math.abs(c13 - c23) > 5;
    }

    private static File getScreenPic() throws Exception 
    {
//        File pic = new File(path + "pic.png");
    	File pic = new File("E:/��ͼ/screen.png");
        if (pic.exists()) {//����һ��֮ǰ��һ��ͼƬ
            File back = new File(path + System.currentTimeMillis()+".png");
            if (!back.exists() || back.delete()) pic.renameTo(back);
        }
//        exec(adb + " shell screencap -p /sdcard/screen.png");
//        exec(adb + " pull /sdcard/screen.png " + pic.getAbsolutePath());
          exec("adb shell screencap -p /sdcard/screen.png");
          exec("adb pull /sdcard/screen.png E:/��ͼ/screen.png" );
        if (!pic.exists()) throw new Exception("error getScreenPic");
        return pic;
    }

    private static void exec(String cmd) throws Exception {
        Process ps = null;
        try {
            System.out.println(cmd);
            ps = Runtime.getRuntime().exec(cmd.split(" "));
            int code = ps.waitFor();
            if (code != 0) throw new Exception("exec error(code=" + code + "): " + cmd);
        } finally {
            if (ps != null) ps.destroy();
        }
    }
}