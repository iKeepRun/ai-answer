<template>
  <div>{{ userAnswerVO }}</div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'
import { getUserAnswerVoByIdUsingGet } from '@/api/userAnswerController'
import { ref, onMounted } from 'vue'
const route = useRoute()
const answerId = Number(route.params.id)

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

<style scoped>
</style>