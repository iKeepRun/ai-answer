<template>
  <div class="custom-input-wrapper" @click="inputRef?.focus()">
    <!-- 标签组（绝对定位覆盖输入框） -->
    <a-tag-group
      class="tag-container"
      :style="{ display: tags.length ? 'flex' : 'none' }"
      @close="handleClose"
    >
      <a-tag v-for="tag in tags" :key="tag" closable>{{ tag }}</a-tag>
    </a-tag-group>

    <!-- 输入框（透明背景） -->
    <a-input
      ref="inputRef"
      v-model:value="inputValue"
      :style="{ opacity: 0, width: '100%' }"
      @blur="handleInputBlur"
      @keyup.enter="handleInputConfirm"
    />
  </div>
</template>
  
<script lang="ts" setup>
import { ref } from 'vue'

const tags = ref<string[]>([])
const inputValue = ref('')
const inputRef = ref()

const handleClose = (tag: any) => {
  tags.value = tags.value.filter((t) => t !== tag)
}

const handleInputBlur = () => {
  if (inputValue.value.trim()) {
    tags.value.push(inputValue.value.trim())
    inputValue.value = ''
  }
}

const handleInputConfirm = () => {
  if (inputValue.value.trim()) {
    tags.value.push(inputValue.value.trim())
    inputValue.value = ''
  }
}
</script>
  
  <style scoped>
.custom-input-wrapper {
  position: relative;
  display: inline-block;
  cursor: text;
}

.tag-container {
  position: absolute;
  top: 4px;
  left: 4px;
  right: 4px;
  bottom: 4px;
  overflow-y: auto;
  max-height: 100px;
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  padding: 4px;
}

.ant-tag {
  margin: 2px 0;
  background: #fff;
  border-color: #e0e0e0;
}

.ant-input {
  padding: 0; /* 移除默认内边距 */
}
</style>