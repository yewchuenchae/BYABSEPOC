const User = () => import('@/pages/user')   // 个人中心
export default [
    {
        path: '/user',
        name: 'user',
        components: {
            default: User
        }
    }
]