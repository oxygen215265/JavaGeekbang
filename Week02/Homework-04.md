# GC Summary


# Serial GC

Command: -Xmx256m -Xms256m -XX:+PrintGCDetails -XX:+UseSerialGC
新生代：serial 单线程 复制算法
老年代：serial old 单线程 标记整理

## Heap
 def new generation   total 78656K, used 20564K
  eden space 69952K,  29% used 
  from space 8704K,   0% used 
  to  space 8704K,   0% used
 tenured generation   total 174784K, used 174556K
 Metaspace  used 3087K, capacity 4556K, committed 4864K, reserved 1056768K
  class space    used 327K, capacity 392K, committed 512K, reserved 1048576K

## Heap Analysis

def new generation(Young 区) -> 1/3 堆内存 （eden 80% + from 10% + to 10%) ,最大只能用eden + 一个survive区，即90%young区空间

tenured generation（old 区）-> 2/3堆内存

## Log Analysis

> [GC (Allocation Failure) [DefNew: 69952K->8703K(78656K), 0.0098728 secs] 69952K->19320K(253440K), 0.0100350 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 

Minor GC, young区从69952K清理至8703K，整个堆内存从69952K清理至19320K，差值是由于部分对象进入老年代。对象在第一次GC就晋升老年代是由于s0空间不足以装下eden中存活的对象，导致部分对象提前晋升至老年代，回收后young区内存大小等于survive区容量也可证明这点。

>  [GC (Allocation Failure) [DefNew: 78655K->78655K(78656K), 0.0000188 secs][Tenured: 166312K->138832K(174784K), 0.0239953 secs] 244967K->138832K(253440K), [Metaspace: 3080K->3080K(1056768K)], 0.0240665 secs] [Times: user=0.01 sys=0.01, real=0.03 secs] 

Eden区满触发Minor GC，与之前类似，survive区装不下所有存活对象导致部分对象提前晋升，而老年代空间也不足，触发Full GC（Tenured: 166312K->138832K) 

> [Full GC (Allocation Failure) [Tenured: 174711K->174709K(174784K), 0.0118142 secs] 253367K->182294K(253440K), [Metaspace: 3081K->3081K(1056768K)], 0.0118678 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 

根据Full GC (Allocation Failure) 得知Full GC是由对象提前晋升或长期存活的对象进入老年代，但老年代空间不足触发Full GC。



# Parallel GC

-Xmx1g -Xms1g -XX:+PrintGCDetails -XX:+UseParallelGC

新生代 ParNew 多线程 复制算法
老年代 Parallel Old 多线程 标记整理

## Heap

 PSYoungGen      total 232960K, used 45683K 
  eden space 116736K, 4% used 
  from space 116224K, 34% used 
  to   space 116224K, 0% used 
 ParOldGen       total 699392K, used 436997K 
  object space 699392K, 62% used 
 Metaspace       used 3088K, capacity 4556K, committed 4864K, reserved 1056768K
  class space    used 327K, capacity 392K, committed 512K, reserved 1048576K

## Heap Analysis

与Serial GC不同的是，由于未关闭自适应参数，对于不同大小的垃圾对象，eden和survive区的大小也会随之变化，经过测试垃圾对象越大，eden和survive区大小越接近。
Young区 1/3堆内存
Old区 2/3堆内存

## Log Analysis

>[GC (Allocation Failure) [PSYoungGen: 262144K->43517K(305664K)] 262144K->83073K(1005056K), 0.0270124 secs] [Times: user=0.02 sys=0.08, real=0.03 secs] 

Minor GC , 可以看出eden初始大小为262144K （75% Young区），同样由于survive区不足以装下所有eden存活对象，导致部分对象在第一次Minor GC就提前晋升（猜测自适应基于这种情况，增大了survive区大小）

> [Full GC (Ergonomics) [PSYoungGen: 38555K->0K(232960K)] [ParOldGen: 605506K->339171K(699392K)] 644062K->339171K(932352K), [Metaspace: 3081K->3081K(1056768K)], 0.0718656 secs] [Times: user=0.09 sys=0.37, real=0.07 secs] 

Full GC，Ergonomics 表示此次GC是由于空间担保失败，晋升到老年代的平均大小大于老年代的剩余大小，导致触发Full GC


# CMS GC

-Xmx1g -Xms1g -XX:+PrintGCDetails -XX:+UseConcMarkSweepGC

新生代：ParNew 多线程 复制
老年代：CMS 多线程 标记清除

## Heap

 par new generation   total 314560K, used 58119K 
  eden space 279616K,   8% used 
  from space 34944K,  99% used 
  to   space 34944K,   0% used 
 concurrent mark-sweep generation total 699072K, used 456877K 
 Metaspace       used 3088K, capacity 4556K, committed 4864K, reserved 1056768K
  class space    used 327K, capacity 392K, committed 512K, reserved 1048576K

## Heap Analysis
CMS GC的堆内存分布与Serial GC的类似
Young 区-> 1/3 堆内存 （eden 80% + from 10% + to 10%) ,最大只能用eden + 一个survive区，即90%young区空间

Old 区-> 2/3堆内存


## Log Analysis

>[GC (Allocation Failure) [ParNew: 279616K->34944K(314560K), 0.0464946 secs] 279616K->90439K(1013632K), 0.0471622 secs] [Times: user=0.07 sys=0.13, real=0.05 secs] 

ParNew类似于Serial回收器的多线程版本
user -> GC线程占用的CPU时间总和
sys -> OS调用或等待系统时间
real -> 暂停时间
对于多线程回收器，user可能会大于real。
YoungGC触发条件及部分对象提前晋升和串行/并行回收器一样，不再重复。

### Full GC
###  Initial Mark（STW）
>[GC (CMS Initial Mark) [1 CMS-initial-mark: 356812K(699072K)] 416943K(1013632K), 0.0002230 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
标记老年代中所有的GC Roots,标记被年轻代中活着的对象引用的对象。

### Concurrent Mark
>[CMS-concurrent-mark-start]
[CMS-concurrent-mark: 0.003/0.003 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
遍历整个年老代并且标记活着的对象,如果有对象引用发生变化，JVM会标记该区域为Dirty Card

### Concurrent Preclean
> [CMS-concurrent-preclean-start]
[CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 

将上一次标记被标记为Dirty Card的对象及可达的对象重新遍历标记，清楚dirty 标记

### Concurrent Abortable Preclean
>[CMS-concurrent-abortable-preclean-start]
[GC (Allocation Failure) [ParNew: 314519K->34941K(314560K), 0.0419076 secs] 671331K->475311K(1013632K), 0.0419335 secs] [Times: user=0.18 sys=0.04, real=0.04 secs] 
[CMS-concurrent-abortable-preclean: 0.007/0.264 secs] [Times: user=0.79 sys=0.15, real=0.26 secs] 

可中断的预处理阶段，这个阶段尝试着去承担接下来STW的Final Remark阶段足够多的工作直到发生aboart的条件。

### Final Remark(STW)
>[GC (CMS Final Remark) [YG occupancy: 40538 K (314560 K)][Rescan (parallel) , 0.0006199 secs][weak refs processing, 0.0000493 secs][class unloading, 0.0007597 secs][scrub symbol table, 0.0003473 secs][scrub string table, 0.0000920 secs][1 CMS-remark: 673068K(699072K)] 713607K(1013632K), 0.0019127 secs] [Times: user=0.00 sys=0.01, real=0.00 secs] 
>
完成标记整个年老代的所有的存活对象
[Rescan (parallel) , 0.0006199 secs] 重新扫描堆中存活对象
[weak refs processing, 0.0000493 secs]  处理弱引用
[class unloading, 0.0007597 secs] 卸载无用的类
[scrub symbol table, 0.0003473 secs] 清理分别包含类级元数据和内部化字符串的符号和字符串表的耗时。

### Concurrent Sweep
> [CMS-concurrent-sweep-start]
[CMS-concurrent-sweep: 0.003/0.003 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 


清除没有标记的对象，回收内存。
### Concurrent Reset
>[CMS-concurrent-reset-start]
[CMS-concurrent-reset: 0.002/0.002 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 

重置CMS算法内部的数据结构，准备下一个CMS生命周期。




# G1 GC

-Xmx512m -Xms512m -XX:+PrintGCDetails -XX:+UseG1GC
新生代老年代均为 G1 多线程 复制+标记整理


## Heap
garbage-first heap total 524288K, used 352469K 
region size 1024K, 3 young (3072K), 2 survivors (2048K)
Metaspace used 3088K, capacity 4556K, committed 4864K, reserved 1056768K
class space  used 327K, capacity 392K, committed 512K, reserved 1048576K

## Heap Analysis
G1GC划分为512个region，每个region大小为1024K, 当前Young区三个region，survive区两个region

## Log Analysis

https://www.cnblogs.com/javaadu/p/11220234.html

https://www.cnblogs.com/yufengzhang/p/10571081.html



