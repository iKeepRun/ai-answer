<template>
  <div class="answerResultPage">
    <a-card hoverable title="答题结果">
      <a-row :gutter="100">
        <a-col flex="1" wrap>
          <h1>{{ userAnswerVO?.resultName }}</h1>
          <p>结果描述：{{ userAnswerVO?.resultDesc }}</p>
          <p>结果id：{{ userAnswerVO?.resultId }}</p>
          <p>结果评分：{{ userAnswerVO?.resultScore }}</p>
          <p>我的答案：{{ userAnswerVO?.choices }}</p>
          <p>应用id：{{ userAnswerVO?.appId }}</p>
          <p>应用类型：{{ APP_TYPE_MAP[userAnswerVO?.appType || 0] }}</p>
          <p>评分策略：{{ SCORING_STRATEGY_MAP[userAnswerVO?.scoringStrategy || 0] }}</p>
          <p>答题人：{{ userAnswerVO?.user?.userName }}</p>
          <p>答题时间：{{ dayjs(userAnswerVO?.createTime).format('YYYY-MM-DD HH:mm:ss') }}</p>

          <a-button type="primary" :href="`/answer/do/${userAnswerVO?.appId}`">去答题</a-button>
        </a-col>
        <a-col flex="400px">
          <img
            src="https://k.sinaimg.cn/n/sinakd20110/560/w1080h1080/20230930/915d-f3d7b580c33632b191e19afa0a858d31.jpg/w700d1q75cms.jpg"
            alt="结果图片"
            style="width: 320px"
          />
          <!-- <img :src="userAnswerVO?.resultPicture" alt="结果图片" style="width: 320px" /> -->
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import dayjs from 'dayjs'
import { useRoute } from 'vue-router'
import { getUserAnswerVoByIdUsingGet } from '@/api/userAnswerController'
import { ref, onMounted } from 'vue'
import { APP_TYPE_MAP, SCORING_STRATEGY_MAP } from '@/constant/App'
const route = useRoute()
console.log('路由参数', route.params.id)

// const answerId = Number(route.params.id)
const answerIdParam = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id

const answerId = BigInt(answerIdParam)

const userAnswerVO = ref<API.UserAnswerVO>()
const loadData = async () => {
  const res = await getUserAnswerVoByIdUsingGet({ id: answerId })
  if (res.data.data) {
    userAnswerVO.value = res.data.data
  }
}

onMounted(() => {
  loadData()
})
//当前得选项
</script>

<style scoped></style>
