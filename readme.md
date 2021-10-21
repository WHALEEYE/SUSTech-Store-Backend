### 用户逻辑

#### 登录/注册

可以选择

1. 手机号 + 验证码

2. 学工号 + 密码

进行登录；

初次登录只可使用手机号 + 验证码，自动注册新用户，用户可进入个人主页完善信息。

#### 个人信息

用户可绑定学工号，支付宝账户，设置密码。

绑定学工号和支付宝账户后才可进行交易；绑定学工号并设置密码后才可以进行学工号 + 密码登录。

可使用手机号 + 验证码重设密码。

#### 头像上传

上传时应裁剪至正方形。

前后端均会进行验证。

#### 账户

账户可删除，应提示不可撤销，实际上逻辑删除。

#### 更改密码

更改密码前，需要发送手机验证码进行验证。

#### 学工号绑定

绑定学工号后会自动生成职位，入学年份等信息。

学工号绑定后无法更改。

1. 学工号第一位为1则为学生。
   1. 学工号第二三位为入学年份。
   2. 学工号第四位为1则为本科生，4，5，6则为研究生。
2. 学工号第一位为3则为教职工，无需入学年份。
3. 其余情况均为无效学工号。

#### 支付宝帐号绑定

支付宝账号可随时修改，需要短信校验。

#### 通知系统

使用短信进行通知，用户可以选择是否开启以下功能中的短信通知：

1. 二手交易
2. 跑腿代购
3. API交易



### 商品逻辑

已售出商品不可下架（即删除）。

若商品下架，关联订单全部删除。

#### 商品图片上传

商品图片上传后会进行缩放，高度限定为1080（加水印？）。



### 订单逻辑（二手交易）

订单不可主动删除。

#### 订单发起及状态

点击购买后，需要选择交易地点及时间。

1. 交易地点需在南方科技大学及附近（使用半径确定）。
2. 交易时间必须晚于当前时间一小时。

选择后买家点击发送订单，订单将被创建并发给卖家，该订单进入**待确认状态**。

##### 状态1：待确认

卖家可以在确认阶段关闭交易或更改信息。

1. 若关闭交易，订单直接进入**已关闭状态**。
2. 若确认交易，卖家可以修改订单价格，之后该订单发回给买家，订单进入**待付款状态**。

##### 状态2：待付款

卖家收到付款订单后可以选择关闭交易或付款。

1. 若关闭交易，订单直接进入**已关闭状态**。

2. 若付款，则订单进入**交易中状态**，商品款暂时冻结，商品显示已售出，其他所有订单进入**已关闭状态**。

   **此时会生成一个六位成交码给买家，生成一个六位退款码给卖家，同时还会生成一个双方都知道的四位交易密钥。**
   
   以上信息会通过短信形式发送至双方手机中。

##### 状态3：交易中

双方进行线下交易，开始前卖家应向买家先核对交易密钥，确认买家身份。交易期间应协商一致，最终结果为卖家获得成交码，或买家获得退款码。

1. 若卖家输入成交码，则订单进入**交易成功状态**。
2. 若买家输入退款码，则订单进入**已退款状态**。
3. 若二者均未在设定交易时间后的14天内填写对方的码信息，则钱款返回买家，订单进入**已关闭状态**。

##### 状态4：交易成功

钱款打入卖家账户，卖家已售出总金额增加。

##### 状态5：已退款

钱款返回买家。

##### 状态6：已关闭

#### 码管理

若用户输入错误码次数过多，将逐级递增地扣除信用分。



### 后台管理*

#### 后台权限管理

后台管理员权限分为3个等级：

1. Checker，只可查看后台数据。
2. Modifier，可以编辑，增删后台数据。
3. Super Admin，可以编辑后台管理员权限。

#### 商品管理

可以根据商品标题或描述进行模糊搜索，结果需分页。

可以下架商品，可以多选商品批量下架。

#### 前台用户管理

可以根据用户名进行模糊搜索，结果需分页。

可以更改用户的名字及个人描述。

可以封禁用户，可以多选用户批量封禁。

#### 订单管理

可以强制关闭处于**待确认**或**待付款**状态的订单。



### 前后端均需验证

价格必须大于0。

经纬度需要在南科大一定范围内。

评价分不可超过5分。



### 逻辑删除

用户表、商品表和订单表设备份表实现逻辑删除，其余表直接物理删除，不设备份。



### 需要发送短信的操作

#### 验证码

1. 手机号登录
2. 修改密码
3. 修改绑定支付宝账号
4. 注销账号

#### 交易码

#### 通知


### 待讨论内容
- 商品可以分为哪些类别
- 如何设计一个迅速轻量的交易过程，用于互助业务
  - 默认信任发出帮助请求者
- 前端页面分工
  - 主业务（工作量最大，页面多）
    - 蓝晨溪 个人中心 + 登录
    - 王辰宇 主页 + 商品页 
  - 互助（实现主业务后工作量不大）
    - 暂定，完成basic之后分配
  - 后台（有现成脚手架，可以快速上手）
    - 王志灏
  - API交易（不一定做）
    - 暂定
