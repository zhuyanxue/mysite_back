package com.mysite.api.controller;

import com.alibaba.fastjson.JSONArray;
import com.mysite.api.config.Result;
import com.mysite.api.pojo.*;
import com.mysite.api.service.*;
import com.mysite.api.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Api("mysite所有接口，请按照以下分类查看接口")
//做rest风格的数据返回
@RestController
@RequestMapping("/api")   //访问基址
@CrossOrigin  //跨域注解，
public class QinYanController {
    @Autowired
    UserService userService; //用户注入

    @Autowired
    AppService appService; //app注入

    @Autowired
    FrameService frameService;//导航注入

    @Autowired
    BlockService blockService;//块注入

    @Autowired
    MinFrameService minFrameService;

    @Autowired
    VideoService videoService;

    @Autowired
    DetailService detailService;

    @Autowired
    CodeService codeService;

    @Autowired
    NoteService noteService;

    @Autowired
    DeskImgService deskImgService;

    @Autowired
    FooterService footerService;


    //日志输出
    private Logger logger = LoggerFactory.getLogger(QinYanController.class);

    //文件上传
    @Value("${file.uploadFolder}")
    private String uploadAbsolutePath;
    @Value("${file.retrunUrl}")
    private String retrunUrl;

    //替换路径
    @Value("${file.repalceurl}")
    private String repalceUrl;

    @ApiOperation("补充swagger实体类显示")
    @UserLoginToken
    @GetMapping("/ShowSwaggerPojoWithNoScan")
    public Result getModel(@RequestBody Code code,@RequestBody DeskImg deskImg,@RequestBody Footer footer){
        return Result.sucess();
    }

    @ApiOperation(value = "使用elasticsearch搜索，显示高亮结果",tags = "搜索")
    @UserLoginToken
    @GetMapping("/getSearchResult")
    public Result searchResult(@RequestParam("keyWords")String key) throws Exception{
        List<Detail> detailList=detailService.search(key);
        if(detailList.size()>0){
            return Result.sucess(detailList);
        }else {
            return Result.fail("未查到相关数据");
        }
    }

    @ApiOperation(value = "清空elasticsearch文档数据，再重新初始化，避免数据冗余",tags = "搜索")
    @UserLoginToken
    @PostMapping("/deleteGetAllEs")
    public Result deleteAndGet(){
        detailService.deleteAndGetAll();
        return Result.sucess();
    }
    /*==========================================   底部   ====================================================*/
    @ApiOperation(value = "新增一条页脚信息，并设置状态值为1（显示）",tags = "页脚信息")
    @UserLoginToken
    @PostMapping("/newFooter")
    public Result newFooter(@RequestParam("content")String content){
        Footer footer=new Footer();
        footer.setContent(content);
        List<Footer> footerList=footerService.findByStatus(1);//所有1（显示）
        for(Footer footer1:footerList){
            footer1.setStatus(0);//全置0
        };
        footer.setStatus(1);
        footerService.saveFooter(footer);
        return Result.sucess();
    }

    @ApiOperation(value = "拿到状态值为1的一条页脚信息",tags = "页脚信息")
    @UserLoginToken
    @GetMapping("/getFooterOne")
    public Result findFooterOne(){
        List<Footer> footerList=footerService.findByStatus(1);
        if(footerList.size()>0){
            return Result.sucess(footerList.get(0));
        }else {
            return Result.fail("未找到相关底部数据");
        }
    }

    @ApiOperation(value = "获得所有页脚数据记录",tags = "页脚信息")
    @UserLoginToken
    @GetMapping("/getAllFooter")
    public Result findAllFooters(){
        List<Footer> footerList=footerService.findAllFooter();
        return Result.sucess(footerList);
    }

    @ApiOperation(value = "删除页脚信息",tags = "页脚信息")
    @UserLoginToken
    @DeleteMapping("/deleteFooter")
    public Result delteFooter(@RequestParam("id")int id){
        footerService.deleteFooter(id);
        return Result.sucess();
    }

    @ApiOperation(value = "设置页脚信息显示，置状态为1",tags = "页脚信息")
    @UserLoginToken
    @PutMapping("/setStatusOne")
    public Result setTopFooter(@RequestParam("id")int id){
        Footer footer=footerService.getOneFooter(id);
        List<Footer> footerList=footerService.findByStatus(1);
        for(Footer footer1:footerList){
            footer1.setStatus(0);
        };
        footer.setStatus(1);
        footerService.saveFooter(footer);
        return Result.sucess();
    }




    /*==========================================   壁纸    ====================================================*/
    @ApiOperation(value = "上传壁纸，并显示",tags = "壁纸")
    @UserLoginToken
    @PostMapping("/saveDeskImg")
    public Result saveDeskImg(@RequestParam("url")String url){
        //上传时，先全部设0
        List<DeskImg> deskImgList=deskImgService.findByStatus(1);//找到所有为1的。
        if(deskImgList.size()>0){ //更新状态
            for(DeskImg deskImg1:deskImgList){
                deskImg1.setStatus(0);
                deskImgService.saveDeskImg(deskImg1);
            }
        };
        DeskImg deskImg=new DeskImg();
        deskImg.setUrl(url);
        deskImg.setStatus(1);//默认为0
        deskImgService.saveDeskImg(deskImg);
        return Result.sucess();
    }

    @ApiOperation(value = "查找所有壁纸",tags = "壁纸")
    @UserLoginToken
    @GetMapping("/findAllDeskImg")
    public Result findAllDeskImg(){
        List<DeskImg> deskImgList=deskImgService.findAllDeskImg();
        if(deskImgList.size()>0){
            return  Result.sucess(deskImgList);
        }else {
            return Result.fail("还未上传过壁纸");
        }
    }

    @ApiOperation(value = "查找正在显示的壁纸",tags = "壁纸")
    @UserLoginToken
    @GetMapping("/getStatusDeskImg")
    public Result getSetDeskImg(){
     List<DeskImg> deskImgList=deskImgService.findByStatus(1);
     if(deskImgList.size()>0){
         String deskImgSetUrl=deskImgList.get(0).getUrl();
         return Result.sucess(deskImgSetUrl);
     }else {
         return Result.fail("还未设置壁纸");
     }
    }

    @ApiOperation("未被调用的接口")
    @UserLoginToken
    @PutMapping("/updateDeskImg")
    public Result updataDeskImg(@RequestParam("id")int id){
        //上传时，先全部设0
        List<DeskImg> deskImgList=deskImgService.findByStatus(1);//找到所有为1的。
        if(deskImgList.size()>0){ //更新状态
            for(DeskImg deskImg1:deskImgList){
                deskImg1.setStatus(0);
                deskImgService.saveDeskImg(deskImg1);
            }
        };

        DeskImg deskImg=deskImgService.findById(id);
        deskImg.setStatus(1);
        deskImgService.saveDeskImg(deskImg);
        return Result.sucess();
    }







    /*==========================================   笔记    ====================================================*/
    @ApiOperation(value = "新增笔记",tags = "笔记")
    @UserLoginToken
    @PostMapping("/newRealNote")
    public Result NewNote(@RequestParam("content")String content,@RequestParam("uid")int uid,@RequestParam("did")int did){
        Note note=new Note();
        note.setContent(content);
        Users users=userService.getUser(uid);
        Detail detail=detailService.findOneById(did);
        note.setUser(users);
        note.setDetail(detail);
        Date date=new Date();
        note.setCreateDate(date);
        noteService.saveNote(note);
        return Result.sucess();
    }

    @ApiOperation(value = "删除笔记",tags = "笔记")
    @UserLoginToken
    @DeleteMapping("/deleteNote")
    public Result deleteNote(@RequestParam("nid")int nid){
        noteService.deleteNote(nid);
        return Result.sucess();
    }

    @ApiOperation(value = "编辑笔记",tags = "笔记")
    @UserLoginToken
    @PutMapping("/editSaveNote")
    public Result editNoteSave(@RequestParam("nid")int nid,@RequestParam("content")String content){
        Note note=noteService.findOneById(nid);
        note.setContent(content);
        noteService.saveNote(note);
        return Result.sucess();
    }


    /*==========================================   详情    ====================================================*/
    @ApiOperation(value = "新增主题和内容",tags = "详情")
    @UserLoginToken
    @PostMapping("/newSubmitDetail")
    public Result newDetail(@RequestParam("title")String title,@RequestParam("details")String details,@RequestParam("mfid")int mfid){
        //保存内容到数据库
        MinFrame minFrame=minFrameService.getOneById(mfid);
        Detail detail=new Detail();
        detail.setMinFrame(minFrame);
        detail.setTitle(title);
        detail.setDetails(details);
        detailService.saveDetail(detail);
        return Result.sucess();
    }

    @ApiOperation(value = "编辑内容时，删除已上传的图片",tags = "详情")
    @UserLoginToken
    @PostMapping("/detaleOutImgs")
    public Result deleteOutImgs(@RequestBody String dImgs){
        List<String> cdImgs= JSONArray.parseArray(dImgs,String.class);
        if(cdImgs.size()>0){
            System.out.println(cdImgs);
            for(String img:cdImgs){
                String imgUrl=img;
                String imgName=imgUrl.replace(repalceUrl,"");
                String imgAddress=uploadAbsolutePath+imgName;
                File absoleFile = new File(imgAddress);
                if(absoleFile.exists()){
                    absoleFile.delete();
                }
            };
            return Result.sucess();
        }else {
            return Result.fail("未产生废弃图片");
        }
    }

    @ApiOperation(value = "拿到所属小导航下所有详情数据",tags = "详情")
    @UserLoginToken
    @GetMapping("/getAllDetails")
    public Result getAllDetailByMin(@RequestParam("mfid")int mfid){
        MinFrame minFrame=minFrameService.getOneById(mfid);
        List<Detail> details=detailService.findAllByMf(minFrame);
        if(details.size()>0){
            return Result.sucess(details);
        }else {
            return Result.fail("尚未添加步骤详情数据");
        }
    }

    @ApiOperation(value = "删除详情记录",tags = "详情")
    @UserLoginToken
    @DeleteMapping("/deleteDetail")
    public Result deleteDetail(@RequestParam("id")int id){
        //删除数据库记录
        detailService.deleteDetail(id);
        return Result.sucess();
    }
    @ApiOperation(value = "编辑详情记录，保存",tags = "详情")
    @UserLoginToken
    @PutMapping("/saveEditDetail")
    public Result saveEditDetail(@RequestParam("id")int id,@RequestParam("title")String title,@RequestParam("details")String details){
        Detail detail=detailService.findOneById(id);
        detail.setTitle(title);
        detail.setDetails(details);
        detailService.saveDetail(detail);
        return  Result.sucess();
    }

    /*==========================================   视频    ====================================================*/
    @ApiOperation(value = "上传视频",tags = "视频")
    @UserLoginToken
    @PostMapping("/saveVideo")
    public Result saveUpload(@RequestParam("bid")int bid,@RequestParam("videoUrl")String videoUrl,@RequestParam("name")String name,@RequestParam("status")int status){
        Block block=blockService.findOneById(bid);
        Videos videos=new Videos();
        videos.setBlock(block);
        videos.setUrl(videoUrl);
        videos.setName(name);
        videos.setStatus(status);
        videoService.saveVideo(videos);
        return Result.sucess();
    }

    @ApiOperation(value = "拿到展示的视频记录，状态值为1",tags = "视频")
    @UserLoginToken
    @GetMapping("/getCanVideo")
    public Result getVideo(@RequestParam("bid")int bid){
        Block block=blockService.findOneById(bid);
        List<Videos> lists=videoService.findByBlcokAndStatusOne(block,1);
        if(lists.size()>1){
            List<String> returnName=new ArrayList<String>();
            for(Videos video:lists){
                    returnName.add(video.getName());
            }
            return Result.fail("获取置顶视频数据大于1，请手动调整相应数据状态！",returnName);
        }else{
            if(lists.size()>0){
                return Result.sucess(lists.get(0));
            }else{
                return Result.fail("尚未添加视频数据！",1);
            }
        }
    }

    @ApiOperation(value = "返回视频所属的模块",tags = "视频")
    @UserLoginToken
    @GetMapping("/inGetBlock")
    public Result getInVideo(@RequestParam("bid")int bid){
        Block block=blockService.findOneById(bid);
        return Result.sucess(block);
    }

    @ApiOperation(value = "获得指定模块下的所有视频数据",tags = "视频")
    @UserLoginToken
    @GetMapping("/getAllVideoByBlock")
    public Result getAllVideos(@RequestParam("bid")int bid){
        Block block=blockService.findOneById(bid);
        List<Videos> videosList1=videoService.findByBlcokAndStatusOne(block,1);
        if(videosList1.size()>1){
            return Result.fail("置顶数据不只一个，请调整任意一视频状态！",videosList1);
        }else{
            List<Videos> videosList2=videoService.findAllByBlcok(block);
           return Result.sucess(videosList2);
        }
    }

    @ApiOperation(value = "删除视频",tags = "视频")
    @UserLoginToken
    @DeleteMapping("/deleteVideo")
    public Result deleteVideo(@RequestParam("vid")int vid){
        String resultInfo = null;
        Videos videos=videoService.findOneById(vid);
        String videosName=videos.getName();

        String videoAddress=uploadAbsolutePath+videosName;
        File absoleFile = new File(videoAddress);
        if(absoleFile.exists()){
            absoleFile.delete();
            resultInfo="已删除视频文件";
        }else{
            resultInfo="未找到原视频文件";
        };

        videoService.deleteVideo(vid);

        return Result.sucess(resultInfo);
    }

    @ApiOperation(value = "设置视频作为显示，将状态值由0置1",tags = "视频")
    @UserLoginToken
    @PutMapping("/editTopVideo")
    public Result topVideo(@RequestParam("vid")int vid,@RequestParam("bid")int bid){
        Block block=blockService.findOneById(bid);
        List<Videos> videosList=videoService.findByBlcokAndStatusOne(block,1);
        for(Videos videos:videosList){
            videos.setStatus(0);
        };
        Videos video=videoService.findOneById(vid);
        video.setStatus(1);
        videoService.saveVideo(video);
        return Result.sucess();
    }
    /*==========================================   小导航   ====================================================*/
    @ApiOperation(value = "通过所属模块拿到所有小导航",tags = "小导航")
    @UserLoginToken
    @GetMapping("/getMinFrame")
    public Result findAllMinFrameBlock(@RequestParam("bid")int bid){
        Block block=blockService.findOneById(bid);
        List<MinFrame> minFrames=minFrameService.findAllByBlock(block);
        if(!minFrames.isEmpty()){
            return Result.sucess(minFrames);
        }else{
            return Result.fail("此模块还尚未添加数据！");
        }
    }
    @ApiOperation(value = "新增一条小导航记录",tags = "小导航")
    @UserLoginToken
    @PostMapping("/submitMinFrame")
    public Result newMinFrame(@RequestParam("mintitle")String minTitle,@RequestParam("bid")int bid){
        Block block=blockService.findOneById(bid);
        MinFrame minFrame=new MinFrame();
        minFrame.setBlock(block);
        minFrame.setMinTitle(minTitle);
        minFrameService.saveMinFrame(minFrame);
        return Result.sucess();
    }

    @ApiOperation(value = "删除一条小导航记录",tags = "小导航")
    @UserLoginToken
    @DeleteMapping("/deleteMinFrame")
    public Result deleteMinFrame(@RequestParam("mid")int mid){
        minFrameService.deleteMinFrame(mid);
        return Result.sucess();
    }

    @ApiOperation(value = "编辑一条小导航记录",tags = "小导航")
    @UserLoginToken
    @PutMapping("/editMinFrame")
    public Result updataMinFrame(@RequestParam("id")int id,@RequestParam("minTitle")String minTitle){
        MinFrame minFrame=minFrameService.getOneById(id);
        minFrame.setMinTitle(minTitle);
        minFrameService.saveMinFrame(minFrame);
        return Result.sucess();
    }


    /*==========================================   模块    ====================================================*/
    @ApiOperation(value = "新增一个模块",tags = "模块")
    @UserLoginToken
    @PostMapping("/newBlockSave")
    public  Result newBlock(@RequestParam("frameid")int fid,@RequestParam("title")String title,@RequestParam("describes")String describe,@RequestParam("progress")int progress){
        Block block=new Block();
        Frame frame=frameService.findById(fid);
        block.setFrame(frame);//块
        block.setTitle(title);//标题
        block.setDescribes(describe);//详情
        block.setProgress(progress);//进度
        blockService.saveBlock(block);
        return Result.sucess();
    }

    @ApiOperation(value = "查询导航下的所有模块",tags = "模块")
    @UserLoginToken
    @GetMapping("/defaultGetBlock")
    public Result findAllByFrame(@RequestParam("fid")int fid){
        Frame frame=frameService.findById(fid);
        List<Block> blocks=blockService.findAllBlockByFrame(frame);
        if(!blocks.isEmpty()){
            return Result.sucess(blocks);
        }else{
            return Result.fail("此导航还尚未添加任何块！");
        }
    }

    @ApiOperation(value = "删除一个模块",tags = "模块")
    @UserLoginToken
    @DeleteMapping("/deleteBlock")
    public Result deleteBlock(@RequestParam("bid")int bid){
        blockService.deleteBlockById(bid);
        return Result.sucess();
    }

    @ApiOperation(value = "编辑一个模块",tags = "模块")
    @UserLoginToken
    @PutMapping("/updataBlock")
    public Result upateBlock(@RequestParam("id")int id,@RequestParam("title")String title,@RequestParam("describes")String describes){
        Block block=blockService.findOneById(id);
        block.setTitle(title);
        block.setDescribes(describes);
        blockService.saveBlock(block);
        return  Result.sucess();
    }

    /*==========================================  导航    ====================================================*/

    //参数的传递结束，若用requestparam接收，则应是application/x-www-form-urlencoded，qs转换。若用requestbody,则该是application/json。
    @ApiOperation(value = "新增一条导航记录",tags = "导航")
    @UserLoginToken
    @PostMapping("/newFrame")
    public Result newFrame(@RequestParam("frameName")String frameName,@RequestParam("appid")int id ){
        Frame frame=new Frame();
        frame.setFrameName(frameName);
        App app=appService.findById(id);
        frame.setApp(app);
        frameService.saveFrame(frame);
        return Result.sucess();
    }

    @ApiOperation(value = "查询所属应用下的所有导航",tags = "导航")
    @UserLoginToken
    @GetMapping("/getAllFrames")
    public Result getAllFrame(@RequestParam("appid")int id){
        App app=appService.findById(id);
        List<Frame> frames=frameService.findAllByApp(app);
        if(!frames.isEmpty()){
            return Result.sucess(frames);
        }else {
            return Result.fail("此项应用还尚未添加任何导航数据！");
        }
    }

    @ApiOperation(value = "删除一条导航记录",tags = "导航")
    @UserLoginToken
    @DeleteMapping("/deleteRealFrame/{id}")
    public Result deleteFrame(@PathVariable("id")int id){
        frameService.deleteFrame(id);
        return Result.sucess();
    }

    @ApiOperation(value = "获得一条导航记录",tags = "导航")
    @UserLoginToken
    @GetMapping("/getOneInfo")
    public Result getInfo(@RequestParam("id")int id){
        Frame frame=frameService.findById(id);
        if(frame!=null){
            return  Result.sucess(frame);
        }else{
            return Result.fail("未查到相关数据！");
        }
    }

    @ApiOperation(value = "更新一条导航记录",tags = "导航")
    @UserLoginToken
    @PutMapping("/updataFrame")
    public Result updateFrame(@RequestBody Frame frame){
        frameService.saveFrame(frame);
        return Result.sucess();
    }

    /*==========================================  用户   ====================================================*/
    @ApiOperation(value = "登录",tags = "用户")
    @PassToken  //去掉token验证
    @PostMapping("/login")
    public Result login(@RequestBody Users users){
        //加密获得密码
        String orign=users.getPassword();
        String name=users.getName();
        String salt = PasswordUtil.md5(users.getName()+users.getPassword()+"qinyan");
        String realPassword = PasswordUtil.md5(users.getPassword()+salt);
        Users user=userService.login(users.getName(),realPassword);
        if(user!=null){
            String token = TokenUtil.getToken(user); //拿到token
            String id=String.valueOf(user.getId());
            return Result.sucess(user,token);
        }else {
            return Result.fail("用户名或密码错误!");
        }
    }

    @ApiOperation(value = "注册",tags = "用户")
    @PassToken
    @PostMapping("/submitRegister")
    public Result register(@RequestBody Users users){
        /*查看昵称是否重复
        List<Users> usersList=userService.findAllUser();
        for(Users users1:usersList){
            if(users1.getName()==users.getName()){
                return Result.fail("昵称已经被使用，请换一个！");
            }
        };*/
        String salt = PasswordUtil.md5(users.getName()+users.getPassword()+"qinyan");
        String md5Pwd = PasswordUtil.md5(users.getPassword()+salt);
        users.setPassword(md5Pwd);
        //默认角色,管理员、青彦（仅一个）。自动为管理员
        if(users.getName().equals("青彦")){
            users.setRole("管理员");
        }else {
            users.setRole("用户");
        }
        userService.save(users);
        return Result.sucess();
    };

    @ApiOperation(value = "判断用户名是否已被使用",tags = "用户")
    @PassToken
    @PostMapping("/judgeName")
    public Result seeIsName(@RequestParam("name")String name){
        boolean isUse=judgeName(name);
        if(isUse){
            return Result.sucess("昵称已经被使用，请换一个！");
        }else {
            return Result.fail("用户名未被使用");
        }
    }

    public boolean judgeName(String name){
        List<Users> usersList=userService.findAllUser();
        for(Users users:usersList){
            if(users.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    //重置密码
    @ApiOperation(value = "重置密码",tags = "用户")
    @PassToken
    @PostMapping("/subNewPassword")
    public Result setNewPassWord(@RequestParam("email")String email,@RequestParam("password")String password,HttpServletRequest request){
        //根据邮箱查用户
        List<Users> usersList=userService.findByEmail(email);

        StringBuffer buf=new StringBuffer();//得到的所有用户信息、。
        for(Users users:usersList){
            //密码加密
            buf.append("登录名："+users.getName()+",新密码："+password+";");
            String salt = PasswordUtil.md5(users.getName()+password+"qinyan");
            String md5Pwd = PasswordUtil.md5(password+salt);
            users.setPassword(md5Pwd);
            //保存
            userService.save(users);
        }
        int usersize=usersList.size();//用户个数

        //。二维码生成
        urlImg userInfoImg=new urlImg();

        //内容字符串。文件路径
        String fileName = UUID.randomUUID().toString()+".jpg";//文件名
        boolean getImg=userInfoImg.orCode(buf.toString(),uploadAbsolutePath+fileName);
        if(getImg){
            //成功生成二维码。返回二维码路径
            String fileUrl=retrunUrl;
            fileUrl = fileUrl + request.getContextPath() + "/files/" + fileName;
            return Result.sucess(usersize,fileUrl);//返回二维码路径。
        }else{
            //未生成二维码
            return Result.fail(String.valueOf(usersize),usersList);//返回用户列表和密码
        }
    }

    @ApiOperation(value = "生成二维码工具",tags = "用户")
    @PassToken  //去掉token验证
    @PostMapping("/newUrlUserImg")
    public Result newUserMyImg(@RequestParam("content")String contents){
        //String usrHome = System.getProperty("user.home");
        String fileName = UUID.randomUUID().toString()+".jpg";//文件名
        urlImg userInfoImg=new urlImg();
        boolean getImg=userInfoImg.orCode(contents,"C:\\"+fileName);
        if(getImg){
            return Result.sucess(fileName);
        }else {
            return Result.fail("二维码生成失败");
        }
    }


    private static JavaMailSenderImpl javaMailSender;
    static {
        javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.qq.com");//链接服务器
        //javaMailSender.setPort(25);//默认使用25端口发送
        javaMailSender.setUsername("1789204734@qq.com");//账号
        javaMailSender.setPassword("xbohnyrlxnqhbbdi");//授权码
        javaMailSender.setDefaultEncoding("UTF-8");

        Properties properties = new Properties();
        //properties.setProperty("mail.debug", "true");//启用调试
        //properties.setProperty("mail.smtp.timeout", "1000");//设置链接超时
        //设置通过ssl协议使用465端口发送、使用默认端口（25）时下面三行不需要
        properties.setProperty("mail.smtp.auth", "true");//开启认证
        properties.setProperty("mail.smtp.socketFactory.port", "465");//设置ssl端口
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        javaMailSender.setJavaMailProperties(properties);
    }

    @ApiOperation(value = "发送邮箱验证码",tags = "用户")
    @PassToken
    @PostMapping("/sendIdentifyCode")
    public Result sendCode(@RequestParam("emailAddress")String email){
        //校验邮箱，是否存在用户
        List<Users> userslist=userService.findByEmail(email);
        if(userslist.size()>0) {
            List<Code> codeList = codeService.getAllCode();
            if (codeList.size() > 0) {
                codeService.deleteAll();
            }
            ;
            Code newcode = new Code();
            //发送时间
            Date sendtime = new Date();
            newcode.setSendTime(sendtime);
            //验证码
            String orignCode = String.format("%04d", new Random().nextInt(9999));//四位随机验证码
            StringBuilder sb = new StringBuilder();
            String sendContent = "您好！您重置密码的验证码是：【" + orignCode + "】，此验证码五分钟内有效！";
            sb.append(sendContent);

            //发送验证码
            try {
               /*
               MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom("1789204734@qq.com", "1789204734");
                helper.setTo(email);
                helper.setSubject("青彦网【重置密码】");
                helper.setText(sb.toString(), true);
                javaMailSender.send(message);*/

                JavaMailSenderImpl mailSender=new JavaMailSenderImpl();
                mailSender.setHost("smtp.163.com");
                mailSender.setUsername("13658766721@163.com");
                mailSender.setPassword("ZOLMZPMAVKMQPAFO");

                SimpleMailMessage messages = new SimpleMailMessage();
                messages.setFrom("13658766721@163.com");
                messages.setTo(email);
                messages.setSubject("青彦网【重置密码】");//主题
                messages.setText(sb.toString());//正文
                mailSender.send(messages);


            } catch (Exception e) {
                logger.error("邮件发送失败", e.getMessage());
                e.printStackTrace();
            }
            //对验证码加密保存到库
            String md5Code = PasswordUtil.md5(orignCode + "qinyan");
            newcode.setIcode(md5Code);
            codeService.add(newcode);
            return Result.sucess();
        }else {
            return Result.fail("该邮箱尚未注册过！");
        }
    }

    //校验验证码
    @ApiOperation(value = "验证邮箱验证码是否正确",tags = "用户")
    @PassToken
    @PostMapping("/submitCode")
    public Result seeCode(@RequestParam("code")String code){
        String md5Code=PasswordUtil.md5(code + "qinyan");
        Code code1=codeService.findByIcode(md5Code);
        if(code1!=null){
            Date nowtime=new Date();
            Date oldtime=code1.getSendTime();
            long diff=nowtime.getTime()-oldtime.getTime();
            int real=(int)diff/60000;
            if(real>5){
                return Result.fail("验证码已过期，请重新获取");
            }else{
                return Result.sucess();
            }
        }else{
            return Result.fail("验证码输入有误");
        }
    }

    @ApiOperation(value = "查找指定用户",tags = "管理")
    @UserLoginToken  //需要token验证
    @GetMapping("/searchResultName")
    public Result searchByNameKey(@RequestParam("key")String key){
        if(key==""){
            List<Users> usersList=userService.findAllUser();
            if(usersList.size()>0){
                return Result.sucess(usersList);
            }else{
                return Result.fail("未找到相关用户!");
            }
        }else {
            List<Users> usersList = userService.searchByNameKey(key);
            if(usersList.size()>0){
                return Result.sucess(usersList);
            }else{
                return Result.fail("未找到相关用户!");
            }
        }
    }

    @ApiOperation(value = "设置用户角色",tags = "管理")
    @UserLoginToken  //需要token验证
    @PostMapping("/changeUserStatus")
    public Result setPutStatus(@RequestParam("changeRole")String role,@RequestParam("id")int id){
        Users users=userService.getUser(id);
        users.setRole(role);
        userService.save(users);
        return Result.sucess();
    }

    @ApiOperation(value = "更新用户邮箱",tags = "管理")
    @UserLoginToken  //需要token验证
    @PostMapping("/updateUserEmail")
    public Result changeEmail(@RequestParam("id")int id,@RequestParam("email")String email){
        Users users=userService.getUser(id);
        users.setEmail(email);
        userService.save(users);
        return Result.sucess();
    }


    /*==========================================  应用    ====================================================*/
    //查找所有app
    @ApiOperation(value = "查找指定所有应用",tags = "应用")
    @UserLoginToken  //需要token验证
    @GetMapping("/getAllAapp")
    public Result getAllApp(){
        List<App> apps=appService.findAllApp();
        if(!apps.isEmpty()){
            return Result.sucess(apps);
        }else{
            return Result.fail("未查询到任何应用数据，请与管理员联系！");
        }
    }
    //加解锁
    @ApiOperation(value = "对应用加锁和解锁",tags = "应用")
    @UserLoginToken
    @PostMapping("/suoapp")
    public Result suoApp(@RequestParam("id")int id){
        App app=appService.findById(id);
        String appStatus=app.getStatus();
        if(appStatus.equals(("加锁"))){
            app.setStatus("解锁");
        }else{
            app.setStatus("加锁");
        }
        appService.save(app);
        return Result.sucess();
    }


    //保存应用
    @ApiOperation(value = "新增一个应用",tags = "应用")
    @UserLoginToken
    @PostMapping("/saveApp")
    public Result saveApp(@RequestParam("appName")String appName,@RequestParam("img")String img,@RequestParam("userId")int userid){
        App app=new App();
        app.setAppName(appName);
        app.setImg(img);
        app.setStatus("加锁");
        Users users=userService.getUser(userid);
        app.setUsers(users);
        appService.save(app);
        return Result.sucess();
    }

    //删除壁纸
    @ApiOperation(value = "删除壁纸",tags = "壁纸")
    @UserLoginToken
    @DeleteMapping("/deleteDeskImg")
    public Result deleteDeskImg(@RequestParam("id")int id,@RequestParam("imgurl")String imgurl){
        //先删除文件
        String resultInfo = null;
        String img=imgurl.replace(repalceUrl,"");
        String imgAddress=uploadAbsolutePath+img;
        File absoleFile = new File(imgAddress);
        if(absoleFile.exists()){
            absoleFile.delete();
            resultInfo="已删除图片文件";
        }else{
            resultInfo="未找原图片文件";
        };
        deskImgService.deleteDeskImgById(id);
        return Result.sucess(resultInfo);
    }
    @ApiOperation(value = "获得应用名称",tags = "应用")
    @UserLoginToken
    @GetMapping("/getAppName")
    public Result findAppName(@RequestParam("id")int id){
        App app=appService.findById(id);
        String appName=app.getAppName();
        return Result.sucess(appName);
    }


    //删除应用
    @ApiOperation(value = "删除一个应用",tags = "应用")
    @UserLoginToken
    @DeleteMapping("/deleteApp/{id}")
    public Result deleteApp(@PathVariable("id")int id){
        String resultInfo = null;
        App app=appService.findById(id);
        String oldImg=app.getImg();
        String img=oldImg.replace(repalceUrl,"");

        String imgAddress=uploadAbsolutePath+img;
        File absoleFile = new File(imgAddress);
        if(absoleFile.exists()){
            absoleFile.delete();
            resultInfo="已经删除图标";
        }else{
            resultInfo="未找到原图标";
        };
        appService.deleteAppById(id);
        return Result.sucess(resultInfo);
    }

    @ApiOperation(value = "删除二维码图片",tags = "用户")
    @PassToken  //去掉token验证
    @PostMapping("/delteInfoImg")
    public Result deleteImgUrl(@RequestParam("imgurl")String imgUrl){
        String imgName=imgUrl.replace(repalceUrl,"");
        String imgAddress=uploadAbsolutePath+imgName;
        File absoleFile = new File(imgAddress);
        if(absoleFile.exists()){
            absoleFile.delete();
            return Result.sucess();
        }else{
            return Result.fail("未找到二维码图片");
        }
    }




    //获取一个应用
    @ApiOperation(value = "获得一个应用记录",tags = "应用")
    @UserLoginToken
    @GetMapping("/getOneApp")
    public Result getEditApp(@RequestParam("id")int id){
        App app=appService.findById(id);
        if(app!=null){
        return Result.sucess(app);
        }else {
            return Result.fail("未查到相关数据，请与管理员联系");
        }
    }
    //保存编辑应用，仅名称
    @ApiOperation(value = "编辑一个应用（仅名称）",tags = "应用")
    @UserLoginToken
    @PutMapping("/putAppName")
    public Result putAppName(@RequestParam("id")int id,@RequestParam("editName")String editName){
        App app=appService.findById(id);
        app.setAppName(editName);
        appService.save(app);
        return Result.sucess();
    }
    //保存编辑应用，所有信息
    @ApiOperation(value = "编辑一个应用（全信息）",tags = "应用")
    @UserLoginToken
    @PutMapping("/saveEditApp")
    public Result putAllApp(@RequestBody App app,@RequestParam("oldImgName")String oldImg,@RequestParam("userId")int id){
        Users users=userService.getUser(id);
        app.setUsers(users);
        String resultInfo = null;
        String oldFile=uploadAbsolutePath+oldImg;
        File absoleFile = new File(oldFile);
        if(absoleFile.exists()){
            absoleFile.delete();
            resultInfo="已经删除原图标";
        }else{
            resultInfo="未找到原图标";
        };
        //保存新信息
        if(app!=null){
            appService.save(app);
            return Result.sucess(resultInfo);
        }else {
            return  Result.fail("未查询到原应用！");
        }

    }




    /*==========================================  上传    ====================================================*/

    //
    @ApiOperation(value = "富文本图片上传实现",tags = "详情")
    @UserLoginToken
    @RequestMapping("/uploadDetailImg")
    public Result returnDetailImgUrl(@RequestParam("pic") MultipartFile picture,HttpServletRequest request){
        if(!picture.isEmpty()){
            String fileName = picture.getOriginalFilename();
            Calendar currTime = Calendar.getInstance();
            String time = String.valueOf(currTime.get(Calendar.YEAR))+String.valueOf((currTime.get(Calendar.MONTH)+1));
            //获取文件名后缀
            String suffix = fileName.substring(picture.getOriginalFilename().lastIndexOf("."));
            suffix = suffix.toLowerCase();
            fileName = UUID.randomUUID().toString()+suffix;
            File targetFile = new File(uploadAbsolutePath, fileName);
            if(!targetFile.getParentFile().exists()){    //注意，判断父级路径是否存在
                targetFile.getParentFile().mkdirs();
            }

            try {
                //上传
                picture.transferTo(targetFile);

            } catch (IOException e) {
                e.printStackTrace();
                return Result.fail("图片上传失败！");
            }
            String fileUrl=retrunUrl;
            fileUrl = fileUrl + request.getContextPath() + "/files/" + fileName;
            //System.out.println("图片已上传");
            return new Result("0000","上传成功",fileUrl);
        }else{
            return Result.fail("上传失败，请确保文件是图片！");
        }
    }


    //图片上传
    @ApiOperation(value = "壁纸上传实现",tags = "壁纸")
    @UserLoginToken
    @RequestMapping("/uploadImg")
    public Result uploadImg(@RequestParam("picture") MultipartFile picture,HttpServletRequest request){
        if(!picture.isEmpty()){
            String fileName = picture.getOriginalFilename();
            Calendar currTime = Calendar.getInstance();
            String time = String.valueOf(currTime.get(Calendar.YEAR))+String.valueOf((currTime.get(Calendar.MONTH)+1));
            //获取文件名后缀
            String suffix = fileName.substring(picture.getOriginalFilename().lastIndexOf("."));
            suffix = suffix.toLowerCase();
            fileName = UUID.randomUUID().toString()+suffix;
            File targetFile = new File(uploadAbsolutePath, fileName);
            if(!targetFile.getParentFile().exists()){    //注意，判断父级路径是否存在
                targetFile.getParentFile().mkdirs();
            }

            try {
                //上传
                picture.transferTo(targetFile);

            } catch (IOException e) {
                e.printStackTrace();
                return Result.fail("图片上传失败！");
            }
            String fileUrl=retrunUrl;
            fileUrl = fileUrl + request.getContextPath() + "/files/" + fileName;
            return new Result("0000","上传成功",fileUrl);
        }else{
            return Result.fail("上传失败，请确保文件是图片！");
        }

    }
    //修改图片，上传
    @ApiOperation(value = "富文本图片编辑",tags = "详情")
    @UserLoginToken
    @RequestMapping("/editImg")
    public Result editUploadImg(@RequestParam("editImage") MultipartFile editImage,HttpServletRequest request){
        if(!editImage.isEmpty()){
            String fileName = editImage.getOriginalFilename();
            Calendar currTime = Calendar.getInstance();
            String time = String.valueOf(currTime.get(Calendar.YEAR))+String.valueOf((currTime.get(Calendar.MONTH)+1));
            //获取文件名后缀
            String suffix = fileName.substring(editImage.getOriginalFilename().lastIndexOf("."));
            suffix = suffix.toLowerCase();
            fileName = UUID.randomUUID().toString()+suffix;
            File targetFile = new File(uploadAbsolutePath, fileName);
            if(!targetFile.getParentFile().exists()){    //注意，判断父级路径是否存在
                targetFile.getParentFile().mkdirs();
            }

            try {
                //上传
                editImage.transferTo(targetFile);

            } catch (IOException e) {
                e.printStackTrace();
                return Result.fail("图片上传失败！");
            }
            String fileUrl=retrunUrl;
            fileUrl = fileUrl + request.getContextPath() + "/files/" + fileName;
            return new Result("0000","上传成功",fileUrl);
        }else{
            return Result.fail("上传失败，请确保文件是图片！");
        }

    }

    //视频上传
    @ApiOperation(value = "视频上传实现",tags = "视频")
    @UserLoginToken
    @RequestMapping("/uploadVideo")
    public Result uploadVideo(@RequestParam("video") MultipartFile video,HttpServletRequest request){
        if(!video.isEmpty()){
            String fileName = video.getOriginalFilename();
            Calendar currTime = Calendar.getInstance();
            //String time = String.valueOf(currTime.get(Calendar.YEAR))+String.valueOf((currTime.get(Calendar.MONTH)));
            String time =  new SimpleDateFormat("yyyy-MM-dd_").format(new Date().getTime());
            String uId = UUID.randomUUID().toString().replace("-", "");
            //获取文件名后缀
            String suffix = fileName.substring(video.getOriginalFilename().lastIndexOf("."));
            suffix = suffix.toLowerCase();
            fileName = time+uId+suffix;
            File targetFile = new File(uploadAbsolutePath, fileName);
            if(!targetFile.getParentFile().exists()){    //注意，判断父级路径是否存在
                targetFile.getParentFile().mkdirs();
            }
            try {
                //上传
                video.transferTo(targetFile);
            } catch (IOException e) {
                e.printStackTrace();
                return Result.fail("视频上传失败！");
            }
            String fileUrl=retrunUrl;
            fileUrl = fileUrl + request.getContextPath() + "/files/" + fileName;
            logger.info("fileUrl:" + fileUrl);
            return new Result("0000","上成功",fileUrl);
        }else{
            return Result.fail("上传失败，请确保文件是视频文件！");
        }

    }

}
