<!-- message.vue -->
<template>
  <div class="echojoy-message">
    <p>{{ message }}</p>
  </div>
</template>
<script>
export default {
  name: "EchojoyMessage",
  data() {
    return {
      message: "",
      type: "",
      duration: 3000
    };
  },
  mounted() {
    setTimeout(() => {
      // 3s 后通过父级移除子元素的方式来移除该组件实例和 DOM 节点
      this.$destroy(true);
      this.$el.parentNode.removeChild(this.$el);
    }, 3000);

  },
};
</script>
<style>
.echojoy-message {
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  color: #fff;
  z-index: 9999;
  background: transparent;
}

</style>
