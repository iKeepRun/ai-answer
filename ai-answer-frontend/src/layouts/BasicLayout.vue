<template>
  <a-layout :style="{ width: '100%', height: '100%' }">
    <a-layout-header :style="{ background: '#fff', padding: 0 }">
      <GlobalHeader />
    </a-layout-header>
    <a-layout-content>
      <router-view />
    </a-layout-content>
    <a-layout-footer>
      <a href="https://github.com/iKeepRun/ai-answer" target="_blank"> -- AI智能答题应用 -- </a>
    </a-layout-footer>
  </a-layout>
</template>

<script setup lang="ts">
import GlobalHeader from '@/components/GlobalHeader.vue'
</script>

<style scoped>
.ant-layout-content {
  background: linear-gradient(to right, #fefefe, #fff);
  padding: 20px 20px 48px 20px;
  /* box-sizing: border-box;
  max-width: 1200px;
  width: 100%;
  margin: 20px auto 28px; */
}
.ant-layout-footer {
  background: #efefef;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}
</style>
