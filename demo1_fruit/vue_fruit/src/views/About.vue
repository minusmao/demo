<template>
  <div class="about">
    <h1>This is an about page</h1>
    <!-- 测试 ECharts 框架 -->
    <div id="myChart" :style="{ width: '800px', height: '600px' }"></div>
    <div id="myLine" :style="{ width: '800px', height: '600px' }"></div>
  </div>
</template>

<!-- 测试 ECharts 框架 -->
<script>
export default {
  name: "Echarts",
  mounted() {
    // 基于准备好的dom，初始化echarts实例
    let myChart = this.$echarts.init(document.getElementById("myChart"));
    // 绘制图表
    myChart.setOption({
      title: {
        text: "水果销量统计",
        left: "center",
        top: 20,
        textStyle: {
          color: "#555555",
        },
      },
      tooltip: {},
      xAxis: {
        data: ["苹果", "香蕉", "橘子", "火龙果", "葡萄", "西瓜"],
      },
      yAxis: {},
      series: [
        {
          name: "销量",
          type: "bar",
          data: [
            {
              value: 333,
              itemStyle: {
                color: "#3fb1e3",
              },
            },
            {
              value: 133,
              itemStyle: {
                color: "#c4ebad",
              },
            },
            {
              value: 99,
              itemStyle: {
                color: "#c4ebad",
              },
            },
            {
              value: 222,
              itemStyle: {
                color: "#6be6c1",
              },
            },
            {
              value: 399,
              itemStyle: {
                color: "#3fb1e3",
              },
            },
            {
              value: 123,
              itemStyle: {
                color: "#c4ebad",
              },
            },
          ],
        },
      ],
    });

    // 创建 myLine 的 Echarts 对象
    let myLine = this.$echarts.init(document.getElementById("myLine"));
    // 图形信息
    let option = {
      title: {
        text: "ECharts 入门示例",
      },
      legend: {
        data: ["name1", "name2"],
      },
      xAxis: {
        type: "category",
        data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
      },
      yAxis: {
        type: "value",
      },
      series: [
        {
          name: "name1",
          data: [150, 230, 224, 218, 135, 147, 260],
          type: "line",
        },
        {
          name: "name2",
          data: [218, 135, 147, 150, 260, 230, 224],
          type: "line",
        },
      ],
    };
    // 绘制图形
    myLine.setOption(option);
  },
};
</script>
