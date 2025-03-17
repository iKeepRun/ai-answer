import router from '@/router'
import { loginUserStore } from '@/store/userStore'
import ACCESS_ENUM from '@/access/accessEnum'
import checkAccess from '@/access/checkAccess'

// 进入页面前，进行权限校验
router.beforeEach(async (to, from, next) => {
  console.log('beforeeach to', to)
  // 获取当前登录用户
  const userStore = loginUserStore()
  let loginUser = userStore.loginUser
  console.log('beforeeach loginUser', loginUser)

  // 如果之前没有尝试获取过登录用户信息，才自动登录
  if (!loginUser || !loginUser.userRole) {
    // 加 await 是为了等待用户登录成功并获取到值后，再执行后续操作
    await userStore.fetchUser()
    loginUser = userStore.loginUser
  }

  // 当前页面需要的权限
  const needAccess = (to.meta?.access as string) ?? ACCESS_ENUM.GUEST
  console.log('needAccess', needAccess)
  console.log('loginUser', loginUser)
  // 要跳转的页面必须登录
  if (needAccess !== ACCESS_ENUM.GUEST) {
    // 如果没登录，跳转到登录页面
    if (!loginUser || !loginUser.userRole || loginUser.userRole === ACCESS_ENUM.GUEST) {
      next(`/user/login?redirect=${to.fullPath}`)
    }
    // 如果已经登录了，判断权限是否足够，如果不足，跳转到无权限页面
    if (!checkAccess(needAccess, loginUser)) {
      next('/noAuth')
      return
    }
  }
  next()
})
