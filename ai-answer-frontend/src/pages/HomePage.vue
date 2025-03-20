<template>
  <div class="homePage">
    <a-input-search
      v-model:value="queryParams.appName"
      placeholder="请输入你想要得应用"
      enter-button
      style="width: 500px"
      size="large"
      @search="onSearch"
    />
    <a-config-provider :locale="zhCN">
      <a-list
        :grid="{ gutter: [10, 10], xs: 1, sm: 2, md: 4, lg: 4, xl: 4, xxl: 5 }"
        :data-source="data"
        :pagination="pagination"
      >
        <template #renderItem="{ item }">
          <a-list-item>
            <RouterLink :to="`/app/detail/${item.id}`">
              <AppCard :item="item" />
            </RouterLink>
          </a-list-item>
        </template>
      </a-list>
    </a-config-provider>
  </div>
</template>

<script setup lang="ts">
import { listAppVoByPageUsingPost } from '@/api/appController'
import { onMounted, reactive, ref, computed } from 'vue'
import AppCard from '@/components/AppCard.vue'
import zhCN from 'ant-design-vue/es/locale/zh_CN'

const total = ref(0)

const data = ref<API.AppVO[]>([])
const queryParams = reactive<API.AppQueryRequest>({
  current: 1,
  pageSize: 10,
})

const pagination = computed(() => {
  return {
    current: queryParams.current,
    pageSize: queryParams.pageSize,
    total: total.value,
    showTotal: () => `共${total.value}条`,
    showSizeChanger: true,
    onChange: handleTableChange,
  }
})

const getAppList = async () => {
  const res = await listAppVoByPageUsingPost(queryParams)
  if (res.data.data) {
    total.value = res.data.data.total ?? 0
    data.value = res.data.data.records ?? []
  }
}

const handleTableChange = (page: number, pageSize: number) => {
  queryParams.current = page
  queryParams.pageSize = pageSize
  console.log('分页参数', queryParams)
  getAppList()
}

const onSearch = () => {
  getAppList()
}

onMounted(() => {
  getAppList()
})
</script>

<style scoped>
.homePage {
  padding: 0 100px 100px 100px;
}
.ant-input-group-wrapper {
  display: flex;
  margin: 100px auto 80px;
}
</style>
