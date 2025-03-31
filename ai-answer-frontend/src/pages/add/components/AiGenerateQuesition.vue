<template>
  <div>
    <a-button
      @click="
        () => {
          openDrawer = true
        }
      "
      type="primary"
      ghost
      style="margin: 0 0 10px 5px"
      >AI生成题目</a-button
    >
    <a-drawer
      v-model:open="openDrawer"
      class="custom-class"
      root-class-name="root-class-name"
      :root-style="{ color: 'blue' }"
      style="color: red"
      title="应用信息"
      placement="right"
    >
      <a-form @submit="onAiGenerate">
        <a-form-item label="应用名称">
          <a-input v-model:value="appInfo!.appName" disabled> </a-input>
        </a-form-item>
        <a-form-item label="应用描述">
          <a-input v-model:value="appInfo!.appDesc" disabled> </a-input>
        </a-form-item>
        <a-form-item label="应用类型">
          <a-input v-model:value="APP_TYPE_MAP[appInfo?.appType || 0]" disabled> </a-input>
        </a-form-item>
        <a-form-item label="题目数量">
          <a-input
            placeholder="请输入题目数量"
            v-model:value="questionAiCreateRequest.questionNum"
          ></a-input
        ></a-form-item>
        <a-form-item label="选项数量"
          ><a-input
            placeholder="请输入选项数量"
            v-model:value="questionAiCreateRequest.optionNum"
          ></a-input>
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button type="primary" html-type="submit">点击生成</a-button>
            <a-button type="primary" @click="doSSESubmit">实时生成</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-drawer>
    <div v-if="openLoad" class="example">
      <a-spin tip="AI生成中" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watchEffect } from 'vue'
import { APP_TYPE_MAP } from '@/constant/App'
import { aiCreateQuestionUsingPost } from '@/api/questionController'
import { message } from 'ant-design-vue'
import { BASE_URL } from '@/config/apiConfig'

interface Props {
  appInfo: API.AppVO
  onSuccess: (data: API.QuestionContentDTO[]) => void
  onSSESuccess: (data: API.QuestionContentDTO) => void
  onSSEFinish: (event: unknown) => void
}

const props = withDefaults(defineProps<Props>(), {})

const openDrawer = ref(false)
const openLoad = ref(false)

const questionAiCreateRequest = ref<API.QuestionAiCreateRequest>({
  appName: props.appInfo.appName,
  appDesc: props.appInfo.appDesc,
  appType: APP_TYPE_MAP[props.appInfo.appType || 0],
  questionNum: 1,
  optionNum: 4,
})

const doSSESubmit = () => {
  openDrawer.value = false
  openLoad.value = true
  const params = new URLSearchParams({
    appName: questionAiCreateRequest.value.appName || '',
    appDesc: questionAiCreateRequest.value.appDesc || '',
    appType: questionAiCreateRequest.value.appType || '',
    questionNum: (questionAiCreateRequest.value.questionNum || 0).toString(),
    optionNum: (questionAiCreateRequest.value.optionNum || 0).toString(),
  })

  const sseUrl = `${BASE_URL}/api/question/ai/generate/sse?${params.toString()}`
  // 创建 SSE 请求
  const eventSource = new EventSource(sseUrl)

  // 接收消息
  eventSource.onmessage = function (event) {
    props.onSSESuccess(JSON.parse(event.data))
    // console.log('数据数据', event.data)
  }
  // 生成结束，关闭连接
  eventSource.onerror = function (event) {
    if (event.eventPhase === EventSource.CLOSED) {
      eventSource.close()
      openLoad.value = false
      props.onSSEFinish(event)
    }
  }
}

watchEffect(() => {
  questionAiCreateRequest.value.appName = props.appInfo.appName
  questionAiCreateRequest.value.appDesc = props.appInfo.appDesc
  questionAiCreateRequest.value.appType = APP_TYPE_MAP[props.appInfo.appType || 0]
})

const onAiGenerate = async () => {
  openLoad.value = true
  const res = await aiCreateQuestionUsingPost(questionAiCreateRequest.value)
  if (res.data.code === 0 && res.data.data) {
    // emit('aiGenerate', res.data.data)
    props.onSuccess(res.data.data)
    // questionContent.value = res.data.data
    openDrawer.value = false
    openLoad.value = false
    message.success(`题目生成成功，新增${questionAiCreateRequest.value.questionNum}道题目`)
  } else {
    openLoad.value = false
    message.error('生成失败')
  }
}
</script>

<style scoped>
.example {
  position: absolute;
  top: 40%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  border-radius: 4px;
  margin-bottom: 20px;
  padding: 30px 50px;
  margin: 20px 0;
  z-index: 9999;
}
</style>
