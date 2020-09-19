package com.icocos.bigdata.stream

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

object StreamWordCountScala {

  def main(args: Array[String]): Unit = {

    // 1. 获取上下文
    val env = StreamExecutionEnvironment.getExecutionEnvironment;

    // 2. 读入数据
    var host = "localhost"
    try {
      val tool = ParameterTool.fromArgs(args)
      host = tool.get("host")
    } catch {
      case e: Exception =>
        System.err.println("域名或IP未设置，使用默认localhost")
    }
    var port: Int = 9999
    try {
      val tool: ParameterTool = ParameterTool.fromArgs(args)
      port = tool.getInt("port")
    } catch {
      case e: Exception =>
        System.err.println("端口未设置，使用默认端口9999")
    }
    val text = env.socketTextStream(host, port)

    // 导入隐式转换
    import org.apache.flink.api.scala._

    // 3. 转换操作
    text.flatMap(_.split(","))
        .map((_,1))
        .keyBy(0)
        .sum(1)
        .print()
        .setParallelism(1); // timeWindow(Time.seconds(5))

    // 打印
    text.print();

  }

}
