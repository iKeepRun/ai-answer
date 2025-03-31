<template>
  <div class="myAnswerPage">
    <!-- 条件选择 -->
    <a-form layout="inline" :model="queryParams" style="margin-left: 200px" @finish="onSearch">
      <a-form-item>
        <a-input v-model:value="queryParams.resultName" placeholder="请输入结果名称">
          <template #prefix><UserOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
        </a-input>
      </a-form-item>
      <a-form-item>
        <a-input v-model:value="queryParams.resultDesc" placeholder="请输入结果描述">
          <template #prefix><LockOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
        </a-input>
      </a-form-item>
      <a-form-item>
        <a-input v-model:value="queryParams.appId" placeholder="请输入应用id">
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
          <template v-if="column.dataIndex === 'userAvatar'">
            <a-avatar :src="record.userAvatar" />
          </template>
          <template v-if="column.dataIndex === 'userRole'">
            <a-tag v-if="record.userRole === 'admin'" color="green">管理员</a-tag>
            <a-tag v-else color="blue">普通用户</a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'appType'">
            {{ APP_TYPE_MAP[record.appType] }}
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
import {
  deleteUserAnswerUsingPost,
  listMyUserAnswerVoByPageUsingPost,
} from '@/api/userAnswerController'
import { onMounted, reactive, ref, computed } from 'vue'
import zhCN from 'ant-design-vue/es/locale/zh_CN'
import message from 'ant-design-vue/es/message'
import dayjs from 'dayjs'
import { APP_TYPE_MAP } from '@/constant/App'

const total = ref(0)
const data = ref<API.UserAnswerVO[]>([])
const queryParams = reactive<API.UserAnswerQueryRequest>({
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

const getUserAnswerList = async () => {
  const res = await listMyUserAnswerVoByPageUsingPost(queryParams)
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
  getUserAnswerList()
}
const onSearch = () => {
  getUserAnswerList()
}

const handleEditorClick = () => {
  console.log('编辑')
}

const handleDeleteClick = async (id: number) => {
  const res = await deleteUserAnswerUsingPost({ id })
  if (res.data.data) {
    getUserAnswerList()
  } else {
    message.error('删除失败')
  }
}

onMounted(() => {
  getUserAnswerList()
})

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
  },
  {
    title: '应用id',
    dataIndex: 'appId',
  },
  {
    title: '应用类型',
    dataIndex: 'appType',
  },
  {
    title: '评分策略',
    dataIndex: 'scoringStrategy',
  },
  {
    title: '用户答案',
    dataIndex: 'choices',
  },
  {
    title: '评分结果id',
    dataIndex: 'resultId',
  },
  {
    title: '结果名称',
    dataIndex: 'resultName',
  },
  {
    title: '结果描述',
    dataIndex: 'resultDesc',
  },
  {
    title: '结果图片',
    dataIndex: 'resultPicture',
  },
  {
    title: '得分',
    dataIndex: 'resultScore',
  },
  {
    title: '用户id',
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
.myAnswerPage {
  padding: 20px 0 60px 0;
}
</style>
