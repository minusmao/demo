<!-- 使用 ECharts 框架 -->
<template>
  <div class="about">
    <div id="myChart" :style="{width: '100%', height: '500px'}"></div>
  </div>
</template>

<script>
  export default {
    name: 'Echarts',
    // 数据定义
    data() {
      return {
        /*
          绘图所需的数据，格式如下：
            {
              names: ['name1', 'name2'],
              values: [
                {
                  value: value1,
                  itemStyle: {color: "#3fb1e3"}
                },
                {
                  value: value2,
                  itemStyle: {color: "#3fb1e3"}
                }
              ]
            }
        */
        showData: null
      }
    },
    // 钩子函数，用在初始化页面完成后，再对 dom 节点进行相关操作
    mounted() {
      // 发送请求，获得数据
      axios.get('http://localhost:8088/fruit/getBarVO').then((response) => {
        // 后端返回数据
        this.showData = response.data;

        // 基于准备好的dom，初始化echarts实例
        let myChart = this.$echarts.init(document.getElementById('myChart'))
        // 绘制图表
        myChart.setOption({
          title: {
            text: '水果销量统计',
            left: 'center',
            top: 20,
            textStyle: {
              color: '#555555'
            }
          },
          tooltip: {},
          // 柱状图的横坐标名称数据
          xAxis: {
            data: this.showData.names
          },
          yAxis: {},
          // 柱状图每个柱子的数据
          series: [{
            name: '销量',
            type: 'bar',
            data: this.showData.values
          }]
        });
      });
    }
  }
</script>