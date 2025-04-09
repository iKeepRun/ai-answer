# AI答题应用

> 基于 Vue 3 + Spring Boot + 小程序的智能答题应用平台

## 项目介绍

这是一个智能答题应用平台，支持 Web 端和小程序端。用户可以创建、分享和参与各类答题活动，系统会基于 AI 技术对答题结果进行智能分析和总结。

## 技术栈

### 前端 (ai-answer-frontend)
- Vue 3 + TypeScript
- Vite 构建工具
- ESLint + Prettier 代码规范
- OpenAPI 类型生成

### 后端 (ai-answer-backend)
- Spring Boot
- MySQL 数据库
- Maven 构建工具
- Docker 容器化部署

### 小程序 (mini-program)
- Taro + TypeScript
- React 框架
- 微信小程序

## 主要功能

- 答题应用创建与管理
- 智能题目生成
- 在线答题与评分
- AI 答题结果分析
- 答题数据统计与可视化
- 小程序端答题支持

## 项目结构

```
.
├── ai-answer-frontend/    # Web 前端项目
├── ai-answer-backend/     # Java 后端项目
└── mini-program/          # 小程序项目
```

## 开发环境

- Node.js >= 16
- JDK >= 17
- Maven >= 3.8
- MySQL >= 8.0

## 快速开始

1. 克隆项目
```bash
git clone [项目地址]
```

2. 启动后端服务
```bash
cd ai-answer-backend
mvn spring-boot:run
```

3. 启动前端项目
```bash
cd ai-answer-frontend
npm install
npm run dev
```

4. 启动小程序项目
```bash
cd mini-program
npm install
npm run dev:weapp
```

## 项目特点

- 🚀 前后端分离架构
- 💡 AI 智能分析
- 📱 多端支持（Web + 小程序）
- 🎨 现代化 UI 设计
- 🔒 安全可靠的数据处理
- 📊 丰富的数据统计功能

## 贡献指南

欢迎提交 Issue 和 Pull Request 来帮助改进项目。



