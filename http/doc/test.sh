source /etc/profile

###################################
# Please change these parameters according to your real env.
###################################
# set Java Home: Remember that dolphin only supports JDK8!
# JAVA_HOME=/usr/local/jdk1.8.0_60
# check JAVA_HOME
if [ x"$JAVA_HOME" == x ]; then
    echo "==================== Failed! ====================="
    echo "======         Please set JAVA_HOME         ======"
    echo "=================================================="
    exit 1
fi

#running user
RUNNING_USER=root

# set ulimit
#ulimit -s 20480

# application directory
cd `dirname $0`
APP_HOME="$(pwd)"
APP_NAME="$(cd ${APP_HOME} && find -mindepth 1 -maxdepth 1 -name '*.jar' |awk -F'/' '{print $NF}')"

#使用哪个环境下的配置文件dev、test、prod
PROFILES=test

# Java JVM lunch parameters
if [ x"$JAVA_OPTS" == x ]; then
    JAVA_OPTS="-Djava.awt.headless=true -Djava.net.preferIPv4Stack=true" #"-javaagent:/usr/local/jacoco-0.7.9/lib/jacocoagent.jar=output=tcpserver,port=8893,address=0.0.0.0"
fi
#-server:一定要作为第一个参数，在多个CPU时性能佳
#-Xms512m
#设置堆的初始化大小，默认是物理内存的1/64
#-Xmx512m
#设置堆的最大大小，默认是物理内存的1/4。默认空余堆内存小于40%时，JVM就会增大堆直到-Xmx的最大限制；空余堆内存大于70%时，JVM会减少堆直到 -Xms的最小限制。因此服务器一般设置-Xms、-Xmx相等以避免在每次GC 后调整堆的大小。对象的堆内存由称为垃圾回收器的自动内存管理系统回收。
#-Xmn228m
#设置年轻代初始化大小，等同于-XX:Newsize
#-XX:MaxNewSize=size
#设置年轻代的最大大小
#-xx:NewRatio=4
#年轻代和老年代相对的比值大小大小，如果设置-XX：NewRatio=4，指标是Young Generation：Old Generation = 1:4，即年轻代为老年代的1/4。Xms=Xmx并且设置了Xmn的情况下，该参数不需要进行设置。
#-XX:SurvivorRatio=4
#设置年轻代大小与幸存区大小的比例，默认为8.在Young Gerenation中，from(Survivor1)、to(Survivor2)与Eden的比值，若-XX：SurvivorRatio=6，由于from、to内存大小相等，from 、to分别占Young Generation的空间的1/8，Eden占6/8；
#-XX:MetaspaceSize
#设置元空间大小，在jdk8之后取代-XX:PermSize
#-Xss256k 设置每个线程栈的大小，等同于 -XX:ThreadStackSize.JDK5.0以后每个线程堆栈大小为1M,以前每个线程堆栈大小为256K.更具应用的线程所需内存大小进行 调整.在相同物理内存下,减小这个值能生成更多的线程.但是操作系统对一个进程内的线程数还是有限制的,不能无限生成,经验值在3000~5000左右
#一般小的应用， 如果栈不是很深， 应该是128k够用的 大的应用建议使用256k。这个选项对性能影响比较大，需要严格的测试。（校长）
#
#-XX:+UseConcMarkSweepGC
#启用对老年代的CMS垃圾回收，Oracle官方推荐启用。当被启用时， -XX:+UseParNewGC也会被自动设置。
#-XX:+AggressiveHeap
#试图是使用大量的物理内存，长时间大内存使用的优化，能检查计算资源（内存， 处理器数量）至少需要256MB内存。大量的CPU／内存， （在1.4.1在4CPU的机器上已经显示有提升）
#-XX:+UseCMSInitiatingOccupancyOnly
#使用手动定义初始化定义开始CMS收集，禁止hostspot自行触发CMS GC。默认不开启
#
#
#-XX:MaxTenuringThreshold=10
#垃圾最大年龄，如果设置为0的话,则年轻代对象不经过Survivor区,直接进入年老代. 对于年老代比较多的应用,可以提高效率.如果将此值设置为一个较大值,则年轻代对象会在Survivor区进行多次复制,这样可以增加对象再年轻代的存活 时间,增加在年轻代即被回收的概率
#该参数只有在串行GC时才有效。最大值是15.对于parallel 回收器，默认是15，对于CMS回收器，默认6.当对象在年轻代中回收次数达到该值，则会被迁移至老年代。
#-XX:+AggressiveOpts
#加快编译，默认不开启
#-XX:+UseBiasedLocking
#锁机制的性能改善
#-Xnoclassgc
#禁用垃圾回收
#-XX:+UseParallelGC
#Full GC采用parallel MSC
#-XX:+UseParNewGC
#设置年轻代为并行收集
#-XX:ParallelGCThreads=2
#并行收集器的线程数，he default value depends on the number of CPUs available to the JVM.此值最好配置与处理器数目相等 同样适用于CMS
#-XX:+UseParallelOldGC
#年老代垃圾收集方式为并行收集(Parallel Compacting)
#-XX:MaxGCPauseMillis=500
#每次年轻代垃圾回收的最长时间(最大暂停时间)，如果无法满足此时间,JVM会自动调整年轻代大小,以满足此值.
#-XX:+UseAdaptiveSizePolicy
#自动选择年轻代区大小和相应的Survivor区比例，设置此选项后,并行收集器会自动选择年轻代区大小和相应的Survivor区比例,以达到目标系统规定的最低相应时间或者收集频率等,此值建议使用并行收集器时,一直打开.
#-XX:+ScavengeBeforeFullGC
#Full GC前调用YGC，默认为true
#-XX:CMSInitiatingOccupancyFraction=70
#设置当老年代占比时启动CMS垃圾回收周期，参数指定CMS垃圾回收器在老年代达到80%的时候开始工作，如果不指定那么默认的值为92%；
#-XX:+CMSClassUnloadingEnabled
#启用当CMS回收时，不进行类的加载，默认开启。开启永久带（jdk1.8以下版本）或元数据区（jdk1.8及其以上版本）收集，如果没有设置这个标志，
# 一旦永久代或元数据区耗尽空间也会尝试进行垃圾回收，但是收集不会是并行的，而再一次进行Full GC；
#-XX:SoftRefLRUPolicyMSPerMB=time
#每兆堆空闲空间中SoftReference的存活时间
#-XX:TargetSurvivorRatio=30
# 年轻代垃圾回收后幸存区大小占比，默认50%
#-XX:+CMSParallelRemarkEnabled
#减少Remark阶段暂停的时间，启用并行Remark，如果Remark阶段暂停时间长，可以启用这个参数
# -XX:+CMSScavengeBeforeRemark
#如果Remark阶段暂停时间太长，可以启用这个参数，在Remark执行之前，先做一次ygc。因为这个阶段，年轻带也是cms的gcroot，
# cms会扫描年轻带指向老年代对象的引用，如果年轻带有大量引用需要被扫描，会让Remark阶段耗时增加
#-XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0
#两个参数是针对cms垃圾回收器碎片做优化的，CMS是不会移动内存的， 运行时间长了，会产生很多内存碎片， 导致没有一段连续区域可以存放大对象，
# 出现”promotion failed”、”concurrent mode failure”, 导致fullgc，启用UseCMSCompactAtFullCollection 在FULL GC的时候， 对年老代的内存进行压缩
#-XX:+CMSConcurrentMTEnabled
#-XX:ConcGCThreads=4
#定义并发CMS过程运行时的线程数。比如value=4意味着CMS周期的所有阶段都以4个线程来执行。尽管更多的线程会加快并发CMS过程，
# 但其也会带来额外的同步开销。因此，对于特定的应用程序，应该通过测试来判断增加CMS线程数是否真的能够带来性能的提升。如果未设置这个参数，
# JVM会根据并行收集器中的-XX:ParallelGCThreads参数的值来计算出默认的并行CMS线程数：
#ParallelGCThreads = (ncpus <=8 ? ncpus : 8+(ncpus-8)*5/8) ，ncpus为cpu个数，
#ConcGCThreads =(ParallelGCThreads + 3)/4
#这个参数一般不要自己设置，使用默认就好，除非发现默认的参数有调整的必要；
#-XX:+ExplicitGCInvokesConcurrent
#-XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses
#开启foreground CMS GC，CMS gc 有两种模式，background和foreground，正常的cms gc使用background模式，就是我们平时说的cms gc；
# 当并发收集失败或者调用了System.gc()的时候，就会导致一次full gc，这个fullgc是不是cms回收，而是Serial单线程回收器，加入了参数-XX:+ExplicitGCInvokesConcurrent后，
# 执行full gc的时候，就变成了CMS foreground gc，它是并行full gc，只会执行cms中stop the world阶段的操作，效率比单线程Serial full GC要高；
# 需要注意的是它只会回收old，因为cms收集器是老年代收集器；而正常的Serial收集是包含整个堆的，加入了参数-XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses,代表永久带也会被cms收集；
#-XX:+CMSParallelInitialMarkEnabled
#开启初始标记过程中的并行化，进一步提升初始化标记效率;
#-XX:LargePageSizeInBytes=128m
# 启用大内存页支持。内存页的大小不可设置过大， 会影响Perm的大小
#-XX:+UseFastAccessorMethods
# 原始类型的快速优化

#-XX:+PrintCommandLineFlags
#打印出启动参数行
#-XX:+PrintGC
#Enables printing of messages at every GC. By default, this option is disabled.
#输出形式:
#[GC 118250K->113543K(130112K), 0.0094143 secs]
#[Full GC 121376K->10414K(130112K), 0.0650971 secs]
#-verbose:gc
#报告每次垃圾回收事件
#-XX:+PrintGCDetails
#-XX:+PrintGCApplicationStoppedTime
#打印垃圾回收期间程序暂停的时间
#-XX:+PrintGCTimeStamps
#输出GC的时间戳（以基准时间的形式）
#-XX:+PrintGCDateStamps
#输出GC的时间戳（以日期的形式，如 2013-05-04T21:53:59.234+0800）
#-XX:+PrintHeapAtGC
#打印GC前后的详细堆栈信息
#-XX:+PrintTenuringDistribution
# 打印年代详情
#-XX:+PrintClassHistogram
#garbage collects before printing the histogram.
#
#-Xloggc:logs/gc.log
#指定verbose GC事件日志的文件保存位置，如果同时存在-verbose:gc，将会覆盖后者
#-XX:+HeapDumpOnOutOfMemoryError
#-XX:HeapDumpPath=logs/dump.log
#则是内存溢出时dump堆
#https://docs.oracle.com/javase/8/docs/technotes/tools/windows/java.html?spm=a2c6h.13066369.0.0.58465789Cm6P9d



if [ x"$JAVA_MEM_OPTS" == x ]; then
    JAVA_MEM_OPTS="-server -Xms212m -Xmx212m -XX:MaxDirectMemorySize=20m -Xmn108m -Xss256k -XX:+UseSerialGC -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -Xloggc:logs/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs/dump.log"
fi



# path of log file, because logback can't create missing directory, we need to help it by shell script
LOGS_DIR="$APP_HOME/logs"
# LOGS_DIR="/data/log-center/zetty"
if [ ! -d $LOGS_DIR ]; then
    sudo -E -u ${RUNNING_USER} sh -c "mkdir $LOGS_DIR"
    chmod 777 $LOGS_DIR
    #echo "created logs directory: path=$LOGS_DIR"
fi
STDOUT_FILE=$LOGS_DIR/start.log



# waiting timeout for starting, in seconds
START_WAIT_TIMEOUT=30

psid=0

checkpid() {
    psid=$(sudo sh -c "/bin/ps aux" | grep $APP_NAME |grep -v grep |awk '{print $2}')
    if [[ -z "$psid" ]]; then
       psid=0
    fi
}


###################################
#(函数)启动程序
 #
 #说明：
 #1. checkpid，刷新$psid全局变量
 #2. 如果程序已经启动（$psid不等于0），则提示程序已启动
 #3. 如果程序没有被启动，则执行启动命令行
 #4. 启动命令执行后，再次调用checkpid函数
 #5. 如果步骤4的结果能够确认程序的pid,则打印[OK]，否则打印[Failed]
 #注意：echo -n 表示打印字符后，不换行
 #注意: "nohup 某命令 >/dev/null 2>&1 &" 的用法
###################################
start() {
   checkpid

   if [ $psid -ne 0 ]; then
      echo "warn: $APP_NAME already started! (pid=$psid)"
   else
      echo -n "Starting $APP_NAME ..."
      sudo -E -u ${RUNNING_USER} sh -c "nohup $JAVA_HOME/bin/java $JAVA_OPTS $JAVA_MEM_OPTS $PINPOINT_OPTS -jar ${APP_HOME}/${APP_NAME} server.profiles.active=$PROFILES >>$STDOUT_FILE 2>&1 & "
      sleep 1
      checkpid
      if [ $psid -ne 0 ]; then
         echo "(pid=$psid)     [OK]"
      else
         echo "      [Failed]"
      fi
   fi
}


###################################
#(函数)停止程序
#
#说明：
#1. 首先调用checkpid函数，刷新$psid全局变量
#2. 如果程序已经启动（$psid不等于0），则开始执行停止，否则，提示程序未运行
#3. 使用kill -9 pid命令进行强制杀死进程
#4. 执行kill命令行紧接其后，马上查看上一句命令的返回值: $?
#5. 如果步骤4的结果$?等于0,则打印[OK]，否则打印[Failed]
#6. 为了防止java程序被启动多次，这里增加反复检查进程，反复杀死的处理（递归调用stop）。
#注意：echo -n 表示打印字符后，不换行
#注意: 在shell编程中，"$?" 表示上一句命令或者一个函数的返回值
###################################
stop() {
   STOP_WAIT_TIME="90"
   checkpid

   if [ $psid -ne 0 ]; then
      echo -n "Stopping $APP_NAME ...(pid=$psid) "
      sudo sh -c "kill $psid"

      let kwait=${STOP_WAIT_TIME}
      count=0;
      until [ $psid -eq 0 ] || [ $count -gt $kwait ]  
      do
          echo -n -e ".";
          sleep 1
          checkpid
          let count=$count+1;
      done
      if [ $count -gt $kwait ];then
          echo -n -e "\nkilling processes which didn't stop after ${STOP_WAIT_TIME} seconds\n"
          sudo sh -c "kill -9 $psid"
      fi
      echo "[OK]"
   else
      echo "================================"
      echo "warn: $APP_NAME is not running"
      echo "================================"
   fi
}


###################################
#(函数)检查程序运行状态
#
#说明：
#1. 首先调用checkpid函数，刷新$psid全局变量
#2. 如果程序已经启动（$psid不等于0），则提示正在运行并表示出pid
#3. 否则，提示程序未运行
###################################
status() {
   checkpid

   if [ $psid -ne 0 ];  then
      echo "$APP_NAME is running! (pid=$psid)     "
   else
      echo "$APP_NAME is not running     "
   fi
}



###################################
#(函数)打印系统环境参数
###################################
info() {
   echo "System Information:"
   echo "********************************************************"
   echo `head -n 1 /etc/redhat-release`
   echo `uname -a`
   echo
   echo "JAVA_HOME=$JAVA_HOME"
   echo `$JAVA_HOME/bin/java -version`
   echo
   echo "APP_HOME=$APP_HOME"
   echo "APP_NAME=$APP_NAME"
   echo "********************************************************"
}

extend_opts(){
   #echo "extend_opts starting.."
   while getopts "a:p:f:" optname
    do
      #echo "The $optname is $OPTARG"
      case "$optname" in
        "a")
          echo "Option $optname(PROFILES) is $OPTARG"
          PROFILES=$OPTARG
          ;;
         "f")
          echo "Option $optname(WAR_FILE) is $OPTARG"
          WAR_FILE=$OPTARG
          ;;
        "?")
          echo "Usage: args [-p] [-a]"
          echo "-p means http port"
          echo "-m means profiles active"
          exit 1
          ;;
        *)
        # Should not occur
          echo "Unknown error while processing options"
          ;;
      esac
    done
}

extend_opts "$@"

###################################
#读取脚本的最后一个参数，进行判断
#参数取值范围：{start|stop|restart|status|info}
#如参数不在指定范围之内，则打印帮助信息
###################################
args=$#
case "${!args}" in
   'start')
      start
      ;;
   'stop')
     stop
     ;;
   'restart')
     stop
     start
     ;;
   'status')
     status
     ;;
   'info')
     info
     ;;
  *)
     echo "Usage: $0 {start|stop|restart|status|info}"
     exit 1
esac

