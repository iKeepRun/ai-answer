<template>
  <div>
    <a-button type="primary" @click="showModal" style="margin: 0 0 10px 10%">新增</a-button>
    <a-modal
      v-model:open="open"
      title="新建评分结果"
      okText="确认"
      cancelText="取消"
      @ok="handleOk"
    >
      <a-form :labelCol="{ span: 5 }" labelAlign="left">
        <a-form-item label="应用id"><a-input v-model:value="appId" disabled></a-input></a-form-item>
        <a-form-item label="结果名称"
          ><a-input v-model:value="formState.resultName"></a-input
        ></a-form-item>
        <a-form-item label="结果描述"
          ><a-input v-model:value="formState.resultDesc"></a-input
        ></a-form-item>
        <a-form-item label="结果图标"
          ><a-input v-model:value="formState.resultPicture"></a-input
        ></a-form-item>
        <a-form-item label="结果集">
          <!-- <a-input v-model:value="formState.resultProp"> </a-input> -->
          <a-space>
            <template v-for="tag in state.tags" :key="tag">
              <a-tag closable @close="handleClose(tag)" style="height: 30px; line-height: 30px">
                {{ tag }}
              </a-tag>
            </template>
            <a-input
              v-if="state.inputVisible"
              ref="inputRef"
              v-model:value="state.inputValue"
              type="text"
              size="small"
              :style="{ width: '78px' }"
              @blur="handleInputConfirm"
              @keyup.enter="handleInputConfirm"
            />
            <a-tag
              v-else
              style="
                background: #fff;
                border-style: dashed;
                width: 80px;
                height: 30px;
                text-align: center;
                line-height: 30px;
              "
              @click="showInput"
            >
              <plus-outlined />
              点击添加
            </a-tag>
          </a-space>
        </a-form-item>
        <a-form-item label="结果得分范围"
          ><a-input v-model:value="formState.resultScoreRange"></a-input
        ></a-form-item>
      </a-form>
    </a-modal>
    <!-- 表格 -->
    <a-config-provider :locale="zhCN">
      <a-table
        :columns="columns"
        :data-source="scoreresults"
        :style="{ width: '90%', margin: '0 auto' }"
        :pagination="pagination"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'userRole'">
            <a-tag v-if="record.userRole === 'admin'" color="green">管理员</a-tag>
            <a-tag v-else color="blue">普通用户</a-tag>
          </template>

          <template v-else-if="column.dataIndex === 'createTime'">
            {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
          </template>
          <template v-else-if="column.dataIndex === 'updateTime'">
            {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
          </template>

          <template v-else-if="column.dataIndex === 'resultProp'">
            {{ JSON.stringify(record.resultProp) }}
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button
                type="primary"
                @click="
                  () => {
                    handleEditorClick(record)
                  }
                "
                >编辑</a-button
              >
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
import zhCN from 'ant-design-vue/es/locale/zh_CN'
import { onMounted, ref, reactive, computed, nextTick, watch } from 'vue'
import { useRoute } from 'vue-router'
import dayjs from 'dayjs'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import {
  listMyScoringResultVoByPageUsingPost,
  deleteScoringResultUsingPost,
  addScoringResultUsingPost,
  editScoringResultUsingPost,
} from '@/api/scoringResultController'

const open = ref<boolean>(false)
const route = useRoute()

const inputRef = ref()
const state = reactive({
  tags: [] as string[],
  inputVisible: false,
  inputValue: '',
})

const formState = ref<API.ScoringResultAddRequest | API.ScoringResultEditRequest>({
  appId: Number(route.params?.id),
})

// 监听 state.tags 的变化，并更新 formState.resultProp
watch(
  () => state.tags,
  (newTags) => {
    formState.value.resultProp = newTags // 手动同步
  },
  { immediate: true } // 立即执行一次，确保初始值正确
)

watch(
  () => formState.value.resultProp,
  (newTags) => {
    state.tags = newTags || []
  }
)

// watch([() => state.tags, () => formState.value.resultProp], ([newTags, newResultProp]) => {
//   formState.value.resultProp = newTags
//   state.tags = newResultProp || []
// })

const handleClose = (removedTag: string) => {
  const tags = state.tags.filter((tag) => tag !== removedTag)
  state.tags = tags
}

const showInput = () => {
  state.inputVisible = true
  nextTick(() => {
    inputRef.value.focus()
  })
}

const handleInputConfirm = () => {
  const inputValue = state.inputValue
  let tags = state.tags
  if (inputValue.length > 20) {
    message.error('标签不能超过20个字符')
    return
  }
  if (inputValue && tags.indexOf(inputValue) === -1) {
    tags = [...tags, inputValue]
  }

  Object.assign(state, {
    tags,
    inputVisible: false,
    inputValue: '',
  })
}

const showModal = () => {
  open.value = true
  formState.value = {}
}

const scoreresults = ref<API.ScoringResultVO[]>([])
const total = ref(0)
const queryParams = reactive<API.ScoringResultQueryRequest>({
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

const appId = Number(route.params?.id)
const loadMyData = async () => {
  if (appId === null || appId === undefined) {
    return
  }
  const res = await listMyScoringResultVoByPageUsingPost({
    ...queryParams,
    appId,
    sortField: 'createTime',
    sortOrder: 'desc',
  })

  if (res.data.data?.records?.length > 0) {
    scoreresults.value = res.data.data.records
    total.value = res.data.data.total
  } else {
    open.value = true
  }
}

// 新增评分结果
const handleOk = async () => {
  open.value = false
  if (appId === null || appId === undefined) {
    return
  }
  let res
  if (formState.value.id) {
    res = await editScoringResultUsingPost({ ...formState.value })
  } else {
    res = await addScoringResultUsingPost({ ...formState.value })
  }

  if (res.data.data) {
    loadMyData()
  } else {
    message.error('操作失败')
  }
}

const handleTableChange = (pagination: any) => {
  console.log(pagination)
  queryParams.current = pagination.current
  queryParams.pageSize = pagination.pageSize
  console.log('分页参数', queryParams)
  loadMyData()
}

const handleEditorClick = async (record: API.ScoringResultVO) => {
  console.log('编辑', record)
  open.value = true
  formState.value = record
  // const res = await editScoringResultUsingPost(record)
  // if (res.data.data) {
  //   loadMyData()
  // } else {
  //   message.error('编辑失败')
  // }
}

const handleDeleteClick = async (id: number) => {
  const res = await deleteScoringResultUsingPost({ id })
  if (res.data.data) {
    loadMyData()
  } else {
    message.error('删除失败')
  }
}

onMounted(() => {
  loadMyData()
})
const columns = [
  {
    title: 'id',
    dataIndex: 'id',
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
    title: '结果属性集合',
    dataIndex: 'resultProp',
  },
  {
    title: '结果得分范围',
    dataIndex: 'resultScoreRange',
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
