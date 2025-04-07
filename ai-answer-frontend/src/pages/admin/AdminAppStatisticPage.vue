<template>
  <div>
    <h1>热门应用统计</h1>
    <v-chart :option="options" style="height: 300px" />

    <h1>应用结果统计</h1>
    <div class="searchBar">
      <a-input-search
        v-model:value="appId"
        :style="{ width: '320px' }"
        placeholder="输入 appId"
        button-text="搜索"
        enter-button
        @search="getAnswerResultCount"
      />
    </div>
    <div style="margin-bottom: 16px" />

    <v-chart :option="resultOption" style="height: 300px" />
  </div>
</template>

<script setup lang="ts">
import VChart from 'vue-echarts'
import 'echarts'
import {
  getAppAnswerCountUsingGet,
  getAppAnswerResultCountUsingGet,
} from '@/api/appStatisticController'
import { onMounted, ref, watch, watchEffect } from 'vue'

const appId = ref('')

const options = ref({
  xAxis: {
    type: 'category',
    data: [] as string[],
    name: '应用id',
  },
  yAxis: {
    type: 'value',
    name: '做题用户数',
  },
  series: [
    {
      data: [] as number[],
      type: 'bar',
    },
  ],
})

const resultOption = ref({
  title: {
    // text: '回答结果统计',
    left: 'center',
  },
  tooltip: {
    trigger: 'item',
  },
  legend: {
    orient: 'vertical',
    left: 'left',
  },
  series: [
    {
      type: 'pie',
      radius: '50%',
      data: [{ value: 0, name: '' }],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)',
        },
      },
    },
  ],
})

const tempAppId = ref()

const getAnswerCount = async () => {
  const res = await getAppAnswerCountUsingGet()
  if (res.data.code === 0 && res.data.data) {
    tempAppId.value = res.data.data[0].appId?.toString() || ''
    res.data.data.forEach((item: API.AppAnswerCountDTO) => {
      options.value.xAxis.data.push(item.appId?.toString() || '')
      options.value.series[0].data.push(item.answerCount ?? 0)
    })
  }
}

const getAnswerResultCount = async () => {
  const res = await getAppAnswerResultCountUsingGet({ appId: tempAppId.value })
  if (res.data.code === 0 && res.data.data) {
    const result = res.data.data.map((item: API.AppAnswerResultCountDTO) => {
      return {
        value: item.resultNameCount || 0,
        name: item.resultName || '',
      }
    })

    resultOption.value.series[0].data = result
  }
}

watchEffect(() => {
  tempAppId.value = appId.value
})

onMounted(async () => {
  await getAnswerCount()
  getAnswerResultCount()
})
</script>

<style scoped>
</style>