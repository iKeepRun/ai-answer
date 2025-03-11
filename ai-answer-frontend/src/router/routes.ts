import HomePage from '../pages/HomePage.vue'
import LoginPage from '../pages/LoginPage.vue'
import RegisterPage from '../pages/RegisterPage.vue'
import AboutPage from '../pages/AboutPage.vue'
import type { RouteRecordRaw } from 'vue-router'
import UserLayout from '@/layouts/UserLayout.vue'
import BasicLayout from '@/layouts/BasicLayout.vue'
import NoAuthPage from '@/pages/NoAuthPage.vue'
import ACCESS_ENUM from '@/access/accessEnum'

export const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: '基础',
    component: BasicLayout,
    children: [
      {
        path: '/',
        name: '首页',
        component: HomePage,
        meta: { isVisible: true },
      },
      {
        path: '/about',
        name: '关于',
        component: AboutPage,
        meta: { isVisible: true },
      },
    ],
  },
  {
    path: '/user',
    name: '用户',
    component: UserLayout,
    children: [
      {
        path: '/user/login',
        name: '用户登录',
        component: LoginPage,
        meta: { isVisible: false },
      },
      {
        path: '/user/register',
        name: '注册',
        component: RegisterPage,
        meta: {
          isVisible: false,
          // access: ACCESS_ENUM.ADMIN,
        },
      },
    ],
  },
  {
    path: '/noAuth',
    name: '404',
    component: NoAuthPage,
  },
]
