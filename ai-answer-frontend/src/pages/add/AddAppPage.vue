<template>
  <div class="add-app-page">
    <h1 style="text-align: center">{{ route.name }}</h1>
    <a-form
      :model="formState"
      style="max-width: 400px; margin: 0 auto"
      labelAlign="right"
      :label-col="{ span: 6 }"
      @finish="onFinish"
    >
      <a-form-item
        label="应用名称"
        name="appName"
        :rules="[{ required: true, message: '请输入应用名' }]"
      >
        <a-input v-model:value="formState.appName" />
      </a-form-item>

      <a-form-item
        label="应用详情"
        name="appDesc"
        :rules="[{ required: true, message: '请输入应用详情' }]"
      >
        <a-input v-model:value="formState.appDesc" />
      </a-form-item>

      <a-form-item label="应用图标" name="appIcon">
        <a-input v-model:value="formState.appIcon" />
      </a-form-item>

      <a-form-item label="应用类型" name="appType">
        <a-select
          ref="select"
          v-model:value="formState.appType"
          style="width: 120px"
          @change="handleAppTypeChange"
        >
          <a-select-option v-for="(value, key) in APP_TYPE_MAP" :key="key" :value="Number(key)">
            {{ value }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="评分策略" name="scoringStrategy">
        <a-select
          ref="select"
          v-model:value="formState.scoringStrategy"
          style="width: 120px"
          @change="handleAppTypeChange"
        >
          <a-select-option
            v-for="(value, key) in SCORING_STRATEGY_MAP"
            :key="key"
            :value="Number(key)"
          >
            {{ value }}</a-select-option
          >
        </a-select>
      </a-form-item>
      <a-form-item :wrapperCol="{ offset: 6 }">
        <a-button type="primary" html-type="submit" size="large" style="width: 200px"
          >确认</a-button
        >
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { ref, watchEffect } from 'vue'
import { addAppUsingPost, getAppVoByIdUsingGet, editAppUsingPost } from '@/api/appController'
import { message } from 'ant-design-vue'
import { useRouter, useRoute } from 'vue-router'
import { APP_TYPE_MAP, SCORING_STRATEGY_MAP } from '@/constant/App'

const router = useRouter()
const route = useRoute()
const formState = ref<API.AppAddRequest>({})

// const oldFormState=ref
const queryParams = route.params

const loadData = async () => {
  if (queryParams.id) {
    const res = await getAppVoByIdUsingGet(queryParams)
    console.log('xxxxx', res)
    if (res.data.data) {
      formState.value = res.data.data
    }
  } else {
    return
  }
}

watchEffect(() => {
  loadData()
})

const onFinish = async () => {
  let res
  if (queryParams.id) {
    res = await editAppUsingPost(formState.value)
  } else {
    res = await addAppUsingPost(formState.value)
  }
  if (res.data.code === 0 && res.data.data) {
    message.success('操作成功，即将跳转到详情页')
    setTimeout(() => {
      router.push(`/app/detail/${queryParams.id ?? res.data.data}`)
    }, 3000)
  } else {
    message.error('创建失败')
  }
}

const handleAppTypeChange = (value: string) => {
  console.log(`selected ${value}`)
}
</script>

<style scoped>
.add-app-page {
}
</style>
