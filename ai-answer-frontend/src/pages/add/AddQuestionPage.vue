<template>
  <div class="add-question-page">
    <h1>创建题目</h1>
    <h2>应用名称: {{ appInfo?.appName }}</h2>

    <a-form class="form" @submit="onFinish">
      <a-form-item label="题目列表">
        <a-space>
          <a-button
            @click="() => addQuestion(questionContent.length)"
            type="primary"
            ghost
            style="margin-bottom: 10px"
            >底部添加题目</a-button
          >
          <AiGenerateQuesition
            :appInfo="appInfo || {}"
            :onSuccess="childBack"
            :onSSESuccess="childSSEBack"
            :onSSEFinish="childFinish"
          />
        </a-space>

        <div v-for="(item, index) in questionContent" :key="index">
          <a-form-item :label="'题目' + (index + 1)">
            <a-space>
              <a-button
                @click="addQuestion(index + 1)"
                style="color: #87d068; border-color: #87d068"
                >添加题目</a-button
              >
              <a-button danger @click="removeQuestion(index)">删除题目</a-button>
            </a-space>
          </a-form-item>
          <a-form-item :label="`题目${index + 1}标题`">
            <a-input v-model:value="item.title"></a-input>
          </a-form-item>
          <a-form-item :label="'题目' + (index + 1) + '选项列表'" labelAlign="right">
            <a-button
              @click="() => addOption(index, item.options?.length || 0)"
              style="margin-bottom: 10px"
              type="primary"
              ghost
              >底部添加选项</a-button
            >
            <div v-for="(option, optionIndex) in item.options" :key="optionIndex">
              <a-form-item>
                <a-button disabled size="small" style="margin-right: 48px; color: black"
                  >选项{{ optionIndex + 1 }}</a-button
                >
                <a-space :size="[12, 'small']" wrap>
                  <a-button danger size="small" @click="() => removeOption(index, optionIndex)"
                    >删除</a-button
                  >
                  <a-button size="small" @click="() => resetOption(index, optionIndex)"
                    >重置</a-button
                  >
                </a-space>

                <div style="margin: 10px 0"></div>
                <a-form-item label="选项key" :labelCol="{ span: 2 }"
                  ><a-input v-model:value="option.key"></a-input
                ></a-form-item>
                <a-form-item label="选项值" :labelCol="{ span: 2 }"
                  ><a-input v-model:value="option.value"></a-input
                ></a-form-item>
                <a-form-item label="选项结果" :labelCol="{ span: 2 }"
                  ><a-input v-model:value="option.result"></a-input
                ></a-form-item>
                <a-form-item label="选项分数" :labelCol="{ span: 2 }"
                  ><a-input v-model:value="option.score"></a-input
                ></a-form-item>
              </a-form-item>
            </div>
          </a-form-item>
        </div>
      </a-form-item>
      <a-form-item>
        <a-button
          type="primary"
          html-type="submit"
          :style="{ width: '200px', margin: '0 auto', display: 'block' }"
          >提交</a-button
        >
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  listQuestionVoByPageUsingPost,
  editQuestionUsingPost,
  addQuestionUsingPost,
} from '@/api/questionController'
import { getAppVoByIdUsingGet } from '@/api/appController'
import { message } from 'ant-design-vue'
import AiGenerateQuesition from './components/AiGenerateQuesition.vue'

const route = useRoute()
const router = useRouter()
const queryAppParams = route.params

const oldQuestion = ref<API.QuestionVO>({})
const questionContent = ref<API.QuestionContentDTO[]>([])

const appInfo = ref<API.AppVO>()
const loadAppInfo = async () => {
  const res = await getAppVoByIdUsingGet({ id: +queryAppParams.id })
  if (res.data.data) {
    appInfo.value = res.data.data
  }
}

//获取题目
const loadData = async () => {
  if (!queryAppParams.id) {
    return
  }
  const res = await listQuestionVoByPageUsingPost({
    appId: +queryAppParams.id,
    current: 1,
    pageSize: 1,
  })
  if (res.data.data?.records) {
    oldQuestion.value = res.data.data.records[0]
    questionContent.value = oldQuestion.value.questionContent ?? []
  }
}

const addQuestion = (index: number) => {
  questionContent.value.splice(index, 0, { title: '', options: [] })
}

const removeQuestion = (index: number) => {
  questionContent.value.splice(index, 1)
}
const addOption = (questionIndex: number, index: number) => {
  questionContent.value[questionIndex].options?.splice(index, 0, {
    key: '',
    value: '',
    result: '',
    score: 0,
  })
}

const removeOption = (questionIndex: number, index: number) => {
  questionContent.value[questionIndex].options?.splice(index, 1)
}

const resetOption = (questionIndex: number, index: number) => {
  questionContent.value[questionIndex].options?.splice(index, 1, {
    key: '',
    value: '',
    result: '',
    score: 0,
  })
}
const onFinish = async () => {
  //参数校验
  if (questionContent.value.length === 0 || queryAppParams.id === undefined) {
    return
  }
  let res
  if (oldQuestion.value?.id) {
    res = await editQuestionUsingPost({
      id: Number(oldQuestion.value.id),
      questionContent: questionContent.value,
    })
  } else {
    res = await addQuestionUsingPost({
      appId: +queryAppParams.id,
      questionContent: questionContent.value,
    })
  }
  if (res.data.code === 0) {
    message.success('操作成功')
    setTimeout(() => {
      router.push(`/app/detail/${queryAppParams.id}`)
    }, 3000)
  } else {
    message.error('操作失败' + res.data.message)
  }
}

const childBack = (data: API.QuestionContentDTO[]) => {
  questionContent.value.splice(questionContent.value.length, 0, ...data)
}

const childSSEBack = (data: API.QuestionContentDTO) => {
  questionContent.value = [...questionContent.value, data]
}

const childFinish = () => {
  message.success('生成成功')
}
// watchEffect(() => {
//   questionContent.value = oldQuestion.value?.questionContent ?? []
// })

onMounted(() => {
  loadData()
  loadAppInfo()
})
</script>

<style scoped>
.add-question-page {
  padding: 0 200px;
  margin: 0 auto;
}

/* .ant-input {
  width: 1000px;
} */
</style>
