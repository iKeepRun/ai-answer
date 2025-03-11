<template>
  <a-row justify="space-between" align="middle" :wrap="false">
    <a-col flex="auto">
      <a-menu
        :selectedKeys="selectedKeys"
        theme="light"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
        @click="menuClickHandler"
      >
        <a-menu-item key="0" :style="{ marginLeft: '100px' }">
          <div class="tab-bar">
            <img src="../assets/question.png" class="tab-bar-icon" />
            <div class="tab-bar-title">AI 答题平台</div>
          </div>
        </a-menu-item>

        <a-menu-item
          v-for="item in visbleRoutes"
          :key="item.path"
          :style="{ marginLeft: '50px', fontSize: '18px' }"
          >{{ item.name }}</a-menu-item
        >
      </a-menu>
    </a-col>
    <a-col flex="100px">
      <div v-if="loginUser.id">
        <a-dropdown>
          <a-space>
            <a> {{ loginUser.userName }} <DownOutlined /></a>
          </a-space>
          <template #overlay>
            <a-menu>
              <a-menu-item @click="handleLogout"> 退出登录 </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </div>
      <div v-else>
        <a-button type="primary" shape="round" @click="$router.push('/user/login')">登录</a-button>
      </div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { routes } from '../router/routes.ts'
import { loginUserStore } from '@/store/userStore.ts'
import { userLogoutUsingPost } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import { DownOutlined } from '@ant-design/icons-vue'
import checkAccess from '@/access/checkAccess.ts'

const { loginUser } = loginUserStore()
console.log('loginUserxxx', loginUser)

// const parentRoutes = routes.filter((item) => item.name === '基础')
// const visbleRoutes = parentRoutes[0]?.children?.filter((item) => item.meta?.isVisible) || []

const visbleParentRoutes = routes.filter((parentItem) => {
  const visibleChildren = parentItem.children?.filter((childItem) => {
    if (!childItem.meta?.isVisible) {
      return false
    }
    //校验权限
    if (!checkAccess(childItem.meta?.access as string, loginUser)) {
      return false
    }
    return true
  })

  return visibleChildren && visibleChildren.length > 0
})

const visbleRoutes = visbleParentRoutes.flatMap((item) => {
  return item.children || []
})

const router = useRouter()
const route = useRoute()
const selectedKeys = ref<string[]>([route.path])

router.afterEach((to) => {
  selectedKeys.value = [to.path]
})

const menuClickHandler = ({ key }: { key: string }) => {
  router.push(key)
}

const handleLogout = async () => {
  const res = await userLogoutUsingPost()
  if (res.data.data) {
    message.success(res.data.message)
    // loginUserStore().$reset()
    router.push({
      path: '/user/login',
      replace: true,
    })
  } else {
    message.error(res.data.message)
  }
}
</script>

<style scoped>
.tab-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  .tab-bar-icon {
    width: 48px;
    height: 48px;
  }
  .tab-bar-title {
    font-size: 18px;
    margin-left: 20px;
  }
}
</style>
