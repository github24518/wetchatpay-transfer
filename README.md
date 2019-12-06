# springboot接入微信企业付款

### 1.开发者文档
 https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_1
 
### 2.配置文件 application.properties
```
## 微信应用ID
wx.appid=wx1111111111111 
## 商户账号   
wx.mchid=000000001
## API密钥              
wx.api.secret=aaaaaaaaaa
## 微信转账请求地址 
wx.transfer_url=https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers   

```

### 3.项目中引入支付证书文件
```
  apiclient_cert.p12

```

### 4.maven引入插件
```
     <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-resources-plugin</artifactId>
          <configuration>
            <encoding>UTF-8</encoding>
            <!-- 过滤后缀为pem、pfx的证书文件 -->
            <nonFilteredFileExtensions>
              <nonFilteredFileExtension>pem</nonFilteredFileExtension>
              <nonFilteredFileExtension>pfx</nonFilteredFileExtension>
              <nonFilteredFileExtension>p12</nonFilteredFileExtension>
            </nonFilteredFileExtensions>
          </configuration>
        </plugin>
      </plugins>
```

### 5.下载源码文件  source文件夹  
```
   @Resource
   WechatPayUtil wechatPayUtil;
   
   @Resource("${wx.appid:}") 
   String appid;
   
    @Test
     public void transfer(){
          //支付流水号
          String orderNo = "123456";       
          //用户授权的微信openid
          String openid = ""       ;      
          //用户真实姓名
          String realName = "张三" ;      
          //转账0.3元。最低0.3元。
          Bigdecimal  amount = new Bigdecimal("0.3"); 
          //转账备注
          String remark = "转账张三"  ; 
          //微信应用id
          String appid = appid;  
          //发送转账请求
          wechatPayUtil.transfer(orderNo, openid, realName, amount, remark,appid);
          System.err.println("done");
     }

```











