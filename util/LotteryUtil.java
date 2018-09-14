package lottery.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import http.HttpUtil;
import lottery.bean.Data;
import lottery.bean.Lottery;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 彩票工具类
 * Created by TimAimee on 2018/9/6.
 */
public class LotteryUtil {
    /**
     * 访问的链接
     */
    private String url = "http://f.apiplus.net/cqssc-2.json";
    /**
     * 一个集合
     */
    List<String> arraylist;

    /**
     * 一个LotteryUtil构造方法
     */
    public LotteryUtil() {
        arraylist = new ArrayList<>();
    }

    /**
     * 得到一个lottery对象
     * @param filePath 文件路径
     */
    public void getLottery(String filePath) {
        try {
            Lottery lottery = getLotteryObject();
            System.out.println(getLotteryObject());
            ArrayList<Data> arrData = lottery.getData();
            for (Data data : lottery.getData()) {
                String ste = data.getOpentime() + ",重庆时时彩开奖号:" + data.getOpencode();
                if (arraylist.contains(ste)) {
                    System.out.println("不保存到文件，因为重复");
                } else {
                    System.out.println("保存到文件:" + ste);
                    appendToFile(filePath,ste);
                    arraylist.add(ste);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 把一个url拿到的json转成lottery对象
     * @return 返回一个Lottery对象
     * @throws Exception
     */
    public Lottery getLotteryObject() throws Exception {
        String json = HttpUtil.sendGetRequest(url);
        Lottery lottery = getLotteryFormJson(json);
        return lottery;
    }
    /**
     * 传人一个json字符串还回一个lottery对象；
     * @param json
     * @return Lottery对象
     * @throws Exception
     */
    public Lottery getLotteryFormJson(String json) throws Exception {
        JSONObject jsonObject = JSON.parseObject(json);
        Lottery Lottery = JSON.toJavaObject((JSON) jsonObject, Lottery.class);
        return Lottery;
    }

    /**
     * 规定一个时间格式以这种格式还回一个时间
     * @param str
     * @return 返回一个Data对象
     * @throws ParseException
     */
    public Date getDate(String str) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse("2012-02-28 09:58:00");
        return date;
    }


    /**
     * 把传人的文件累加写入到文件
     * @param file 传人的文件
     * @param conent
     */
    public static void appendToFile(String file, String conent) {
//        System.out.println(file);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            out.write(conent+"\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



