package im.qiaofeng.file.fileuploaddownload.utils;

import com.alibaba.fastjson.JSONObject;
import im.qiaofeng.file.fileuploaddownload.constants.ErrorCodeConstants;
import im.qiaofeng.file.fileuploaddownload.exception.UtilException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@Slf4j
public class HttpClientUtil {


    public static JSONObject Get(String path, Map<String, Object> queryParams) {
        try {
            HttpClient httpclient = HttpClientMng.getClient();
            Iterator<String> iter = queryParams.keySet().iterator();
            String temp = path + "?";
            while (iter.hasNext()) {
                String key = iter.next();
                temp = temp + key + "=" + queryParams.get(key) + "&";
            }
            String url = temp.substring(0, temp.lastIndexOf("&"));
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            return JSONObject.parseObject(result);
        } catch (IOException e) {
            log.error("发送get请求失败:" + e);
            throw new UtilException(ErrorCodeConstants.SYSTEM_ERROR);
        }
    }

    public static JSONObject post(String path, String templateJsonStr) {
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(path);
            //添加传入参数
            httpPost.setEntity(new StringEntity(templateJsonStr, StandardCharsets.UTF_8));

            //发送相应数据
            CloseableHttpResponse reseponse = httpClient.execute(httpPost);
            HttpEntity res = reseponse.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(res.getContent(), StandardCharsets.UTF_8));
            String result = "";
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            log.error("发送post请求失败:" + e);
            throw new UtilException(ErrorCodeConstants.SYSTEM_ERROR);
        }
    }

}
