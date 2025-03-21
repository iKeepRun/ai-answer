import HomePage from '../pages/HomePage.vue'
import LoginPage from '../pages/user/LoginPage.vue'
import RegisterPage from '../pages/user/RegisterPage.vue'
import AboutPage from '../pages/AboutPage.vue'
import type { RouteRecordRaw } from 'vue-router'
import UserLayout from '@/layouts/UserLayout.vue'
import BasicLayout from '@/layouts/BasicLayout.vue'
import AdminAppPage from '@/pages/admin/AdminAppPage.vue'
import AdminQuestionPage from '@/pages/admin/AdminQuestionPage.vue'
import AdminScoringResultPage from '@/pages/admin/AdminScoringResultPage.vue'
import AdminUserAnswerPage from '@/pages/admin/AdminUserAnswerPage.vue'
import ACCESS_ENUM from '@/access/accessEnum'
import AdminUserPage from '@/pages/admin/AdminUserPage.vue'
import AppDetailPage from '@/pages/app/AppDetailPage.vue'
import AddAppPage from '@/pages/add/AddAppPage.vue'
import AddQuestionPage from '@/pages/add/AddQuestionPage.vue'
import AddScoringResultPage from '@/pages/add/AddScoringResultPage.vue'
import AddTest from '@/pages/add/AddTest.vue'
import DoAnswerPage from '@/pages/answer/DoAnswerPage.vue'

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
        path: '/add/app',
        name: '创建应用',
        component: AddAppPage,
        meta: { isVisible: true },
      },
      {
        path: '/add/app/:id',
        props: true,
        name: '创建应用',
        component: AddAppPage,
        meta: { isVisible: false },
      },
      {
        path: '/add/question/:id',
        name: '创建题目',
        component: AddQuestionPage,
        props: true,
        meta: { isVisible: false },
      },
      {
        path: '/add/test',
        name: '创建测试',
        component: AddTest,
        meta: { isVisible: true },
      },
      {
        path: '/add/scoring_result/:id',
        name: '创建评分',
        component: AddScoringResultPage,
        props: true,
        meta: { isVisible: false },
      },
      {
        path: '/answer/do/:appId',
        name: '开始答题',
        component: DoAnswerPage,
        meta: { isVisible: false },
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
