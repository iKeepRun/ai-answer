<template>
  <div>
    <a-card hoverable style="width: 300px">
      <template #cover>
        <img alt="example" :src="item.appIcon" style="height: 200px; object-fit: contain" />
      </template>
      <a-card-meta :title="item.appName" :description="item.appDesc"> </a-card-meta>

      <div style="margin-top: 10px; display: flex; align-items: center">
        <a-avatar :src="item.appIcon" :size="16" style="margin-right: 8px" />
        <!-- <img :src="item.appIcon" style="width: 16px; height: 16px; margin-right: 8px" /> -->
        {{ item.user?.userName ?? '' }}
        <ShareAltOutlined style="margin-left: 180px" />
      </div>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { defineProps, withDefaults } from 'vue'
import { ShareAltOutlined } from '@ant-design/icons-vue'

interface props {
  item: API.AppVO
}

withDefaults(defineProps<props>(), {
  item: () => ({}),
})
</script>

<style scoped>
</style>