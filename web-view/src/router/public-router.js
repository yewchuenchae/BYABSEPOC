const Guide = () => import('@/pages/guide') // 引导页
const Home = () => import('@/pages/home')   // 首页
export default [
    {
        path: '/guide',
        name: 'guide',
        components: {
            default: Guide
        }
    },
    {
        path: '/home',
        name: 'home',
        components: {
            default: Home
        }
    }
]