<template>
<div>
  <!-- 测试 ECharts 框架 -->
  <div id="myChart" :style="{width: '100%', height: '500px'}"></div>
</div>
</template>

<!-- 测试 ECharts 框架 -->
<script>
  export default {
    name: 'Echarts',
    // 数据定义
    data() {
      return {
        /*
          绘图所需的数据，格式如下：
            [
              {value: 1048, name: 'name1'},
              {value: 735, name: 'name2'}
            ]
        */
        showData: null
      }
    },
    // 钩子函数，用在初始化页面完成后，再对 dom 节点进行相关操作
    mounted(){
      // 发送请求，获得数据
      axios.get('http://localhost:8088/fruit/listPieVO').then((response) => {
        // 后端返回多个对象的数组
        this.showData = response.data;

        // 基于准备好的dom，初始化echarts实例，注意：绘制图表必须放到 axios 的回调函数里面，即保证在数据更新后再绘图
        let myChart = this.$echarts.init(document.getElementById('myChart'))
        // 绘制图表
        myChart.setOption({
          tooltip: {
            trigger: 'item'
          },
          legend: {
            top: '5%',
            left: 'center'
          },
          series: [
            {
              name: '访问来源',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
              },
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '40',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              /* 笔记：引用定义好的显示数据 */
              data: this.showData
            }
          ]
        });
      });
    }
  }
</script>
