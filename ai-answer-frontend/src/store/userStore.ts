import ACCESS_ENUM from '@/access/accessEnum'
import { getLoginUserUsingGet } from '@/api/userController'
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const loginUserStore = defineStore('loginUser', () => {
  const loginUser = ref<API.LoginUserVO>({
    userName: '未登录',
  })

  function setUser(newLoginUser: API.LoginUserVO) {
    loginUser.value = newLoginUser
  }

  async function fetchUser() {
    const res = await getLoginUserUsingGet()
    if (res.data.data) {
      loginUser.value = res.data.data
    } else {
      loginUser.value.userRole = ACCESS_ENUM.GUEST
    }
  }

  return { loginUser, setUser, fetchUser }
})
