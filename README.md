# 分析应用方法耗时的swing工具
> 采用该项目(https://github.com/zjw-swun/AppMethodOrder)的思路,基于trace文件分析过滤出当前应用(包名)下的方法信息.
# 使用方法
 - 将``sdk\platform-tools``下的dmtracedump添加到系统环境变量
 - 可使用tool文件夹下的``Method-trace-analysis.jar`` 直接导入`.trace文件`,一键分析
# 效果图

<img src="art/preview.png"  />

# 注意点
 - 需先将`dmtracedump`添加至环境变量
 - 方法耗时的单位未`us`
 - `unknow`意味着该方法未结束调用