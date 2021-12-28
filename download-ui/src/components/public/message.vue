<!-- message.vue -->
<template>
  <div class="echojoy-message" :class="type ? `echojoy-message--${type}` : ''">
    <p>{{message}}</p>
  </div>
</template>
<script>
  export default {
    name: "EchojoyMessage",
    data() {
      return {
        message: "",
        type:"",
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
<style lang="scss" scoped>
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
    &--error p { background: rgba(255, 0, 0, 0.5); }
    &--success p { background: rgba(0, 255, 0, 0.5); }
    > p {
      padding: 12px 22px;
      font-size: 18px;
      border-radius: 4px;
      background: rgba(17, 17, 17, 0.7);
    }
}
</style>