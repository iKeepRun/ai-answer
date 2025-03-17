<template>
  <div class="appDetailPage">
    <a-card hoverable style="width: 60%; margin: 0 auto">
      <a-row>
        <a-col flex="auto" class="content-wrapper">
          <h1>{{ data.appName }}</h1>
          <p>{{ data.appDesc }}</p>
          <p>应用类型: {{ APP_TYPE_MAP[data.appType] }}</p>
          <p>评分策略: {{ SCORING_STRATEGY_MAP[data.scoringStrategy] }}</p>
          <p>
            作者:
            <a-avatar :src="data.user?.userAvatar" :size="24" style="margin: 0 4px 0 4px" />
            {{ data.user?.userName }}
          </p>
          <p>创建时间: {{ dayjs(data.createTime).format('YYYY-MM-DD HH:mm:ss') }}</p>
          <a-button type="primary">开始答题</a-button>
          <a-button style="background: #f2f4f5; margin-left: 10px">分享应用</a-button>
        </a-col>
        <a-col flex="300px">
          <a-image :src="data.appIcon" alt="" style="width: 300px; height: 300px" />
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive } from 'vue'
import { useRoute } from 'vue-router'
import { getAppVoByIdUsingGet } from '@/api/appController'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import { APP_TYPE_MAP, SCORING_STRATEGY_MAP } from '@/constant/App'

const route = useRoute()
const queryId = route.params

const data = reactive<API.AppVO>({})
const appDetailData = async () => {
  const res = await getAppVoByIdUsingGet(queryId)
  if (res.data.data) {
    console.log(res.data.data)
    Object.assign(data, res.data.data)
    message.success('成功')
  } else {
    message.error('失败')
  }
}
onMounted(() => {
  appDetailData()
})
</script>

<style scoped>
.appDetailPage {
  padding: 20px 0 60px 0;
}

.appDetailPage .content-wrapper > * {
  margin-bottom: 22px;
}
</style>