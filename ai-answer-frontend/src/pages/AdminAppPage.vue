<template>
  <div class="adminAppPage">
    <!-- 条件选择 -->
    <a-form layout="inline" :model="queryParams" style="margin-left: 110px" @finish="onSearch">
      <a-form-item>
        <a-input v-model:value="queryParams.appName" placeholder="输入应用名称">
          <template #prefix><UserOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
        </a-input>
      </a-form-item>
      <a-form-item>
        <a-input v-model:value="queryParams.appDesc" placeholder="输入应用描述">
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
        :style="{ width: '95%', margin: '0 auto' }"
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
          <template v-else-if="column.dataIndex === 'createTime'">
            {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
          </template>
          <template v-else-if="column.dataIndex === 'updateTime'">
            {{ dayjs(record.updateTime).format('YYYY-MM-DD HH:mm:ss') }}
          </template>
          <template v-else-if="column.dataIndex === 'reviewTime'">
            {{ record.reviewTime && dayjs(record.reviewTime).format('YYYY-MM-DD HH:mm:ss') }}
          </template>
          <template v-else-if="column.dataIndex === 'reviewStatus'">
            {{ REVIEW_STATUS_MAP[record.reviewStatus] }}
          </template>
          <template v-else-if="column.dataIndex === 'appType'">
            {{ APP_TYPE_MAP[record.appType] }}
          </template>
          <template v-else-if="column.dataIndex === 'scoringStrategy'">
            {{ SCORING_STRATEGY_MAP[record.scoringStrategy] }}
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button
                v-if="record.reviewStatus !== REVIEW_STATUS_ENUM.PASS"
                type="primary"
                style="background: #17d173"
                @click="
                  () => {
                    handleReviewClick(record, REVIEW_STATUS_ENUM.PASS, '')
                  }
                "
              >
                通过
              </a-button>

              <a-button
                v-if="record.reviewStatus !== REVIEW_STATUS_ENUM.REJECT"
                danger
                type="primary"
                style="background: #e16573"
                @click="
                  () => {
                    handleReviewClick(record, REVIEW_STATUS_ENUM.REJECT, '不符合商家要求')
                  }
                "
              >
                拒绝
              </a-button>

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
import { deleteAppUsingPost, listAppByPageUsingPost, reviewAppUsingPost } from '@/api/appController'
import { onMounted, reactive, ref, computed } from 'vue'
import zhCN from 'ant-design-vue/es/locale/zh_CN'
import message from 'ant-design-vue/es/message'
import dayjs from 'dayjs'
import {
  APP_TYPE_MAP,
  REVIEW_STATUS_ENUM,
  REVIEW_STATUS_MAP,
  SCORING_STRATEGY_MAP,
} from '@/constant/App'

const total = ref(0)
const data = ref<API.App[]>([])
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
  }
})

const getAppList = async () => {
  const res = await listAppByPageUsingPost(queryParams)
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
  getAppList()
}
const onSearch = () => {
  getAppList()
}

const handleReviewClick = async (record: API.App, reviewStatus: number, message: string) => {
  const res = await reviewAppUsingPost({
    appid: record.id,
    message,
    reviewStatus,
  })
  if (res.data.data) {
    getAppList()
  } else {
    message.error('审核失败')
  }
}

const handleDeleteClick = async (id: number) => {
  const res = await deleteAppUsingPost({ id })
  if (res.data.data) {
    getAppList()
  } else {
    message.error('删除失败')
  }
}

onMounted(() => {
  getAppList()
})

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
  },
  {
    title: '名称',
    dataIndex: 'appName',
  },
  {
    title: '描述',
    dataIndex: 'appDesc',
  },
  {
    title: '图标',
    dataIndex: 'appIcon',
  },
  {
    title: '应用类型',
    dataIndex: 'appType',
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
  },
  {
    title: '评分策略',
    dataIndex: 'scoringStrategy',
  },
  {
    title: '审核状态',
    dataIndex: 'reviewStatus',
  },
  {
    title: '审核信息',
    dataIndex: 'reviewMessage',
  },
  {
    title: '审核时间',
    dataIndex: 'reviewTime',
  },
  {
    title: '审核人',
    dataIndex: 'reviewerId',
  },
  {
    title: '创建人',
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
.adminAppPage {
  padding: 20px 0 60px 0;
}
</style>
