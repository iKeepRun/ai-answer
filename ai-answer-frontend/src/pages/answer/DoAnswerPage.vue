<template>
  <div class="doAnswerPage">
    <a-space style="margin-bottom: 10px">
      <a-button
        type="primary"
        size="small"
        @click="
          () => {
            questionIndex--
            currentOption = answerList[questionIndex] ? answerList[questionIndex] : ''
          }
        "
        :disabled="questionIndex <= 0"
        >上一题</a-button
      >
      <a-button
        type="primary"
        size="small"
        :disabled="!currentOption || questionIndex >= data.length - 1"
        @click="
          () => {
            questionIndex++
            currentOption = answerList[questionIndex] ? answerList[questionIndex] : ''
          }
        "
        >下一题</a-button
      >

      <a-button
        v-if="data.length - 1 === questionIndex && currentOption"
        type="primary"
        size="small"
        >查看结果</a-button
      >
    </a-space>
    <!-- 加载状态 -->
    <a-spin v-if="loading" tip="加载中...">
      <a-empty description="正在加载题目..." />
    </a-spin>

    <a-card v-else>
      <h1>{{ currentQuestion.title }}</h1>
      <div style="display: flex; flex-direction: column">
        <a-radio-group v-model:value="currentOption" @change="doChange">
          <a-radio
            v-for="(item, index) in formattedOptions"
            :key="index"
            :value="item.value"
            style="display: flex; margin-bottom: 10px"
          >
            {{ item.value }}.&nbsp;{{ item.label }}
          </a-radio>
        </a-radio-group>
      </div>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { listQuestionVoByPageUsingPost } from '@/api/questionController'
import { computed, onMounted, reactive, ref, watchEffect } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
//当前得选项
const currentOption = ref('')
//所选答案得集合
const answerList = ref<string[]>([])

const appId = Number(route.params.appId)

//查询参数
const queryParams = reactive<API.QuestionQueryRequest>({
  current: 1,
  pageSize: 10,
  appId,
})

// 题目列表
const data = ref<API.QuestionContentDTO[]>([])
//题目索引
const questionIndex = ref(0)
const loading = ref(true)

//当前题目
const currentQuestion = ref<API.QuestionContentDTO>({})
console.log('ddddd', currentQuestion.value.options)

// 格式化 options 为 a-radio-group 所需的格式
const formattedOptions = computed(() => {
  if (currentQuestion.value.options) {
    return currentQuestion.value.options.map((item) => ({
      label: item.value, // 假设后端返回的 value 是选项的显示文本
      value: item.key, // 假设后端返回的 key 是选项的值
    }))
  }
  return [] // 如果没有 options，返回空数组
})

const getQuestionList = async () => {
  try {
    const res = await listQuestionVoByPageUsingPost(queryParams)
    if (res.data.data?.records) {
      data.value = res.data.data.records[0].questionContent || []
      currentQuestion.value = data.value[questionIndex.value]
    }
  } catch (error) {
    console.error('获取题目列表失败:', error)
  } finally {
    loading.value = false
  }
}

watchEffect(() => {
  currentQuestion.value = data.value[questionIndex.value]
})

const doChange = () => {
  answerList.value[questionIndex.value] = currentOption.value
}

onMounted(() => {
  getQuestionList()
})
</script>

<style scoped>
.doAnswerPage {
  padding: 0 100px;
}
</style>
