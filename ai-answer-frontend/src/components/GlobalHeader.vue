<template>
  <a-row justify="space-between" align="middle">
    <a-col flex="auto">
      <a-menu
        :selectedKeys="selectedKeys"
        theme="light"
        mode="horizontal"
        :style="{ lineHeight: '64px', maxWidth: '900px' }"
        @click="menuClickHandler"
      >
        <a-menu-item key="0" :style="{ marginLeft: '100px' }">
          <div class="tab-bar">
            <img src="../assets/question.png" class="tab-bar-icon" />
            <div class="tab-bar-title">AI 答题平台</div>
          </div>
        </a-menu-item>

        <a-menu-item
          v-for="item in visibleRoutes"
          :key="item.path"
          :style="{ marginLeft: '50px', fontSize: '18px' }"
          >{{ item.name }}</a-menu-item
        >
        <template #overflowedIndicator><EllipsisOutlined style="margin-left: 8px" /></template>
      </a-menu>
    </a-col>
    <a-col flex="100px">
      <div v-if="userStore.loginUser.id">
        <a-dropdown>
          <a-space>
            <a> {{ userStore.loginUser.userName }} <DownOutlined /></a>
          </a-space>
          <template #overlay>
            <a-menu>
              <a-menu-item @click="handleLogout"> 退出登录 </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </div>
      <div v-else>
        <a-button type="primary" href="/user/login">登录</a-button>
      </div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { routes } from '@/router/routes.ts'
import { loginUserStore } from '@/store/userStore.ts'
import { userLogoutUsingPost } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import { DownOutlined, EllipsisOutlined } from '@ant-design/icons-vue'
import checkAccess from '@/access/checkAccess.ts'

console.log('fffff', routes)
const userStore = loginUserStore()
console.log('loginUserxxx', userStore.loginUser)

const router = useRouter()
const route = useRoute()
const selectedKeys = ref<string[]>([route.path])

router.afterEach((to) => {
  selectedKeys.value = [to.path]
})
// console.log('loginUser', userStore.loginUser)

const visibleRoutes = computed(() => {
  console.log('当前得路由数组', routes)
  return routes
    .filter((route) => {
      // const newRoute = { ...route }
      if (route.children) {
        route.children = route.children.filter((child) => {
          return (
            checkAccess(child.meta?.access as string, userStore.loginUser) &&
            child.meta?.isVisible !== false
          )
        })
      }
      return true
    })
    .flatMap((route) => {
      console.log('route', route)
      return route.children || []
    })
})

console.log('visibleRoutes', visibleRoutes.value)

const menuClickHandler = ({ key }: { key: string }) => {
  router.push(key)
}

const handleLogout = async () => {
  const res = await userLogoutUsingPost()
  if (res.data.data) {
    message.success('退出登录成功')
    await userStore.fetchUser()
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
