// message.js
import Vue from "vue"; // 引入 Vue 是因为要用到 Vue.extend() 这个方法
import message from "./message.vue"; // 引入刚才的 toast 组件



let messageConstructor = Vue.extend(message); // 这个在前面的前置知识内容里面有讲到
let instance;

const Message = function (options = {}) {
    instance = new messageConstructor({
        data:options
    }).$mount(); // 渲染组件
    document.body.appendChild(instance.$el); // 挂载到 body 下

    // this.$message.error() 其实就等价于 Message.error()
    ["success", "error"].forEach(type => {
        Message[type] = options => {
            options.type = type;
            return Message(options);
        };
    });
};
export default Message;