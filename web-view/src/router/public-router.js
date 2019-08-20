const Guide = () => import('@/pages/guide') // 引导页
const Home = () => import('@/pages/home')   // 首页
const Tabbar = () => import ('@/components/tabbar') // 底部导航栏
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
            default: Home,
            tabbar: Tabbar
        }
    }
]