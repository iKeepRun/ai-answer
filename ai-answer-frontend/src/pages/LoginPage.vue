<template>
  <div class="loginPage">
    <h2 style="margin: 90px 0 25px 0">用户登录</h2>
    <a-form
      :model="formState"
      style="max-width: 480px; margin: 0 auto"
      labelAlign="left"
      :label-col="{ span: 4 }"
      @submit="handlerLogin"
    >
      <a-form-item
        label="用户名"
        field="userAccount"
        :rules="[{ required: true, message: '请输入用户名' }]"
      >
        <a-input v-model:value="formState.userAccount" />
      </a-form-item>

      <a-form-item
        label="密码"
        field="userPassword"
        :rules="[{ required: true, message: '请输入密码' }]"
      >
        <a-input-password v-model:value="formState.userPassword" />
      </a-form-item>

      <a-form-item name="remember">
        <div>已有账号？<a href="/user/register">去注册</a></div>
      </a-form-item>

      <a-form-item>
        <a-button type="primary" html-type="submit">登录</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { userLoginUsingPost } from '@/api/userController'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { loginUserStore } from '@/store/userStore'

const router = useRouter()
const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})

const userStore = loginUserStore()

const handlerLogin = async () => {
  const res = await userLoginUsingPost(formState)
  if (res.data.code === 0 && res.data.data) {
    userStore.setUser(res.data.data)
    // userStore.fetchUser()
    router.push('/')
  } else {
    message.error(res.data.message)
  }
}
</script>

<style scoped>
.loginPage {
}
</style>
