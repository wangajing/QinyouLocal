

秦游

软件设计说明书



作者：王阿晶  孙杰 梁春晓 李绍坤  申宝明




学校：中国石油大学（华东）
指导老师：李勇
   

时间：2008年7月5日









摘要
本文档主要对要制作的手机游戏做了总体的设计，包括创意、场景、内容。也对软件系统的架构做了设计，包括软件的逻辑设计和代码设计。
第一章总述对整个游戏做了概括的说明，提出了最基本的要求。第二章场景设计描述了游戏的创意和场景，包括许多游戏的细节，展示了游戏的蓝图。第三章是本文档的核心，对软件的系统设计做了详细的描述，包括每一关的系统架构，每一关的核心算法等等，即是描述软件如何实现。
第四章对游戏中用到了主要技术进行了介绍，如J2ME的概述，J2ME游戏开发包，J2ME 3D技术，蓝牙技术等。第五章是项目规划，提出了项目的分工和时间安排，对整个软件生命周期做了细致的安排，使软件实施时有照可依。
整个游戏关数多，而且每一关的设计思想迥异，可以说每一关都是一款完整的游戏，有的是2D的，有的是3D的，所以整个游戏实现的难度很大。但是考虑到组里5个人都是编程能手，又都勤奋好学，充分的发挥了每个成员的能力，凭着我们旺盛的创造激情和充分的团队合作，完成并高质量地完成了游戏，完全没有问题。




关键词：多视角多关卡，历史背景，3D,蓝牙。




目录                                                                

第一章 总述••••••••••••••••••••••••••••••••••••••••••••••••••••••••4
第二章 游戏场景设计••••••••••••••••••••••••••••••••••••••••••••••••5
    	2.1主菜单设计
		2.2 第一关设计
		2.3 第二关设计   
		2.4 第三关设计   
		 
第三章 软件逻辑及代码设计•••••••••••••••••••••••••••••••••••••••••11
  		3.1主菜单设计
		3.2 第一关设计
		3.3 第二关设计   
		3.4 第三关设计   
		 
第四主要技术概述•••••••••••••••••••••••••••••••••••••••••••••••••••36
		4.1 J2ME技术简介
		4.2 J2ME声音播放技术
		4.3 Moblie 3D技术
		4.4 蓝牙技术
第五章 项目规划••••••••••••••••••••••••••••••••••••••••••••••••••••39
		5.1项目分工
		5.2 进度计划
		









第一章	  总述

1.1设计目标：
总体目标是以秦皇地宫为主要场景设计背景，结合主要的历史人物及其著名事件为主要历史背景设计，制作一款多视角，多场景的经典的迷宫类、射击类、竞赛类相融合的手机游戏。用户在游戏中闯关，完成任务后得到一定分数和宝物并进入下一关。要求每一关的视角和场景、内容完全不一样，给用户以全新的感受。并且每一关的内容可以切合历史背景主线，让用户在游戏中体味历史。难度适中，可玩性高，趣味性强，但操作要求尽可能的简单,容易上手。画面要求绚丽而舒服，色调尽量一致，场景要合理，能与历史相对应。
另外可以单人游戏，也支持通过蓝牙的多人游戏。
1.2 运行平台：
		编译环境：JDK1.4.2   WTK2.0   NETBEANS5.0 另加SonyErricssion开发包
		真机测试环境：SonyErricssion Z550C
1.3涉及的技术：   
1. J2ME CLDC2.0游戏包中的Sprite,GameCanvas技术
2. J2ME Mobile 3D技术(JSR184)
3. J2ME Blutooth技术(JSR82)
4. Photoshop平面处理技术
5. 3DMAX 3D处理技术













第二章 游戏场景设计
2.1.主菜单设计
             主菜单具有的选项有：单人游戏/双人游戏  新游戏/继续游戏/高分榜/帮助/关于。用具有金属质感的按钮来显示，选中后按钮发光表示显示。
 
	
继续游戏可以继续上一次玩的关数，新游戏是重新从第一关开始。高分榜可显示分数前五名的玩家的姓名和分数。关于显示软件作者。

2.2.第一关场景设计
 	名称：八卦迷阵
 	背景：在秦梦与将军对话之后，知道要搜集到3块和氏璧，方能借助和氏璧力量回到现代。于是她急不可耐的去寻找和氏璧，在奔跑的时候，突然掉入了一个陷阱中，然后昏迷了。醒来之后，秦梦发现自己身在一个变化莫测的迷宫中。就在这时一个灵魂向她走来。在一阵寒暄后，秦梦知道那是李斯的灵魂，在李斯被腰斩之后其灵魂被困于这个迷宫，而秦梦要离开这里，就必须帮助李斯搜集到迷宫李斯的八个遗物。游戏就开始了。
 	视角：3D第一视角
 	场景：一个由砖墙组成的迷宫，月光照亮了整个迷宫，环境幽暗。迷宫中有巨大的魔石在迷宫中自由移动，随时可能使秦梦粉身碎骨，还有许多特殊效果的道具在迷宫中。秦梦在迷宫中行走，可以碰撞“半两”铜钱，获得积分，可以碰撞问号得到附属效果（加速、减速或逆向），可以碰撞地图显示的道具获得迷宫的地图；撞击箱子，获得寻找的遗物。
 	规则：玩家共有3个生命。玩家要在500秒内获得八种遗物，这八种遗物分别放在了正八边形的顶点上，八件遗物分别是头盔，书简，佩剑，弓弩，铠甲，配饰，军靴，长矛；然后在60秒内返回到出发点点，即可通过游戏。在游戏中如果与巨石发生碰撞，玩家的生命减一，并从出发点重新开始。如果是在寻找8种道具完毕返回出发点的阶段，一旦与巨石碰撞，游戏将失败。
 	按键说明：
在游戏中，一般只用到下面的按键。它们对应的效果如下：
按键	效果
UP或数字键2	向前移动
DOWN或数字键8	向后移动
LEFT或数字键4	向左旋转
RIGHT或数字键6	向右旋转
   此外，在游戏过程中，按右功能键显示暂停菜单（不适用于多人游戏模式下）。在暂停菜单中，UP(或数字键2)和DOWN（或数字键8）下面调整菜单，fire键（或数字键5）确认选择的菜单项。
 	游戏道具：
游戏中的道具分为如下类型：
道具类型	道具图标	道具效果	说明
大地图		在屏幕上显示迷宫的整个地图，但不包括玩家所在的位置	通过碰撞道具获得，显示8秒
小地图		在屏幕上显示玩家所在的位置周围的地图，并显示玩家的位置	通过碰撞道具获得，显示8秒
金币		获得加分100	通过碰撞金币获得加分
加速道具		玩家移动加速	通过碰撞问号道具获得
减速道具		玩家移动减速	通过碰撞问号道具获得
逆转道具		玩家反方向移动	通过碰撞问号道具获得
箱子道具		获得遗物一个	通过碰撞箱子道具获得，遗物可能是头盔，书简，佩剑，弓弩，铠甲，配饰，军靴，长矛。
游戏中的Boss：
在游戏中有4个巨石在迷宫中任意行走。在寻找8件遗物时，一旦与他们碰撞就会送至出发点；在获得到8件遗物后，一旦与他们碰撞就会失败，而不再减少生命值。
其他：
在游戏中还有其他的一些辅助信息：
1）	罗盘：罗盘用于显示玩家所面向的方向，罗盘上显示8个黑点，分别表示8件遗物所在的方位，如果其中获得一件遗物，罗盘上对应位置的点变为蓝点。移动的蓝点表示玩家在整个迷宫中的位置。当8个道具完全获得后，罗盘上将连成正8边形，类似八卦阵的形状。
2）	生命值、分数、时间：在屏幕上还显示了玩家当前的生命值、分数和剩余的游戏时间。当剩余游戏时间小于30秒时间显示变为红色，用于提示玩家尽快完成任务。
3）	玩家状态：当玩家获得大地图、小地图、加速、减速、逆向道具后，应显示相应的状态。
4）	事件提醒：当玩家获得道具、获得遗物等事件发生时，应显示提醒文字。
 多人游戏：
	在多人游戏中，允许两人共同完成游戏。
1）	规则：两个人在500秒的时间内共同去寻找八件遗物，在八件遗物寻找到后，任何一个人在规定时间内返回出发点就算胜利。
2）	注意：
1、	游戏开始前，服务器端先建立服务，服务端玩家先进入迷宫中，等待客户端的加入。
2、	在服务器端建立服务完毕后，客户端开启游戏，加入到游戏中，服务器端和客户端将同时初始化。
3、	任何一个玩家胜利，游戏就胜利了。
4、	在该模式下，积分=获得遗物个数*200+获得金币个数*100。
5、	当其中一方失败时，该方会切换到队友的视角镜头内。
6、	双方失败，游戏将结束。
7、	在二人游戏中，玩家不可以暂停，只有当胜利或失败后，玩家方能离开游戏。

2.3第二关场景设计
名称：天赐羽翼
背景：游客们身陷陵墓机关，主人公为救大家勇闯皇陵，突破迷宫后得到李斯赠送的神之羽翼，在陵墓中又遇到了阵列蝙蝠、无相陨石、石头巨人、还有梦幻天使的种种考验，在勇气与信心的鼓励下，最终闯出阻挠并得到了梦幻天使赠予的一部和氏璧碎片，在正义与信念的驱使下勇敢的面对接下来的考验。
视角：第三人称 俯视视角
场景：
主人公生命初始值为100。
本关共有四个场景：
第一场景是阵列蝙蝠，士兵需要躲避蝙蝠毒标的攻击以及蝙蝠的碰撞，消灭蝙蝠可以加分，主人公发射的能量球每击中一只蝙蝠可以获得10分，但被蝙蝠撞击生命值就会减5，在成功闯过四波阵列蝙蝠之后，进入第二场景；
第二场景是无相陨石，士兵只能躲避来自星空中不定陨石雨的撞击，主人公每被一块陨石击中生命值就会减5，在躲过无相陨石雨之后，进入第三场景；
第三场景是石头巨人，三只小石头人周身环绕能量波，碰撞攻击主人公，主人公只能躲避三只小石头人的碰撞，当主人公与石头人发生碰撞时，石头人会消失，主人公会死亡。接下来会出现一个大石头巨人，并且发射各种能量球，主人公必须躲避能量球的攻击并不保持与石头人碰撞，大石头巨人共有50条生命值，当主人公的能量球击中石头巨人时，其生命值会减1，直至石头巨人死亡，进入第四场景；
第四场景是梦幻天使，梦幻天使会释放串圆、基因螺旋、旋转、散型、心型、星型、辐射波环的能量攻击，主人公需要躲避并不与梦幻天使发生碰撞，主人公可以使用能量球攻击梦幻天使，其生命值为200，每被击中一次生命值减1，当其生命值减为0时，主人公闯关成功；
角色操作：上（数字键2）：上移；
          下（数字键8）：下移；
          左（数字键4）：左移；
          右（数字键6）：右移；
          OK键：开火；
          7键：防护能量弹。
为了便于主人公在危机时刻启动保护，在主人公进入第三、四场景的时候，会获得三枚防护能量弹，按7号键启动可以消灭屏幕内的所以攻击。若主人公的生命值减为0并且没有消灭最终梦幻天使时，闯关失败。

2.4第三关场景设计
名称 神勇骑术
背景：主人翁秦梦拿到了前两块破碎的和氏璧后得知最后一块和氏璧在苏秦的手里，便前往借取和氏璧，不料苏秦突然遭刺客袭击，和氏璧落入刺客手中，奄奄一息的苏秦请求秦梦去夺回和氏璧，同时为了解救困在秦代的游客，秦梦毫不犹豫地骑上马车利用不太娴熟的骑术追赶刺客。不过秦梦很快便掌握了骑术，不断拉近与刺客的距离，当距离足够近时，秦梦利用随身携带的5枚飞刀击杀敌人。技术高超的秦梦一击便打中刺客的头部取回了和氏璧
视角：第一人称视角
场景：玩家的任务是追赶正在逃跑的刺客，一路要躲避多种有害的障碍物，当然也有一些帮助的障碍物。最终发射飞刀将刺客击毙。
场景氛围两个部分，前半程为幽静的树林，石板小道上布满了各种障碍，障碍物采用古典的中国元素，障碍分为
道具
	外形	效果
1	金币	五行元素的金 得到可获得积分
2
	树怪	强大树怪来自五行之木，一旦碰到树怪，便会紧紧的抓住你，暂停一秒钟。
3	火焰	五行元素中的火，碰撞到火焰受到惊吓的马便会狂奔，加速3秒
4	水行	五行元素中水，碰到该道具受到水的润泽，便可以迅速的回复生命值，生命值加10
5	兵马俑	五行元素中的土，土行元素拥有强大的法术，一旦碰撞便会将你送回游戏的起点，并且丢失一条生命机会
6 	左树枝	道路上倒下的树木，一旦被树枝划到便会减少生命值30，但在左边由一个小的缺口可以通过
7	右树枝	道路上倒下的树木，一旦被树枝划到便会减少生命值30，但在右边由一个小的缺口可以通过
后半程的场景是在峡谷中，前方滚动的瀑布，营造出美妙的傍晚的景色，前方的敌人离得更近了，此时可以发射飞刀击杀刺客了，一共有五枚飞刀，在敌人消失在视野之前必须要用这五枚飞刀杀死刺客。
角色操作：
前半程：
Left 向左移动，
Right 向右移动
Up  跳跃，
Down 无操作
后半程：
Left 准心左移
 Right 准心右移
 Up准心上移
 Down准心下移
Fire 发射飞刀

第三章 软件逻辑及代码设计
3.1主菜单的设计

3.1.1软件逻辑设计
主要模块：主菜单类，积分排行类和游戏设置类；
主菜单类中包含有游戏帮助和关于我们；
模块功能： 
1.	主菜单类负责所有子菜单的管理和显示，控制游戏进度，更新、加载积分排行。
2.	分榜类负责从进度和RMS存储中提取所有高分信息并显示出来。
3  戏设置类继承了Form类，使用文本框让用户输入用户姓名，用单选按钮选择是否需要音乐，如果选择开启音乐可以调节音量，如果关闭音量则屏蔽掉音量。
4   游戏帮助和关于我们是嵌套在主菜单类中的。



用单












3.1.2. 软件详细设计

1.	主菜单的显示算法： 设计了俩个索引值，MenuIndex表示当前显示的是第几个菜单，而MenuItemIndex表示的是当前菜单的所指的位置；菜单图片的导入使用Image.createImage()函数; 在适当的位置画出使用g.drawImage()函数; 其中帮助和关于均由g.drawString()函数实现，未使用Form管理类，在指向菜单项的过程中，我们采用滑动的效果，即首先置MenuItemIndex一个初值，当它在移动的时候不断的加一个数值较小的数字，这样会有缓慢滑动的效果；

2.	积分排行榜的算法：游戏结束后获得分数，将该分数与记录集中的分数相对比。先判断是否可以进高分榜：若记录集中分数条目少与5条，则可写入高分；否则，与记录集中的最后一条比较，若比它大，替换，然后与倒数第二条比较，若比它大，也替换，依次类推。这样分数就放到了合适的位置上了。

3	RMS记录集的实现，记录管理系统（RMS）是移动信息设备简表（MIDP）中非常关键的
子系统，RMS通过一系列应用程序编程接口(API)为MIDP应用程序提供了本地数据持久性存储的机制，记录管理系统是一个较为简单的面向记录的数据库；在RMS中存储的数据是以记录（Record）为单元的，记录不能单独存在，必须的属于某个RecordStore，记录管理系统没有对数据的形势做限制，记录可以是字符，数字，数组或者是图片，只要数据可以被转换成字节数组就可以，应用程序负责对数据编码，以及将其转换为字节数组并写入到RMS系统中，从RMS中读出的数据依然是字节数组，应用程序需要对数据进行解码并转换为原始的数据，RMS的实现是线程安全的，RecordStore确保所有单个的记录存储操作都是原子，同步和串行化的，这样可以在多次访问后依然保持数据的完整性，然而，如果在一个MIDLET中使用多个线程来访问同一个RecordStore,应用程序应该负责协调多线程的访问，否则会出现意料不到的结果，比如破坏记录中的结果。记录管理系统的API定义在javax.microedition.rms包内，包括一个类，4个接口和5个异常。
创建RecordStore对象的信息代码：database = RecordStore.openRecordStore(filename,true).读取RecordStore对象的信息首先要打开
记录，然后获取这个记录所对应的recordID。通过调用RecordStore类的getRecord()方法可以把记录读取出来。RecordStore提供了俩个重载的getRecord()方法。Public byte[] 
getRecord(int recorded).这个方法从RecordStore对象中读取参数recordID标示的记录数据，返回值为byte[]。如果记录中没有数据，则返回null;public int getRecord(int recorded,byte[] buffer,int offset).这个方法从RecordStore对象中读取参数recordId标示的记录数据。放入字节数组buffer中，开始复制数据的偏移量为offset，通常offset等于0，方法的返回值是所读取数据的字节长度。调用RecordStore类的setRecord()方法可以更新记录的内容，方法为public void setRecord(int recorded, byte[] data,int offset,int numBytes),也可以采用另一种替代的方法来更新记录的数据，首先调用deleteRecord()方法把记录删除，然后使用addRecord()方法把数据添加到RecordStore对象中，删除记录非常简单，把想要删除的记录的recordId作为参数传递给deleteRecord(int recorded)即可，一旦记录被删除，那么记录所对应的recordId将不再使用。
要实现自定义数据与byte数组相互转换，需要ByteArrayOutputStream, DataOutputStream, ByteArrayInputStream, DataInputStream这四个类的帮助。要写入数据首先要建立一个ByteArrayOutputStream实例baos，然后将它作为参数传入DataOutputStream的构造来产生一个实例dos。Dos有一组方方便的I/O方法：Write(),
例如WriteInt()用于写入int型，WriteChar（）用于写入字符型，当写入操作完成后，可以利用baos的toByteArray方法的得到一个byte[]数组，这个数组含有刚刚写入的数据，将它传给addRecord就可以增加一条Record,最后关闭打开的流；要读入数据就利用ByteArrayInputStream和DataInputStream这俩个类。首先利用getRecord(int)得到刚刚写入的byte数组，然后利用得到的byte数组构造一个ByteArrayInputStream的实例bais，再用DataInputStream包装它，得到一个实例dis. DataInputStream有一组的方便的I/O方法用于读入DataOutputStream对应方法写入的数据，读入的顺序和写入的顺序是一致的，同样，不再使用流时候，关闭流以节约资源。

4	游戏设置类的实现： 在游戏设置类中，继承了Form管理类，有Gauge类，ChoiceGroup类和TextField类，Gauge类实现音量，Gauge组件用于显示一个进度条。有俩种类型的Gauge，交互式和非交互式Gauge。前者允许用户交互式的Gauge作出调整，而后者只能通过编码来更新Gauge显示进度。对定义一个Gauge类的对象，使用append()函数载入，便可显示音量的画面，利用手机的左右键可以调制音量的大小，当选定音量后，按左软键便可，这时其音量被保存进了RMS记录集中，TextField组建用来接受文本的输入，组件可以制定标签，最大字符数及所接受的数据类型。TextField组件可以实现在输入密码等敏感信息时的屏蔽功能。TextField(String label,String text,int maxSize,int constraints),其中，第一个参数是文本框的名称，第二个参数是文本框内的文字内容的默认值，第三个参数是输入文本的最大限度的长度，最后一个参数constraints指定在TextField中允许的输入类型。然后调用Form对象的append()方法将它添加到Form中。ChoiceGroup组件使用户可以从定义的一组条目中选择。ChoiceGroup形式：多选(multi-seletion),等同于复选框；单选(exclusive-selection),等同于单选按钮组。ChoiceGroup(String label,int vchoiceType)；第一个参数是标签，第二个是选择列表的类型,然后调用Form对象的append()方法将它添加到Form中。

3.1.3动画的实现

 
动画的制作继承了j2me中的低级界面GameCanvas类，GameCanvas是Canvas的子类。它代表了游戏的基本界面。在GameCanvas上进行绘画代替了Canvas绘画。GameCanvas的主要改进在于它自动实现了双缓冲，并提供了轮询键盘输入事件的方法。利用gettHeight(),getWidh()来取得当前的界面大小。通过getGraphics()方法取的Graphics对象,调用flushGraphics()方法，该方法将界面缓冲区一次性的绘画到显示界面上，在GameCanvas中使用getKeyStates()轮询键盘，getKeyStates()方法会检查键盘状态并返回一个整形的标记，该标记中的每一位都代表了一个按键的状态，若该位为1则说明按键被按下，相反，若为0则说明按键抬起，所以只要测试该位，就可以的知键盘的状态了
在动画CartoonCanvas类中，设定了pictureIndex值，该值表示当前播放的是第几个动画，动画图片载入是由Image.createImage()函数和g.drawImage()函数来实现，在GameCanvas中调用paint方法进行绘制。在动画播放中，会有文字不断的循环显示，可以按fire键跳过，在第二个动画显示时是俩个人物的简短对话，第三个动画是历史背景和任务的交代
，图片和文字不停的循环播放。

3.2 第一关游戏：八卦迷阵 
3.2.1软件逻辑设计
根据上面的场景设计和规则，可以将游戏分为如下的几个模块：
1、迷宫生成的算法设计
2、3D场景的搭建和绘制
3、玩家与墙、道具的碰撞检测和移动实现
4、boss的AI移动处理
5、状态栏和信息栏显示
6、音乐部分
7、蓝牙互联部分
考虑到面向对象的效率较面向过程的效率低一些，在兼顾设计思路清晰和代码重用的情况下，为了提高游戏的执行效率，采用了面向过程的思想和实现方式。结合上面的模块分析，游戏主模块运行的流程图如下：















1.	游戏分为如下的模块：
2.	迷宫生成模块：借助PRIM迷宫生成算法生成用数组表示的迷宫。
3.	3D模型生成模块：该模块负责读取文件或者创建3D迷宫绘制中需要的3D模型，包括地面、墙、游戏中的3D道具等；
4.	2D和3D场景绘制模块：根据生成的迷宫和3D模型来绘制3D模型，同时根据游戏当前的状态绘制2D图形来显示游戏提示或状态；绘制的2D图形包括：玩家状态的提示、玩家分数和生命，指南针，地图，玩家获得某种道具的提示等；
5.	玩家的碰撞检测模块：检测玩家与墙和道具的碰撞检测，与墙碰撞后，玩家就不能移动；与道具发生碰撞，需要根据碰撞的道具不同，产生不同的效果
6.	敌人的AI移动处理：处理魔石在迷宫中的移动。
7.	按键处理模块：检测玩家的按键情况并按键处理
8.	音乐处理模块：进行音乐播放和音效的播放
9.	蓝牙互联模块：建立连接并向对方写数据或读取数据
10.	游戏主模块：调用以上模块，协调各个模块
各模块之间的关系如下：

3.2.2软件详细设计
迷宫生成算法：
这里生成的迷宫算法采用了Prim迷宫生成算法，这种算法生成的迷宫的特点是：
1、	在迷宫中从任意一点到另一点的路径是唯一的；
2、	迷宫生成的效率和质量都很高；
Prim算法的数据结构和算法如下：
1、	初始化数组int maze[12][12]，每个元素maze[i][j]表示一个单元格，如果maze[i][j]能够与1相与不等于0，则该单元格有上面的墙；与2相与不等于0，则该单元格有下面的墙；与4相与不等于0，则该单元格有左边的墙；与8相与不等于0，则该单元格有右边的墙。初始化时，每个单元格都有上下左右的墙，即maze[i][j]= 63。
2、	随即获得一个单元格，将他标记为迷宫的一部分。这个单元格的墙添加到墙列表int todo[]中。
3、	当todo为空时，算法结束，否则执行下面的语句：
（1）	从列表中随即获得一个墙，如果这个墙对应的另一个单元格不在迷宫中，那么：
（1）	打开这个墙，将这个单元格标记为迷宫的一部分；
（2）	添加这个墙的邻居到todo中，转到3继续；
下面是用Prim算法生成20*20的迷宫：

















迷宫的生成算法有多种，包括深度优先迷宫生成算法、Prim迷宫生成算法、随即的 Kruskal迷宫生成算法等。这里选择Prim迷宫生成算法，主要考虑到Prim算法的易于实现性、高效性。
在Maze类中封装了迷宫的生成算法。这个类中有表示迷宫的数组字段和表示迷宫大小的MAZE_WIDTH和MAZE_HEIGHT静态常量，方便程序的其他模块获得生成迷宫的信息。类中的init方法封装Prim算法。在构造方法中调用了该方法，完成对mazeArray的初始化。
要获得表示迷宫的数组，可以：
Maze maze = new Maze();
int[][] mazeArray = maze.getMaze();
二、3D模型生成模块
在该模块中，主要负责从m3g文件中读取3D渲染中需要的3D模型。包括地面平面、砖墙、3D道具等。在m3g文件中，各种模型的存放是按照树形结构存放的。其中树根是World，要读取模型，先要读取世界，然后在世界中根据模型的ID来读取模型。
另外，在获得3D模型中，还采用了立即模式，用程序代码来创建平面。创建平面的类是MeshFactory。他定义了四个顶点，顶点中保存了顶点的位置、法向量、纹理坐标等信息。然后定义外观，并设置贴图图片即可完成Mesh平面的创建。
Model3DFactory中实现上述功能，在这个类中分别定义地面、砖墙、中心地面、金币、问号、巨石、大地图道具、小地图道具等模型。
三、游戏主模块
在Maze3Dcanvas中，实现了游戏主模块。
1、	数据结构：
主要包括如下的数据：
1.	玩家相关数据：
玩家的位置、生命值、分数、朝向的角度；
2.	道具相关数据：
道具所在位置，包括硬币所在位置、八件遗物的位置、地图提示和随机道具的位置，分别用两个数组表示位置x和z，道具旋转角度。
3.	敌人移动的相关数据：
敌人的位置，敌人的运动方向和敌人旋转角度
4.	3D绘制的相关数据：
为简单起见，我们将3D场景绘制需要的数据也放入了这个模块中，数据包括光照、照相机、背景、绘制需要的模型、3D画笔等。
5.	与整体游戏有关的数据：
包括当前的游戏状态（正常、暂停、成功、失败4个状态）、游戏进行的时间倒计时、当前游戏的阶段（寻找遗物阶段或者是返回原点阶段）等变量
2、	算法难点：
在游戏的主模块中的主要难点是游戏状态的转换和各个模块根据不同状态的组织协调，下面是游戏状态的转化图：

3、	按键处理模块：
本游戏中基本的按键只有UP（2）、DOWN（8）、LEFT（4）、RIGHT（6）。每种按键的响应情况和处理情况如下如下：
按键	效果	按键处理
UP或数字键2	向前移动	根据玩家视角角度、玩家当前的状态（加速、减速）计算出移动在x和z轴上的分量，然后在玩家位置的x和z分量分别加上上面两个值；
DOWN或数字键8	向后移动	根据玩家视角角度、玩家当前的状态（加速、减速）计算出移动在x和z轴上的分量，然后在玩家位置的x和z分量分别减去上面两个值；
LEFT或数字键4	向左旋转	玩家视角加上18°；
RIGHT或数字键6	向右旋转	玩家视角减去18°；
在游戏中，还涉及到碰撞处理的问题，如果在朝向某个方向与墙壁发生碰撞时，玩家将在不能再朝向这个方向移动。下面以按UP键移动为例，来解决上述问题：
	当玩家按UP键后，程序相应的移动玩家的位置，然后检测当前位置是否在与墙壁的碰撞范围内，如果不在，返回，如果在，需要恢复到原来的位置。
	程序代码如下：   
 posX -= dx;
         posZ -= dz;
        if (isCollideWithWall()) {
            posX += dx;
            posZ += dz;
         }
假设当玩家位置x在（300，306）之间表示与墙发生了碰撞，那么玩家的初始位置为300，而玩家在加速时一次移动为20，那么如果用上面的方法，x加上20后，下面的isCollideWithWall()将无法检测到碰撞。所以，为了提高移动的准确度和范围，采用分步移动的方法，具体的算法如下：
假设总共移动20，采用分4次的方法移动，即每次移动5，如果移动可以移动5，那么再试图移动到10，如果可以移动到10，再试图移动到15。如果不能继续移动返回即可。流程图如下：
4、3D场景的绘制
	与一般的游戏不同，因为本游戏中的场景地图是随机生成的，所以在绘制3D场景中，采用了立即模式的绘制，绘制的内容和算法如下：
1、	绘制3D迷宫地图
从Model3DFactory中获得地面和墙，然后对其进行平移和旋转（注意：先平移后旋转），根据迷宫数据放在适当的位置即可。例如，mazeMap [m][n]&2!=0，即单元格[i][j]的下面有墙，绘制的算法如下：
		  if ((mazeMap[m][n] & 2) != 0) /* 这个单元格有下面的墙*/ {
              t.setIdentity();//将旋转矩阵置为单位矩阵
             t.postTranslate(m * 100 + 50, 0, n * 100 + 100);//平移到该位置（m*100,n*100+100）
					//该平面不需要旋转
             g3d.render(wall, t);//绘制
                }
		在绘制整个3D迷宫时，有3种方法：
1、	完全绘制：即将整个迷宫都绘制出来，即
     for (m = maxI; m < Maze.MAZE_WIDTH && m - maxI <= i; m++) {
         for (n = maxJ; n < Maze.MAZE_HEIGHT && n - maxJ <= j; n++) {
		drawWall();
		drawPlane();
}
}	
	这种绘制方法简单，绘制出来的3D场景质量高。但循环次数太多，而且绘制了一些不需要绘制的场景，效率较低。
2、	部分绘制：即只绘制玩家位置四周的场景，根据玩家位置，计算出以玩家为中心的小迷宫，然后绘制这个小迷宫即可。
如下图所示，以玩家所在位置O为中心，计算绘制红色区域的迷宫即可。

这种算法提高了绘制的效率，减少了不必要的绘制，但还不是很理想，玩家朝向一个方向，而与之相反的方向的绘制将是无效的。
3、	按方向绘制：分析上面算法的缺点后，可以根据玩家的方向来决定绘制迷宫的范围：
玩家朝向在（-45°，45°）：如左图，绘制图中的红色区域




玩家朝向在（-135°，-45°）：如左图，绘制图中的红色区域


玩家朝向在大于135°或小于-135°：如左图，绘制图中的红色区域




玩家朝向在(45,°135°)：如左图，绘制图中的红色区域





2、	道具的绘制：
道具的绘制较为简单，对于每一个道具的进行绘制即可。不过考虑到视野的问题和迷宫的局部绘制问题，对于每个道具也需要判断是不是在绘制范围内来进行绘制
5、玩家的碰撞检测：
	在3D游戏中，一种碰撞检测的方法是利用射线法，即从某点朝某个方向发射一条射线，在与某物体相交后，返回这个点到相交点的距离，如果距离小于某个值，就表示发生碰撞。
	考虑到本游戏的特殊性，这里的碰转检测没有采用射线法，而是采用简单的位置检测方法。
1、	与墙碰撞的检测：
假设玩家当前位置在(x,y)，如下图。对于点（x,y），首先判断这个点周围是否有墙，如果有左墙，检测它与左墙的距离，如果在一定的范围内，那么就发生了碰撞。同样的方法对上面的墙、下面的墙和右边的墙采用相同的方式。
为了提高检测的效率，对于位置A，显然不可能与墙发生碰撞，对于红色方框内的位置，显示是不可能与墙发生碰撞的。再有在红色方框和黑色色方框之间的位置才有可能发生碰撞。所以在进行与墙碰撞检测之前，可以先检测位置是否在红色方框内，如果在可以直接返回没有碰撞。
2、	与道具碰撞的检测：
每个道具的位置都是可知的，可以之间判断玩家所在的位置与道具所在位置是否重合，如果重合，即发生碰撞。
6、敌人的AI移动模块：
	在游戏中，又四个巨石在迷宫中自由移动。为了实现巨石的自由移动，我们采用了如下实现了如下两种算法：
1、	随机移动算法：
如上图，在巨石到达位置A后，找到四周可以走通的方向，并随机从中挑选一个（假设随机到方向下）。然后进入B位置后，在随即生成一个方向，如此下去即可。
这种算法简单，但巨石很有可能会在A、B两个位置来回移动。所以该算法并不是很智能。效果比较差。
2、	记录路径算法：
要提高敌人的AI，可以用堆栈记录巨石经过的位置和该位置走过的方向。
1、数据结构：
下面的类PointInfo保存了巨石的所经过点的信息，包括点的位置，方向信息（即某个方向走没走过，在字节型directionInfo中的低四位表示了方向信息，例如第一位为0，表示走过，为1表示没有走过或不能走），在initDirection中保存了初始方向，在下图中，如B的初始方向是左，因为A在B的左侧，从A到B后，B的初始化方向就是左。
Class PointInfo{
 int x;//点所在的位置x
 int y; //点所在的位置y
 byte directionInfo;//方向信息
 byte initDirecton;//初始化方向，即该点从从哪个方向来的
};
Vector stack；
2、算法过程：
如图，假设起始点为A，从A中找出一个可以走的方向右同时将A加入堆栈stack中，同时标记A的右边已经走过。然后走到B，将B加入堆栈中，并设置B的初始化方向为左，B的左侧不能走；再从B中可以走的方向中选择一个方向，假设选择是下，标记B的下不能走，然后走到C，将C的初始化置为上，C的上侧不能走；发现C中的四周都不能走，选择初始化方向移动巨石，并 从堆栈中删除C，如此，返回到B。在B的位置信息中，左侧和下侧都不能再走，走到D。按照如此的算法继续。直到返回到A点，即A点的前后都走过。那么将A从堆栈删除后，再将A添加到堆栈后，算法将继续执行。
	这种算法的优点在于能够让巨石更加规则在迷宫中运动。缺点是需要保存巨石走过的信息。

7、音乐处理模块
本游戏的音乐分为两种，一种是简单的背景音乐，在启动播放后，要不停的循环播放，直到游戏结束；另一种是音效，即在与道具碰撞时发出的声音。实现音乐播放，我们使用MusicControl类来对音乐进行管理。在需要播放指定音乐时，只需：
MusicControl mc = new MusicControl（true）；//true表示音乐是循环播放的
mc.changeMusic(“1.mid”);//指定播放音乐
mc.musicStart();//播放音乐
在MusicControl中，封装了音乐播放的细节。而音乐播放本身也比较简单。只需调用Player类即可实现音乐播放。
8、蓝牙互联模块
	蓝牙互联模块主要的功能是：
	对服务端：创建地图，将地图信息发送给客户端，传送自己的信息给客户端，并接收客户端的信息。
	对客户端：传送自己的信息给服务端，并接收服务端的信息。
1、	数据结构：
要实现通信，必须首先定义通信的数据结构：
1、	建立游戏连接：
在建立游戏连接，客户端首先向服务端发送字符M，服务器读取字符如果是M，将地图数组发送给客户端。
2、	游戏开始：
在游戏开始后，要传递的信息数据结构是：
位置X（4）	位置Z（4）	生命值（1）	道具ID（1）
		如果游戏自己失败了，只需传递一个字节0，表示这方失败。
		在另一方受到失败后，该方将发送如下的字节值：
位置X（4）	位置Z（4）	生命值（1）	道具ID（1）	角度（4）
如果任意一方A胜利，那么整个游戏胜利，在一方A发送完数据后，另一方B根据数据也能判断游戏胜利。所以只要有一方胜利，在传递完信息后，就停止传送信息。
如果双方都失败，也将停止传送。
2、	算法设计：
在通信中，我们要客户端和服务器在一定时间内定时传送自己的信息。例如200毫秒传递一次。双方根据传递的信息，显示对方的状态。在接收到信息后双方根据更新自己保存的关于对方的信息。然后显示。
3.3第二关游戏：天赐羽翼
3.3.1软件逻辑设计
根据剧情和编码需要，该篇共分4个逻辑操作，阵列蝙蝠、无相陨石、石头巨人、还有梦幻天使各场景中的逻辑操作。
功能设计： 
 1、大体游戏框架搭建：
     地图拼合
     主人公的位置及移动
     边界控制
     可视窗口控制
 2、游戏功能：
     接受用户键盘输入
     主人公上下左右移动、释放能量球
     场景跳转和过渡
     主人公生命值、分数、位置显示和记录
     游戏的暂停、恢复、退出、帮助信息
     音乐的开关
3、游戏逻辑设计






3.3.2软件详细设计
主要算法：
  1、阵列蝙蝠：
    蝙蝠一个阵列5只，由左至右及由右至左飞临屏幕，每只蝙蝠以5颗毒标为一波连续释放，每次检测主人公与蝙蝠、主人公与蝙蝠毒标、蝙蝠与主人公能量球的碰撞。在游戏开始设置记录屏幕最下方为currentPosition等于0处，此后实时增加作为记录主人公位置的参照。
第一场景游戏参数的初始化
蝙蝠与女孩的碰撞处理
蝙蝠能量球与女孩碰撞的处理
女孩子弹与蝙蝠boss的碰撞处理
阵列蝙蝠由左向右移动路线设置
阵列蝙蝠由右向左移动路线设置
第一场景游戏屏幕效果绘制
        2、无相陨石：
          以精灵类设置陨石，起始位置位于左上角，当陨石飞出屏幕边界时会让其在屏幕同一方向坐标的对面位置出现，给玩家以四面八方，无声无相的感觉。
第二场景游戏参数的初始化
陨石运动效果
陨石与女孩的碰撞处理
陨石精灵的飞行位置处理
第二场景游戏屏幕效果绘制
        3、石头巨人：
          前三个小石头巨人，周身环绕能量波（能量波以旋转型制作）移动，当其移动到特定位置时就会加速，主人公必须防止与之碰撞。
第三场景游戏小石头巨人参数的初始化
小石头巨人与女孩的碰撞处理
小石头巨人的移动设置
第三场景游戏小石头巨人屏幕效果绘制
          接下来的大石头巨人周身同样环绕能量波并以移动形式出现，并且发射各种能量球，必须检测主人公位置与能量球位置是否发生碰撞。
第三场景游戏大石头巨人参数的初始化
大石头巨人与女孩的碰撞处理
大石头巨人与女孩子弹发生碰撞的处理
大石头巨人的移动设置
第三场景游戏大石头巨人屏幕效果绘制
        4、梦幻天使：
          梦幻天使以移动的形式并且释放各种的能量彩色球，必须检测主人公位置与能量球位置是否发生碰撞。
第四场景游戏参数的初始化
女孩子弹与梦幻天使的碰撞处理
女孩与梦幻天使的碰撞处理
第四场景游戏屏幕效果绘制
        5、游戏中彩弹效果函数处理：
周身环绕旋转线：通过一个函数来改变参数ringAngle的值，再利用代数中正弦、余弦函数对ringAngle计算操作得到两个值，通过这两个值来改变x坐标、y坐标实现对图片出现位置的控制（以主人公的位置为中心点用两个值来加减操作中心点坐标x、坐标y来操作图片坐标x、坐标y，利用位置坐标的实时变化以及屏幕的周期刷新实现能量球移动位置变化效果）。

串圆线：利用一个函数来改变参数rad的值，以主人公的位置为中心点用与rad值相加结果来作为组成串圆的能量球的纵坐标y的值，横坐标保持同一个值，利用另一个函数通过计算的坐标来循环绘制4个固定半径大小的圆，使之周期连续出现。

散形线：通过一个函数来改变参数line的值，利用line的值来改变x坐标、y坐标实现对图片出现位置的控制（利用代数中正弦、余弦对line的值进行计算操作，从而得到两个值，以主人公的位置为中心点，用两个值与中心点坐标x，坐标y的加减来实现散型线中能量球位置坐标的确定，利用几个相同函数只是改变相应参数坐标实现多条散型线，并利用位置坐标的实时变化以及屏幕的周期刷新实现能量球移动位置变化效果）。

基因螺旋曲线：利用一个函数来实现改变参数ringAngle的值，再利用代数中正弦函数对ringAngle计算操作得到一个值，通过这这个值来改变x坐标、y坐标实现对图片出现位置的控制（以主人公的位置为中心点，用这个值来加减操作中心点坐标x、坐标y来操作图片坐标x、坐标y，利用位置坐标的实时变化以及屏幕的周期刷新实现能量球移动位置变化效果）。

心形线：通过一个函数来改变参数line的值，利用line的值来改变x坐标、y坐标实现对图片出现位置的控制（以主人公的位置为中心点，利用line值与中心点位置坐标x、坐标y的加减操作得到组成心型线的能量球的的位置坐标x、y，利用位置坐标的实时变化以及屏幕的周期刷新实现能量球移动位置变化效果）。

x2四型线：通过一个函数来改变参数line的值，利用line的值来改变x坐标、y坐标实现对图片出现位置的控制（利用代数中平方函数对line的值进行计算操作，从而得到两个值，以主人公的位置为中心点，用两个值与中心点坐标x，坐标y的加减来实现x2四型线中能量球位置坐标的确定，利用位置坐标的实时变化以及屏幕的周期刷新实现能量球移动位置变化效果）。

星形线：通过一个函数来改变参数line的值，利用line的值来改变x坐标、y坐标实现对图片出现位置的控制（利用代数中正弦、余弦对line的值进行计算操作，得到三个值，以主人公的位置为中心点，用三个值与中心点坐标x，坐标y的加减来实现星型线中能量球位置坐标的确定，利用位置坐标的实时变化以及屏幕的周期刷新实现能量球移动位置变化效果）。

辐射波环线：通过一个函数来改变参数rad的值，利用另一函数来改变参数ringAngle的值，再利用rad和ringAngle的值来改变x坐标、y坐标实现对图片出现位置的控制（利用代数中正弦、余弦对rad和ringAngle的值进行计算操作，从而得到两个值，以主人公的位置为中心点用两个值来操作图片坐标x、坐标y，利用位置坐标的实时变化以及屏幕的周期刷新实现能量球移动位置变化效果）。

        6、游戏中还设置：MusicControl类用于控制音乐（实现音乐的更换、播放、暂停、获得当前播放时间点、调节音量、改变节拍的功能）
                         PausedMenu类用于暂停的菜单的设置（实现暂停菜单中各个菜单项的按键效果的响应实现）
                         FlyShotScene类用于游戏中各种插入场景的设置（场景与游戏的切换调节，以及场景中语言的设计及出现效果）
        7、游戏中相当重要的一点就是碰撞检测，为实现游戏的需要专门设置了两个碰撞检测函数，两个函数的参数设置不同，（其中一个为两碰撞检测图片的左上角点坐标x、坐标y，另一个只包含其中一个图片左上角点的x坐标与y坐标）用于实现一图片中心位于另一图片中心时即认为发生碰撞的效果实现。
        8、背景图片的效果实现：背景就是利用数组实现的一张图片（首先将一张图片分成具有一定区别度的四副图片，并对每副图片进行编号，在一个数组中利用编号对数组中数据赋值，通过改变数组中数据值即可改变图片效果），它的基本的功能就是实时的滚动，以显示主人公及游戏中怪物移动的效果（其实是通过在每次屏幕刷新时对屏幕中图片出现位置的改变来让玩家感觉背景的移动效果）。为了实现衔接自然，当第一个数组图片显示完毕后就将图片数组重新设置到位置0处，从而使背景图连接在一起，实现图片的移动。
3.4 第三关游戏：神勇骑术
     3.4.1软件逻辑设计
1.根据游戏的主体剧情可以将游戏分为如下的几块：








 
1）玩家模块
    游戏的主角，拥有向左移动向右移动的能力，向左移动时车身换成左倾的图片，在图片右倾时换成是车身右倾的图片，一旦按键松开则恢复正常状态下的图片 在危机的时刻还可以向上跳起躲开障碍物，跳起过程中不会碰撞到任何道具。马车的左右振动更加使马车的跑动栩栩如生。但是想要打败敌人获得游戏的胜利还必须借助fire键的发射飞刀来攻击敌人，不过飞刀的数目有限，只有五枚，打中敌人的头部便可一击必杀。即使你的技术在高超也有时候的时候一旦被树枝划伤便会闪烁几下。一旦生命值为0 便会返回原点重新挑战，再当生命的条数也为0且 生命值为0那么游戏就失败了。
飞刀的发射方式采用不可连发的方式，若连发则太容易杀死敌人，所以我们的做法是当第一次发射的飞刀在屏幕内仍有显示的话那么fire键不会得到响应。飞刀的轨迹是由发射时的马车位置和准心的位置来决定的。
  
2）背景模块
游戏的背景对游戏的效果起了烘托的作用，首先第一人称视角是由背景烘托出来，背景的不断移动及其速度的变化营造出马车行驶中的各种效果。场景的切换使得游戏在视觉上不再疲劳，能够享受多种风格的场景。
3）敌人模块
前半程中敌人离自己比较远，只能见到依稀的车影在振动，到了后半程后敌人离自己要近了一些，但是敌人也是时而近时而远，沿着左右方向和上下方向移动，让玩家很难瞄准。使得敌人智能一些，也增加了游戏的可玩性。敌人移动的路线主要是在三条不同距离的横线上运动。当在一条横向运行2周后便会跳到另外一天横向的线上继续运动。并且敌人在不同的点会发出强大的旋转子弹。一旦玩家被该子弹击中便会丢失一条生命。
4）道具模块
整个道具的布置才是本关游戏的重中之重。形形色色的道具，错落有秩地排列在赛道上迎面向玩家移动过来，为了适应视角，道具的走向都是走斜线。在赛道正中的左边则是向做的一条斜线，在赛道正中右边的则是向右的一条斜线。道具的信息使用3数组保存，一个保存初始的x坐标，一个是初始的y坐标，还有一个保存的是道具的类型。 考虑到对于碰撞检测的精度要求不是太高，各种道具的碰撞检测采用矩形检测的方法，这样可以提升游戏的性能。 道具效果的实现，根据道具的类型实现，加金币和加生命值减生命值的道具是在碰撞后改变其属性值。 加速效果和暂停效果的道具则是碰撞后改变了背景和道具的移动速度。
5）音乐模块
音乐对游戏的效果起着重要的作用，我启用了两个音乐线程，背景音乐，和碰撞音效。背景音乐循环播放，较长。碰撞音效采用较短的音乐，在发生碰撞时根据不同的道具播放不同的声音。在碰撞到加速道具时，为了营造更好的氛围，我们也改变了音乐的速度，加速结束，则换回原来的速度，这并非通过换音乐来实现的，而是通过改变音乐的rate来实现的。
6）暂停模块
游戏的同时偶尔会有点事想要暂停一下游戏，所以一个暂停菜单是非常必要的，暂停菜单中不仅可以对游戏暂停还可以改变音乐的开关设置。
7）蓝牙模块
这一关游戏的蓝牙模块较为简单，我们传输的数据有玩家生命值，玩家的位置，玩家剩余生命的条数。在地图上显示自己和对方在赛道的位置，通过判断其生命和位置来决定何方胜利。
8）人性化的提示栏
不仅具有文字的提示，而且游戏中遇到暂停，加速和返回原点时都有图形的提示栏，提示栏的时间也是和各种道具的效果相配合。在游戏中第一次接触的人可能还不了解各种道具的作用，当玩家碰撞到道具时便会提示当前的各种效果帮助玩家迅速掌握游戏的要领。
9）合理的得分计算
本关游戏的得分由路中获得的金币，剩余的血量，剩余的飞刀数，和最终完成游戏所用的时间所决定。这样的得分方式使得玩家探索更多的游戏玩法去获得更高的几分。同时增加了游戏的可玩性。
3.4.2软件详细设计
数据结构的设计
角色和飞刀用还有刺客用矢量来描述
道具分布：用数组来描述，如{x1，x2......} {y1，y2......}
{Position1,Position2......}
主要算法
1．网络游戏算法：本关服务端具有全套系统，客户端传数据到服务端；服务端接受并处理、把最新的游戏数据，包括当前位置、玩家的生命值，玩家剩余生命的次数发给客户端。客户端和服务器都能处理游戏的数据。但是服务器拥有创建服务的功能。
2. 道具的设置：道具由一个图片的数组保存，由一个一维数组保存一次出现的道具类型另一个一维数组保存道具一次出现的初始x坐标，另一数组保存出现的位置。
 3. 道具移动原理：初始道具都在不在屏幕的范围内，当游戏开始后道具的位置值一旦和当前游戏的赛道的位置值相一致便得到初始化，进入屏幕范围内，然后再根据其初始的x坐标选择其运动的轨迹。其斜率固定，当速度发生变化时，道具移动的速度也发生变化。
由于速度会发生变化，所以初始化道具的时机不能选择固定的位置值而是由保存的位置值周围的一个范围来确定，这样避免在加速时由于当前道路位置和保存的道具位置值不一致而导致道具混乱
一旦道具得到初始化后便根据其初始的x值是否大于道路中间，若在左边那么向左边以固定的斜率移动，在右边时则向右边移动。移动时y值每次移动量由速度speed决定，x的坐标由初始坐标值加上已经移动的y量乘上斜率，这样保证了直线不会受取整的影响而影响太大，偏离原来的轨道。
  
4.马车的振动效果：定义一个标志值，当马车在向左移且未达到左边界的时候向左移一旦达到左边界则向右移。反之，亦然。
5.马车的闪烁效果：定义一个是否可见的标志符，当可见时显示马车一段时间，然后转换为不可见状态一段时间，如此反复。
6.刺客的AI移动:
刺客在3个不同的距离上移动，当在某一个距离上由中心开始移动向左移动直至到达左边界然后返回再到右边界然后返回循环两次后变换距离再做这样的往复运动。
7．敌人子弹的发射：
敌人子弹的发射是配合敌人的AI移动实现的。当敌人在离敌人最近的地方时移动到在中间时便会沿竖直方向发射旋转的子弹，当移动到离玩家较远的地方时将发射斜线方向的旋转子弹。
8.子弹旋转的实现
子弹旋转分支按照固定的角度分开，每移动一次旋转分支绕着圆心旋转30°。旋转效果的子弹便实现了。
9．发射飞刀的按键控制
如果对fire键不加以控制那么一直按着fire键则会一直发射飞刀，那么会浪费有限的飞刀。所以在当上次发射的飞刀还在屏幕范围内时，按下fire键不进行处理。这样便实现了对发射飞刀的控制。
10.得分计算
本关游戏的得分由路中获得的金币，剩余的血量，剩余的飞刀数，和最终完成游戏所用的时间所决定。当所用的时间在不同的时间范围内则加上不同的分数。
11.蓝牙模块
游戏客户端向服务器发送一个字符S然后服务器收到后哦向客户端发送一个S，然后置同步信号为true ，客户端和服务器互相发送自己游戏信息，进行处理。一旦一方游戏失败则另一方胜利。
第四章 主要技术概述

4.1 J2ME技术简介
        J2ME是Java2 Micro Edition 的缩写，是Sun公司为了把Java应用于移动通讯设备、嵌入式设备或消费性电器而推出的一项技术。(一般在个人电脑上的应用为J2SE ?Java 2 Standard Edition; 在企业中的应用为J2EE ?Java 2 Enterprise Edition)J2ME包括虚拟机、针对设备的API库、针对设备的配置和框架定义(Configuration & Profile)等.

4.2 J2ME声音播放技术
J2ME中，处理声音需要使用到Mobile Media API(MMAPI)，该包是MIDP1.0的可选包，在MIDP2.0中已经包含了这个包。一般手机支持的声音文件格式为wav、mid和mpg等.
　　在声音处理中，有很多处理的方式，这里说一下最常用的情况，播放JAR文件中的wav文件。
　　播放声音文件的方法：
　　 1、按照一定的格式读取声音文件。
　　播放JAR文件中的声音文件一般是将声音文件处理成流的形式。示例代码：
InputStream is = getClass().getResourceAsStream("/Autorun.wav");
　　其中Autorun.wav文件位于JAR文件的根目录下，如果位于别的目录，需要加上目录名称，如/res /Autorun.wav。
　　 2、将读取到的内容传递给播放器。
　　将流信息传递给播放器，播放器按照一定的格式来进行解码操作，示例代码：
Player player = Manager.createPlayer(is,"audio/x-wav");
　　其中第一个参数为流对象，第二个参数为声音文件的格式。
　　 3、播放声音。
　　使用Player对象的start方法，可以将声音播放出来，示例代码：
player.start()；
　　在播放声音时也可以设定声音播放的次数，可以使用Player类中的setLoopCount方法来实现.
4.3 Moblie 3D技术
JSR184标准（M3G：Mobile 3D Graphics）为Java移动应用程序定义了一个简洁的3D API接口，J2ME程序可以非常方便地使用M3G来实现3D应用比如游戏等等。
    M3G是J2ME的一个可选包，以OpenGL为基础的精简版，一共有30个类，运行在CLDC1.1/CLDC2.0上（必须支持浮点运算），可以在MIDP1.0和MIDP2.0中使用。目前，支持M3G的手机有Nokia 6230/3650/7650/6600、Siemens S65/CX65/S55/M55、Sony-Ericsson K700i/P800/P900、Moto 220/T720等。M3G只是一个Java接口，具体的底层3D引擎一般由C代码实现，比如许多手机厂商的3D引擎采用的便是SuperScape公司的Swerve引擎，这是一个专门为移动设备设计的高性能3D引擎。
    类似于Microsoft的D3D，M3G支持两种3D模式：立即模式（immediate mode）和保留模式（retained mode）。在立即模式下，开发者必须手动渲染每一帧，从而获得较快的速度，但代码较繁琐；在保留模式下，开发者只需设置好关键帧，剩下的动画由M3G完成，代码较简单，但速度较慢。M3G也允许混合使用这两种模式。
    3D模型可以在程序中创建，但是非常繁琐。因此，M3G提供一个Loader类，允许直接从一个单一的.m3g文件中读入全部3D场景。m3g文件可以通过3D Studio Max之类的软件创建。
    如果熟悉OpenGL，那么M3G是非常容易理解的。在M3G中，Graphics3D是3D渲染的屏幕接口，World代表整个3D场景，包括Camera（用于设置观察者视角）、Light（灯光）、Background（背景）和树型结构的任意数量的3D物体。3D对象在计算机中用点（Point, Pixel）、线（Line, Polyline, Spline）、面（Mesh）来描述，具体存储和运算（如旋转、投影）都是矩阵运算和变换。
	4.4 蓝牙技术
每一个蓝牙应用都表现为一个蓝牙服务，提供服务的一方称之为服务端，消费服务的一方称之为客户端，与日常中所提到的服务端-客户端模型基本相似。UUID类用来惟一识别一个蓝牙服务或属性，UUIS实例是不可改变的，每一个这样的标识符都要在时空上保证惟一，存在两种形式的UUID类,16或32位的简短形式，以及128位的完整UUID。
总的来说蓝牙服务在使用之前要进行初始化的过程，无论客户端还是服务器端都要进行初始化。对于服务端来说就是创建服务、等待客户访问、通信，首先利用UUID计算URL，创建L2CAPConnectionNotifier连接或StreamConnectionNotifier连接，利用LocalDevice取得对应此连接的服务ServiceRecord；然后调用ConnetionNotifier的方法，等待连接；最后如果连接成功打开，即可开启新的线程进行通信。对于客户端来说，首先通过LocalDevice取得DiscoveryAgent对象，利用该对象发起一次设备查询，设备找到后会调用DiscoveryListener接口对应的方法，我们要记录下远端设备RemoteDevice，再次通过DiscoveryAgent对象，并提供刚刚找到的远端设备，发起一次服务查询来查询感兴趣的远端服务。服务找到后会调用DiscoveryListener接口对应的方法，将ServiceRecord服务记录下来，至此，一次完整的搜索结束，得到了感兴趣的ServiceRecord列表。最后通过ServiceRecord的相应方法可以取得远端服务的URL，这样就可以根据服务的类型选择创建对应的SPP的StreamConnection或对应L2CAP的L2CAPConnection来进行数据通信了。
所以基于以上的理论知识，我们这款游戏的蓝牙部分的设计如下
（1）	服务器端：首先通过getLocalDevice()方法取得LocalDevice对象实例，还必须调用setDiscoverable()方法设置好模式。有三种发现模式：DiscoveryAgent.GIAC代表没有时间限制的模式，DiscoveryAgent.LIAC代表在规定的时间内可被搜索，超过一定时间就进入不可搜索状态。DiscoveryAgent.NOT_DISCOVERABLE代表不可搜索状态。在本款游戏的蓝牙程序中我们采用的是没有时间限制的模式，即设置localDevice.setDiscoverable(DiscoveryAgent.GIAC)。随后通过StreamConnection建立一个连接，同时创建一个服务。在初始化完成之后就进入等待客户端连接请求阶段，线程一直处于阻塞状态直到捕捉到客户端的连接请求。
（2）	客户端：首先如同服务器端得到本地设备，并取得蓝牙代理，接下来进入设备查询的过程，discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);用于查询远端的服务器设备，每查找到一个设备方法deviceDiscovered(RemoteDevice btDevice, DeviceClass cod)便得到调用，将RemoteDevice加入到设备矢量数组中，接着便进入阻塞状态，直到设备查询完成之后inquiryCompleted()方法得到调用，并唤醒当前的线程，接下来对查询到的设备进行服务查询，每查到一个服务servicesDiscovered(int transID, ServiceRecord[] servRecord)方法得到调用，并将服务加到服务的矢量数组中，接着同样进入阻塞状态直到查询完毕serviceSearchCompleted()得到调用，同时唤醒当前线程。最后是建立连接的阶段，遍历服务记录的矢量数组中，取出一个服务记录同时据此得到URS值，然后尝试通过此URL建立连接，如果连接成功便退出遍历循环，否则取下一个服务记录。
（3）	输入输出流的创建：因为此款游戏是双机游戏，也就是说无须建立三个或多个连接，只须建立两部手机的蓝牙连接即可，并且在连接建立之后就可以一直存在下去。同时我们考虑到，如果每次传输数据时就创建输入输出流，传输完成之后关闭，等到下一次传输时，重新创建此输入输出流，这样会造成手机资源的浪费，所以我们决定采用将输入输出流作为server或client的字段，在连接建立成功后就不会关闭。
至于传输的数据的作用和方式在游戏画布主类中实现在此只考虑数据的传输问题。

第五章 项目规划
5.1项目分工
根据组员的各自的特点和项目情况，分工如下：
姓名	任务
王阿晶（组长）	担任组长，负责项目人员的分工和协调；3D迷宫的设计和编码实现；迷宫多人游戏中的研究与实现
孙杰	担任美工；游戏创意与剧情设计；平面图形处理；3D图形处理；音频处理
梁春晓  	负责游戏的菜单和动画设计和编码；数据的永久性存储实现；文档编写
李少坤	负责射击游戏的创意设计和编码；文档编写
申宝明	负责神勇骑术游戏的创意设计和编码；该游戏多人游戏中的研究与实现
5.2进度计划
 严格按照软件工程生命周期，安排如下：
阶段	时间	任务
设计阶段	7月1日-7月15日	概要设计；详细设计
编码阶段	7月16日-8月20日	编码实现
测试阶段	8月20日-9月1日	测试；文档完善






参考文献：
《J2ME手机编程基础》 Leopold Lee编著 清华大学出版社
《J2ME手机开发入门》刘斌 丁璇 庞晖 编著 人民邮电出版社
《J2ME开发精解》詹建飞 编著 电子工业出版社
《J2ME核心技术与最佳实践》詹建飞 编著 电子工业出版社	

