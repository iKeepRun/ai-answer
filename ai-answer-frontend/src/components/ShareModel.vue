<template>
  <a-modal v-model:visible="visible" @cancel="closeModal" :footer="false">
    <template #title> 分享</template>
    <h4 style="margin-top: 0">复制分享链接</h4>
    <a-typography-paragraph copyable>
      {{ link }}
    </a-typography-paragraph>
    <h4>手机扫码查看</h4>
    <img :src="code" />
  </a-modal>
</template>
  
  <script setup lang="ts">
//@ts-expect-error '预料错误'
import QRCode from 'qrcode'
import { defineExpose, ref, withDefaults } from 'vue'

/**
 * 定义组件属性类型
 */
interface Props {
  title: string
  link: string
}

/**
 * 给组件指定初始值
 */
const props = withDefaults(defineProps<Props>(), {
  title: () => '分享',
  link: () => '',
})

// 是否可见
const visible = ref(false)
// 要展示的图片
const code = ref()

// 打开弹窗
const openModal = () => {
  visible.value = true
}

// 关闭弹窗
const closeModal = () => {
  visible.value = false
}

// 二维码生成
QRCode.toDataURL(props.link)
  .then((url: string) => {
    console.log('xfqfwf', url)
    code.value = url
  })
  .catch((err: any) => {
    console.error(err)
  })

defineExpose({ openModal })
</script>
  