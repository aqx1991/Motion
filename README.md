# Motion

## Android端记录跑步运动轨迹数据的App

  本项目功能实现了跑步运动轨迹、运动数据（运动日期记录、单个日期运动次数、每次运动的基本数据（运动距离、时长、消耗、速度等））。地图及运动轨迹展示使用的高德地图（可参考高德官方文档进行改进）；运动相关数据使用Realm数据库保存，登录注册功能也是有本地Realm数据库模拟完成；运动日历展示使用开源框架，可更具需求自行修改。
  
  运动轨迹处理修改优化可参考高德官方文档 : https://lbs.amap.com/dev/demo/path-record#Android
  
  界面参考IOS端YSRun（项目地址：https://github.com/moshuqi/YSRun ）
  
  代码简易，欢迎来指点交流！觉得还可以，给个Star^_^

## 更新日志
### 2019.04.09更新：
    * 1.修改定位方式，解决坐标偏移问题；
    * 2.修改轨迹绘制方式，轨迹平滑优化；
    * 3.修改轨迹数据格式，存取数据优化。
  
## 界面预览
### 首页运动日历
<img width="365" height="640" src="https://github.com/aqx1991/Motion/blob/master/ScreenShot/%E9%A6%96%E9%A1%B5.jpg"/>

### 开始运动
<img width="365" height="640" src="https://github.com/aqx1991/Motion/blob/master/ScreenShot/%E8%BF%90%E5%8A%A8%E5%80%92%E8%AE%A1%E6%97%B6.jpg"/>

### 地图模式
<img width="365" height="640" src="https://github.com/aqx1991/Motion/blob/master/ScreenShot/%E8%BF%90%E5%8A%A8-%E5%9C%B0%E5%9B%BE%E6%A8%A1%E5%BC%8F.jpg"/>

### 跑步模式
<img width="365" height="640" src="https://github.com/aqx1991/Motion/blob/master/ScreenShot/%E8%BF%90%E5%8A%A8-%E8%B7%91%E6%AD%A5%E6%A8%A1%E5%BC%8F.jpg"/>

### 运动结果
<img width="365" height="640" src="https://github.com/aqx1991/Motion/blob/master/ScreenShot/%E8%BF%90%E5%8A%A8%E7%BB%93%E6%9E%9C.jpg"/>

### 运动结果
<img width="365" height="640" src="https://github.com/aqx1991/Motion/blob/master/ScreenShot/%E8%BF%90%E5%8A%A8%E8%AE%B0%E5%BD%95.jpg"/>
