import Vue from "vue"

/**
 * 全局方法
 */

Vue.mixin({
    methods: {
        // 判断是否有 perm 该权限
		hasAuth(perm) {
			var authority = this.$store.state.menus.permList

            // 如果权限列表中有该权限这返回 true
			return authority.indexOf(perm) > -1
		}
	}
})