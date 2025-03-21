<template>
  <div class="adminQuestionPage">
    <!-- 条件选择 -->
    <a-form layout="inline" :model="queryParams" style="margin-left: 200px" @finish="onSearch">
      <a-form-item>
        <a-input v-model:value="queryParams.appId" placeholder="请输入应用ID">
          <template #prefix><UserOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
        </a-input>
      </a-form-item>
      <a-form-item>
        <a-input v-model:value="queryParams.userId" placeholder="请输入用户ID">
          <template #prefix><LockOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
        </a-input>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit"> 搜索 </a-button>
      </a-form-item>
    </a-form>
    <div style="margin: 20px 0"></div>
    <!-- 表格 -->
    <a-config-provider :locale="zhCN">
      <a-table
        :columns="columns"
        :data-source="data"
        :style="{ width: '90%', margin: '0 auto' }"
        :pagination="pagination"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'questionContent'">
            <span v-for="item in JSON.parse(record.questionContent)" :key="item.title">
              {{ item }}
              <br />
            </span>
          </template>

          <template v-else-if="column.dataIndex === 'createTime'">
            {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
          </template>
          <template v-else-if="column.dataIndex === 'updateTime'">
            {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button type="primary" @click="handleEditorClick">编辑</a-button>
              <a-button
                type="primary"
                danger
                @click="
                  () => {
                    handleDeleteClick(record.id)
                  }
                "
                >删除</a-button
              >
            </a-space>
          </template>
        </template>
      </a-table>
    </a-config-provider>
  </div>
</template>
<script lang="ts" setup>
import { deleteQuestionUsingPost, listQuestionByPageUsingPost } from '@/api/questionController'
import { onMounted, reactive, ref, computed } from 'vue'
import zhCN from 'ant-design-vue/es/locale/zh_CN'
import message from 'ant-design-vue/es/message'
import dayjs from 'dayjs'

const total = ref(0)
const data = ref<API.Question[]>([])

const queryParams = reactive<API.QuestionQueryRequest>({
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
  }
})

const getQuestionList = async () => {
  const res = await listQuestionByPageUsingPost(queryParams)
  console.log('接口参数', res.data.data)
  if (res.data.data) {
    total.value = res.data.data.total ?? 0
    data.value = res.data.data.records ?? []
  }
}

const handleTableChange = (pagination: any) => {
  console.log(pagination)
  queryParams.current = pagination.current
  queryParams.pageSize = pagination.pageSize
  console.log('分页参数', queryParams)
  getQuestionList()
}
const onSearch = () => {
  getQuestionList()
}

const handleEditorClick = () => {
  console.log('编辑')
}

const handleDeleteClick = async (id: number) => {
  const res = await deleteQuestionUsingPost({ id })
  if (res.data.data) {
    getQuestionList()
  } else {
    message.error('删除失败')
  }
}

onMounted(() => {
  getQuestionList()
})

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
  },
  {
    title: '题目',
    dataIndex: 'questionContent',
  },
  {
    title: '应用id',
    dataIndex: 'appId',
  },
  {
    title: '创建用户id',
    dataIndex: 'userId',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
  },
  {
    title: '操作',
    key: 'action',
  },
]
</script>

<style scoped>
.adminQuestionPage {
  padding: 20px 0 60px 0;
}
</style>
