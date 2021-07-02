
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

### Young GC
> GC pause (G1 Evacuation Pause) (young), 0.0056021 secs]

此处触发YoungGC （G1 Evacuation Pause）是由于两次尝试分配内存失败
>[Parallel Time: 4.8 ms, GC Workers: 8]

Parallel Time 即为STW时间 ，GC Worker表示GC回收线程数
>[GC Worker Start (ms): Min: 192.3, Avg: 192.4, Max: 192.5, Diff: 0.2]

Min为第一个垃圾收集线程开始工作时JVM启动后经过的时间,Max为最后一个垃圾
收集线程开始工作时JVM启动后经过的时间,diff为min和max之间的差值
>[Ext Root Scanning (ms): Min: 0.1, Avg: 0.2, Max: 0.3, Diff: 0.2, Sum: 1.7]

扫描根，根引用连同RS记录的外部引用作为扫描存活对象的入口。
>[Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
>
每个分区都有自己的RSet，用来记录其他分区指向当前分区的指针，如果RSet有更新，会被标记为dirty，并放入一个缓冲区.
>[Processed Buffers: Min: 0, Avg: 0.0, Max: 0, Diff: 0, Sum: 0]
>
在Update RS这个过程中处理日志缓冲区的数量

>[Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.2]

扫描每个新生代分区的RSet

>[Code Root Scanning (ms): Min: 0.0, Avg: 0.5, Max: 3.8, Diff: 3.8, Sum: 3.8]

扫描代码中的root节点

>[Object Copy (ms): Min: 0.0, Avg: 2.9, Max: 3.5, Diff: 3.5, Sum: 23.5]

将当前分区中存活的对象拷贝到新的分区。

>[Termination (ms): Min: 0.0, Avg: 0.6, Max: 0.8, Diff: 0.8, Sum: 5.0]
[Termination Attempts: Min: 1, Avg: 1.0, Max: 1, Diff: 0, Sum: 8]

当一个垃圾收集线程完成任务后，会尝试帮助其他垃圾线程完成任务，min表示该垃圾收集线程什么时候尝试terminatie，max表示该垃圾收集回收线程什么时候真正terminated。
>[GC Worker Other (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.2, Sum: 0.4]
[GC Worker Total (ms): Min: 4.2, Avg: 4.3, Max: 4.4, Diff: 0.2, Sum: 34.7]
[GC Worker End (ms): Min: 196.7, Avg: 196.8, Max: 196.8, Diff: 0.1]

GC Worker Other->垃圾收集线程在完成其他任务的时间
GC Worker Total->展示每个垃圾收集线程的最小、最大、平均、差值和总共时间。
GC Worker End->min表示最早结束的垃圾收集线程结束时该JVM启动后的时间；max表示最晚结束的垃圾收集线程结束时该JVM启动后的时间。

>[Code Root Fixup: 0.0 ms]  释放用于管理并行垃圾收集活动的数据结构
[Code Root Purge: 0.0 ms] 清理更多的数据结构
[Clear CT: 0.0 ms] 清理card table
[Other: 0.7 ms] -> 不确定此处意义
[Choose CSet: 0.0 ms] 选择要进行回收的分区放入CSet, 垃圾最多的优先
[Ref Proc: 0.3 ms] 处理java中的各类引用
[Ref Enq: 0.0 ms] 遍历引用，将不能回收的放入pending列表
[Redirty Cards: 0.0 ms] 在回收过程中被修改的card会被标记为Dirty
[Humongous Register: 0.1 ms] 巨型对象可以在新生代收集的时候被回收
[Humongous Reclaim: 0.0 ms] 回收巨型对象，释放分区的时间
[Free CSet: 0.0 ms] 将要释放的分区还回到free列表。
[Eden: 25600.0K(25600.0K)->0.0B(27648.0K) Survivors: 0.0B->4096.0K Heap: 32305.0K(512.0M)->12168.7K(512.0M)]
[Times: user=0.01 sys=0.01, real=0.01 secs]

### Concurrent Marking


>[GC pause (G1 Humongous Allocation) (young) (initial-mark), 0.0009794 secs]

并发垃圾回收阶段开始,initial-mark阶段是作为新生代垃圾收集中的一部分存在的

>[GC concurrent-root-region-scan-start]
[GC concurrent-root-region-scan-end, 0.0000657 secs]

根分区扫描,扫描新的survivor分区，找到这些分区内的对象指向当前分区的引用.

>[GC concurrent-mark-start]
[GC concurrent-mark-end, 0.0008221 secs]

并发标记,与应用程序并行运行，线程数量为总线程数的25%。
标记存活对象,计算存活对象占整个分区的比例
>[GC remark [Finalize Marking, 0.0001578 secs] [GC ref-proc, 0.0000863 secs] [Unloading, 0.0006849 secs], 0.0013529 secs]
>
Remark阶段（STW）,重新标记STAB队列中的对象。
处理引用，卸载无用的类。

>[GC cleanup 349M->345M(512M), 0.0005762 secs]
[Times: user=0.00 sys=0.00, real=0.00 secs]

清理阶段（STW）, 清理没有存活对象的老年代分区和巨型对象分区，老年代分区按存活率排序

>[GC concurrent-cleanup-start]
[GC concurrent-cleanup-end, 0.0000128 secs]

并发清理阶段，完成清理阶段剩余的的工作。

### Mixed GC

>GC pause (G1 Evacuation Pause) (mixed), 0.0036667 secs]
>
混合回收会在并发标记结束后进行
日志与YoungGC相同，只有第一行不同，mixed表示此次GC为混合垃圾回收
年轻代和老年代会同时被回收
并发标记后，老年代中百分百为垃圾的内存分段被回收了，部分为垃圾的内存分段被计算了出来，老年代中的内存分段默认分8次回收，G1会优先回收垃圾多的内存分段。垃圾占内存分段比例越高的，越会被先回收。

### Full GC

>[Full GC (Allocation Failure)  460M->332M(512M), 0.0259303 secs]
[Eden: 0.0B(25600.0K)->0.0B(39936.0K) Survivors: 0.0B->0.0B 
Heap: 460.9M(512.0M)->332.4M(512.0M)], 
[Metaspace: 3081K->3081K(1056768K)]
[Times: user=0.03 sys=0.00, real=0.03 secs]

堆内存空间不足以分配新对象，触发Full GC，单线程，STW时间长，G1应避免FullGC的发生。
日志与serialGC类似，不再重复。







