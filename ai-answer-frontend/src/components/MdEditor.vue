<template>
  <Editor :value="value" :plugins="plugins" @change="handleChange" />
</template>

<script setup lang="ts">
import gfm from '@bytemd/plugin-gfm'
import highlight from '@bytemd/plugin-highlight'
import { Editor } from '@bytemd/vue-next'
import { ref } from 'vue'

const value = ref('')

const plugins = [
  gfm(),
  highlight(),
  // Add more plugins here
]

const handleChange = (v: string) => {
  value.value = v
}
</script>

<style scoped></style>
