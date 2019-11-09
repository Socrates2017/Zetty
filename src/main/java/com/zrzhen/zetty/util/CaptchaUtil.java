package com.zrzhen.zetty.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @author chenanlian
 */
public class CaptchaUtil {

    private static final Logger log = LoggerFactory.getLogger(CaptchaUtil.class);


    private static Random random = new Random();

    public static String createToken() {
        //文字素材
        String words = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        char[] cs = words.toCharArray();
        String out = "";
        for (int i = 0; i <= 3; i++) {
            char c = cs[random.nextInt(cs.length)];
            out += c;
        }
        return out;
    }

    public static byte[] createCode(String code) {
        //默认背景为黑色
        BufferedImage image = new BufferedImage(100, 50, BufferedImage.TYPE_INT_RGB);
        //获取画笔
        Graphics graphics = image.getGraphics();
        //默认填充为白色
        graphics.fillRect(0, 0, 100, 50);
        for (int i = 0; i < code.length(); i++) {
            //设置随机的颜色
            graphics.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            graphics.setFont(new Font("微软雅黑", Font.BOLD, 30));
            char c = code.charAt(i);
            graphics.drawString(c + "", i * 20, 30);
        }
        //画干扰线
        int max = random.nextInt(10);
        for (int i = 0; i < max; i++) {
            graphics.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            graphics.drawLine(random.nextInt(100), random.nextInt(50), random.nextInt(100), random.nextInt(50));
        }
        //画干扰点
        int max2 = random.nextInt(10);
        for (int i = 0; i < max2; i++) {
            graphics.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            graphics.drawOval(random.nextInt(80), random.nextInt(40), random.nextInt(5), random.nextInt(10));
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //format:图片格式，“gif"等；
        //out:目标；特别的，如果目标为byte数组，则将其预设为ByteArrayOutputStream即可传入此方法，执行完后，只要toByteArray()即可获得byte[]
        try {
            ImageIO.write(image, "png", byteArrayOutputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return byteArrayOutputStream.toByteArray();
    }

    public static void main(String[] args) throws IOException {

        String token = CaptchaUtil.createToken();
        byte[] bytes = createCode(token);
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        BufferedImage image = ImageIO.read(in);
        ImageIO.write(image, "png", new File("D:\\验证码3.jpg"));

    }
}
