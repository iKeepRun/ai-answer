<template>
  <div class="registerPage">
    <h2 style="margin: 90px 0 25px 0">用户注册</h2>
    <a-form
      :model="formState"
      style="max-width: 480px; margin: 0 auto"
      labelAlign="left"
      :label-col="{ span: 4 }"
      @submit="handlerRegister"
      @finishFailed="onFinishFailed"
    >
      <a-form-item
        label="用户名"
        field="userAccount"
        :rules="[{ required: true, message: '请输入用户名', trigger: 'change' }]"
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
      <a-form-item
        label="确认密码"
        field="checkPassword"
        :rules="[{ required: true, message: '请输入确认密码' }]"
      >
        <a-input-password v-model:value="formState.checkPassword" />
      </a-form-item>

      <a-form-item name="remember">
        <div>已有账号？<a href="/user/login">去登录</a></div>
      </a-form-item>

      <a-form-item>
        <a-button type="primary" html-type="submit">注册</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { userRegisterUsingPost } from '@/api/userController'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
// import { loginUserStore } from '@/store/userStore'

const router = useRouter()
const formState = reactive<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
})

// const userStore = loginUserStore()
const onFinishFailed = (errorInfo: any) => {
  message.error('注册失败', errorInfo)
  console.log('Failed:', errorInfo)
}
const handlerRegister = async () => {
  const res = await userRegisterUsingPost(formState)
  if (res.data.code === 0 && res.data.data) {
    // userStore.setUser(res.data.data)
    // userStore.fetchUser()
    message.success('注册成功')
    router.push('/user/login')
  } else {
    message.error(res.data.message)
  }
}
</script>

<style scoped>
.registerPage {
}
</style>
