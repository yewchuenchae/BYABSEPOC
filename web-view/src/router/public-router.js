const Guide = () => import('@/pages/guide') // 引导页
const Home = () => import('@/pages/home')   // 首页
const Selection = () => import('@/pages/selection') // 选择页

const Tabbar = () => import ('@/components/tabbar') // 底部导航栏
export default [
    {
        path: '/',
        redirect: '/selection' 
    },
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
        }
    }, 
    {
        path: '/selection',
        name: 'selection',
        components: {
            default: Selection
        }
    }
]