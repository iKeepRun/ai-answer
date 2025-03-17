import HomePage from '../pages/HomePage.vue'
import LoginPage from '../pages/LoginPage.vue'
import RegisterPage from '../pages/RegisterPage.vue'
import AboutPage from '../pages/AboutPage.vue'
import type { RouteRecordRaw } from 'vue-router'
import UserLayout from '@/layouts/UserLayout.vue'
import BasicLayout from '@/layouts/BasicLayout.vue'
import AdminAppPage from '@/pages/AdminAppPage.vue'
import AdminQuestionPage from '@/pages/AdminQuestionPage.vue'
import AdminScoringResultPage from '@/pages/AdminScoringResultPage.vue'
import AdminUserAnswerPage from '@/pages/AdminUserAnswerPage.vue'
import ACCESS_ENUM from '@/access/accessEnum'
import AdminUserPage from '@/pages/AdminUserPage.vue'
import AppDetailPage from '@/pages/AppDetailPage.vue'

export const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: '基础',
    component: BasicLayout,
    children: [
      {
        path: '/home',
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
      {
        path: '/admin/user',
        name: '用户管理',
        component: AdminUserPage,
        meta: { isVisible: true, access: ACCESS_ENUM.ADMIN },
      },
      {
        path: '/admin/app',
        name: '应用管理',
        component: AdminAppPage,
        meta: { isVisible: true, access: ACCESS_ENUM.ADMIN },
      },
      {
        path: '/admin/question',
        name: '题目管理',
        component: AdminQuestionPage,
        meta: { isVisible: true, access: ACCESS_ENUM.ADMIN },
      },
      {
        path: '/admin/scoring_result',
        name: '评分管理',
        component: AdminScoringResultPage,
        meta: { isVisible: true, access: ACCESS_ENUM.ADMIN },
      },
      {
        path: '/admin/user_answer',
        name: '回答管理',
        component: AdminUserAnswerPage,
        meta: { isVisible: true, access: ACCESS_ENUM.ADMIN },
      },
      {
        path: '/app/detail/:id',
        name: '应用详情',
        component: AppDetailPage,
        props: true,
        meta: { isVisible: false },
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
]
